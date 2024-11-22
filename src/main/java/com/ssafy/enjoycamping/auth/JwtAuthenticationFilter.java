package com.ssafy.enjoycamping.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.JwtAuthenticationException;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.model.TokenType;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.user.util.JwtPayload;
import com.ssafy.enjoycamping.user.util.JwtProvider;
import com.ssafy.enjoycamping.user.util.JwtProvider.TokenPair;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Optional<String> accessToken = getTokenFromCookie(request, "accessToken");
        Optional<String> refreshToken = getTokenFromCookie(request, "refreshToken");

        if (accessToken.isPresent()) {
            ParsedToken parsedAccessToken = jwtProvider.parseToken(accessToken.get());

            switch (parsedAccessToken.getState()) {
                case VALID -> {
                    String userId = (String)parsedAccessToken.getClaims().get("id");
                    issueNewTokens(response, userId);
                    filterChain.doFilter(request, response);
                    filterChain.doFilter(request, response);
                    return;
                }
                case EXPIRED -> {
                    if (refreshToken.isPresent()) {
                        handleRefreshToken(refreshToken.get(), response);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    filterChain.doFilter(request, response);
                    return;
                }
                case INVALID -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } else if (refreshToken.isPresent()) {
            handleRefreshToken(refreshToken.get(), response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
    }
	
    
	private Optional<String> getTokenFromCookie(HttpServletRequest request, String tokenName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> tokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
	private void handleRefreshToken(String refreshToken, HttpServletResponse response) {
        ParsedToken parsedRefreshToken = jwtProvider.parseToken(refreshToken);

        if (parsedRefreshToken.getState() == JwtProvider.TokenState.VALID) {
            String userId = parsedRefreshToken.getClaims().getSubject();

            // Redis에 저장된 Refresh Token과 비교
            String storedToken = jwtProvider.getStoredRefreshToken(Integer.parseInt(userId));
            if (refreshToken.equals(storedToken)) {
                issueNewTokens(response, userId);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
	
	private void issueNewTokens(HttpServletResponse response, String userId) {
        String newAccessToken = JwtProvider.createAccessToken(JwtPayload.builder()
				.id(Integer.parseInt(userId))
				.issuedAt(new Date())
				.tokenType(TokenType.ACCESS)
				.build());
        String newRefreshToken = JwtProvider.createAccessToken(JwtPayload.builder()
				.id(Integer.parseInt(userId))
				.issuedAt(new Date())
				.tokenType(TokenType.REFRESH)
				.build());

        jwtProvider.saveRefreshToken(Integer.parseInt(userId), newRefreshToken);

        Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
        accessTokenCookie.setHttpOnly(true);
        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
	
    private void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);//쿠키의 유효시간 7일
        response.addCookie(cookie);
    }
    
    
}
