package com.ssafy.enjoycamping.user.util;

import java.util.Date;

import com.ssafy.enjoycamping.common.model.TokenType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtPayload {
    private int id;
    private Date issuedAt;
    private TokenType tokenType;
}
