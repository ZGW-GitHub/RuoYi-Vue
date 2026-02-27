package com.ruoyi.business.archive.dal.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageRecord;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageReq;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 档案项表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface CadreArchiveItemMapper extends BaseMapper<CadreArchiveItem> {

    default List<CadreArchiveItem> listByArchiveId(List<Long> archiveIdList, String treeExcludeImageType) {
        return lambdaChainQueryWrapper()
                .in(CadreArchiveItem::getArchiveId, archiveIdList)
                .orderByAsc(CadreArchiveItem::getParentId)
                .orderByAsc(CadreArchiveItem::getSort)
                .ne(StrUtil.isNotBlank(treeExcludeImageType), CadreArchiveItem::getItemType, treeExcludeImageType)
                .list();
    }

    default List<CadreArchiveItem> listByArchiveIdAndItemId(List<Long> archiveIdList, Long archiveItemId, List<String> parentItemIdList, String treeExcludeImageType) {
        return lambdaChainQueryWrapper()
                .in(CadreArchiveItem::getArchiveId, archiveIdList)
                .eq(CadreArchiveItem::getId, archiveItemId).or().in(CadreArchiveItem::getId, parentItemIdList).or().eq(CadreArchiveItem::getParentId, archiveItemId)
                .ne(StrUtil.isNotBlank(treeExcludeImageType), CadreArchiveItem::getItemType, treeExcludeImageType)
                .orderByAsc(CadreArchiveItem::getParentId)
                .orderByAsc(CadreArchiveItem::getSort)
                .list();
    }

    /**
     * 分页查询档案项
     *
     * @param page 分页对象
     * @param req 查询条件
     * @return 分页结果
     */
    Page<CadreArchiveItemPageRecord> selectPages(Page<CadreArchiveItemPageRecord> page, @Param("req") CadreArchiveItemPageReq req);

    default void deleteByArchiveId(List<Long> oldCadreArchiveIdList) {
        lambdaChainUpdateWrapper()
                .in(CadreArchiveItem::getArchiveId, oldCadreArchiveIdList)
                .remove();
    }

}