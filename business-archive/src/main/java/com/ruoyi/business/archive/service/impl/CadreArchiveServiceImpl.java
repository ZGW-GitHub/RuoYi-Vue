package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.common.domain.dto.DeptDTO;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.common.mapper.SysMapper;
import com.ruoyi.business.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 干部档案 Service 实现类
 *
 * @author Snow
 */
@Service
public class CadreArchiveServiceImpl extends ServiceImpl<CadreArchiveMapper, CadreArchive> implements CadreArchiveService {

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    @Resource
    private SysMapper sysMapper;

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

}