package com.coderscampus.rendezvous_reminder.web;
import com.coderscampus.rendezvous_reminder.domain.Email;
import com.coderscampus.rendezvous_reminder.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.processing.SupportedAnnotationTypes;

@Controller
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/home")
    public String getHomepage(ModelMap model){
        model.put("email", "");
        return "home";
    }

    @PostMapping("/submit-email")
    public String submitEmail(@RequestParam String email){
        Email newEmail = new Email(email);
        newEmail.setEmailAddress(email);
        emailService.save(email);
        System.out.println("email submitted");
        return "home";
    }
}
