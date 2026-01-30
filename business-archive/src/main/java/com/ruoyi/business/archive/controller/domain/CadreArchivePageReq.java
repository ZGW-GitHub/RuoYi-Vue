package com.ruoyi.business.archive.controller.domain;

import com.ruoyi.business.common.domain.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CadreArchivePageReq extends PageReq {

    private Long deptId;

    private String cadreName;

    private String cadreDeptName;

    private String archiveStockStatus;

}
