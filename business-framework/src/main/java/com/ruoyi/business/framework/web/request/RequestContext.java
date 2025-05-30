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

package com.ruoyi.business.framework.web.request;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 愆凡
 * @date 2022/6/13 16:47
 */
@Slf4j
public class RequestContext {

	private final Map<RequestContextEnum, String> contextInfo = new ConcurrentHashMap<>();

	protected RequestContext(String traceId) {
		if (StrUtil.isBlank(traceId)) {
			throw new RuntimeException("TraceId 为空");
		}

		contextInfo.put(RequestContextEnum.TRACE_ID, traceId);
	}

	protected void addInfo(RequestContextEnum key, String value) {
		if (RequestContextEnum.TRACE_ID.equals(key)) {
			log.warn("【 链路追踪 】>>>>>> 线程【 {} 】试图覆盖 TRACE_ID ，线程栈：{}", Thread.currentThread().getName(), Thread.currentThread().getStackTrace());
			return;
		}

		contextInfo.put(key, StrUtil.blankToDefault(value, StrUtil.EMPTY));
	}

	protected void removeInfo(RequestContextEnum key) {
		contextInfo.remove(key);
	}

	protected String getInfo(RequestContextEnum key) {
		return StrUtil.nullToEmpty(contextInfo.get(key));
	}

	protected String getTraceId() {
		return contextInfo.get(RequestContextEnum.TRACE_ID);
	}

	protected Map<RequestContextEnum, String> getInfos() {
		return contextInfo;
	}

	@Override
	public String toString() {
		return contextInfo.toString();
	}

}
