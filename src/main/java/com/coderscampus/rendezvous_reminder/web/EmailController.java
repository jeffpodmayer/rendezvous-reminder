package com.coderscampus.rendezvous_reminder.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmailController {

    @GetMapping("/")
    public String getHomepage(){
        return "home";
    }
}
