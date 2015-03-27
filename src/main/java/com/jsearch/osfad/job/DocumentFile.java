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
package com.jsearch.osfad.job;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsearch.osfad.job.SFileFilter.EXTENSIONS;

/**
 * @author Chang for using EXTENSIONS variable, make a new File Class
 */
public class DocumentFile extends File {
	public static final Logger log = LoggerFactory
			.getLogger(DocumentFile.class);
	public static final StringBuffer sb = new StringBuffer(255);
	public EXTENSIONS extension = null;				/* currunt file's extension */

	/**
	 * 
	 * @param pathname - full file path
	 * @param extension - file extension
	 */
	public DocumentFile(String pathname, EXTENSIONS extension) {
		super(pathname);
		this.extension = extension;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param pathname - full file path
	 */
	public DocumentFile(String pathname) {
		super(pathname);
	}

	/**
	 * Make a DocumentFile instance using a extension filter
	 */
	public DocumentFile[] listDocFiles() {
		final String ss[] = list(); //Error occur when request for wrong Path
		final String parentPath = getAbsolutePath()+File.separator;
		final int parentPath_length = parentPath.length();
		if (ss == null)
			return null;
		ArrayList<DocumentFile> files = new ArrayList<DocumentFile>();
		EXTENSIONS temp_extension;
		sb.append(parentPath);					/* set parent path */
		for (String s : ss){
			if ((temp_extension = accept(s)) != null) {
				synchronized (this) {				/*I know now single thread, so it is invalid */
					sb.setLength(parentPath_length);
					//sb.append(parentPath);
					sb.append(s);
					files.add(new DocumentFile(sb.toString(), temp_extension));
				}
			}
		}
		sb.setLength(0);
		return files.toArray(new DocumentFile[files.size()]);
	}

	/**
	 * Make a DocumentFile instance using custom filter
	 * maybe dictionary filter
	 */
	public DocumentFile[] listDocFiles(FileFilter filter) {
		DocumentFile tempfile = null;
		final String ss[] = list();
		final String parentPath = getAbsolutePath()+File.separator;
		final int parentPath_length = parentPath.length();
		if (ss == null)
			return null;
		ArrayList<DocumentFile> files = new ArrayList<DocumentFile>();
		sb.append(parentPath);				/* set parent path */
		for (String s : ss) {
			synchronized (this) {					/*I know now single thread, so it is invalid */
				sb.setLength(parentPath_length);
				sb.append(s);
				tempfile = new DocumentFile(sb.toString());
			}
			//
			if ((filter == null) || filter.accept(tempfile)) {
				files.add(tempfile);
			}
		}
		sb.setLength(0);
		return files.toArray(new DocumentFile[files.size()]);
	}

	
	/**
	 * File Extension Filter
	 */
	private static int i = 0;
	private static int j = 0;
	private static EXTENSIONS[] extensions = EXTENSIONS.values();
	private static int numOfExtension = EXTENSIONS.values().length;

	/**
	 * target - file name check files extension
	 */
	public static EXTENSIONS accept(String target) {
		for (i = 0; i < numOfExtension; i++) {
			for (j = 0; j < extensions[i].extension_count; j++) {
				if (target.endsWith(extensions[i].extension[j])) {
					return extensions[i];
				}
			}
		}
		return null;
	}
	
	public static DocumentFile[] listDocFiles(List<String> fileList) {
		if(null == fileList) {
			return new DocumentFile[]{};
		}
		final String ss[] = (String[])fileList.toArray(); //Error occur when request for wrong Path
		ArrayList<DocumentFile> files = new ArrayList<DocumentFile>();
		EXTENSIONS temp_extension;
		for (String s : ss){
			if ((temp_extension = accept(s)) != null) {
				files.add(new DocumentFile(s, temp_extension));
			}
		}
		return files.toArray(new DocumentFile[files.size()]);
	}
	
	public static final DocumentFile[] listDocFiles(String path) {
		if(null == path) {
			return new DocumentFile[]{};
		}
		ArrayList<DocumentFile> files = new ArrayList<DocumentFile>();
		EXTENSIONS temp_extension;
		if ((temp_extension = accept(path)) != null) {
			files.add(new DocumentFile(path, temp_extension));
		}
		return files.toArray(new DocumentFile[files.size()]);
	}
	
	
	private static final ArrayList<DocumentFile> listOfDocumentArray = new ArrayList<DocumentFile>();

	/**
	 * Find document files from all children
	 * 
	 * @param directory
	 *            This is parent directory
	 * @return File array instance
	 */
	public static final DocumentFile[] listDocFilesFromRecursive(String directoryPath) {
		DocumentFile[] documentlist = null;
		DocumentFile[] directoryList = null;
		DocumentFile[] result = null;
		DocumentFile directory = null;
		if(null != directoryPath)
			directory = new DocumentFile(directoryPath); 
		else
			return null;
		documentlist = directory.listDocFiles();		/* 	Search Document Files	*/
//		log.error("documentlist count number : "+documentlist.length);
//		for (DocumentFile f : documentlist){
//			log.error("list : "+f.toString());
//		}
		int documentlistLength = documentlist.length;
		int directoryListLength = 0;
		try {
			for (int j = 0; j < documentlistLength; j++) {
				listOfDocumentArray.add(documentlist[j]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found documents in directory");
		}

		try {
			directoryList = directory.listDocFiles(SFileFilter.directoryfilter);		/* 	Search Document Files using dictionary filter	*/
			directoryListLength = directoryList.length;
			for (int i = 0; i < directoryListLength; i++) {
				addDocumentFromAllDirectory((DocumentFile)directoryList[i]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found directories");
		}
		/**
		 * toArray require parameter if no parameter, runtime error occurs
		 */
		result = new DocumentFile[listOfDocumentArray.size()];
		listOfDocumentArray.toArray(result);
		clearArrayList(); // clear Arraylist
		return result;
	}

	private static final void addDocumentFromAllDirectory(DocumentFile directory) {
		DocumentFile[] documentlist = null;
		DocumentFile[] directoryList = null;
		documentlist = directory.listDocFiles();				/* 	Search Document Files	*/
		try {
			for (int j = 0; j < documentlist.length; j++) {
				listOfDocumentArray.add(documentlist[j]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found documents in directory");
		}
		// listOfDocumentArray.add(documentlist);
		try {
			directoryList = directory.listDocFiles(SFileFilter.directoryfilter);		/* 	Search Document Files using dictionary filter	*/
			for (int i = 0; i < directoryList.length; i++) {
				addDocumentFromAllDirectory(directoryList[i]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found directories");
		}
	}

	public static final void clearArrayList() {
		listOfDocumentArray.clear();
		log.debug("clear Files ArrayList");
	}
}
