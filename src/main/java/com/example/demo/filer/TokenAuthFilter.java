package com.example.demo.filer;

import com.example.demo.jwtUtil.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
/**
 * @Author: Jianjun Guo
 * @Date: Sep 1st 2022
 * Filter for token login, if request with correct token, then it can access the backend
 * BasicAuthFilter IS BEFORE FilterSecurityInterceptor in the filter chain, so we have to define permit ALL in the filter
 * */
public class TokenAuthFilter extends BasicAuthenticationFilter {
    public TokenAuthFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // permitAll: url
        // System.err.println(request.getServletPath());
        if(request.getServletPath().equals("/studentreg")
                || request.getServletPath().equals("/mentorreg")
                || request.getServletPath().equals("/adminreg")){
            chain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().contains("index")) {
            chain.doFilter(request, response);
        }
        UsernamePasswordAuthenticationToken authentication = null;
        authentication = getAuth(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{
            throw new ServletException("Token auth error");
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuth(HttpServletRequest request){
        String token = "";
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("token")){
                // System.out.println(c.getName());
                token = c.getValue();
            }
        }
        // System.out.println("toke == "+token);
        if (token != null && !"".equals(token.trim())) {
            // System.err.println("ENTER");
            // 从Token中解密获取用户名
            // TokenUtil tokenUtil = new TokenUtil();
            String userName = TokenUtil.getUsername(token);
            // System.out.println("username: "+userName);
            if (userName != null) {
                // 从Token中解密获取用户角色
                String role = TokenUtil.getRole(token);

                // 将ROLE_XXX,ROLE_YYY格式的角色字符串转换为数组
                String[] roles = role.split(",");
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (String s : roles) {
                    authorities.add(new SimpleGrantedAuthority(s));
                    // System.out.println("role: "+s);
                }
                return new UsernamePasswordAuthenticationToken(userName, token, authorities);
            }
            return null;
        }
        return null;
        /*
        // using header 'Authorization' to store token
        String token = request.getHeader("Authorization");
        System.err.println(token);
        if (!StringUtils.hasText(token)) {
            token = request.getParameter("token");
        }
        if (token != null && !"".equals(token.trim())) {
            // 从Token中解密获取用户名
            TokenUtil tokenUtil = new TokenUtil();
            String userName = tokenUtil.getUsername(token);
            System.out.println("username: "+userName);
            if (userName != null) {
                // 从Token中解密获取用户角色
                String role = tokenUtil.getRole(token);
                // 将ROLE_XXX,ROLE_YYY格式的角色字符串转换为数组
                String[] roles = role.split(",");
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (String s : roles) {
                    authorities.add(new SimpleGrantedAuthority(s));
                    System.out.println("role: "+s);
                }
                return new UsernamePasswordAuthenticationToken(userName, token, authorities);
            }
            return null;
        }
        return null;
        * */

    }
}
