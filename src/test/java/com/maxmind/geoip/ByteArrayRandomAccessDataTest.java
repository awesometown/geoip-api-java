package com.maxmind.geoip;

import static org.testng.Assert.assertEquals;

import java.io.EOFException;
import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class ByteArrayRandomAccessDataTest {

	private byte[] defaultBytes = new byte[256];
	private ByteArrayRandomAccessData data;

	@BeforeClass
	public void setupSuite() {
		for (int i = 0; i < 255; i++) {
			defaultBytes[i] = (byte) i;
		}
	}

	@BeforeMethod
	public void setup() {
		data = new ByteArrayRandomAccessData(defaultBytes);
	}

	@Test
	public void testReadReturnsMinus1WhenNoData() throws IOException {
		ByteArrayRandomAccessData empty = new ByteArrayRandomAccessData(new byte[0]);
		assertEquals(empty.read(new byte[1]), -1);

	}

	@Test
	public void testReadReturnsMinus1WhenNoMoreData() throws IOException {
		data.seek(256);
		assertEquals(data.read(new byte[1]), -1);
	}

	@Test
	public void testReadReturnsTruncatedResultWhenInsufficientData() throws IOException {
		byte[] buf = new byte[512];
		long read = data.read(buf);
		assertEquals(read, 256);
		for (int i = 256; i < 512; i++) {
			assertEquals(buf[i], 0);
		}
	}

	@Test
	public void testReadByteMovesPointerForward() throws IOException {
		assertEquals(data.readByte(), 0);
		assertEquals(data.readByte(), 1);
	}

	@Test
	public void testReadReturnsExpectedData() throws IOException {
		byte[] buf = new byte[256];
		long read = data.read(buf);
		assertEquals(read, 256);
		assertEquals(buf, defaultBytes);
	}

	@Test
	public void testReadWithLenOnlyReadsLenBytes() throws IOException {
		byte[] buf = new byte[100];
		long read = data.read(buf, 0, 50);
		assertEquals(read, 50);
		assertEquals(buf[49], 49);
		assertEquals(buf[50], 0);
	}

	@Test
	public void testReadMultipleUpdatesFilePointer() throws IOException {
		byte[] buf = new byte[10];
		data.read(buf);
		assertEquals(data.getFilePointer(), 10);
	}

	@Test
	public void testSeekingMovesToExpectedLocation() throws IOException {
		data.seek(234);
		assertEquals(data.readByte(), (byte) 234);
	}

	@Test(expectedExceptions = IOException.class)
	public void testSeekNegativeThrowsException() throws IOException {
		byte[] bytes = new byte[200];
		ByteArrayRandomAccessData data = new ByteArrayRandomAccessData(bytes);
		data.seek(-1);
	}

	@Test(expectedExceptions = EOFException.class)
	public void testReadFullyThrowsEOFWhenOutOfData() throws Exception {
		byte[] bytes = new byte[300];
		data.readFully(bytes);
	}

	@Test
	public void testReadFullyReadsLenBytes() throws IOException {
		byte[] bytes = new byte[100];
		data.readFully(bytes, 0, 50);
		assertEquals(bytes[49], 49);
		assertEquals(bytes[50], 0);
	}

}
