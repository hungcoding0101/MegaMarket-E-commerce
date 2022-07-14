package com.hung.Ecommerce.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hung.Ecommerce.DTO.RequestToUpdateNotification;
import com.hung.Ecommerce.Service.NotificationService;
import com.hung.Ecommerce.Service.UserService;
import com.nimbusds.oauth2.sdk.GeneralException;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	@Autowired
		private NotificationService notificationService;

	
	@PatchMapping("/updateStatus")
		public ResponseEntity<String> updateStatus(@RequestBody  List<String> request,
				OAuth2Authentication authentication) throws GeneralException {
				try {
					notificationService.updateStatus(request, authentication.getName());
					return ResponseEntity.ok("");
				} catch (Exception e) {
					throw new GeneralException("");
				}
	}
}
