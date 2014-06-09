package com.d2.osfad.executor;

import java.io.File;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d2.osfad.job.IJobItem;
import com.d2.osfad.job.WorkerFindAllDoc;
import com.d2.osfad.job.WorkerFindDoc;
import com.d2.osfad.job.WorkerFunctions;
import com.d2.osfad.main.ICallBack;

public class ExecutorWorkerDocument extends AbstractInternalExecutor implements
		IExternalExecutor {
	public final static int SCALE_FACTOR = 2; /* a factor of count of threads */
	protected static Logger log = LoggerFactory
			.getLogger(ExecutorWorkerDocument.class);
	int threadCount; 						/* total count */
	private static ExecutorWorkerDocument singleton = null;
	/**
	 * jobQueue a list of files to be searched
	 */
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null; /*
											 * a queue to save job is shared
											 * with threads
											 */
	private Runnable workerFindDoc = null;
	private Runnable workerFunctions = null;
	private Runnable workerFindAllDoc = null;
	private ThreadPoolExecutor threadPool = null;
	private ExecutorService jobExecutor = null; /* Executor interface */

	private ExecutorWorkerDocument() {
		jobQueue = new ConcurrentLinkedQueue<IJobItem>();
		workerFindDoc = new WorkerFindDoc(this);
		workerFunctions = new WorkerFunctions(this);
		workerFindAllDoc = new WorkerFindAllDoc(this);
		/**
		 * instantiate a ThreadPool as jobExecutor max = processor * factor + 1
		 * ( plus one for light-weight thread)
		 */
		threadCount = Runtime.getRuntime().availableProcessors()
				* SCALE_FACTOR + 1; /* processor * factor + 1 */
		log.info("threadCount : " + threadCount);

		jobExecutor = threadPool = new ThreadPoolExecutor(threadCount,
				threadCount, 1, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(threadCount * 2, true),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public final static ExecutorWorkerDocument getSingleInstance() {
		if (singleton == null)
			singleton = new ExecutorWorkerDocument();
		return singleton;
	}

	@Override
	public final void findKeywordFromOneDirectory(File path, String query,
			ICallBack callback) {
		// TODO Auto-generated method stub
		/**
		 * algorithm 
		 * 1)check available 
		 * 2)register callback 
		 * 3)path initialize
		 * 4)execute worker
		 */
		if (threadPool.getActiveCount() > 0) {
			log.info("still Running Threads...");
			return;
		}
		setCallback(callback);
		arguments.put(argumentsEnum.DIRECTORY_PATH,path);
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindDoc);
	}

	@Override
	public final void findKeywordFromOneDirectoryInternalCallback() {
		// TODO Auto-generated method stub
		for (int i = 0; i < threadPool.getPoolSize(); i++) {
			jobExecutor.execute(workerFunctions);
		}
	}
	
	@Override
	public final void findKeywordFromAllDirectories(File path, String query,
			ICallBack callback) {
		/**
		 * algorithm
		 * 1)check available
		 * 2)register callback
		 * 3)path initialize
		 * 4)execute worker
		 */
		if (threadPool.getActiveCount() > 0) {
			log.info("still Running Threads...");
			return;
		}
		setCallback(callback);
		arguments.put(argumentsEnum.DIRECTORY_PATH,path);
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindAllDoc);
		
		// TODO Auto-generated method stub
	}

	@Override
	public final void shutdownExecutor() {
		// TODO Auto-generated method stub
		jobExecutor.shutdown();
	}

	@Override
	public final void stopJobThread() {
		// TODO Auto-generated method stub
		WorkerFunctions.stopFunctionsFlag = true;
		WorkerFindDoc.stopFinderFlag = true;
	}

	@Override
	public final void clearJobQueue() {
		// TODO Auto-generated method stub
		/*if (threadPool.getActiveCount() > 0) {
			log.info("still Running Threads...");
			return;
		}*/
		jobQueue.clear();
		log.debug("clear Arguments Haspmap");
		System.gc();
	}
	@Override
	public final void clearArgumentsHashMap() {
		// TODO Auto-generated method stub
		/*if (threadPool.getActiveCount() > 0) {
			log.info("still Running Threads...");
		return;
		}*/
		arguments.clear();
		log.debug("clear Arguments Haspmap");
		System.gc();
	}
	// Implements of InternalExecutor class at bottom --

	public final ConcurrentLinkedQueue<IJobItem> getQueue() {
		// TODO Auto-generated method stub
		return this.jobQueue;
	}

	public final synchronized void notifyJobFinish() {
		// TODO Auto-generated method stub
//		log.debug("Active Thread Count : " + threadPool.getActiveCount());
		if (threadPool.getActiveCount() == 1) {
			log.debug("end Job..");
			callBackToCaller();
		}
	}

	protected final void callBackToCaller() {
		// TODO Auto-generated method stub
		if (callback != null) {
			callback.callback();
			clearJobQueue();
			clearArgumentsHashMap();
		}
	}
	// Implements of InternalExecutor class at top --


}
