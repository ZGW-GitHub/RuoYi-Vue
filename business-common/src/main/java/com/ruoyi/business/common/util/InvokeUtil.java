package com.ruoyi.business.common.util;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Snow
 */
public class InvokeUtil {

    public static <P, R> List<R> intercept(Collection<P> collection, Supplier<List<R>> supplier) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }
        return CollUtil.emptyIfNull(supplier.get());
    }

    public static <R, K> Map<K, R> collToMap(Collection<R> collection, Function<R, K> keyMapper) {
        return collection.stream().collect(Collectors.toMap(keyMapper, Function.identity(), (o, n) -> o));
    }

}
