/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.threadlocal;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread local per una mappa di date format
 * @author Marchino Alessandro
 *
 */
public class DateFormatMapThreadLocal extends ThreadLocal<Map<String, DateFormat>> {

	@Override
	protected Map<String, DateFormat> initialValue() {
		return new HashMap<String, DateFormat>();
	}

}
