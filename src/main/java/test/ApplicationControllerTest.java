package test;

import static org.junit.Assert.*;
import it.shortener.DAO.RedisDAO;
import it.shortener.frontController.ApplicationController;
import it.shortener.utility.ShortUrlGenerator;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class ApplicationControllerTest {

	private static ArrayList<String>savedShortUrl;
	@Test
	public void addShortUrlTest() {
		String[]longUrls={"www.google.it", "www.yahoo.it","","www.tuttoOk.it"};
		String[]shortUrls={"","ass","test","tuttoOk"};
		String[]returns={ApplicationController.ERR_EMPTY_SHORT_URL_VALUE,ApplicationController.ERR_BAD_WORDS_IN_SHORT_URL_VALUE,
				ApplicationController.ERR_EMPTY_LONG_URL_VALUE,ApplicationController.NO_ERR_VALUE};
		for(int i=0;i<longUrls.length;i++){
			RedisDAO.getInstance().remove(shortUrls[i]);
			JSONObject obj=ApplicationController.addShortUrl(shortUrls[i], longUrls[i]);
			obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
			String errKey=(String)obj.get(ApplicationController.ERR_KEY);
			assertEquals("Test "+i,returns[i],errKey);
		}
	}
	
	@Test
	public void addClickTest() {
		String[]longUrls={"www.google.it", "www.yahoo.it",};
		String[]shortUrls={"validTest","errTest",};
		String stringOfStats="[{\"name\":\"Added on:\",\"value\":\"Jul 4, 2015\"},{\"name\":\"# Tot Clicks\",\"value\":1},{\"name\":\"Today's clicks\",\"value\":1},{\"name\":\"This month clicks\",\"value\":1},{\"name\":\"This year clicks\",\"value\":1},{\"name\":\"Max number of clicks from\",\"value\":\"localhost\"}]";
		
		RedisDAO.getInstance().remove(shortUrls[0]);
		ApplicationController.addShortUrl(shortUrls[0], longUrls[0]);
		
		String[]returns={ApplicationController.NO_ERR_VALUE,ApplicationController.ERR_KEY_NOT_FOUND_VALUE,};
		
		for(int i=0;i<longUrls.length;i++){
			JSONObject obj=ApplicationController.addClick(shortUrls[i], "192.168.0.1");
			obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
			String errKey=(String)obj.get(ApplicationController.ERR_KEY);
			assertEquals("Test stats 1."+i,returns[i],errKey);
			
			if(errKey.equalsIgnoreCase(ApplicationController.NO_ERR_VALUE)){
				obj=ApplicationController.getStats(shortUrls[i]);
				obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
				JSONArray arrayObj=(JSONArray)obj.get(ApplicationController.STATS_KEY);
				assertEquals("Test 2."+i,stringOfStats,arrayObj.toString());
			}
		}
	}
	
	@Test
	public void generateShortUrlTest() {
		String[]longUrls={"www.facebook.it", "www.youtube.it","www.yahoo.it","johnn dep.sadsad"};
		
		for(int i=0;i<longUrls.length;i++){
			JSONObject obj=ApplicationController.generateShortUrl(longUrls[i]);
			obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
			String shortUrl=(String)obj.get(ApplicationController.GENERATE_SHORT_URL_KEY);
			//System.out.println(shortUrl);
			assertNotEquals("Test "+i+" LongUrl="+longUrls[i], shortUrl,"");
		}
	}
	
	@Test
	public void getLongUrlTest() {
		String[]longUrls={"www.google.it", "http://www.yahoo.it","facebook.it","www.tuttoOk.it"};
		String[]shortUrls={"ggl","yhh","test","tuttoOk"};
		String[]shortUrlsToTest={"ggl","yhh","test1",""};
		
		String[]errReturns={ApplicationController.NO_ERR_VALUE,ApplicationController.NO_ERR_VALUE,ApplicationController.ERR_KEY_NOT_FOUND_VALUE,ApplicationController.ERR_EMPTY_SHORT_URL_VALUE};
		String[]returns={"http://www.google.it","http://www.yahoo.it"};
		for(int i=0;i<longUrls.length;i++){
			RedisDAO.getInstance().remove(shortUrls[i]);
			ApplicationController.addShortUrl(shortUrls[i], longUrls[i]);
			JSONObject obj=ApplicationController.getLongUrl(shortUrlsToTest[i]);
			obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
			String errKey=(String)obj.get(ApplicationController.ERR_KEY);
			assertEquals("Test "+i,errReturns[i],errKey);
			
			if(errKey.equalsIgnoreCase(ApplicationController.NO_ERR_VALUE)){
				String longUrl=(String)obj.get(ApplicationController.GET_LONG_URL_KEY);
				assertEquals("Test "+i, returns[i],longUrl);
			}
		}
	}
	
	@Test
	public void getStatsTest() {
		String[]longUrls={"www.google.it", "www.yahoo.it",};
		String[]shortUrls={"validTest","errTest",};
		String stringOfStats="[{\"name\":\"Added on:\",\"value\":\"Jul 4, 2015\"},{\"name\":\"# Tot Clicks\",\"value\":1},{\"name\":\"Today's clicks\",\"value\":1},{\"name\":\"This month clicks\",\"value\":1},{\"name\":\"This year clicks\",\"value\":1},{\"name\":\"Max number of clicks from\",\"value\":\"localhost\"}]";
		
		RedisDAO.getInstance().remove(shortUrls[0]);
		ApplicationController.addShortUrl(shortUrls[0], longUrls[0]);
		
		String[]returns={ApplicationController.NO_ERR_VALUE,ApplicationController.ERR_KEY_NOT_FOUND_VALUE,};
		
		for(int i=0;i<longUrls.length;i++){
			JSONObject obj=ApplicationController.addClick(shortUrls[i], "192.168.0.1");
			obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
			String errKey=(String)obj.get(ApplicationController.ERR_KEY);
			assertEquals("Test stats 1."+i,returns[i],errKey);
			
			if(errKey.equalsIgnoreCase(ApplicationController.NO_ERR_VALUE)){
				obj=ApplicationController.getStats(shortUrls[i]);
				obj=(JSONObject) obj.get(ApplicationController.BEGINNING_JSON_KEY);
				JSONArray arrayObj=(JSONArray)obj.get(ApplicationController.STATS_KEY);
				assertEquals("Test 2."+i,stringOfStats,arrayObj.toString());
			}
		}
	}
}
