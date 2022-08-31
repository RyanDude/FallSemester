package com.example.demo.Controllers;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.entity.Account;
import com.example.demo.entity.Role;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
public class TController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @RequestMapping("/hi")
    public String test(){return "hello, student";}
    @RequestMapping("/test")
    public String t(){
        return "test";
    }
    @RequestMapping("/studentreg")
    @ResponseBody
    public String StudentReg(@RequestBody Account account){
        System.err.println("ENTERED");
        if(accountRepository.findByName(account.getName()) != null){return "Username already exist";}
        Account user = new Account();
        user.setName(account.getName());
        user.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        user.setEnabled(true);
        Date date = new Date();
        user.setLast_login(new Timestamp(date.getTime()));
        Role role = new Role();
        role.setRole("STUDENT");
        System.err.println(user.getName());
        accountRepository.save(user);
        accountRepository.flush();
        role.setAccount(accountRepository.findByName(account.getName()));
        roleRepository.save(role);
        roleRepository.flush();

        Student student = new Student();
        student.setAid(accountRepository.findIdByName(account.getName()).get(0));
        studentRepository.save(student);
        return "ok";
    }
}
