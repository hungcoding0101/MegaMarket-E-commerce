package com.hung.Ecommerce.CustomFilters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hung.Ecommerce.Model.StaticResourceMapper;
import com.hung.Ecommerce.Service.StaticResourceMapperService;
import static java.lang.System.*;

public class StaticResourceMappingFilter extends OncePerRequestFilter{

	private StaticResourceMapperService staticResourceMapperService;
	
	
	public StaticResourceMappingFilter(StaticResourceMapperService staticResourceMapperService) {
		super();
		this.staticResourceMapperService = staticResourceMapperService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			
			String resourceCode = request.getPathInfo().substring(request.getPathInfo().lastIndexOf("/")+1);
			
			List<StaticResourceMapper> mapper = staticResourceMapperService.findByProperty("fileKey", resourceCode, false, false);
			if(mapper.isEmpty()) {
					response.sendError(HttpStatus.NOT_FOUND.value(), "The file not found");
					return;
			}
			
			else {
				String correspondingFile = mapper.get(0).getFileName();
				String newResourceUrl = request.getRequestURI().replace(resourceCode, correspondingFile);
				out.print("HERE: NEW URL: " + newResourceUrl);
				request.getRequestDispatcher(newResourceUrl).forward(request, response);
			}
				
			
	}

}
