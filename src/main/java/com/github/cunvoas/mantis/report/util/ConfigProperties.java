package com.github.cunvoas.mantis.report.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigProperties {
	
	public static final String JDBC_DRIVER="jdbc-driver";
	public static final String JDBC_URL="jdbc-url";
	public static final String JDBC_LOGIN="jdbc-login";
	public static final String JDBC_PASSWD="jdbc-passwd";

	public static final String MANTIS_TABLE_PREFIX="table-prefix";

			
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProperties.class);
    
    private static final String BUNDLE_NAME = "mantisdb";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    
    private ConfigProperties() {
    }

    public static String getProperty(String key) {
        try {
        	String prop = RESOURCE_BUNDLE.getString(key);
        	LOGGER.debug("BUNDLE {}: {}={}", BUNDLE_NAME, key, prop);
            return prop;
        } catch (MissingResourceException e) {
            LOGGER.warn("Impossible de trouver la cle {} dans le fichier {}" , new Object[] {  ConfigProperties.class, key, BUNDLE_NAME});
            return '!' + key + '!';
        }
    }
}
