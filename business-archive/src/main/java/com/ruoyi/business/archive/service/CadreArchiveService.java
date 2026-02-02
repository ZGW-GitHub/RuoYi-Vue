package com.ruoyi.business.archive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveResp;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.common.domain.resp.PageResp;

/**
 * 干部档案 Service 接口
 * 
 * @author Snow
 */
public interface CadreArchiveService extends IService<CadreArchive> {

    /**
     * 列表
     *
     * @param req req
     * @return {@link PageResp }<{@link CadreArchiveResp }>
     */
    PageResp<CadreArchiveResp> page(CadreArchivePageReq req);

}