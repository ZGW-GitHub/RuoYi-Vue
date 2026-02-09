package com.ruoyi.business.common.mybatis.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Snow
 */
@Data
public class TreeDTO<T extends TreeDTO> {

    private String key;
    private String title;
    private String parentKey;
    private String ancestorsKey;
    private Boolean disabled = false;
    private List<T> children;

}
