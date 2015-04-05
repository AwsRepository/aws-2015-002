package es.us.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import es.us.aws.Actor;
import es.us.aws.Series;
import es.us.aws.SeriesPersistence;

public class SeriesServlet extends HttpServlet {

	private static final long serialVersionUID = 2652891130816683939L;
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(SeriesServlet.class.getName());
	
	Gson gson = new Gson();

	public void init() throws ServletException {
		super.init();

		//SeriesPersistence.deleteAllSeries();
		
		Series s1 = new Series("Game of thrones","David Benioff",5,40,2011);
		Series s2 = new Series("Breaking Bad","Vince Gilligan",5,62,2008);
		Series s3 = new Series("House of cards","Beau Willimon",3,39,2010);

		
		SeriesPersistence.insertSeries(s1);
		SeriesPersistence.insertSeries(s2);
		SeriesPersistence.insertSeries(s3);
		
		Actor a11 = new Actor("Peter", "Dinklage","Male", 1969, "EEUU");
		Actor a12 = new Actor("Emilia", "Clarke","Female", 1986, "England");
		
		SeriesPersistence.insertSeriesActorInit(a11, s1.getTitle());
		SeriesPersistence.insertSeriesActorInit(a12, s1.getTitle());
		
		Actor a21 = new Actor("Aaron", "Paul", "Male", 1979, "EEUU");
		Actor a22 = new Actor("Bryan", "Cranston", "Male", 1956, "EEUU");
		
		SeriesPersistence.insertSeriesActorInit(a21, s2.getTitle());
		SeriesPersistence.insertSeriesActorInit(a22, s2.getTitle());
		
		Actor a31 = new Actor("Kevin", "Spacey", "Male", 1959, "EEUU");
		Actor a32 = new Actor("Robin", "Wright", "Female", 1966, "EEUU");
		
		SeriesPersistence.insertSeriesActorInit(a31, s3.getTitle());
		SeriesPersistence.insertSeriesActorInit(a32, s3.getTitle());
		
		log("End init");
		
	}

	//Métodos HTTP - Copiamos la idea del ejemplo de clase, el código queda mucho más legible
	
	public void doGet (HttpServletRequest req, HttpServletResponse res) {
//		try {
//			init();
//		} catch (ServletException e) {
//			e.printStackTrace();
//		}
		
		process(req,res);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	public void doPut (HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	public void doDelete (HttpServletRequest req, HttpServletResponse res) {
		process(req,res);
	}
	
	public void process (HttpServletRequest req, HttpServletResponse res){
		
		String method = req.getMethod();
		String path = req.getPathInfo();
	
		log(req.getRequestURI() + " : ["+method+"|"+path+"] ");		

		if (path != null && !path.equals("/")){

			String[] pathComponents = path.split("/");
			String resource = pathComponents[1]; //ID de la Serie
			
			log("Single action over resource '"+resource+"'");	
			
			if(pathComponents.length>2 && pathComponents[2].equalsIgnoreCase("actors")){
				//ACTORES
				processActors(method, pathComponents[1] , req, res);
			}
			else{
				processSeries(method, pathComponents[1] , req, res);
			}
		}
		else{
			log("Action over the list of resources");		
			processResourceList(method, req, res);
		}

	}
	
	
	private void processSeries(String method, String resource,
			HttpServletRequest req, HttpServletResponse res) {

		if(method == "POST"){
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
				
		if(!SeriesPersistence.existsSeries(resource)){
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		

		switch (method){
	
			case "PUT" : updateSeries(resource, req, res); break;
	
			case "GET" : getSeries(resource, req, res); break;
	
			case "DELETE" : deleteSeries(resource, req, res); break;				
		}
	}
	
	
	private void processResourceList(String method, HttpServletRequest req,
			HttpServletResponse res)  {
		
		switch (method){
			case "POST" : postSeries(req,res); break;
	
			case "PUT" : res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); break;
	
			case "GET" : getAllSeries(req,res); break;
	
			case "DELETE" : deleteAllSeries(req,res); break;
		}
	}
	
	//Crear nueva serie
	private void postSeries(HttpServletRequest req, HttpServletResponse resp){
		
		try{
			Series s = new Series();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = req.getReader();
			String jsonString;
			while ((jsonString = br.readLine()) != null) {
				sb.append(jsonString);
			}
	
			jsonString = sb.toString();
			
			s = gson.fromJson(jsonString, Series.class);
			
			// el titulo debe estar en camel case por eso se crea otro exists
			if(!SeriesPersistence.existsSeriesPost(s.getTitle())){
				SeriesPersistence.insertSeries(s);
			}
			else{
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
			}

		}
		catch(Exception e){
			
		}
		
	}
	
	//Listado de series
	private void getAllSeries(HttpServletRequest req, HttpServletResponse resp){
		try {
			
			Map<String,String> params = getSearchParameters(req, resp);
			
			String jsonString = gson.toJson(SeriesPersistence.selectAllSeries(params));
			
			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		} 
		catch (IOException e) {
			log(e.toString());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
	
	//Borrar todas las series
	private void deleteAllSeries(HttpServletRequest req, HttpServletResponse resp){
		SeriesPersistence.deleteAllSeries();
	}
	
	//Obtener serie
	private void getSeries(String series, HttpServletRequest req, HttpServletResponse res){
		String jsonString = gson.toJson(SeriesPersistence.selectSeries(series));
		res.setContentType("text/json");
		try {
			res.getWriter().println(jsonString);
		} catch (IOException e) {
			log(e.toString());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
	private void updateSeries(String series, HttpServletRequest req, HttpServletResponse res){
		try{
			Series s = new Series();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = req.getReader();
			String jsonString;
			while ((jsonString = br.readLine()) != null) {
				sb.append(jsonString);
			}

			jsonString = sb.toString();
			s = gson.fromJson(jsonString, Series.class);
			
			SeriesPersistence.updateSeries(s, series);
		}
		catch(Exception e){
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
	}
	
	private void deleteSeries(String series, HttpServletRequest req, HttpServletResponse resp){
		SeriesPersistence.deleteSeries(series);
	}
	
	private void processActors(String method, String series,
			HttpServletRequest req, HttpServletResponse res) {

		if(!SeriesPersistence.existsSeries(series)){
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		switch (method){
	
			case "POST" : postActor(series,req,res); break;
			
			case "PUT" : res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); break;
	
			case "GET" : getActors(series,req,res); break;
	
			case "DELETE" : deleteActors(series,req,res); break;			
		}
	}
	
	//Añade un nuevo actor a la serie
	private void postActor(String series, HttpServletRequest req, HttpServletResponse res){
		try{
			Actor a = new Actor();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = req.getReader();
			String jsonString;
			while ((jsonString = br.readLine()) != null) {
				sb.append(jsonString);
			}
	
			jsonString = sb.toString();
			
			a = gson.fromJson(jsonString, Actor.class);
			
			SeriesPersistence.insertSeriesActor(a, series);
		}
		catch(Exception e){
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
	
	//Obtiene la lista de actores de la serie
	private void getActors(String series, HttpServletRequest req, HttpServletResponse res){
		try{
						
			Map<String,String> params = getSearchParameters(req, res);
			
			String jsonString = gson.toJson(SeriesPersistence.selectSeriesActors(series,params));
			res.setContentType("text/json");
			res.getWriter().println(jsonString);
		}
		catch(Exception e){
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
	
	//Borra todos los actores de la serie
	private void deleteActors(String series, HttpServletRequest req, HttpServletResponse res){
		SeriesPersistence.deleteSeriesActors(series);
	}
	
	private Map<String,String> getSearchParameters(HttpServletRequest req, HttpServletResponse res){
		
		Map<String,String> params = new HashMap<String, String>();
		
		if(req.getParameter("withActors")!=null){
			params.put("withActors", req.getParameter("withActors"));
		}
		if(req.getParameter("from")!=null){
			params.put("from", req.getParameter("from"));
		}
		if(req.getParameter("limit")!=null){
			params.put("limit", req.getParameter("limit"));
		}
		if(req.getParameter("offset")!=null){
			params.put("offset", req.getParameter("offset"));
		}
		
		
		return params;
	}
	   
}
