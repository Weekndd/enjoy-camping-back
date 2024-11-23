package com.ssafy.enjoycamping.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ssafy.enjoycamping.user.dto.UserDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE) //생성자 private으로 생성
public class UserPrincipal implements UserDetails {
	private UserDto userDto; 
	
	public static UserPrincipal of(UserDto userDto) {//생성자 대신 of메서드 강제
		return new UserPrincipal(userDto);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//TODO: 관리자 페이지, 관리자 기능이 생기면 추가해줄것
		//현재는 토큰이 있다면 User로 추정함
		return Collections.emptyList(); 
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userDto.getEmail();
	}
	
}
