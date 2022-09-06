package com.example.demo.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_MENTOR')")
public class MentorController {
    @RequestMapping("/mentor/hi")
    public String hi(){return "hi, mentor";}
}
