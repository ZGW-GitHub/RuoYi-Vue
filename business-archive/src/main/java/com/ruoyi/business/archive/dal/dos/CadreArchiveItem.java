package com.ruoyi.business.archive.dal.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.common.mybatis.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 档案项表 bus_cadre_archive_item
 * 
 * @author Snow
 */
@Data
@TableName("bus_cadre_archive_item")
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveItem extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

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

    /**
     * 材料页数
     */
    private Integer materialPageCount;

    /** 父id */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 顺序 */
    private Integer sort;

}