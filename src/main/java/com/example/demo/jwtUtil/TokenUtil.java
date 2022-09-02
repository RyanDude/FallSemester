package com.example.demo.jwtUtil;

import com.example.demo.entity.JwtUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

public class TokenUtil {
    private static final String secret = "abcdefghijklmnOPQRSTUVWXYZ";
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    public String generateAccessToken(JwtUser user) {
        System.err.println("what s wrong?");
        return "Bearer "+Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getUsername()))
                .setIssuer("CodeJava")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }
}
