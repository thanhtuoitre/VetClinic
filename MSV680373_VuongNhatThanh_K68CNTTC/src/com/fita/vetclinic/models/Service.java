package com.fita.vetclinic.models;

public class Service {
	private int serviceId;
	private String name; // Tên dịch vụ
	private double price;
	private String description;

	public Service() {
	}

	public Service(int serviceId, String name, double price, String description) {
		this.serviceId = serviceId;
		this.name = name;
		this.price = price;
		this.description = description;
	}

	// --- Getters and Setters ---
	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}