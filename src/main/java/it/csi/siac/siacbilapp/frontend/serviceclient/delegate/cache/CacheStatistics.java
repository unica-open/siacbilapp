/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy.CacheRollingPolicy;

/**
 * Statistics for the cache.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/10/2014
 *
 */
public class CacheStatistics implements Serializable {
	
	/** For serialization purpose */
	private static final long serialVersionUID = 2233106971957945330L;
	
	private transient CacheRollingPolicy cacheRollingPolicy;
	private Integer cacheSize;
	private Long hitsCount;
	private Long cachedHitsCount;
	private BigInteger savedTime;
	private Date lastCachedTime;
	private Class<?> lastCachedTimeClass;
	private Date firstCachedTime;
	private Class<?> firstCachedTimeClass;
	private Integer maxHitsCount;
	private Class<?> maxHitsCountClass;
	private Long maxResponseTime;
	private Class<?> maxResponseTimeClass;
	private BigInteger maxSavedTime;
	private Class<?> maxSavedTimeClass;
	private Long reclaimedValueInCacheMapCount;
	private Integer lockMapSize;
	
	/** Default empty constructor */
	public CacheStatistics() {
		// Costruttore vuoto di utilita'
	}
	
	/**
	 * Constructor given the properties.
	 * 
	 * @param cacheRollingPolicy            the cacheRollingPOlicy
	 * @param cacheSize                     the cacheSize
	 * @param hitsCount                     the hitsCount
	 * @param cachedHitsCount               the cachedHitsCount
	 * @param savedTime                     the savedTime
	 * @param lastCachedTime                the lastCachedTime
	 * @param lastCachedTimeClass           the lastCachedTimeClass
	 * @param firstCachedTime               the firstCachedTime
	 * @param firstCachedTimeClass          the firstCachedTimeClass
	 * @param maxHitsCount                  the maxHitsCount
	 * @param maxHitsCountClass             the maxHitsCountClass
	 * @param maxResponseTime               the maxResponseTime
	 * @param maxResponseTimeClass          the maxResponseTimeClass
	 * @param maxSavedTime                  the maxSavedTime
	 * @param maxSavedTimeClass             the maxSavedTimeClass
	 * @param reclaimedValueInCacheMapCount the reclaimedValueInCacheMapCount
	 * @param lockMapSize                   the lockMapSize
	 */
	public CacheStatistics(CacheRollingPolicy cacheRollingPolicy, Integer cacheSize, Long hitsCount, Long cachedHitsCount, BigInteger savedTime,
			Date lastCachedTime, Class<?> lastCachedTimeClass, Date firstCachedTime, Class<?> firstCachedTimeClass, Integer maxHitsCount, Class<?> maxHitsCountClass,
			Long maxResponseTime, Class<?> maxResponseTimeClass, BigInteger maxSavedTime, Class<?> maxSavedTimeClass,
			Long reclaimedValueInCacheMapCount, Integer lockMapSize) {
		super();
		this.cacheRollingPolicy = cacheRollingPolicy;
		this.cacheSize = cacheSize;
		this.hitsCount = hitsCount;
		this.cachedHitsCount = cachedHitsCount;
		this.savedTime = savedTime;
		this.lastCachedTimeClass = lastCachedTimeClass;
		this.firstCachedTimeClass = firstCachedTimeClass;
		this.maxHitsCount = maxHitsCount;
		this.maxHitsCountClass = maxHitsCountClass;
		this.maxResponseTime = maxResponseTime;
		this.maxResponseTimeClass = maxResponseTimeClass;
		this.maxSavedTime = maxSavedTime;
		this.maxSavedTimeClass = maxSavedTimeClass;
		this.reclaimedValueInCacheMapCount = reclaimedValueInCacheMapCount;
		this.lockMapSize = lockMapSize;
		
		if(lastCachedTime != null) {
			this.lastCachedTime = new Date(lastCachedTime.getTime());
		}
		if(firstCachedTime != null) {
			this.firstCachedTime = new Date(firstCachedTime.getTime());
		}
	}

	/**
	 * @return the cacheRollingPolicy
	 */
	public CacheRollingPolicy getCacheRollingPolicy() {
		return cacheRollingPolicy;
	}

	/**
	 * @param cacheRollingPolicy the cacheRollingPolicy to set
	 */
	public void setCacheRollingPolicy(CacheRollingPolicy cacheRollingPolicy) {
		this.cacheRollingPolicy = cacheRollingPolicy;
	}

	/**
	 * @return the cacheSize
	 */
	public Integer getCacheSize() {
		return cacheSize;
	}

	/**
	 * @param cacheSize the cacheSize to set
	 */
	public void setCacheSize(Integer cacheSize) {
		this.cacheSize = cacheSize;
	}

	/**
	 * @return the hitsCount
	 */
	public Long getHitsCount() {
		return hitsCount;
	}

	/**
	 * @param hitsCount the hitsCount to set
	 */
	public void setHitsCount(Long hitsCount) {
		this.hitsCount = hitsCount;
	}

	/**
	 * @return the cachedHitsCount
	 */
	public Long getCachedHitsCount() {
		return cachedHitsCount;
	}

	/**
	 * @param cachedHitsCount the cachedHitsCount to set
	 */
	public void setCachedHitsCount(Long cachedHitsCount) {
		this.cachedHitsCount = cachedHitsCount;
	}
	
	/**
	 * @return the cachedHitsCount
	 */
	public Long getMissedHitsCount() {
		return hitsCount.longValue() - cachedHitsCount.longValue();
	}

	/**
	 * @return the savedTime
	 */
	public BigInteger getSavedTime() {
		return savedTime;
	}

	/**
	 * @param savedTime the savedTime to set
	 */
	public void setSavedTime(BigInteger savedTime) {
		this.savedTime = savedTime;
	}

	/**
	 * @return the lastCachedTime
	 */
	public Date getLastCachedTime() {
		return lastCachedTime == null ? null : new Date(lastCachedTime.getTime());
	}

	/**
	 * @param lastCachedTime the lastCachedTime to set
	 */
	public void setLastCachedTime(Date lastCachedTime) {
		this.lastCachedTime = lastCachedTime == null ? null : new Date(lastCachedTime.getTime());
	}

	/**
	 * @return the lastCachedTimeClass
	 */
	public Class<?> getLastCachedTimeClass() {
		return lastCachedTimeClass;
	}

	/**
	 * @param lastCachedTimeClass the lastCachedTimeClass to set
	 */
	public void setLastCachedTimeClass(Class<?> lastCachedTimeClass) {
		this.lastCachedTimeClass = lastCachedTimeClass;
	}

	/**
	 * @return the firstCachedTime
	 */
	public Date getFirstCachedTime() {
		return firstCachedTime == null ? null : new Date(firstCachedTime.getTime());
	}

	/**
	 * @param firstCachedTime the firstCachedTime to set
	 */
	public void setFirstCachedTime(Date firstCachedTime) {
		this.firstCachedTime = firstCachedTime == null ? null : new Date(firstCachedTime.getTime());
	}

	/**
	 * @return the firstCachedTimeClass
	 */
	public Class<?> getFirstCachedTimeClass() {
		return firstCachedTimeClass;
	}

	/**
	 * @param firstCachedTimeClass the firstCachedTimeClass to set
	 */
	public void setFirstCachedTimeClass(Class<?> firstCachedTimeClass) {
		this.firstCachedTimeClass = firstCachedTimeClass;
	}

	/**
	 * @return the maxHitsCount
	 */
	public Integer getMaxHitsCount() {
		return maxHitsCount;
	}

	/**
	 * @param maxHitsCount the maxHitsCount to set
	 */
	public void setMaxHitsCount(Integer maxHitsCount) {
		this.maxHitsCount = maxHitsCount;
	}

	/**
	 * @return the maxHitsCountClass
	 */
	public Class<?> getMaxHitsCountClass() {
		return maxHitsCountClass;
	}

	/**
	 * @param maxHitsCountClass the maxHitsCountClass to set
	 */
	public void setMaxHitsCountClass(Class<?> maxHitsCountClass) {
		this.maxHitsCountClass = maxHitsCountClass;
	}

	/**
	 * @return the maxResponseTime
	 */
	public Long getMaxResponseTime() {
		return maxResponseTime;
	}

	/**
	 * @param maxResponseTime the maxResponseTime to set
	 */
	public void setMaxResponseTime(Long maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}

	/**
	 * @return the maxResponseTimeClass
	 */
	public Class<?> getMaxResponseTimeClass() {
		return maxResponseTimeClass;
	}

	/**
	 * @param maxResponseTimeClass the maxResponseTimeClass to set
	 */
	public void setMaxResponseTimeClass(Class<?> maxResponseTimeClass) {
		this.maxResponseTimeClass = maxResponseTimeClass;
	}

	/**
	 * @return the maxSavedTime
	 */
	public BigInteger getMaxSavedTime() {
		return maxSavedTime;
	}

	/**
	 * @param maxSavedTime the maxSavedTime to set
	 */
	public void setMaxSavedTime(BigInteger maxSavedTime) {
		this.maxSavedTime = maxSavedTime;
	}

	/**
	 * @return the maxSavedTimeClass
	 */
	public Class<?> getMaxSavedTimeClass() {
		return maxSavedTimeClass;
	}

	/**
	 * @param maxSavedTimeClass the maxSavedTimeClass to set
	 */
	public void setMaxSavedTimeClass(Class<?> maxSavedTimeClass) {
		this.maxSavedTimeClass = maxSavedTimeClass;
	}

	/**
	 * @return the reclaimedValueInCacheMapCount
	 */
	public Long getReclaimedValueInCacheMapCount() {
		return reclaimedValueInCacheMapCount;
	}

	/**
	 * @param reclaimedValueInCacheMapCount the reclaimedValueInCacheMapCount to set
	 */
	public void setReclaimedValueInCacheMapCount(Long reclaimedValueInCacheMapCount) {
		this.reclaimedValueInCacheMapCount = reclaimedValueInCacheMapCount;
	}

	/**
	 * @return the lockMapSize
	 */
	public Integer getLockMapSize() {
		return lockMapSize;
	}

	/**
	 * @param lockMapSize the lockMapSize to set
	 */
	public void setLockMapSize(Integer lockMapSize) {
		this.lockMapSize = lockMapSize;
	}
	
}
