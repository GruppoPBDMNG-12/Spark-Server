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
				
				return ApplicationController.addShortUrl(shortUrl, longUrl);
			}
		});
		get(new Route("/generateShortUrl") {
			@Override
			public Object handle(Request request, Response response) {
				/*Set<String >a=request.headers();
				for(String s:a){
					System.out.println(s+" ="+ request.headers(s));
				}
				
				response.header("access-control-allow-origin", request.headers("Host"));
				response.header("content-type", "text/plain");*/
				SparkServer.setResponseHeader(request, response);
				String longUrl = request.queryParams("longUrl");
				
				return ApplicationController.generateShortUrl(longUrl).toString();
			}
		});
		get(new Route("/index.html") {
			@Override
			public Object handle(Request request, Response response) {
				Set<String> origin = (request.headers());
				System.out.println(request.headers("Host"));
				System.out.println(origin);
				response.header("access-control-allow-origin", request.headers("Host"));
				response.header("content-type", "text/plain");
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
				return ApplicationController.getLongUrl(shortUrl);
			}
		});

		get(new Route("/getStats") {
			@Override
			public Object handle(Request request, Response response) {
				String shortUrl = request.queryParams("shortUrl");
				return ApplicationController.getStats(shortUrl);
			}
		});
		get(new Route("/addClick") {
			@Override
			public Object handle(Request request, Response response) {
				String shortUrl = request.queryParams("shortUrl");
				String ipAddress=request.ip();
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
			
			/*
			 * "access-control-allow-origin": origin,
                 "content-type": "text/plain",
                 "content-length": responseBody.length
			 */
		 res.header("access-control-allow-origin", origin);
		 res.header("content-type", "text/plain");
	 }

	
	 private static void setOptionRequestResponseHeader(Request req,Response res){
		 String origin=req.headers("Origin");
		 /*"access-control-allow-origin": origin,
         "access-control-allow-methods": "GET, POST, PUT, DELETE, OPTIONS",
         "access-control-allow-headers": "content-type, accept",
         "access-control-max-age": 10, // Seconds.
         "content-length": 0*/
		 res.header("access-control-allow-origin", origin);
		 res.header("access-control-allow-methods", "GET, OPTIONS");
		 res.header("access-control-allow-headers", "content-type, accept");
		 res.header("access-control-max-age", 10+"");
		 res.header("content-length", 0+"");

		 
	 }
}