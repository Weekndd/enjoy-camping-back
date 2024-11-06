package com.ssafy.enjoycamping.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Builder
@ToString
@Alias("user")
public class User {
	int id;
	String email;
	String password;
	String name;
	boolean deleteFlag;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}