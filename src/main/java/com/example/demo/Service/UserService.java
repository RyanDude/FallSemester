package com.example.demo.Service;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.entity.Account;
import com.example.demo.entity.JwtUser;
import com.example.demo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Jianjun Guo
 * @Date: Sep 1st
 * */

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByName(username);
        if(account == null){
            throw new UsernameNotFoundException("Account Not Found");
        }else{
            List<Role> roles = roleRepository.findByAccount_id(account.getId());
            if(roles!=null && !roles.isEmpty()){
                account.setRoles(roles);
            }
            return new JwtUser(account);
        }
    }
}
