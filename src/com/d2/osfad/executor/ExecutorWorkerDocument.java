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

import com.d2.osfad.job.DocumentFile;
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
	int threadCount; 						/* total thread count */
	private static ExecutorWorkerDocument singleton = null;
	/**
	 * jobQueue a list of files to be searched
	 */
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null; /*
											 * a queue to save job is shared
											 * with threads
											 */
	private Runnable workerFindDoc = null; //현재 디렉토리에서 문서 찾기 
	private Runnable[] workerFunctions = null; //문자열 검색  : 문서 검색 후 실행됨
	private Runnable workerFindAllDoc = null; //모든 디렉토리에서 문서 검색 순서 
	private ThreadPoolExecutor threadPool = null; //스레드 풀 : 스레드 할당 -> 서비스 및 
	private ExecutorService jobExecutor = null; /* Executor interface */ //스레드 매니저
	
	private ExecutorWorkerDocument() {
		jobQueue = new ConcurrentLinkedQueue<IJobItem>();
		workerFindDoc = new WorkerFindDoc(this);
//		workerFunctions = new WorkerFunctions(this);
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
		workerFunctions=new WorkerFunctions[threadCount];
		
		for(int i=0; i < threadCount; i++){
			workerFunctions[i] = new WorkerFunctions(this);
		}
	}

	public final static ExecutorWorkerDocument getSingleInstance() {
		if (singleton == null)
			singleton = new ExecutorWorkerDocument();
		return singleton;
	}

	@Override
	public final void findKeywordFromOneDirectory(String path, String query,
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
			log.warn("Still Run Threads...");
			return;
		}
		setCallback(callback);
		arguments.put(argumentsEnum.KEYWORD,query);
		arguments.put(argumentsEnum.DIRECTORY_PATH,new DocumentFile(path));
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindDoc);
	}
	
	@Override
	public final void findKeywordFromAllDirectories(String path, String query,
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
		arguments.put(argumentsEnum.KEYWORD,query);
		arguments.put(argumentsEnum.DIRECTORY_PATH,new DocumentFile(path));
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindAllDoc);
		
		// TODO Auto-generated method stub
	}

	@Override
	public final void findKeywordFromOneDirectoryInternalCallback() {
		// TODO Auto-generated method stub
		for (int i = 0; i < threadCount; i++) {
			jobExecutor.execute(workerFunctions[i]);
		}
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
	int result=0;
	public final synchronized void notifyJobFinish(int result) {
		this.result += result;
		// TODO Auto-generated method stub
//		log.debug("Active Thread Count : " + threadPool.getActiveCount());
		if (threadPool.getActiveCount() == 1) {
			log.info("end Job.. Find "+result+" Documents");
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
