package edu.pitt.rods.apollo.epidemicmodels;

public class ModelMetaData {
	String Name;
	String Author;
	String Organization;
	String Description;
	String Jurisdiction;

	public String getJurisdiction() {
		return Jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		Jurisdiction = jurisdiction;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getOrganization() {
		return Organization;
	}

	public void setOrganization(String organization) {
		Organization = organization;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
}
