package es.us.aws;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.gson.Gson;

public class MovieServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	
	private static List<Movie> peliculas;

	
	
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		peliculas = Movies.lista();

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String uri = req.getRequestURI();

		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			// Rama para GET unico

			String t = uricompoment[2];

			if (Movies.containsName(peliculas, t)) {

				Gson gson2 = new Gson();
				String jsonString2 = gson2.toJson(Movies.getName(peliculas, t));

				resp.setContentType("text/json");
				resp.getWriter().println(jsonString2);

			} else {
				// No existe recurso
				resp.sendError(404);
			}

		} else {
			// Rama para Get total
			Gson gson = new Gson();
			String jsonString = gson.toJson(peliculas);

			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String uri = req.getRequestURI();

		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			// Rama para POST unico

			resp.sendError(404);

		} else {

			Movie m = null;
			Gson gson = new Gson();
			StringBuilder sb = new StringBuilder();
			BufferedReader br = req.getReader();
			String jsonString;
			while ((jsonString = br.readLine()) != null) {
				sb.append(jsonString);
			}

			jsonString = sb.toString();

			try {
				m = gson.fromJson(jsonString, Movie.class);
			} catch (Exception e) {
				System.out.println("ERROR parsing Movie: " + e.getMessage());
			}

			// Comprobar que no existe previamente la pelicula
			peliculas.add(m);

			// Que debe devolver en este caso el service �?�?
			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String uri = req.getRequestURI();

		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			// Rama para GET unico

			String t = uricompoment[2];

			if (Movies.containsName(peliculas, t)) {

				Gson gson2 = new Gson();
				String jsonString2 = gson2.toJson(Movies.getName(peliculas, t));

				peliculas.remove(Movies.getName(peliculas, t));
				
				//???Se devolveria
				resp.setContentType("text/json");
				resp.getWriter().println(jsonString2);

			} else {
				// No existe recurso
				resp.sendError(404);
			}

		} else {
			// Rama para Get total
			Gson gson = new Gson();
			String jsonString = gson.toJson(peliculas);

			//Se queda vacia la lista de peliculas
			peliculas=new LinkedList<Movie>();
			
			
			//???Se devuelven todas las peliculas que se han borrado
			resp.setContentType("text/json");
			resp.getWriter().println(jsonString);
		}
		
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String uri = req.getRequestURI();

		String[] uricompoment = uri.split("/");

		if (uricompoment.length > 2) {
			// Rama para GET unico

			String t = uricompoment[2];

			if (Movies.containsName(peliculas, t)) {
				
				Movie m = null;
				Gson gson = new Gson();
				StringBuilder sb = new StringBuilder();
				BufferedReader br = req.getReader();
				String jsonString;
				while ((jsonString = br.readLine()) != null) {
					sb.append(jsonString);
				}

				jsonString = sb.toString();

				try {
					m = gson.fromJson(jsonString, Movie.class);
				} catch (Exception e) {
					System.out.println("ERROR parsing Movie: " + e.getMessage());
				}
				
				peliculas.remove(Movies.getName(peliculas, t));
				peliculas.add(m);
				

			} else {
				// No existe recurso
				resp.sendError(404);
			}

		} else {
			//No existe funcion
			resp.sendError(404);

		}

		
		
	}
	
	
}
