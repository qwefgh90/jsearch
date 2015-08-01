package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qwefgh90.io.jsearch.JSearch;

public class JSearchTest {

	public static Logger LOG = LoggerFactory.getLogger(JSearchTest.class);

	@Test
	public void extractTextTest() throws IOException
	{
		String content = JSearch.extractContentsFromFile(new File(getClass().getResource("/HTTP.hwp").getFile()));
		LOG.debug(content.length()+"");
		assertTrue(content.length()>0);
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/1234.ppt").getFile()));
		LOG.debug(content.length()+"");
		assertTrue(content.length()>0);
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/1234.xlsx").getFile()));
		LOG.debug(content.length()+"");
		assertTrue(content.length()>0);
		content = JSearch.extractContentsFromFile(getClass().getResource("/1234.doc").getFile());
		LOG.debug(content.length()+"");
		assertTrue(content.length()>0);
		content = JSearch.extractContentsFromFile(getClass().getResource("/1234.txt").getFile());
		LOG.debug(content.length()+"");
		assertTrue(content.length()>0);
		LOG.info("[JSearch 텍스트 추출 성공!]");
	}

	@Test
	public void findTextTest() throws IOException
	{
		File hwp = new File(getClass().getResource("/HTTP.hwp").getFile());
		File ppt = new File(getClass().getResource("/1234.ppt").getFile());
		File doc = new File(getClass().getResource("/1234.doc").getFile());
		File txt = new File(getClass().getResource("/1234.txt").getFile());
		assertTrue(JSearch.isContainsKeywordFromFile(hwp, "음성, 화상, 데이타 등과 같이"));
		assertTrue(JSearch.isContainsKeywordFromFile(ppt, "Samsung, Sony-Ericsson, Motorola"));
		assertTrue(JSearch.isContainsKeywordFromFile(getClass().getResource("/1234.xlsx").getFile(), "프"));
		assertTrue(JSearch.isContainsKeywordFromFile(doc, "Add/Modify Logfile, Add"));
		assertTrue(JSearch.isContainsKeywordFromFile(txt, "한 텍스트"));
		LOG.info("[JSearch 텍스트 검색 성공!]");
	}

	@Test
	public void findKeywordWithDirectoryTest() throws IOException
	{
		int size = JSearch.getFileListContainsKeywordFromDirectory(getClass().getResource("/").getFile(), "음성, 화상, 데이타 등과 같이").size();
		LOG.info("[디렉토리 검색 중 1]");
		int size2 = JSearch.getFileListContainsKeywordFromDirectory(getClass().getResource("/").getFile(), "음성, 화상, 데이타 등과 같이", false).size();
		LOG.info("[디렉토리 검색 중 2]");
		LOG.debug(String.valueOf(size));
		assertTrue(size>0);
		assertTrue(size == size2);
		LOG.info("[디렉토리 검색 성공!]");
	}

	@Test
	public void findKeywordWithDirectoryRecursiveTest() throws IOException
	{
		List<File> list = JSearch.getFileListContainsKeywordFromDirectory(
				getClass().getResource("/").getFile()
				, "구현이 필요없으며 잘 작성된 정의파일을 통해 하위 호환성을 확보할 수 있으며 Java, C++, Phython등 대부분의 언어에서 사용이 가능하다"
				, true);
		LOG.info(list.toString());
		assertTrue(list.size() > 0);
		LOG.info("[재귀적 디렉토리 검색 성공!]");
	}
//	Encoder/Decoder 구현이 필요없으며 잘 작성된 정의파일을 통해 하위 호환성을 확보할 수 있으며 Java, C++, Phython등 대부분의 언어에서 사용이 가능하다
}

