package it.shortener.frontController;

import it.shortener.entity.UrlAssociation;
import it.shortener.utility.BadWordsChecker;
import it.shortener.utility.ShortUrlGenerator;

import org.json.JSONObject;

public class ApplicationController {

	public static final String BEGINNING_JSON_KEY = "data";

	public static final String GENERATE_SHORT_URL_KEY = "shortUrl";
	public static final String GET_LONG_URL_KEY = "longUrl";
	public static final String STATS_KEY = "stats";

	public static final String ERR_KEY = "err";
	public static final String NO_ERR_VALUE = "ok";
	public static final String ERR_USED_SHORT_URL_VALUE = "used";
	public static final String ERR_KEY_NOT_FOUND_VALUE = "keyNotFound";
	public static final String ERR_EMPTY_LONG_URL_VALUE = "Empty LongUrl";
	public static final String ERR_EMPTY_SHORT_URL_VALUE = "Empty ShortUrl";
	public static final String ERR_BAD_WORDS_IN_SHORT_URL_VALUE = "Bad Words in short url are not allowed";

	public static JSONObject addShortUrl(String shortUrl, String longUrl) {
		JSONObject dataObj = new JSONObject();
		/*
		 * String shortUrl = request.queryParams("shortUrl"); String longUrl =
		 * request.queryParams("longUrl");
		 */
		try{
		if (shortUrl.equalsIgnoreCase("undefined")
				|| shortUrl.equalsIgnoreCase("")) {
			dataObj.put(ERR_KEY, ERR_EMPTY_SHORT_URL_VALUE);
		} else if (longUrl.equalsIgnoreCase("undefined")
				|| longUrl.equalsIgnoreCase("")) {
			dataObj.put(ERR_KEY, ERR_EMPTY_LONG_URL_VALUE);
		} else if (!BadWordsChecker.isBadWordsFree(shortUrl)) {
			dataObj.put(ERR_KEY, ERR_BAD_WORDS_IN_SHORT_URL_VALUE);
		} else {
			boolean isCreated = UrlAssociation.createNewAssociation(shortUrl,
					longUrl);
			if (isCreated) {
				dataObj.put(ERR_KEY, NO_ERR_VALUE);
			} else {
				dataObj.put(ERR_KEY, ERR_USED_SHORT_URL_VALUE);
			}
		}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		JSONObject toReturn = new JSONObject();
		toReturn.put(BEGINNING_JSON_KEY, dataObj);
		return toReturn;
	}
	
	public static JSONObject generateShortUrl(String longUrl) {
			JSONObject dataObj = new JSONObject();
			if (longUrl.equalsIgnoreCase("undefined")||longUrl.equalsIgnoreCase("")) {
				dataObj.put(ERR_KEY, ERR_EMPTY_LONG_URL_VALUE);
			} else {
				String shortUrl = ShortUrlGenerator
						.generateShortUrl(longUrl);
				dataObj.put(ERR_KEY, NO_ERR_VALUE);
				dataObj.put(GENERATE_SHORT_URL_KEY, shortUrl);
			}
			JSONObject toReturn = new JSONObject();
			toReturn.put(BEGINNING_JSON_KEY, dataObj);

			return toReturn;
	}
	public static JSONObject getLongUrl(String shortUrl) {
			JSONObject dataObj = new JSONObject();
			if (shortUrl.equalsIgnoreCase("undefined")||shortUrl.equalsIgnoreCase("")) {
				dataObj.put(ERR_KEY, ERR_EMPTY_SHORT_URL_VALUE);
			} else {
				UrlAssociation ua = UrlAssociation
						.getUrlAssociation(shortUrl);
				if (ua != null) {
					dataObj.put(ERR_KEY, NO_ERR_VALUE);
					dataObj.put(GET_LONG_URL_KEY, ua.getLongUrl());
				} else {

					dataObj.put(ERR_KEY, ERR_KEY_NOT_FOUND_VALUE);

				}
			}
			JSONObject toReturn = new JSONObject();
			toReturn.put(BEGINNING_JSON_KEY, dataObj);

			return toReturn;
	}

	public static JSONObject getStats(String shortUrl)  {
			JSONObject dataObj = new JSONObject();
			if (shortUrl.equalsIgnoreCase("undefined")||shortUrl.equalsIgnoreCase("")) {
				dataObj.put(ERR_KEY, ERR_EMPTY_SHORT_URL_VALUE);
			} else {
				UrlAssociation ua = UrlAssociation
						.getUrlAssociation(shortUrl);
				if (ua == null) {
					dataObj.put(ERR_KEY, ERR_KEY_NOT_FOUND_VALUE);
				} else {
					dataObj.put(ERR_KEY, NO_ERR_VALUE);
					dataObj.put(STATS_KEY, ua.getStats());
				}
			}
			JSONObject toReturn = new JSONObject();
			toReturn.put(BEGINNING_JSON_KEY, dataObj);

			return toReturn;
}
	public static JSONObject addClick(String shortUrl,String ipAddress)  {
		JSONObject dataObj = new JSONObject();
			if (shortUrl.equalsIgnoreCase("undefined")||shortUrl.equalsIgnoreCase("")) {
				dataObj.put(ERR_KEY, ERR_EMPTY_SHORT_URL_VALUE);
			} else {
				UrlAssociation ua = UrlAssociation
						.getUrlAssociation(shortUrl);
				if (ua != null) {
					ua.addClick(ipAddress);
					dataObj.put(ERR_KEY, NO_ERR_VALUE);
					dataObj.put(GET_LONG_URL_KEY, ua.getLongUrl());
				} else {
					dataObj.put(ERR_KEY, ERR_KEY_NOT_FOUND_VALUE);
				}
			}
			JSONObject toReturn = new JSONObject();
			toReturn.put(BEGINNING_JSON_KEY, dataObj);
			return toReturn;
		}

	public static String redirect(String shortUrl,String ipAddress){
			UrlAssociation ua = UrlAssociation
					.getUrlAssociation(shortUrl);
			if (ua != null) {
				ua.addClick(ipAddress);
				return ua.getLongUrl();
			}
		return "";
	}
}
