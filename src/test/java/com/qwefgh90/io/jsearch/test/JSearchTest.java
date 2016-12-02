package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.qwefgh90.jsearch.JSearch;

public class JSearchTest {

	public static Logger LOG = LoggerFactory.getLogger(JSearchTest.class);

	@Test
	public void extractTextTest() throws IOException, URISyntaxException
	{
		//HWP
		String content = JSearch.extractContentsFromFile(new File(getClass().getResource("/hwp/HTTP.hwp").getFile()));
		assertThat(content, new StringContains("Protocol"));
		assertThat(content, new StringContains("패킷교환"));
		assertThat(content, new StringContains("WAP와"));
		
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/hwp/malware_info.hwp").getFile()));
		assertThat(content, new StringContains("Rootkit"));
		assertThat(content, new StringContains("백도어"));
		
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/hwp/VHD.hwp").getFile()));
		assertThat(content, new StringContains("diskpart"));
		assertThat(content, new StringContains("관리자계정"));
		
		//PPT
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/ppt/1234.ppt").getFile()));
		assertThat(content, new StringContains("모바일 기기"));
		assertThat(content, new StringContains("Tablet PC"));
		
		content = JSearch.extractContentsFromFile(Paths.get(getClass().getResource("/ppt/템플릿.pptx").toURI()).toFile());
		assertThat(content, new StringContains("Insert Sub"));
		assertThat(content, new StringContains("investigation"));
		
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/xlsx/1234.xlsx").getFile()));
		assertThat(content, new StringContains("프로젝트 준비"));
		//assertThat(content, new StringContains("2013-10-13"));
		assertThat(content, new StringContains("13"));
		
		//TXT
		content = JSearch.extractContentsFromFile(new File(getClass().getResource("/text/1234_euc_kr.txt").getFile()));
		assertThat(content, new StringContains("직접"));
		assertThat(content, new StringContains("txt"));
		
		//DOC
		content = JSearch.extractContentsFromFile(getClass().getResource("/doc/1234.doc").getFile());
		assertThat(content, new StringContains("BTO"));
		assertThat(content, new StringContains("<$MSG_SEV>"));
		
		content = JSearch.extractContentsFromFile(getClass().getResource("/doc/template.docx").getFile());
		assertThat(content, new StringContains("Float over text"));
		assertThat(content, new StringContains("magnetic"));
		
		content = JSearch.extractContentsFromFile(Paths.get(getClass().getResource("/doc/네티한글가이드.doc").toURI()).toFile());
		assertThat(content, new StringContains("DefaultHttpResponse"));
		assertThat(content, new StringContains("webSocketLocation"));
		
		//PDF
		content = JSearch.extractContentsFromFile(getClass().getResource("/pdf/javascript.pdf").getFile());
		assertThat(content, new StringContains("NewHeart"));
		assertThat(content, new StringContains("vbscript:msgbox()"));
		
		content = JSearch.extractContentsFromFile(getClass().getResource("/pdf/javascript2._p_d_f_").getFile());
		assertThat(content, new StringContains("NewHeart"));
		assertThat(content, new StringContains("vbscript:msgbox()"));
		
		content = JSearch.extractContentsFromFile(getClass().getResource("/pdf/boot.pdf").getFile());
		assertThat(content, new StringContains("실습기기에서"));
		assertThat(content, new StringContains("ld-script – linker"));
		
		//XML
		content = JSearch.extractContentsFromFile(getClass().getResource("/xml/web.xml").getFile());
		LOG.trace(content.replaceAll(" +", " ").replaceAll("\n", ""));
		assertThat(content, new StringContains("org.apache.catalina.servlets.DefaultServlet"));
		
		//ETC
		content = JSearch.extractContentsFromFile(getClass().getResource("/etc/catalina.properties").getFile());
		assertThat(content, new StringContains("package.access"));
		
		content = JSearch.extractContentsFromFile(getClass().getResource("/etc/cdk.mp3").getFile());
		assertThat(content, new StringContains("ccMixter"));
		
	}

	@Test
	public void findKeywordWithDirectoryNoRecursiveTest() throws IOException
	{
		int size = JSearch.getFileListContainsKeywordFromDirectory(getClass().getResource("/").getFile(), "음성, 화상, 데이타 등과 같이", false).size();
		assertTrue("file count is " + size, size == 0);
	}

	@Test
	public void findKeywordWithDirectoryRecursiveTest() throws IOException
	{
		List<File> list = JSearch.getFileListContainsKeywordFromDirectory(
				getClass().getResource("/").getFile()
				, "org.apache.catalina.servlets.DefaultServlet" // /conf/web.xml
				, true);
		assertTrue("file count is " + list.size(), list.size() > 0);
	}
}

