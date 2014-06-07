package com.d2.osfad.job;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.executor.AbstractInternalExecutor;
import com.d2.osfad.executor.AbstractInternalExecutor.argumentsEnum;


/**
 * implementions of Callable Interface
 * Find Document Class (hwp, doc, ...)
 * @author Chang
 *
 */
public class WorkerFindDoc implements Runnable {
	protected static Logger log = LoggerFactory.getLogger(WorkerFindDoc.class);
	public static boolean stopFinderFlag = false;
	
	public static final String[] extension = {".hwp",".docs"};
	public static final int exCount = extension.length;
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null;						/* Executor's queue */
	private File directory = null;
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
		/**
		 * 1)initiate arguments & queue & directory
		 * 2)fetch fileList to match extensions
		 * 3)offer(provide) lists divided into queue
		 * 4)call findKeyword function
		 */
		if(jobQueue==null)															/* queue ready */
			jobQueue = iexecutor.getQueue();
		arguments = (HashMap<argumentsEnum,Object>)iexecutor.getArguments();
		directory = (File)arguments.get(argumentsEnum.DIRECTORY_PATH);
		maxThreadCount = (Integer)arguments.get(argumentsEnum.THREAD_COUNT);
		foundfiles = directory.listFiles(extensionfilter);
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
		}
		/**
		 * execute document functions through callback
		 */
		iexecutor.findKeywordFromOneDirectoryInternalCallback();			
		iexecutor.notifyJobFinish();
		return;
	}
	public final File getPath() {
		return directory;
	}
	public final void setPath(File directory) {
		this.directory = directory;
	}
	
	/**
	 * extension filter (hwp, doc, ... add this!!!)
	 */
	public static final FilenameFilter extensionfilter = new FilenameFilter() {
		private int i = 0;
		@Override
		public boolean accept(File arg0, String arg1) {
			for (i = 0 ; i < exCount ; i++){
				if(arg1.endsWith(extension[i]))
				{
					return true;
				}
			}
			return false;
		}
	};
}
