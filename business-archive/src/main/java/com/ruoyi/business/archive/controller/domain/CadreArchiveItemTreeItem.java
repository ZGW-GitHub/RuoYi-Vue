package com.ruoyi.business.archive.controller.domain;

import com.ruoyi.business.common.mybatis.domain.TreeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveItemTreeItem extends TreeDTO {

    /** 干部档案id */
    private Long archiveId;

    /** 档案类别 */
    private String archiveType;

    /** 部门名称 */
    private String itemName;

    /** 父id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 顺序 */
    private Integer sort;

}
