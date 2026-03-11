package com.ruoyi.business.archive.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.archive.controller.domain.CadreArchiveDeptSaveReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveDeptTreeItem;
import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import com.ruoyi.business.common.domain.req.IdsReq;

import java.util.List;

/**
 * 干部档案部门表 服务层
 *
 * @author Snow
 */
public interface CadreArchiveDeptService extends IService<CadreArchiveDept> {

    /**
     * 树
     *
     * @return {@link List }<{@link CadreArchiveDeptTreeItem }>
     */
    List<CadreArchiveDeptTreeItem> tree();

    /**
     * 保存部门
     *
     * @param req 保存请求
     */
    Boolean save(CadreArchiveDeptSaveReq req);

    /**
     * 批量删除部门
     *
     * @param req req
     */
    Boolean delete(IdsReq req);

}
