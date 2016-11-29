package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qwefgh90.jsearch.algorithm.QS;

public class QSTest {

	Logger LOG = LoggerFactory.getLogger(QSTest.class);
	
	@Test
	public void qsBooleanTest()
	{
		String target = "hello wro";
		String pt = " wr";
		QS qs = QS.compile(pt);
		assertTrue(qs.isExist(target));
	}
	
	@Test
	public void qsListTest()
	{
		String target = "hello wrx wrx";
		String target2 = "hello wro";
		String pt = " wrx";
		QS qs = QS.compile(pt);
		assertTrue(qs.findAll(target).size() == 2);
		assertTrue(qs.findAll(target2).size() == 0);
	}
}
