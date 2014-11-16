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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.d2.osfad.exception.AlreadyRunThreadsException;
import com.d2.osfad.job.DocumentFile;
import com.d2.osfad.job.IJobItem;
import com.d2.osfad.main.ICallBack;

public abstract class AbstractInternalExecutor {
	public static enum argumentsEnum{DIRECTORY_PATH,THREAD_COUNT,KEYWORD,FILE_LIST,FILE_PATH};
	protected int requestId = 0;						/* caller's request id */
	protected ICallBack callback = null;				/* caller's callback when all jobs are finished */
	protected HashMap<argumentsEnum,Object> arguments = new HashMap<argumentsEnum,Object>();
	private ConcurrentLinkedQueue<IJobItem> jobQueue = null; /*
	 * a queue to save job is shared
	 * with threads
	 */

	public AbstractInternalExecutor()
	{
		jobQueue = new ConcurrentLinkedQueue<IJobItem>();
	}

	public final void internalClearJobQueue() {
		// TODO Auto-generated method stub
		/*if (threadPool.getActiveCount() > 0) {
		log.info("still Running Threads...");
		return;
	}*/
		jobQueue.clear();
	}

	/**
	 * get jobQueue
	 * @return jobQueue
	 */
	public final ConcurrentLinkedQueue<IJobItem> getQueue() {
		// TODO Auto-generated method stub
		return this.jobQueue;
	}

	/**
	 * Clear ArgumentsHashMap
	 */
	public final void internalClearArgumentsHashMap() {
		// TODO Auto-generated method stub
		arguments.clear();
	}

	/**
	 * clear run state (for find)
	 */
	public abstract void clear();

	/**
	 * Clear Job State
	 */
	public abstract void internalClearJobState();

	/**
	 * Callback
	 * start to find keyword
	 * callback from workerFindDoc
	 */
	public abstract void findKeywordFromOneDirectoryInternalCallback(int documentCount);

	/**
	 * Observer Pattern & Callback Function
	 * notify finishing job and when only finishing, can callback
	 * require syncronized
	 */
	public abstract void notifyJobFinish(int result);

	/**
	 * Observer Pattern
	 * notify add result and when only not stop, can callback
	 * require syncronized
	 * @param key
	 * @param value
	 */
	public abstract void notifyAddResult(DocumentFile key, List<Integer> value);

	/**
	 * call caller's functions
	 */
	protected abstract void callBackToCaller();

	/**
	 * 
	 * @return
	 */
	public final int getRequestId() {
		return requestId;
	}

	/**
	 * 
	 * @param requestId
	 */
	public final void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	/**
	 * 
	 * @return
	 */
	public final ICallBack getCallback() {
		return callback;
	}

	/**
	 * 
	 * @param callback
	 */
	public final void setCallback(ICallBack callback) {
		this.callback = callback;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<argumentsEnum,Object> getArguments() {
		return arguments;
	}

	/**
	 * 
	 * @param arguments
	 */
	public void setArguments(HashMap<argumentsEnum,Object> arguments) {
		this.arguments = arguments;
	}

	/**
	 * 
	 * @param path: a path to be searched
	 * @param query: a keyword to find
	 * @param callback: when a work is finished, call callback function 
	 */
	public abstract void internalFindKeywordFromOneDirectory(String path, String query, ICallBack callback)
			throws AlreadyRunThreadsException;

	/**
	 * This function find all sub directories. this is different from OneDirectory
	 * @param path
	 * @param query
	 * @param callback
	 */
	public abstract void internalFindKeywordFromRecursiveDirectories(String path, String query, ICallBack callback)
			throws AlreadyRunThreadsException;

	/**
	 * This function find keyword from one file
	 * @param filePath
	 * @param query
	 * @param callback
	 */
	public abstract void internalFindKeywordfromOneDocument(String filePath, String query, ICallBack callback)
			throws AlreadyRunThreadsException;

	/**
	 * This function find keyword from a list of files
	 * @param fileList
	 * @param query
	 * @param callback
	 */
	public abstract void internalFindKeywordfromPathList(List<String> fileList, String query, ICallBack callback)
			throws AlreadyRunThreadsException;

	/**
	 * Threads is shutdown
	 */
	public abstract void internalShutdownExecutor();

	/**
	 * Stop job of finding keyword
	 */
	public abstract void internalStopJobThread(); 


	/**
	 * get flag
	 * @return
	 */
	public abstract boolean getStopFlag();

	/**
	 * set flag
	 */
	public abstract void setStopFlag(boolean stopFlag);
}
