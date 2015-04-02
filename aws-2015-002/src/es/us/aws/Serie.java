package es.us.aws;

public class Serie {

	String name;
	String director;
	int episodes;  
	int year;
	
	public Serie(){
	}
	
	public Serie(String name, String director, int episodes, int year) {
		super();
		this.name = name;
		this.director = director;
		this.episodes = episodes;
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getEpisodes() {
		return episodes;
	}

	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	
}
