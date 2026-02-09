package com.ruoyi.business.archive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageRecord;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeResp;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.common.domain.resp.PageResp;

/**
 * 档案项表 服务层
 * 
 * @author Snow
 */
public interface CadreArchiveItemService extends IService<CadreArchiveItem> {

    /**
     * 树
     *
     * @param req req
     * @return {@link CadreArchiveItemTreeResp }
     */
    CadreArchiveItemTreeResp tree(CadreArchiveItemTreeReq req);

    /**
     * 页面
     *
     * @param req req
     * @return {@link PageResp }<{@link CadreArchiveItemPageRecord }>
     */
    PageResp<CadreArchiveItemPageRecord> page(CadreArchiveItemPageReq req);

}