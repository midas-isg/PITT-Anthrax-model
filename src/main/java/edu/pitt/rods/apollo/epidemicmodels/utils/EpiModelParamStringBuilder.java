package edu.pitt.rods.apollo.epidemicmodels.utils;

public class EpiModelParamStringBuilder {
	private String paramString = "";

	public void addParam(String name, String type, String value) {
		paramString += name + ":" + type + "=" + value + ";";
	}

	public String getParamString() {
		return paramString;
	}

}
