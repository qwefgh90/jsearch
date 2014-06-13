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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argo.hwp.HwpTextExtractor;
import com.d2.osfad.executor.AbstractInternalExecutor;
import com.d2.osfad.job.IJobItem.JOBID;
import com.search.algorithm.QS;

/**
 * contains many functions find keyword, find meta data
 * 
 * @author Chang
 * 
 */
public class WorkerFunctions implements Runnable {
	protected static Logger log = LoggerFactory
			.getLogger(WorkerFunctions.class);
	public static boolean stopFunctionsFlag = false;
	public static QS qsinstance = null;
	private AbstractInternalExecutor iexecutor = null;
	private ConcurrentLinkedQueue<IJobItem> jobqueue = null; /* Executor's queue */

	public WorkerFunctions(AbstractInternalExecutor iexecutor) {
		this.iexecutor = iexecutor;
	}

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		// .info("start...");
		int startoff = 0;
		int endoff = 0;
		IJobItem tempItem = null;
		JOBID jobid;
		JobItemFile item = null;
		DocumentFile[] fileList = null;
		List<Integer> keywordList = null;
		final StringWriter write = new StringWriter(4096);
		jobqueue = iexecutor.getQueue();
		while ((tempItem = jobqueue.poll()) != null) {
			/**
			 * 1) retrieve item and downcasting 2) select job from jobid 3) job
			 * processing :)
			 */
			jobid = tempItem.getJobId();
			switch (jobid) {
			case FINDKEYWORD:
				/**
				 * 1)a processing of find keyword
				 */
				item = (JobItemFile) tempItem;
				startoff = item.startOff;
				endoff = item.endOff;
				fileList = (DocumentFile[]) item.getFileList();
				for (int i = startoff; i < endoff; i++) {
					// log.info("[ThreadId:"+
					// Thread.currentThread().getId() + "] FileName : " +
					// fileList[i].getName());
					switch (fileList[i].extension) {
					case HWP: {
						try {
							if (HwpTextExtractor.extract(fileList[i], write)) {
								keywordList = QS.qs.findAll(write.toString());
								if (keywordList.size() > 0)
									log.info("Found Keyword : "
											+ keywordList.toString() + ", "
											+ fileList[i].getName());
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							log.error("Can't found Files in extract");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.debug("IOException in extract");
						} finally {
						}
					}
					case DOC:{
						log.info("DOC Files");
						
					}
					case PPT:{
						log.info("PPT Files");
						
					}
					case EXCEL:{
						log.info("EXCEL Files");
						
					}
					default: {

					}
					}

					write.getBuffer().setLength(0); /* buffer clear */
					// write.getBuffer()..delete(0, bf.length()); /* another
					// method to clear buffer */

				}
				/*
				 * try { Thread.sleep(1); } catch (InterruptedException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */
				break;
			}
		}
		try {
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.debug("IOException StringWriter");
		}
		iexecutor.notifyJobFinish();
		return;
	}
}
