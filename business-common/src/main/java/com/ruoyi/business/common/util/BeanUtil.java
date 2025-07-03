/*
 * Copyright (C) <2024> <Snow>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ruoyi.business.common.util;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.business.common.domain.resp.PageResp;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Snow
 */
@Slf4j
public class BeanUtil {

    public static <S, T> T map(S source, Class<T> targetClass, String... ignoreProperties) {
        return cn.hutool.core.bean.BeanUtil.copyProperties(source, targetClass, ignoreProperties);
    }

    public static <S, T> List<T> mapList(Collection<S> sourceList, Class<T> targetClass, String... ignoreProperties) {
        if (CollUtil.isEmpty(sourceList)) {
            return Collections.emptyList();
        }

        return sourceList.stream().map(source -> map(source, targetClass, ignoreProperties)).toList();
    }

    public static <S, T> PageResp<T> mapPage(IPage<S> pageResp, Class<T> targetClass, String... ignoreProperties) {
        List<S> pageRecordList = pageResp.getRecords();
        if (CollUtil.isEmpty(pageRecordList)) {
            return PageResp.of(pageResp.getTotal(), new ArrayList<>());
        }

        List<T> targetPageRecordList = pageRecordList.stream().map(source -> map(source, targetClass, ignoreProperties)).toList();
        return PageResp.of(pageResp.getTotal(), targetPageRecordList);
    }

    public static <S, T> PageResp<T> mapPage(PageResp<S> pageResp, Class<T> targetClass, String... ignoreProperties) {
        List<S> pageRecordList = pageResp.getRecords();
        if (CollUtil.isEmpty(pageRecordList)) {
            return PageResp.of(pageResp.getTotal(), new ArrayList<>());
        }

        List<T> targetPageRecordList = pageRecordList.stream().map(source -> map(source, targetClass, ignoreProperties)).toList();
        return PageResp.of(pageResp.getTotal(), targetPageRecordList);
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target, ignoreProperties);
    }

    public static void copyProperties(Object source, Object target, boolean ignoreCase) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target, ignoreCase);
    }

    public static void copyProperties(Object source, Object target, CopyOptions copyOptions) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target, copyOptions);
    }

}
