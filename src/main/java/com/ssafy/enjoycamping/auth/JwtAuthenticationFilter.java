package com.ssafy.enjoycamping.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.JwtAuthenticationException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.user.util.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
		
		try {
			 // 1. Authorization 헤더에서 토큰 추출
	        String token = jwtProvider.getToken();
	        // 2. 토큰 검증
	        if (token != null) {
	        	jwtProvider.validateToken(token);
	            // 3. 토큰에서 사용자 정보 추출 및 인증 객체 생성
	            Authentication authentication = jwtProvider.getAuthentication(token);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	        else {
	        	throw new JwtAuthenticationException(BaseResponseStatus.EMPTY_JWT);
	        }
		} catch (BaseException exception) {
			log.error("JwtAuthentication Authentication Exception Occurs! - {}",exception.getClass());
		}
        // 4. 다음 필터로 이동
        filterChain.doFilter(request, response);
		
	}
}
