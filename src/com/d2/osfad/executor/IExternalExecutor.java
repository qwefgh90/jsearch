package com.d2.osfad.executor;

import java.io.File;

import com.d2.osfad.main.ICallBack;
/**
 * 
 * @author Chang
 * contains Executor & ThreadPool
 *	Whenever, threads can be stopped.
 */
public interface IExternalExecutor {
/**
 * 
 * @param path a path to be searched
 * @param query a keyword to find
 * @param callback when a work is finished, call callback function 
 */
public abstract void findKeywordFromOneDirectory(File path,String query, ICallBack callback);
/**
 * this function find all sub directories. this is different from OneDirectory
 * @param path
 * @param query
 * @param callback
 */
public abstract void findKeywordFromAllDirectories(File path, String query, ICallBack callback);

public abstract void shutdownExecutor();
void stopJobThread(); 
void clearJobQueue();
void clearArgumentsHashMap();
}
