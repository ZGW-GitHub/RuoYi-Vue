package com.ruoyi.business.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum YNEnum {

    YES("Y", 1, "1", "是"),
    NO("N", 0, "0", "否"),
    ;

    private final String codeStr;
    private final Integer codeNum;
    private final String codeNumStr;
    private final String desc;

}
