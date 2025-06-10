package com.fita.vetclinic.models;

import java.util.Date;

public class Appointment {
	private int appointmentId;
	private int petId; // FK to tbl_pets
	private int doctorId; // FK to tbl_doctors
	private Date appointmentDate; 
	private String reason;
	private String status;

	public Appointment() {
	}

	public Appointment(int appointmentId, int petId, int doctorId, Date appointmentDate, String reason, String status) {
		this.appointmentId = appointmentId;
		this.petId = petId;
		this.doctorId = doctorId;
		this.appointmentDate = appointmentDate;
		this.reason = reason;
		this.status = status;
	}

	// --- Getters and Setters ---
	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}