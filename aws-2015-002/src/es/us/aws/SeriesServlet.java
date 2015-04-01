package es.us.aws;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class SeriesServlet extends HttpServlet {

	private static final long serialVersionUID = 2652891130816683939L;

//	public void init() throws ServletException {
//		super.init();
//
//		SeriesPersistence.deleteAllSeries();
//		Series s1 = new Series("Game of thrones","",20,2010);
//		SeriesPersistence.insertSeries(s1, iniciales(s1.getName()));
//		Series s2 = new Series("House of cards","",20,2010);
//		SeriesPersistence.insertSeries(s2, iniciales(s2.getName()));
//		Series s3 = new Series("Breaking Bad","",20,2010);
//		SeriesPersistence.insertSeries(s3, iniciales(s3.getName()));
//		Series s4 = new Series("Big Bang Theory","",20,2010);
//		SeriesPersistence.insertSeries(s4, iniciales(s4.getName()));
//		
//		Actor a = new Actor("Kevin", "Spacey", new Date(), "EEUU");
//		SeriesPersistence.insertSeriesActor(a, "HOC");
//
//
//	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");
		
		if (uricompoment.length > 2) {//Serie concreta
			
			String s = uricompoment[2];//ID de la serie
			
			if (SeriesPersistence.existsSeries(s)) {
				
				if(uricompoment.length > 3){
					if (uricompoment[3].equalsIgnoreCase("actors")){
						//Actores de la serie concreta
						Gson gson2 = new Gson();
						String jsonString2 = gson2.toJson(SeriesPersistence.selectSeriesActors(s));
						resp.setContentType("text/json");
						resp.getWriter().println(jsonString2);
					}
					else{
						resp.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				}
				else{ //Datos de serie concreta
					Gson gson2 = new Gson();
					String jsonString2 = gson2.toJson(SeriesPersistence.selectSeries(s));
					resp.setContentType("text/json");
					resp.getWriter().println(jsonString2);
				}
			} 
			else {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

		} 
		else { //Obtiene la lista completa de series
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(SeriesPersistence.selectAllSeries());
			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {	
			
			String s = uricompoment[2];//ID de la serie
			
			if (SeriesPersistence.existsSeries(s)) {
				if(uricompoment.length > 3){
					if(uricompoment[3].equalsIgnoreCase("actors")){
						//Añadir actor a la serie
						Actor a = new Actor();
						Gson gson = new Gson();
						StringBuilder sb = new StringBuilder();
						BufferedReader br = req.getReader();
						String jsonString;
						while ((jsonString = br.readLine()) != null) {
							sb.append(jsonString);
						}

						jsonString = sb.toString();
						
						a = gson.fromJson(jsonString, Actor.class);
						
						SeriesPersistence.insertSeriesActor(a, s);
						
					}
					else{
						resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					}
				}
				else{
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);				}
			
			}
		}
		 
		else {//Crear nueva serie

			Series s = new Series();
			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = req.getReader();
			String jsonString;
			while ((jsonString = br.readLine()) != null) {
				sb.append(jsonString);
			}

			jsonString = sb.toString();
			
			s = gson.fromJson(jsonString, Series.class);
			
			String iniciales=iniciales(s.name);
			
			if(!SeriesPersistence.existsSeries(iniciales)){
				SeriesPersistence.insertSeries(s, iniciales);
			}
			else{
				resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}
			
			
		}
	}

	
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			String key = uricompoment[2];
			if(uricompoment.length > 3){
				if(uricompoment[3].equalsIgnoreCase("actors")){
					if (SeriesPersistence.existsSeries(key)) {
						SeriesPersistence.deleteSeriesActors(key);
					}
					else{
						resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}
				}
				else{
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			}
			else{
				if (SeriesPersistence.existsSeries(key)) {


				SeriesPersistence.deleteSeries(key);


				} else {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			}
		} 
		else {
			
			Gson gson = new Gson();
			gson.toJson(SeriesPersistence.selectAllSeries());

			//Se queda vacia la lista de series
			SeriesPersistence.deleteAllSeries();	

		}
		
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			String key = uricompoment[2];

			if (SeriesPersistence.existsSeries(key)){
				
				Series s = new Series();
				Gson gson = new Gson();
				StringBuilder sb = new StringBuilder();
				BufferedReader br = req.getReader();
				String jsonString;
				while ((jsonString = br.readLine()) != null) {
					sb.append(jsonString);
				}

				jsonString = sb.toString();
				s = gson.fromJson(jsonString, Series.class);
				
				SeriesPersistence.updateSeries(s, key);
				

			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} 
		else{
			resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}	
	}
	
	public String iniciales(String name){
		String iniciales="";
		String[] words = name.split(" ");
		for(int i=0;i<words.length;i++){
			iniciales+=words[i].substring(0, 1).toUpperCase();
		}
		return iniciales;
	}
}
