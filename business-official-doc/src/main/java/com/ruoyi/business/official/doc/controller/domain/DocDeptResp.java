package com.ruoyi.business.official.doc.controller.domain;

import com.ruoyi.business.official.doc.dal.dos.DocDept;
import com.ruoyi.business.official.doc.dal.dos.DocDeptGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DocDeptResp extends DocDept {

    private DocDeptGroup deptGroupInfo;

}
