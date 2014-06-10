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
package com.d2.osfad.main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.argo.hwp.HwpTextExtractor;
import com.d2.osfad.executor.ExecutorWorkerDocument;
import com.d2.osfad.executor.IExternalExecutor;

public class javahwp_parse implements ICallBack{
	protected static Logger log = LoggerFactory.getLogger(HwpTextExtractor.class);
	static long start;
	public javahwp_parse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 파일 객체 생성 = 
		start = System.nanoTime();
		
		ICallBack callback = new javahwp_parse();

		IExternalExecutor docexecutor = ExecutorWorkerDocument.getSingleInstance();
		docexecutor.findKeywordFromAllDirectories
		(new File("E:\\11work\\workspaceJava\\Forked-One-Search-Find-All-Documents-1\\resource"), "AAA", callback);

		//docexecutor.findKeywordFromOneDirectory(new File("resource"), "", callback);
		/*log.info("processor : "+Runtime.getRuntime().availableProcessors());
		File hwpfile = new File("resource/한국어문규정집.hwp");
		// StringWriter 버퍼 생성
		StringWriter sw = new StringWriter(4096);
		try {
			//텍스트 추출
			boolean b = HwpTextExtractor.extract(hwpfile, sw);
			if(b){
				PrintWriter pw = new PrintWriter("test/한국어문규정집.txt");
				pw.write(sw.toString());
				pw.close();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		/*TestHwpV5Extractor test1 = new TestHwpV5Extractor();
		try {
			test1.testExtractText();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestHwpV3Extractor test2 = new TestHwpV3Extractor();
		try {
			test2.testExtractText();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			Thread.sleep(5000);
			docexecutor.shutdownExecutor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("main end");
	}

	@Override
	public void callback() {
		// TODO Auto-generated method stub
		long end = System.nanoTime();
		System.out.println( "실행 시간 : " + ( end - start )/1000000000.0 );
		log.info("callback from Executor");
	}

}
