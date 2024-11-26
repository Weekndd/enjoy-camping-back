package com.ssafy.enjoycamping.common.initializer;

import com.ssafy.enjoycamping.common.model.TokenType;
import com.ssafy.enjoycamping.user.util.JwtPayload;
import com.ssafy.enjoycamping.user.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInitializer {

    private final JwtProvider jwtProvider;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeJwt() {
        try {
            JwtPayload payload = JwtPayload.builder()
                    .id(1) // 테스트용 ID
                    .issuedAt(new Date())
                    .tokenType(TokenType.ACCESS)
                    .build();

            // 테스트 토큰 생성
            String testToken = jwtProvider.createAccessToken(payload);

            // 테스트 토큰 파싱
            jwtProvider.parseToken(testToken);

            log.info("JWT initialization completed successfully.");
        } catch (Exception e) {
            log.error("JWT initialization failed: {}", e.getMessage());
        }
    }
}
