package com.bt.biztaker.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bt.biztaker.entity.Card;
import com.bt.biztaker.entity.Company;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonUtils {

	public static final Logger LOGGER = Logger.getLogger(JsonUtils.class.getName());
	private static Gson gson = new Gson();

	public static Gson getGsonInstance() {
		return gson;
	}
	public static String getMessageJson(String message) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("message", message);
		return gson.toJson(jsonObject);
	}

	public static String getJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Hibernate5Module()).setSerializationInclusion(Include.NON_NULL);
		objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		String json = null;
		try {
			json = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return json;
	}

	public static String getJson(JsonObject jsonObject) {
		return gson.toJson(jsonObject);
	}
	
	public static JsonObject parseMyCards(List<Card> cards) {
		JsonObject data = new JsonObject();
		JsonArray cardList = new JsonArray();
		JsonArray companyList = new JsonArray();
		Set<Long> companyIds = new HashSet<>();
		for(Card card : cards) {
			cardList.add(parseCardObject(card));
			if(!companyIds.contains(card.getCompany().getId())) {
				companyList.add(parseCompany(card.getCompany()));
				companyIds.add(card.getCompany().getId());
			}
			
		}
		data.add("cards", cardList);
		data.add("companys",companyList);
		return data;
	}
	private static JsonObject parseCompany(Company company) {
		JsonObject companyObject = new JsonObject();
		companyObject.addProperty("id", company.getId());
		companyObject.addProperty("companyName", company.getCompanyName());
		companyObject.addProperty("companyAddress", company.getCompanyAddress());
		companyObject.addProperty("createdbyId", company.getCreatedby().getId());
		companyObject.addProperty("createdbyName", company.getCreatedby().getFirstName()+" "+company.getCreatedby().getLastName());
		companyObject.addProperty("latitude", company.getLatitude());
		companyObject.addProperty("longitude", company.getLongitude());
		companyObject.addProperty("createdDate", company.getCreatedDate());
		companyObject.add("metas", JsonUtils.getGsonInstance().toJsonTree(company.getMetaDataList()));
		return companyObject;
	}
	
	private static JsonObject parseCardObject(Card card) {
		JsonObject cardObject = new JsonObject();
		cardObject.addProperty("cardId", card.getId());
		cardObject.addProperty("companyId", card.getCompany().getId());
		cardObject.addProperty("cardTitle", card.getCardTitle());
		cardObject.addProperty("cardDescription", card.getCardDescription());
		cardObject.addProperty("mobileNumber", card.getMobileNumber());
		cardObject.addProperty("phoneNumber",card.getPhoneNumber());
		cardObject.addProperty("cardAccessibility", card.getCardAccessibility().toString());
		cardObject.addProperty("createdDate", card.getCreatedDate());
		cardObject.addProperty("createdbyId", card.getCreatedby().getId());
		cardObject.addProperty("createdbyName", card.getCreatedby().getFirstName()+" "+card.getCreatedby().getLastName());
		cardObject.addProperty("updatedDate", card.getUpdatedDate());
		cardObject.addProperty("isMine", card.getIsMine());
		cardObject.add("metas", JsonUtils.getGsonInstance().toJsonTree(card.getMetaDataList()));
		return cardObject;
		
	}

}
