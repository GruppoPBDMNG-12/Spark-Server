package it.shortener.populate_db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HREFRetriver {
	
	
	
	
	public static ArrayList<String> retriveUrls(String filePath) {
		ArrayList<String>urls=new ArrayList<String>();
		BufferedReader br = null;
		 String htmlText="";
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line;
			    while ((line = br.readLine()) != null) {
			       htmlText+=line;
			    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		// the pattern we want to search for
		Pattern p = Pattern.compile("<A HREF=\"(\\S+)\"");
		Matcher m = p.matcher(htmlText);

		// if we find a match, get the group
		while (m.find()) {
			// get the matching group
			// get the matching group
			String codeGroup = m.group(1);
			if(codeGroup.startsWith("http")){
				urls.add(codeGroup);
				System.out.format("%s\n", codeGroup);
			}
			// print the group
		}
		return urls;
	}
}
