package com.ruoyi.business.archive.controller.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Snow
 */
@Data
@Accessors(chain = true)
public class CadreArchiveImportResp {

    private Integer totalCount;

    private Integer failCount;

    private Integer successCount;

    private List<String> failFileNameList;

}
