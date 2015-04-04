package es.us.aws;

public class Movie {

	private String title;
	private String director;
	private Integer year;
	private Integer minutes;

	public Movie(){}
	public Movie(String title, String director, Integer year, Integer minutes) {
		super();
		this.title = title;
		this.director = director;
		this.year = year;
		this.minutes = minutes;
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
}
