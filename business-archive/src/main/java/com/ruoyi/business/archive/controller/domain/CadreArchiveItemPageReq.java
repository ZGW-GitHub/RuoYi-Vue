package com.ruoyi.business.archive.controller.domain;

import com.ruoyi.business.common.domain.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveItemPageReq extends PageReq {

    /**
     * 干部姓名
     */
    private String cadreName;

    /**
     * 档案类别
     */
    private List<String> itemTypeList;

    /**
     * 档案名称
     */
    private String itemName;

}
