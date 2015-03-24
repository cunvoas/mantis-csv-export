package com.github.cunvoas.mantis.report.mantis;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvReport {
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvReport.class);
	
	
	public void report(File csvFileOut, ResultSet resultSet) {
		OutputStream csvOut = null;
		CSVPrinter csvPrinter = null;
		
		try {
			Character delimiter = Character.valueOf(',');
			Character escape = Character.valueOf('"');
			
			
			CSVFormat format = CSVFormat.DEFAULT
									.withHeader(resultSet)
									.withDelimiter(delimiter)
									.withEscape(escape)
									;
			
			//CSVFormat format = CSVFormat.EXCEL.withHeader(resultSet).withEscape(Character.valueOf('"'));
			csvOut = FileUtils.openOutputStream(csvFileOut, true);
			csvPrinter = new CSVPrinter(new PrintWriter(csvOut), format);
			
			csvPrinter.printRecords(resultSet);
			
		} catch (IOException e) {
			LOGGER.error("CSV Write Error", e);
		} catch (SQLException e) {
			LOGGER.error("CSV extract error", e);
		} finally {
			IOUtils.closeQuietly(csvPrinter);
			IOUtils.closeQuietly(csvOut);
		}
	}
}
