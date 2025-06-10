package com.fita.vetclinic.models;

public class MedicalRecordService {
	private int recordId;
	private int serviceId;
	private int quantity;
	private double servicePriceAtTime;

	public MedicalRecordService() {
	}

	public MedicalRecordService(int recordId, int serviceId, int quantity, double servicePriceAtTime) {
		this.recordId = recordId;
		this.serviceId = serviceId;
		this.quantity = quantity;
		this.servicePriceAtTime = servicePriceAtTime;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getServicePriceAtTime() {
		return servicePriceAtTime;
	}

	public void setServicePriceAtTime(double servicePriceAtTime) {
		this.servicePriceAtTime = servicePriceAtTime;
	}

}