package it.shortener;

import java.util.Date;

import org.json.JSONObject;

public class Click {
	public static final String GEOIP_KEY="GeoPos";
	private String geoLocation;
	private Date date;
	
	public Click(String geoPosition){
		this.geoLocation=geoPosition;
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
		return null;
	}
	
	public static Click jsonToClick(JSONObject jsonObj){
		return new Click(jsonObj.getString(GEOIP_KEY));
	}
}
