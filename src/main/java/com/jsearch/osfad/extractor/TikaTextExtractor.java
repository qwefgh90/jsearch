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
/**
 * 
 */
package com.jsearch.osfad.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @author Chang
 * 
 */
public class TikaTextExtractor {
	protected static Logger log = LoggerFactory
			.getLogger(TikaTextExtractor.class);
	private final Parser parser = new AutoDetectParser();
	private final StringWriter textWriter = new StringWriter();
	private final StringBuffer textBuffer = textWriter.getBuffer();
	private final ContentHandler handler = getTextContentHandler(textWriter);
	/**
	 * 
	 * @param file - office file
	 * @return success
	 */
	public final boolean extract(File file) {
		ParseContext context = new ParseContext();	//only 1-run 1-use, temporary object
		Metadata metadata = new Metadata();			//only 1-run 1-use, temporary object
		
		textBuffer.setLength(0);
		boolean success = false;
		TikaInputStream input = null;
		try {
			input = TikaInputStream.get(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		try {
			parser.parse(input, handler, metadata, context);

			success=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
		}
		// log.info(textWriter.toString());
		// log.info(textMainBuffer.toString());
		// log.info(htmlBuffer.toString());
		// log.info(xmlBuffer.toString());
		return success;
	}

	public final String getExtractText(){
		return textWriter.toString();
	}

	private static ContentHandler getTextContentHandler(Writer writer) {
		return new BodyContentHandler(writer);
	}
	/**
	 * A recursive parser that saves certain images into the temporary
	 * directory, and delegates everything else to another downstream parser.
	 */
}
