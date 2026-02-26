package com.ruoyi.business.archive.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.ruoyi.business.archive.config.ArchiveConfig;
import com.ruoyi.business.archive.constants.CadreArchiveFileConstant;
import com.ruoyi.business.archive.controller.domain.CadreArchiveImportResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.enums.ArchiveItemTypeEnum;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveItemService;
import com.ruoyi.business.common.enums.GenderEnum;
import com.ruoyi.business.common.enums.YNEnum;
import com.ruoyi.business.framework.web.component.concurrent.TraceThreadPoolExecutor;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.exception.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Snow
 */
@Slf4j
@Service
public class CadreArchiveFileParseUtil {

    public static final Long CADRE_DEFAULT_DEPT_ID = 100L;
    public static final Long CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID = 0L;

    private static final Lock LOCK = new ReentrantLock();
    private static final ExecutorService EXECUTOR_SERVICE = new TraceThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000), new NamedThreadFactory("archive-import", true));
    private static final CompletionService<String> COMPLETION_SERVICE = new ExecutorCompletionService<>(EXECUTOR_SERVICE);

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    @Resource
    private CadreArchiveItemMapper cadreArchiveItemMapper;

    @Resource
    private CadreArchiveItemService cadreArchiveItemService;

    public void parseAsync(List<MultipartFile> fileList, Map<String, CadreArchiveItem> commonItemMap, Map<String, String> parseCadreIdNumberMap) {
        try {
            boolean locked = LOCK.tryLock(6, TimeUnit.SECONDS);
            if (!locked) {
                throw new ServiceException("存在正在导入的档案任务, 请稍候再试！");
            }
        } catch (InterruptedException e) {
            log.error("【 档案导入 】锁竞争异常: {}", e.getMessage(), e);
            throw new ServiceException("存在正在导入的档案任务, 请稍候再试！");
        }

        try {
            CadreArchiveFileParseUtil parseService = (CadreArchiveFileParseUtil) AopContext.currentProxy();
            fileList.forEach(file -> {
                COMPLETION_SERVICE.submit(() -> parseService.parse(file, commonItemMap, parseCadreIdNumberMap));
            });
        } catch (Exception e) {
            log.error("【 档案导入 】提交任务异常: {}", e.getMessage(), e);
        }
    }

    public CadreArchiveImportResp waitCompletion(CadreArchiveImportResp resp) {
        try {
            Integer totalCount = resp.getTotalCount();

            Integer failCount = 0;
            Integer successCount = 0;
            List<String> failFileList = new ArrayList<>(totalCount);
            for (int i = 0; i < totalCount; i++) {
                try {
                    String result = COMPLETION_SERVICE.take().get();
                    if (StrUtil.isBlank(result)) {
                        successCount++;
                    } else {
                        failCount++;
                        failFileList.add(result);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("【 档案导入 】等待任务完成时发生异常: {}", e.getMessage(), e);
                }
            }

            resp.setFailCount(failCount);
            resp.setSuccessCount(successCount);
            resp.setFailFileNameList(failFileList);

            return resp;
        } finally {
            LOCK.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String parse(MultipartFile file, Map<String, CadreArchiveItem> commonItemMap, Map<String, String> parseCadreIdNumberMap) {
        String fileName = StrUtil.subBefore(file.getOriginalFilename(), StrUtil.DOT, true);

        try (InputStream inputStream = file.getInputStream()) {
            Boolean success = parse(inputStream, fileName, commonItemMap, parseCadreIdNumberMap);
            if (success) {
                return StrUtil.EMPTY;
            } else {
                return file.getOriginalFilename();
            }
        } catch (Exception e) {
            log.error("【 档案导入 】文件: {}. 档案导入失败: {}", fileName, e.getMessage(), e);
            return file.getOriginalFilename();
        }
    }

    private Boolean parse(InputStream inputStream, String fileName, Map<String, CadreArchiveItem> commonItemMap, Map<String, String> parseCadreIdNumberMap) {
        StopWatch stopWatch = StopWatch.create(IdUtil.fastSimpleUUID());
        stopWatch.start();

        String unzipDirName = fileName + "-" + LocalDateTime.now().format(DatePattern.PURE_DATETIME_MS_FORMATTER);
        log.debug("【 档案导入 】文件: {}. 开始处理.", unzipDirName);

        Path unzipPath = null;
        try {
            // 校验文件类型
            // String fileType = FileTypeUtil.getType(inputStream);
            // if (!fileType.equals("zip")) {
            //     log.warn("【 档案导入 】文件: {}. 文件类型不正确：{}", uploadFileName, fileType);
            //     return;
            // }

            // 解压
            unzipPath = PathUtil.mkdir(Path.of(ArchiveConfig.getUnzipDir() + FileUtil.FILE_SEPARATOR + unzipDirName));
            ZipUtil.unzip(inputStream, unzipPath.toFile(), Charset.defaultCharset());
            log.debug("【 档案导入 】文件: {}. 解压完成, 解压地址: {}", unzipDirName, unzipPath);

            // 执行解析
            List<String> oldArchiveFileUriList = doParse(unzipPath.toFile(), commonItemMap, parseCadreIdNumberMap);

            // 删除旧档案
            oldArchiveFileUriList.stream().map(RuoYiConfig::fileUrlToPath).forEach(item -> {
                try {
                    FileUtil.del(item);
                } catch (Exception e) {
                    log.error("【 档案导入 】文件: {}. 旧档案: {}, 删除异常: {}", unzipDirName, item, e.getMessage(), e);
                }
            });

            return true;
        } catch (Exception e) {
            log.error("【 档案导入 】文件: {}. 档案导入失败: {}", unzipDirName, e.getMessage(), e);
            return false;
        } finally {
            log.debug("【 档案导入 】文件: {}. 处理完成, 耗时: {}", unzipDirName, stopWatch.getTotalTimeMillis());
            if (unzipPath != null) {
                try {
                    FileUtil.del(unzipPath.toFile());
                } catch (Exception e) {
                    log.error("【 档案导入 】文件: {}. 解压文件夹删除失败: {}", unzipDirName, e.getMessage(), e);
                }
            }
        }
    }

    private List<String> doParse(File fileDir, Map<String, CadreArchiveItem> commonItemMap, Map<String, String> parseCadreIdNumberMap) {
        // 查找 xml 文件
        Optional<File> xmlFIleOpt = FileUtil.loopFiles(fileDir, item -> item.isFile() && item.getName().endsWith(".xml")).stream().findFirst();
        if (xmlFIleOpt.isEmpty()) {
            log.warn("【 档案导入 】文件: {}. 未找到 xml 文件", fileDir.getName());
            return Collections.emptyList();
        }

        // 读取 XML
        File xmlFile = xmlFIleOpt.get();
        Document document = XmlUtil.readXML(xmlFile);
        Element rootElement = document.getDocumentElement(); // 获取根元素
        Element personInfoElement = XmlUtil.getElement(rootElement, CadreArchiveFileConstant.USER_BASIC_INFO); // 人员基本信息
        Element directoryInfoElement = XmlUtil.getElement(rootElement, CadreArchiveFileConstant.DIRECTORY_INFO); // 档案信息

        // 校验身份证号
        String cadreName = XmlUtil.getElement(personInfoElement, CadreArchiveFileConstant.USER_NAME).getTextContent();
        String idNumber = XmlUtil.getElement(personInfoElement, CadreArchiveFileConstant.USER_ID_NUMBER).getTextContent();
        if (StrUtil.isBlank(idNumber)) {
            log.warn("【 档案导入 】文件: {}. 身份证号为空", fileDir.getName());
            return Collections.emptyList();
        }

        String preValue = parseCadreIdNumberMap.putIfAbsent(idNumber, "importing");
        if (StrUtil.isNotBlank(preValue)) {
            log.warn("【 档案导入 】文件: {}. 身份证号重复导入：{}", fileDir.getName(), idNumber);
            return Collections.emptyList();
        }

        // 文件夹重命名
        File sourceDir = xmlFile.getParentFile();
        String archiveStorageDirName = StrUtil.format("{}-{}-{}", cadreName, idNumber, System.currentTimeMillis());
        sourceDir = FileUtil.rename(sourceDir, archiveStorageDirName, true);

        // 文件移动
        File archiveStoragePath = new File(ArchiveConfig.getStorageDir(), archiveStorageDirName);
        log.debug("【 档案导入 】文件: {}. 文件转存开始, 源地址: {}, 目标地址: {}", fileDir.getName(), sourceDir.getAbsolutePath(), archiveStoragePath.getAbsolutePath());
        FileUtil.move(sourceDir, new File(ArchiveConfig.getStorageDir()), true);
        log.debug("【 档案导入 】文件: {}. 文件转存完成.", fileDir.getName());

        // 查询旧档案
        List<CadreArchive> oldCadreArchiveList = cadreArchiveMapper.listByIdNumber(idNumber);
        if (CollUtil.isNotEmpty(oldCadreArchiveList)) {
            List<Long> oldCadreArchiveIdList = oldCadreArchiveList.stream().map(CadreArchive::getId).toList();
            cadreArchiveMapper.deleteByIds(oldCadreArchiveIdList);
            cadreArchiveItemMapper.deleteByArchiveId(oldCadreArchiveIdList);
        }

        // 处理干部信息
        CadreArchive cadreArchive = processCadresInfo(personInfoElement, idNumber, cadreName);
        cadreArchive.setArchiveFilePath(RuoYiConfig.filePathToUrl(archiveStoragePath.getAbsolutePath()));
        cadreArchiveMapper.insert(cadreArchive);

        // 档案条目信息
        List<Element> itemElementList = CollUtil.emptyIfNull(XmlUtil.getElements(directoryInfoElement, CadreArchiveFileConstant.DIRECTORY_RECORD));
        List<CadreArchiveItem> cadreArchiveItemList = itemElementList.stream()
                .map(item -> buildCadreArchiveItem(cadreArchive.getId(), item, commonItemMap))
                .flatMap(Collection::stream)
                .toList();
        cadreArchiveItemService.saveBatch(cadreArchiveItemList);

        // 返回旧档案文件地址
        return oldCadreArchiveList.stream().map(CadreArchive::getArchiveFilePath).toList();
    }

    private CadreArchive processCadresInfo(Element personInfo, String idNumber, String userName) {
        String sex = XmlUtil.getElement(personInfo, CadreArchiveFileConstant.USER_SEX).getTextContent();
        String ethnic = XmlUtil.getElement(personInfo, CadreArchiveFileConstant.USER_ETHNIC).getTextContent();
        String birthDate = XmlUtil.getElement(personInfo, CadreArchiveFileConstant.USER_BIRTHDAY).getTextContent();

        CadreArchive cadreArchive = new CadreArchive();
        cadreArchive.setCadreName(userName);
        cadreArchive.setCadreNamePy(PinyinUtil.getFirstLetter(userName, StrUtil.EMPTY));
        cadreArchive.setIdNumber(idNumber);
        cadreArchive.setDeptId(cadreArchive.getId() == null ? CADRE_DEFAULT_DEPT_ID : cadreArchive.getDeptId());
        cadreArchive.setBirthday(cadreBirthdayFormat(birthDate));
        cadreArchive.setGender("男".equals(sex) ? GenderEnum.MAN.getCode() : "2");
        cadreArchive.setEthnic(ethnic);
        cadreArchive.setArchiveStockStatus(YNEnum.YES.getCodeNumStr());

        return cadreArchive;
    }

    private List<CadreArchiveItem> buildCadreArchiveItem(Long archiveId, Element recordElement, Map<String, CadreArchiveItem> commonItemMap) {
        String typeNo = XmlUtil.getElement(recordElement, CadreArchiveFileConstant.TYPE_NO).getTextContent();
        CadreArchiveItem commonItem = commonItemMap.get(typeNo);
        if (commonItem == null) {
            log.warn("未找到公共档案目录项，类号：{}", typeNo);
            return Collections.emptyList();
        }

        String serialNo = XmlUtil.getElement(recordElement, CadreArchiveFileConstant.SERIAL_NO).getTextContent();
        String materialName = XmlUtil.getElement(recordElement, CadreArchiveFileConstant.MATERIAL_NAME).getTextContent();
        String materialTime = XmlUtil.getElement(recordElement, CadreArchiveFileConstant.MATERIAL_FORMATION_TIME).getTextContent();
        String pageCountStr = XmlUtil.getElement(recordElement, CadreArchiveFileConstant.PAGE_COUNT).getTextContent();
        Integer pageCount = StrUtil.isBlank(pageCountStr) ? 0 : Integer.parseInt(pageCountStr);

        // 构建 item
        CadreArchiveItem archiveItem = new CadreArchiveItem();
        archiveItem.setId(IdUtil.getSnowflakeNextId());
        archiveItem.setArchiveId(archiveId);
        archiveItem.setItemType(typeNo);
        archiveItem.setItemName(materialName);
        archiveItem.setSort(StrUtil.isBlank(serialNo) ? 0 : Integer.parseInt(serialNo));
        archiveItem.setMaterialDate(StrUtil.nullToEmpty(materialTime));
        archiveItem.setMaterialPageCount(pageCount);
        archiveItem.setParentId(commonItem.getId());
        archiveItem.setAncestors(commonItem.getAncestors() + "," + commonItem.getId());

        // 获取图片路径
        List<String> originalImageList = getImgPath(recordElement, CadreArchiveFileConstant.ORIGINAL_IMAGE_DATA, CadreArchiveFileConstant.ORIGINAL_IMAGE_DATA_TEXT);
        List<String> optimizeImageList = getImgPath(recordElement, CadreArchiveFileConstant.OPTIMIZE_IMAGE_DATA, CadreArchiveFileConstant.OPTIMIZE_IMAGE_DATA_TEXT);
        List<CadreArchiveItem> itemList = new ArrayList<>(optimizeImageList.size() + originalImageList.size());
        buildArchiveImageItem(archiveId, archiveItem, itemList, originalImageList, ArchiveItemTypeEnum.ORIGINAL_IMAGE);
        buildArchiveImageItem(archiveId, archiveItem, itemList, optimizeImageList, ArchiveItemTypeEnum.OPTIMIZE_IMAGE);
        itemList.add(archiveItem);
        return itemList;
    }

    private void buildArchiveImageItem(Long archiveId, CadreArchiveItem archiveItem, List<CadreArchiveItem> itemList, List<String> imageList, ArchiveItemTypeEnum itemType) {
        for (int i = 0; i < imageList.size(); i++) {
            String imagePath = imageList.get(i);
            CadreArchiveItem imageItem = new CadreArchiveItem();
            imageItem.setArchiveId(archiveId);
            imageItem.setId(IdUtil.getSnowflakeNextId());
            imageItem.setParentId(archiveItem.getId());
            imageItem.setAncestors(archiveItem.getAncestors() + "," + archiveItem.getId());
            imageItem.setItemType(itemType.getCode());
            imageItem.setItemName(imagePath);
            imageItem.setSort(i + 1);
            imageItem.setMaterialDate(StrUtil.EMPTY);
            imageItem.setMaterialPageCount(1);
            itemList.add(imageItem);
        }
    }

    private List<String> getImgPath(Element item, String imageDataElementTag, String imageNameElementTag) {
        List<String> imagePathList = new ArrayList<>();

        // 格式：优化图像数据 -> 优化图像数据_Text -> data
        Element imgRootElement = XmlUtil.getElement(item, imageDataElementTag);
        if (imgRootElement != null) {
            List<Element> imgElementList = XmlUtil.getElements(imgRootElement, imageNameElementTag);
            CollUtil.emptyIfNull(imgElementList).forEach(imgElement -> {
                NodeList nodeList = imgElement.getElementsByTagName(CadreArchiveFileConstant.IMAGE_PATH_TAG);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String textContent = nodeList.item(i).getTextContent();
                    imagePathList.add(StrUtil.replaceFirst(textContent, StrUtil.BACKSLASH, StrUtil.EMPTY));
                }
            });

            return imagePathList;
        }

        // 格式：优化图像数据_Text
        NodeList nodeList = item.getElementsByTagName(imageNameElementTag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            String textContent = nodeList.item(i).getTextContent();
            imagePathList.add(StrUtil.replaceFirst(textContent, StrUtil.BACKSLASH, StrUtil.EMPTY));
        }
        return imagePathList;
    }

    private String cadreBirthdayFormat(String birthday) {
        if (StrUtil.isBlank(birthday)) {
            return StrUtil.EMPTY;
        }

        birthday = birthday.replaceAll("[^0-9]", "");

        if (birthday.length() == 6) {
            String year = birthday.substring(0, 4);
            String month = birthday.substring(4, 6);
            return year + "." + month;
        }
        if (birthday.length() == 8) {
            String year = birthday.substring(0, 4);
            String month = birthday.substring(4, 6);
            String day = birthday.substring(6, 8);
            return year + "." + month + "." + day;
        }
        return birthday;
    }

}
