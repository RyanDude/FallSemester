package com.example.demo.Controllers;

import com.example.demo.Repository.*;
import com.example.demo.Service.impl.FilesStorageService;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
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
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private StuMenRepository stuMenRepository;
    @Autowired
    private ConfirmRepository confirmRepository;

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
        System.out.println("username " + username);
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
        stu.get(0).setField(student.getField());
        studentRepository.save(stu.get(0));
        studentRepository.flush();
        return new ResEntity<>(200, "update successfully!");
    }
    @RequestMapping("/student/search")
    @ResponseBody
    public ResEntity<Page<Mentor>> search(
            @RequestParam(name = "name") String name, @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam("size") int pageSize){
        Page<Mentor> query = mentorRepository.getAll(PageRequest.of(pageNumber, pageSize), name);
        // System.out.println("total pages" + query.getTotalElements());
        return new ResEntity<Page<Mentor>>(query, 200);
    }
    @RequestMapping("/student/recommend")
    @ResponseBody
    public ResEntity<Slice<Mentor>> recommend(@RequestParam(name = "pageNumber") int pageNumber,
                                              @RequestParam("size") int pageSize){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"User not found");
        }
        List<Student> stu = studentRepository.findByAid(ids.get(0));
        if(stu == null || stu.isEmpty()){
            return new ResEntity<>(404,"User not found");
        }
        Page<Mentor> mentors = null;
        if(stu.get(0).getLikedGender().equals("Both")){
            mentors = mentorRepository.recommendBy(PageRequest.of(pageNumber, pageSize), stu.get(0).getLikedPos());
        }else{
            mentors = mentorRepository.recommend(PageRequest.of(pageNumber, pageSize), stu.get(0).getLikedGender(), stu.get(0).getLikedPos());
        }
        return new ResEntity<>(mentors, "success", 200);
    }
    @RequestMapping("/student/follow")
    @ResponseBody
    public ResEntity<String> follow(@RequestParam("mid") long mid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404, "user not found");
        }
        List<Student> stu = studentRepository.findByAid(ids.get(0));
        if(stu == null || stu.isEmpty()){
            return new ResEntity<>(404, "user not found");
        }
        long sid = stu.get(0).getId();
        if(stuMenRepository.find(sid, mid)==null && confirmRepository.findByMidAndSid(mid, sid) == null){
            StuMen m = new StuMen();
            m.setMid(mid);
            m.setSid(sid);
            stuMenRepository.save(m);
            return new ResEntity<>(200, "success");
        }
        return new ResEntity<>(404, "user not found");
    }

    @Autowired
    FilesStorageService storageService;
    @Autowired
    FileInfoRepository fileInfoRepository;

    @PostMapping("/student/upload")
    public ResEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404, "user not found");
        }
        try {
            storageService.save(file);
            storageService.save(file);
            System.out.println(file.getOriginalFilename());
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(file.getOriginalFilename());
            fileInfo.setOwner(ids.get(0));
            fileInfoRepository.save(fileInfo);
        } catch (Exception e) {
        }
        return new ResEntity<>("success",200);
    }

    @GetMapping("/student/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping("/student/send_file")
    @ResponseBody
    public ResEntity<String> send(@RequestParam("mid")long mid, @RequestParam("file_name") String file_name){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        List<Student> students= studentRepository.findByAid(ids.get(0));
        if(students == null || students.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setShared(mid);
        fileInfo.setName(file_name);
        fileInfo.setOwner(ids.get(0));
        fileInfoRepository.save(fileInfo);
        return new ResEntity<>("success",200);
    }
    @RequestMapping("/student/self_file")
    @ResponseBody
    public ResEntity<List<FileInfo>> sfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        return new ResEntity<>(fileInfoRepository.findByOwner(ids.get(0)), 200);
    }
    @RequestMapping("/student/shared_file")
    @ResponseBody
    public ResEntity<List<FileInfo>> rfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        return new ResEntity<>(fileInfoRepository.findByShared(ids.get(0)), 200);
    }


}
