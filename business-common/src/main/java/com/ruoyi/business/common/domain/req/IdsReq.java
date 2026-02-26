package com.ruoyi.business.common.domain.req;

import cn.hutool.core.collection.CollUtil;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
@Data
public class IdsReq {

    @NotEmpty(message = "数据ID不能为空")
    private List<String> ids;

    public List<Long> ids() {
        return CollUtil.emptyIfNull(ids).stream().map(Long::valueOf).collect(Collectors.toList());
    }

}
