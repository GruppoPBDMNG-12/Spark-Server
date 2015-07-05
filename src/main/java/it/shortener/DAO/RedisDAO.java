package it.shortener.DAO;

import it.shortener.utility.MyJSonString;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisDAO {
		private Jedis jedis = new Jedis("127.0.0.1",6379);
	
	
	private static final RedisDAO instance=new RedisDAO();
	private RedisDAO(){
		
	}
	public static RedisDAO getInstance(){
		return instance;
	}
	
	public void checkConnection(){
		openConnection();
		closeConnection();
	}
	
	public boolean exist(String shortUrl){
		openConnection();
		boolean exists=jedis.exists(shortUrl);
		closeConnection();
		return exists;
	}
	public void keylist(){
		openConnection();
		Set<String>a=jedis.keys("");
		System.out.println(a);
		closeConnection();
	}
	public void remove(String shortUrl){
		openConnection();
		jedis.del(shortUrl);
		closeConnection();
	}
	public MyJSonString getValue(String shortUrl){
		openConnection();
		String result=jedis.get(shortUrl);
		if(result==null){
			return null;
		}
		MyJSonString jsonString=new MyJSonString(result);
		closeConnection();
		return jsonString;
	}
	
	public void setValue(String shortUrl,MyJSonString jsonString){
	    jedis.set(shortUrl, jsonString.getJsonString());
	}	
	
	private void openConnection(){
	      jedis.connect();
	}
	private void closeConnection(){
		jedis.close();
	}
}
