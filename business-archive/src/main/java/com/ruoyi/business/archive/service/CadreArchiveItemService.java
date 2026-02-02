package com.ruoyi.business.archive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeItem;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeReq;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;

import java.util.List;

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
     * @return {@link List }<{@link CadreArchiveItemTreeItem }>
     */
    List<CadreArchiveItemTreeItem> tree(CadreArchiveItemTreeReq req);

}