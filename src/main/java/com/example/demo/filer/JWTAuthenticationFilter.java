package com.example.demo.filer;

import com.example.demo.entity.JwtUser;
import com.example.demo.entity.LoginUser;
import com.example.demo.jwtUtil.SeucrityVars;
import com.example.demo.jwtUtil.TokenUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> roles=user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        TokenUtil tokenUtil = new TokenUtil();
        String token = tokenUtil.generateAccessToken(user);
        // token - username
        //stringRedisTemplate.opsForValue().set(token, user.getUsername());
        System.err.println("SUUUCCC");
        //redisTemplate.opsForValue().set(user.getUsername(), token);
        response.setHeader(SeucrityVars.TOKEN_HEADER, token);
    }
}
