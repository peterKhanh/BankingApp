package peter.bankapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import peter.bankapp.dto.EmailDetails;
import peter.bankapp.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	 @Autowired
	 private JavaMailSender javaMailSender;
	 
	 @Value("${spring.mail.username}")
	 private String senderEmail;

	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		// TODO Auto-generated method stub
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setTo(emailDetails.getSubject());
			
			javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");
		
		} catch (MailException e) {
			throw new RuntimeException(e);
		}
		
	}
	 
	 
}
