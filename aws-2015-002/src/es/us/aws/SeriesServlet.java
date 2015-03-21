package es.us.aws;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class SeriesServlet extends HttpServlet {

	private static Map<String,Series> series;

	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		series = new HashMap<String,Series>();
		
		Series s = new Series("Game of thrones","",20,2010);
		series.put(iniciales(s.name), s);
		s = new Series("House of cards","",20,2010);
		series.put(iniciales(s.name), s);
		s = new Series("Breaking Bad","",20,2010);
		series.put(iniciales(s.name), s);
		s = new Series("Big Bang Theory","",20,2010);
		series.put(iniciales(s.name), s);


	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			String s = uricompoment[2];
			
			if (series.containsKey(s)) {

				Gson gson2 = new Gson();
				String jsonString2 = gson2.toJson(series.get(s));
				resp.setContentType("text/json");
				resp.getWriter().println(jsonString2);
			} 
			else {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

		} 
		else {
			
			Gson gson = new Gson();
			String jsonString = gson.toJson(series);

			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {	
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} 
		else {

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
			
			String iniciales=iniciales(s.name)+"("+String.valueOf(s.year)+")";
			
			if(!series.containsKey(iniciales)){
				series.put(iniciales, s);
			}
			else{
				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
			
			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}
	}

	
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			String clave = uricompoment[2];

			if (series.containsKey(clave)) {

				Gson gson2 = new Gson();
				String jsonString2 = gson2.toJson(series.get(clave));

				series.remove(clave);
				
				//???Se devolveria
				resp.setContentType("text/json");
				resp.getWriter().println(jsonString2);

			} else {
				// No existe recurso
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

		} else {
			// Rama para Get total
			Gson gson = new Gson();
			String jsonString = gson.toJson(series);

			//Se queda vacia la lista de peliculas
			series.clear();
			
			//???Se devuelven todas las peliculas que se han borrado
			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}
		
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String uri = req.getRequestURI();
		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			String clave = uricompoment[2];

			if (series.containsKey(clave)){
				
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
				
				series.put(clave,s);
				

			} else {
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}

		} 
		else {
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
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
