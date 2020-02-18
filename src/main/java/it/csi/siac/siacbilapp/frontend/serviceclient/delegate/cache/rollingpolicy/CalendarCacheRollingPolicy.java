/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.rollingpolicy;

import java.util.Calendar;
import java.util.Date;

/**
 * A CacheRollingPolicy which checks staleness via a Calendar.
 * 
 * @author Domenico
 * @version 1.0.0 - 01/10/2014
 */
public abstract class CalendarCacheRollingPolicy implements CacheRollingPolicy {

	/**
	 * Checks whether the date makes the cached data stale.
	 *
	 * @param data           the cached date
	 * @param calendarFields the calendar fields to check
	 * 
	 * @return true, if the date is expired
	 */
	protected boolean isExpiredDate(Date data, int... calendarFields) {
		Calendar cacheDate = Calendar.getInstance();
		cacheDate.setTime(data);
		Calendar now = Calendar.getInstance();
		
		for(int calendarField : calendarFields){
			int cf = now.get(calendarField);
			int cfCache = cacheDate.get(calendarField);

			if (cfCache < cf) {
				return true;
			}
		}

		return false;
	}
}
