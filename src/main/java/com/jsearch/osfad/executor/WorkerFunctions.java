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
package com.jsearch.osfad.executor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argo.hwp.HwpTextExtractor;
import com.jsearch.osfad.job.DocumentFile;
import com.jsearch.osfad.job.IJobItem;
import com.jsearch.osfad.job.JobItemFile;
import com.jsearch.osfad.job.IJobItem.JOBID;
import com.qwefgh90.io.jsearch.algorithm.QS;
import com.qwefgh90.io.jsearch.extractor.PlainTextExtractor;
import com.qwefgh90.io.jsearch.extractor.TikaTextExtractor;

/**
 * contains many functions find keyword, find meta data
 * 
 * @author Chang
 * 
 */
public class WorkerFunctions implements Runnable {
	protected static Logger log = LoggerFactory
			.getLogger(WorkerFunctions.class);
	private AInternalExecutor iexecutor = null;
	private ConcurrentLinkedQueue<IJobItem> jobqueue = null; /* Executor's queue */
	private final TikaTextExtractor tikaExtractor = new TikaTextExtractor();
	private final PlainTextExtractor textExtractor = new PlainTextExtractor();
	private int result = 0;
	public WorkerFunctions(AInternalExecutor iexecutor) {
		this.iexecutor = iexecutor;
	}

	public final void run()
	{
		// TODO Auto-generated method stub
		// .info("start...");
		int startoff = 0;
		int endoff = 0;
		IJobItem tempItem = null;
		JOBID jobid;
		JobItemFile item = null;
		DocumentFile[] fileList = null;
		List<Integer> keywordList = null;
		/**
		 * nice combination
		 */
		final StringWriter writer = new StringWriter(4096);
		final StringBuffer writer_bf = writer.getBuffer();
		jobqueue = iexecutor.getQueue();
		while ((tempItem = jobqueue.poll()) != null && iexecutor.getStopFlag() != true) {
			/**
			 * operation process
			 * 1) retrieve item and down casting
			 * 2) select job from job id
			 * 3) job processing :)
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
				for (int i = startoff; i < endoff && iexecutor.getStopFlag() != true; i++) {
					switch (fileList[i].extension) {
					case HWP: {
						log.info("[HWP] "+fileList[i].getName());
						try {

							if (HwpTextExtractor.extract(fileList[i], writer)) {
								keywordList = QS.qs.findAll(writer.toString());
								if (keywordList.size() > 0){
									log.info("[OK Found] "+
											fileList[i].getName() + ", " + keywordList.toString());
									iexecutor.notifyAddResult(fileList[i], keywordList);
								}
							}
						} catch (FileNotFoundException e) {
							log.error("Can't found Document Files");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.error(e.toString());
						} finally {
							result++;
						}
						break;
					}
					case DOC: {
						log.info("[DOC] "+fileList[i].getName());
						try{
							if(tikaExtractor.extract(fileList[i])){
								keywordList = QS.qs.findAll(tikaExtractor.getText());
								if (keywordList.size() > 0){
									log.info("[OK Found] "+
											fileList[i].getName() + ", " + keywordList.toString());
									iexecutor.notifyAddResult(fileList[i], keywordList);
								}
							}
						}catch(Exception e) {

						}finally {
							result++;
						}
						break;
					}
					case PPT: {
						log.info("[PPT] "+fileList[i].getName());
						try{
							if(tikaExtractor.extract(fileList[i])){
								keywordList = QS.qs.findAll(tikaExtractor.getText());
								if (keywordList.size() > 0){
									log.info("[OK Found] "+
											fileList[i].getName() + ", " + keywordList.toString());
									iexecutor.notifyAddResult(fileList[i], keywordList);
								}
							}
						}catch(Exception e) {

						}finally {
							result++;
						}

						break;
					}
					case EXCEL: {
						log.info("[EXCEL] "+fileList[i].getName());
						try{
							if(tikaExtractor.extract(fileList[i])){
								keywordList = QS.qs.findAll(tikaExtractor.getText());
								if (keywordList.size() > 0){
									log.info("[OK Found] "+
											fileList[i].getName() + ", " + keywordList.toString());
									iexecutor.notifyAddResult(fileList[i], keywordList);
								}
							}
						}catch(Exception e) {

						}finally {
							result++;
						}
						break;
					}
					case TEXT: {
						log.info("[TEXT] "+fileList[i].getName());
						try{
							if(textExtractor.extract(fileList[i])){
								keywordList = QS.qs.findAll(textExtractor.getText());
								if (keywordList.size() > 0){
									log.info("[OK Found] "+
											fileList[i].getName() + ", " + keywordList.toString());
									iexecutor.notifyAddResult(fileList[i], keywordList);
								}
							}
						}catch(Exception e) {

						}finally {
							result++;
						}
						break;
					}
					case PDF: {
						log.info("[PDF] "+fileList[i].getName());
						try{
							if(tikaExtractor.extract(fileList[i])){
								keywordList = QS.qs.findAll(tikaExtractor.getText());
								if (keywordList.size() > 0){
									log.info("[OK Found] "+
											fileList[i].getName() + ", " + keywordList.toString());
									iexecutor.notifyAddResult(fileList[i], keywordList);
								}
							}
						}catch(Exception e) {

						}finally {
							result++;
						}
						break;
					}
					default: {
						result++;
					}
					}

					writer_bf.setLength(0); /* buffer clear */
					/**
					 * Very Cool & not change buffer(because 0 smaller than
					 * original size) -->
					 * http://grepcode.com/file/repository.grepcode
					 * .com/java/root/
					 * jdk/openjdk/6-b14/java/lang/AbstractStringBuilder
					 * .java#AbstractStringBuilder.setLength%28int%29
					 * write.getBuffer()..delete(0, bf.length()); /* another
					 * method to clear buffer
					 */
				}
				
				/*
				 * try { Thread.sleep(1); } catch (InterruptedException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */
				break;
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.debug("IOException StringWriter");
		}

		iexecutor.notifyJobFinish(1);
		
		return;
	}
}
