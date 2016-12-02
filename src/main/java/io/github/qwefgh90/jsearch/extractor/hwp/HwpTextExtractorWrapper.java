package io.github.qwefgh90.jsearch.extractor.hwp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;


/**
 * This software has been developed with reference to
 * the HWP file format open specification by Hancom, Inc.
 * http://www.hancom.co.kr/userofficedata.userofficedataList.do?menuFlag=3
 * 한글과컴퓨터의 한/글 문서 파일(.hwp) 공개 문서를 참고하여 개발하였습니다.
 * @author qwefgh90
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
