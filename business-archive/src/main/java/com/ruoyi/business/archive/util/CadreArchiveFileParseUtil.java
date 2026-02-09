package com.ruoyi.business.archive.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.ruoyi.business.archive.config.ArchiveConfig;
import com.ruoyi.business.archive.constants.CadreArchiveFileConstant;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.enums.ArchiveItemTypeEnum;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveItemService;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.common.enums.GenderEnum;
import com.ruoyi.business.common.enums.YNEnum;
import com.ruoyi.common.config.RuoYiConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Snow
 */
@Slf4j
@Service
public class CadreArchiveFileParseUtil {

    private static final String UNZIP_DIR_NAME = "archive_import_unzip";
    private static final Map<String, String> IMPORTING_MAP = new ConcurrentHashMap<>();

    public static final Long CADRE_DEFAULT_DEPT_ID = 100L;
    public static final Long CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID = 0L;

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    @Resource
    private CadreArchiveService cadreArchiveService;

    @Resource
    private CadreArchiveItemService cadreArchiveItemService;

    @Resource
    private CadreArchiveItemMapper cadreArchiveItemMapper;

    @Transactional(rollbackFor = Exception.class)
    public void parse(MultipartFile file, Map<String, CadreArchiveItem> commonItemMap) {
        String uploadFileName = file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream();) {
            parse(uploadFileName, inputStream, commonItemMap);
        } catch (Exception e) {
            log.error("【 档案导入 】文件: {}. 档案导入失败: {}", uploadFileName, e.getMessage(), e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void parse(String uploadFileName, InputStream inputStream, Map<String, CadreArchiveItem> commonItemMap) {
        String unzipBaseDir = RuoYiConfig.getUploadPath() + FileUtil.FILE_SEPARATOR + UNZIP_DIR_NAME;

        // 校验文件类型
        // String fileType = FileTypeUtil.getType(inputStream);
        // if (!fileType.equals("zip")) {
        //     log.warn("【 档案导入 】文件: {}. 文件类型不正确：{}", uploadFileName, fileType);
        //     return;
        // }

        // 解压
        File unzipDir = new File(unzipBaseDir + FileUtil.FILE_SEPARATOR + IdUtil.fastSimpleUUID());
        FileUtil.mkdir(unzipDir);
        ZipUtil.unzip(inputStream, unzipDir, Charset.defaultCharset());

        // 查找 xml 文件
        Optional<File> xmlFIleOpt = FileUtil.loopFiles(unzipDir, item -> item.isFile() && item.getName().endsWith(".xml")).stream().findFirst();
        if (xmlFIleOpt.isEmpty()) {
            log.warn("【 档案导入 】文件: {}. 未找到 xml 文件", uploadFileName);
            return;
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
            log.warn("【 档案导入 】文件: {}. 身份证号为空", uploadFileName);
            return;
        }

        String preValue = IMPORTING_MAP.putIfAbsent(idNumber, "importing");
        if (StrUtil.isNotBlank(preValue)) {
            log.warn("【 档案导入 】文件: {}. 身份证号重复导入：{}", uploadFileName, idNumber);
            return;
        }

        // 存储目录
        // File archiveFileStorageDir = new File(RuoYiConfig.getUploadPath() + FileUtil.FILE_SEPARATOR + ARCHIVE_STORAGE_DIR_NAME);
        // FileUtil.mkdir(archiveFileStorageDir);

        File sourceDir = xmlFile.getParentFile();
        String archiveFileStorageDirName = StrUtil.format("{}-{}-{}", cadreName, idNumber, System.currentTimeMillis());
        File archiveFileStoragePath = new File(ArchiveConfig.getStorageDir(), archiveFileStorageDirName);
        try {
            // 检查源目录权限
            if (!sourceDir.canRead()) {
                throw new RuntimeException("源目录无读取权限：" + sourceDir.getAbsolutePath());
            }

            // 检查目标目录权限
            File archiveFileStorageDir = new File(ArchiveConfig.getStorageDir());
            if (!archiveFileStorageDir.canWrite()) {
                throw new RuntimeException("目标目录无写入权限：" + archiveFileStorageDir.getAbsolutePath());
            }

            // 移动目录
            sourceDir = FileUtil.rename(sourceDir, archiveFileStorageDirName, true);
            FileUtil.move(sourceDir, archiveFileStorageDir, true);
        } catch (Exception e) {
            log.error("【 档案导入 】文件: {}. 文件转存失败: {}", sourceDir, e.getMessage(), e);
            return;
        }

        // 处理干部信息
        CadreArchive cadreArchive = processCadresInfo(personInfoElement, idNumber, cadreName);
        cadreArchive.setArchiveFilePath(RuoYiConfig.filePathToUrl(archiveFileStoragePath.getAbsolutePath()));
        cadreArchiveMapper.insert(cadreArchive);

        // 档案条目信息
        List<Element> itemElementList = CollUtil.emptyIfNull(XmlUtil.getElements(directoryInfoElement, CadreArchiveFileConstant.DIRECTORY_RECORD));
        List<CadreArchiveItem> cadreArchiveItemList = itemElementList.stream()
                .map(item -> buildCadreArchiveItem(cadreArchive.getId(), item, commonItemMap))
                .flatMap(Collection::stream)
                .toList();
        cadreArchiveItemService.saveBatch(cadreArchiveItemList);
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

    public String cadreBirthdayFormat(String birthday) {
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
