package com.ssafy.enjoycamping.common.wrapper;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class HttpResponseWrapper extends ContentCachingResponseWrapper {
    public HttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }
}
