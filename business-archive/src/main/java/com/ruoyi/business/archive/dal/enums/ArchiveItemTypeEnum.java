package com.ruoyi.business.archive.dal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum ArchiveItemTypeEnum {

    FOUR("四", "学历学位、职称、学术、培训等材料"),
    NINE("九", "工资、任免、出国、会议等材料"),
    ORIGINAL_IMAGE("originalImage", "原始图像"),
    OPTIMIZE_IMAGE("optimizeImage", "优化图像"),
    ;

    private final String code;
    private final String desc;

}
