package com.ruoyi.business.archive.controller.domain;

import lombok.Data;

/**
 * @author Snow
 */
@Data
public class CadreArchiveItemTreeReq {

    private Long archiveId;

    private Long archiveItemId;

    private Boolean treeIncludeImage;

}
