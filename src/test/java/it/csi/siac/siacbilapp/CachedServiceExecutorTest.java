/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Assert;
import org.junit.Test;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.ServiceResponseCache;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;

/**
 * Classe di test per il CachedServiceExecutor e i suoi metodi interni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/07/2015
 *
 */
public class CachedServiceExecutorTest extends BaseJUnit4TestCase {
	
	/**
	 * Test 1
	 */
	@Test
	public void test1() {
		final String methodName = "test1";
		new Thread(new TestClass("prova")).start();
		new Thread(new TestClass("prova")).start();

		sleep(methodName, 20000L);
		System.gc();

		new Thread(new TestClass("prova")).start();
	}
	
	private void sleep(String methodName, long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			log.error(methodName, "Interrupted", e);
			Assert.fail("Interrupted exception");
		}
	}
	
	private static class TestClass implements Runnable {
		private static Map<String, Lock> locks = new WeakHashMap<String, Lock>();
		private final LogUtil log = new LogUtil(getClass());
		private final String cacheKey;

		TestClass(String cacheKey) {
			this.cacheKey = cacheKey;
		}
		
		@Override
		public void run() {
			this.syncTest();
		}
		
		private void sleep(String methodName, long ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				log.error(methodName, "Interrupted", e);
			}
		}

		public void syncTest() {
			final String methodName = "syncTest";
			String threadName = Thread.currentThread().getName();
			log.info(methodName, "syncTest on " + cacheKey + " Start..." + "[" + threadName + "]");
			
			System.gc();
			Lock lock = locks.get(cacheKey);
			if (lock == null) {
				synchronized (locks) {
					lock = locks.get(cacheKey);
					log.info(methodName, "Lock in map: " + lock+ "[" + threadName + "]");
					if (lock == null) {
						lock = new ReentrantLock();
						locks.put(new String(cacheKey), lock);
					}
				}
			}
			log.info(methodName, "Lock created: " + lock+ "[" + threadName + "]");
			
			lock.lock();
			try{
				log.info(methodName, "SynchronizedBlock on " + cacheKey + " Start..." + "[" + threadName + "] - lock " + lock);
				sleep(methodName, 5000L);
				log.info(methodName, "SynchronizedBlock on " + cacheKey + " End." + "[" + threadName + "] - lock " + lock);
			} finally {
				lock.unlock();
			}
			
			lock = null;
			System.gc();
			
//			synchronized (name.intern()) {
//				log.info(methodName, "SynchronizedBlock on " + name + " Start..." + "[" + threadName + "]");
//				sleep(methodName, 5000L);
//				log.info(methodName, "p: " + name + " [" + Thread.currentThread().getName() + "]");
//				log.info(methodName, "SynchronizedBlock on " + name + " End." + "[" + threadName + "]");
//			}
			log.info(methodName, "syncTest on " + cacheKey + " End." + "[" + threadName + "]");
		}

	}
	
	/**
	 * Test 2
	 */
	@Test
	public void test2() {
		final String methodName = "test2";
		final ReferenceQueue<ServiceResponseCache> rq = new ReferenceQueue<ServiceResponseCache>();
		final Map<String, WeakReference<ServiceResponseCache>> map = new ConcurrentHashMap<String, WeakReference<ServiceResponseCache>>();
		
		ExecutorService ex = Executors.newFixedThreadPool(1);
		ex.execute(new ReferenceQueueReader(rq));
		
		ServiceResponseCache src = new ServiceResponseCache(new InserisceDocumentoSpesaResponse(), 5000);
		map.put("InserisceDocumentoSpesaResponse", new WeakReference<ServiceResponseCache>(src, rq));
		src = null;
		sleep(methodName, 2000);
		System.gc();
		sleep(methodName, 2000);
		System.gc();
		Reference<ServiceResponseCache> weakReference = map.get("InserisceDocumentoSpesaResponse");
		if(weakReference == null) {
			log.info(methodName, "Null WeakReference");
		} else {
			log.info(methodName, "WeakReference not null. Value: " + weakReference.get());
		}
		ex.shutdownNow();
		try {
			ex.awaitTermination(2, TimeUnit.SECONDS);
		} catch(InterruptedException ie) {
			Assert.fail("Interrupted: " + ie.getMessage());
		}
	}
	
	private static final class ReferenceQueueReader implements Runnable {

		private final LogUtil log = new LogUtil(getClass());
		private final ReferenceQueue<ServiceResponseCache> rq;
		
		ReferenceQueueReader(ReferenceQueue<ServiceResponseCache> rq) {
			this.rq = rq;
		}
		
		@Override
		public void run() {
			final String methodName = "run";
			try {
				while(true) {
					Reference<? extends ServiceResponseCache> remove = rq.remove(1 * 1000);
					if(remove != null) {
						log.info(methodName, "Removed reference: " + remove);
					}
				}
			} catch (InterruptedException e1) {
				log.info(methodName, "Exiting thread...");
			}
		}
		
	}

}
