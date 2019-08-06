package com.bt.biztaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);
	private static ApplicationContext instance = null;
	private ConfigProperties configProperties;

	private ApplicationContext(WebConfiguration webConfiguration) {
		this.configProperties = webConfiguration.getConfigProperties();
	}

	public static ApplicationContext init(WebConfiguration webConfiguration) {
		instance = new ApplicationContext(webConfiguration);
		return get();
	}

	public static ApplicationContext get() {
		if (instance == null) {
			LOGGER.error("Application Context is not initialized !");
			throw new RuntimeException("Application Context is not initialized !");
		}
		return instance;
	}

	public ConfigProperties getConfigProperties() {
		return configProperties;
	}

}
