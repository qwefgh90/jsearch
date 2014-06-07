package com.d2.osfad.executor;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.d2.osfad.job.IJobItem;
import com.d2.osfad.main.ICallBack;

public abstract class AbstractInternalExecutor {
public static enum argumentsEnum{DIRECTORY_PATH,THREAD_COUNT};
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
public abstract void notifyJobFinish();
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
