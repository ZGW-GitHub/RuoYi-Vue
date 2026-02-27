package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.constants.CadreArchiveFileConstant;
import com.ruoyi.business.archive.controller.domain.*;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.enums.ArchiveItemTypeEnum;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveDeptMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveItemMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveItemService;
import com.ruoyi.business.archive.util.CadreArchiveFileParseUtil;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.business.common.util.TreeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 档案项表 服务实现
 *
 * @author Snow
 */
@Service
public class CadreArchiveItemServiceImpl extends ServiceImpl<CadreArchiveItemMapper, CadreArchiveItem> implements CadreArchiveItemService {

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    @Resource
    private CadreArchiveItemMapper cadreArchiveItemMapper;

    @Resource
    private CadreArchiveDeptMapper cadreArchiveDeptMapper;

    /**
     * 树
     *
     * @param req req
     * @return {@link CadreArchiveItemTreeResp }
     */
    @Override
    public CadreArchiveItemTreeResp tree(CadreArchiveItemTreeReq req) {
        String archiveImageType = req.getArchiveImageType();
        String catalogueTreeType = req.getCatalogueTreeType();
        String treeExcludeImageType = StrUtil.isNotBlank(archiveImageType) ? (ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode().equals(archiveImageType)
                ? ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode() : ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode()) : StrUtil.EMPTY;

        CadreArchive cadreArchive = cadreArchiveMapper.selectById(req.getArchiveId());
        if (cadreArchive == null) {
            return new CadreArchiveItemTreeResp();
        }

        List<CadreArchiveItem> itemList;
        if (req.getArchiveItemId() == null) {
            itemList = cadreArchiveItemMapper.listByArchiveId(Arrays.asList(req.getArchiveId(), CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID), treeExcludeImageType);
        } else {
            List<String> parentItemIdList = Optional.of(req.getArchiveItemId()).map(cadreArchiveItemMapper::selectById)
                    .map(CadreArchiveItem::getAncestors).map(ancestors -> StrUtil.split(ancestors, ','))
                    .orElse(Collections.emptyList());
            itemList = cadreArchiveItemMapper.listByArchiveIdAndItemId(Arrays.asList(req.getArchiveId(), CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID),
                    req.getArchiveItemId(), parentItemIdList, treeExcludeImageType);
        }

        List<CadreArchiveItemTreeItem> treeItemList = BeanUtil.mapList(itemList, CadreArchiveItemTreeItem.class, (source, target) -> {
            String materialDate = source.getMaterialDate();
            boolean isCommonItem = source.getArchiveId().equals(CadreArchiveFileParseUtil.CADRE_COMMON_ARCHIVE_ITEM_ARCHIVE_ID);
            if (isCommonItem) {
                target.setTitle(source.getItemName());
            } else if (source.getItemType().equals(ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode())
                    || source.getItemType().equals(ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode())) {
                target.setTitle(source.getSort().toString());
            } else if (StrUtil.isBlank(materialDate)) {
                target.setTitle(StrUtil.format("{}、{}", source.getSort(), source.getItemName()));
            } else {
                target.setTitle(StrUtil.format("{}、{}（{}）", source.getSort(), source.getItemName(), materialDate));
            }

            if (ArchiveItemTypeEnum.FOUR.getCode().equals(source.getItemType())
                    || ArchiveItemTypeEnum.NINE.getCode().equals(source.getItemType())) {
                target.setDisabled(true);
            }

            target.setKey(String.valueOf(source.getId()));
            target.setParentKey(String.valueOf(source.getParentId()));
            target.setAncestorsKey(source.getAncestors());

            if (ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode().equals(source.getItemType())) {
                target.setImageUri(CadreArchiveFileConstant.getImageUrl(cadreArchive.getArchiveFilePath(), source.getItemName(), ArchiveItemTypeEnum.ORIGINAL_IMAGE));
            }
            if (ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode().equals(source.getItemType())) {
                target.setImageUri(CadreArchiveFileConstant.getImageUrl(cadreArchive.getArchiveFilePath(), source.getItemName(), ArchiveItemTypeEnum.OPTIMIZE_IMAGE));
            }
        });

        // 构建树
        List<CadreArchiveItemTreeItem> treeData = TreeUtil.buildTree(treeItemList);

        // 收集图片
        List<CadreArchiveItemTreeResp.CadreArchiveImage> originalImageList = new ArrayList<>();
        List<CadreArchiveItemTreeResp.CadreArchiveImage> optimizeImageList = new ArrayList<>();
        gatherImage(cadreArchive, null, treeData, originalImageList, optimizeImageList);

        // 移除图片节点
        if (!"print".equals(catalogueTreeType)) {
            removeImageNode(treeData);
        }

        Long deptId = cadreArchive.getDeptId();
        CadreArchiveDept cadreArchiveDept = cadreArchiveDeptMapper.selectById(deptId);

        CadreArchiveResp cadreArchiveResp = new CadreArchiveResp();
        cadreArchiveResp.setCadreName(cadreArchive.getCadreName());
        cadreArchiveResp.setIdNumber(cadreArchive.getIdNumber());
        cadreArchiveResp.setCreateTime(cadreArchive.getCreateTime());
        cadreArchiveResp.setDeptInfo(cadreArchiveDept);

        // 返回
        return new CadreArchiveItemTreeResp()
                .setCadreArchiveInfo(cadreArchiveResp)
                .setTreeData(treeData)
                .setOriginalImageList(originalImageList)
                .setOptimizeImageList(optimizeImageList);
    }

    @Override
    public PageResp<CadreArchiveItemPageRecord> page(CadreArchiveItemPageReq req) {
        Page<CadreArchiveItemPageRecord> page = cadreArchiveItemMapper.selectPages(req.mybatisPage(), req);
        return PageResp.of(page.getTotal(), page.getRecords());
    }

    private void removeImageNode(List<CadreArchiveItemTreeItem> treeData) {
        if (CollUtil.isEmpty(treeData)) {
            return;
        }

        treeData.forEach(item -> {
            List<CadreArchiveItemTreeItem> childrenList = item.getChildren();
            if (CollUtil.isEmpty(childrenList)) {
                return;
            }

            childrenList = childrenList.stream().filter(children -> {
                String itemType = children.getItemType();
                return !StrUtil.equalsAny(itemType, ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode(), ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode());
            }).toList();
            item.setChildren(childrenList);

            removeImageNode(childrenList);
        });
    }

    public void gatherImage(CadreArchive cadreArchive,
                            CadreArchiveItemTreeItem parent,
                            List<CadreArchiveItemTreeItem> treeData,
                            List<CadreArchiveItemTreeResp.CadreArchiveImage> originalImageList,
                            List<CadreArchiveItemTreeResp.CadreArchiveImage> optimizeImageList) {
        treeData.forEach(item -> {
            if (CollUtil.isNotEmpty(item.getChildren())) {
                gatherImage(cadreArchive, item, item.getChildren(), originalImageList, optimizeImageList);
            }

            if (ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode().equals(item.getItemType())) {
                originalImageList.add(new CadreArchiveItemTreeResp.CadreArchiveImage()
                        .setType(ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode())
                        .setName(item.getItemName())
                        .setUrl(CadreArchiveFileConstant.getImageUrl(cadreArchive.getArchiveFilePath(), item.getItemName(), ArchiveItemTypeEnum.ORIGINAL_IMAGE))
                        .setKey(item.getKey())
                        .setParentKey(item.getParentKey())
                        .setAncestorsKey(item.getAncestorsKey())
                        .setMaterialName(getMaterialName(parent))
                        .setMaterialDate(parent != null ? parent.getMaterialDate() : StrUtil.EMPTY));
            } else if (ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode().equals(item.getItemType())) {
                optimizeImageList.add(new CadreArchiveItemTreeResp.CadreArchiveImage()
                        .setType(ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode())
                        .setName(item.getItemName())
                        .setUrl(CadreArchiveFileConstant.getImageUrl(cadreArchive.getArchiveFilePath(), item.getItemName(), ArchiveItemTypeEnum.OPTIMIZE_IMAGE))
                        .setKey(item.getKey())
                        .setParentKey(item.getParentKey())
                        .setAncestorsKey(item.getAncestorsKey())
                        .setMaterialName(getMaterialName(parent))
                        .setMaterialDate(parent != null ? parent.getMaterialDate() : StrUtil.EMPTY));
            }
        });
    }

    private String getMaterialName(CadreArchiveItemTreeItem item) {
        return item != null ? StrUtil.format("{} - {}、{}", item.getItemType(), item.getSort(), item.getItemName()) : StrUtil.EMPTY;
    }

}