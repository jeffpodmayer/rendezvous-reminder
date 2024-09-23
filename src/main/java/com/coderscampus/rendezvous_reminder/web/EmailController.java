package com.coderscampus.rendezvous_reminder.web;
import com.coderscampus.rendezvous_reminder.domain.Email;
import com.coderscampus.rendezvous_reminder.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class EmailController {

    @Autowired
    EmailService emailService;

    @GetMapping("/")
    public String getHomepage(Model model){
        model.addAttribute("email", new Email());
        return "home";
    }

    @PostMapping("/submit-email")
    public String submitEmail(@ModelAttribute("email") Email email){
        emailService.save(email);
        System.out.println("Email submitted: " + email.getEmailAddress());
        return "home";
    }
}
