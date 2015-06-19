package it.shortener;
import static spark.Spark.*;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import spark.*;
 
public class SparkServer {
		private static final String JSON_STRING_BEGINNING="{\"data\": ";
		private static final String JSON_STRING_ENDING="}";
		
		
		private static final String ADD_SHORT_URL_OK_JSON=JSON_STRING_BEGINNING+"{\"add\":\"ok\"}+JSON_STRING_ENDING";
		private static final String ADD_SHORT_URL_ERR_KEY_JSON=JSON_STRING_BEGINNING+"{\"add\":\"key\"}"+JSON_STRING_ENDING;
		private static final String GENERATED_SHORT_URL_JSON=JSON_STRING_BEGINNING+"{\"shortUrl\":\"?\"}"+JSON_STRING_ENDING;
		private static final String GET_LONG_URL_JSON=JSON_STRING_BEGINNING+"{\"longUrl\":\"?\"}"+JSON_STRING_ENDING;
		private static final String ERROR_KEY_NOT_FOUND=JSON_STRING_BEGINNING+"{\"ERROR\":\"Short url not mapped\"}"+JSON_STRING_ENDING;
		public static HashMap<String, UrlAssociation>urlAssociations=new HashMap<String, UrlAssociation>();
	    
	    public static void main(String[] args) {
	    	urlAssociations.put("1Tinyurl",new UrlAssociation("1Tinyurl","www.google.it"));
	    	get(new Route("/addShortUrl") {	
	            @Override
	            public Object handle(Request request, Response response) {
	            	
	                String shortUrl=request.queryParams("shortUrl");
	                String longUrl=request.queryParams("longUrl");
	                if(urlAssociations.containsKey(shortUrl)){
	                	return ADD_SHORT_URL_ERR_KEY_JSON;
	                }
	                urlAssociations.put(shortUrl,new UrlAssociation(shortUrl,longUrl));
	                return ADD_SHORT_URL_OK_JSON;
	            }
	        });
	    	get(new Route("/generateShortUrl") {
	            @Override
	            public Object handle(Request request, Response response) {
	                String longUrl=request.queryParams("longUrl");
	                String shortUrl="";
	                do{
	                	int random=(int)Math.random()*1000;
	                	shortUrl=longUrl.hashCode()+"_"+random;
	                }while(urlAssociations.containsKey(shortUrl));
	                String toReturn=GENERATED_SHORT_URL_JSON.replace("?", shortUrl);
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				}
			});
	    	get(new Route("/getLongUrl") {
				@Override
				public Object handle(Request request, Response response) {
					String shortUrl = request.queryParams("shortUrl");
					String toReturn =GET_LONG_URL_JSON;
					if (urlAssociations.containsKey(shortUrl)) {
						toReturn=toReturn.replace("?",urlAssociations.get(shortUrl)
								.getLongUrl());
					}
					return toReturn;
				}
			});
	        
	        get(new Route("/getStats") {
	            @Override
	            public Object handle(Request request, Response response) {
	            	String shortUrl=request.queryParams("shortUrl");
	            	String toReturn="";
	            	if(urlAssociations.containsKey(shortUrl)){
	            		toReturn=JSON_STRING_BEGINNING+urlAssociations.get(shortUrl).getStats()+JSON_STRING_ENDING;       		
	            	}else{
	            		toReturn=ERROR_KEY_NOT_FOUND;
	            	}
	                return toReturn;
	            }
	        });
	        get(new Route("/addClick") {
	            @Override
	            public Object handle(Request request, Response response) {
	            	String shortUrl=request.queryParams("shortUrl");
	            	if(urlAssociations.containsKey(shortUrl)){
		            	String ipAddress=request.ip();
	            		urlAssociations.get(shortUrl).addClick(ipAddress);
	            	}

            		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
            		Date today= new Date();
            		String dateOut= dateFormatter.format(today);
            		
	                return dateOut;
	            }
	        });
	        
	    }
	}
//ORIGINAL EXAMPLE
/*get(new Route("/hello") {
    @Override
    public Object handle(Request request, Response response) {
    	String a=request.queryParams("firstName");
    	Set<String> queryParams = request.queryParams();
    	
	    StringBuilder str = new StringBuilder();
	    str.append("Request Parameters are <br/>"+a);
	    for(String param : queryParams){
	    	
	    	str.append(param).append(" ").append(request.queryParams(param)).append("<br />");
	    }
	    
	    return str.toString();
    }
});*/

