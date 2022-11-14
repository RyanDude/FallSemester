package com.example.demo.filer;

import com.example.demo.entity.JwtUser;
import com.example.demo.entity.LoginUser;
import com.example.demo.jwtUtil.TokenUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/***
 * @Author: Jianjun Guo
 * @Date: Sep 1st 2022
 * Filter for login with name & pass, after login, it will generate token for the request
 * **/
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
        // set up the filter url
        super.setFilterProcessesUrl("/auth/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException
    {
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try{
            LoginUser loginUser=objectMapper.readValue(request.getInputStream(), LoginUser.class);
            System.err.println(loginUser.getUsername()+" "+loginUser.getPassword());
            UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(loginUser.getUsername(),loginUser.getPassword());
            return authenticationManager.authenticate(token);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication)
    {
        JwtUser user=(JwtUser)authentication.getPrincipal();
        // TokenUtil tokenUtil = new TokenUtil();
        String token = TokenUtil.generateAccessToken(user);
        HttpCookie cookie = ResponseCookie.from("token", token)
                .maxAge(3600)
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        response.setHeader("Authorization", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0));
    }
}
