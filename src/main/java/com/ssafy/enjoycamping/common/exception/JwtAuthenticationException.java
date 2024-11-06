package com.ssafy.enjoycamping.common.exception;

import com.ssafy.enjoycamping.common.response.BaseResponseStatus;

public class JwtAuthenticationException extends BaseException {
    public JwtAuthenticationException(BaseResponseStatus status) {super(status);}
}
