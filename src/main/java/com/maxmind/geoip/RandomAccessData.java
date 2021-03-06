package com.maxmind.geoip;

import java.io.IOException;

public interface RandomAccessData {

	void close() throws IOException;

	long getFilePointer() throws IOException;

	long length() throws IOException;

	int read(byte[] b) throws IOException;

	int read(byte[] b, int off, int len) throws IOException;

	byte readByte() throws IOException;

	void readFully(byte[] b) throws IOException;

	void readFully(byte[] b, int offset, int length) throws IOException;

	void seek(long pos) throws IOException;
}
