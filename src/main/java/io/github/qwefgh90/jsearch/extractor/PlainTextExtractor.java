/**
 * 
 */
package io.github.qwefgh90.jsearch.extractor;

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
 * Plain Text Extractor <br>
 * <br>
 * <strong>process</strong>
 * <ol>
 * <li>call extract()</li>
 * <li>call getText()</li>
 * </ol>
 * 
 * @author Chang
 */
public class PlainTextExtractor {
	protected static Logger log = LoggerFactory.getLogger(PlainTextExtractor.class);

	enum UTF_BOM {
		UTF_8(0xEF, 0xBB, 0xBF), UTF_16_LITTLE(0xFF, 0xFE), UTF_16_BIG(0xFE, 0xFF);

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

	/**
	 * 
	 * @param file
	 *            - target file
	 * @return whether to be success
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 */
	public static final String extract(File file) throws IOException {
		UniversalDetector detector = new UniversalDetector(null);
		StringWriter writer = new StringWriter();
		StringBuffer wbuffer = writer.getBuffer();

		byte[] buffer = null;
		int buffer_len = 0;
		String detectedCharset = null;
		wbuffer.setLength(0);
		// log.info("[TEXT]" + file.getName());
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			buffer_len = (int) file.length();
			buffer = new byte[buffer_len];
			bis.read(buffer);
			try { /* BOM (Byte Order Mask) */
				if (UTF_BOM.UTF_8.compare(buffer)) {
					log.debug("UTF_8_BOM");
					writer.append(new String(buffer, UTF_BOM.UTF_8.mark.length,
							buffer.length - UTF_BOM.UTF_8.mark.length, Charset.forName("UTF-8")));
				} else if (UTF_BOM.UTF_16_LITTLE.compare(buffer)) {
					log.debug("UTF_16LE_BOM");
					writer.append(new String(buffer, UTF_BOM.UTF_16_LITTLE.mark.length,
							buffer.length - UTF_BOM.UTF_8.mark.length, Charset.forName("UTF-16LE")));
				} else if (UTF_BOM.UTF_16_BIG.compare(buffer)) {
					log.debug("UTF_16BE_BOM");
					writer.append(new String(buffer, UTF_BOM.UTF_16_BIG.mark.length,
							buffer.length - UTF_BOM.UTF_8.mark.length, Charset.forName("UTF-16BE")));
				} else { /* NOT BOM (Byte Order Mask) */
					detector.handleData(buffer, 0, buffer_len);
					detector.dataEnd();
					detectedCharset = detector.getDetectedCharset();
					log.debug("NOT_BOM " + detectedCharset);

					if (detectedCharset != null) { /* detected encoding */
						writer.append(new String(buffer, Charset.forName(detectedCharset)));
					} else { /* default encoding */
						detectedCharset = "UTF-8";
						writer.append(new String(buffer, Charset.forName(detectedCharset)));
					}
				}
			} finally {
				detector.reset();
			}
		}

		return writer.toString();
	}
}
