package com.jsearch.osfad.executor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsearch.osfad.executor.AbstractInternalExecutor.argumentsEnum;
import com.jsearch.osfad.job.DocumentFile;
import com.jsearch.osfad.job.IJobItem;
import com.jsearch.osfad.job.JobItemFile;
import com.jsearch.osfad.job.SFileFilter.EXTENSIONS;
import com.search.algorithm.QS;

public class WorkerFindFromOneDocument implements Runnable {
	protected static Logger log = LoggerFactory.getLogger(WorkerFindFromOneDocument.class);
	AbstractInternalExecutor iexecutor = null;
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null;						/* Executor's queue */
	private HashMap<argumentsEnum,Object> arguments = null;
	public WorkerFindFromOneDocument(AbstractInternalExecutor iexecutor)
	{
		super();
		this.iexecutor = iexecutor;
	}

	public void run()
	{
		final int maxThreadCount;
		final String filePath;
		final DocumentFile[] files;
		/*
		 * 1)create document file object
		 * 2)create job item
		 * 3)callback internal
		 */
		if(null == jobQueue)
			jobQueue = iexecutor.getQueue();
		jobQueue = iexecutor.getQueue();
		arguments = (HashMap<argumentsEnum,Object>)iexecutor.getArguments();
		maxThreadCount = (Integer)arguments.get(argumentsEnum.THREAD_COUNT);
		filePath = (String)arguments.get(argumentsEnum.FILE_PATH);
		files = DocumentFile.listDocFiles(filePath);
		QS.qs = QS.compile((String) arguments.get(argumentsEnum.KEYWORD));			/* static initialize */
		
		if(null != files && files.length == 1) {
			jobQueue.offer(new JobItemFile(files, 0, 1));
		} else {
			log.error("Not Find Error");
		}
		
		/**
		 * execute document functions through callback
		 */
		if(null != files)
			iexecutor.findKeywordFromOneDirectoryInternalCallback(files.length);	
		else
			iexecutor.findKeywordFromOneDirectoryInternalCallback(0);			
		iexecutor.notifyJobFinish(0);
		return;
	}

}
