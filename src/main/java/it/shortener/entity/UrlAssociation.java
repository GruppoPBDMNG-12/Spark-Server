package it.shortener.entity;

import it.shortener.DAO.UrlAssociationDAO;
import it.shortener.frontController.ApplicationController;
import it.shortener.utility.IPLocator;
import it.shortener.utility.MyJSonString;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class UrlAssociation {
	private static final String SHORTURL_JSON_KEY="shortUrl";
	private static final String LONGURL_JSON_KEY="longUrl";
	private static final String CLICKS_ARRAY_JSON_KEY="clicks";
	private static final String DATE_JSON_KEY="date";
	
	private static final String ADDED_DATE_KEY="Added on:";
	private static final String NUM_TOT_CLICK_KEY="# Tot Clicks";
	private static final String NUM_CLICKS_TODAY_KEY="Today's clicks";
	private static final String NUM_CLICKS_MONTH_KEY="This month clicks";
	private static final String NUM_CLICKS_YEAR_KEY="This year clicks";
	private static final String MAX_LOC_KEY="Max number of clicks from";
	
	private static final String LOCATION_NAME_KEY="location";
	private static final String LOCATION_NUM_OF_CLICKS_KEY="click";
	

	private String shortUrl;
	private String longUrl;
	private Date date;
	private ArrayList<Click> clicks;
	
	private static final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
	private static UrlAssociationDAO uaDAO=UrlAssociationDAO.getInstance();
	
	public UrlAssociation(String shortUrl, MyJSonString jsonString){
		this.shortUrl=shortUrl;
		String a="{\"test\": "+jsonString.getJsonString()+"}";
		JSONObject obj=new JSONObject(a);
		obj=obj.getJSONObject("test");
		this.longUrl=obj.getString(LONGURL_JSON_KEY);
		this.shortUrl=obj.getString(SHORTURL_JSON_KEY);
		try {
			this.date=dateFormatter.parse(obj.getString(DATE_JSON_KEY));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray clicksJSONArray=obj.getJSONArray(CLICKS_ARRAY_JSON_KEY);
		clicks=new ArrayList<Click>();
		for(int i=0;i<clicksJSONArray.length();i++){
			clicks.add(new Click(clicksJSONArray.getJSONObject(i)));
		}
		
	}
	
	private UrlAssociation(String shortUrl,String longUrl){
		this.shortUrl=shortUrl;
		this.longUrl=longUrl;
		longUrl=longUrl.replace("http://", "");
		longUrl=longUrl.replace("https://", "");
		longUrl="http://"+longUrl;
		clicks=new ArrayList<Click>();
		this.date=new Date();
	}
	
	
	public JSONArray getStats(){
		return generateStats();
	}
	

	public String getShortUrl() {
		return shortUrl;
	}
	
	public String getLongUrl(){
		return longUrl;
	}

	public void addClick(String ipAddress){
		Date today= new Date();
		try{
			clicks.add(new Click(IPLocator.getIstance().getLocation(ipAddress).toString(),today));
		}catch(Exception e){
			System.out.println(e.getMessage()+e.getCause());
		}
		uaDAO.updateUrlAssociation(this);
	}

	public MyJSonString getJsonString(){
		JSONObject uaJson=new JSONObject();
		uaJson.put(SHORTURL_JSON_KEY, shortUrl);
		uaJson.put(LONGURL_JSON_KEY, longUrl);
		uaJson.put(DATE_JSON_KEY, dateFormatter.format(date));
		
		JSONArray clickJsonArray=new JSONArray();
		for(Click c: clicks){
			clickJsonArray.put(c.toJason());
		}
		
		uaJson.put(CLICKS_ARRAY_JSON_KEY, clickJsonArray);
		return new MyJSonString(uaJson.toString());
	}
	

	public static boolean createNewAssociation(String shortUrl,String longUrl){
		UrlAssociation ua=new UrlAssociation(shortUrl,longUrl);
		return uaDAO.newAssociation(ua);
	}
	
	public static UrlAssociation getUrlAssociation(String shortUrl){
		return uaDAO.getUrlAssociation(shortUrl);
	}
	
	private JSONArray generateStats(){

		HashMap<String,Integer>locationsClicks=new HashMap<String, Integer>();
		int clicksDay=0,clicksMonth=0,clicksYear=0;

		Date today=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		int todayMonth=cal.get(Calendar.MONTH);
		int todayDay=cal.get(Calendar.DAY_OF_MONTH);
		int todayYear=cal.get(Calendar.YEAR);
	
		for(int i=0;i<clicks.size();i++){	
			String loc=clicks.get(i).geoLocation();
			if(locationsClicks.containsKey(loc)){
				locationsClicks.put(loc, locationsClicks.get(loc)+1);
			}else{
				locationsClicks.put(loc, 1);
			}

			cal.setTime(clicks.get(i).getDate());
			int month = cal.get(Calendar.MONTH);
			int year=cal.get(Calendar.YEAR);
			int day=cal.get(Calendar.DAY_OF_MONTH);
			if(todayYear==year){
				clicksYear++;
				if(todayMonth==month){
					clicksMonth++;
					if(todayDay==day){
						clicksDay++;
					}
				}
			}
		}

		String maxLoc=null;
		JSONArray locationsJsonArray=new JSONArray();
		Set<String>locations=locationsClicks.keySet();
		for(String s:locations){
			JSONObject locationObj=new JSONObject();
			locationObj.put(LOCATION_NAME_KEY, s);
			locationObj.put(LOCATION_NUM_OF_CLICKS_KEY, locationsClicks.get(s)+"");
			locationsJsonArray.put(locationObj);
			if(maxLoc==null){
				maxLoc=s;
			}else{
				if(locationsClicks.get(maxLoc)<locationsClicks.get(s)){
					maxLoc=s;
				}
			}
		}
		JSONArray statsJsonArray=new JSONArray();
		JSONObject addedOnJsonObj=new JSONObject();
		addedOnJsonObj.put("name", ADDED_DATE_KEY);
		addedOnJsonObj.put("value",dateFormatter.format(date));
		statsJsonArray.put(addedOnJsonObj);
		
		JSONObject numTotClickJsonObj=new JSONObject();
		numTotClickJsonObj.put("name", NUM_TOT_CLICK_KEY);
		numTotClickJsonObj.put("value", clicks.size());
		statsJsonArray.put(numTotClickJsonObj);

		JSONObject numTodayClickJsonObj=new JSONObject();
		numTodayClickJsonObj.put("name", NUM_CLICKS_TODAY_KEY);
		numTodayClickJsonObj.put("value", clicksDay);
		statsJsonArray.put(numTodayClickJsonObj);

		JSONObject numMonthClickJsonObj=new JSONObject();
		numMonthClickJsonObj.put("name", NUM_CLICKS_MONTH_KEY);
		numMonthClickJsonObj.put("value", clicksMonth);
		statsJsonArray.put(numMonthClickJsonObj);
		
		JSONObject numYearClickJsonObj=new JSONObject();
		numYearClickJsonObj.put("name", NUM_CLICKS_YEAR_KEY);
		numYearClickJsonObj.put("value", clicksYear);
		statsJsonArray.put(numYearClickJsonObj);

		JSONObject numMaxLocJsonObj=new JSONObject();
		numMaxLocJsonObj.put("name", MAX_LOC_KEY);
		numMaxLocJsonObj.put("value", maxLoc);
		statsJsonArray.put(numMaxLocJsonObj);	
		

		JSONObject locationGraphObject=new JSONObject();
		locationGraphObject.put("name", "GraphData");
		locationGraphObject.put("value", locationsJsonArray);
		statsJsonArray.put(locationGraphObject);	
		return statsJsonArray;
	}
	public static void main(String[] args) {
		System.out.println(ApplicationController.getStats("bBYnHf"));
	}
}
