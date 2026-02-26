package com.ruoyi.business.archive.dal.mapper;

import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 干部档案部门表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface CadreArchiveDeptMapper extends BaseMapper<CadreArchiveDept> {

    default List<CadreArchiveDept> selectAll() {
        return lambdaChainQueryWrapper()
                .list();
    }

    /**
     * 查询子部门数量
     *
     * @param parentId 父部门ID
     * @return 子部门数量
     */
    default Long countChildren(Long parentId) {
        return lambdaChainQueryWrapper()
                .eq(CadreArchiveDept::getParentId, parentId)
                .count();
    }

    /**
     * 查询所有子孙部门
     *
     * @param deptId 部门ID
     * @return 子孙部门列表
     */
    List<CadreArchiveDept> listDescendant(Long deptId);

}
