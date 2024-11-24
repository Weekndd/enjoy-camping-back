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
                    String userId = String.valueOf(parsedAccessToken.getClaims().get("id"));
                    Authentication authentication = jwtProvider.getAuthentication(userId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    issueNewTokens(response, userId);
                    filterChain.doFilter(request, response);
                    return;
                }
                case EXPIRED -> { //accessToken 만료
                    if (refreshToken.isPresent()) { //RefreshToken 비어있는 경우
                    	log.warn("accessToken 만료됨");
                        handleRefreshToken(refreshToken.get(), response);
                        filterChain.doFilter(request, response);
                        return;
                    } 
                    else { //accessToken 만료되고 refreshToken 비어있는 경우
                    	log.error("accessToken만료, refreshToken 비어있음");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        filterChain.doFilter(request, response);
                        return;
                    }
                    
                }
                case INVALID -> {
                	log.error("accessToken 유효하지 않음");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } 
        else if (refreshToken.isPresent()) { //accessToken부재 RefreshToken 존재
            handleRefreshToken(refreshToken.get(), response);
        } 
        else {
        	//accessToken과 refreshToken 둘 다 없는 경우
            response.setStatus(HttpServletResponse.SC_OK);
        }
        filterChain.doFilter(request, response);
    }
	
	private void handleRefreshToken(String refreshToken, HttpServletResponse response) {
        ParsedToken parsedRefreshToken = jwtProvider.parseToken(refreshToken);

        switch(parsedRefreshToken.getState()) {
        	case VALID -> { //refreshToken 유효한 경우
        		String userId = String.valueOf(parsedRefreshToken.getClaims().get("id"));
                // Redis에 저장된 Refresh Token과 비교
                if(jwtProvider.checkStoredRefreshToken(userId, refreshToken)) {
                    issueNewTokens(response, userId);
                    log.warn("accessToken이 갱신됨");
                    Authentication authentication = jwtProvider.getAuthentication(userId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } 
                else {
                	log.error("refreshToken이 저장된refreshToken과 다름");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
        	}
        	case EXPIRED -> {
        		log.error("accessToken, refreshToken 둘 다 만료 됨");
        		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	}
        	case INVALID -> {
        		log.error("accessToken 만료, refreshToken 유효하지 않음");
        		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	}
        } 
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
	private void issueNewTokens(HttpServletResponse response, String userId) {
        String newAccessToken = jwtProvider.createAccessToken(JwtPayload.builder()
				.id(Integer.parseInt(userId))
				.issuedAt(new Date())
				.tokenType(TokenType.ACCESS)
				.build());
        String newRefreshToken = jwtProvider.createRefreshToken(JwtPayload.builder()
				.id(Integer.parseInt(userId))
				.issuedAt(new Date())
				.tokenType(TokenType.REFRESH)
				.build());

        jwtProvider.saveRefreshToken(Integer.parseInt(userId), newRefreshToken);

        Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
