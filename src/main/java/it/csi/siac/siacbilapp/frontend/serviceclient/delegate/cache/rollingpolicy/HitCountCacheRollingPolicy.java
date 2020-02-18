/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy;

import java.util.Date;

/**
 * A CacheRollingPolicy based on hit count.
 * 
 * @author Domenico
 * @version 1.0.0 - 01/10/2014
 */
public class HitCountCacheRollingPolicy implements CacheRollingPolicy {
	
	private final int hitCountMax;
	
	/**
	 * Instantiates a new policy with the given maximum number of hits before considering the data as stale.
	 *
	 * @param hitCountMax the maximum hit count
	 */
	public HitCountCacheRollingPolicy(int hitCountMax) {
		super();
		this.hitCountMax = hitCountMax;
	}

	@Override
	public boolean isExpired(Date data, int hitCount) {
		return hitCount > hitCountMax;
	}

}
