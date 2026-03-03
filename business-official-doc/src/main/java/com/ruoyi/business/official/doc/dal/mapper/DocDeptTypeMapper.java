package com.ruoyi.business.official.doc.dal.mapper;

import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import com.ruoyi.business.official.doc.dal.dos.DocDeptType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 机构类型表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface DocDeptTypeMapper extends BaseMapper<DocDeptType> {

    default List<DocDeptType> selectAll() {
        return lambdaChainQueryWrapper()
                .orderByAsc(DocDeptType::getParentId, DocDeptType::getSort)
                .list();
    }

}
