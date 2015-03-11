package com.github.cunvoas.mantis.report.mantis;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestQueryHelper {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetQryValueCaseColumn() {
		String caseQry = QueryHelper.getQryValueCaseColumn("priority");
		Assert.assertEquals("", "CASE MB.priority WHEN '20' THEN 'BASSE' WHEN '30' THEN 'NORMALE' WHEN '40' THEN 'HAUTE' WHEN '50' THEN 'URGENTE' ELSE 'NON DEFINI '||MB.priority END as Priorite,",  caseQry);
	}

}
