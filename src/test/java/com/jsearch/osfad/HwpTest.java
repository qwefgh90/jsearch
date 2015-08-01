package com.jsearch.osfad;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argo.hwp.HwpTextExtractor;
import com.jsearch.osfad.exception.AlreadyRunThreadsException;
import com.jsearch.osfad.executor.ExternalExcutor;
import com.jsearch.osfad.executor.IExternalExecutor;
import com.jsearch.osfad.executor.WorkerFunctions;
import com.jsearch.osfad.job.DocumentFile;
import com.jsearch.osfad.main.ICallBack;
import com.qwefgh90.io.jsearch.extractor.HwpTextExtractorWrapper;

public class HwpTest {
	protected static Logger LOG = LoggerFactory
			.getLogger(HwpTest.class);

	@Test
	public void hwplibtest() throws FileNotFoundException, IOException
	{
		HwpTextExtractorWrapper hwpEx = new HwpTextExtractorWrapper();
		hwpEx.extract(new File(getClass().getResource("/HTTP.hwp").getFile()));
		assertTrue(hwpEx.getText().length() > 0);
		LOG.debug(String.valueOf(hwpEx.getText().length()));
		LOG.info("[한글 엔진 테스트 성공!]");
	}

}

