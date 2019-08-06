package com.bt.biztaker.dto;

import org.hibernate.validator.constraints.NotBlank;

public class LoginDto {

	@NotBlank(message = "email cannot be null or empty")
	public String email;
	
	@NotBlank(message = "password cannot be null or empty")
	public String password;
}
