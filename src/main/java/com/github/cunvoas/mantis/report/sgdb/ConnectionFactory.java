/**
 * 
 */
package com.github.cunvoas.mantis.report.sgdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cunvoas.mantis.report.util.ConfigProperties;

/**
 * @author CUNVOAS
 */
public class ConnectionFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private static boolean registred=false;
	
	/**
	 * get mantis connection.
	 * @return
	 */
	public static Connection getConnection() {
		String driver = ConfigProperties.getProperty(ConfigProperties.JDBC_DRIVER);
		String url = ConfigProperties.getProperty(ConfigProperties.JDBC_URL);
		String user = ConfigProperties.getProperty(ConfigProperties.JDBC_LOGIN);
		String passwd = ConfigProperties.getProperty(ConfigProperties.JDBC_PASSWD);
		
		Connection connection = null;
		try {
			
			if (!registred)  {
				Class.forName(driver).newInstance();
				registred=true;
			}
			connection = DriverManager.getConnection(url, user, passwd);
			
		} catch (InstantiationException e) {
			LOGGER.error("JDBC Driver cannot be instantiated", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("JDBC Driver loading error", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("JDBC Driver not found", e);
		} catch (SQLException e) {
			LOGGER.error("JDBC Driver connection error", e);
		}
		
		return connection;
	}
	
	public static void closeQuietly(Connection closable) {
		if (closable!=null) {
			try {
				closable.close();
			} catch (SQLException e) {
				//ignore
			}
		}
	}
	
	public static void closeQuietly(Statement closable) {
		if (closable!=null) {
			try {
				closable.close();
			} catch (SQLException e) {
				//ignore
			}
		}
	}
	
	public static void closeQuietly(ResultSet closable) {
		if (closable!=null) {
			try {
				closable.close();
			} catch (SQLException e) {
				//ignore
			}
		}
	}
}
