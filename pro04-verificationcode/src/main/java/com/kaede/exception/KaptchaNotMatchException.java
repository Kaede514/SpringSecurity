package com.kaede.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author kaede
 * @create 2022-11-22
 *
 * 自定义验证码认证异常
 */

public class KaptchaNotMatchException extends AuthenticationException {

    public KaptchaNotMatchException(String msg) {
        super(msg);
    }

    public KaptchaNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
