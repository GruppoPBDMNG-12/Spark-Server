package it.shortener.populate_db;

import it.shortener.DAO.UrlAssociationDAO;
import it.shortener.entity.UrlAssociation;
import it.shortener.utility.ShortUrlGenerator;

import java.util.ArrayList;


public class DatabasePopolutor {
	public static final int defaultNumberOfClicksForShortUrl=15;
	private static ArrayList<UrlAssociation>urlAssociations=new ArrayList<UrlAssociation>();
	
	
	public static void popoluteDB(int numberOfClicks){
		System.out.println("Populating the DB");
		DatabasePopolutor.saveUrls();
		for(UrlAssociation urlAssociation:urlAssociations){
			System.out.println("Inserting clicks for the shorturl:"+urlAssociation.getShortUrl());
			String[] generatedIPs=RandomGaussianIPGenerator.generateIPAdrresses(numberOfClicks);
			for(int i=0;i<generatedIPs.length;i++){
				urlAssociation.addClick(generatedIPs[i]);
			}
			UrlAssociationDAO.getInstance().newAssociation(urlAssociation);
		}
	}
	
	private static void saveUrls(){
		String path=System.getProperty("user.dir")+"\\src\\main\\resources\\myBookmarks.html";
		ArrayList<String>urls=HREFRetriver.retriveUrls(path);
		for(String url:urls){
			String shortUrl=ShortUrlGenerator.generateShortUrl(url);
			urlAssociations.add(new UrlAssociation(shortUrl, url));
		}
	}
}
