package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeItem;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemTreeReq;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.service.CadreArchiveItemService;
import com.ruoyi.business.common.mybatis.domain.TreeDTO;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 档案项表 服务实现
 *
 * @author Snow
 */
@Service
public class CadreArchiveItemServiceImpl extends ServiceImpl<CadreArchiveItemMapper, CadreArchiveItem> implements CadreArchiveItemService {

    @Resource
    private CadreArchiveItemMapper cadreArchiveItemMapper;

    /**
     * 树
     *
     * @param req req
     * @return {@link List }<{@link CadreArchiveItemTreeItem }>
     */
    @Override
    public List<CadreArchiveItemTreeItem> tree(CadreArchiveItemTreeReq req) {
        List<CadreArchiveItem> itemList = cadreArchiveItemMapper.listByArchiveId(req.getArchiveId());
        List<CadreArchiveItemTreeItem> treeItemList = BeanUtil.mapList(itemList, CadreArchiveItemTreeItem.class, (source, target) -> {
            target.setTitle(source.getItemName());
            target.setKey(String.valueOf(source.getId()));
            target.setParentKey(String.valueOf(source.getParentId()));
        });

        return buildTree(treeItemList);
    }

    private <T extends TreeDTO> List<T> buildTree(List<T> itemList) {
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyList();
        }

        List<T> resultList = new ArrayList<>();
        List<String> itemIdList = itemList.stream().map(T::getKey).toList();
        for (T item : itemList) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!itemIdList.contains(item.getParentKey())) {
                recursionFn(itemList, item);
                resultList.add(item);
            }
        }

        if (CollUtil.isEmpty(resultList)) {
            return itemList;
        }
        return resultList;
    }

    /**
     * 递归方法
     *
     * @param itemList 项目列表
     * @param item     项目
     */
    private <T extends TreeDTO> void recursionFn(List<T> itemList, T item) {
        List<T> childList = getChildList(itemList, item);
        item.setChildren(childList);

        for (T child : childList) {
            if (hasChild(itemList, child)) {
                recursionFn(itemList, child);
            }
        }
    }

    private <T extends TreeDTO> List<T> getChildList(List<T> itemList, T item) {
        List<T> resultList = new ArrayList<>();
        for (T i : itemList) {
            if (StringUtils.isNotNull(i.getParentKey()) && i.getParentKey().equals(item.getKey())) {
                resultList.add(i);
            }
        }
        return resultList;
    }

    private <T extends TreeDTO> boolean hasChild(List<T> itemList, T child) {
        return !getChildList(itemList, child).isEmpty();
    }

}