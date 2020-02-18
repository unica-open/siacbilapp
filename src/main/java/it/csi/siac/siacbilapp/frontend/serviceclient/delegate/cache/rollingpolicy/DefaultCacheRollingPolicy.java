/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy;

/**
 * Default rolling policies.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 * 
 */
public enum DefaultCacheRollingPolicy {

	/** Consider the item as always stale. */
	ALWAYS(new AlwaysCacheRollingPolicy()),
	
	/** Consider the item as never stale. */
	NEVER(new NeverCacheRollingPolicy()),
	
	/** Consider the item as stale if it was hit 10 times. */
	HIT_10(new HitCountCacheRollingPolicy(10)),
	
	/** Consider the item as stale if it was hit 100 times. */
	HIT_100(new HitCountCacheRollingPolicy(100)),
	
	/** Consider the item as stale if it was hit 1000 times. */
	HIT_1000(new HitCountCacheRollingPolicy(1000)),
	
	/** Consider the item as stale if a day has past since the caching. */
	DAILY(new DailyCacheRollingPolicy()),
	
	/** Consider the item as stale if a week has past since the caching. */
	WEEKLY(new WeeklyCacheRollingPolicy()),
	
	/** Consider the item as stale if a month has past since the caching. */
	MONTHLY(new MonthlyCacheRollingPolicy()),

	/** Consider the item as stale if a year has past since the caching. */
	YEARLY(new YearlyCacheRollingPolicy()),
	;

	private transient CacheRollingPolicy rollingPolicy;

	private DefaultCacheRollingPolicy(CacheRollingPolicy rollingPolicy) {
		this.rollingPolicy = rollingPolicy;
	}

	/**
	 * @return the rollingPolicy
	 */
	public CacheRollingPolicy getRollingPolicy() {
		return rollingPolicy;
	}

}
