
	import java.net.InetAddress;

import redis.clients.jedis.Jedis;
public class RedisConnection {
	   public static void main(String[]args) throws Exception {
		  String a= InetAddress.getLocalHost().getHostAddress();
		  System.out.println(a);
	      Jedis jedis = new Jedis("localhost",6379/*+"/redis:6379"*/);
	      System.out.println("Connection to server sucessfully");
	      //check whether server is running or not
	      System.out.println("Server is running: "+jedis.ping());
	      jedis.close();
	 }
}
