package com.jsearch.osfad.executor;

import java.util.List;

import com.jsearch.osfad.exception.AlreadyRunThreadsException;
import com.jsearch.osfad.main.ICallBack;

public class ExternalExcutor implements IExternalExecutor{
	
	InternalExcutor internalExcutor = null;
	public ExternalExcutor() {
		super();
		internalExcutor = InternalExcutor.getSingleInstance();
	}

	public void findKeywordFromOneDirectory(String path, String query,
			ICallBack callback) throws AlreadyRunThreadsException {
		internalExcutor.internalFindKeywordFromOneDirectory(path, query, callback);
		
	}

	public void findKeywordFromRecursiveDirectories(String path, String query,
			ICallBack callback) throws AlreadyRunThreadsException {
		// TODO Auto-generated method stub
		internalExcutor.internalFindKeywordFromRecursiveDirectories(path, query, callback);
		
	}

	public void findKeywordfromOneDocument(String filePath, String query,
			ICallBack callback) throws AlreadyRunThreadsException {
		// TODO Auto-generated method stub
		internalExcutor.internalFindKeywordfromOneDocument(filePath, query, callback);
		
	}

	public void findKeywordfromPathList(List<String> pathList, String query,
			ICallBack callback) throws AlreadyRunThreadsException {
		// TODO Auto-generated method stub
		internalExcutor.internalFindKeywordfromPathList(pathList, query, callback);
		
	}

	public void shutdownExecutor() {
		// TODO Auto-generated method stub
		internalExcutor.internalShutdownExecutor();
	}

	public void stopWork() {
		// TODO Auto-generated method stub
		internalExcutor.internalStopJobThread();
	}

	public void clearJobQueue() {
		// TODO Auto-generated method stub
		internalExcutor.internalClearJobQueue();
	}

	public void clearArgumentsHashMap() {
		// TODO Auto-generated method stub
		internalExcutor.internalClearArgumentsHashMap();
	}


}
