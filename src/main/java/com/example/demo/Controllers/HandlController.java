package com.example.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HandlController {
    @RequestMapping("/200")
    public String suc(){
        return "200";
    }
    @RequestMapping("/404")
    public String fail(){
        return "404";
    }
}
