package com.tongwei.common.exception;

import com.tongwei.common.model.ResultCode;

/**
 * @author yangz
 * @date 2018年1月16日 上午11:07:22
 * @description 认证异常
 */
public class AuthenticationExcption extends ApplicationException {

    private static final long serialVersionUID = 5214146953001236471L;

    public static final String MESSAGE = "用户认证异常";

    public AuthenticationExcption() {
        super(MESSAGE);
    }

    public AuthenticationExcption(String message) {
        super(message);
        this.code = ResultCode.AUTHENTICATION_ERROR;
    }

    public AuthenticationExcption(int code, String message) {
        super(message);
        this.code = code;
    }

    public AuthenticationExcption(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.AUTHENTICATION_ERROR;
    }

    public AuthenticationExcption(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AuthenticationExcption(Throwable cause) {
        super(cause);
        this.code = ResultCode.AUTHENTICATION_ERROR;
    }
}
