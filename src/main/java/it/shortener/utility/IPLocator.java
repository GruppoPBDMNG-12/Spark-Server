package it.shortener.utility;
import java.io.File;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class IPLocator {
	private static final IPLocator istance = new IPLocator();
	private File file;

	public static IPLocator getIstance() {
		return istance;
	}

	public static void main(String[] args) {
		IPLocator obj = new IPLocator();
		String location = obj.getLocation("192.168.0.1");
		System.out.println(location);
	}

	private IPLocator() {
		String path = System.getProperty("user.dir") + "\\src\\main\\resources";
		file = new File(path + "\\GeoLiteCity.dat");
	}

	public String getLocation(String ipAddress) {
		ServerLocation serverLocation = null;
		try {

			serverLocation = new ServerLocation();
			LookupService lookup = new LookupService(file,
					LookupService.GEOIP_MEMORY_CACHE);
			Location locationServices = lookup.getLocation(ipAddress);
			serverLocation.setCountryName(locationServices.countryName);

		} catch (Exception e) {
			serverLocation.setCountryName("localhost");
		}
		return serverLocation.toString();

	}

	class ServerLocation {
		String country;

		public void setCountryName(String country) {
			this.country = country;
		}

		public String toString() {
			return country;
		}
	}
}
