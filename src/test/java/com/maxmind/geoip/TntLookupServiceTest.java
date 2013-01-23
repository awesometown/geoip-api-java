package com.maxmind.geoip;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Random;

import org.testng.annotations.Test;

public class TntLookupServiceTest {

	@Test
	public void testTntResultsConsistentWithMaxmind() throws IOException {
		String dataFile = "src/test/resources/GeoLiteCity.dat";
		ReferenceLookupService ls = new ReferenceLookupService(dataFile);
		LookupService tntls = new LookupService(getRandomAccessData(dataFile));

		Random r = new Random(new Date().getTime());
		byte[] addrBytes = new byte[4];

		for (int i = 0; i < 10000; i++) {
			r.nextBytes(addrBytes);
			InetAddress addr = InetAddress.getByAddress(addrBytes);
			Location expected = ls.getLocation(addr);
			Location actual = tntls.getLocation(addr);
			if (expected == null) {
				assertNull(actual);
			} else {
				assertEquals(actual.area_code, expected.area_code);
				assertEquals(actual.countryCode, expected.countryCode);
				assertEquals(actual.countryName, expected.countryName);
				assertEquals(actual.region, expected.region);
				assertEquals(actual.city, expected.city);
				assertEquals(actual.postalCode, expected.postalCode);
				assertEquals(actual.latitude, expected.latitude);
				assertEquals(actual.longitude, expected.longitude);
				assertEquals(actual.dma_code, expected.dma_code);
				assertEquals(actual.area_code, expected.area_code);
				assertEquals(actual.metro_code, expected.metro_code);
			}
		}
	}

	private RandomAccessData getRandomAccessData(String sourceFile) throws IOException {
		File f = new File(sourceFile);
		byte[] bytes = new byte[(int) f.length()];

		FileInputStream in = new FileInputStream(f);
		in.read(bytes);
		in.close();

		return new ByteArrayRandomAccessData(bytes);
	}
}
