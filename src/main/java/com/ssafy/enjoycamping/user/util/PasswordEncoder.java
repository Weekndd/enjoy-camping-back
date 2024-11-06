package com.ssafy.enjoycamping.user.util;

import java.security.MessageDigest;

import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.InternalServerErrorException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

	public static String encode(String password) throws BaseException {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
		    byte[] hashedPassword = md.digest(password.getBytes());
		    for (byte b : hashedPassword) {
		        sb.append(String.format("%02x", b));
		    }
		} catch (Exception e) {
			throw new InternalServerErrorException(BaseResponseStatus.ENCRYPTION_ERROR);
		}
		return sb.toString();
	}

	public static boolean matches(String rawPassword, String encodedPassword) {
	    return encode(rawPassword).equals(encodedPassword);
	}
}