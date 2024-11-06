package com.ssafy.enjoycamping.user.dto;

import lombok.*;

public class LoginDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	public static class RequestLoginDto {
		private String email;
		private String password;
	}
	
	@Getter
	@Setter
	@ToString
	@Builder
	public static class ResponseLoginDto {
		private String accessToken;
		private String refreshToken;
	}
}
