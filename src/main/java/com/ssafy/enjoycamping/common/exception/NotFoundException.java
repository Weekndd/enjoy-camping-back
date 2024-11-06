package com.ssafy.enjoycamping.common.exception;

import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {
    public NotFoundException(BaseResponseStatus status) {
        super(status);
    }
}
