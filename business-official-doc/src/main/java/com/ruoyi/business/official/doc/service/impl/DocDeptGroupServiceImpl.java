package com.ruoyi.business.official.doc.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.business.common.util.TreeUtil;
import com.ruoyi.business.official.doc.controller.domain.DocDeptGroupTreeItem;
import com.ruoyi.business.official.doc.dal.dos.DocDeptGroup;
import com.ruoyi.business.official.doc.dal.mapper.DocDeptGroupMapper;
import com.ruoyi.business.official.doc.service.DocDeptGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 机构类型表 服务实现
 *
 * @author Snow
 */
@Service
public class DocDeptGroupServiceImpl extends ServiceImpl<DocDeptGroupMapper, DocDeptGroup> implements DocDeptGroupService {

    @Resource
    private DocDeptGroupMapper docDeptGroupMapper;

    /**
     * 树
     *
     * @return {@link List }<{@link DocDeptGroupTreeItem }>
     */
    @Override
    public List<DocDeptGroupTreeItem> tree() {
        List<DocDeptGroup> typeList = docDeptGroupMapper.selectAll();
        if (CollUtil.isEmpty(typeList)) {
            return Collections.emptyList();
        }

        // 构建 resp
        List<DocDeptGroupTreeItem> treeItemList = BeanUtil.mapList(typeList, DocDeptGroupTreeItem.class, (source, target) -> {
            target.setKey(String.valueOf(source.getId()));
            target.setTitle(source.getGroupName());
            target.setParentKey(String.valueOf(source.getParentId()));
            target.setAncestorsKey(source.getAncestors());
        });

        return TreeUtil.buildTree(treeItemList);
    }

}
