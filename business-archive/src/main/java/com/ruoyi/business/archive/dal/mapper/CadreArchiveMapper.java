package com.ruoyi.business.archive.dal.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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

    default Page<CadreArchive> selectPages(Page<CadreArchive> page, CadreArchivePageReq req) {
        LambdaQueryChainWrapper<CadreArchive> wrapper = lambdaChainQueryWrapper();
        if (StrUtil.isNotBlank(req.getCadreName())) {
            wrapper.like(CadreArchive::getCadreName, req.getCadreName()).or()
                    .likeRight(CadreArchive::getCadreNamePy, req.getCadreName());
        }

        return wrapper.eq(req.getDeptId() != null, CadreArchive::getDeptId, req.getDeptId())
                .eq(StrUtil.isNotBlank(req.getArchiveStockStatus()), CadreArchive::getArchiveStockStatus, req.getArchiveStockStatus())
                .page(page);
    }

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