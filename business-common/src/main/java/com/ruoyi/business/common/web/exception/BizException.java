package com.ruoyi.business.common.web.exception;

import com.ruoyi.business.common.web.exception.code.BizExceptionCode;

/**
 * @author Snow
 */
public class BizException extends Exception {

    public BizException(BizExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public BizException(BizExceptionCode exceptionCode, String message) {
        super(exceptionCode, message);
    }

    public BizException(BizExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode, cause);
    }

    public BizException(BizExceptionCode exceptionCode, String message, Throwable cause) {
        super(exceptionCode, message, cause);
    }

}
