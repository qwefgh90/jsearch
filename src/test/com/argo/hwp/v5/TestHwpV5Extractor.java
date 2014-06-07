package test.com.argo.hwp.v5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

import com.argo.hwp.v5.HwpTextExtractorV5;

public class TestHwpV5Extractor {
	public String extract(String path) throws FileNotFoundException,
			IOException {
		File file = new File(path);
		// System.out.println(file.getAbsolutePath());
		StringWriter writer = new StringWriter(4096);
		HwpTextExtractorV5.extractText(file, writer);
		return writer.toString();
	}

	/**
	 * 디버그.. 문자와 코드값 출력
	 * 
	 * @param t
	 * @return
	 */
	private String withCode(String t) {
		StringWriter writer = new StringWriter(4096);
		for (int ii = 0; ii < t.length(); ii++) {
			char ch = t.charAt(ii);
			if (ch == ' ' || ch == '\n')
				continue;
			writer.append(ch);
			if (ch >= 128) {
				writer.append("\t").append(String.format("0x%1$04x", (int) ch));
			}
			writer.append("\n");
		}
		return writer.toString();
	}

	private String extractIgnoreException(String path) {
		try {
			return extract(path);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Test
	public void testExtractText() throws IOException, ClassNotFoundException {
		String str = extract("resource/한국어문규정집.hwp");
		//유니코드로 반환됨을 확인하는 코드
		//System.out.println(str.toCharArray()[0] + Integer.toHexString(str.toCharArray()[0]));
		PrintWriter pw = new PrintWriter(new File("test/한국어문규정집.txt"));
		pw.write(str);
		pw.close();
		//System.out.println(extract("resource/한국어문규정집.hwp"));

		PrintWriter pw1 = new PrintWriter(new File("test/한글 특수문자표.txt"));
		pw1.write(extract("resource/한글 특수문자표.hwp"));
		pw1.close();
		
		PrintWriter pw2 = new PrintWriter(new File("test/최창원김철진_lab1.txt"));
		pw2.write(extract("resource/최창원김철진_lab1.hwp"));
		pw2.close();
		//System.out.println(extract("resource/한글 특수문자표.hwp"));
	}
}