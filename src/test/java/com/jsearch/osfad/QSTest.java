package com.jsearch.osfad;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qwefgh90.io.jsearch.algorithm.QS;

public class QSTest {

	Logger LOG = LoggerFactory.getLogger(QSTest.class);
	
	@Test
	public void qsBooleanTest()
	{
		String target = "hello wro";
		String pt = " wr";
		QS qs = QS.compile(pt);
		LOG.debug(qs.isExist(target)+"");
		assertTrue(qs.isExist(target));
		LOG.info("[퀵서치 알고리즘 찾기 성공!]");
	}
	
	@Test
	public void qsListTest()
	{
		String target = "hello wrx wrx";
		String target2 = "hello wro";
		String pt = " wrx";
		QS qs = QS.compile(pt);
		LOG.debug(qs.findAll(target).size()+"");
		assertTrue(qs.findAll(target).size() == 2);
		LOG.debug(qs.findAll(target2).size()+"");
		assertTrue(qs.findAll(target2).size() == 0);
		LOG.info("[퀵서치 알고리즘 리스트 반환 성공!]");
	}
}
