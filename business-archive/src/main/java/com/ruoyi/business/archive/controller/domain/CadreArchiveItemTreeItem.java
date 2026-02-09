package com.ruoyi.business.archive.controller.domain;

import com.ruoyi.business.common.mybatis.domain.TreeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveItemTreeItem extends TreeDTO<CadreArchiveItemTreeItem> {

    /** 干部档案id */
    private Long archiveId;

    /** 档案类别 */
    private String itemType;

    /** 档案名称 */
    private String itemName;

    /**
     * 材料日期
     */
    private String materialDate;

    /** 顺序 */
    private Integer sort;

}
