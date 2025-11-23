package com.infosys.seatsync.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.infosys.seatsync.model.EmailDetails;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;
    
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(EmailDetails emailDetails){
        try {
            SimpleMailMessage mailMsg = new SimpleMailMessage();
            mailMsg.setFrom(emailSender);
            mailMsg.setTo(emailDetails.getRecipient());
            mailMsg.setText(emailDetails.getMessageBody());
            mailMsg.setSubject(emailDetails.getSubject());
            mailMsg.setFrom("noreply@gmail.com");
            javaMailSender.send(mailMsg);
            logger.info("Mail sent successfully");
        }catch (MailException exception){
        	logger.debug("Failure occurred while sending email");
        }
    }
}
