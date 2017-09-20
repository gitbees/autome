package com.auto.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public final class CukeConstants {

	public static java.util.Properties testdataprops = null;

	private static Logger logger = LoggerFactory.getLogger(CukeConstants.class);

	static {

		try {

			testdataprops = new Properties();
			InputStream in = CukeConstants.class.getClassLoader().getResourceAsStream("testdata.properties");
			testdataprops.load(in);

		} catch (IOException obj) {
			if (logger.isErrorEnabled()) {
				logger.error(
						"Exception occured while loading the Properties files",
						obj);
			}
		}

	}

	public interface TEST_DATA {
		
		String ENV = (String) testdataprops.getProperty("ENV");

		String BROWSER = (String) testdataprops.getProperty("BROWSER");

		String LOCATION = (String) testdataprops.getProperty("LOCATION");

		String BASE_URL = (String) testdataprops.getProperty("BASE_URL");

	}

	public static void main(String args[]) {
		
		System.out.println((String) testdataprops.getProperty("ENV"));
	}

}
