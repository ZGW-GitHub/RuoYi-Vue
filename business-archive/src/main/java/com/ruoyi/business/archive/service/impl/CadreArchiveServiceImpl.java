package com.ruoyi.business.archive.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveService;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.common.util.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 干部档案 Service 实现类
 *
 * @author Snow
 */
@Service
public class CadreArchiveServiceImpl extends ServiceImpl<CadreArchiveMapper, CadreArchive> implements CadreArchiveService {

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    /**
     * 列表
     *
     * @param req req
     * @return {@link PageResp }<{@link CadreArchiveResp }>
     */
    @Override
    public PageResp<CadreArchiveResp> page(CadreArchivePageReq req) {
        Page<CadreArchive> page = cadreArchiveMapper.selectPages(req.mybatisPage(), req);
        return PageResp.of(page.getTotal(), BeanUtil.mapList(page.getRecords(), CadreArchiveResp.class));
    }

}