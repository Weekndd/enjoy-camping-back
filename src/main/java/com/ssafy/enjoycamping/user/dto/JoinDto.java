package com.ssafy.enjoycamping.user.dto;

import lombok.*;

public class JoinDto {
	
	@Getter
	@Setter
	@ToString
	@Builder
	public static class RequestJoinDto {
		private String name;
		private String email;
		private String password;
	}
	
	@Getter
	@Setter
	@ToString
	@Builder
	public static class ResponseJoinDto {
		private int id;
	}
}
