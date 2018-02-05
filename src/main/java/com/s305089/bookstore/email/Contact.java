package com.s305089.bookstore.email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/contact/**")
@Controller
public class Contact {

    private static final Logger logger = Logger.getLogger(Contact.class);

    @Autowired
    private transient SimpleMailMessage templateMessage;

    @Autowired
    private transient MailSender mailTemplate;

    @RequestMapping
    public String index() {
        return "contact/index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post(@Valid ContactInfo info, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Getting ready to send email");
        sendMessage(info); //If exception => redirect to uncauhtExaption.jspx
        logger.info("Done sending email");
        return "contact/ok";

    }

    private void sendMessage(ContactInfo user) {
        org.springframework.mail.SimpleMailMessage mailMessage = new org.springframework.mail.SimpleMailMessage(templateMessage);

        String toEmail = mailMessage.getFrom();
        String userEmail = user.getFromEmail();
        String subject = user.getSubject() + " - from: " + user.getFromName();
        String message = user.getMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setCc(userEmail);
        mailMessage.setFrom(toEmail); //Has to do this since gmail is authenticated with "my" account
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        logger.info("Message from " + mailMessage.getFrom());
        logger.info("Message to " + mailMessage.getTo()[0]);
        logger.info("Message CC to " + mailMessage.getCc()[0]);
        logger.info("Message subject: " + mailMessage.getSubject());
        logger.info("Message from name " + mailMessage.getFrom());

        mailTemplate.send(mailMessage);
    }
}

