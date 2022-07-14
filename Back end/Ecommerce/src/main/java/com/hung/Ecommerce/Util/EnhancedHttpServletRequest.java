package com.hung.Ecommerce.Util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EnhancedHttpServletRequest extends HttpServletRequestWrapper{

	private Map<String, String[]> additionalParams = new HashMap<String, String[]>();
	private Map<String, String> headerMap = new HashMap<String, String>();
    private HttpServletRequest request;

    public EnhancedHttpServletRequest(
      HttpServletRequest request, Map<String, String[]> additionalParams, Map<String, String> headerMap) {
	        super(request);
	        this.request = request;
	        
	        if(additionalParams != null) {
	        		this.additionalParams = additionalParams;
	        }
	       
	        if(headerMap != null) {
	        	 this.headerMap = headerMap;
	        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
	        Map<String, String[]> map = request.getParameterMap();
	        Map<String, String[]> param = new HashMap<String, String[]>();
	        param.putAll(map);
	        param.putAll(additionalParams);
	        return param;
    }
    
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = request.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(request.getHeaderNames());
        for (String name : headerMap.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(request.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }
}
