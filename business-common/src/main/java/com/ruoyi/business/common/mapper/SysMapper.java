package com.ruoyi.business.common.mapper;

import com.ruoyi.business.common.domain.dto.DeptDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Snow
 */
@Mapper
public interface SysMapper {

    List<DeptDTO> selectDeptById(@Param("idList") List<Long> idList);

}
