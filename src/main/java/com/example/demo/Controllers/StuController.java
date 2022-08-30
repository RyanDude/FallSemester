package com.example.demo.Controllers;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StuController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("/student/hi")
    public String hi(){return "hi, Student";}
    @RequestMapping("/student/info")
    public ResponseEntity<Student> info() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.err.println(username);
        List<Long> account_id = accountRepository.findIdByName(username);
        if(account_id == null || account_id.isEmpty()){
            throw new Exception();
        }
        List<Student> student = studentRepository.findByAccount_id(account_id.get(0));
        if(student == null || student.isEmpty()){ throw new Exception(); }
        return new ResponseEntity<Student>(student.get(0), HttpStatus.OK);
    }
    @RequestMapping("/student/update")
    public ResponseEntity<String> update(@RequestBody Student student) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> account_id = accountRepository.findIdByName(username);
        if(account_id == null || account_id.isEmpty()){
            return new ResponseEntity<String>("No such Account", HttpStatus.NOT_FOUND);
        }
        student.setAccount_id(account_id.get(0));
        studentRepository.save(student);
        return new ResponseEntity<String>("successfully updated", HttpStatus.OK);
    }

}
