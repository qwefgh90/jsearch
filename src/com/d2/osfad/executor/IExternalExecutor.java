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
