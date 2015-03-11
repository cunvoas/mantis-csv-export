/**
 * 
 */
package com.github.cunvoas.mantis.report;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cunvoas.mantis.report.mantis.CsvReport;
import com.github.cunvoas.mantis.report.mantis.QueryHelper;
import com.github.cunvoas.mantis.report.sgdb.ConnectionFactory;
import com.github.cunvoas.mantis.report.util.FileEncodingConverter;

/**
 * @author CUNVOAS
 */
public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info("START EXPORT");
		System.setProperty("file.encoding", "Cp1252");
		
		CsvReport csvReport = new CsvReport();

		Connection connection = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		File csvFile = null;
		try {
			connection = ConnectionFactory.getConnection();
			
			String query = QueryHelper.getReportQueryTpl();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
						 
			csvFile = new File("c:/mantis.csv");
			FileUtils.deleteQuietly(csvFile);

			csvReport.report(csvFile, rs);
			
		} catch (Exception e) {
			LOGGER.error("Export Mantis", e);
		} finally {
			ConnectionFactory.closeQuietly(rs);
			ConnectionFactory.closeQuietly(ps);
			ConnectionFactory.closeQuietly(connection);
		}

		LOGGER.info("STOP EXPORT");
		LOGGER.info("START CONVERT ENCODING");

		FileEncodingConverter.convert(csvFile, 
				Charset.forName("UTF-8"), Charset.forName("windows-1252"));// ISO-8859-1
		LOGGER.info("END CONVERT ENCODING");

	}

}
