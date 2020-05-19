package util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;

/**
 * A common interface for finding the installation settings for the
 * implementation.
 * 
 * @author Petr Troshin
 * @version 1.0 October 2009
 */
public class PropertyHelper {

	private final java.util.Properties properties;

	private static Logger log = Logger.getLogger(PropertyHelper.class);

	public PropertyHelper(StringReader input) {
		try {
			this.properties = new java.util.Properties();
			this.properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Properties file could not be found !",
					e);
		}
	}

	public PropertyHelper(String... propertyPaths) throws IOException {
		this(getReader(propertyPaths));
	}

	public PropertyHelper(File... propertyPaths) throws IOException {
		this(getReader(propertyPaths));
	}

	/**
	 * @param propertiesFile
	 * @throws FileNotFoundException
	 * 
	 *             public PropertyHelper(File propertiesFile) throws
	 *             FileNotFoundException { this(new
	 *             java.io.FileInputStream(propertiesFile)); }
	 */
	/**
	 * @return input stream for default location for properties
	 * @throws IOException
	 */
	private static StringReader getReader(String... propertyPaths)
			throws IOException {
		String props = "";
		for (String path : propertyPaths) {
			File prop = new File(path);
			props += PropertyHelper.readProp(prop);
		}
		return new StringReader(props);
	}

	private static String readProp(File propFile) throws IOException {
		String props = "";
		if (propFile == null) {
			log.error("PropFile is expected!");
			return "";
		}
		if (propFile.exists() && propFile.canRead()) {
			props += FileUtil.readFileToString(propFile) + SysPrefs.newlinechar;

		} else {
			log.debug("Cannot read properties file: "
					+ propFile.getAbsolutePath());
		}
		return props;
	}

	private static StringReader getReader(File... propertyPaths)
			throws IOException {
		String props = "";
		assert propertyPaths != null && propertyPaths.length > 0 : " No property files provided!";
		for (File prop : propertyPaths) {
			log.debug("Reading properties from file: " + prop.getAbsolutePath());
			props += PropertyHelper.readProp(prop);
		}
		return new StringReader(props);
	}

	public String getProperty(String name) {
		// load from context first
		String property = null;
		// load from file
		if (!Util.isEmpty(name) && this.properties != null) {
			property = this.properties.getProperty(name);
		}
		// it is an error if both do not have this name
		if (property == null) {
			log.warn("Properties '" + name + "' not found in file.");
			return null;
		}
		return property.trim();
	}

	public java.util.Properties getProperties() {
		return this.properties;
	}
}
