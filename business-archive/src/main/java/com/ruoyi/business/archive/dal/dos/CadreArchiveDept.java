package com.ruoyi.business.archive.dal.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.common.mybatis.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 干部档案部门表 bus_cadre_archive_dept
 * 
 * @author Snow
 */
@Data
@TableName("bus_cadre_archive_dept")
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveDept extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门名称 */
    private String deptName;

    /** 父id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 顺序 */
    private Integer sort;

}
