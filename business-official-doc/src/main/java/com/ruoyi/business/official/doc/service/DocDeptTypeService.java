package com.ruoyi.business.official.doc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.official.doc.controller.domain.DocDeptTypeTreeItem;
import com.ruoyi.business.official.doc.dal.dos.DocDeptType;

import java.util.List;

/**
 * 机构类型表 服务层
 *
 * @author Snow
 */
public interface DocDeptTypeService extends IService<DocDeptType> {

    /**
     * 树
     *
     * @return {@link List }<{@link DocDeptTypeTreeItem }>
     */
    List<DocDeptTypeTreeItem> tree();

}
