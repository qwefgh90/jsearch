/**
 * 
 */
package com.d2.osfad.extractor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chang
 * 
 */
public class PlainTextExtractor {
	protected static Logger log = LoggerFactory
			.getLogger(PlainTextExtractor.class);

	enum UTF_BOM {
		UTF_8(0xEF, 0xBB, 0xBF), UTF_16_LITTLE(0xFF, 0xFE), UTF_16_BIG(0xFE,
				0xFF);

		public byte[] mark = null;

		UTF_BOM(int... arg) {
			mark = new byte[arg.length];
			for (int i = 0; i < arg.length; i++) {
				mark[i] = (byte) arg[i];
			}
		}

		public boolean compare(byte[] src) {
			if (src != null) {
				for (int i = 0; i < this.mark.length; i++) {
					if (src[i] != mark[i])
						return false;
				}
			} else {
				throw new NullPointerException();
			}
			return true;
		}
	}

	private final UniversalDetector detector = new UniversalDetector(null);

	/**
	 * 
	 * @param file
	 *            - target file
	 * @param writer
	 *            - text content
	 * @return whether to be success
	 */
	public final boolean extract(File file, StringWriter writer) {
		boolean success = false;
		BufferedInputStream bis = null;
		byte[] buffer = null;
		int buffer_len = 0;
		String detectedCharset = null;
		log.info("[TEXT]" + file.getName());
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		buffer_len = (int) file.length();
		buffer = new byte[buffer_len];
		try {
			bis.read(buffer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error(e1.toString());
		}
		try { /* BOM (Byte Order Mask) */
			if (UTF_BOM.UTF_8.compare(buffer)) {
				log.info("UTF_8_BOM");
				writer.append(new String(buffer, UTF_BOM.UTF_8.mark.length,
						buffer.length - UTF_BOM.UTF_8.mark.length, Charset
								.forName("UTF-8")));
			} else if (UTF_BOM.UTF_16_LITTLE.compare(buffer)) {
				log.info("UTF_16LE_BOM");
				writer.append(new String(buffer,
						UTF_BOM.UTF_16_LITTLE.mark.length, buffer.length
								- UTF_BOM.UTF_8.mark.length, Charset
								.forName("UTF-16LE")));
			} else if (UTF_BOM.UTF_16_BIG.compare(buffer)) {
				log.info("UTF_16BE_BOM");
				writer.append(new String(buffer,
						UTF_BOM.UTF_16_BIG.mark.length, buffer.length
								- UTF_BOM.UTF_8.mark.length, Charset
								.forName("UTF-16BE")));
			} else { /* NOT BOM (Byte Order Mask) */
				detector.handleData(buffer, 0, buffer_len);
				detector.dataEnd();
				detectedCharset = detector.getDetectedCharset();
				log.info("NOT_BOM " + detectedCharset);

				if (detectedCharset != null) { /* detected encoding */
					writer.append(new String(buffer, Charset
							.forName(detectedCharset)));
				} else { 						/* default encoding */
					detectedCharset = "EUC-KR";
					writer.append(new String(buffer, Charset
							.forName(detectedCharset)));
				}
			}
			success = true;
		} catch (IllegalCharsetNameException e) {
			log.error(e.toString());
		} catch (NullPointerException e) {
			log.error(e.toString());
		} catch (IllegalArgumentException e) {
			log.error(e.toString());
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			detector.reset();
		}

		try {
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}

		return success;
	}
}
