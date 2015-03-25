package es.us.aws;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class SeriesPersistence {
	
	
	//TODO no devolver HTML, error conflict si existe en post, 
	
	public static void insertSeries(Series s, String key){
		Key seriesKey = KeyFactory.createKey("Series", s.getName());
	    Entity series = new Entity("Series", seriesKey);
	    series.setProperty("name", s.getName());
	    series.setProperty("director", s.getDirector());
	    series.setProperty("episodes", s.getEpisodes());
	    series.setProperty("year", s.getYear());
	 
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(series); 
	}
	
	public static Series selectSeries(String key){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key seriesKey = KeyFactory.createKey("Series", key);
	    Query query = new Query("Series", seriesKey);
	    PreparedQuery pq = datastore.prepare(query);
		Entity e = pq.asSingleEntity();
	    Series series = new Series();
	    series.setName((String) e.getProperty("name"));
		series.setDirector((String) e.getProperty("director"));
		series.setEpisodes( (int)(long) e.getProperty("episodes"));
		series.setYear((int) (long)e.getProperty("year"));
		return series;
	    
	}
	
	public static boolean existsSeries(String key){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key seriesKey = KeyFactory.createKey("Series", key);
	    Query query = new Query("Series", seriesKey);
	    PreparedQuery pq = datastore.prepare(query);
		return pq.asSingleEntity()!=null;
	 
	}
	
	public static List<Series> selectAllSeries(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Query query = new Query("Series");
	    PreparedQuery pq = datastore.prepare(query);
		Iterable <Entity> it = pq.asIterable();
		List<Series> series = new LinkedList<Series>();
		
		for(Entity e:it){
			Series s = new Series();
			s.setName((String) e.getProperty("name"));
			s.setDirector((String) e.getProperty("director"));
			s.setEpisodes( (int) (long) e.getProperty("episodes"));
			s.setYear((int) (long) e.getProperty("year"));
			series.add(s);
		}
		
		return series;
	    
	}
	
	public static void updateSeries(Series s, String key){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Series");
		FilterPredicate filtro = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, key);
		query.setFilter(filtro);
		PreparedQuery pq = datastore.prepare(query);
		Entity series = pq.asSingleEntity();
		
		series.setProperty("name", s.getName());
		series.setProperty("director", s.getDirector());
		series.setProperty("episodes", s.getEpisodes());
		series.setProperty("year", s.getYear());
		
        datastore.put(series);

		
	}
	
	public static void deleteSeries(String key){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		FilterPredicate filtro = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, key);
		Query query = new Query("Series");
        query.setFilter(filtro);
        PreparedQuery pq = datastore.prepare(query);
        Entity series = pq.asSingleEntity();
        datastore.delete(series.getKey());
	}
	
	public static List<Actor> selectSeriesActors(String key){
		List<Actor> actors = new LinkedList<Actor>(); 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key seriesKey = KeyFactory.createKey("Series", key);
		Query query = new Query("Actor").setAncestor(seriesKey);
		PreparedQuery pq = datastore.prepare(query);
		Iterator<Entity> it = pq.asIterator();
		while(it.hasNext()){
			Actor a = new Actor();
			Entity e = it.next();
			a = new Actor();
			a.setName((String) e.getProperty("name"));
			a.setSurname((String) e.getProperty("surname"));
			a.setBirthday( (Date) e.getProperty("birhtday"));
			a.setCountry( (String) e.getProperty("country"));
			actors.add(a);
		}
		return actors;
	}
	
	public static void insertSeriesActor(Actor a, String key){
		Key seriesKey = KeyFactory.createKey("Series", key);
		Entity actor = new Entity("Actor", "actor_id", seriesKey);
		//series.setProperty("key", key);
		actor.setProperty("name", a.getName());
		actor.setProperty("surname", a.getSurname());
		actor.setProperty("date", a.getBirthday());
		actor.setProperty("country", a.getCountry());
	 
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(actor); 
	}
	
	public static void deleteSeriesActor(String aKey, String key){
		Key seriesKey = KeyFactory.createKey("Series", key);
		Key actorKey = KeyFactory.createKey("Series", aKey);
		FilterPredicate filtro = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, actorKey);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Actor").setAncestor(seriesKey);
		query.setFilter(filtro);
        PreparedQuery pq = datastore.prepare(query);
        Entity e = pq.asSingleEntity();
        datastore.delete(e.getKey());
	}
	
	public static void deleteSeriesActors(String key){
		Key seriesKey = KeyFactory.createKey("Series", key);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Actor").setAncestor(seriesKey);
        PreparedQuery pq = datastore.prepare(query);
        Iterator<Entity> it = pq.asIterator();
		while(it.hasNext()){
			Entity e = it.next();
			datastore.delete(e.getKey());
		}
	}
	
	public static void deleteAllSeries(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Series");
        PreparedQuery pq = datastore.prepare(query);
        Iterator<Entity> it = pq.asIterator();
		while(it.hasNext()){
			Entity e = it.next();
			datastore.delete(e.getKey());
		}
	}
	
	

	
}
