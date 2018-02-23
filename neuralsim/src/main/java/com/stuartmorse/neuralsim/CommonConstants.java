package com.stuartmorse.neuralsim;

public enum CommonConstants {

	RESOURCE_PATH("simulatorDefaults.properties");

	private String value;

	CommonConstants(String value) {
		this.value = value;
	}

	String getValue() {
		return value;
	}
}
