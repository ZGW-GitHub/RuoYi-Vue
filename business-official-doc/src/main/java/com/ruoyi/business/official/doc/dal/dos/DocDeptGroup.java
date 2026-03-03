package com.ruoyi.business.official.doc.dal.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.common.mybatis.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 机构类型表 bus_doc_dept_group
 * 
 * @author Snow
 */
@Data
@TableName("bus_doc_dept_group")
@EqualsAndHashCode(callSuper = true)
public class DocDeptGroup extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 类型名称 */
    private String groupName;

    /** 父id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 顺序 */
    private Integer sort;

}
