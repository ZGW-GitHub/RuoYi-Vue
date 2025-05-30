/*
 * Copyright (C) <2023> <Snow>
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

package com.ruoyi.business.common.web.exception;

import com.ruoyi.business.common.web.exception.code.ExceptionCode;
import lombok.Getter;

/**
 * @author Snow
 * @date 2023/5/22 21:39
 */
@Getter
public class Exception extends RuntimeException {

	/**
	 * 业务错误码
	 *
	 * @see ExceptionCode#getCode()
	 */
	private final Integer code;

	/**
	 * 错误提示
	 *
	 * @see ExceptionCode#getMessage()
	 */
	private final String message;

	public Exception(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());

		this.code = exceptionCode.getCode();
		this.message = exceptionCode.getMessage();
	}

	public Exception(ExceptionCode exceptionCode, String message) {
		super(message);

		this.code = exceptionCode.getCode();
		this.message = message;
	}

	public Exception(ExceptionCode exceptionCode, Throwable cause) {
		super(exceptionCode.getMessage(), cause);

		this.code = exceptionCode.getCode();
		this.message = exceptionCode.getMessage();
	}

	public Exception(ExceptionCode exceptionCode, String message, Throwable cause) {
		super(message, cause);

		this.code = exceptionCode.getCode();
		this.message = message;
	}

}
