package it.shortener.DAO;

import it.shortener.UrlAssociation;

import java.util.HashMap;

class RedisDAO {
	public static HashMap<String, UrlAssociation>urlAssociations=new HashMap<String, UrlAssociation>();
	
	
	
	private static final RedisDAO instance=new RedisDAO();
	private RedisDAO(){
		urlAssociations.put("1Tinyurl",new UrlAssociation("1Tinyurl","www.google.it"));
	}
	public static RedisDAO getInstance(){
		return instance;
	}
	
	public String getValue(String shortUrl){
		return urlAssociations.get(shortUrl).getJsonString().getJsonString();
	}
	
	
	public boolean setValue(String shortUrl,/*String*/UrlAssociation urlAssociationJson){
		urlAssociations.put(shortUrl, urlAssociationJson);
		return true;
	}	
}
