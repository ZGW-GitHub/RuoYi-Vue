package com.ruoyi.business.archive.controller.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.business.common.mybatis.domain.TreeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveDeptTreeItem extends TreeDTO<CadreArchiveDeptTreeItem> {

    /** 部门名称 */
    private String deptName;

    /** 父部门ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 排序 */
    private Integer sort;

    /**
     * 档案数
     */
    private Long archiveCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
