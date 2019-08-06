package com.bt.biztaker.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bt.biztaker.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;

@Entity
@Table(name = "card")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@ManyToOne
	@JsonProperty
    @JoinColumn(name = "company_id", nullable= false)
	private Company company;
	
	@Column(length = 100,name="card_title")
	@JsonProperty
	private String cardTitle;
	
	@Column(length = 500,name="card_description")
	@JsonProperty
	private String cardDescription;
	
	@Column(length = 20,name="mobile_number")
	@JsonProperty
	private Integer mobileNumber;
	
	@Column(length = 20,name="phone_number")
	@JsonProperty
	private Integer phoneNumber;
	
	@Column(length = 5,name="accessibility")
	@JsonProperty
	private CardAccessibility cardAccessibility;
	
	@Column(length = 50,name="created_date")
	@JsonProperty
	private Long createdDate;
	
	@ManyToOne
    @JoinColumn(name = "created_by", nullable= false)
	private Person createdby;
	
	@Transient
	private List<MetaData> metaDatas;
	
	@Column(length = 50,name="updated_date",nullable=false)
	@JsonProperty
	private Long updatedDate;
	
	@Column(name="isMine",nullable=false)
	@JsonProperty
	private Boolean isMine = false;
	
	@Access(javax.persistence.AccessType.PROPERTY)
	@Column(name="metaDatasJson",length = 2000)
	public String getMetaDatas() {
		if(this.metaDatas != null) {
			return JsonUtils.getGsonInstance().toJson(this.metaDatas);
		}
		return null;
	}
	public List<MetaData> getMetaDataList() {
		return metaDatas;
	}

	public void setMetaDatas(String metaDatasJson) {
		if(metaDatasJson != null && !StringUtils.isNullOrEmpty(metaDatasJson)) {
			this.metaDatas = JsonUtils.getGsonInstance().fromJson(metaDatasJson,new TypeToken<ArrayList<MetaData>>() {}.getType());
		}
	}
	public void setMetaData(MetaData meta) {
		if(this.metaDatas == null) {
			this.metaDatas = new ArrayList<>(1);
		}
		this.metaDatas.add(meta);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getCardTitle() {
		return cardTitle;
	}
	public void setCardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
	}
	public String getCardDescription() {
		return cardDescription;
	}
	public void setCardDescription(String cardDescription) {
		this.cardDescription = cardDescription;
	}
	public Integer getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(Integer mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Integer getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public CardAccessibility getCardAccessibility() {
		return cardAccessibility;
	}
	public void setCardAccessibility(CardAccessibility cardAccessibility) {
		this.cardAccessibility = cardAccessibility;
	}
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public Person getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Person createdby) {
		this.createdby = createdby;
	}
	public Long getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Boolean getIsMine() {
		return isMine;
	}
	public void setIsMine(Boolean isMine) {
		this.isMine = isMine;
	}
	
}
