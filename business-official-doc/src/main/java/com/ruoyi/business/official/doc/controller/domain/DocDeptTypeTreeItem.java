package com.ruoyi.business.official.doc.controller.domain;

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
public class DocDeptTypeTreeItem extends TreeDTO<DocDeptTypeTreeItem> {

    /** 类型名称 */
    private String typeName;

    /** 父id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 顺序 */
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
