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

package com.ruoyi.business.common.web.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Code 规范：<br/>
 * <ul>
 *     <li>均为 6 位数字</li>
 *     <li>BizExceptionCode 	: 11xxxx</li>
 *     <li>ApiExceptionCode 	: 12xxxx</li>
 *     <li>UserExceptionCode	: 13xxxx</li>
 * </ul>
 *
 * @author 愆凡
 * @date 2022/6/13 21:51
 */
@Getter
@AllArgsConstructor
public enum SysExceptionCode implements ExceptionCode {

	/**
	 * 通用错误码
	 */
	COMMON_ERROR(100001, "服务器繁忙，请联系管理员"),
	UNKNOWN_ERROR(100002, "服务器故障，请联系管理员"),

	/**
	 * 系统异常
	 */
	LIMIT_ERROR(110001, "访问过于频繁，请稍后再试"),
	ILLEGAL_REQUEST(110002, "非法请求，请重新刷新页面操作"),

	/**
	 * Api 异常
	 */
	API_SCAN_PARAM_MORE_THAN_ONE(120001, "API {} - {} ，参数个数大于 1"),
	API_SCAN_REPEAT(120002, "API {} - {} 重复"),
	API_INVOKE_REQUEST_IS_BLACK(120101, "入参 api 为空"),
	API_INVOKE_API_NOT_EXIST(120102, "API {} - {} 不存在"),

	/**
	 * trace 异常
	 */
	REQUEST_CONTEXT_EXCEPTION(130001, "RequestContext 异常"),
	REQUEST_CONTEXT_EXIST(130002, "RequestContext 已存在"),
	REQUEST_CONTEXT_NOT_EXIST(130003, "RequestContext 不存在"),
	TRACE_ID_BLANK(130005, "TraceId 为空"),

	/**
	 * MQ 异常
	 */
	MQ_ROCKETMQ_CREATE_FAIL(190101, "RocketMQ 创建失败"),
	MQ_ROCKETMQ_START_FAIL(190102, "RocketMQ 启动失败"),
	MQ_KAFKA_CREATE_FAIL(190103, "Kafka 创建失败"),
	MQ_KAFKA_START_FAIL(190104, "Kafka 启动失败"),

	/**
	 * 定时任务
	 */
	BAD_XXL_JOB_HANDLER(190201, "定时任务配置错误");

	private final int    code;
	private final String message;

}
