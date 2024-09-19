package com.coderscampus.rendezvous_reminder.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {

    @GetMapping("/home")
    public String getHomepage(){
        return "home";
    }

    @PostMapping("/submit-email")
    public String submitEmail(@RequestParam String email){
        System.out.println("email submitted");
        return "home";
    }
}
