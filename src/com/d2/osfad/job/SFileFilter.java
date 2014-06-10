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
package com.d2.osfad.job;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFileFilter {
	public static final Logger log = LoggerFactory.getLogger(SFileFilter.class);
	public static final String[] extension = { ".hwp", ".docs" };
	public static final int exCount = extension.length;
	private static final ArrayList<File> listOfDocumentArray = new ArrayList<File>();
	/**
	 * extension filter (hwp, doc, ... add this!!!)
	 */
	public static final FilenameFilter extensionfilter = new FilenameFilter() {
		private int i = 0;

		@Override
		public boolean accept(File arg0, String arg1) {
			for (i = 0; i < exCount; i++) {
				if (arg1.endsWith(extension[i])) {
					return true;
				}
			}
			return false;
		}
	};
	public static final FileFilter directoryfilter = new FileFilter() {

		@Override
		public boolean accept(File arg0) {
			// TODO Auto-generated method stub
			log.debug(""+arg0.toString());
			return arg0.isDirectory();
		}
	};
/**
 * Find document files from all children
 * @param directory	This is parent directory
 * @return	File array instance
 */
	public static final File[] getDocumentFromAllDirectory(File directory) {
		File[] documentlist = null;
		File[] directoryList = null;
		File[] result = null;
		documentlist = directory.listFiles(extensionfilter);
		try {
			for (int j = 0; j < documentlist.length; j++) {
				listOfDocumentArray.add(documentlist[j]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found documents in directory");
		}

		try {
			directoryList = directory.listFiles(directoryfilter);
			for (int i = 0; i < directoryList.length; i++) {
				addDocumentFromAllDirectory(directoryList[i]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found directories");
		}
//		log.error("count number : "+listOfDocumentArray.size());
		/**
		 * toArray require parameter
		 * if no parameter, runtime error occurs
		 */
		result = new File[listOfDocumentArray.size()];
		listOfDocumentArray.toArray(result);
		clearArrayList();									//clear Arraylist
		return result;
	}

	private static final void addDocumentFromAllDirectory(File directory) {
		File[] documentlist = null;
		File[] directoryList = null;
		documentlist = directory.listFiles(extensionfilter);
		try {
			for (int j = 0; j < documentlist.length; j++) {
				listOfDocumentArray.add(documentlist[j]);
			}
		} catch (NullPointerException exception) {
			log.error("Not found documents in directory");
		}
		// listOfDocumentArray.add(documentlist);
		try {
			directoryList = directory.listFiles(directoryfilter);
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
