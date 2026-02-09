package com.ruoyi.business.archive.dal.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageRecord;
import com.ruoyi.business.archive.controller.domain.CadreArchiveItemPageReq;
import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.archive.dal.enums.ArchiveItemTypeEnum;
import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Arrays;
import java.util.List;

/**
 * 档案项表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface CadreArchiveItemMapper extends BaseMapper<CadreArchiveItem> {

    default List<CadreArchiveItem> listByArchiveIdExcludedImage(List<Long> archiveIdList) {
        return lambdaChainQueryWrapper()
                .in(CadreArchiveItem::getArchiveId, archiveIdList)
                .notIn(CadreArchiveItem::getItemType, Arrays.asList(ArchiveItemTypeEnum.ORIGINAL_IMAGE.getCode(), ArchiveItemTypeEnum.OPTIMIZE_IMAGE.getCode()))
                .orderByAsc(CadreArchiveItem::getParentId)
                .orderByAsc(CadreArchiveItem::getSort)
                .list();
    }

    default List<CadreArchiveItem> listByArchiveId(List<Long> archiveIdList) {
        return lambdaChainQueryWrapper()
                .in(CadreArchiveItem::getArchiveId, archiveIdList)
                .orderByAsc(CadreArchiveItem::getParentId)
                .orderByAsc(CadreArchiveItem::getSort)
                .list();
    }

    default List<CadreArchiveItem> listByArchiveIdAndItemId(List<Long> archiveIdList, Long archiveItemId, List<String> parentItemIdList) {
        return lambdaChainQueryWrapper()
                .in(CadreArchiveItem::getArchiveId, archiveIdList)
                .eq(CadreArchiveItem::getId, archiveItemId).or().in(CadreArchiveItem::getId, parentItemIdList)
                .or().eq(CadreArchiveItem::getParentId, archiveItemId)
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

}