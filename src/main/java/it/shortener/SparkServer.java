package it.shortener;
import static spark.Spark.*;
import it.shortener.DAO.RedisDAO;
import it.shortener.DAO.UrlAssociationDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.json.JsonObject;
import javax.print.DocFlavor.URL;

import org.eclipse.jetty.server.Authentication.ResponseSent;
import org.json.JSONObject;

import spark.*;
 
public class SparkServer {
	
		private static final String BEGINNING_JSON_KEY="data";
		
		private static final String GENERATE_SHORT_URL_KEY="shortUrl";
		private static final String GET_LONG_URL_KEY="longUrl";
		private static final String STATS_KEY = "stats";
		
		private static final String ERR_KEY="err";
		private static final String NO_ERR_VALUE="ok";
		private static final String ERR_USED_SHORT_URL_VALUE="used";
		private static final String ERR_KEY_NOT_FOUND_VALUE="keyNotFound";	    
		
	    public static void main(String[] args) {
	    	RedisDAO.getInstance().remove("pronto");
	    	System.out.println("inserito?"+UrlAssociation.createNewAssociation("pronto", "sonoPronto"));
	    	System.out.println("eccolo "+UrlAssociation.getUrlAssociation("pronto").getLongUrl());
	    	
	    	get(new Route("/addShortUrl") {	
	            @Override
	            public Object handle(Request request, Response response) {
	            	JSONObject dataObj=new JSONObject();
	                String shortUrl=request.queryParams("shortUrl");
	                String longUrl=request.queryParams("longUrl");
	                boolean isCreated=UrlAssociation.createNewAssociation(shortUrl,longUrl);
	                if(isCreated){
		            	dataObj.put(ERR_KEY, NO_ERR_VALUE);
	                }else{
		            	dataObj.put(ERR_KEY, ERR_USED_SHORT_URL_VALUE);
	                }
	                JSONObject toReturn=new JSONObject();
	                toReturn.put(BEGINNING_JSON_KEY, dataObj);
	                return toReturn;
	            }
	        });
	    	get(new Route("/generateShortUrl") {
	            @Override
	            public Object handle(Request request, Response response) {
	                String longUrl=request.queryParams("longUrl");
	                String shortUrl=ShortUrlGenerator.generateShortUrl(longUrl);
	                JSONObject dataObj=new JSONObject();
	                dataObj.put(ERR_KEY, NO_ERR_VALUE);
	                dataObj.put(GENERATE_SHORT_URL_KEY, shortUrl);
	                JSONObject toReturn=new JSONObject();
	                toReturn.put(BEGINNING_JSON_KEY, dataObj);
	                return toReturn;
	            }
	        });
	     	get(new Route("/index.html") {
				@Override
				public Object handle(Request request, Response response) {
					OutputStream os;
					try {
						os = response.raw().getOutputStream();
						IndexFileReader.getTextFile(os);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return "";
				}
			});
	    	get(new Route("/getLongUrl") {
				@Override
				public Object handle(Request request, Response response) {
					String shortUrl = request.queryParams("shortUrl");
					
					String longUrl=UrlAssociation.getUrlAssociation(shortUrl).getLongUrl();
					JSONObject dataObj=new JSONObject();
					
					if (longUrl!=null) {
						dataObj.put(ERR_KEY, NO_ERR_VALUE);
						dataObj.put(GET_LONG_URL_KEY,longUrl);
					}else{
						dataObj.put(ERR_KEY, ERR_KEY_NOT_FOUND_VALUE);
					}
					JSONObject toReturn=new JSONObject();
	                toReturn.put(BEGINNING_JSON_KEY, dataObj);
	                return toReturn;
				}
			});
	        
	        get(new Route("/getStats") {
	            @Override
	            public Object handle(Request request, Response response) {
	            	String shortUrl=request.queryParams("shortUrl");
	            	UrlAssociation ua=UrlAssociation.getUrlAssociation(shortUrl);
	            	
	            	JSONObject dataObj=new JSONObject();
	            	if(ua==null){
	            		dataObj.put(ERR_KEY, ERR_KEY_NOT_FOUND_VALUE);
	            	}else{
	            		ua.addClick(request.ip());
	            		dataObj.put(ERR_KEY, NO_ERR_VALUE);
						dataObj.put(STATS_KEY, ua.getStats());
	            	}
	            	JSONObject toReturn=new JSONObject();
	                toReturn.put(BEGINNING_JSON_KEY, dataObj);
	                
	                System.out.println(toReturn);
	                return toReturn;
	            }
	        });
	        get(new Route("/addClick") {
	            @Override
	            public Object handle(Request request, Response response) {
	            	System.out.println("ADD CLICK");
	            	String shortUrl=request.queryParams("shortUrl");
	            	UrlAssociation ua=UrlAssociation.getUrlAssociation(shortUrl);
	            	if(ua!=null){
		            	String ipAddress=request.ip();
	            		ua.addClick(ipAddress);
	            	}
	                return "";
	            }
	        });
	        
	    }
	}