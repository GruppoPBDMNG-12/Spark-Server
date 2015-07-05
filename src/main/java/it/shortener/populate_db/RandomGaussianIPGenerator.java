package it.shortener.populate_db;

import java.util.Random;

public class RandomGaussianIPGenerator {
	
	public static String[] generateIPAdrresses(int number){
		String[]ipAddresses=new String[number];
		RandomGaussian gaussian = new RandomGaussian();
	    double MEAN = 127.5f; 
	    double VARIANCE = MEAN/4f;
	    
		for(int i=0;i<number;i++){
			String toAdd=RandomGaussianIPGenerator.nextInt() + "." + RandomGaussianIPGenerator.nextInt() + "."
					+ "" + RandomGaussianIPGenerator.nextInt() + "." + RandomGaussianIPGenerator.nextInt();
			ipAddresses[i]=toAdd;
		}
		return ipAddresses;
	}
	private static int nextInt(){
	    RandomGaussian gaussian = new RandomGaussian();
	    double MEAN = 127.5f; 
	    double VARIANCE = MEAN/4f;
	    	int a=0;
	    	do{	
	    		a=gaussian.getGaussian(MEAN, VARIANCE);
	    	}while(a>255 || a<0);
	      //log("Generated : " + a);
	    return a;
 }  
	
	static class RandomGaussian{
	  private Random fRandom = new Random();
	  public RandomGaussian() {
	  }
	  private int getGaussian(double aMean, double aVariance){
	    return (int) (aMean + fRandom.nextGaussian() * aVariance);
	  }
	}
	private static void log(Object aMsg){
	    System.out.println(String.valueOf(aMsg));
	  }
}
