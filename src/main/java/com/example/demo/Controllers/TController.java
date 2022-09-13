package com.example.demo.Controllers;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.MentorRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @RequestMapping("/hi")
    @ResponseBody
    public Map<String, String> test(){
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");
        return map;
    }
    @RequestMapping("/test")
    public String t(){
        return "test";
    }
    @RequestMapping("/studentreg")
    @ResponseBody
    public ResEntity<Student> StudentReg(@RequestBody Account account){
        if(accountRepository.findByName(account.getName()) != null){
            // return new ResEntity<Student>(404, "username has been registered");
            return new ResEntity<Student>(new Student(),"username has been registered", 404);
        }
        Account user = new Account();
        user.setName(account.getName());
        user.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        user.setEnabled(true);
        Date date = new Date();
        user.setLast_login(new Timestamp(date.getTime()));
        Role role = new Role();
        role.setRole("STUDENT");
        accountRepository.save(user);
        accountRepository.flush();
        role.setAccount(accountRepository.findByName(account.getName()));
        roleRepository.save(role);
        roleRepository.flush();

        Student student = new Student();
        student.setAid(accountRepository.findIdByName(account.getName()).get(0));
        studentRepository.save(student);

        return new ResEntity<Student>(new Student(),"register successfully", 200);
    }
    @RequestMapping("/adminreg")
    @ResponseBody
    public ResEntity<String> AdminReg(@RequestBody Account account){
        System.err.println("username: "+ account.getName());
        System.err.println("password: "+ account.getPassword());
        if(accountRepository.findByName(account.getName()) != null){
            return new ResEntity<>(404, "username has been registered");
        }
        Account act = new Account();
        act.setName(account.getName());
        act.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        act.setEnabled(true);
        act.setLast_login(new Timestamp(new Date().getTime()));
        Role role = new Role();
        role.setRole("ADMIN");

        accountRepository.save(act);
        accountRepository.flush();
        role.setAccount(accountRepository.findByName(act.getName()));
        roleRepository.save(role);
        roleRepository.flush();
        return new ResEntity<>(200, "register successfully");
    }

    @RequestMapping("/mentorreg")
    @ResponseBody
    public ResEntity<String> MentorReg(@RequestBody Account account){
        if(accountRepository.findByName(account.getName()) != null){
            return new ResEntity<>(404, "username has been registered");
        }
        Account user = new Account();
        user.setName(account.getName());
        user.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        user.setEnabled(true);
        user.setLast_login(new Timestamp(new Date().getTime()));
        accountRepository.save(user);
        accountRepository.flush();

        Role role = new Role();
        role.setAccount(accountRepository.findByName(account.getName()));
        role.setRole("MENTOR");
        roleRepository.save(role);
        roleRepository.flush();

        Mentor mentor = new Mentor();
        mentor.setAid(accountRepository.findIdByName(user.getName()).get(0));
        mentorRepository.save(mentor);
        return new ResEntity<>(200, "register successfully");
    }
    @RequestMapping("/get_role")
    @ResponseBody
    public ResEntity<String> getRole(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new ResEntity<String>(roles.get(0), "");
    }
}
