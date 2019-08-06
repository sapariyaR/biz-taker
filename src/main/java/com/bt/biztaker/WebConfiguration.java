package com.bt.biztaker;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookieAuthConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;

public class WebConfiguration extends Configuration implements AssetsBundleConfiguration {

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@Valid
	@NotNull
	private JwtCookieAuthConfiguration jwtCookieAuth = new JwtCookieAuthConfiguration();

	/****** Responsible for assets folder outside of jar *****/
	@Valid
	@NotNull
	@JsonProperty("assets")
	private final AssetsConfiguration assets = new AssetsConfiguration();
	
	@Valid
	@NotNull
	@JsonProperty("configProperties")
	private final ConfigProperties configProperties = new ConfigProperties();
	

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.database = dataSourceFactory;
	}

	@JsonProperty("jwtCookieAuth")
	public JwtCookieAuthConfiguration getJwtCookieAuth() {
		return jwtCookieAuth;
	}

	@Override
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	}

	public ConfigProperties getConfigProperties() {
		return configProperties;
	}
	
	@Valid
	@NotEmpty
    private String jwtTokenSecret = "dfwzsdzwh823zebdwdz772632gdsbd";

    public byte[] getJwtTokenSecret() throws UnsupportedEncodingException {
        return jwtTokenSecret.getBytes("UTF-8");
    }
}
