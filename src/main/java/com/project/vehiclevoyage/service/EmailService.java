package com.project.vehiclevoyage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    public boolean sendEmail(String toEmail, String subject, String body) {
        boolean f = false;
        String fromEmail = "admin.vehiclevoyage@vehiclevoyage.in";

        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();
        System.out.println("PROPERTIES: " + properties);

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("admin.vehiclevoyage@vehiclevoyage.in", "@21vehiclevoyage");
            }
        });

        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);
        try {
            // Set From
            m.setFrom(fromEmail);

            //add recipients
            m.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(toEmail));

            //adding subject to message
            m.setSubject(subject);

            //adding body to message
            m.setText(body);

            //send
            javax.mail.Transport.send(m);

            System.out.println("Email sent successfully");

            f = true;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return f;
    }
}
