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

package com.ruoyi.business.common.web.controller.advice;

import com.ruoyi.business.common.domain.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Snow
 */
@Slf4j
@Order(2)
@RestControllerAdvice(basePackages = "com.ruoyi.business")
public class CustomResponseBodyHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(final MethodParameter returnType,
							final Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(@Nullable final Object body,
								  final MethodParameter returnType,
								  final MediaType selectedContentType,
								  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  final ServerHttpRequest request,
								  final ServerHttpResponse response) {

		if (body instanceof Result<?>) {
			return body;
		}
		return Result.success(body);

	}

}
