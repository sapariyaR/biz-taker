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
@Table(name = "company")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@Column(length = 100,name="company_name")
	@JsonProperty
	private String companyName;
	
	@Column(length = 500,name="company_address")
	@JsonProperty
	private String companyAddress;
	
	@Column(length = 100,name="latitude")
	@JsonProperty
	private Double latitude;
	
	@Column(length = 100,name="longitude")
	@JsonProperty
	private Double longitude;
	
	@ManyToOne
    @JoinColumn(name = "created_by", nullable= false)
	private Person createdby;
	
	@Column(length = 50,name="created_date")
	@JsonProperty
	private Long createdDate;

	@Transient
	private List<MetaData> metaDatas;
	
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Person getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Person createdby) {
		this.createdby = createdby;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	
}
