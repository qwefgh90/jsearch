package io.github.qwefgh90.jsearch.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import com.argo.hwp.HwpTextExtractor;

/**
 * Wrapper class of JAVA-HWP which is open source
 * <br><br>
 * <strong>process</strong>
 * <ol>
 * <li>
 * call extract()
 * </li>
 * <li>
 * call getText()
 * </li>
 * </ol>
 * @author cheochangwon
 *
 */
public class HwpTextExtractorWrapper {
	
	StringWriter writer = new StringWriter();
	StringBuffer buffer = writer.getBuffer();
	public final boolean extract(File source) throws FileNotFoundException, IOException {
		buffer.setLength(0);
		return HwpTextExtractor.extract(source, writer);
	}

	public final String getText(){
		return writer.toString();
	}
}
