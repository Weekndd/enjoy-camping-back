package com.ssafy.enjoycamping.user.util;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ssafy.enjoycamping.auth.ParsedToken;
import com.ssafy.enjoycamping.common.exception.BaseException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {
	private final UserDetailsService userDetailsService;
	
	@Value("${spring.application.name}")
    private String issuerConfig;

    @Value("${security.jwt.access-token.expiration}")
    private long accessTokenExpirationConfig;

    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshTokenExpirationConfig;

    @Value("${security.jwt.secret-key}")
    private String secretKeyConfig;
    
    @Autowired
    private RedisTemplate<Integer, String> redisTemplate;

    private static String ISSUER;
    private static long ACCESS_TOKEN_EXPIRATION;
    private static long REFRESH_TOKEN_EXPIRATION;
    private static SecretKey SECRET_KEY;
    private static RedisTemplate<Integer, String> REDIS_TEMPLATE;

    @PostConstruct
    public void init() {
        ISSUER = issuerConfig;
        ACCESS_TOKEN_EXPIRATION = accessTokenExpirationConfig;
        REFRESH_TOKEN_EXPIRATION = refreshTokenExpirationConfig;
        SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyConfig));
        REDIS_TEMPLATE = redisTemplate;
    }

    public enum TokenState {
        VALID,
        EXPIRED,
        INVALID
    }
    // 새 AccessToken과 RefreshToken을 담는 DTO
    public record TokenPair(String accessToken, String refreshToken) {}
    
    public String createAccessToken(JwtPayload jwtPayload){
        String token = Jwts.builder()
                .claim("id", jwtPayload.getId())
                .claim("tokenType", jwtPayload.getTokenType())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();
        return token;
    }

    public String createRefreshToken(JwtPayload jwtPayload) {
        String refreshToken = Jwts.builder()
                .claim("id", jwtPayload.getId())
                .claim("tokenType", jwtPayload.getTokenType())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();

    	Duration ttl = Duration.ofMillis(REFRESH_TOKEN_EXPIRATION);
        //Redis에 RefreshToken저장
    	REDIS_TEMPLATE.opsForValue().set(jwtPayload.getId(), refreshToken, ttl);
        return refreshToken;
    }
    
    public void deleteRefreshToken(int userId) {
        REDIS_TEMPLATE.delete(userId);
    }
    
	public int getAuthenticatedUserId() throws BaseException {
    	Optional<String>token = getAccessToken(); //쿠키에서 토큰 가져옴
    	int userId =  Jwts.parser()
    			.verifyWith(SECRET_KEY)
    			.build()
    			.parseSignedClaims(token.get())
    			.getPayload()
    			.get("id",Integer.class);
    	return userId;
    }
    private Optional<String> getAccessToken() throws BaseException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
    
    public boolean isValidToken(String token, boolean isRefreshToken) {
    	 try {
    		 Jwts.parser()
             .verifyWith(SECRET_KEY)
             .build()
             .parseSignedClaims(token);
             return true;
         } catch (ExpiredJwtException e) {
             // 토큰이 만료된 경우
             if (!isRefreshToken) throw e; // Access Token이 만료된 경우는 별도 처리
             return false;
         } catch (JwtException | IllegalArgumentException e) {
             return false;
         }
    }

    public Authentication getAuthentication(String userId) {
        UserDetails principal = userDetailsService.loadUserByUsername(userId);
        //TODO: admin API를 만들었을 때는 UserDetail 구현체의 getAuthorities()를 통해 권한을 가져올 것
        //DB User테이블에 Role 컬럼 추가해야함..
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    
    public boolean checkStoredRefreshToken(String userId, String refreshToken) {
    	String storedToken = REDIS_TEMPLATE.opsForValue().get(Integer.parseInt(userId));
    	if(refreshToken.equals(storedToken)) {
    		return true;
    	}
    	return false;
    }
    
    public ParsedToken parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return new ParsedToken(TokenState.VALID, claims);
        } catch (ExpiredJwtException e) {
            return new ParsedToken(TokenState.EXPIRED, e.getClaims());
        } catch (JwtException | IllegalArgumentException e) {
            return new ParsedToken(TokenState.INVALID, null);
        }
    }
    
	public void saveRefreshToken(int userId, String newRefreshToken) {
		Duration ttl = Duration.ofMillis(REFRESH_TOKEN_EXPIRATION);
		REDIS_TEMPLATE.opsForValue().set(userId, newRefreshToken, ttl);
	}
}
