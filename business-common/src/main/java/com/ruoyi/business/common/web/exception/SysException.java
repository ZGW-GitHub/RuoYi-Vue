package com.ruoyi.business.common.web.exception;

import com.ruoyi.business.common.web.exception.code.SysExceptionCode;

/**
 * @author Snow
 */
public class SysException extends Exception {

	public SysException(SysExceptionCode exceptionCode) {
		super(exceptionCode);
	}

	public SysException(SysExceptionCode exceptionCode, String message) {
		super(exceptionCode, message);
	}

	public SysException(SysExceptionCode exceptionCode, Throwable cause) {
		super(exceptionCode, cause);
	}

	public SysException(SysExceptionCode exceptionCode, String message, Throwable cause) {
		super(exceptionCode, message, cause);
	}

	public SysException(String message) {
		this(SysExceptionCode.COMMON_ERROR, message);
	}

	public SysException(Throwable cause) {
		this(SysExceptionCode.COMMON_ERROR, cause);
	}

	public SysException(String message, Throwable cause) {
		this(SysExceptionCode.COMMON_ERROR, message, cause);
	}

}
