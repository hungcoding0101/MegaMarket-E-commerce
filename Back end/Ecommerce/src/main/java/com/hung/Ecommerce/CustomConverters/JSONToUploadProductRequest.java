package com.hung.Ecommerce.CustomConverters;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;

public class JSONToUploadProductRequest implements Converter<String, RequestToUploadProduct>{

	@Override
	public RequestToUploadProduct convert(String source) {
		ObjectMapper mapper = new ObjectMapper();
		RequestToUploadProduct thisProduct = null;
		try {
			 thisProduct = mapper.readValue(source, RequestToUploadProduct.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return thisProduct;
	}

}
