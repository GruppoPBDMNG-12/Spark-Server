package it.shortener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class Click {
	public static final String GEOIP_KEY="GeoPos";
	public static final String DATE_KEY="Date";
	private String geoLocation;
	private Date date;
	

	private static final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
	
	public Click(String geoPosition,String date){
		this.geoLocation=geoPosition;
		try {
			this.date=dateFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Click(JSONObject json){
		this.geoLocation=json.getString(GEOIP_KEY);
		try {
			this.date=dateFormatter.parse(json.getString(DATE_KEY));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	public Click(String geoPosition,Date date){
		this.geoLocation=geoPosition;
		this.date=date;
	}
	public String geoLocation(){
		return geoLocation;
	}
	public Date getDate(){
		return date;
	}
	public JSONObject toJason(){
		JSONObject jsonObj=new JSONObject();
		jsonObj.put(GEOIP_KEY, geoLocation());
		jsonObj.put(DATE_KEY,dateFormatter.format(date));
		return jsonObj;
	}
	
	public static Click jsonToClick(JSONObject jsonObj){
		return new Click(jsonObj.getString(GEOIP_KEY), jsonObj.getString(DATE_KEY));
	}
}
