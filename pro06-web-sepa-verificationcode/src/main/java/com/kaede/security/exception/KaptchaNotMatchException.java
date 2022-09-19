package com.kaede.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author kaede
 * @create 2022-09-19
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
