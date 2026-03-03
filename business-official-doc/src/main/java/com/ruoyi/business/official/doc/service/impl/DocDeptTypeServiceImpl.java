package com.ruoyi.business.official.doc.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.business.common.util.TreeUtil;
import com.ruoyi.business.official.doc.controller.domain.DocDeptTypeTreeItem;
import com.ruoyi.business.official.doc.dal.dos.DocDeptType;
import com.ruoyi.business.official.doc.dal.mapper.DocDeptTypeMapper;
import com.ruoyi.business.official.doc.service.DocDeptTypeService;
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
public class DocDeptTypeServiceImpl extends ServiceImpl<DocDeptTypeMapper, DocDeptType> implements DocDeptTypeService {

    @Resource
    private DocDeptTypeMapper docDeptTypeMapper;

    /**
     * 树
     *
     * @return {@link List }<{@link DocDeptTypeTreeItem }>
     */
    @Override
    public List<DocDeptTypeTreeItem> tree() {
        List<DocDeptType> typeList = docDeptTypeMapper.selectAll();
        if (CollUtil.isEmpty(typeList)) {
            return Collections.emptyList();
        }

        // 构建 resp
        List<DocDeptTypeTreeItem> treeItemList = BeanUtil.mapList(typeList, DocDeptTypeTreeItem.class, (source, target) -> {
            target.setKey(String.valueOf(source.getId()));
            target.setTitle(source.getTypeName());
            target.setParentKey(String.valueOf(source.getParentId()));
            target.setAncestorsKey(source.getAncestors());
        });

        return TreeUtil.buildTree(treeItemList);
    }

}
