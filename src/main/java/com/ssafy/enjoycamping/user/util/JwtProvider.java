package com.ssafy.enjoycamping.user.util;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.model.TokenType;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {
	
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

    public static String createAccessToken(JwtPayload jwtPayload){
        return Jwts.builder()
                .claim("id", jwtPayload.getId())
                .claim("token_type", jwtPayload.getTokenType())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();
    }

    public static String createRefreshToken(JwtPayload jwtPayload) {
    	String refreshToken = Jwts.builder()
                .claim("id", jwtPayload.getId())
                .claim("token_type", jwtPayload.getTokenType())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();

    	Duration ttl = Duration.ofMillis(REFRESH_TOKEN_EXPIRATION);
    	REDIS_TEMPLATE.opsForValue().set(jwtPayload.getId(), refreshToken, ttl);
        return refreshToken;
    }
    
    public static void deleteRefreshToken(int userId) {
        REDIS_TEMPLATE.delete(userId);
    }
    
    public static int getAuthenticatedUserId(TokenType tokenType) throws BaseException {
    	String token = getToken();
    	JwtPayload payload = verifyToken(token);
    	Integer userId = payload.getId();
    	TokenType t = payload.getTokenType();
    	
    	if(t != tokenType) // error
    	
    	if(tokenType == TokenType.REFRESH) verifyRefreshToken(token, userId);
    	return userId;
    }
    
    private static void verifyRefreshToken(String token, int userId) throws BaseException {
    	String storedToken = REDIS_TEMPLATE.opsForValue().get(userId);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new UnauthorizedException(BaseResponseStatus.INVALID_JWT);
        }
    }

    private static JwtPayload verifyToken(String token) throws BaseException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(SECRET_KEY).build()
                    .parseSignedClaims(token);

            return new JwtPayload(claimsJws.getPayload().get("id", Integer.class), claimsJws.getPayload().getIssuedAt(),TokenType.);
        } catch (Exception e) {
            throw new UnauthorizedException(BaseResponseStatus.INVALID_JWT);
        }
    }
    
    private static String getToken() throws BaseException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = request.getHeader("X-ACCESS-TOKEN");

        if (accessToken == null || accessToken.length() == 0)
            throw new UnauthorizedException(BaseResponseStatus.EMPTY_JWT);
        return accessToken;
    }
}
