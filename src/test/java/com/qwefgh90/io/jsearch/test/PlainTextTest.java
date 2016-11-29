package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qwefgh90.jsearch.extractor.PlainTextExtractor;

public class PlainTextTest {
	
	public static Logger LOG = LoggerFactory.getLogger(PlainTextTest.class);

	@Test
	public void extract() throws IOException
	{
		PlainTextExtractor e = new PlainTextExtractor();
		String textUtf8 = PlainTextExtractor.extract(new File(getClass().getResource("/text/1234_utf_8.txt").getFile()));
		String textUnicode = PlainTextExtractor.extract(new File(getClass().getResource("/text/1234_unicode.txt").getFile()));
		String textEuckr = PlainTextExtractor.extract(new File(getClass().getResource("/text/1234_euc_kr.txt").getFile()));
		assertThat(textUtf8, new StringContains("텍스트"));
		assertThat(textUtf8, new StringContains("txt"));
		assertThat(textUnicode, new StringContains("텍스트"));
		assertThat(textUnicode, new StringContains("txt"));
		assertThat(textEuckr, new StringContains("텍스트"));
		assertThat(textEuckr, new StringContains("txt"));
	}
	
}
