package es.us.aws;

import java.util.List;

public class Movie {

	private String title;
	private String director;
	private Integer year;
	private Integer minutes;
	private List<Actor> actors;
	public Movie(){}
	public Movie(String title, String director, Integer year, Integer minutes,
			List<Actor> actors) {
		super();
		this.title = title;
		this.director = director;
		this.year = year;
		this.minutes = minutes;
		this.actors = actors;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMinutes() {
		return minutes;
	}
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}
	public List<Actor> getActors() {
		return actors;
	}
	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}
}
