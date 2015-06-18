
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class MyJSonReader {
	private JSONObject jsonObj;
	public MyJSonReader(MyJSonString jsonString){
		jsonObj= new JSONObject(jsonString);
	}
	public String getNode(String key){
		 
		System.out.println(jsonObj.getString(key));
		return jsonObj.getString(key);
	}
	
	public ArrayList<Click> getArrayNode(String key){
		ArrayList<Click> clicks=new ArrayList<Click>();
		JSONArray values = jsonObj.getJSONArray("animals");
		  
		  for (int i = 0; i < values.length(); i++) {
		    
		    JSONObject clickJsonObj = values.getJSONObject(i); 
		    clicks.add(Click.jsonToClick(clickJsonObj));
		  }
		return clicks;
	}
}
