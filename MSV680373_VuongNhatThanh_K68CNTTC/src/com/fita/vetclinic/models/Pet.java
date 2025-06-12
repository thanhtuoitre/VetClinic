package com.fita.vetclinic.models;

import java.util.Date;

public class Pet {
	private int petId;
	private String name;
	private String species;
	private String breed;
	private String gender;
	private Date birthdate;
	private double weight;
	private int userId;
	private String imagePath; 

	public Pet() {
	}

	
	public Pet(String name, String species, String breed, String gender, Date birthdate, double weight, int userId, String imagePath) {
	    this.name = name;
	    this.species = species;
	    this.breed = breed;
	    this.gender = gender;
	    this.birthdate = birthdate;
	    this.weight = weight;
	    this.userId = userId;
	    this.imagePath = imagePath;
	}

	public Pet(int petId, String name, String species, String breed, String gender, Date birthdate, double weight,
			int userId, String imagePath) {
		this.petId = petId;
		this.name = name;
		this.species = species;
		this.breed = breed;
		this.gender = gender;
		this.birthdate = birthdate;
		this.weight = weight;
		this.userId = userId;
		this.imagePath = imagePath;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getuserId() {
		return userId;
	}

	public void setuserId(int userId) {
		this.userId = userId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	public String toString() {
	    return "ID: " + petId + " - " + name;
	}

}
