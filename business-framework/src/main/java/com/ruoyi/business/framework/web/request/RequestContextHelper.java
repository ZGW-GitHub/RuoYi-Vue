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
import com.alibaba.ttl.TransmittableThreadLocal;
import com.ruoyi.business.framework.web.util.MDCUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author 愆凡
 * @date 2022/6/13 16:47
 */
@Slf4j
public class RequestContextHelper {

	// private static final TransmittableThreadLocal<RequestContext> TTL = new TransmittableThreadLocal<>();
	private static final ThreadLocal<RequestContext> TTL = new TransmittableThreadLocal<>();

	public static RequestContext startContext(String traceId, boolean mdcTrace) {
		// set MDC traceId
		if (mdcTrace) {
			MDCUtil.setTraceId(traceId);
		}

		RequestContext requestContext = TTL.get();
		if (requestContext != null) {
			throw new RuntimeException("RequestContext 已存在");
		}

		requestContext = new RequestContext(traceId);

		// set RequestContext to ThreadLocal
		TTL.set(requestContext);

		return requestContext;
	}

	/**
	 * 这种方式不强求使用 TransmittableThreadLocal、TtlExecutors
	 *
	 * @param parentRequestContext 父 RequestContext
	 * @param mdcTrace             是否配置 MDC traceId
	 *
	 * @return 子 RequestContext
	 */
	public static RequestContext startChildContext(RequestContext parentRequestContext, boolean mdcTrace) {
		if (parentRequestContext == null) {
			throw new RuntimeException("RequestContext 不存在");
		}

		String parentTraceId = parentRequestContext.getTraceId();
		String childTraceId = MDCUtil.generateTraceId();
		log.info("[ 链路追踪 ] RequestContext 派生. parentTraceId : {}, childTraceId : {}", parentTraceId, childTraceId);

		RequestContext childRequestContext = createChildContext(parentRequestContext, childTraceId);

		// set RequestContext to ThreadLocal
		TTL.set(childRequestContext);

		// set MDC traceId
		if (mdcTrace) {
			MDCUtil.setTraceId(childTraceId);
		}

		return childRequestContext;
	}

	public static RequestContext currentContext() {
		return TTL.get();
	}

	public static Boolean hasContext() {
		return TTL.get() != null;
	}

	public static void addInfo(RequestContextEnum key, String value) {
		TTL.get().addInfo(key, value);
	}

	public static String getInfo(RequestContextEnum key) {
		RequestContext requestContext = TTL.get();
		if (requestContext == null) {
			return null;
		}

		return requestContext.getInfo(key);
	}

	public static String getTraceId() {
		return Optional.ofNullable(currentContext()).map(RequestContext::getTraceId).orElse(StrUtil.EMPTY);
	}

	public static void clear(boolean clearMDC) {
		TTL.remove();

		if (clearMDC) {
			MDCUtil.clear();
		}
	}

	private static RequestContext createChildContext(RequestContext parentRequestContext, String childTraceId) {
		RequestContext childRequestContext = new RequestContext(childTraceId);
		parentRequestContext.getInfos().forEach((key, value) -> {
			if (RequestContextEnum.TRACE_ID == key) {
				return;
			}
			childRequestContext.addInfo(key, value);
		});
		return childRequestContext;
	}

}
