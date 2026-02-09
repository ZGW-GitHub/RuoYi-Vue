package com.ruoyi.business.archive.controller.domain;

import lombok.Data;

/**
 * @author Snow
 */
@Data
public class CadreArchiveItemPageRecord {

    /**
     * 档案ID
     */
    private Long archiveId;

    /**
     * 档案项ID
     */
    private Long itemId;

    /**
     * 干部姓名
     */
    private String cadreName;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 档案类别
     */
    private String itemType;

    /**
     * 档案名称
     */
    private String itemName;

    /**
     * 材料日期
     */
    private String materialDate;

    /**
     * 材料页数
     */
    private Integer materialPageCount;

}
