package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argo.hwp.v5.HwpTextExtractorV5;
import com.qwefgh90.io.jsearch.extractor.HwpTextExtractorWrapper;

public class HwpTest {
	protected static Logger LOG = LoggerFactory
			.getLogger(HwpTest.class);

	@Test
	public void hwplibtest() throws FileNotFoundException, IOException
	{
		HwpTextExtractorWrapper hwpEx = new HwpTextExtractorWrapper();
		hwpEx.extract(new File(getClass().getResource("/HTTP.hwp").getFile()));
		StringWriter writer = new StringWriter();
		HwpTextExtractorV5.extractText(new File(getClass().getResource("/HTTP.hwp").getFile()), writer);
		assertTrue(hwpEx.getText().length() > 0);
		LOG.debug(String.valueOf(hwpEx.getText().length()));
		LOG.info("[한글 엔진 테스트 성공!]");
	}

}

