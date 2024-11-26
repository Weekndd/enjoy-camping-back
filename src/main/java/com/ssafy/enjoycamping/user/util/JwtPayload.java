package com.ssafy.enjoycamping.user.util;

import com.ssafy.enjoycamping.common.model.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class JwtPayload {
    private int id;
    private Date issuedAt;
    private TokenType tokenType;
}
