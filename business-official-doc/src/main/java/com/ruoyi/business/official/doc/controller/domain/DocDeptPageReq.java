package com.ruoyi.business.official.doc.controller.domain;

import com.ruoyi.business.common.domain.req.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DocDeptPageReq extends PageReq {

    private String deptName;

    private Long deptType;

    private String deptLevel;

}
