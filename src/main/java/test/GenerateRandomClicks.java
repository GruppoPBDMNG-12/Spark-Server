package test;

import it.shortener.entity.UrlAssociation;
import it.shortener.utility.ShortUrlGenerator;

import java.util.ArrayList;


public class GenerateRandomClicks {
	private static ArrayList<String>shortUrls=new ArrayList<String>();
	public static void saveUrls(){
		String path=System.getProperty("user.dir")+"\\src\\main\\resources\\myBookmarks.html";
		ArrayList<String>urls=HREFRetriver.retriveUrls(path);
		for(String url:urls){
			String shortUrl=ShortUrlGenerator.generateShortUrl(url);
			System.out.println("aggiungo "+shortUrl);
			UrlAssociation.createNewAssociation(shortUrl, url);
			shortUrls.add(shortUrl);
		}
	}
	
	
	
	
	public static void main(String[] args) {
		GenerateRandomClicks.saveUrls();
		
		for(String shortUrl:shortUrls){
			System.out.println(shortUrl);
			String[] generatedIp=RandomGaussianIPGenerator.generateIPAdrresses(100);
			for(int i=0;i<generatedIp.length;i++){
				UrlAssociation.getUrlAssociation(shortUrl).addClick(generatedIp[i]);
			}
		}	
		
	}
}
