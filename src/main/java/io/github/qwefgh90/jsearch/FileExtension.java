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
@Deprecated
public class FileExtension{
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
