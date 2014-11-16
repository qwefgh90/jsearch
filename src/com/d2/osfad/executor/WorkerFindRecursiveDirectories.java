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
package com.d2.osfad.executor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.exception.EmptyDirectoryException;
import com.d2.osfad.executor.AbstractInternalExecutor.argumentsEnum;
import com.d2.osfad.job.DocumentFile;
import com.d2.osfad.job.IJobItem;
import com.d2.osfad.job.JobItemFile;
import com.d2.osfad.job.SFileFilter;
import com.search.algorithm.QS;


/**
 * 
 * @author changchang
 * Contain worker thread control function 
 * This operates on thread
 */
public class WorkerFindRecursiveDirectories implements Runnable{
	protected static Logger log = LoggerFactory.getLogger(WorkerFindFromOneDirectory.class);
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null;						/* Executor's queue */
	private HashMap<argumentsEnum,Object> arguments = null;
	private AbstractInternalExecutor internalExecutor = null;
	public WorkerFindRecursiveDirectories(AbstractInternalExecutor internalExecutor)
	{
		this.internalExecutor = internalExecutor;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		final int maxThreadCount;
		final int arrOffset;
		final int remainder;
		final File[] foundfiles;
		final DocumentFile directory;
		/**
		 * 1)initiate arguments & queue & directory
		 * 2)fetch fileList to match extensions
		 * 3)offer(provide) lists divided into queue
		 * 4)call findKeyword function
		 */
		if(jobQueue==null)															/* queue ready */
			jobQueue = internalExecutor.getQueue();
		arguments = (HashMap<argumentsEnum,Object>)internalExecutor.getArguments();
		maxThreadCount = (Integer)arguments.get(argumentsEnum.THREAD_COUNT);
		directory = (DocumentFile)arguments.get(argumentsEnum.DIRECTORY_PATH);
		QS.qs = QS.compile((String) arguments.get(argumentsEnum.KEYWORD));			/* static initialize */
		foundfiles = SFileFilter.getDocumentFromAllDirectory(directory);
		/**
		 * offer job into queue
		 */
		if (foundfiles != null){
			/**
			 * divide algorithm
			 * 0~15(count)
			 * thread Count :4
			 * 0~4, 4~8, 8~12 (less than greater number)
			 * --> 8~12 + remainder(3 = 15%4)
			 * 0~4, 4~8, 8~15
			 */
			arrOffset = foundfiles.length/maxThreadCount;		/* example len: 11, count: 2 ...0: 0~5, 1: 5~10*/
			remainder = foundfiles.length%maxThreadCount;		/* remainder */
			if (arrOffset != 0) {
				log.debug("arrOffset == " + arrOffset);
				log.debug("remainder == " + remainder);
				for (int i = 0; i < maxThreadCount; i++) {
					log.info((arrOffset * i) +" ~ "+ ((arrOffset * (i + 1)) + remainder));
					if (i == maxThreadCount - 1) {				/* if this is a final jobItem */
						jobQueue.offer(new JobItemFile(foundfiles, arrOffset
								* i, (arrOffset * (i + 1)) + remainder));
					} else {
						jobQueue.offer(new JobItemFile(foundfiles, arrOffset
								* i, arrOffset * (i + 1)));
					}
				}
				/**
				 * if threadCount > fileListCount
				 * execute below code
				 */
			} else {
				for (int i = 0 ; i < remainder ; i ++){
					jobQueue.offer(new JobItemFile(foundfiles, i,i+1));
				}
			}
		}else{
			log.error("Not Find Error");
			try {
				throw new EmptyDirectoryException();
			} catch (EmptyDirectoryException e) {
				e.printStackTrace();
			}
		}
		/**
		 * execute document functions through callback
		 */
		if(foundfiles!=null)
			internalExecutor.findKeywordFromOneDirectoryInternalCallback(foundfiles.length);	
		else
			internalExecutor.findKeywordFromOneDirectoryInternalCallback(0);
		internalExecutor.notifyJobFinish(0);
		return;
	}
}
