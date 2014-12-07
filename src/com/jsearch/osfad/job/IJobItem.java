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
package com.jsearch.osfad.job;

/**
 * @author Chang items to be stored in jobQueue contains file, jobid
 */
public interface IJobItem {
	enum JOBID {
		FINDKEYWORD("JobItemFile");

		private String jobid=null;
		JOBID(String arg) {
				this.jobid = arg ;
		}

		String getJobIdString() {
			return jobid;
		}
	}

	/**
	 * get job id
	 * 
	 * @return
	 */
	public JOBID getJobId();
}
