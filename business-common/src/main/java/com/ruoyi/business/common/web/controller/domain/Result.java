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

package com.ruoyi.business.common.web.controller.domain;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.ruoyi.business.common.web.exception.code.ExceptionCode;
import com.ruoyi.business.framework.web.request.RequestContextHelper;
import com.ruoyi.business.common.web.exception.Exception;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 愆凡
 * @date 2022/6/13 22:07
 */
@Slf4j
@Getter
public class Result<T> implements Serializable {

	/**
	 * 返回码
	 */
	private Integer code;

	/**
	 * 返回码说明
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private T data;

	/**
	 * 链路 ID
	 */
	private String traceId;

	/**
	 * 时间戳
	 */
	private final LocalDateTime timestamp = LocalDateTime.now();

	private Result() {
	}

	public static Result<String> success() {
		return success("");
	}

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.code = HttpStatus.HTTP_OK;
		result.message = StrUtil.EMPTY;
		result.data = data;
		result.traceId = RequestContextHelper.getTraceId();
		return result;
	}

	public static <T> Result<T> error(ExceptionCode exceptionCode) {
		return error(exceptionCode, exceptionCode.getMessage());
	}

	public static <T> Result<T> error(ExceptionCode exceptionCode, String message) {
		Result<T> result = new Result<>();
		result.code = exceptionCode.getCode();
		result.message = message;
		result.traceId = RequestContextHelper.getTraceId();
		return result;
	}

	public static <T> Result<T> error(Exception exception) {
		return error(exception, exception.getMessage());
	}

	public static <T> Result<T> error(Exception exception, String message) {
		Result<T> result = new Result<>();
		result.code = exception.getCode();
		result.message = message;
		result.traceId = RequestContextHelper.getTraceId();
		return result;
	}

}
