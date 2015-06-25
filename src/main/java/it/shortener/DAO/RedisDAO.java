package it.shortener.DAO;

import it.shortener.MyJSonString;
import it.shortener.UrlAssociation;

import java.util.HashMap;

import redis.clients.jedis.Jedis;

public class RedisDAO {
		private Jedis jedis = new Jedis("127.0.0.1",6379);
	
	
	private static final RedisDAO instance=new RedisDAO();
	private RedisDAO(){
		
	}
	public static RedisDAO getInstance(){
		return instance;
	}
	
	public void remove(String shortUrl){
		openConnection();
		jedis.del(shortUrl);
		closeConnection();
	}
	public MyJSonString getValue(String shortUrl){
		openConnection();
		MyJSonString jsonString=new MyJSonString(jedis.get("shortUrl"));
		closeConnection();
		return jsonString;
	}
	
	
	public void setValue(String shortUrl,MyJSonString jsonString){
		System.out.println("set value "+jsonString);
	    jedis.set("shortUrl", jsonString.getJsonString());
	}	
	
	private void openConnection(){
	      jedis.connect();
	}
	private void closeConnection(){
		jedis.close();
	}
	
	public static void main(String[] args) {
		/*MyJSonString a=RedisDAO.getInstance().getValue("csada");
		System.out.println(a.getJsonString());*/
		RedisDAO.getInstance().remove("pronto");
	}
}
