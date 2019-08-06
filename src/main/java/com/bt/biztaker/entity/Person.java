package com.bt.biztaker.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ravi Sapariya
 * @Date 28/01/2018
 *
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {
	
	private static final long serialVersionUID = 1l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;
	
	@Column(length = 100,name="first_name")
	@JsonProperty
	private String firstName;
	
	@Column(length = 100,name="last_name")
	@JsonProperty
	private String lastName;
	
	@Column(length = 5,name="gender")
	@JsonProperty
	private Gender gender;
	
	@Column(length = 50,name="birth_date")
	@JsonProperty
	private Long birthDate;
	
	@Column(length = 100,name="latitude")
	@JsonProperty
	private Double latitude;
	
	@Column(length = 100,name="longitude")
	@JsonProperty
	private Double longitude;
	
	@Column(length = 200,name="address")
	@JsonProperty
	private String address;
	
	@Column(length = 500,name="token")
	@JsonProperty
	private String token;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(length = 50,name="updated_date",nullable=false)
	private Long updatedDate;
	
	@Column(length = 100,name="dp_url")
	@JsonProperty
	private String dpUrl;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Long getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Long birthDate) {
		this.birthDate = birthDate;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getDpUrl() {
		return dpUrl;
	}

	public void setDpUrl(String dpUrl) {
		this.dpUrl = dpUrl;
	}

}
