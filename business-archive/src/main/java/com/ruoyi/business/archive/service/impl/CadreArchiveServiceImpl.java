package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.controller.domain.CadreArchiveImportResp;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveDeptMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.archive.util.CadreArchiveFileParseUtil;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.common.mapper.SysMapper;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.business.common.util.FileUploadUtil;
import com.ruoyi.business.common.web.exception.BizException;
import com.ruoyi.business.common.web.exception.code.BizExceptionCode;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * 干部档案 Service 实现类
 *
 * @author Snow
 */
@Slf4j
@Service
public class CadreArchiveServiceImpl extends ServiceImpl<CadreArchiveMapper, CadreArchive> implements CadreArchiveService {

    @Resource
    private SysMapper sysMapper;

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    @Resource
    private CadreArchiveItemMapper cadreArchiveItemMapper;

    @Resource
    private CadreArchiveDeptMapper cadreArchiveDeptMapper;

    @Resource
    private CadreArchiveFileParseUtil cadreArchiveFileParseUtil;

    /**
     * 列表
     *
     * @param req req
     * @return {@link PageResp }<{@link CadreArchiveResp }>
     */
    @Override
    public PageResp<CadreArchiveResp> page(CadreArchivePageReq req) {
        Page<CadreArchive> page = cadreArchiveMapper.page(req.mybatisPage(), req);
        PageResp<CadreArchiveResp> pageResp = PageResp.of(page.getTotal(), BeanUtil.mapList(page.getRecords(), CadreArchiveResp.class));
        if (CollUtil.isEmpty(pageResp.getRecords())) {
            return pageResp;
        }

        List<Long> deptIdList = pageResp.getRecords().stream().map(CadreArchiveResp::getDeptId).distinct().toList();
        if (CollUtil.isEmpty(deptIdList)) {
            return pageResp;
        }

        List<CadreArchiveDept> deptList = cadreArchiveDeptMapper.selectByIds(deptIdList);
        Map<Long, CadreArchiveDept> deptMap = deptList.stream().collect(Collectors.toMap(CadreArchiveDept::getId, Function.identity()));
        pageResp.getRecords().forEach(resp -> {
            if (resp.getDeptId() == null) {
                return;
            }

            CadreArchiveDept deptDTO = deptMap.get(resp.getDeptId());
            resp.setDeptInfo(deptDTO);
        });

        return pageResp;
    }

    /**
     * 删除
     *
     * @param req req
     */
    @Override
    public void delete(IdsReq req) {
        // 查询旧档案
        List<CadreArchive> cadreArchiveList = cadreArchiveMapper.selectByIds(req.getIds());

        // 删除数据库
        CadreArchiveService cadreArchiveService = (CadreArchiveService) AopContext.currentProxy();
        cadreArchiveService.doDelete(req);

        // 删除档案文件
        cadreArchiveList.stream().map(CadreArchive::getArchiveFilePath).map(FileUploadUtil::fileUrlToPath).forEach(item -> {
            try {
                FileUtil.del(item);
            } catch (Exception e) {
                log.error("【 档案删除 】旧档案: {}, 删除异常: {}", item, e.getMessage(), e);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void doDelete(IdsReq req) {
        cadreArchiveMapper.deleteByIds(req.getIds());
        cadreArchiveItemMapper.deleteByArchiveId(req.ids());
    }

    /**
     * 导入档案
     *
     * @param fileList      文件列表
     * @param updateSupport 更新支持
     */
    @Override
    public CadreArchiveImportResp importArchive(List<MultipartFile> fileList, Boolean updateSupport) {
        List<CadreArchiveItem> itemList = cadreArchiveItemMapper.listByArchiveId(Collections.singletonList(CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID), "");
        Map<String, CadreArchiveItem> itemMap = itemList.stream().collect(Collectors.toMap(CadreArchiveItem::getItemType, Function.identity(), (v1, v2) -> v1));

        Map<String, String> parseCadreIdNumberMap = new ConcurrentHashMap<>(fileList.size());
        cadreArchiveFileParseUtil.parseAsync(fileList, itemMap, parseCadreIdNumberMap);
        return cadreArchiveFileParseUtil.waitCompletion(new CadreArchiveImportResp().setTotalCount(fileList.size()));
    }

    @Override
    public CadreArchiveImportResp importByDeveloper(String fileDir) {
        boolean fileDirExist = FileUtil.exist(fileDir);
        if (!fileDirExist) {
            throw new BizException(BizExceptionCode.MESSAGE, "目录不存在");
        }

        File[] dirArray = new File(fileDir).listFiles();
        if (ArrayUtil.isEmpty(dirArray)) {
            throw new BizException(BizExceptionCode.MESSAGE, "目录下没有文件");
        }
        List<File> dirList = Arrays.stream(dirArray).filter(File::isDirectory).toList();
        if (CollUtil.isEmpty(dirList)) {
            throw new BizException(BizExceptionCode.MESSAGE, "目录下没有文件");
        }

        List<CadreArchiveItem> itemList = cadreArchiveItemMapper.listByArchiveId(Collections.singletonList(CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID), "");
        Map<String, CadreArchiveItem> itemMap = itemList.stream().collect(Collectors.toMap(CadreArchiveItem::getItemType, Function.identity(), (v1, v2) -> v1));

        Map<String, String> parseCadreIdNumberMap = new ConcurrentHashMap<>(dirList.size());
        cadreArchiveFileParseUtil.parseAsync(dirList, itemMap, parseCadreIdNumberMap);
        return cadreArchiveFileParseUtil.waitCompletion(new CadreArchiveImportResp().setTotalCount(dirList.size()));
    }

    /**
     * 导出
     *
     * @param req      请求参数
     * @param response 响应对象
     */
    @Override
    public void export(IdsReq req, HttpServletResponse response) {
        List<Long> ids = req.ids();

        // 限制最大导出数量
        if (ids.size() > 10) {
            throw new BizException(BizExceptionCode.MESSAGE, "批量导出最多支持10条记录");
        }

        // 查询档案记录
        List<CadreArchive> archiveList = cadreArchiveMapper.selectByIds(ids);
        if (CollUtil.isEmpty(archiveList)) {
            throw new BizException(BizExceptionCode.MESSAGE, "未找到要导出的档案记录");
        }

        // 临时目录
        String tempDir = RuoYiConfig.getDownloadPath() + "temp_" + System.currentTimeMillis();
        File tempDirFile = new File(tempDir);
        if (!tempDirFile.exists()) {
            boolean ignore = tempDirFile.mkdirs();
        }

        try {
            List<File> zipFiles = new ArrayList<>();

            // 压缩每个档案文件夹
            for (CadreArchive archive : archiveList) {
                String archiveFilePath = archive.getArchiveFilePath();
                if (archiveFilePath == null || archiveFilePath.isEmpty()) {
                    log.warn("档案ID: {} 的文件路径为空,跳过", archive.getId());
                    continue;
                }

                // 转换为磁盘路径
                String diskPath = FileUploadUtil.fileUrlToPath(archiveFilePath);
                File archiveDir = new File(diskPath);

                if (!archiveDir.exists() || !archiveDir.isDirectory()) {
                    log.warn("档案ID: {} 的文件路径不存在或不是目录: {}", archive.getId(), diskPath);
                    continue;
                }

                // 压缩单个档案文件夹
                String zipFileName = archive.getCadreName() + "_" + archive.getIdNumber() + ".zip";
                File zipFile = new File(tempDir, zipFileName);
                ZipUtil.zip(archiveDir.getAbsolutePath(), zipFile.getAbsolutePath());
                zipFiles.add(zipFile);
            }

            if (zipFiles.isEmpty()) {
                throw new BizException(BizExceptionCode.MESSAGE, "没有可导出的档案文件");
            }

            // 将所有压缩文件再次打包
            String finalZipName = "档案批量导出_" + System.currentTimeMillis() + ".zip";
            String finalZipPath = tempDir + File.separator + finalZipName;

            // 创建最终压缩包
            File[] zipFileArray = zipFiles.toArray(new File[0]);
            ZipUtil.zip(new File(finalZipPath), false, zipFileArray);

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            FileUtils.setAttachmentResponseHeader(response, finalZipName);

            // 输出文件
            FileUtils.writeBytes(finalZipPath, response.getOutputStream());

        } catch (IOException e) {
            log.error("批量导出档案失败", e);
            throw new BizException(BizExceptionCode.MESSAGE, "批量导出档案失败: " + e.getMessage());
        } finally {
            // 清理临时文件
            try {
                if (tempDirFile.exists()) {
                    FileUtil.del(tempDirFile);
                }
            } catch (Exception e) {
                log.error("清理临时文件失败: {}", tempDir, e);
            }
        }
    }

    /**
     * 更新在库状态
     *
     * @param id                  档案ID
     * @param archiveStockStatus  在库状态
     */
    @Override
    public void updateStockStatus(Long id, String archiveStockStatus) {
        CadreArchive cadreArchive = new CadreArchive();
        cadreArchive.setId(id);
        cadreArchive.setArchiveStockStatus(archiveStockStatus);
        cadreArchiveMapper.updateById(cadreArchive);
    }

    /**
     * 更新单位
     *
     * @param id      档案ID
     * @param deptId  单位ID
     */
    @Override
    public void updateDept(Long id, Long deptId) {
        CadreArchive cadreArchive = new CadreArchive();
        cadreArchive.setId(id);
        cadreArchive.setDeptId(deptId);
        cadreArchiveMapper.updateById(cadreArchive);
    }

}