package es.us.aws;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class SeriesPersistence {
		
	public static void insertSeries(Series s){
	    
		Entity series = seriesToEntity(s);
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(series); 
	}
	
	public static Series selectSeries(String key){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key seriesKey = KeyFactory.createKey("Series", key);
	    Query query = new Query("Series", seriesKey);
	    PreparedQuery pq = datastore.prepare(query);
		Entity e = pq.asSingleEntity();
	    Series series = entityToSeries(e);
		return series;
	}
	
	public static boolean existsSeries(String key){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key seriesKey = KeyFactory.createKey("Series", key);
	    Query query = new Query("Series", seriesKey);
	    PreparedQuery pq = datastore.prepare(query);
		return pq.asSingleEntity()!=null;
	}
	
	public static boolean existsActor(String key){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key actorKey = KeyFactory.createKey("Actor", key);
	    Query query = new Query("Actor", actorKey);
	    PreparedQuery pq = datastore.prepare(query);
		return pq.asSingleEntity()!=null;
	}
	
	public static boolean existsSeriesPost(String key){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key seriesKey = KeyFactory.createKey("Series", toCamelCase(key));
	    Query query = new Query("Series", seriesKey);
	    PreparedQuery pq = datastore.prepare(query);
		return pq.asSingleEntity()!=null; 
	}
	
	public static List<Series> selectAllSeries(Map<String,String> params){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		int offset =  0;
		int limit =  1000;//MAX
		
		if(params.containsKey("offset")){
			offset = Integer.parseInt(params.get("offset"));
		}
		if(params.containsKey("limit")){
			limit = Integer.parseInt(params.get("limit"));
		}
		
	    Query query = new Query("Series");
	    
	    //Problems with CompositeFilter
	    //ERROR con los parentesis en Query
	    //ERROR PARENTESIS --> SELECT * FROM Series WHERE (from >= 2010 AND to <= 2012)
	    //Only one inequality filter per query is supported.
	    
	    
	    if(params.containsKey("from")){
			Filter yearFromFilter = new FilterPredicate("year", 
														FilterOperator.GREATER_THAN_OR_EQUAL,
														Integer.parseInt(params.get("from")));
			query.setFilter(yearFromFilter);
		}
	   
	    PreparedQuery pq = datastore.prepare(query);
		Iterable <Entity> it = pq.asIterable(FetchOptions.Builder.withLimit(limit).offset(offset));
		
		boolean withoutActors = params.containsKey("withActors") && params.get("withActors").equalsIgnoreCase("false");
		boolean withActors = params.containsKey("withActors") && params.get("withActors").equalsIgnoreCase("true");
		
		List<Series> series = new LinkedList<Series>();
		
		for(Entity e:it){
			Series s = entityToSeries(e);
			String key = toCamelCase(s.getTitle());
			
			if(withoutActors && selectSeriesActors(key,params).size()==0)
				series.add(s);
			else if(withActors && selectSeriesActors(key,params).size()>0)
				series.add(s);
			else if (!withoutActors && !withActors)
				series.add(s);
		}
		
		return series;
	    
	}
	
	public static void updateSeries(Series s, String key){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Series");
	    Key seriesKey = KeyFactory.createKey("Series", key);
		FilterPredicate filtro = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, seriesKey);
		query.setFilter(filtro);
		PreparedQuery pq = datastore.prepare(query);
		Entity series = pq.asSingleEntity();
		
		if(!s.getTitle().equals(key)){
			
		}
		else{
			series.setProperty("title", s.getTitle());
			series.setProperty("creator", s.getCreator());
			series.setProperty("seasons", s.getSeasons());
			series.setProperty("episodes", s.getEpisodes());
			series.setProperty("year", s.getYear());
		}
		
        datastore.put(series);

		
	}
	
	public static void deleteSeries(String key){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key seriesKey = KeyFactory.createKey("Series", key);
		FilterPredicate filtro = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, seriesKey);
		Query query = new Query("Series");
        query.setFilter(filtro);
        PreparedQuery pq = datastore.prepare(query);
        Entity series = pq.asSingleEntity();
        datastore.delete(series.getKey());
	}
	
	public static List<Actor> selectSeriesActors(String key, Map<String,String> params){
		List<Actor> actors = new LinkedList<Actor>(); 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		int offset =  0;
		int limit =  1000;//MAX
		
		if(params.containsKey("offset")){
			offset = Integer.parseInt(params.get("offset"));
		}
		if(params.containsKey("limit")){
			limit = Integer.parseInt(params.get("limit"));
		}
		
		Key seriesKey = KeyFactory.createKey("Series", key);
		Query query = new Query("Actor").setAncestor(seriesKey);
		PreparedQuery pq = datastore.prepare(query);
		Iterable<Entity> it =  pq.asIterable(FetchOptions.Builder.withLimit(limit).offset(offset));
		
		for(Entity e:it){
			Actor a = entityToActor(e);
			actors.add(a);
		}
		return actors;
	}
	
	public static void insertSeriesActorInit(Actor a, String key){
		Entity actor = actorToEntity(a,toCamelCase(key));	 
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(actor); 
	}
	
	public static void insertSeriesActor(Actor a, String key){
		Entity actor = actorToEntity(a,key);	 
	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    datastore.put(actor); 
	}
	
	public static void deleteSeriesActor(String aKey, String key){
		Key seriesKey = KeyFactory.createKey("Series", key);
		Key actorKey = KeyFactory.createKey("Actor", aKey);
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
	
	private static Entity seriesToEntity(Series series){
		Entity seriesEntity = new Entity("Series", toCamelCase(series.getTitle()));
		seriesEntity.setProperty("title", series.getTitle());
		seriesEntity.setProperty("creator", series.getCreator());
		seriesEntity.setProperty("seasons", series.getSeasons());
		seriesEntity.setProperty("episodes", series.getEpisodes());
		seriesEntity.setProperty("year", series.getYear());
		return seriesEntity;
	}
	
	private static Series entityToSeries(Entity e){
		Series s = new Series();
		s.setTitle((String) e.getProperty("title"));
		s.setCreator((String) e.getProperty("creator"));
		s.setSeasons( (int) (long) e.getProperty("seasons"));
		s.setEpisodes( (int) (long) e.getProperty("episodes"));
		s.setYear((int) (long) e.getProperty("year"));
		return s;
	}
	
	private static Entity actorToEntity(Actor actor, String key){
		Key seriesKey = KeyFactory.createKey("Series", key);
		Entity actorEntity = new Entity("Actor", toCamelCase(actor.getName()+" "+actor.getSurname()), seriesKey);
		actorEntity.setProperty("name", actor.getName());
		actorEntity.setProperty("surname", actor.getSurname());
		actorEntity.setProperty("gender", actor.getGender());
		actorEntity.setProperty("year", actor.getYear());
		actorEntity.setProperty("country", actor.getCountry());
		return actorEntity;
	}
	
	private static Actor entityToActor(Entity e){
		Actor a = new Actor();
		a.setName((String) e.getProperty("name"));
		a.setSurname((String) e.getProperty("surname"));
		a.setGender((String) e.getProperty("gender"));
		a.setYear( (int)(long) e.getProperty("year"));
		a.setCountry( (String) e.getProperty("country"));
		return a;
	}

	public static String toCamelCase(String s){
	   String[] parts = s.split(" ");
	   String camelCaseString = "";
	   for (String part : parts){
	      camelCaseString = camelCaseString + toProperCase(part);
	   }
	   return camelCaseString;
	}

	static String toProperCase(String s) {
	    return s.substring(0, 1).toUpperCase() +
	               s.substring(1).toLowerCase();
	}
	
	public static Actor selectActor(String key){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key actorKey = KeyFactory.createKey("Actor", key);
	    Query query = new Query("Actor", actorKey);
	    PreparedQuery pq = datastore.prepare(query);
		Entity e = pq.asSingleEntity();
	    Actor actor = entityToActor(e);
		return actor;    
	}

	public static boolean existsActorPost(Actor a, String series) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key seriesKey = KeyFactory.createKey("Series", series);
	    Key actorKey = KeyFactory.createKey("Actor", toCamelCase(a.getName()+" "+a.getSurname()));
	    FilterPredicate filter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, actorKey);
		
	    Query query = new Query("Actor").setAncestor(seriesKey);
	    query.setFilter(filter);
	    
	    PreparedQuery pq = datastore.prepare(query);
		return pq.asSingleEntity()!=null;
	}
	
	
}
