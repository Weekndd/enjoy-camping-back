package com.ssafy.enjoycamping.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.enjoycamping.user.util.JwtAuthenticationFilter;
import com.ssafy.enjoycamping.user.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui.html", // 인증 없이 접근 가능
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 권한 필요
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
            .formLogin(formLogin -> formLogin.disable()) // 기본 로그인 폼 비활성화
            .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), OncePerRequestFilter.class); // JWT 필터 추가
        
        return http.build();
    }
}
