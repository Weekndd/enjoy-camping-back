package com.ssafy.enjoycamping.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ssafy.enjoycamping.user.dto.UserDto;
import com.ssafy.enjoycamping.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final UserService userService;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		int userId = Integer.parseInt(username);
		UserDto loginUser = UserDto.builder()
				.id(userId)
				.build();
		//UserDetails의 구현체인 UserPrincipal객체 반환
		return UserPrincipal.of(loginUser);
	}
}
