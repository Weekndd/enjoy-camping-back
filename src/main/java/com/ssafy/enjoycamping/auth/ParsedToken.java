package com.ssafy.enjoycamping.auth;

import com.ssafy.enjoycamping.user.util.JwtProvider;

import io.jsonwebtoken.Claims;

public class ParsedToken {
    private final JwtProvider.TokenState state;
    private final Claims claims;

    public ParsedToken(JwtProvider.TokenState state, Claims claims) {
        this.state = state;
        this.claims = claims;
    }

    public JwtProvider.TokenState getState() {
        return state;
    }

    public Claims getClaims() {
        return claims;
    }
}