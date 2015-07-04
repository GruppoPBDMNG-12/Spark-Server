package it.shortener.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class BadWordsChecker {
	private static HashSet<String> hashSet = new HashSet<String>();

	private static void init() {
		if (hashSet.isEmpty()) {
			FileReader fileReader=null;
			BufferedReader bufferedReader=null;
			try {
				String path = System.getProperty("user.dir")
						+ "\\src\\main\\resources\\";
				fileReader = new FileReader(path + "ProfanityWords.txt");

				bufferedReader = new BufferedReader(fileReader);
				while (true) {
					String wordGetted= bufferedReader.readLine();
					
					if (wordGetted == null) {
						break;
					} else {
						hashSet.add(wordGetted);
					}
				}
			} catch (Exception e) {
				System.out.println("arrivo in errore" +e.getMessage());
				try {
					bufferedReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
	}
	public static boolean isBadWordsFree(String s){
		BadWordsChecker.init();
		
		for(String badWord:hashSet){
			if(s.contains(badWord))
				return false;
		}
		return true;
	}
}