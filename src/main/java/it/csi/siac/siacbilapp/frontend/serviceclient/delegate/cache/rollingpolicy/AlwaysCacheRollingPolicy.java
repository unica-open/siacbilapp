/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy;

import java.util.Date;

/**
 * A CacheRollingPolicy which always marks a cached data as stale.
 */
public class AlwaysCacheRollingPolicy implements CacheRollingPolicy {

	@Override
	public boolean isExpired(Date data, int hitCount) {
		return true;
	}

}
