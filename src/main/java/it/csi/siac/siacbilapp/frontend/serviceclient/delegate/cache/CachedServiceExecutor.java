/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.exception.CachedServiceExecutorException;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy.CacheRollingPolicy;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy.DefaultCacheRollingPolicy;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.KeyAdapter;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Cache proxy for service execution.
 * <br>
 * Executes the service only if the response is not already in cache or stale according to the specified {@link CacheRollingPolicy}.
 * 
 * @author Domenico Lisi
 * @author Marchino Alessandro
 * 
 * @version 1.0.0 - 30/09/2014
 * @version 1.1.0 - 06/10/2014 - Use of {@link SoftReference}s for cached values
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CachedServiceExecutor {
	
	private final LogUtil log = new LogUtil(this.getClass());
	
	/** The application-level cache */
	private final Map<String, Reference<ServiceResponseCache>> cache = new ConcurrentHashMap<String, Reference<ServiceResponseCache>>();
	/** The locks map */
	private final Map<String, Lock> locks = new WeakHashMap<String, Lock>();
	
	private DefaultCacheRollingPolicy defaultCacheRollingPolicyEnum;
	private CacheRollingPolicy defaultCacheRollingPolicy;
	
	/** The executor used to keep track of <abbr title="Garbage Collector">GC</abbr>-reclaimed cached values */
	private final ExecutorService reclaimedCacheLoggingListenerExecutor = Executors.newFixedThreadPool(1);
	/** The reference queue for <abbr title="Garbage Collector">GC</abbr>-reclaimed cached values */
	private final ReferenceQueue<ServiceResponseCache> referenceQueue = new ReferenceQueue<ServiceResponseCache>();
	/** Counter for <abbr title="Garbage Collector">GC</abbr>-reclaimed cached values */
	private final AtomicLong gcCounter = new AtomicLong();
	
	/**
	 * Initialization for the Executor.
	 */
	@PostConstruct
	public void init() {
		final String methodName = "init";
		log.info(methodName, "CachedServiceExecutor created...");
		
		initDefaultCacheRollingPolicy();
		
		reclaimedCacheLoggingListenerExecutor.execute(new ReclaimedCacheLoggingListener(gcCounter, referenceQueue));
	}

	/**
	 * Inits the default cache rolling policy.
	 *
	 */
	private void initDefaultCacheRollingPolicy() {
		final String methodName = "initDefaultCacheRollingPolicy";
		
		// Se la cache rolling policy di default e' impostata da enum inizializzo defaultCacheRollingPolicy.
		if(defaultCacheRollingPolicyEnum != null){
			defaultCacheRollingPolicy = defaultCacheRollingPolicyEnum.getRollingPolicy();
		}

		// Se la cache rolling policy non e' impostata ne' da enum ne' come classe la inizializzo a NEVER.
		if(defaultCacheRollingPolicy == null){
			defaultCacheRollingPolicy = DefaultCacheRollingPolicy.NEVER.getRollingPolicy();
		}
		
		log.info(methodName, "defaultCacheRollingPolicy: " + defaultCacheRollingPolicy.getClass().getName());
	}
	
	/**
	 * Destruction of the Executor.
	 */
	@PreDestroy
	public void preDestroy() {
		final String methodName = "preDestroy";
		log.info(methodName, "CachedServiceExecutor destroyed. Shutting down the ReclaimedCacheLoggingListenerExecutor...");
		reclaimedCacheLoggingListenerExecutor.shutdownNow();
		log.info(methodName, "CachedServiceExecutor shut down");
	}

	/**
	 * Executes the service via default KeyAdapter and CacheRollingPolicy.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param req the request for the service invocation
	 * @param serviceExecutor the service executor
	 * 
	 * @return the response from the service, either cached or new
	 * 
	 * @see #executeService(ServiceRequest, ServiceExecutor, KeyAdapter, CacheRollingPolicy)
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse> RES executeService(REQ req, ServiceExecutor<REQ, RES> serviceExecutor) {
		KeyAdapter<REQ> keyAdapter = instantiateKeyAdapter(req);
		return executeService(req, serviceExecutor, keyAdapter, defaultCacheRollingPolicy);
	}

	/**
	 * Executes the service via default CacheRollingPolicy.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param req the request for the service invocation
	 * @param serviceExecutor the service executor
	 * @param keyAdapter the keyAdapter
	 * 
	 * @return the response from the service, either cached or new
	 * 
	 * @see #executeService(ServiceRequest, ServiceExecutor, KeyAdapter, CacheRollingPolicy)
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse> RES executeService(REQ req, ServiceExecutor<REQ, RES> serviceExecutor,
			KeyAdapter<REQ> keyAdapter) {
		return executeService(req, serviceExecutor, keyAdapter, defaultCacheRollingPolicy);
	}

	/**
	 * Executes the service via default KeyAdapter.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param req the request for the service invocation
	 * @param serviceExecutor the service executor
	 * @param cacheRollingPolicy the cacheRollingPolicy
	 * 
	 * @return the response from the service, either cached or new
	 * 
	 * @see #executeService(ServiceRequest, ServiceExecutor, KeyAdapter, CacheRollingPolicy)
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse> RES executeService(REQ req, ServiceExecutor<REQ, RES> serviceExecutor,
			CacheRollingPolicy cacheRollingPolicy) {
		KeyAdapter<REQ> keyAdapter = instantiateKeyAdapter(req);
		return executeService(req, serviceExecutor, keyAdapter, cacheRollingPolicy);
	}

	/**
	 * Executes the service via default KeyAdapter.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param req the request for the service invocation
	 * @param serviceExecutor the service executor
	 * @param keyAdapter the keyAdapter
	 * @param cacheRollingPolicy the cacheRollingPolicy
	 * 
	 * @return the response from the service, either cached or new
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse> RES executeService(REQ req, ServiceExecutor<REQ, RES> serviceExecutor,
			KeyAdapter<REQ> keyAdapter, CacheRollingPolicy cacheRollingPolicy) {
		final String methodName = "executeService";
		String cacheKey = "CachedServiceExecutor_" + req.getClass().getName() + "_" + keyAdapter.computeKey(req);

		// Obtains the lock from the lock map
		Lock lock = locks.get(cacheKey);
		if (lock == null) {
			synchronized (locks) {
				lock = locks.get(cacheKey);
				if (lock == null) {
					lock = new ReentrantLock();
					/*
					 * We create a new String by copying the previous one used as key.
					 * 
					 * By doing so, we avoid using a strong reference to an otherwise un-garbage-collactable object
					 * (the cacheKey String, which is persisted in-memory in the String pool), thus making
					 * the lock we just created eligible to be garbage-collected via the mechanics of the WeakHashMap.
					 * 
					 * This will obviously not work if the string was interned (or will be interned prior to the GC-cycle which
					 * will clean the map). But (we hope that) this case should be rare enough not to cause serious
					 * heap pollution.
					 */
					locks.put(new String(cacheKey), lock);
				}
			}
		}

		// Locks down
		lock.lock();
		try {
			if (cache.containsKey(cacheKey)) {
				// We have the cached data
				Reference<ServiceResponseCache> reference = cache.get(cacheKey);
				ServiceResponseCache src = reference.get();
				if(src == null) {
					log.info(methodName, "The cacheKey has been reclaimed by Garbage Collector: " + cacheKey);
				} else if (cacheRollingPolicy.isExpired(src.getCacheDate(), src.getHitCount())) {
					// Stale data: we ought to obtain it again
					log.info(methodName, "The cacheKey has expired: " + cacheKey + ". (hitCount:" + src.getHitCount() + ", cacheDate:" + src.getCacheDate() + ")");
				} else {
					// The data is not yet stale: use it
					src.hit();
					log.info(methodName, "Returning cached result for cacheKey: " + cacheKey + ". (hitCount:" + src.getHitCount() + ", cacheDate:"
							+ src.getCacheDate() + ")");
					return src.getServiceResponse();
				}
			}

			log.info(methodName, "invoking service for cacheKey: " + cacheKey);
			long currentTimeMillis = System.currentTimeMillis();
			RES res = serviceExecutor.executeService(req);
			if(res != null && res.getErrori() != null && res.getErrori().isEmpty()) {
				ServiceResponseCache src = new ServiceResponseCache(res, System.currentTimeMillis() - currentTimeMillis);
				cache.put(cacheKey, new SoftReference<ServiceResponseCache>(src, referenceQueue));
				src = null;
			}

			return res;
		} finally {
			// Release the lock
			lock.unlock();
		}
	}

	/**
	 * Instantiates a standard KeyAdapter to create the cache key with.
	 * 
	 * @param req the request for which the KeyAdapter is to be created
	 * 
	 * @return the KeyAdapter correponding to the request
	 */
	@SuppressWarnings("unchecked")
	private <REQ extends ServiceRequest> KeyAdapter<REQ> instantiateKeyAdapter(REQ req) {
		String className = "it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.keyadapter." + req.getClass().getSimpleName() + "KeyAdapter";
		Class<?> keyAdapterClass;

		try {
			// Tries to instantiates the class for the keyAdapter
			keyAdapterClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new CachedServiceExecutorException("KeyAdapter not found: " + className, e);
		}

		KeyAdapter<REQ> keyAdapter;
		try {
			// Tries to obtain an instance for the keyAdapter class
			keyAdapter = (KeyAdapter<REQ>) keyAdapterClass.newInstance();
		} catch (InstantiationException e) {
			throw new CachedServiceExecutorException("KeyAdapter not instantiable: " + className + ". Check for the presence of an empty constructor.", e);
		} catch (IllegalAccessException e) {
			throw new CachedServiceExecutorException("Access to the KeyAdapeter constructor impossible: " + className, e);
		}

		return keyAdapter;
	}
	
	/**
	 * @return the defaultCacheRollingPolicy
	 */
	public CacheRollingPolicy getDefaultCacheRollingPolicy() {
		return defaultCacheRollingPolicy;
	}

	/**
	 * @param defaultCacheRollingPolicy the defaultCacheRollingPolicy to set
	 */
	public void setDefaultCacheRollingPolicy(CacheRollingPolicy defaultCacheRollingPolicy) {
		this.defaultCacheRollingPolicy = defaultCacheRollingPolicy;
	}


	/**
	 * @return the defaultCacheRollingPolicyEnum
	 */
	public DefaultCacheRollingPolicy getDefaultCacheRollingPolicyEnum() {
		return defaultCacheRollingPolicyEnum;
	}

	/**
	 * @param defaultCacheRollingPolicyEnum the defaultCacheRollingPolicyEnum to set
	 */
	public void setDefaultCacheRollingPolicyEnum(DefaultCacheRollingPolicy defaultCacheRollingPolicyEnum) {
		this.defaultCacheRollingPolicyEnum = defaultCacheRollingPolicyEnum;
	}
	
	/**
	 * Clears the cache.
	 */
	public void clearCache() {
		cache.clear();
	}

	/**
	 * Clear a cache key
	 * @param key the key to clear
	 * @return whether the reference was correctly deleted from the cache
	 */
	public boolean clearKey(String key) {
		Reference<ServiceResponseCache> reference = cache.remove(key);
		if(reference == null) {
			// Reference not already in cache
			return false;
		}
		reference = null;
		return true;
	}
	
	/**
	 * Obtain the cached keys.
	 * <br>
	 * The <em>probable</em> in the previous sentence is to be understood in the sense that we do <strong>not</strong> synchronize
	 * the cache prior or during the key retrieval.
	 * @return the keys in the cache
	 */
	public Set<String> obtainProbableCachedKeys() {
		return cache.keySet();
	}
	
	/**
	 * Obtain the cached keys.
	 * @return the keys in the cache
	 */
	public Set<String> obtainCachedKeys() {
		synchronized(cache) {
			return obtainProbableCachedKeys();
		}
	}
	
	/**
	 * Computes the probable statistics for the cache.
	 * <br>
	 * The <em>probable</em> in the previous sentence is to be understood in the sense that we do <strong>not</strong> synchronize
	 * the cache prior or during the loop.
	 * 
	 * @return the probable statistics for the cache, if the cache (probably) contains at least an element; <code>null</code> otherwise
	 */
	public CacheStatistics computeProbableStatistics() {
		// HitsCount
		long hitsCount = 0L;
		long cachedHitsCount = 0L;
		BigInteger savedTime = BigInteger.ZERO;
		long lastCachedTime = Long.MIN_VALUE;
		Class<?> lastCachedTimeClass = null;
		long firstCachedTime = Long.MAX_VALUE;
		Class<?> firstCachedTimeClass = null;
		int maxHitsCount = 0;
		Class<?> maxHitsCountClass = null;
		long maxResponseTime = Long.MIN_VALUE;
		Class<?> maxResponseTimeClass = null;
		BigInteger maxSavedTime = BigInteger.ZERO;
		Class<?> maxSavedTimeClass = null;
		
		for(Reference<ServiceResponseCache> reference : cache.values()) {
			ServiceResponseCache value = reference.get();
			if(value == null) {
				// Garbage Collecteds
				continue;
			}
			long currentMaxHitsCount = maxHitsCount;
			long currentMaxResponseTime = maxResponseTime;
			long currentLastCachedTime = lastCachedTime;
			long currentFirstCachedTime = firstCachedTime;
			BigInteger currentMaxSavedTime = maxSavedTime;
			// HitsCount
			hitsCount += value.getHitCount();
			// CachedHitsCount
			cachedHitsCount = value.getHitCount() - 1;
			savedTime = savedTime.add(BigInteger.valueOf(cachedHitsCount).multiply(BigInteger.valueOf(value.getElapsedMillis())));
			// LastCachedDate
			lastCachedTime = Math.max(lastCachedTime, value.getCacheDate().getTime());
			if(lastCachedTime > currentLastCachedTime) {
				lastCachedTimeClass = value.getServiceResponseClass();
			}
			// FirstCachedDate
			firstCachedTime = Math.min(firstCachedTime, value.getCacheDate().getTime());
			if(firstCachedTime < currentFirstCachedTime) {
				firstCachedTimeClass = value.getServiceResponseClass();
			}
			// MaxHitsCount
			maxHitsCount = Math.max(maxHitsCount, value.getHitCount());
			if(maxHitsCount > currentMaxHitsCount) {
				maxHitsCountClass = value.getServiceResponseClass();
			}
			// MaxResponseTime
			maxResponseTime = Math.max(maxResponseTime, value.getElapsedMillis());
			if(maxResponseTime > currentMaxResponseTime) {
				maxResponseTimeClass = value.getServiceResponseClass();
			}
			// MaxSavedTime
			maxSavedTime = maxSavedTime.max(savedTime);
			if(maxSavedTime.compareTo(currentMaxSavedTime) > 0) {
				maxSavedTimeClass = value.getServiceResponseClass();
			}
		}
		
		CacheStatistics cs = new CacheStatistics();
		cs.setCacheRollingPolicy(getDefaultCacheRollingPolicy());
		cs.setCacheSize(cache.size());
		cs.setHitsCount(Long.valueOf(hitsCount));
		cs.setCachedHitsCount(Long.valueOf(cachedHitsCount));
		cs.setSavedTime(savedTime);
		cs.setReclaimedValueInCacheMapCount(Long.valueOf(gcCounter.get()));
		
		if(maxHitsCountClass != null) {
			cs.setMaxHitsCountClass(maxHitsCountClass);
			cs.setMaxHitsCount(Integer.valueOf(maxHitsCount));
		}
		if(maxResponseTimeClass != null) {
			cs.setMaxResponseTimeClass(maxResponseTimeClass);
			cs.setMaxResponseTime(Long.valueOf(maxResponseTime));
		}
		if(maxSavedTimeClass != null) {
			cs.setMaxSavedTimeClass(maxSavedTimeClass);
			cs.setMaxSavedTime(maxSavedTime);
		}
		if(lastCachedTime > Long.MIN_VALUE) {
			cs.setLastCachedTimeClass(lastCachedTimeClass);
			cs.setLastCachedTime(new Date(lastCachedTime));
		}
		if(firstCachedTime < Long.MAX_VALUE) {
			cs.setFirstCachedTimeClass(firstCachedTimeClass);
			cs.setFirstCachedTime(new Date(firstCachedTime));
		}
		
		cs.setLockMapSize(Integer.valueOf(locks.size()));
		
		return cs;
	}
	
	/**
	 * Obtains the informations regarding the cache.
	 * 
	 * @return the informations for the cache, if the cache contains at least an element; <code>null</code> otherwise
	 */
	public CacheStatistics computeStatistics() {
		synchronized (cache) {
			return computeProbableStatistics();
		}
	}
	
	/**
	 * {@link Runnable} implementor logging eventually <abbr title="Garbage Collector">GC</abbr>-reclaimed references from the referenceQueue.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 06/10/2014
	 *
	 */
	private static class ReclaimedCacheLoggingListener implements Runnable {
		private final LogUtil logger = new LogUtil(this.getClass());
		
		private final AtomicLong garbageCollectorCounter;
		private final ReferenceQueue<ServiceResponseCache> cacheReferenceQueue;
		
		/**
		 * Costruttore di default.
		 * @param garbageCollectorCounter il contatore per il GC
		 * @param cacheReferenceQueue la reference queue per la cache
		 */
		ReclaimedCacheLoggingListener(AtomicLong garbageCollectorCounter, ReferenceQueue<ServiceResponseCache> cacheReferenceQueue) {
			this.garbageCollectorCounter = garbageCollectorCounter;
			this.cacheReferenceQueue = cacheReferenceQueue;
		}
		
		@Override
		public void run() {
			final String methodName = "run";
			logger.info(methodName, "Listening for items removed from the cache");
			try {
				while(true) {
					Reference<? extends ServiceResponseCache> removed = cacheReferenceQueue.remove();
					long count = garbageCollectorCounter.incrementAndGet();
					logger.debug(methodName, "Removed item " + removed + " from cache (" + count + " references reaped since thread started)");
				}
			} catch(InterruptedException ie) {
				logger.info(methodName, "Shutting down the thread...");
			}
			logger.info(methodName, "Thread shut down");
		}
	}
	
}
