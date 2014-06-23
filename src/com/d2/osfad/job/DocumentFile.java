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
package com.d2.osfad.job;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystem;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.job.SFileFilter.EXTENSIONS;

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
	private int i = 0;
	private int j = 0;
	private EXTENSIONS[] extensions = EXTENSIONS.values();
	private int numOfExtension = EXTENSIONS.values().length;

	/**
	 * target - file name check files extension
	 */
	public EXTENSIONS accept(String target) {
		for (i = 0; i < numOfExtension; i++) {
			for (j = 0; j < extensions[i].extension_count; j++) {
				if (target.endsWith(extensions[i].extension[j])) {
					return extensions[i];
				}
			}
		}
		return null;
	}
}
