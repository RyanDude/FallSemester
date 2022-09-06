package com.example.demo.jwtUtil;

import com.example.demo.entity.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Jianjun Guo
 * @Date: Sep 1st
 * */

public class TokenUtil {
    private static final String secret = "abcdefghijklmnOPQRSTUVWXYZ";
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    public String generateAccessToken(JwtUser user) {
        List<String> roles=user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return   Jwts.builder()
                .setSubject(user.getUsername())
                .claim(SeucrityVars.ROLE_CLAIMS, String.join(",", roles))
                .setIssuer("J.G")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }
    public boolean isValid(String token, UserDetails userDetails){
        String username = getUsername(token);
        return username.equals(userDetails.getUsername());
    }
    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
    public String getRole(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.get(SeucrityVars.ROLE_CLAIMS).toString();
    }
}
