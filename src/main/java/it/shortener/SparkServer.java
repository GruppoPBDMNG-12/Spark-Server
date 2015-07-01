package it.shortener;

import static spark.Spark.*;
import it.shortener.DAO.RedisDAO;
import it.shortener.entity.UrlAssociation;
import it.shortener.frontController.ApplicationController;
import it.shortener.utility.IndexFileReader;

import java.io.IOException;
import java.io.OutputStream;

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
				String longUrl = request.queryParams("longUrl");
				return ApplicationController.generateShortUrl(longUrl);
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

	}
}