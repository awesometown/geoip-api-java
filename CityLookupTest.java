/* CityLookupTest.java */

import com.maxmind.geoip.*;
import java.io.IOException;

/* sample of how to use the GeoIP Java API with GeoIP City database */
/* Usage: java CityLookupTest 64.4.4.4 */

class CityLookupTest {
    public static void main(String[] args) {
	try {
	    LookupService cl = new LookupService("/usr/local/share/GeoIP/GeoIPCity.dat");
            Location l1 = cl.getLocation("213.52.50.8");
            Location l2 = cl.getLocation(args[0]);
	    System.out.println("countryCode: " + l2.countryCode +
                               "\n countryName: " + l2.countryName +
                               "\n region: " + l2.region +
                               "\n city: " + l2.city +
                               "\n postalCode: " + l2.postalCode +
                               "\n latitude: " + l2.latitude +
                               "\n longitude: " + l2.longitude +
                               "\n distance: " + l2.distance(l1) +
                               "\n distance: " + l1.distance(l2) + 
 			       "\n dma code: " + l2.dma_code +
 			       "\n area code: " + l2.area_code);

	    cl.close();
	}
	catch (IOException e) {
	    System.out.println("IO Exception");
	}
    }
}
