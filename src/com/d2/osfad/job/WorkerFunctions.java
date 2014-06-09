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
		int startoff=0;
		int endoff=0;
		IJobItem tempItem = null;
		JOBID jobid;
		JobItemFile item=null;
		File[] fileList=null;
		List<Integer> keywordList=null;
		final StringWriter write = new StringWriter(4096);
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
				fileList = item.getFileList();
				for (int i = startoff; i < endoff; i++){
//					log.info("[ThreadId:"+
//							Thread.currentThread().getId() + "] FileName : " + fileList[i].getName());
					try {
						if(HwpTextExtractor.extract(fileList[i], write))
						{
							keywordList = QS.qs.findAll(write.toString());
							if(keywordList.size()>0)
								log.info("Found Keyword : "+keywordList.toString() +", " + fileList[i].getName());
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						log.debug("Can't found Files in extract");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.debug("IOException in extract");
					}finally{
						write.getBuffer().setLength(0);							/* buffer clear */
//						write.getBuffer()..delete(0, bf.length());				/* another method to clear buffer */
					}
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
