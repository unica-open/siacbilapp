/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy;

import java.util.Date;

/**
 * Rolling policy for cached data.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 */
public interface CacheRollingPolicy {
	
	/**
	 * Checks whether the cached item is stale.
	 *
	 * @param cacheDate the date in which the item was cached
	 * @param hitCount  the number of times the cached item was used
	 * 
	 * @return true if the policy considers the cached value as stale
	 */
	boolean isExpired(Date cacheDate, int hitCount);

}
