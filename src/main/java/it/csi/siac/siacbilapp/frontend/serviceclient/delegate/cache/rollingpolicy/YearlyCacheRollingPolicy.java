/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy;

import java.util.Calendar;
import java.util.Date;

/**
 * A CacheRollingPolicy which marks data as stale yearly.
 * 
 * @author Domenico
 * @version 1.0.0 - 01/10/2014
 *
 */
public class YearlyCacheRollingPolicy extends CalendarCacheRollingPolicy {

	@Override
	public boolean isExpired(Date data, int hitCount) {
		return isExpiredDate(data, Calendar.YEAR);
	}

}
