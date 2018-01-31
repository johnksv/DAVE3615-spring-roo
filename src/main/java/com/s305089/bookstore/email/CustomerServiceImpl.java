package com.s305089.bookstore.email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private transient SimpleMailMessage templateMessage;

    @Autowired
    private transient MailSender mailTemplate;

    public void sendMessage(String mailTo, String message) {
        org.springframework.mail.SimpleMailMessage mailMessage = new org.springframework.mail.SimpleMailMessage(templateMessage);
        mailMessage.setTo(mailTo);
        mailMessage.setText(message);
        mailTemplate.send(mailMessage);
    }
}
