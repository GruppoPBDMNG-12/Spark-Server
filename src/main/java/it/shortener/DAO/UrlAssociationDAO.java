package it.shortener.DAO;

import it.shortener.entity.UrlAssociation;
import it.shortener.utility.MyJSonString;

public class UrlAssociationDAO {
	private static final RedisDAO dao=RedisDAO.getInstance();
	private static final UrlAssociationDAO instance=new UrlAssociationDAO();
	
	private UrlAssociationDAO() {
	}
	
	public static UrlAssociationDAO getInstance(){
		return instance;
	}
	
	public UrlAssociation getUrlAssociation(String shortUrl){
		MyJSonString jsonString=dao.getValue(shortUrl);
		if(jsonString==null){
			return null;
		}
		return new UrlAssociation(shortUrl, jsonString);
	}
	
	public boolean newAssociation(UrlAssociation ua){
		if(!isUnique(ua.getShortUrl())){
			return false;
		}
		dao.setValue(ua.getShortUrl(), ua.getJsonString());
		return true;
	}
	
	public boolean isUnique(String shortUrl){
		return (dao.getValue(shortUrl)==null);
	}
	
	public boolean updateUrlAssociation(UrlAssociation ua){
		dao.setValue(ua.getShortUrl(), ua.getJsonString());
		return true;
	}
}
