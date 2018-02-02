package com.s305089.bookstore.email;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@RequestMapping("/contact/**")
@Controller
public class Contact {

    private static final Logger logger = Logger.getLogger(Contact.class);

    @RequestMapping(method = RequestMethod.POST)
    public String post(@Valid ContactInfo info, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Getting ready to send email");
        sendMessage(info);
        logger.info("Done sending email");
        return "contact/index";
    }



    @RequestMapping
    public String index() {
        return "contact/index";
    }

    @Autowired
    private transient SimpleMailMessage templateMessage;

    @Autowired
    private transient MailSender mailTemplate;

    private void sendMessage(ContactInfo user) {
        org.springframework.mail.SimpleMailMessage mailMessage = new org.springframework.mail.SimpleMailMessage(templateMessage);
        mailMessage.setTo(user.getMessage());
        mailMessage.setText(user.getMessage());
        mailMessage.setCc(mailMessage.getFrom());
        mailMessage.setFrom(user.getFromEmail());
        mailMessage.setSubject(user.getSubject());

        logger.info("Sending message to " + mailMessage.getTo()[0]);
        logger.info("Sending message CC to " + mailMessage.getCc()[0]);
        logger.info("Sending message from " + mailMessage.getFrom());
        mailTemplate.send(mailMessage);
    }
}

