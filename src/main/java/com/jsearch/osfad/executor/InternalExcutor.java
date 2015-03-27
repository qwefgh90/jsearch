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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsearch.osfad.exception.AlreadyRunThreadsException;
import com.jsearch.osfad.job.DocumentFile;
import com.jsearch.osfad.job.IJobItem;
import com.jsearch.osfad.job.WorkerFunctions;
import com.jsearch.osfad.main.ICallBack;

public class InternalExcutor extends AbstractInternalExecutor{
	public final static int SCALE_FACTOR = 2; /* a factor of count of threads */
	protected static Logger log = LoggerFactory
			.getLogger(InternalExcutor.class);
	int threadCount; 						/* total thread count */
	private static InternalExcutor singleton = null;
	/**
	 * jobQueue a list of files to be searched
	 */
	private Runnable workerFindFromOneDirectory = null; //현재 디렉토리에서 문서 찾기 
	private Runnable workerFindFromRecursiveDirectories = null; //모든 디렉토리에서 문서 검색 순서 
	private Runnable workerFindFromOneDocument = null; //하나의 문서에서 검색
	private Runnable workerFindFromPathList = null; //경로 리스트를 읽어와서 검색
	private Runnable[] workerFunctions = null; //문자열 검색  : 문서 검색 후 실행됨
	private ThreadPoolExecutor threadPool = null; //스레드 풀 : 스레드 할당 -> 서비스 및 
	private ExecutorService jobExecutor = null; /* Executor interface */ //스레드 매니저
	
	private InternalExcutor()
	{
		super();
		initWorkerClass();
		/**
		 * instantiate a ThreadPool as jobExecutor max = processor * factor + 1
		 * ( plus one for light-weight thread)
		 */
		threadCount = Runtime.getRuntime().availableProcessors()
				* SCALE_FACTOR + 1; /* processor * factor + 1 */
		initThreadPool(threadCount);
		initWorkerFunctions(threadCount);
	}

	/**
	 * init Workers for thread
	 */
	private void initWorkerClass()
	{
		workerFindFromOneDirectory = new WorkerFindFromOneDirectory(this);
		workerFindFromRecursiveDirectories = new WorkerFindRecursiveDirectories(this);
		workerFindFromOneDocument = new WorkerFindFromOneDocument(this);
		workerFindFromPathList = new WorkerFindFromPathList(this);
	}
	
	/**
	 * init Worker functions
	 * @param threadCount
	 */
	private void initWorkerFunctions(int threadCount)
	{
		workerFunctions = new WorkerFunctions[threadCount];
		
		for(int i=0; i < threadCount; i++){
			workerFunctions[i] = new WorkerFunctions(this);
		}
	}
	
	/**
	 * create ThreadPool
	 * @param threadCount
	 */
	private void initThreadPool(int threadCount)
	{
		jobExecutor = threadPool = new ThreadPoolExecutor(threadCount,
				threadCount, 1, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(threadCount * 2, true),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public final static InternalExcutor getSingleInstance() {
		if (singleton == null)
			singleton = new InternalExcutor();
		return singleton;
	}

	/**
	 * operation process
	 * 1)check available 
	 * 2)register callback 
	 * 3)path initialize
	 * 4)execute control role worker
	 */
	@Override
	public final void internalFindKeywordFromOneDirectory(String path, String query,
			ICallBack callback) throws AlreadyRunThreadsException {
		if (threadPool.getActiveCount() > 0) {
			log.warn("Still Run Threads...");
			throw new AlreadyRunThreadsException();
		}
		clear();
		setCallback(callback);
		arguments.put(argumentsEnum.KEYWORD,query);
		arguments.put(argumentsEnum.DIRECTORY_PATH,new DocumentFile(path));
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindFromOneDirectory);
	}

	/**
	 * operation process
	 * 1)check available
	 * 2)register callback
	 * 3)path initialize
	 * 4)execute control role worker
	 */
	@Override
	public final void internalFindKeywordFromRecursiveDirectories(String path, String query,
			ICallBack callback) throws AlreadyRunThreadsException
	{
		if (threadPool.getActiveCount() > 0) {
			log.info("still Running Threads...");
			throw new AlreadyRunThreadsException();
		}
		clear();
		setCallback(callback);
		arguments.put(argumentsEnum.KEYWORD, query);
		arguments.put(argumentsEnum.DIRECTORY_PATH, path);
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindFromRecursiveDirectories);
	}

	@Override
	public void internalFindKeywordfromOneDocument(String filePath, String query,
			ICallBack callback) {
		clear();
		setCallback(callback);
		arguments.put(argumentsEnum.KEYWORD, query);
		arguments.put(argumentsEnum.FILE_PATH, filePath);
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindFromOneDocument);
	}

	@Override
	public void internalFindKeywordfromPathList(List<String> pathList, String query,
			ICallBack callback) {
		clear();
		setCallback(callback);
		arguments.put(argumentsEnum.KEYWORD, query);
		arguments.put(argumentsEnum.FILE_LIST, pathList);
		arguments.put(argumentsEnum.THREAD_COUNT, threadCount);
		setArguments(arguments);
		jobExecutor.execute(workerFindFromPathList);
		
	}

	@Override
	public final void internalShutdownExecutor() {
		// TODO Auto-generated method stub
		jobExecutor.shutdown();
	}

	
	// Implements of InternalExecutor class at bottom --
	@Override
	public final void findKeywordFromOneDirectoryInternalCallback(int documentCount) {
		// TODO Auto-generated method stub
		for (int i = 0; i < threadCount; i++) {
			jobExecutor.execute(workerFunctions[i]);
		}
	}
	
	
	@Override
	public final synchronized void notifyJobFinish(int result) {
		this.endThreadsCount += result;
		if (this.endThreadsCount == this.threadCount || this.getStopFlag() == true) {
			log.info("end Job.. "+this.endThreadsCount+" threads");
			callBackToCaller();
		}
	}

	@Override
	public synchronized void notifyAddResult(DocumentFile key, List<Integer> value) {
		if (this.getStopFlag() == false)
			resultMap.put(key, value);
	}
	
	protected final void callBackToCaller() {
		// TODO Auto-generated method stub
		if (callback != null) {
			callback.callback();
			callback.callbackResultList(new HashMap<DocumentFile, List<Integer>>(resultMap));
			callback = null;	// finish call callback function
		}
	}
	// Implements of InternalExecutor class at top --


}
