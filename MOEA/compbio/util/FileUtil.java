package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Utility methods to work with file system
 * 
 * @author pvtroshin
 * @version 1.0 July 2009
 */
public class FileUtil {

	private static final Logger log = Logger.getLogger(FileUtil.class);

	public static boolean isDirectoryEmpty(String dirpath) {
		assert !Util.isEmpty(dirpath);
		return isDirectoryEmpty(new File(dirpath));
	}

	public static boolean exist(String file) {
		assert !Util.isEmpty(file);
		return new File(file).exists();
	}

	/**
	 * Copy the content of the sourceFile to the destination File.
	 * 
	 * @param sourceFile
	 *            the source
	 * @param destinationFile
	 *            the destination
	 * @throws IOException
	 */
	public static void copy(File sourceFile, File destinationFile)
			throws IOException {
		if (!destinationFile.exists()) {
			destinationFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destinationFile).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
		} finally {
			if (destination != null && destination.isOpen()) {
				destination.close();
			}
			if (source != null && source.isOpen()) {
				source.close();
			}
		}
	}

	/**
	 * 
	 * Method copies the content of the source Stream to the destination File.
	 * The source stream are left open.
	 * 
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @throws IOException
	 */
	public static void copy(InputStream source, File destination)
			throws IOException {

		BufferedInputStream bfinput = new BufferedInputStream(source);
		BufferedOutputStream destStream = null;
		try {
			destStream = new BufferedOutputStream(new FileOutputStream(
					destination));
			byte[] buff = new byte[1024];
			int length = 0;
			while (true) {
				length = bfinput.read(buff);
				if (length <= 0) {
					break;
				}
				destStream.write(buff, 0, length);
			}
			destStream.close();
		} finally {
			closeSilently(destStream);
		}
	}

	private static boolean isNotEmpty(String filepath,
			int minSizeExpectedInBytes) {
		assert !Util.isEmpty(filepath);
		File file = new File(filepath);
		if (!file.exists()) {
			return false;
		}

		assert file.isFile() : "File expected! but directory is given!";
		assert file.canRead() : "Does not have permissions to read the file!";

		long bytelenght = file.length();
		// Assume that file must have content
		if (bytelenght > minSizeExpectedInBytes) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean hasData(String filepath, int minSizeExpectedInBytes) {
		return isNotEmpty(filepath, minSizeExpectedInBytes);
	}

	public static boolean hasData(String filepath) {
		return isNotEmpty(filepath, 0);
	}

	public static boolean isDirectoryEmpty(File directory) {
		assert directory != null;
		assert directory.canRead() : "Cannot read!";
		assert directory.isDirectory() : "Directory expected, but file is given!";

		return directory.listFiles().length == 0;
	}

	/**
	 * Get list of all files from the particular directory
	 * 
	 * @param directory
	 *            name
	 * @return Array of the files in the directory
	 */
	public static File[] getAllFiles(final String directory) {
		final File ff = new File(directory);
		return ff.listFiles();
	}

	/**
	 * Get list of files from the particular directory passed through FileFilter
	 * 
	 * @param directory
	 *            name
	 * @param fileFilter
	 * @return Array of the files in the directory which satisfy the FileFilter
	 *         criteria
	 */
	public static File[] getFiles(final String directory,
			final FileFilter fileFilter) {
		final File ff = new File(directory);
		return ff.listFiles(fileFilter);
	}

	/**
	 * 
	 * @param fileName
	 * @return file extension
	 */
	public static String getFileExtension(final String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")).trim();
	}

	/**
	 * Create a temp file of a given size Util.getTempFile
	 * 
	 * @param sizeinKBytes
	 *            value must be between 1 (kb) and 1000000 (1Gb)
	 * @return File
	 * @throws IOException
	 */
	public static File getTempFile(final int sizeinKBytes) throws IOException {
		assert sizeinKBytes > 0 : "Size must be positive integer but the value received is: "
				+ sizeinKBytes;
		assert sizeinKBytes < 1000000 : "Size must be less than 1 Gb!";
		final File file = File.createTempFile("" + System.currentTimeMillis(),
				".bin");
		final OutputStream out = new FileOutputStream(file);
		final byte buf[] = new byte[1024];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) i;
		}
		for (int i = 0; i < sizeinKBytes; i++) {
			out.write(buf);
		}
		try {
			out.close();
		} finally {
			closeSilently(log, out);
		}
		return file;
	}

	public static byte[] readFile(final File file) throws IOException {
		FileInputStream inStream = new FileInputStream(file);
		final BufferedInputStream bis = new BufferedInputStream(inStream);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[256];
		int length;
		while (true) {
			length = bis.read(buffer);
			if (0 >= length) {
				break;
			}
			out.write(buffer, 0, length);
		}
		byte[] result = out.toByteArray();
		try {
			out.close();
			bis.close();
			inStream.close();
		} finally {
			closeSilently(log, out);
			closeSilently(log, bis);
			closeSilently(log, inStream);
		}
		return result;
	}

	public static String readFileToString(final InputStream inStream)
			throws IOException {
		final byte[] bytes = new byte[inStream.available()];
		inStream.read(bytes);
		final ByteArrayOutputStream contentStr = new ByteArrayOutputStream();
		contentStr.write(bytes);
		String content = contentStr.toString();
		try {
			contentStr.close();
		} finally {
			closeSilently(log, contentStr);
		}
		return content;
	}

	/**
	 * FilenameFilter implementation Allow filter the directory contents by file
	 * extension.
	 */
	public static class ExtensionFilter implements FilenameFilter {

		String extension = "";

		public ExtensionFilter(final String extension) {
			this.extension = extension;
		}

		public boolean accept(final File dir, final String name) {
			return (name.endsWith(this.extension));
		}
	}

	/**
	 * Reads file into a single String, returns empty string if file was empty
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(final File file) throws IOException {
		String fileStr = "";
		String line = null;
		if (file.exists()) {
			FileReader reader = new FileReader(file);
			final BufferedReader br = new BufferedReader(reader);
			while ((line = br.readLine()) != null) {
				fileStr += line + "\n";
			}
			try {
				br.close();
				reader.close();
			} finally {
				closeSilently(log, br);
				closeSilently(log, reader);
			}
		}
		return fileStr;
	}

	/**
	 * Write any String data to file.
	 * 
	 * @param data
	 * @param filePathandName
	 *            File name with full path
	 * @throws IOException
	 */
	public static void writeToFile(final String data,
			final String filePathandName) throws IOException {
		FileWriter fwriter = null;
		BufferedWriter bw = null;
		try {
			fwriter = new FileWriter(filePathandName);
			bw = new BufferedWriter(fwriter);
			bw.write(data);
			bw.close();
			fwriter.close();
		} finally {
			closeSilently(log, bw);
			closeSilently(log, fwriter);
		}
	}

	/**
	 * Write any String data to file.
	 * 
	 * @param data
	 * @param filePathandName
	 *            File name with full path
	 * @throws IOException
	 */
	public static void writeToFile(final byte[] data,
			final String filePathandName, boolean append) throws IOException {
		FileOutputStream outStream = null;
		BufferedOutputStream bw = null;
		try {
			outStream = new FileOutputStream(filePathandName, append);
			bw = new BufferedOutputStream(outStream);
			bw.write(data);
			bw.close();
			outStream.close();
		} finally {
			closeSilently(log, bw);
			closeSilently(log, outStream);
		}
	}

	public static void appendToFile(final byte[] data,
			final String filePathandName) throws IOException {
		writeToFile(data, filePathandName, true);
	}

	public static void writeToFile(final byte[] data,
			final String filePathandName) throws IOException {
		writeToFile(data, filePathandName, false);
	}

	public static List<String> getFileNameList(String path) {
		return getFileNameList(path, null);
	}

	public static List<String> getFileNameList(String path,
			FilenameFilter filter) {
		assert !util.Util.isEmpty(path);
		assert new File(path).isDirectory();
		if (filter == null) {
			return Arrays.asList(new File(path).list());
		}
		return Arrays.asList(new File(path).list(filter));
	}

	public final static void closeSilently(Logger log, Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage(), e.getCause());
			}
		}
	}

	public final static void closeSilently(java.util.logging.Logger log,
			Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				log.severe(e.getLocalizedMessage() + " Cause: " + e.getCause());
			}
		}
	}

	public final static void closeSilently(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage(), e.getCause());
			}
		}
	}
}
