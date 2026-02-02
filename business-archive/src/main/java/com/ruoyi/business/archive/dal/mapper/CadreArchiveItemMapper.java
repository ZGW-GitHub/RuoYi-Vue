package com.ruoyi.business.archive.dal.mapper;

import com.ruoyi.business.archive.dal.dos.CadreArchiveItem;
import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 档案项表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface CadreArchiveItemMapper extends BaseMapper<CadreArchiveItem> {

    default List<CadreArchiveItem> listByArchiveId(Long archiveId) {
        return lambdaChainQueryWrapper()
                .eq(CadreArchiveItem::getArchiveId, archiveId)
                .orderByAsc(CadreArchiveItem::getSort)
                .list();
    }

}