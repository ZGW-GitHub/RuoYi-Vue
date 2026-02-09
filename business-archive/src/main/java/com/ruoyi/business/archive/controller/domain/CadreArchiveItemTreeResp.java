package com.ruoyi.business.archive.controller.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * @author Snow
 */
@Data
@Accessors(chain = true)
public class CadreArchiveItemTreeResp {

    private CadreArchiveResp cadreArchiveInfo;
    private List<CadreArchiveItemTreeItem> treeData = Collections.emptyList();
    private List<CadreArchiveImage> originalImageList = Collections.emptyList();
    private List<CadreArchiveImage> optimizeImageList = Collections.emptyList();

    @Data
    public static class CadreArchiveImage {
        private String type;
        private String name;
        private String url;
        private String key;
        private String parentKey;
        private String ancestorsKey;
        private String materialName;
        private String materialDate;
    }

}
