package com.fita.vetclinic.models;

public class Vaccine {
	private int vaccineId;
	private String name; // Tên vaccine
	private String description;
	private String manufacture; // Name directly from image ("manufacture")

	public Vaccine() {
	}

	public Vaccine(int vaccineId, String name, String description, String manufacture) {
		this.vaccineId = vaccineId;
		this.name = name;
		this.description = description;
		this.manufacture = manufacture;
	}

	public int getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(int vaccineId) {
		this.vaccineId = vaccineId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

}