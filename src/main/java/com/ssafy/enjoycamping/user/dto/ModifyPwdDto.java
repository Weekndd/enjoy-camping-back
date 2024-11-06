package com.ssafy.enjoycamping.user.dto;

import lombok.*;

public class ModifyPwdDto {

	@Getter
	@Setter
	@ToString
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RequestModifyPwdDto {
		private String password;
		private String newPassword;
	}
	
	@Getter
	@Setter
	@ToString
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ResponseModifyPwdDto {
		private int id;
	}
}
