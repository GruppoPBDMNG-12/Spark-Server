package it.shortener.DAO;

import it.shortener.UrlAssociation;

import java.util.HashMap;

public class UrlAssociationDAO {
	private static final RedisDAO dao=RedisDAO.getInstance();
	private static final UrlAssociationDAO instance=new UrlAssociationDAO();
	
	private UrlAssociationDAO() {
	}
	
	public static UrlAssociationDAO getInstance(){
		return instance;
	}
	
	public String getLongUrl(String shortUrl){
		UrlAssociation ua=new UrlAssociation(shortUrl, dao.getValue(shortUrl));
		if(ua.isEmpty()){
			return "error";//TODO
		}
		return ua.getLongUrl();
	}
	
	public boolean addAssociation(String shortUrl,String longUrl){
		UrlAssociation ua=new UrlAssociation(shortUrl, longUrl);
		return dao.setValue(shortUrl, ua.getJsonString());
	}
	
	public void addClick(String shortUrl,String ipAddress){
		UrlAssociation ua=new UrlAssociation(shortUrl, dao.getValue(shortUrl));
		ua.addClick(ipAddress);
		dao.setValue(shortUrl, ua.getJsonString());
	}
	
	public boolean isExistingShortUrl(String shortUrl){
		return (dao.getValue(shortUrl)==null);
	}
	
}
