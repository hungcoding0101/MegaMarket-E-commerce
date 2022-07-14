package com.hung.Ecommerce.Service;

import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;

	public interface MailService {
		public CompletableFuture<String> sendMail(String from, String subject, String toAddresses,
				String ccAddresses, String bccAddresses, String body) throws MessagingException ;
}
