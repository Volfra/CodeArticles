package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.log4j.Logger;

/**
 * A utility to access the content of the file from a specific position while
 * the file is being written to from external processes.
 * 
 * @author pvtroshin
 * @version 1.0 December 2010
 */
public class FileWatcher {

	public static final int MIN_CHUNK_SIZE_BYTES = 256; // 256 byte
	public static final int MAX_CHUNK_SIZE_BYTES = 1024 * 1024 * 10; // 10Mb

	private static final Charset charset = Charset.forName("UTF-8");
	private static final CharsetDecoder decoder = charset.newDecoder();
	private static final Logger log = Logger.getLogger(FileWatcher.class);

	private long read = 0;
	private int chunkSizeBytes;
	private FileChannel fc;

	private FileWatcher(FileInputStream input, int chunkSizeBytes) {
		assert input != null : "Input must be provided!";
		assert chunkSizeBytes >= 1;
		this.chunkSizeBytes = chunkSizeBytes;
		fc = input.getChannel();
	}

	/**
	 * 
	 * @param input
	 * @param chunkSizeBytes
	 * @return
	 */
	public static FileWatcher newFileWatcher(String input, int chunkSizeBytes) {
		validateInput(input, chunkSizeBytes);
		File file = new File(input);
		FileInputStream freader = null;
		try {
			freader = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			throw new IllegalArgumentException("File " + file.getAbsolutePath()
					+ " must exist!");
		}
		return new FileWatcher(freader, chunkSizeBytes);
	}

	public static FileWatcher newProgressWatcher(String input) {
		if (Util.isEmpty(input)) {
			throw new NullPointerException("Input must be provided!");
		}
		File file = new File(input);
		FileInputStream freader = null;
		try {
			freader = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			throw new IllegalArgumentException("File " + file.getAbsolutePath()
					+ " must exist!");
		}
		return new FileWatcher(freader, 3);
	}

	public boolean disconnected() {
		return fc == null;
	}

	public String pull() throws IOException {
		if (disconnected()) {
			throw new IllegalStateException(
					"This FileWatcher has disconnected from the file. Please construct a new one.");
		}
		/*
		 * It is legal to still pull even if there is not data in the file given
		 * the knowledge that it will be there later, thus return empty string
		 * if there is no data available as yet.
		 */
		if (!hasMore()) {
			return "";
		}
		// Get the file's size and then map it into memory
		long sz = fc.size();

		// Could cast safely as chunkSize is in int range
		if (chunkSizeBytes > sz) {
			chunkSizeBytes = (int) sz;
		}
		// Could cast safely as chunkSize is in int range
		// Allocate Buffer of exact size needed to accommodate the remaining
		// data, to make sure that the data transfered is exactly the same
		// as in
		// the file
		// log.debug("Size: " + sz);
		// log.debug("Read: " + read);
		if (sz < read) {
			throw new IllegalStateException(
					"File has been reduced during pooling! File size is " + sz
							+ ". While " + read
							+ " bytes has been already read from this file!");
		}
		// log.debug("Changing the chunk size to: " + chunkSizeBytes);
		return read(read).toString();
	}

	private String read(long fromPosition) throws IOException {
		read = fromPosition;
		long sz = fc.size();
		if (read > 0 && sz - read < chunkSizeBytes) {
			chunkSizeBytes = (int) (sz - read);
		}
		// log.debug("Changing the chunk size to: " + chunkSizeBytes);
		ByteBuffer bf = ByteBuffer.allocateDirect(chunkSizeBytes);
		// Set cursor to the last position of the last read operation
		fc.position(read);
		int length = fc.read(bf);
		// Position cursor to the beginning of the data chunk
		bf.rewind();
		CharBuffer cb = decoder.decode(bf);
		// If something was read from file increment the position
		if (length > 0) {
			read += length;
		}
		return cb.toString();
	}

	public String pull(long position) throws IOException {
		if (disconnected()) {
			throw new IllegalStateException(
					"This FileWatcher has disconnected from the file. Please construct a new one.");
		}
		long size = fc.size();
		if (position == size) {
			log.debug("Cursor is at the end of the file! "
					+ "Nothing is going to be returned as a result of pull oparation!"
					+ "Consider checking whether the file has more data (FileWatcher.hasMore()) "
					+ "before pulling to avoid waist of resources!");
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException(
					"Position must be within 0 and file size (" + size
							+ ") but given value is " + position);
		}
		return read(position);
	}

	public static void validateInput(String input, int chunkSizeBytes) {
		if (chunkSizeBytes > MAX_CHUNK_SIZE_BYTES
				|| chunkSizeBytes < MIN_CHUNK_SIZE_BYTES) {
			throw new IllegalArgumentException(
					"chunkSize must be between 256 bytes and 10Mb. But given value is: "
							+ chunkSizeBytes);
		}
	}

	public boolean hasMore() throws IOException {
		if (disconnected()) {
			throw new IllegalStateException(
					"This FileWatcher has disconnected from the file. Please construct a new one.");
		}
		// Ask channel about the size, as it could have been modified from the
		// previous read
		long size = fc.size();
		// log.debug("HasMore read: " + read + " size: " + size);
		return read < size;
	}

	public long getCursorPosition() {
		if (disconnected()) {
			throw new IllegalStateException(
					"This FileWatcher has disconnected from the file. Please construct a new one.");
		}
		return read;
	}

	/**
	 * Disconnect method must be called explicitly, as this class is used to
	 * read incomplete files (e.g. when writing continues) it is impossible to
	 * know from within this class where the file was read completely or not.
	 * Thus it is the responsibility of the caller to call this method.
	 */
	public void disconnect() {
		if (fc != null) {
			// Close the channel and the stream
			try {
				fc.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				fc = null;
			}
		}
	}

	public byte getProgress() throws IOException {
		String progress = get3Chars();
		if (Util.isEmpty(progress)) {
			return 0;
		}
		progress = progress.trim();
		assert progress.length() <= 3;
		return Byte.parseByte(progress);
	}

	String get3Chars() throws IOException {
		if (disconnected()) {
			throw new IllegalStateException(
					"This FileWatcher has disconnected from the file. Please construct a new one.");
		}
		if (chunkSizeBytes > 3) {
			chunkSizeBytes = 3;
		}
		return read(0);
	}

}
