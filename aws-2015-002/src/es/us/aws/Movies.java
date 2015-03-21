package es.us.aws;

import java.util.LinkedList;
import java.util.List;

public class Movies{

public static List<Movie> lista(){
	List<Movie> res=new LinkedList<Movie>();
	
	Movie b=new Movie("Shutter Island","Martin Scorsese",new Integer(2010),new Integer(138));
	res.add(b);
	
	Movie b2=new Movie("Seven","David Fincher",new Integer(1995),new Integer(127));
	res.add(b2);
	
	Movie b3=new Movie("El club de la lucha","David Fincher",new Integer(1999),new Integer(139));
	res.add(b3);
	
	return res;
		
		
	}

public static boolean containsName(List<Movie> l, String n){
	boolean res=false;
	for(Movie m:l){
		if(m.getTitulo().equals(n))
			res=true;
		
	}
	return res;
}

public static Movie getName(List<Movie> l, String n){
	Movie res=new Movie();
	for(Movie m:l){
		if(m.getTitulo().equals(n)){
			//Pasar a pelo, deberia de ir con clone o algo asi
			res=m;			
		}
	}
	return res;
}


}
