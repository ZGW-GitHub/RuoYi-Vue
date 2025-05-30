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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 愆凡
 * @date 2022/6/13 16:49
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum RequestContextEnum {

	/**
	 * 链路 ID
	 */
	TRACE_ID("TRACE_ID"),

	/**
	 * 当前用户信息：用户ID
	 */
	// USER_ID("USER_ID"),

	/**
	 * 当前用户信息：用户姓名
	 */
	// USER_NAME("USER_NAME"),
	;

	private final String code;

}
