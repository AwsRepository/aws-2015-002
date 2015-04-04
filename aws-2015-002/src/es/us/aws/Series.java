package es.us.aws;

public class Series {

	String title;
	String creator;
	int seasons;  
	int episodes;  
	int year;
	
	public Series(){
	}

	public Series(String title, String creator, int seasons, int episodes,
			int year) {
		super();
		this.title = title;
		this.creator = creator;
		this.seasons = seasons;
		this.episodes = episodes;
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getSeasons() {
		return seasons;
	}

	public void setSeasons(int seasons) {
		this.seasons = seasons;
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
