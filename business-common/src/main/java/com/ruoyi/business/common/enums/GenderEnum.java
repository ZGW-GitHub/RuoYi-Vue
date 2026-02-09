package com.ruoyi.business.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Snow
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    MAN("1", "男"),
    WOMAN("2", "女");

    private final String code;
    private final String desc;

}
