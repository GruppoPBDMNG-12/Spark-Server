package it.shortener.DAO;

import it.shortener.MyJSonString;
import it.shortener.UrlAssociation;

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
		if(jsonString.getJsonString()==null){
			return null;
		}
		return new UrlAssociation(shortUrl, jsonString);
	}
	
	public boolean newAssociation(UrlAssociation ua){
		if(isExistingShortUrl(ua.getShortUrl())){
			return false;
		}
		dao.setValue(ua.getShortUrl(), ua.getJsonString());
		return true;
	}
	
	public boolean isExistingShortUrl(String shortUrl){
		return (dao.getValue(shortUrl).getJsonString()==null);
	}
	
	public boolean updateUrlAssociation(UrlAssociation ua){
		dao.setValue(ua.getShortUrl(), ua.getJsonString());
		return true;
	}
}
