package com.ssafy.enjoycamping.user.dto;

import java.time.LocalDateTime;

import com.ssafy.enjoycamping.user.entity.User;
import com.ssafy.enjoycamping.user.util.EncryptionService;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class UserDto {
	int id;
	String email;
	String password;
	String name;
	boolean deleteFlag;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;

	public static UserDto fromEntity(User user){
		return UserDto.builder()
				.id(user.getId())
				.email(EncryptionService.decrypt(user.getEmail()))
				.password(user.getPassword())
				.name(user.getName())
				.deleteFlag(user.isDeleteFlag())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
}