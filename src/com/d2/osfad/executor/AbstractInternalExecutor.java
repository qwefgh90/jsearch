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
import java.util.concurrent.ConcurrentLinkedQueue;

import com.d2.osfad.job.IJobItem;
import com.d2.osfad.main.ICallBack;

public abstract class AbstractInternalExecutor {
public static enum argumentsEnum{DIRECTORY_PATH,THREAD_COUNT,KEYWORD};
protected int requestId = 0;						/* caller's request id */
protected ICallBack callback = null;				/* caller's callback when all jobs are finished */
protected HashMap<argumentsEnum,Object> arguments = new HashMap<argumentsEnum,Object>();
/**
 * get jobQueue
 * @return jobQueue
 */
public abstract ConcurrentLinkedQueue<IJobItem> getQueue();
/**
 * Callback
 * start to find keyword
 * callback from workerFindDoc
 */
public abstract void findKeywordFromOneDirectoryInternalCallback();

/**
 * Observer Pattern & Callback Function
 * notify finishing job
 */
public abstract void notifyJobFinish(int result);
/**
 * call caller's functions
 */
protected abstract void callBackToCaller();

public final int getRequestId() {
	return requestId;
}
public final void setRequestId(int requestId) {
	this.requestId = requestId;
}
public final ICallBack getCallback() {
	return callback;
}
public final void setCallback(ICallBack callback) {
	this.callback = callback;
}
public HashMap<argumentsEnum,Object> getArguments() {
	return arguments;
}
public void setArguments(HashMap<argumentsEnum,Object> arguments) {
	this.arguments = arguments;
}

}
