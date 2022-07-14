package com.hung.Ecommerce.Util.JsonConverter;

import static java.lang.System.out;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hung.Ecommerce.DTO.RequestToSearchProduct;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;
import com.hung.Ecommerce.Model.Product_SortTypes;

public class JsonToSearchProduct extends StdDeserializer<RequestToSearchProduct>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonToSearchProduct() {
		this(null);
	}
	
	protected JsonToSearchProduct(Class<RequestToSearchProduct> t) {
		super(t);
	}

	@Override
	public RequestToSearchProduct deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JacksonException {
		
			try {		
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(p);
				
				String keyWords = node.get("keyWords") != null? node.get("keyWords").asText(): null; 
				int offset = node.get("offset") != null? node.get("offset").asInt(): 0;
				int limit = node.get("limit") != null? node.get("limit").asInt(): 0;
				String sort = node.get("sortType") != null? node.get("sortType").asText(): null; 
				
				Product_SortTypes sortType = null;	
				try {
					sortType = Product_SortTypes.valueOf(sort);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				HashMap<String, String> filters = null;
				JsonNode jsonFilters = node.get("filters");
				if(jsonFilters != null && jsonFilters.isObject()) {
						filters =  mapper.readValue(jsonFilters.traverse(), HashMap.class);
				}
				
				RequestToSearchProduct request = new RequestToSearchProduct(keyWords, offset, limit, sortType, filters);
				
				out.println("HERE: SEARCH REQUEST: " + request.toString());
				
				return request;
				
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some errors occurred :(");
			}
	}
}
