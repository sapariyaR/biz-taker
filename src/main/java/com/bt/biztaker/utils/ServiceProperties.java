package com.bt.biztaker.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ServiceProperties extends Properties {

	private static final long serialVersionUID = 1L;
	private static String CONFIG_FILE = "services.properties";
	
	private static ServiceProperties singletonInstance = null;

	public static ServiceProperties getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new ServiceProperties();
		}
		return singletonInstance;
	}
	
	private ServiceProperties() {
		try {
			FileInputStream is = new FileInputStream(new File(CONFIG_FILE));
			load(is);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public String getEnv() {
		return this.getProperty("env");
	}
}
