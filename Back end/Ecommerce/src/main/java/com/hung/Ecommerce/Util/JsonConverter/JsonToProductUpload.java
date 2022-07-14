package com.hung.Ecommerce.Util.JsonConverter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.hung.Ecommerce.DTO.RequestToUploadProduct;
import com.hung.Ecommerce.Model.Product;
import com.hung.Ecommerce.Model.ProductOption;
import com.hung.Ecommerce.Model.ProductOptionStatus;
import com.nimbusds.jose.shaded.json.JSONArray;
import static java.lang.System.*;
public class JsonToProductUpload extends StdDeserializer<RequestToUploadProduct>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonToProductUpload() {
		this(null);
	}
	
	protected JsonToProductUpload(Class<RequestToUploadProduct> t) {
		super(t);
	}

	@Override
	public RequestToUploadProduct deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		
		try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(p);
				
				
				String name = node.get("name") != null? node.get("name").asText(): null; 
				
				Set<String> categories = new HashSet<String>();
				JsonNode jsonCategories = node.get("categories");
				if(jsonCategories != null && jsonCategories.isArray()) {
					Iterator<JsonNode> categoriesIter = jsonCategories.iterator();
					categoriesIter.forEachRemaining(s -> categories.add(s.asText()));
				}
				
				Set<String> characteristics = new HashSet<String>();
				JsonNode jsonCharacteristics = node.get("characteristics");
				if(jsonCharacteristics != null && jsonCharacteristics.isArray()){
					Iterator<JsonNode> characteristicsIter = jsonCharacteristics.iterator();
					characteristicsIter.forEachRemaining(ch -> characteristics.add(ch.asText()));
				}
				
				
				Set<ProductOption> productOptions = new HashSet<>();
				JsonNode jsonProductOptions = node.get("productOptions");
				if(jsonProductOptions != null && jsonProductOptions.isArray()) {
					Iterator<JsonNode> productOptionsIter = jsonProductOptions.iterator();
					productOptionsIter.forEachRemaining(s -> {
							try {
								ProductOption option = mapper.readValue(s.traverse(), ProductOption.class);
								option.setStatus(ProductOptionStatus.GENERATED);
								out.println("HERE: OPTION:"+option.getName());
								productOptions.add(option);
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
				
				Set<String> paymentMethods = new HashSet<>();
				JsonNode jsonPaymentMethods = node.get("paymentMethods");
				if(jsonPaymentMethods != null){
					Iterator<JsonNode> paymentIter = jsonPaymentMethods.iterator();
					paymentIter.forEachRemaining(m -> paymentMethods.add(m.asText()));
				}
				
				Double defaultUnitPrice = node.get("defaultUnitPrice") != null ? node.get("defaultUnitPrice").asDouble():0;
				Double deliveryFeePerUnit = node.get("deliveryFeePerUnit") != null? node.get("deliveryFeePerUnit").asDouble(): 0;
				Double maxDeliveryFee = node.get("maxDeliveryFee") != null? node.get("maxDeliveryFee").asDouble(): 0;
				String brand = node.get("brand") != null? node.get("brand").asText(): null; 
				int totalStock = node.get("totalStock") != null? node.get("totalStock").asInt(): 0;
				String unit = node.get("unit") != null? node.get("unit").asText(): null;
				String estimatedDeliveryTime = node.get("estimatedDeliveryTime") != null? node.get("estimatedDeliveryTime").asText(): null;
				String productDescription = node.get("productDescription") != null? node.get("productDescription").asText(): null;
				
				HashMap<String, String> specificDetails = null;
				JsonNode jsonSpecificDetails = node.get("specificDetails");
				if(jsonSpecificDetails != null && jsonSpecificDetails.isObject()) {
					specificDetails =  mapper.readValue(jsonSpecificDetails.traverse(), HashMap.class);
				}
				
				out.println("HERE: CATE: " + categories.toString());
				
				return new RequestToUploadProduct(name, categories, null, defaultUnitPrice, deliveryFeePerUnit,
						maxDeliveryFee, totalStock, estimatedDeliveryTime, unit, brand, characteristics, productOptions,
						specificDetails, productDescription, paymentMethods);
				
		}catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Some errors occurred :(");
		}
	}
}
