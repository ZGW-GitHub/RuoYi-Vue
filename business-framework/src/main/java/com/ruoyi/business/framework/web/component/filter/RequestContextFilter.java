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

package com.ruoyi.business.framework.web.component.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.business.framework.web.request.RequestContextEnum;
import com.ruoyi.business.framework.web.request.RequestContextHelper;
import com.ruoyi.business.framework.web.util.MDCUtil;
import com.ruoyi.business.framework.web.util.RequestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;

/**
 * @author Snow
 * @date 2023/7/1 21:48
 */
@Slf4j
@Order(0)
@Component("customRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestUrl = RequestUtil.getRequestUrl(request);

		// 0、白名单放行
		if (RequestUtil.isWhiteList(RequestUtil.WHITE_LIST, requestUrl)) {
			log.debug("【 Trace Ignore 】请求: {}", requestUrl);

			// 继续执行 Filter 链
			filterChain.doFilter(request, response);
			return;
		}

		// 1、生成/获取 traceId
		String traceId = request.getParameter(RequestContextEnum.TRACE_ID.getCode());
		if (StrUtil.isBlank(traceId)) {
			traceId = MDCUtil.generateTraceId();
		}

		try {
			// 2、启动 Trace
			RequestContextHelper.startContext(traceId, true);
			log.debug("【 Trace 开始 】请求: {}", requestUrl);

			// 3、继续执行 Filter 链
			filterChain.doFilter(request, response);
		} finally {
			// 4、清除 Trace
			log.debug("【 Trace 清除 】请求: {}", requestUrl);
			RequestContextHelper.clear(true);
		}
	}

}
