package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.qwefgh90.io.jsearch.extractor.TikaTextExtractor;


public class TikaTest {

	public static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TikaTest.class);
	
	@Test
	public void extract() throws IOException, SAXException, TikaException
	{
		TikaTextExtractor tika = new TikaTextExtractor();
		File f = new File(getClass().getResource("/1234.ppt").getFile());
		String text = TikaTextExtractor.extract(f);
		assertTrue(text.length() > 100);
		LOG.debug(String.valueOf(text.length()));
		
		tika = new TikaTextExtractor();
		f = new File(getClass().getResource("/1234.doc").getFile());
		text = TikaTextExtractor.extract(f);
		assertTrue(text.length() > 100);
		LOG.debug(String.valueOf(text.length()));
		
		tika = new TikaTextExtractor();
		f = new File(getClass().getResource("/1234.xlsx").getFile());
		 text = TikaTextExtractor.extract(f);
		assertTrue(text.length() > 100);
		LOG.debug(text.length()+"");
		
		LOG.info("[티카 엔진 테스트 성공!]");
	}
}
