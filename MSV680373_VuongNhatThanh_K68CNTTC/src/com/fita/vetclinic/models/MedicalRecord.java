package com.fita.vetclinic.models;

import java.util.Date;

public class MedicalRecord {

	private int recordId;

	private int petId; // FK to tbl_pets

	private int doctorId; // FK to tbl_doctors

	private Date recordDate;

	private String diagnosis;

	private String treatment;

	private String notes;

	public MedicalRecord() {

	}

	public MedicalRecord(int recordId, int petId, int doctorId, Date recordDate, String diagnosis, String treatment,
			String notes) {

		this.recordId = recordId;

		this.petId = petId;

		this.doctorId = doctorId;

		this.recordDate = recordDate;

		this.diagnosis = diagnosis;

		this.treatment = treatment;

		this.notes = notes;

	}

	public int getRecordId() {

		return recordId;

	}

	public void setRecordId(int recordId) {

		this.recordId = recordId;

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

	public Date getRecordDate() {

		return recordDate;

	}

	public void setRecordDate(Date recordDate) {

		this.recordDate = recordDate;

	}

	public String getDiagnosis() {

		return diagnosis;

	}

	public void setDiagnosis(String diagnosis) {

		this.diagnosis = diagnosis;

	}

	public String getTreatment() {

		return treatment;

	}

	public void setTreatment(String treatment) {

		this.treatment = treatment;

	}

	public void setNotes(String notes) {

		this.notes = notes;

	}

	public String getNotes() {

		return notes;

	}

}