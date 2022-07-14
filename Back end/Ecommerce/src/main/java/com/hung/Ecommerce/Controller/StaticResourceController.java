package com.hung.Ecommerce.Controller;

import static java.lang.System.out;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hung.Ecommerce.Model.StaticResourceMapper;
import com.hung.Ecommerce.Service.StaticResourceMapperService;


@RestController
@RequestMapping("/StaticResources")
public class StaticResourceController {
	
	@Autowired
	private StaticResourceMapperService staticResourceMapperService;
	
	// Find specific file based on a key
	@GetMapping("/**")
		public void mapping(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				String resourceCode = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
				
				List<StaticResourceMapper> mapper = staticResourceMapperService.findByProperty("fileKey", resourceCode, false, false);
				if(mapper.isEmpty()) {
						response.sendError(HttpStatus.NOT_FOUND.value(), "The file not found");
						return;
				}
				
				else {
					String correspondingFile = mapper.get(0).getFileName();
					String newResourceUrl = request.getRequestURI().replace(resourceCode, correspondingFile)
																.replace("StaticResources", "resources");

					request.getRequestDispatcher(newResourceUrl).forward(request, response);
				}
		}
}
