package it.shortener;

import static spark.Spark.*;
import it.shortener.DAO.RedisDAO;
import it.shortener.entity.UrlAssociation;
import it.shortener.frontController.ApplicationController;
import it.shortener.utility.IndexFileReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.json.HTTP;
import org.json.JSONObject;
import org.omg.CORBA.RepositoryIdHelper;

import spark.*;

public class SparkServer {


	public static void main(String[] args) {
		RedisDAO.getInstance().remove("pronto");
		System.out.println("inserito?"
				+ UrlAssociation.createNewAssociation("pronto", "sonoPronto"));
		System.out.println("eccolo "
				+ UrlAssociation.getUrlAssociation("pronto").getLongUrl());

		get(new Route("/addShortUrl") {
			@Override
			public Object handle(Request request, Response response) {
				String shortUrl = request.queryParams("shortUrl");
				String longUrl = request.queryParams("longUrl");
				SparkServer.setResponseHeader(request, response);
				return ApplicationController.addShortUrl(shortUrl, longUrl);
			}
		});
		
		get(new Route("/generateShortUrl") {
			@Override
			public Object handle(Request request, Response response) {
				String longUrl = request.queryParams("longUrl");
				SparkServer.setResponseHeader(request, response);
				return ApplicationController.generateShortUrl(longUrl).toString();
			}
		});
		
		get(new Route("/getLongUrl") {
			@Override
			public Object handle(Request request, Response response) {
				String shortUrl = request.queryParams("shortUrl");
				SparkServer.setResponseHeader(request, response);
				return ApplicationController.getLongUrl(shortUrl);
			}
		});

		get(new Route("/getStats") {
			@Override
			public Object handle(Request request, Response response) {
				String shortUrl = request.queryParams("shortUrl");
				SparkServer.setResponseHeader(request, response);
				return ApplicationController.getStats(shortUrl);
			}
		});
		get(new Route("/addClick") {
			@Override
			public Object handle(Request request, Response response) {
				String shortUrl = request.queryParams("shortUrl");
				String ipAddress=request.ip();
				SparkServer.setResponseHeader(request, response);
				
				return ApplicationController.addClick(shortUrl, ipAddress);
			}
		});
		 get(new Route("/*") {
			 @Override
				public Object handle(Request request, Response response) {
				 RedisDAO.getInstance().keylist();
				String longUrl=ApplicationController.redirect(request.pathInfo().substring(1), request.ip());
				if(longUrl.isEmpty()){
					response.status(404);
					return "Short Url non Mappato";
				}else{
					response.redirect(longUrl);
				}
				return "";
			}
		});
		options(new Route("/*") {
			
			@Override
			public Object handle(Request req, Response res) {
				SparkServer.setOptionRequestResponseHeader(req, res);
				return null;
			}
		});
	}

	 private static void setResponseHeader(Request req,Response res){
		 String origin=req.headers("Origin");
		 res.header("access-control-allow-origin", origin);
		 res.header("content-type", "text/plain");
	 }

	
	 private static void setOptionRequestResponseHeader(Request req,Response res){
		 String origin=req.headers("Origin");
		 res.header("access-control-allow-origin", origin);
		 res.header("access-control-allow-methods", "GET, OPTIONS");
		 res.header("access-control-allow-headers", "content-type, accept");
		 res.header("access-control-max-age", 10+"");
		 res.header("content-length", 0+"");

		 
	 }
}