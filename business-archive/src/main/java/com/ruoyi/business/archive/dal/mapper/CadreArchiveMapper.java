package com.ruoyi.business.archive.dal.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.archive.controller.domain.CadreArchivePageReq;
import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import com.ruoyi.business.common.domain.dto.CountDTO;
import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 干部档案 Mapper 接口
 *
 * @author Snow
 */
@Mapper
public interface CadreArchiveMapper extends BaseMapper<CadreArchive> {

    Page<CadreArchive> page(Page<CadreArchive> page, @Param("req") CadreArchivePageReq req);

    default List<CadreArchive> listByIdNumber(String idNumber) {
        return lambdaChainQueryWrapper()
                .eq(CadreArchive::getIdNumber, idNumber)
                .list();
    }

    default void updateDeptId(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return;
        }

        lambdaChainUpdateWrapper()
                .set(CadreArchive::getDeptId, CadreArchiveDept.ROOT_DEPT_ID)
                .in(CadreArchive::getDeptId, idList);

    }

    /**
     * 查询部门下关联的档案数量
     *
     * @param deptIdList 部门ID列表
     * @return 档案数量
     */
    List<CountDTO> countByDeptId(@Param("deptIdList") List<Long> deptIdList);

    List<CountDTO> countForDeptId();

}