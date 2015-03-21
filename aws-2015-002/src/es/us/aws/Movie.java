package es.us.aws;


public class Movie {

	
	private String titulo;
	private String director;
	private Integer fecha;
	private Integer duracion;
	

	
	public Movie() {
		titulo=new String();
		director=new String();
		fecha= new Integer(0);
		duracion= new Integer(0);
	
	}
	
	
	
	
	public Movie(String titulo, String director, Integer fecha, Integer duracion) {
		super();
		this.titulo = titulo;
		this.director = director;
		this.fecha = fecha;
		this.duracion = duracion;
	}


	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public Integer getFecha() {
		return fecha;
	}
	public void setFecha(Integer fecha) {
		this.fecha = fecha;
	}
	public Integer getDuracion() {
		return duracion;
	}
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}
}
