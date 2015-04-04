package es.us.aws;

import java.util.Date;

public class Actor {

	private String name;
	private String surname;
	private String gender;
	private int year;
	private String country;
	
	public Actor(){};
	
	public Actor(String name, String surname,String gender, int year, String country) {
		super();
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.year = year;
		this.country = country;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
