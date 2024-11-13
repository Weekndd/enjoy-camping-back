package com.ssafy.enjoycamping.user.util;

import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
	@Autowired
	private static RedisTemplate<String, String> redisTemplate;
	
//    public JwtProvider(RedisTemplate<String, String> redisTemplate) {
//		this.redisTemplate = redisTemplate;
//	}

	@Value("${spring.application.name}")
    private String issuerConfig;

    @Value("${security.jwt.access-token.expiration}")
    private long accessTokenExpirationConfig;

    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshTokenExpirationConfig;

    @Value("${security.jwt.secret-key}")
    private String secretKeyConfig;

    private static String ISSUER;
    private static long ACCESS_TOKEN_EXPIRATION;
    private static long REFRESH_TOKEN_EXPIRATION;
    private static SecretKey SECRET_KEY;

    @PostConstruct
    public void init() {
        ISSUER = issuerConfig;
        ACCESS_TOKEN_EXPIRATION = accessTokenExpirationConfig;
        REFRESH_TOKEN_EXPIRATION = refreshTokenExpirationConfig;
        SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyConfig));
    }

    public static String createAccessToken(JwtPayload jwtPayload){
        return Jwts.builder()
                .claim("id", jwtPayload.getId())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();
    }

    public static String createRefreshToken(JwtPayload jwtPayload) {
    	String refreshToken = Jwts.builder()
                .claim("id", jwtPayload.getId())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();

    	
        return refreshToken;
    }

    public static int getUserId() throws BaseException {
        return verifyToken().getId();
    }

    private static JwtPayload verifyToken() throws BaseException {
        String token = getToken();
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(SECRET_KEY).build()
                    .parseSignedClaims(token);

            return new JwtPayload(claimsJws.getPayload().get("id", Integer.class), claimsJws.getPayload().getIssuedAt());
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
