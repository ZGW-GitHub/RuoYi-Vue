package com.ruoyi.business.common.util;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.business.common.mybatis.domain.TreeDTO;
import com.ruoyi.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Snow
 */
public class TreeUtil {

    public static <T extends TreeDTO> List<T> buildTree(List<T> itemList) {
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
    private static <T extends TreeDTO> void recursionFn(List<T> itemList, T item) {
        List<T> childList = getChildList(itemList, item);
        item.setChildren(childList);

        for (T child : childList) {
            if (hasChild(itemList, child)) {
                recursionFn(itemList, child);
            }
        }
    }

    private static <T extends TreeDTO> List<T> getChildList(List<T> itemList, T item) {
        List<T> resultList = new ArrayList<>();
        for (T i : itemList) {
            if (StringUtils.isNotNull(i.getParentKey()) && i.getParentKey().equals(item.getKey())) {
                resultList.add(i);
            }
        }
        return resultList;
    }

    private static <T extends TreeDTO> boolean hasChild(List<T> itemList, T child) {
        return !getChildList(itemList, child).isEmpty();
    }

}
