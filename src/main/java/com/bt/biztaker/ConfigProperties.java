package com.bt.biztaker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigProperties {

	@Valid
	@NotNull
	@JsonProperty("myProperty")
	private String myProperty;

	public String getMyProperty() {
		return myProperty;
	}

	public void setMyProperty(String myProperty) {
		this.myProperty = myProperty;
	}

}
