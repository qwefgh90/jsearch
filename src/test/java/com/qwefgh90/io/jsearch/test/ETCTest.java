package com.qwefgh90.io.jsearch.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ETCTest {
	boolean isUselessTest = true;

	Logger LOG = LoggerFactory.getLogger(ETCTest.class);
	@Test
	public void fileTest() throws IOException
	{
		if(!isUselessTest){
			URL url = getClass().getResource("/HTTP.hwp");
			String f = url.getFile();
			LOG.info(f);
			File file = new File(f);
			LOG.info(file.getAbsolutePath());
			LOG.info(file.getCanonicalPath());
		}
	}
	@Test
	public void fileTest2() throws IOException
	{
		if(!isUselessTest){
			String f = "HTTP.HWP";
			LOG.info("HTTP.HWP");
			File file = new File(f);
			LOG.info(file.getAbsolutePath());
			LOG.info(file.getCanonicalPath());
		}

	}
}
