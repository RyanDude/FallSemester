package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MentorController {
    @RequestMapping("mentor/hi")
    public String hi(){return "hi, mentor";}
}
