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

package com.ruoyi.business.framework.web.util;

import com.ruoyi.business.framework.web.request.RequestContextEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author Snow
 * @date 2023/6/9 20:29
 */
@Slf4j
public class MDCUtil {

	public static void setTraceId(String traceId) {
		MDC.put(RequestContextEnum.TRACE_ID.getCode(), traceId);
	}

	public static String getTraceId() {
		return MDC.get(RequestContextEnum.TRACE_ID.getCode());
	}

	public static void removeTraceId() {
		MDC.remove(RequestContextEnum.TRACE_ID.getCode());
	}

	public static void clear() {
		MDC.clear();
	}

	public static String generateTraceId() {
		return "traceX" + cn.hutool.core.util.IdUtil.fastSimpleUUID();
	}

}
