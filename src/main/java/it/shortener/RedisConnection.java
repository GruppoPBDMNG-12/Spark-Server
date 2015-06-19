package it.shortener;

import redis.clients.jedis.Jedis;

public class RedisConnection {
	   public static void main(String[]args) throws Exception {
	      Jedis jedis = new Jedis("172.17.0.8",6379);
	      jedis.connect();
	      System.out.println("Connection to server sucessfully");
	      System.out.println("Server is running: "+jedis.ping());
	      jedis.close();
	 }
}
