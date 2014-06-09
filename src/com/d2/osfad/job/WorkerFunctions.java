package com.d2.osfad.job;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.executor.AbstractInternalExecutor;
import com.d2.osfad.job.IJobItem.JOBID;

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
	private AbstractInternalExecutor iexecutor = null;
	private ConcurrentLinkedQueue<IJobItem> jobqueue = null; /* Executor's queue */

	public WorkerFunctions(AbstractInternalExecutor iexecutor) {
		this.iexecutor = iexecutor;
	}

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		// .info("start...");
		IJobItem tempItem = null;
		JOBID jobid;
		JobItemFile item=null;
		File[] list=null;
		int startoff=0;
		int endoff=0;
		jobqueue = iexecutor.getQueue();
		while ((tempItem = jobqueue.poll())
				!= null) {
			/**
			 * 1) retrieve item and downcasting
			 * 2) select job from jobid
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
				list = item.getFileList();
				for (int i = startoff; i < endoff; i++){
					log.debug("[ThreadId:"+
							Thread.currentThread().getId() + "] FileName : " + list[i].getName());
				}
				/*try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				break;
			}
		}
		iexecutor.notifyJobFinish();
		return;
	}
}
