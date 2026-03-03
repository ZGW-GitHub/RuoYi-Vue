package com.ruoyi.business.official.doc.dal.mapper;

import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import com.ruoyi.business.official.doc.dal.dos.DocDeptGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 机构类型表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface DocDeptGroupMapper extends BaseMapper<DocDeptGroup> {

    default List<DocDeptGroup> selectAll() {
        return lambdaChainQueryWrapper()
                .orderByAsc(DocDeptGroup::getParentId, DocDeptGroup::getSort)
                .list();
    }

}
