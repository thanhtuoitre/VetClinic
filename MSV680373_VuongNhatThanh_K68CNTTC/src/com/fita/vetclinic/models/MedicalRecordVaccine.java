package com.fita.vetclinic.models;
import java.util.Date;

public class MedicalRecordVaccine {
	private int recordId; // Part of composite PK, FK to MedicalRecord
	private int vaccineId; // Part of composite PK, FK to Vaccine
	private Date vaccinationDate; // Part of composite PK
	private String batchNumber;
	private Date nextDueDate;

	public MedicalRecordVaccine() {
	}

	public MedicalRecordVaccine(int recordId, int vaccineId, Date vaccinationDate, String batchNumber,
			Date nextDueDate) {
		this.recordId = recordId;
		this.vaccineId = vaccineId;
		this.vaccinationDate = vaccinationDate;
		this.batchNumber = batchNumber;
		this.nextDueDate = nextDueDate;
	}

	// --- Getters and Setters ---
	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(int vaccineId) {
		this.vaccineId = vaccineId;
	}

	public Date getVaccinationDate() {
		return vaccinationDate;
	}

	public void setVaccinationDate(Date vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(Date nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

}