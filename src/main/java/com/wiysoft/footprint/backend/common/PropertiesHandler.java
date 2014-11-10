package com.wiysoft.footprint.backend.common;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public final class PropertiesHandler {

	private static final Logger logger = Logger.getLogger(PropertiesHandler.class);

	public static final String PROP_ASSETS_ROOT = "com.wiysoft.footprint.assetsRoot";
	public static final String PROP_ASSETS_ROOT_DEFAULT = System.getenv("user.path") + "/footprint-backend/assets";

	private static Properties props = new Properties();

	static {
		try {
			Properties props = PropertiesLoaderUtils.loadAllProperties("footprint.properties");
			File assetsRootFile = new File(props.getProperty(PROP_ASSETS_ROOT, PROP_ASSETS_ROOT_DEFAULT));
			if (!assetsRootFile.exists()) {
				if (!assetsRootFile.mkdirs()) {
					logger.warn("Failed to create assets root directory: " + assetsRootFile.getAbsolutePath());
				}
			}

		} catch (IOException e) {
			logger.error("Loading footprint.properties failed.", e);
		}
	}

	public PropertiesHandler() {

	}

	public static String getAssetsRoot() {
		String value = props.getProperty(PROP_ASSETS_ROOT, PROP_ASSETS_ROOT_DEFAULT);
		return value.endsWith("/") ? value : (value + "/");
	}
}
