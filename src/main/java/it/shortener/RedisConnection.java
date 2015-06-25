package it.shortener;

import redis.clients.jedis.Jedis;

public class RedisConnection {
	   public static void main(String[]args) throws Exception {
	      Jedis jedis = new Jedis("127.0.0.1",6379);
	      jedis.connect();
	      System.out.println("Connection to server sucessfully");
	      System.out.println("Server is running: "+jedis.ping());
	      jedis.set("pronto", "Redis tutorial");
	      // Get the stored data and print it
	      System.out.println("Stored string in redis:: "+ jedis.get("pronto"));
	      jedis.del("pronto");
	      System.out.println(jedis.get("pronto"));
	      jedis.close();
	 }
}
