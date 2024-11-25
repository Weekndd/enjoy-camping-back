package com.ssafy.enjoycamping.user.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoycamping.auth.UserPrincipal;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.user.dto.FindPwdDto;
import com.ssafy.enjoycamping.user.dto.JoinDto;
import com.ssafy.enjoycamping.user.dto.LoginDto;
import com.ssafy.enjoycamping.user.dto.ModifyPwdDto;
import com.ssafy.enjoycamping.user.dto.UserDto;
import com.ssafy.enjoycamping.user.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "2. USER")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    /**
     * 회원가입
     */
    @PostMapping("/join")
    public BaseResponse<JoinDto.ResponseJoinDto> join(@RequestBody JoinDto.RequestJoinDto request){
        JoinDto.ResponseJoinDto response = userService.join(request);
        return new BaseResponse<>(response);
    }
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<LoginDto.ResponseLoginDto> login(HttpServletRequest httpRequest,
    		HttpServletResponse httpResponse,
    		@RequestBody LoginDto.RequestLoginDto request){
        LoginDto.ResponseLoginDto response = userService.login(request);

        Cookie accessTokenCookie = setCookie("accessToken", response.getAccessToken(), true, "/");
        Cookie refreshTokenCookie = setCookie("refreshToken", response.getRefreshToken(), true, "/");
        Cookie authStateCookie = setCookie("isAuthenticated", "true", false, "/");

        httpResponse.addCookie(accessTokenCookie);
        httpResponse.addCookie(refreshTokenCookie);
        httpResponse.addCookie(authStateCookie);

        return new BaseResponse<>(response);
    }
    
    /**
     * 비밀번호 찾기
     */
    @PostMapping("/findPwd")
    public BaseResponse<FindPwdDto.ResponseFindPwdDto> findPassword(@RequestBody FindPwdDto.RequestFindPwdDto request){
        FindPwdDto.ResponseFindPwdDto response = userService.findPassword(request);
        return new BaseResponse<>(response);
    }

    /**
     * 전체 조회
     */
    @GetMapping
    public BaseResponse<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getUsers();
        return new BaseResponse<>(users);
    }
    
    /**
     * 단건 조회
     */
    @GetMapping("/detail")
    public BaseResponse<UserDto> getUser(Authentication authentication){
    	if(authentication == null) throw  new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        UserDto user = userService.getUser(userPrincipal.getUserId());
        return new BaseResponse<>(user);
    }
    
    /**
     * 비밀번호 변경
     */
    @PatchMapping("/updatePwd")
    public BaseResponse<ModifyPwdDto.ResponseModifyPwdDto> modifyPassword(Authentication authentication, @RequestBody ModifyPwdDto.RequestModifyPwdDto request){
    	if(authentication == null) throw new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        ModifyPwdDto.ResponseModifyPwdDto response = userService.modifyPassword(userPrincipal.getUserId(), request);
    	return new BaseResponse<>(response);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public BaseResponse logout(HttpServletResponse httpResponse, Authentication authentication){
    	if(authentication == null) throw new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Cookie accessTokenCookie = setCookie("accessToken", null, true, "/");
        Cookie refreshTokenCookie = setCookie("refreshToken", null, true, "/");
        Cookie authStateCookie = setCookie("isAuthenticated", "false", false, "/");

        accessTokenCookie.setMaxAge(0);
        refreshTokenCookie.setMaxAge(0);

        httpResponse.addCookie(accessTokenCookie);
        httpResponse.addCookie(refreshTokenCookie);
        httpResponse.addCookie(authStateCookie);

        userService.logout(userPrincipal.getUserId());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    /**
     * 탈퇴
     */
    @PatchMapping("/delete")
    public BaseResponse withdraw(HttpServletResponse httpResponse, Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Cookie accessTokenCookie = setCookie("accessToken", null, true, "/");
        Cookie refreshTokenCookie = setCookie("refreshToken", null, true, "/");
        Cookie authStateCookie = setCookie("isAuthenticated", "false", false, "/");

        accessTokenCookie.setMaxAge(0);
        refreshTokenCookie.setMaxAge(0);

        httpResponse.addCookie(accessTokenCookie);
        httpResponse.addCookie(refreshTokenCookie);
        httpResponse.addCookie(authStateCookie);

        userService.withdraw(userPrincipal.getUserId());
    	return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    private static Cookie setCookie(String name, String value, boolean httpOnly, String path){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath(path);
        return cookie;
    }
}
