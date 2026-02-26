package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.controller.domain.CadreArchiveImportResp;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.archive.util.CadreArchiveFileParseUtil;
import com.ruoyi.business.common.domain.dto.DeptDTO;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.common.mapper.SysMapper;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.common.config.RuoYiConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private CadreArchiveFileParseUtil cadreArchiveFileParseUtil;

    /**
     * 列表
     *
     * @param req req
     * @return {@link PageResp }<{@link CadreArchiveResp }>
     */
    @Override
    public PageResp<CadreArchiveResp> page(CadreArchivePageReq req) {
        Page<CadreArchive> page = cadreArchiveMapper.selectPages(req.mybatisPage(), req);
        PageResp<CadreArchiveResp> pageResp = PageResp.of(page.getTotal(), BeanUtil.mapList(page.getRecords(), CadreArchiveResp.class));
        if (CollUtil.isEmpty(pageResp.getRecords())) {
            return pageResp;
        }

        List<Long> deptIdList = pageResp.getRecords().stream().map(CadreArchiveResp::getDeptId).distinct().toList();
        if (CollUtil.isEmpty(deptIdList)) {
            return pageResp;
        }

        List<DeptDTO> deptList = sysMapper.selectDeptById(deptIdList);
        Map<String, DeptDTO> deptMap = deptList.stream().collect(Collectors.toMap(DeptDTO::getDeptId, Function.identity()));
        pageResp.getRecords().forEach(resp -> {
            if (resp.getDeptId() == null) {
                return;
            }

            DeptDTO deptDTO = deptMap.get(resp.getDeptId().toString());
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
        cadreArchiveList.stream().map(CadreArchive::getArchiveFilePath).map(RuoYiConfig::fileUrlToPath).forEach(item -> {
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
    public CadreArchiveImportResp importArchive(List<MultipartFile> fileList, boolean updateSupport) {
        List<CadreArchiveItem> itemList = cadreArchiveItemMapper.listByArchiveId(Collections.singletonList(CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID), "");
        Map<String, CadreArchiveItem> itemMap = itemList.stream().collect(Collectors.toMap(CadreArchiveItem::getItemType, Function.identity(), (v1, v2) -> v1));

        Map<String, String> parseCadreIdNumberMap = new ConcurrentHashMap<>(fileList.size());
        cadreArchiveFileParseUtil.parseAsync(fileList, itemMap, parseCadreIdNumberMap);
        return cadreArchiveFileParseUtil.waitCompletion(new CadreArchiveImportResp().setTotalCount(fileList.size()));
    }

}