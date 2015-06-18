package it.shortener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
 
public class IndexFileReader {
 
	public static void getTextFile(OutputStream os) {
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
			String path=System.getProperty("user.dir")+"\\src\\main\\resources";
			br = new BufferedReader(new FileReader(path+"\\index.html"));
			FileReader fr=new FileReader(path+"\\index.html");
			
			while(fr.ready()){ 
				os.write(fr.read());
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
	}
}