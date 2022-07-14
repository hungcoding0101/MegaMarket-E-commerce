package com.hung.Ecommerce.Service;

import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImp implements MailService{

	private JavaMailSenderImpl mailsender;
	

	public MailServiceImp() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	public MailServiceImp(JavaMailSenderImpl mailsender) {
		this.mailsender = mailsender;
	}

	@Async("TaskExecutor_In_serviceConfig")
	@Override
	public CompletableFuture<String> sendMail(String from, String subject, String toAddresses, String ccAddresses, String bccAddresses,
										String body) throws MessagingException {
		MimeMessage message = mailsender.createMimeMessage();
		
		try {
				MimeMessageHelper messageHelper = new MimeMessageHelper(message);
				messageHelper.setFrom(from);
				messageHelper.setTo(toAddresses);
				messageHelper.setSubject(subject);
				messageHelper.setText(body, true);
				messageHelper.setPriority(1);
				if(ccAddresses != null && !ccAddresses.isEmpty()) {messageHelper.setCc(ccAddresses);}
				if(bccAddresses != null && !bccAddresses.isEmpty()) {messageHelper.setCc(bccAddresses);}
		}catch (MessagingException e) {
			throw e;
		}
		
		System.out.println("HERE: MAIL SERVICE Thread name: " + Thread.currentThread().getName());
		
		mailsender.send(message);
		CompletableFuture< String> result = new CompletableFuture<>();
		result.complete("Not sent yet");
		return result;
	}
}
