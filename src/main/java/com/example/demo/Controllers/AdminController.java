package com.example.demo.Controllers;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.ConfirmRepository;
import com.example.demo.Repository.MentorRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    MentorRepository mentorRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ConfirmRepository confirmRepository;

    /** total number of students **/
    @RequestMapping("/admin/nstudent")
    @ResponseBody
    public long number_of_student(){
        return studentRepository.count();
    }
    @RequestMapping("/admin/nmentor")
    @ResponseBody
    public long num_of_mentor(){
        return mentorRepository.count();
    }

    /** get all students info **/
    @RequestMapping("/admin/stu")
    @ResponseBody
    public ResEntity<Page<Student>> allStudents(@RequestParam("pageNumber") int pageNumber,
                                                @RequestParam("size") int pageSize){
        Page<Student> students= studentRepository.all(PageRequest.of(pageNumber, pageSize));
        return new ResEntity<>(students, "ok", 200);
    }

    /** get all instructors info **/
    @RequestMapping("/admin/men")
    @ResponseBody
    public ResEntity<Page<Mentor>> allMentors(@RequestParam("pageNumber") int pageNumber,
                                              @RequestParam("size") int pageSize){
        Page<Mentor> mentors = mentorRepository.all(PageRequest.of(pageNumber, pageSize));
        return new ResEntity<>(mentors, "ok", 200);
    }

    /** get confirmed student-mentor info **/
    @RequestMapping("/admin/confirm")
    @ResponseBody
    public ResEntity<List<?>> allConfirm(){
        List<?> ret = confirmRepository.admin();
        return new ResEntity<>(ret, "success", 200);
    }

    /** agree with the confirmed matches **/
    @RequestMapping("/admin/agree")
    @ResponseBody
    public ResEntity<String> agree(@RequestParam("sid") long sid, @RequestParam("mid") long mid){
        Student student = studentRepository.findById(sid);
        Mentor mentor = mentorRepository.findById(mid);
        Confirm c = confirmRepository.findByMidAndSid(mid, sid);
        if(student == null || mentor == null || c == null){
            return new ResEntity<>(404, "mentor id or student id is wrong");
        }
        student.setMentor(mentor);
        confirmRepository.delete(c);
        return new ResEntity<>(200, "success");
    }

    @RequestMapping("/admin/refuse")
    @ResponseBody
    public ResEntity<String> refuse(@RequestParam("sid") long sid, @RequestParam("mid") long mid){
        Student student = studentRepository.findById(sid);
        Mentor mentor = mentorRepository.findById(mid);
        Confirm c = confirmRepository.findByMidAndSid(mid, sid);
        if(student == null || mentor == null || c == null){
            return new ResEntity<>(404, "mentor id or student id is wrong");
        }
        confirmRepository.delete(c);
        return new ResEntity<>(200, "success");
    }
    /** return stu-info with their mentor info **/
    @RequestMapping("/admin/searchStu")
    @ResponseBody
    public ResEntity<Page<Student>> search_stu(
            @RequestParam("sname")String sname, @RequestParam("pageNumber") int pageNumber,
            @RequestParam("size") int pageSize){
        Page<Student> students = studentRepository.findByName(PageRequest.of(pageNumber, pageSize),sname);
        return new ResEntity<>(students, "ok", 200);
    }
    /** return mentor-info with their students **/
    @RequestMapping("/admin/searchMen")
    @ResponseBody
    public ResEntity<Page<Mentor>> search_mentor(
            @RequestParam("mname")String mname, @RequestParam("pageNumber") int pageNumber,
            @RequestParam("size") int pageSize){
        Page<Mentor> mentors = mentorRepository.getAll(PageRequest.of(pageNumber, pageSize), mname);
        return new ResEntity<>(mentors, "ok", 200);
    }

}
