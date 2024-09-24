package com.coderscampus.rendezvous_reminder.service;

import com.coderscampus.rendezvous_reminder.domain.Email;
import com.coderscampus.rendezvous_reminder.repository.EmailRepository;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    public void sendWelcomeEmail(Email email) {
        String recipientEmail = email.getEmailAddress();
        // Email details
        String host = "smtp.gmail.com";
        String from = "jeff.podmayer@gmail.com";
        String username = "jeff.podmayer@gmail.com";
        String password = "aado esqf vool omzr"; // Use your app password

        Properties properties = getProperties(host);

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            message.setSubject("Welcome to the Rendezvous Reminder!");
            String content =
                    "Thank you for signing up to receive cancellation reminders for the Rendezvous Huts! \n"
                    + "Best regards,\n"
                    + "The Rendezvous Reminder Team";

            message.setText(content);

            Transport.send(message);
            System.out.println("Welcome email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendUnsubscribeEmail(Email email) {
        String recipientEmail = email.getEmailAddress();

        // Email details
        String host = "smtp.gmail.com";
        String from = "jeff.podmayer@gmail.com";
        String username = "jeff.podmayer@gmail.com";
        String password = "aado esqf vool omzr"; // Use your app password

        Properties properties = getProperties(host);

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set the subject for the unsubscribe email
            message.setSubject("Unsubscribed from the Rendezvous Reminder");

            // Create the content for the unsubscribe email
            String content =
                    "You have successfully unsubscribed from receiving cancellation reminders for the Rendezvous Huts. \n"
                            + "We're sorry to see you go, but you can always sign up again if you change your mind.\n\n"
                            + "Best regards,\n"
                            + "The Rendezvous Reminder Team";

            message.setText(content);

            // Send the message
            Transport.send(message);
            System.out.println("Unsubscribe email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public void sendEmail(String subject, String content) {
        // Email details
        String host = "smtp.gmail.com";
        String from = "jeff.podmayer@gmail.com";
        String to = "jeff.podmayer@gmail.com";
        String username = "jeff.podmayer@gmail.com";
        String password = "aado esqf vool omzr"; // Use your app password

        Properties properties = getProperties(host);

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Set the actual message
            message.setText(content);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Properties getProperties(String host) {
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    public void save(Email email) {
        emailRepository.save(email);
    }
    @Transactional
    public void unsubscribe(String emailAddress) {
        emailRepository.deleteByEmailAddress(emailAddress);
    }
}
