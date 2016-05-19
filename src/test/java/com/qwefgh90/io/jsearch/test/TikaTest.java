package com.qwefgh90.io.jsearch.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.qwefgh90.io.jsearch.FileExtension;
import com.qwefgh90.io.jsearch.extractor.TikaTextExtractor;

public class TikaTest {

	public static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TikaTest.class);

	@Test
	public void extract() throws IOException, SAXException, TikaException {
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
		LOG.debug(text.length() + "");

		LOG.info("[티카 엔진 테스트 성공!]");
	}

	@Test
	public void mimeTest() throws URISyntaxException, IOException {
		Path resources = Paths.get(getClass().getResource("/").toURI());
		Files.walkFileTree(resources, new SimpleFileVisitor<Path>() {
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (attrs.isRegularFile()) {
					try {
						try(InputStream is = new BufferedInputStream(Files.newInputStream(file))){
						
						System.out.println(file.toAbsolutePath().toString());
						MediaType type = FileExtension.getContentType(is, file.getFileName().toString());
						System.out.println(type.toString());
						String output = TikaTextExtractor.extract(file.toFile());
						System.out.println("출력 : " +
													output.substring(0, output.length() > 50 ? 50 : output.length()));
						
						}
					} catch (Exception e) {
						System.out.println(ExceptionUtils.getStackTrace(e));
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

}
