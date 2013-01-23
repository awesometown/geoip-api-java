package com.maxmind.geoip;

import java.io.EOFException;
import java.io.IOException;

public class ByteArrayRandomAccessData implements RandomAccessData {

	private byte[] buf;
	private boolean isClosed = false;
	private int ptr = 0;

	public ByteArrayRandomAccessData(byte[] buffer) {
		buf = buffer;
	}

	private void throwIfClosed() throws IOException {
		if (isClosed) {
			throw new IOException("RandomAccessData has been closed");
		}
	}

	@Override
	public void close() throws IOException {
		isClosed = true;
		buf = null;
	}

	@Override
	public long getFilePointer() throws IOException {
		throwIfClosed();
		return ptr;
	}

	@Override
	public long length() throws IOException {
		throwIfClosed();
		return buf.length;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return readBytes(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return readBytes(b, off, len);
	}

	private int readBytes(byte[] b, int off, int len) throws IOException {
		throwIfClosed();
		int startIndex = ptr + off;
		if (startIndex >= buf.length) {
			return -1;
		}
		int length = Math.min(buf.length - startIndex, len);
		System.arraycopy(buf, ptr, b, 0, length);
		ptr += length;
		return length;
	}

	@Override
	public byte readByte() throws IOException {
		throwIfClosed();
		return buf[ptr++];
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		throwIfClosed();
		readFully(b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int offset, int length) throws IOException {
		throwIfClosed();
		int n = 0;
		do {
			int count = this.read(b, offset + n, length - n);
			if (count < 0) {
				throw new EOFException();
			}
			n += count;
		}
		while (n < length);
	}

	@Override
	public void seek(long pos) throws IOException {
		if (pos < 0) {
			throw new IOException("Position must be non-negative.");
		} else if (pos > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("ByteArrayRandomAccessData does not support indexes larger than " + Integer.MAX_VALUE);
		}
		ptr = (int) pos;
	}

}
