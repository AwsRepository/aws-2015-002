package es.us.aws;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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
	
	public static boolean existsSeriesPost(String key){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key seriesKey = KeyFactory.createKey("Series", toCamelCase(key));
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
			Series s = entityToSeries(e);
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
		
		series.setProperty("title", s.getTitle());
		series.setProperty("creator", s.getCreator());
		series.setProperty("seasons", s.getSeasons());
		series.setProperty("episodes", s.getEpisodes());
		series.setProperty("year", s.getYear());
		
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
	
	public static List<Actor> selectSeriesActors(String key){
		List<Actor> actors = new LinkedList<Actor>(); 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key seriesKey = KeyFactory.createKey("Series", key);
		Query query = new Query("Actor").setAncestor(seriesKey);
		PreparedQuery pq = datastore.prepare(query);
		Iterator<Entity> it = pq.asIterator();
		while(it.hasNext()){
			Entity e = it.next();
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
			//TODO delete Actors
		}
	}
	
	private static Entity seriesToEntity(Series series){
		Entity seriesEntity = new Entity("Series", toCamelCase(series.getTitle()));
		seriesEntity.setProperty("title", series.getTitle());
		seriesEntity.setProperty("creator", series.getCreator());
		seriesEntity.setProperty("seasons", series.getEpisodes());
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

	static String toCamelCase(String s){
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
	
	public static List paginate(List list, int limit, int offset){
		int length = list.size();
		int first = 0;
		int last = length;
		if(offset<length)
			first=offset;
		if(offset+limit<length)
			last=offset+limit;
		return list.subList(first, last);
	}
	
}
