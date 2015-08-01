package com.qwefgh90.io.jsearch.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import com.argo.hwp.HwpTextExtractor;

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
