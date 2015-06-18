import java.io.File;
import java.io.IOException;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

public class IPLocator {
	private static final IPLocator istance = new IPLocator();
	private File file;

	public static IPLocator getIstance() {
		return istance;
	}

	public static void main(String[] args) {
		IPLocator obj = new IPLocator();
		ServerLocation location = obj.getLocation("126.190.36.45");
		System.out.println(location);
	}

	private IPLocator() {
		String path = System.getProperty("user.dir") + "\\src\\main\\resources";
		file = new File(path + "\\GeoLiteCity.dat");
	}

	public ServerLocation getLocation(String ipAddress) {
		ServerLocation serverLocation = null;
		try {

			serverLocation = new ServerLocation();

			LookupService lookup = new LookupService(file,
					LookupService.GEOIP_MEMORY_CACHE);
			Location locationServices = lookup.getLocation(ipAddress);

			// serverLocation.setCountryCode(locationServices.countryCode);
			serverLocation.setCountryName(locationServices.countryName);
			/*
			 * serverLocation.setRegion(locationServices.region);
			 * serverLocation.setRegionName(regionName.regionNameByCode(
			 * locationServices.countryCode, locationServices.region));
			 * serverLocation.setCity(locationServices.city);
			 * serverLocation.setPostalCode(locationServices.postalCode);
			 * serverLocation
			 * .setLatitude(String.valueOf(locationServices.latitude));
			 * serverLocation
			 * .setLongitude(String.valueOf(locationServices.longitude));
			 */
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return serverLocation;

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
