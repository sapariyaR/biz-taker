package com.bt.biztaker.dto;
import java.security.Principal;

public class SuccessUserDto implements Principal {

	private Long userId;
	private String email;
	private String role;
	private String firstName;
	private String lastName;
	private String gender;
	private Long birthDate;
	private Double latitude;
	private Double longitude;
	private String address;
	private String dpUrl;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
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
	public String getDpUrl() {
		return dpUrl;
	}
	public void setDpUrl(String dpUrl) {
		this.dpUrl = dpUrl;
	}
	
	@Override
	public String getName() {
		return this.userId.toString();
	}
}
