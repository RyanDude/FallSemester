package com.example.demo.Controllers;

import com.example.demo.Repository.AccountRepository;
import com.example.demo.Repository.MentorRepository;
import com.example.demo.entity.Mentor;
import com.example.demo.entity.ResEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_MENTOR')")
public class MentorController {
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private AccountRepository accountRepository;
    @RequestMapping("/mentor/hi")
    public String hi(){return "hi, mentor";}
    @RequestMapping("/mentor/profile")
    @ResponseBody
    public ResEntity<Mentor> getInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<Mentor>(new Mentor(), "Did not find the user", 404);
        }
        List<Mentor> mentors = mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<Mentor>(new Mentor(), "User profile not found", 404);
        }
        return new ResEntity<Mentor>(mentors.get(0), 200);
    }
    @RequestMapping("/mentor/update")
    @ResponseBody
    public ResEntity<String> update(@RequestBody Mentor mentor){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<String>(404, "no such account");
        }
        List<Mentor> mentors= mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<String>(404, "mentor info has not been created");
        }
        mentors.get(0).setCurrent_employer(mentor.getCurrent_employer());
        mentors.get(0).setEmail(mentor.getEmail());
        mentors.get(0).setGender(mentor.getGender());
        mentors.get(0).setRace(mentor.getRace());
        mentors.get(0).setName(mentor.getName());
        mentors.get(0).setTitle(mentor.getTitle());
        mentorRepository.save(mentors.get(0));
        return new ResEntity<String>(200, "updated successfully");
    }
}
