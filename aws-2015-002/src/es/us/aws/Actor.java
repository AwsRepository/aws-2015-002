package es.us.aws;

import java.util.Date;

public class Actor {

	private String name;
	private String surname;
	private Date birthday;
	private String country;
	
	public Actor(){};
	
	public Actor(String name, String surname, Date birthday, String country) {
		super();
		this.name = name;
		this.surname = surname;
		this.birthday = birthday;
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
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
