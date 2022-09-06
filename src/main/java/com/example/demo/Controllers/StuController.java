package com.example.demo.Controllers;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.entity.ResEntity;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Jianjun Guo
 * @Date: Sep 4th 2022
 * */

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_STUDENT')")
public class StuController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StudentRepository studentRepository;


    @RequestMapping("/student/hi")
    public String hi(){return "hi, Student";}

    // @PreAuthorize("hasAnyAuthority('ROLE_MENTOR')")
    @RequestMapping("/student/info")
    @ResponseBody
    public ResponseEntity<Student> info() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> account_id = accountRepository.findIdByName(username);
        if(account_id == null || account_id.isEmpty()){
            throw new Exception();
        }
        List<Student> student = studentRepository.findByAid(account_id.get(0));
        if(student == null || student.isEmpty()){ throw new Exception(); }
        // System.out.println(student.get(0).getName());
        return new ResponseEntity<Student>(student.get(0), HttpStatus.OK);
    }
    /**
     * Bug Fixed
     * if using ResponseEntity, the frontend will always receive ErrorHttpResponse,
     * it is really weird problem...
     * So here I use my own ResEntity as HttpResponse.
     * */
    @RequestMapping("/student/update")
    @ResponseBody
    public ResEntity<String> update(@RequestBody Student student) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        // System.out.println("username " + username);
        List<Long> account_id = accountRepository.findIdByName(username);
        if(account_id == null || account_id.isEmpty()){
            // "No such Account"
            return new ResEntity<>(404, "No such Account");
        }
        List<Student> stu = studentRepository.findByAid(account_id.get(0));
        if(stu == null || stu.isEmpty()){return new ResEntity<>(404, "No such Account");}
        stu.get(0).setEmail(student.getEmail());
        stu.get(0).setGender(student.getGender());
        stu.get(0).setLikedGender(student.getLikedGender());
        stu.get(0).setLikedPos(student.getLikedPos());
        stu.get(0).setPid(student.getPid());
        stu.get(0).setName(student.getName());
        studentRepository.save(stu.get(0));
        studentRepository.flush();
        return new ResEntity<>(200, "update successfully!");
    }

}
