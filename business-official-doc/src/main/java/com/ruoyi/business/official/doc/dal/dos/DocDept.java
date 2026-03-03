package com.ruoyi.business.official.doc.dal.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.common.mybatis.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 机构表 bus_doc_dept
 * 
 * @author Snow
 */
@Data
@TableName("bus_doc_dept")
@EqualsAndHashCode(callSuper = true)
public class DocDept extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 机构名称 */
    private String deptName;

    /** 机构规范化简称 */
    private String officialName;

    /** 机构类型 */
    private Long deptType;

    /** 机构级别(正副厅/本专科) */
    private String deptLevel;

    /** 主管机构ID（与 manage_dept_name 两者只有一个有值） */
    private Long manageDeptId;

    /** 主管机构名称（与 manage_dept_id 两者只有一个有值） */
    private String manageDeptName;

    /** 排序 */
    private Integer sort;

}
