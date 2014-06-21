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
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.executor.AbstractInternalExecutor;
import com.d2.osfad.executor.AbstractInternalExecutor.argumentsEnum;
import com.search.algorithm.QS;

/**
 * @author Chang
 *
 */
public class WorkerFindAllDoc implements Runnable {
	protected static Logger log = LoggerFactory.getLogger(WorkerFindDoc.class);
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null;						/* Executor's queue */
	private HashMap<argumentsEnum,Object> arguments = null;
	private AbstractInternalExecutor iexecutor = null;
	public WorkerFindAllDoc(AbstractInternalExecutor iexecutor){
		this.iexecutor = iexecutor;
	}
	@Override
	public void run(){
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
			jobQueue = iexecutor.getQueue();
		arguments = (HashMap<argumentsEnum,Object>)iexecutor.getArguments();
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
		}
		/**
		 * execute document functions through callback
		 */
		iexecutor.findKeywordFromOneDirectoryInternalCallback();			
		iexecutor.notifyJobFinish();
		return;
	}
}
