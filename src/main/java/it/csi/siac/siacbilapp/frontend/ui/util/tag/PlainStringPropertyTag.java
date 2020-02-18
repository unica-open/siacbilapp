/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.tag;

import org.apache.struts2.views.jsp.PropertyTag;

/**
 * Extension to Struts2's $lt;s:property /&gt; that shows the datum as a plain string
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/02/2019
 *
 */
public class PlainStringPropertyTag extends PropertyTag {
	
	/** For serialization */
	private static final long serialVersionUID = -2792857448750998322L;
	private static final String TO_PLAIN_STRING = ".toPlainString()";
	

	@Override
	public void setValue(String value) {
		if(value != null && value.contains(TO_PLAIN_STRING)) {
			super.setValue(value);
			return;
		}
		super.setValue(value + TO_PLAIN_STRING);
	}

}
