import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;


public class MyJSonWriter {
	public static void main(String[] args) {
		String a=MyJSonWriter.addAssociationToJson("snUnoShort.html", "Sono un long.html");
		MyJSonReader jsonReader=new MyJSonReader(new MyJSonString(a));
		jsonReader.getNode(a);
		
	}
	public static String addAssociationToJson(String shortUrl,String longUrl){
		String toJson="";
		
		
		JsonObjectBuilder empBuilder = Json.createObjectBuilder();
        //JsonObjectBuilder addressBuilder = Json.createObjectBuilder();
 
        empBuilder.add("shortUrl", shortUrl).add("longUrl", longUrl);
        /*for (long phone : emp.getPhoneNumbers()) {
            phoneNumBuilder.add(phone);
        }
         
        addressBuilder.add("street", emp.getAddress().getStreet())
                        .add("city", emp.getAddress().getCity())
                            .add("zipcode", emp.getAddress().getZipcode());
         
        empBuilder.add("id", emp.getId())
                    .add("name", emp.getName())
                        .add("permanent", emp.isPermanent())
                            .add("role", emp.getRole());
         
        empBuilder.add("phoneNumbers", phoneNumBuilder);
        empBuilder.add("address", addressBuilder);
         */
        JsonObject empJsonObject = empBuilder.build();
         
        toJson=empJsonObject.toString();
        System.out.println(toJson);
		return toJson;
	}
}
