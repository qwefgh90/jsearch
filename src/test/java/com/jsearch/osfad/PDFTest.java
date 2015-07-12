package com.jsearch.osfad;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jsearch.osfad.exception.AlreadyRunThreadsException;
import com.jsearch.osfad.executor.ExternalExcutor;
import com.jsearch.osfad.executor.IExternalExecutor;
import com.jsearch.osfad.job.DocumentFile;
import com.jsearch.osfad.main.ICallBack;

public class PDFTest {
//	/** Countdown latch */
//	private CountDownLatch lock = new CountDownLatch(1);
//	
//	@Test
//	public void hwptest(){
//		// TODO Auto-generated method stub
//		// 파일 객체 생성 =
//		ICallBack callback = new Callback();
//		IExternalExecutor docexecutor = new ExternalExcutor();
//		try {
//			docexecutor.findKeywordfromOneDocument(
//					getClass().getResource("/boot.pdf").getFile().substring(1),
//					"부트로더", callback);
//
//			lock.await(3000,TimeUnit.MILLISECONDS);
//			
//			docexecutor.findKeywordfromOneDocument(
//					getClass().getResource("/javascript.pdf").getFile().substring(1),
//					"자바스크립트", callback);
//
//			lock.await(20000,TimeUnit.MILLISECONDS);
//			
//		} catch (AlreadyRunThreadsException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	class Callback implements ICallBack{
//
//		public void callback() {
//
//		}
//
//		public void callbackResultList(Map<DocumentFile, List<Integer>> result) {
//			System.out.println("실행 결과 : "+result.keySet());
//			assertTrue(result.size()>0);
//		}
//	}
}
