/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.servicecache;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CacheStatistics;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;

/**
 * Model class for interacting with the cache.
 * 
 * @author Marchino Alessandro
 */
public class ServiceCacheUtilityModel extends GenericBilancioModel {

	/** For serialization purpose */
	private static final long serialVersionUID = 6211927798486315720L;

	/** Separator for string composition */
	private static final String SEPARATOR = "\n";

	/** The string corresponding to the statistics */
	private String statistics;
	/** Whether the data acquisition should be accurate */
	private Boolean accurate;
	/** The password to use */
	private String password;
	/** The key to use to clear the data */
	private String key;
	/** The keys in cache */
	private Set<String> keys = new HashSet<String>();
	
	/** Costruttore vuoto di default */
	public ServiceCacheUtilityModel() {
		setTitolo("Service cache");
	}
	
	/**
	 * @return the statistics
	 */
	public String getStatistics() {
		return statistics;
	}
	/**
	 * @param statistics the statistics to set
	 */
	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}
	/**
	 * @return the accurate
	 */
	public Boolean getAccurate() {
		return accurate;
	}

	/**
	 * @param accurate the accurate to set
	 */
	public void setAccurate(Boolean accurate) {
		this.accurate = accurate;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the keys
	 */
	public Set<String> getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}

	/**
	 * Computes the cache statistics.
	 * @param cs the cache statistics which must be transformed into a message
	 */
	public void computeStatistics(CacheStatistics cs) {
		final StringBuilder sb = new StringBuilder();
		
		// Default rolling policy
		appendData(sb, true, "Default rolling policy: ", cs.getCacheRollingPolicy());
		// Cache size
		appendData(sb, true, "Cache size: ", cs.getCacheSize());
		// HitsCount
		appendData(sb, true, "Total hits count: ", cs.getHitsCount());
		appendData(sb, true, "Total hits count which hit cached data: ", cs.getCachedHitsCount());
		appendData(sb, true, "Total hits count which missed cached data: ", cs.getMissedHitsCount());
		
		// Max
		appendData(sb, cs.getMaxHitsCountClass() != null, "Response with maximum hit count: ", cs.getMaxHitsCountClass(), " with ", cs.getMaxHitsCount(), " hits");
		appendData(sb, cs.getMaxResponseTimeClass() != null, "Response with longest response time: ", cs.getMaxResponseTimeClass(), " with ", cs.getMaxResponseTime(), " ms");
		// Saved times
		appendData(sb, true, "Total saved time: ", cs.getSavedTime().toString(), " ms");
		appendData(sb, cs.getMaxSavedTimeClass() != null, "Response with maximum saved time: ", cs.getMaxSavedTimeClass(), " with ", cs.getMaxSavedTime(), " ms saved");
		// First and last
		appendData(sb, cs.getLastCachedTimeClass() != null, "Last cached data: ", cs.getLastCachedTimeClass(), " in date ", cs.getLastCachedTime());
		appendData(sb, cs.getFirstCachedTimeClass() != null, "First cached data: ", cs.getFirstCachedTimeClass(), " in date ", cs.getFirstCachedTime());
		
		// Sizes
		appendData(sb, cs.getReclaimedValueInCacheMapCount() != null, "Number of cached values reclaimed by Garbage Collector: ", cs.getReclaimedValueInCacheMapCount());
//		appendData(sb, true, cs.getReclaimedValueInCacheMapCount() != null, "Number of cached values reclaimed by Garbage Collector: ", cs.getReclaimedValueInCacheMapCount());
		appendData(sb, cs.getLockMapSize() != null, "Size of the locks map: ", cs.getLockMapSize());
		
		setStatistics(sb.toString());
	}
	
	/**
	 * Appends data to the string builder
	 * @param sb the string builder to populate
	 * @param condition the condition which must be respected for the data to be added
	 * @param pieces the pieces of data
	 */
	private void appendData(StringBuilder sb, boolean condition, Object... pieces) {
		if(!condition) {
			return;
		}
		for(Object chunk : pieces) {
			if(chunk instanceof Date) {
				sb.append(FormatUtils.formatDate((Date) chunk));
			} else {
				sb.append(chunk);
			}
		}
		sb.append(SEPARATOR);
	}
}
