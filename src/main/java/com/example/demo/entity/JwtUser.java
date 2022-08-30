package com.example.demo.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class JwtUser implements UserDetails {
    private long id;
    private String username;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;
    public JwtUser(){}
    public JwtUser(Account account){
        this.id=account.getId();
        this.authorities=account.getRoles();
        this.username=account.getName();
        this.password=account.getPassword();
        this.enabled = account.isEnabled();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){return this.authorities;}
    @Override
    public String getPassword(){return this.password;}
    @Override
    public String getUsername(){return this.username;}
    @Override
    public boolean isAccountNonExpired(){return true;}
    @Override
    public boolean isAccountNonLocked(){return true;}
    @Override
    public boolean isCredentialsNonExpired(){return true;}
    @Override
    public boolean isEnabled(){return this.enabled;}
    @Override
    public String toString() {
        return "JwtUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
