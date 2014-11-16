package com.d2.osfad.executor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.executor.AbstractInternalExecutor.argumentsEnum;
import com.d2.osfad.job.DocumentFile;
import com.d2.osfad.job.IJobItem;
import com.d2.osfad.job.JobItemFile;
import com.d2.osfad.job.SFileFilter.EXTENSIONS;
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

	@Override
	public void run()
	{
		final int maxThreadCount;
		final String filePath;
		final DocumentFile[] file;
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
		file = DocumentFile.listDocFiles(filePath);
		QS.qs = QS.compile((String) arguments.get(argumentsEnum.KEYWORD));			/* static initialize */
		
		if(null != file && file.length == 1) {
			jobQueue.offer(new JobItemFile(file, 0, 1));
		} else {
			log.error("Not Find Error");
		}
		
		/**
		 * execute document functions through callback
		 */
		if(null != file)
			iexecutor.findKeywordFromOneDirectoryInternalCallback(file.length);	
		else
			iexecutor.findKeywordFromOneDirectoryInternalCallback(0);			
		iexecutor.notifyJobFinish(0);
		return;
	}

}
