package io.github.qwefgh90.jsearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;

/**
 * This enum class represents various extensions and contains utility functions.
 * <br>
 * <br>
 * HWP, DOC, PPT, EXCEL, TEXT, PDF and UNKNOWN are supported.
 * 
 * @author cheochangwon
 *
 */
public enum FileExtension {
	HWP(".hwp"), DOC(".docx", ".doc"), PPT(".pptx", ".ppt"), EXCEL(".xls",
			".xlsx"), TEXT(".txt"), PDF(".pdf"), UNKNOWN("");
	public String[] extension = null;
	public int extension_count;

	FileExtension(String... str) {
		this.extension = str;
		this.extension_count = str.length;
	};

	/**
	 * Find enum object with extension string
	 * 
	 * @param fileName
	 *            extension string
	 * @return FileExtension instance
	 */
	public static FileExtension getFileFormatbyExtension(String fileName) {
		FileExtension[] exArray = FileExtension.values();
		int len = exArray.length;
		for (int i = 0; i < len; i++) {
			String[] strArray = exArray[i].extension;
			int lenOfString = strArray.length;
			for (int j = 0; j < lenOfString; j++) {
				if (fileName.toLowerCase().endsWith(strArray[j].toLowerCase())) {
					// get enum object
					return exArray[i];
				}
			}
		}
		// cant find matching extension
		return UNKNOWN;
	}

	/**
	 * Find enum object with extension string
	 * 
	 * @param fileName
	 *            extension string
	 * @return FileExtension instance
	 */
	public static FileExtension getFileFormatbyExtensionIgnorePostfix(String fileName) {
		FileExtension[] exArray = FileExtension.values();
		int len = exArray.length;
		for (int i = 0; i < len; i++) {
			String[] strArray = exArray[i].extension;
			int lenOfString = strArray.length;
			for (int j = 0; j < lenOfString; j++) {
				if (fileName.toLowerCase().contains(strArray[j].toLowerCase())) {
					// get enum object
					return exArray[i];
				}
			}
		}
		// cant find matching extension
		return UNKNOWN;
	}

	public static MediaType getContentType(File f, String fileName) throws IOException {
		MediaType mediaType;
		try (InputStream is = new BufferedInputStream(new FileInputStream(f))) {
			Metadata md = new Metadata();
			md.set(Metadata.RESOURCE_NAME_KEY, fileName);
			mediaType = MimeTypes.getDefaultMimeTypes().detect(is, md);
		}
		
		if(fileName.toLowerCase().endsWith(".hwp") && mediaType.toString().equals("application/x-tika-msoffice"))
			return new MediaType("application", "x-hwp-v5");
		
		return mediaType;
	}

	public static MediaType getContentType(InputStream is, String fileName) throws IOException {
		MediaType mediaType;
		Metadata md = new Metadata();
		md.set(Metadata.RESOURCE_NAME_KEY, fileName);
		mediaType = MimeTypes.getDefaultMimeTypes().detect(is, md);
		
		if(fileName.toLowerCase().endsWith(".hwp") && mediaType.toString().equals("application/x-tika-msoffice"))
			return new MediaType("application", "x-hwp-v5");
		
		return mediaType;
	}
}
