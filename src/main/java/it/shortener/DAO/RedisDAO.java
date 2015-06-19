package it.shortener.DAO;

import it.shortener.MyJSonString;
import it.shortener.UrlAssociation;

import java.util.HashMap;

class RedisDAO {
	public static HashMap<String, MyJSonString>urlAssociations=new HashMap<String, MyJSonString>();
	
	
	
	private static final RedisDAO instance=new RedisDAO();
	private RedisDAO(){
		//urlAssociations.put("1Tinyurl",new UrlAssociation("1Tinyurl","www.google.it"));
	}
	public static RedisDAO getInstance(){
		return instance;
	}
	
	public MyJSonString getValue(String shortUrl){
		return urlAssociations.get(shortUrl);
	}
	
	
	public boolean setValue(String shortUrl,MyJSonString jsonString){
		urlAssociations.put(shortUrl, jsonString);
		return true;
	}	
}
