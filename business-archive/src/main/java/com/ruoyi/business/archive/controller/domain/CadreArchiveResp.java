package com.ruoyi.business.archive.controller.domain;

import com.ruoyi.business.archive.dal.dos.CadreArchive;
import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Snow
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CadreArchiveResp extends CadreArchive {

    private CadreArchiveDept deptInfo;

}
