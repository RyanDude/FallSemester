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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_MENTOR')")
public class MentorController {
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StuMenRepository stuMenRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ConfirmRepository confirmRepository;

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
        mentors.get(0).setField(mentor.getField());
        mentorRepository.save(mentors.get(0));
        return new ResEntity<String>(200, "updated successfully");
    }
    @RequestMapping("/mentor/waiting")
    @ResponseBody
    public ResEntity<List<Student>> waiting(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404, "user not found");
        }
        List<Mentor> mentors= mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<>(404, "user not found");
        }
        List<StuMen> rs = stuMenRepository.findByMid(mentors.get(0).getId());
        List<Student> students = new ArrayList<>();
        for(StuMen x:rs){
            students.add(studentRepository.findById(x.getSid()));
        }
        return new ResEntity<>(students, "OK", 200);
    }
    @RequestMapping("/mentor/accept")
    @ResponseBody
    public ResEntity<String> accept(@RequestParam("sid") long sid, HttpServletResponse response)throws IOException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>( "user not found", 404);
        }
        List<Mentor> mentors = mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<>( "user not found", 404);
        }
        Student stu = studentRepository.findById(sid);
        if(stu != null){
            Confirm c = new Confirm();
            c.setMid(mentors.get(0).getId());
            c.setSid(stu.getId());
            confirmRepository.save(c);
            stuMenRepository.delete(stuMenRepository.find(sid, mentors.get(0).getId()));
            return new ResEntity<>( "OK", 200);
        }
        return new ResEntity<>( "user not found", 404);
    }
    @RequestMapping("/mentor/refuse")
    @ResponseBody
    public ResEntity<String> refuse(@RequestParam("sid") long sid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>( "user not found", 404);
        }
        List<Mentor> mentors= mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<>( "user not found", 404);
        }
        StuMen r = stuMenRepository.find(sid, mentors.get(0).getId());
        if(r == null){return new ResEntity<>( "match not found", 404);}
        stuMenRepository.delete(r);
        return new ResEntity<>( "refuse successfully", 200);
    }
    @RequestMapping("/mentor/search")
    @ResponseBody
    public ResEntity<Page<Student>> search_student(
            @RequestParam(name = "name") String name, @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam("size") int pageSize
    ){
        return new ResEntity<>(studentRepository.findByName(PageRequest.of(pageNumber, pageSize), name),200);
    }
    @RequestMapping("/mentor/self-students")
    @ResponseBody
    public ResEntity<List<Student>> self(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        List<Mentor> mentors= mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        return new ResEntity<>(mentors.get(0).getStudents(), 200);
    }
    @Autowired
    FilesStorageService storageService;
    @Autowired
    FileInfoRepository fileInfoRepository;

    /** the id here means Aid not sid or mid **/
    @PostMapping("/mentor/upload")
    public ResEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        try {
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

    @GetMapping("/mentor/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping("/mentor/send_file")
    @ResponseBody
    public ResEntity<String> send(@RequestParam("sid")long sid,
                                  @RequestParam("file") MultipartFile file){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Long> ids = accountRepository.findIdByName(username);
        if(ids == null || ids.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        List<Mentor> mentors= mentorRepository.findByAid(ids.get(0));
        if(mentors == null || mentors.isEmpty()){
            return new ResEntity<>(404,"user not found");
        }
        storageService.save(file);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setShared(sid);
        fileInfo.setName(file.getOriginalFilename());
        fileInfo.setOwner(ids.get(0));
        fileInfoRepository.save(fileInfo);
        return new ResEntity<>("success",200);
    }
    @RequestMapping("/mentor/self_file")
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
    @RequestMapping("/mentor/shared_file")
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
