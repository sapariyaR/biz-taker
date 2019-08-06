package com.bt.biztaker.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CardMappingQRDto {
	
	@NotBlank(message = "QR code cannot be null or empty")
	public String qrstring;
	public Double latitude;
	public Double longitude;
}
