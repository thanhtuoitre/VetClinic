package com.fita.vetclinic.models;

public class Doctor {
	private int doctorId; // PK Doctor
	private int userId; // FK tbl_users
	private boolean is_active;
	private String specialization;

	public Doctor() {
	}

	public Doctor(int doctorId, int userId, boolean is_active, String specialization) {
		this.doctorId = doctorId;
		this.userId = userId;
		this.is_active = is_active;
		this.specialization = specialization;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "Bác sĩ ID: " + doctorId + " - " + specialization;
	}
}