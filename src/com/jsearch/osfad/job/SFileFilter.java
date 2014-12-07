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
//
//
//
package com.jsearch.osfad.job;

//Goal : Make Exception Handler for request for wrong path

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFileFilter {
	public static final Logger log = LoggerFactory.getLogger(SFileFilter.class);

	public static enum EXTENSIONS {
		HWP(".hwp"), DOC(".docx"), PPT(".pptx", ".ppt"), EXCEL(".xls"), TEXT(".txt"), PDF(".pdf");
		public String[] extension = null;
		public int extension_count;

		EXTENSIONS(String... str) {
			this.extension = str;
			this.extension_count = str.length;
		};
	}

	public static final FileFilter directoryfilter = new FileFilter() {

		@Override
		public boolean accept(File arg0) {
			// TODO Auto-generated method stub
			return arg0.isDirectory();
		}
	};

}
