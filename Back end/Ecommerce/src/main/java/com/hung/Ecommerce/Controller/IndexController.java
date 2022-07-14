package com.hung.Ecommerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hung.Ecommerce.Model.User;
import com.hung.Ecommerce.Service.IndexService;
import com.hung.Ecommerce.Service.UserService;

@RestController
@RequestMapping("/index")
public class IndexController {

	@Autowired
		private IndexService indexService;
	
	@GetMapping("/mass")
		public void massIndex() {
			try {
				indexService.Reindexing();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
