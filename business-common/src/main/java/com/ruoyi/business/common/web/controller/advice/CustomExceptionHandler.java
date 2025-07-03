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

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.business.common.domain.resp.Result;
import com.ruoyi.business.common.web.exception.BizException;
import com.ruoyi.business.common.web.exception.Exception;
import com.ruoyi.business.common.web.exception.SysException;
import com.ruoyi.business.common.web.exception.code.BizExceptionCode;
import com.ruoyi.business.common.web.exception.code.SysExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Snow
 */
@Slf4j
@Order(1)
@RestControllerAdvice(basePackages = "com.ruoyi.business")
public class CustomExceptionHandler {

	/**
	 * 异常处理程序
	 *
	 * @param request   请求
	 * @param response  响应
	 * @param throwable 异常
	 * @return {@link Result}<{@link Void}>
	 */
	@ExceptionHandler(Throwable.class)
	public Result<Void> exceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
		// 1、获取 rootThrowable
		Throwable rootThrowable = ExceptionUtil.getRootCause(throwable);

		// 2、打印日志
		log.error("【 异常拦截 】>>>>>> 异常类型：{}", rootThrowable.getClass().getSimpleName());

		// 3、处理异常
		return doExceptionHandler(request, response, rootThrowable, throwable);
	}

	private Result<Void> doExceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable rootThrowable, Throwable originalThrowable) {
		if (rootThrowable instanceof ConstraintViolationException constraintViolationException) {
			// 处理验证异常
			return handleConstraintViolationException(constraintViolationException, originalThrowable);
		}

		if (rootThrowable instanceof Exception customRuntimeException) {
			// 处理自定义 RuntimeException
			return handleCustomRuntimeException(customRuntimeException, originalThrowable);
		}

		if (rootThrowable instanceof java.lang.Exception rootException) {
			// 处理 java.lang.Exception
			return handleException(request, response, rootException, originalThrowable);
		}

		// 处理 java.lang.Throwable
		return handleThrowable(request, response, rootThrowable, originalThrowable);
	}

	private Result<Void> handleConstraintViolationException(ConstraintViolationException rootThrowable, Throwable originalThrowable) {
		log.error("【 异常拦截 】>>>>>> ValidationException : {}", rootThrowable.getMessage(), originalThrowable);

		// return Result.error(new BizException(BizExceptionCode.VALIDATION_EXCEPTION, buildViolationMessage(rootThrowable)));
		return Result.error(new BizException(BizExceptionCode.PARAMS_VALIDATION_EXCEPTION));
	}

	private Result<Void> handleCustomRuntimeException(Exception rootThrowable, Throwable originalThrowable) {
		if (rootThrowable instanceof SysException) {
			log.error("【 异常拦截 】>>>>>> 发生系统异常 : {}-{}", rootThrowable.getCode(), rootThrowable.getMessage(), originalThrowable);
			// SysException 为前端返回统一的错误 msg 即：SysExceptionCode.COMMON_ERROR 的 message 属性值
			return Result.error(SysExceptionCode.COMMON_ERROR);
		}

		log.error("【 异常拦截 】>>>>>> 发生业务异常 : {}-{}", rootThrowable.getCode(), rootThrowable.getMessage(), originalThrowable);
		// 业务异常，为前端返回该异常的 message 的属性值
		return Result.error(rootThrowable);
	}

	private Result<Void> handleException(HttpServletRequest request, HttpServletResponse response, java.lang.Exception rootException, Throwable originalThrowable) {
		log.error("【 异常拦截 】>>>>>> 发生 Exception : {}", rootException.getMessage(), originalThrowable);

		// 未知 Exception ，为前端返回统一的错误 msg 即：SysExceptionCode.UNKNOWN_ERROR 的 message 属性值
		return Result.error(SysExceptionCode.UNKNOWN_ERROR);
	}

	private Result<Void> handleThrowable(HttpServletRequest request, HttpServletResponse response, Throwable rootThrowable, Throwable originalThrowable) {
		log.error("【 异常拦截 】>>>>>> 发生 Throwable : {}", rootThrowable.getMessage(), originalThrowable);

		// 未知 Throwable ，为前端返回统一的错误 msg 即：SysExceptionCode.UNKNOWN_ERROR 的 message 属性值
		return Result.error(SysExceptionCode.UNKNOWN_ERROR);
	}

	private String buildViolationMessage(ConstraintViolationException rootThrowable) {
		StringBuilder violationMessage = StrUtil.builder();

		rootThrowable.getConstraintViolations().forEach(constraintViolation -> buildViolationMessage(violationMessage, constraintViolation));

		if (violationMessage.isEmpty()) {
			violationMessage.append(rootThrowable.getMessage());
		} else {
			violationMessage.delete(violationMessage.length() - 2, violationMessage.length());
		}
		return violationMessage.toString();
	}

	private void buildViolationMessage(StringBuilder violationMessage, ConstraintViolation<?> constraintViolation) {
		if (!(constraintViolation instanceof ConstraintViolationImpl<?> violation)) {
			return;
		}

		Class<?> constraintClazz = violation.getLeafBean().getClass();
		violationMessage.append(constraintClazz.getSimpleName()).append(StrUtil.SPACE);
		Path propertyPath = violation.getPropertyPath();
		if (propertyPath instanceof PathImpl path) {
			String propertyName = path.getLeafNode().getName();
			violationMessage.append("的 ").append(propertyName).append(" : ");
		}
		String violationMsg = violation.getMessage();
		violationMessage.append(violationMsg).append(", ");
	}

}
