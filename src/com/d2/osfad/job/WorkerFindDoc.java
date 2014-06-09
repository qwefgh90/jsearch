package com.d2.osfad.job;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.executor.AbstractInternalExecutor;
import com.d2.osfad.executor.AbstractInternalExecutor.argumentsEnum;
import com.search.algorithm.QS;


/**
 * implementions of Callable Interface
 * Find Document Class (hwp, doc, ...)
 * @author Chang
 *
 */
public class WorkerFindDoc implements Runnable {
	public static boolean stopFinderFlag = false;
	protected static Logger log = LoggerFactory.getLogger(WorkerFindDoc.class);
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null;						/* Executor's queue */
	private HashMap<argumentsEnum,Object> arguments = null;
	private AbstractInternalExecutor iexecutor = null;
	public WorkerFindDoc(AbstractInternalExecutor iexecutor){
		this.iexecutor = iexecutor;
	}
	@Override
	public void run(){
		// TODO Auto-generated method stub
		final int maxThreadCount;
		final int arrOffset;
		final int remainder;
		final File[] foundfiles;
		final File directory;
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
		directory = (File)arguments.get(argumentsEnum.DIRECTORY_PATH);
		QS.qs = QS.compile((String) arguments.get(argumentsEnum.KEYWORD));			/* static initialize */
		foundfiles = directory.listFiles(SFileFilter.extensionfilter);
		/**
		 * offer job into queue
		 */
		if (foundfiles != null){
			/**
			 * divide algorithm
			 * 0~15(count)
			 * thread Count :4
			 * 0~4, 4~8, 8~12 (less than end number)
			 * --> 8~12 + remainder(3 = 15%4)
			 * 0~4, 4~8, 8~15
			 */
			arrOffset = foundfiles.length/maxThreadCount;		/* example len: 11, count: 2 ...0: 0~5, 1: 5~10*/
			remainder = foundfiles.length%maxThreadCount;		/* remainder */
			if (arrOffset != 0) {
				for (int i = 0; i < maxThreadCount; i++) {
					if (i == maxThreadCount - 1) {
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
