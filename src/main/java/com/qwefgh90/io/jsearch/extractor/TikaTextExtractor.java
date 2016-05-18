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
package com.qwefgh90.io.jsearch.extractor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.sun.swing.internal.plaf.metal.resources.metal;

/**
 * Office Extractor with Tika which is open source <br>
 * <br>
 * <strong>process</strong>
 * <ol>
 * <li>call extract()</li>
 * <li>call getText()</li>
 * </ol>
 * 
 * @author Chang
 */
public class TikaTextExtractor {
	protected static Logger log = LoggerFactory.getLogger(TikaTextExtractor.class);
	private static final AutoDetectParser parser = new AutoDetectParser(); // thread-safe
																			// http://lucene.472066.n3.nabble.com/Thread-Safety-td646195.html

	/**
	 * 
	 * @param file
	 *            - office file
	 * @return boolean - success
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 * @throws SAXException
	 * @throws TikaException
	 */
	public static final String extract(File file) throws IOException, SAXException, TikaException {
		ContentHandler handler = new BodyContentHandler(); // only 1-run 1-use,
															// fast gone object
		Metadata metadata = new Metadata(); // only 1-run 1-use, fast gone
											// object
		try (InputStream input = new FileInputStream(file)) {
			TikaTextExtractor.parser.parse(input, handler, metadata);
		}
		return handler.toString();
	}

}
