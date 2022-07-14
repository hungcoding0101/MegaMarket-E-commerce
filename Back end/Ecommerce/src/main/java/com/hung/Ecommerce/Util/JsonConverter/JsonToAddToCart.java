package com.hung.Ecommerce.Util.JsonConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hung.Ecommerce.DTO.RequestToAddToCart;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.ProductOptionStatus;

public class JsonToAddToCart extends StdDeserializer<RequestToAddToCart>
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JsonToAddToCart() {
		this(null);
	}

	protected JsonToAddToCart(Class<RequestToAddToCart> vc) {
		super(vc);
	}

	@Override
	public RequestToAddToCart deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JacksonException {

			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(p);
				
				String productId = node.get("productId") != null ? node.get("productId").asText() : null;
				Integer quantity = node.get("quantity") != null ? node.get("quantity").asInt() : null;
				
				Set<Integer> choiceIds = new HashSet<Integer>();
				JsonNode jsonChoiceIds = node.get("choiceIds");
				if(jsonChoiceIds != null && jsonChoiceIds.isArray()) {
					Iterator<JsonNode> choiceIdsIter = jsonChoiceIds.iterator();
					choiceIdsIter.forEachRemaining(s -> {
							try {
								choiceIds.add(mapper.readValue(s.traverse(), Integer.class));
							} catch (JsonMappingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					});
				}
				
				RequestToAddToCart request =  new RequestToAddToCart(productId, quantity, choiceIds, null);
				out.println("HERE: CART REQUEST: " + request);
				return request;
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some errors occurred :(");
			}
	}
	
}
