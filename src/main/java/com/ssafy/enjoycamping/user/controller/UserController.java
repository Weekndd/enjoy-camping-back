package com.ssafy.enjoycamping.user.controller;

import java.util.List;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoycamping.user.dto.FindPwdDto;
import com.ssafy.enjoycamping.user.dto.JoinDto;
import com.ssafy.enjoycamping.user.dto.LoginDto;
import com.ssafy.enjoycamping.user.dto.ModifyPwdDto;
import com.ssafy.enjoycamping.user.dto.UserDto;

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
    @PostMapping("login")
    public BaseResponse<LoginDto.ResponseLoginDto> login(HttpServletRequest httpRequest,
    		HttpServletResponse httpResponse,
    		@RequestBody LoginDto.RequestLoginDto request){
        LoginDto.ResponseLoginDto response = userService.login(request);
        
        Cookie accessTokenCookie = new Cookie("accessToken", response.getAccessToken());
        Cookie refreshTokenCookie = new Cookie("refreshToken", response.getRefreshToken());
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");

        httpResponse.addCookie(accessTokenCookie);
        httpResponse.addCookie(refreshTokenCookie);
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
    @GetMapping("/{index}")
    public BaseResponse<UserDto> getUser(@PathVariable("index") int index){
        UserDto user = userService.getUser(index);
        return new BaseResponse<>(user);
    }
    
    /**
     * 비밀번호 변경
     */
    @PatchMapping("/updatePwd")
    public BaseResponse<ModifyPwdDto.ResponseModifyPwdDto> modifyPassword(@RequestBody ModifyPwdDto.RequestModifyPwdDto request){
        ModifyPwdDto.ResponseModifyPwdDto response = userService.modifyPassword(request);
    	return new BaseResponse<>(response);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public BaseResponse logout(){
        userService.logout();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    /**
     * 탈퇴
     */
    @PatchMapping("/delete")
    public BaseResponse withdraw(){
    	userService.withdraw();
    	return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
