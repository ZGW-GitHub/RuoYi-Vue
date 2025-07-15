package com.ruoyi.business.common.domain.req;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
@Data
public class IdsReq {

    private List<String> ids;

    public List<Long> ids() {
        return CollUtil.emptyIfNull(ids).stream().map(Long::valueOf).collect(Collectors.toList());
    }

}
