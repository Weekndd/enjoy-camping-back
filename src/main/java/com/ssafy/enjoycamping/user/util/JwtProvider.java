package com.ssafy.enjoycamping.user.util;

import java.security.Key;
import java.time.Duration;
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
import com.ssafy.enjoycamping.common.exception.JwtAuthenticationException;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.model.TokenType;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;

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
import jakarta.servlet.http.HttpServletResponse;
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
    
    public static String createAccessToken(JwtPayload jwtPayload){
        return Jwts.builder()
                .claim("id", jwtPayload.getId())
                .claim("tokenType", jwtPayload.getTokenType())
                .issuer(ISSUER)
                .issuedAt(jwtPayload.getIssuedAt())
                .expiration(new Date(jwtPayload.getIssuedAt().getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();
    }

    public static String createRefreshToken(JwtPayload jwtPayload) {
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
    
    public static void deleteRefreshToken(int userId) {
        REDIS_TEMPLATE.delete(userId);
    }
    
    // 없앨 거
	public static int getAuthenticatedUserId(TokenType tokenType) throws BaseException {
    	String token = getToken(); //헤더에서 토큰 가져옴
    	JwtPayload payload = verifyToken(token); 
    	Integer userId = payload.getId();
    	TokenType nowTokenType = payload.getTokenType();
    	
    	if(nowTokenType != tokenType) { //현재 토큰 타입이 필요로 하는 토큰 타입과 맞지 않다면 에러
    		throw new UnauthorizedException(BaseResponseStatus.INVALID_JWT);
    	}
    	
    	if(tokenType == TokenType.REFRESH) verifyRefreshToken(token, userId);
    	return userId;
    }
    
	// 없앨 거
    private static void verifyRefreshToken(String token, int userId) throws BaseException {
    	String storedToken = REDIS_TEMPLATE.opsForValue().get(userId);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new UnauthorizedException(BaseResponseStatus.INVALID_JWT);
        }
    }
    
    
    //////////수정 후
    public static String getToken() throws BaseException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String accessToken = request.getHeader("X-ACCESS-TOKEN");
        return accessToken;
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
    

    private static JwtPayload verifyToken(String token) throws BaseException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(SECRET_KEY).build()
                    .parseSignedClaims(token);
            String tokenType = claimsJws.getPayload().get("tokenType",String.class);
            return new JwtPayload(
            		claimsJws.getPayload().get("id", Integer.class),
            		claimsJws.getPayload().getIssuedAt(),
            		TokenType.valueOf(tokenType));
        } catch (Exception e) {
            throw e;
        }
    }
    
    // Refresh Token 검증 후 새 토큰 재발급
    public TokenPair reissueTokens(String refreshToken) {
    	//RefreshToken이 만료된 경우 예외발생
    	try {
    		isValidToken(refreshToken, true); //유효하지 않으면 에러발생
    		
    		int userId = getUserIdByToken(refreshToken); //페이로드를 만들기 위한 userId
	        String storedToken = REDIS_TEMPLATE.opsForValue().get(userId);
	        if(checkStoredRefreshToken(userId, refreshToken)) {
//	        	throw new 
	        }
	        
	        String newAccessToken = createAccessToken(JwtPayload.builder()
					.id(userId)
					.issuedAt(new Date())
					.tokenType(TokenType.ACCESS)
					.build());
	        String newRefreshToken = JwtProvider.createRefreshToken(JwtPayload.builder()
					.id(userId)
					.issuedAt(new Date())
					.build());
	        TokenPair tokenPair = new TokenPair(newAccessToken, newRefreshToken);
	    	return tokenPair;
    	} 
    	catch (Exception e) {
    		throw e;
    	}
    }
    
    
    private int getUserIdByToken(String token) {
    	Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
    	int userId = Integer.parseInt(claimsJws.getPayload().getId());
    	return userId;
    }
    
    private boolean checkStoredRefreshToken(int userId, String refreshToken) {
    	String storedToken = REDIS_TEMPLATE.opsForValue().get(userId);
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
    
    public Claims getClaims(String token) throws JwtException {
        return (Claims) Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
    }
	
	public void saveRefreshToken(int userId, String newRefreshToken) {
		Duration ttl = Duration.ofMillis(REFRESH_TOKEN_EXPIRATION);
		REDIS_TEMPLATE.opsForValue().set(userId, newRefreshToken, ttl);
	}
	
	public String getStoredRefreshToken(int userId) {
		return REDIS_TEMPLATE.opsForValue().get(userId);
	}
    
}
