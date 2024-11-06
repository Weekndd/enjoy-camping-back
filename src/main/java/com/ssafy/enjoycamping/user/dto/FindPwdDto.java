package com.ssafy.enjoycamping.user.dto;

import lombok.*;

public class FindPwdDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RequestFindPwdDto {
		private String email;
		private String name;
	}
	
	@Getter
	@Setter
	@ToString
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResponseFindPwdDto {
		private String password;
	}
}
