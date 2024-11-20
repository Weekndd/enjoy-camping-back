package com.ssafy.enjoycamping.user.util;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtProvider jwtProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		 // 1. Authorization 헤더에서 토큰 추출
        String token = jwtProvider.get

        // 2. 토큰 검증
        if (token != null && validateToken(token)) {
            // 3. 토큰에서 사용자 정보 추출 및 인증 객체 생성
            var authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 4. 다음 필터로 이동
        filterChain.doFilter(request, response);
		
	}
}
