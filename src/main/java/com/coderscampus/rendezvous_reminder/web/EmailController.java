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
    private EmailService emailService;

    @GetMapping("/")
    public String getHomepage(ModelMap model) {
        model.put("signUpEmail", new Email());
        model.put("unsubscribeEmail", new Email());
        return "home";
    }

    @PostMapping("/submit-email")
    public String submitEmail(@ModelAttribute("signUpEmail") Email email) {
        emailService.save(email);
        System.out.println("Email signed up: " + email.getEmailAddress());
        return "redirect:/";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribeEmail(@ModelAttribute("unsubscribeEmail") Email email) {
        emailService.unsubscribe(email.getEmailAddress());
        System.out.println("Email unsubscribed: " + email.getEmailAddress());
        return "redirect:/";
    }
}
