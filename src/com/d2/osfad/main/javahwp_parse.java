/*
 * Copyright (C) 2014 Changwon CHOE and Bora KIM <qwefgh90@naver.com, lemiyon@naver.com>
 * 
 * This file is part of OSFAD.
 *
 * OSFAD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OSFAD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with OSFAD.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.d2.osfad.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.DocumentSelector;
import org.apache.tika.io.IOUtils;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.argo.hwp.HwpTextExtractor;
import com.d2.osfad.executor.ExecutorWorkerDocument;
import com.d2.osfad.executor.IExternalExecutor;
import com.d2.osfad.extractor.PlainTextExtractor;

public class javahwp_parse implements ICallBack {
	protected static Logger log = LoggerFactory
			.getLogger(HwpTextExtractor.class);
	static long start;

	public javahwp_parse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 파일 객체 생성 =

		

		ICallBack callback = new javahwp_parse();
		IExternalExecutor docexecutor = ExecutorWorkerDocument
				.getSingleInstance();
		start = System.nanoTime();
		docexecutor.findKeywordFromAllDirectories(
						"E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource",
						"최창원", callback);
		// docexecutor.findKeywordFromOneDirectory(new File("resource"), "",
		// callback);

		try {
			Thread.sleep(5000);
			docexecutor.shutdownExecutor();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("main end");

		/*
		 * try { tikaexample(); } catch (Exception e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
	}

	@Override
	public void callback() {
		// TODO Auto-generated method stub
		long end = System.nanoTime();
		System.out.println("실행 시간 : " + (end - start) / 1000000000.0);
		log.info("callback from Executor");
	}

	private static void plaintextexample(){
		PlainTextExtractor pte = new PlainTextExtractor();
		File f = new File("E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource\\unicode_big.txt");
		StringWriter writer = new StringWriter();
		pte.extract(f, writer);
		log.info(writer.toString());
		f = new File("E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource\\utf-8.txt");
		writer.getBuffer().setLength(0);
		pte.extract(f, writer);
		log.info(writer.toString());
		f = new File("E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource\\unicode.txt");
		writer.getBuffer().setLength(0);
		pte.extract(f, writer);
		log.info(writer.toString());
		f = new File("E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource\\euc-kr.txt");
		writer.getBuffer().setLength(0);
		pte.extract(f, writer);
		log.info(writer.toString());
		f = new File("E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource\\utf-8_sublime.txt");
		writer.getBuffer().setLength(0);
		pte.extract(f, writer);
		log.info(writer.toString());
	}
	
	static ImageSavingParser imageParser = null;

	private static void tikaexample() throws Exception {
		ParseContext context;
		context = new ParseContext();
		Metadata metadata = new Metadata();

		// Detector detector = new DefaultDetector();
		Parser parser = new AutoDetectParser();
		imageParser = new ImageSavingParser(parser);

		// context.set(DocumentSelector.class, new ImageDocumentSelector());
		// context.set(Parser.class, imageParser);
		// if (true) return;

		File f = new File("resource/1차아이디어발표PPT.pptx");

		TikaInputStream input = null;
		try {
			input = TikaInputStream.get(f);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringWriter textBuffer = new StringWriter();
		StringWriter textMainBuffer = new StringWriter();
		StringWriter htmlBuffer = new StringWriter(); // formated text
		StringWriter xmlBuffer = new StringWriter(); // xhtml

		ContentHandler handler = new TeeContentHandler(
				getTextContentHandler(textBuffer),
				getTextMainContentHandler(textMainBuffer),
				getHtmlHandler(htmlBuffer), getXmlContentHandler(xmlBuffer));

		parser.parse(input, handler, metadata, context);
		log.info(textBuffer.toString()
				+ "\n===============================\n\n\n");
		// log.info(textMainBuffer.toString()+"\n===============================\n\n\n");
		// log.info(htmlBuffer.toString()+"\n===============================\n\n\n");
		// log.info(xmlBuffer.toString()+"\n===============================\n\n\n");

	}

	private static ContentHandler getTextContentHandler(Writer writer) {
		return new BodyContentHandler(writer);
	}

	private static ContentHandler getTextMainContentHandler(Writer writer) {
		return new BoilerpipeContentHandler(writer);
	}

	private static ContentHandler getXmlContentHandler(Writer writer)
			throws TransformerConfigurationException {
		SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		TransformerHandler handler = factory.newTransformerHandler();
		handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
		handler.setResult(new StreamResult(writer));
		return handler;
	}

	private static ContentHandler getHtmlHandler(Writer writer)
			throws TransformerConfigurationException {
		SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		TransformerHandler handler = factory.newTransformerHandler();
		handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
		handler.setResult(new StreamResult(writer));
		return new ContentHandlerDecorator(handler) {
			@Override
			public void startElement(String uri, String localName, String name,
					Attributes atts) throws SAXException {
				if (XHTMLContentHandler.XHTML.equals(uri)) {
					uri = null;
				}
				if (!"head".equals(localName)) {
					if ("img".equals(localName)) {
						AttributesImpl newAttrs;
						if (atts instanceof AttributesImpl) {
							newAttrs = (AttributesImpl) atts;
						} else {
							newAttrs = new AttributesImpl(atts);
						}

						for (int i = 0; i < newAttrs.getLength(); i++) {
							if ("src".equals(newAttrs.getLocalName(i))) {
								String src = newAttrs.getValue(i);
								if (src.startsWith("embedded:")) {
									String filename = src.substring(src
											.indexOf(':') + 1);
									try {
										File img = imageParser
												.requestSave(filename);
										String newSrc = img.toURI().toString();
										newAttrs.setValue(i, newSrc);
									} catch (IOException e) {
										System.err
												.println("Error creating temp image file "
														+ filename);
										// The html viewer will show a broken
										// image too to alert them
									}
								}
							}
						}
						super.startElement(uri, localName, name, newAttrs);
					} else {
						super.startElement(uri, localName, name, atts);
					}
				}
			}

			@Override
			public void endElement(String uri, String localName, String name)
					throws SAXException {
				if (XHTMLContentHandler.XHTML.equals(uri)) {
					uri = null;
				}
				if (!"head".equals(localName)) {
					super.endElement(uri, localName, name);
				}
			}

			@Override
			public void startPrefixMapping(String prefix, String uri) {
			}

			@Override
			public void endPrefixMapping(String prefix) {
			}
		};
	}

	/**
	 * A {@link DocumentSelector} that accepts only images.
	 */
	private static class ImageDocumentSelector implements DocumentSelector {
		public boolean select(Metadata metadata) {
			String type = metadata.get(Metadata.CONTENT_TYPE);
			return type != null && type.startsWith("image/");
		}
	}

	/**
	 * A recursive parser that saves certain images into the temporary
	 * directory, and delegates everything else to another downstream parser.
	 */
	private static class ImageSavingParser extends AbstractParser {
		private Map<String, File> wanted = new HashMap<String, File>();
		private Parser downstreamParser;
		private File tmpDir;

		private ImageSavingParser(Parser downstreamParser) {
			this.downstreamParser = downstreamParser;

			try {
				File t = File.createTempFile("tika", ".test");
				tmpDir = t.getParentFile();
			} catch (IOException e) {
			}
		}

		public File requestSave(String embeddedName) throws IOException {
			String suffix = ".tika";

			int splitAt = embeddedName.lastIndexOf('.');
			if (splitAt > 0) {
				embeddedName.substring(splitAt);
			}

			File tmp = File.createTempFile("tika-embedded-", suffix);
			wanted.put(embeddedName, tmp);
			return tmp;
		}

		public Set<MediaType> getSupportedTypes(ParseContext context) {
			// Never used in an auto setup
			return null;
		}

		public void parse(InputStream stream, ContentHandler handler,
				Metadata metadata, ParseContext context) throws IOException,
				SAXException, TikaException {
			String name = metadata.get(Metadata.RESOURCE_NAME_KEY);
			if (name != null && wanted.containsKey(name)) {
				FileOutputStream out = new FileOutputStream(wanted.get(name));
				IOUtils.copy(stream, out);
				out.close();
			} else {
				if (downstreamParser != null) {
					downstreamParser.parse(stream, handler, metadata, context);
				}
			}
		}

	}

}
