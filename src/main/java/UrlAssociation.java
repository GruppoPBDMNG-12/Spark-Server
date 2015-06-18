import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;



public class UrlAssociation {
	public static final String LONG_URL_KEY="LongUrl";
	
	private static final String STATS_JSON="{\"clicksNumber\"=\"?\"";
	private String shortUrl;
	private String longUrl;
	private Date DateOf;//TODO
	private ArrayList<Click> clicks;
	
	
	public UrlAssociation(String shortUrl, MyJSonString jsonString){
		this.shortUrl=shortUrl;
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
		int numClicks=clicks.size();
		return STATS_JSON.replace("?", numClicks+"");
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
		
		
		//country with more clicks, How many clicks last day, week, Month
		//giorno in cui e' stato aggiunto
		
		return "";
	}
}
