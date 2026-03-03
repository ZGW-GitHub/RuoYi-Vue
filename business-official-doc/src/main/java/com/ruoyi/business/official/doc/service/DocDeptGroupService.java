package com.ruoyi.business.official.doc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.official.doc.controller.domain.DocDeptGroupTreeItem;
import com.ruoyi.business.official.doc.dal.dos.DocDeptGroup;

import java.util.List;

/**
 * 机构类型表 服务层
 *
 * @author Snow
 */
public interface DocDeptGroupService extends IService<DocDeptGroup> {

    /**
     * 树
     *
     * @return {@link List }<{@link DocDeptGroupTreeItem }>
     */
    List<DocDeptGroupTreeItem> tree();

}
