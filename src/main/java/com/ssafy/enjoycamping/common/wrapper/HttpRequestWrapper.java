package com.ssafy.enjoycamping.common.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

public class HttpRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestWrapper.class);
    private final byte[] cachedInputStream;

    public HttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream inputStream = request.getInputStream();
        this.cachedInputStream = StreamUtils.copyToByteArray(inputStream);
    }

    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private final InputStream cachedBodyInputStream;

            {
                this.cachedBodyInputStream = new ByteArrayInputStream(HttpRequestWrapper.this.cachedInputStream);
            }

            public boolean isFinished() {
                try {
                    return this.cachedBodyInputStream.available() == 0;
                } catch (IOException var2) {
                    HttpRequestWrapper.log.error("HttpRequestWrapper Error", var2);
                    return false;
                }
            }

            public boolean isReady() {
                return true;
            }

            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }

            public int read() throws IOException {
                return this.cachedBodyInputStream.read();
            }
        };
    }
}
