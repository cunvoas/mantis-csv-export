package com.github.cunvoas.mantis.report.mantis;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.cunvoas.mantis.report.util.ConfigProperties;
import com.mysql.jdbc.StringUtils;

public class QueryHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryHelper.class);
	
	private static final String[] VALUE_COLUMNS = {"priority", "severity", "view_state", "status", "resolution", "reproducibility", "projection", "eta"};
	
	public static String getReportQuery() {
		String qry=null;
		String prefix = ConfigProperties.getProperty(ConfigProperties.MANTIS_TABLE_PREFIX);
		
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("report.sql.txt");
			String path = url.getPath().replaceAll("%20", " ");
			qry = FileUtils.readFileToString(new File(path));
			
			
			
			qry = qry.replaceAll("%PREFIX%", prefix);
		} catch (IOException e) {
			LOGGER.error("Fichier de requete illisible", e);
		}
		
		return qry;
	}
	
	public static String getReportQueryTpl() {
		String qry=null;
		String prefix = ConfigProperties.getProperty(ConfigProperties.MANTIS_TABLE_PREFIX);
		
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("report.tpl.txt");
			String path = url.getPath().replaceAll("%20", " ");
			qry = FileUtils.readFileToString(new File(path));
			
			for (int i = 0; i < VALUE_COLUMNS.length; i++) {
				qry = qry.replaceAll("%"+VALUE_COLUMNS[i]+"%", getQryValueCaseColumn(VALUE_COLUMNS[i]));
			}
			
			qry = qry.replaceAll("%CUSTOM_COLUMNS%", getCustomColumns());
			
			qry = qry.replaceAll("%PREFIX%", prefix);
		} catch (IOException e) {
			LOGGER.error("Fichier de requete illisible", e);
		}
		
		LOGGER.info(qry);
		return qry;
	}
	
	
	protected static String getQryValueCaseColumn (String columName) {
		String keyValues = ConfigProperties.getProperty("column.conf."+columName+".values");
		String keyElse = ConfigProperties.getProperty("column.conf."+columName+".else");
		String keyAlias = ConfigProperties.getProperty("column.conf."+columName+".alias");
		if (keyAlias.equals("!column.conf."+columName+".alias!") || StringUtils.isNullOrEmpty(keyAlias)) {
			keyAlias = columName;
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("CASE MB.").append(columName);

		String[] values = keyValues.split(";");
		for (String value : values) {
			String[] kv = value.split(":");
			sBuilder.append(" WHEN '").append(kv[0]).append("' THEN '").append(kv[1]).append("'");
		}
		
		sBuilder.append(" ELSE '").append(keyElse).append(" '||MB.").append(columName);
		sBuilder.append(" END as ").append(keyAlias);
		
		return sBuilder.toString();
	}

	
	protected static String getCustomColumns() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			String propKey = ConfigProperties.getProperty("custom.field."+i);
			if ( ("!custom.field."+i+"!").equals(propKey) ) {
				break;
			}
			sb.append(formatCustomColumn(propKey));
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	protected static String formatCustomColumn(String propertyValue) {
		String[] items = propertyValue.split(";");
		
		StringBuilder sb = new StringBuilder();
		sb.append("\t, (SELECT ");
		if ("text".equals(items[1].toLowerCase())) {
			sb.append("value");
			
		} else if ("date".equals(items[1].toLowerCase())) {
			sb.append("case value WHEN 0 THEN '' ELSE DATE_FORMAT(FROM_UNIXTIME(value), '%Y-%m-%d') END");
		} else if ("checkbox".equals(items[1].toLowerCase())) {
			
			sb.append("CASE WHEN INSTR(value, '|')>0 THEN '' ELSE value END");
			
		} else {
			throw new RuntimeException("custom colum config error");
		}
		sb.append(" FROM %PREFIX%mantis_custom_field_string_table where field_id=");
		sb.append(items[0]);
		sb.append(" and bug_id=MB.id) as ");
		sb.append(items[2]);
/*			
, (SELECT value FROM %PREFIX%mantis_custom_field_string_table where field_id=79 and bug_id=MB.id) as Origine
, (SELECT CASE WHEN INSTR(value, '|')>0 THEN '' ELSE value END FROM %PREFIX%mantis_custom_field_string_table where field_id=83 and bug_id=MB.id) as Domaine
, (SELECT case value WHEN 0 THEN '' ELSE DATE_FORMAT(FROM_UNIXTIME(value), '%Y-%m-%d') END FROM %PREFIX%mantis_custom_field_string_table where field_id=80 and bug_id=MB.id) as DatePrevues
*/
		return sb.toString();
	}
	
}



