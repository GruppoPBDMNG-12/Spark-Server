package it.shortener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;



public class UrlAssociation {
	private static final String STATS_JSON="{\"name\":\"?\", \"value\":\"$\"}";
	private static final String ARRAY_JSON_BEGIN="[";
	private static final String ARRAY_JSON_END="]";
	private static final String ADDED_DATE_KEY="Added on:";
	private static final String NUM_TOT_CLICK_KEY="# Tot Clicks";
	private static final String NUM_CLICKS_TODAY_KEY="Today's clicks";
	private static final String NUM_CLICKS_MONTH_KEY="This month clicks";
	private static final String NUM_CLICKS_YEAR_KEY="This year clicks";
	private static final String MAX_LOC_KEY="Max number of clicks from";
	
	public static final String LONG_URL_KEY="LongUrl";
	private String shortUrl;
	private String longUrl;
	private Date DateOf;//TODO
	private boolean isEmpty=false;;
	private ArrayList<Click> clicks;
	
	
	public UrlAssociation(String shortUrl, MyJSonString jsonString){
		this.shortUrl=shortUrl;
		if(jsonString==null){
			isEmpty=true;
		}
		MyJSonReader jR=new MyJSonReader(jsonString);
		this.longUrl=jR.getNode(LONG_URL_KEY);
	}
	
	public UrlAssociation(String shortUrl,String longUrl){
		this.shortUrl=shortUrl;
		this.longUrl=longUrl;
		checkLongUrl();
		clicks=new ArrayList<Click>();
	}
	
	private void checkLongUrl() {
		if(!longUrl.startsWith("http://") || !longUrl.startsWith("https://")){
			longUrl="http://"+longUrl;
		}
		System.out.println(longUrl);
	}

	public void addClick(String ipAddress){
		clicks.add(new Click(IPLocator.getIstance().getLocation(ipAddress).toString()));
	}
	
	public String getLongUrl(){
		return longUrl;
	}
	public String getStats(){
		
		return generateStats();
		//RITORNERA' json con le statistiche da inviare al client
	}
	public MyJSonString getJsonString(){
		return null;
	}
	
	private String generateStats(){
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
		Set<String>locations=locationsClicks.keySet();
		for(String s:locations){
			if(maxLoc==null){
				maxLoc=s;;
			}else{
				if(locationsClicks.get(maxLoc)<locationsClicks.get(s)){
					maxLoc=s;
				}
			}
		}
		String statsStrinJson=ARRAY_JSON_BEGIN;
		/*statsStrinJson+=STATS_JSON.replace("?", ADDED_DATE_KEY);
		statsStrinJson=statsStrinJson.replace("$", DateOf+"")+",";*/
		statsStrinJson+=STATS_JSON.replace("?", NUM_TOT_CLICK_KEY);
		statsStrinJson=statsStrinJson.replace("$", clicks.size()+"")+",";
		statsStrinJson+=STATS_JSON.replace("?", NUM_CLICKS_TODAY_KEY);
		statsStrinJson=statsStrinJson.replace("$", clicksDay+"")+",";
		statsStrinJson+=STATS_JSON.replace("?", NUM_CLICKS_MONTH_KEY);
		statsStrinJson=statsStrinJson.replace("$", clicksMonth+"")+",";
		statsStrinJson+=STATS_JSON.replace("?", NUM_CLICKS_YEAR_KEY);
		statsStrinJson=statsStrinJson.replace("$", clicksYear+"")+",";
		statsStrinJson+=STATS_JSON.replace("?", MAX_LOC_KEY);
		statsStrinJson=statsStrinJson.replace("$", maxLoc+"");
		statsStrinJson+=ARRAY_JSON_END;
		
		
		//country with more clicks, How many clicks last day, week, Month
		//giorno in cui e' stato aggiunto
		
		return statsStrinJson;
	}

	public boolean isEmpty() {
		return isEmpty;
	}
}
