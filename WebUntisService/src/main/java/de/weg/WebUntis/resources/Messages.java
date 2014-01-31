package de.weg.WebUntis.resources;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "de.weg.isp.WUMessages";

	public static final String COMMON_DOPPELPUNKT = "COMMON.DOPPELPUNKT";
	public static final String COMMON_LEERZEICHEN = "COMMON.LEERZEICHEN";
	public static final String COMMON_SEMIKOLON = "COMMON.SEMIKOLON";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(String key, Object... params) {
		try {
			return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	private Messages() {
	}
}
