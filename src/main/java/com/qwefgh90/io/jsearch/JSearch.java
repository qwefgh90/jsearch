package com.qwefgh90.io.jsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.qwefgh90.io.jsearch.algorithm.QS;
import com.qwefgh90.io.jsearch.extractor.HwpTextExtractorWrapper;
import com.qwefgh90.io.jsearch.extractor.PlainTextExtractor;
import com.qwefgh90.io.jsearch.extractor.TikaTextExtractor;

/**
 * JSearch supports various types of documents with open source engines. <br>
 * And this library contains 3 types of functions. extract...() and
 * isContainsKeyword...() and getFileList...() <br>
 * <br>
 * HWP, DOC, PPT, EXCEL, TEXT, PDF and UNKNOWN are supported.
 * 
 * @author cheochangwon
 */
public class JSearch {

	public static Logger LOG = LoggerFactory.getLogger(JSearch.class);

	/**
	 * extract string
	 * 
	 * @param filePath
	 *            - a file path where you want to extract string.
	 * @return String - a extracted string
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 * @throws ParseException
	 */
	public static String extractContentsFromFile(String filePath) throws IOException, ParseException {
		if (filePath == null)
			throw new NullPointerException("Please input file name.");

		File target = new File(filePath);
		return extractContentsFromFile(target);
	}

	public static class ParseException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			return "file is invalid " + "\n" + super.toString();
		}

	}

	/**
	 * extract string
	 * @param target - a file where you want to extract string.
	 * @return String - a extracted string
	 * @throws IOException - a problem of file. refer to a message.
	 * @throws ParseException 
	 */
	public static String extractContentsFromFile(File target) throws IOException, ParseException
	{
		if(target == null)
			throw new NullPointerException("Please input file name.");

		if(target.isFile() == false)
			throw new RuntimeException("The path which you input isn't File.");

		MediaType mime = FileExtension.getContentType(target, target.getName());
		String mimeString = mime.toString();
		if(mimeString.equals("application/x-hwp") || mimeString.equals("application/x-hwp-v5")){
			HwpTextExtractorWrapper ext = new HwpTextExtractorWrapper();
			ext.extract(target);
			return ext.getText();
		}else{
			try {
				return TikaTextExtractor.extract(target);
			} catch (SAXException | TikaException e) {
				LOG.error(e.toString());
				throw new ParseException();
			}
		}
	}

	/**
	 * get true or false about containing keyword.
	 * 
	 * @param filePath
	 *            - a filePath Document you want
	 * @param keyword
	 *            - a thing you want to find
	 * @return boolean - if having keyword, return true
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 * @throws ParseException
	 */
	public static boolean isContainsKeywordFromFile(String filePath, String keyword)
			throws IOException, ParseException {
		String text = extractContentsFromFile(filePath);
		QS qs = QS.compile(keyword);
		return qs.isExist(text);
	}

	/**
	 * get true or false about containing keyword.
	 * 
	 * @param file
	 *            - a file object Document you want
	 * @param keyword
	 *            - a thing you want to find
	 * @return boolean - if having keyword, return true
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 * @throws ParseException
	 */
	public static boolean isContainsKeywordFromFile(File file, String keyword) throws IOException, ParseException {
		String text = extractContentsFromFile(file);
		QS qs = QS.compile(keyword);
		return qs.isExist(text);
	}

	/**
	 * get a list of files which are containing keyword.
	 * 
	 * @param dirPath
	 *            - target directory
	 * @param keyword
	 *            - a keyword which you want to know.
	 * @return List&lt;File&gt; - a list of files which are containing keyword.
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 * @throws ParseException
	 */
	public static List<File> getFileListContainsKeywordFromDirectory(String dirPath, String keyword)
			throws IOException, ParseException {
		if (dirPath == null)
			throw new NullPointerException("Please input file name.");

		File target = new File(dirPath);
		if (target.isDirectory() == false)
			throw new RuntimeException("The path which you input isn't Directory.");

		File[] fileList = target.listFiles();
		int len = fileList.length;

		List<File> result = new ArrayList<File>();

		// if a file contains keyword, add to list
		for (int i = 0; i < len; i++) {
			if (fileList[i].isFile() && isContainsKeywordFromFile(fileList[i].getAbsolutePath(), keyword))
				result.add(fileList[i]);
		}

		return result;
	}

	/**
	 * get a list of files which are containing keyword.
	 * 
	 * @param dirPath
	 *            - target directory
	 * @param keyword
	 *            - a keyword which you want to know.
	 * @param recursive
	 *            - recursive mode.
	 * @return List&lt;File&gt; - a list of files which contain keyword.
	 * @throws IOException
	 *             - a problem of file. refer to a message.
	 * @throws ParseException
	 */
	public static List<File> getFileListContainsKeywordFromDirectory(String dirPath, String keyword, boolean recursive)
			throws IOException, ParseException {
		if (recursive == false)
			return getFileListContainsKeywordFromDirectory(dirPath, keyword);

		if (dirPath == null)
			throw new NullPointerException("Please input file name.");

		List<File> result = new ArrayList<File>(); // return files which contain
													// keyword.

		Queue<File> dirqueue = new LinkedList<File>(); // directory queue.
		dirqueue.add(new File(dirPath));

		File target;
		/*
		 * It is recursive. searching through files with Queue.
		 */
		while ((target = dirqueue.poll()) != null) {
			if (target.isDirectory() == false)
				throw new RuntimeException("The path which you input isn't Directory.");

			File[] filesInDirectory = target.listFiles();
			int len = filesInDirectory.length;

			for (int i = 0; i < len; i++) {
				if (filesInDirectory[i].isDirectory()) {
					dirqueue.add(filesInDirectory[i]); // add directory file to
														// queue.
				} else if (filesInDirectory[i].isFile()) {
					if (isContainsKeywordFromFile(filesInDirectory[i], keyword))
						result.add(filesInDirectory[i]); // add a success file
															// to list
				} else {
					// will not execute this block?
					LOG.info("unexpected file in java system.");
				}
			}
		}

		return result;
	}

}
