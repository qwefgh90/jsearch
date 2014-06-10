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

import com.search.algorithm.QS;

public class JobItemFile implements IJobItem {
	public final JOBID id = JOBID.FINDKEYWORD;
	public int startOff = 0;		/* equal or greater than startOff */
	public int endOff = 0;			/* less than end off */
	private File[] fileList=null;
	public JobItemFile(File[] fileList,int start, int end){
		this.fileList = fileList;
		startOff=start;
		endOff=end;
	}
	@Override
	public final JOBID getJobId() {
		// TODO Auto-generated method stub
		return id;
	}

	public final File[] getFileList() {
		// TODO Auto-generated method stub
		return fileList;
	}
}
