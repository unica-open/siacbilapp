/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.tag;

import org.apache.struts2.views.jsp.ui.HiddenTag;

/**
 * Extension to Struts2's $lt;s:hidden /&gt; that shows the datum as a plain string
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/02/2019
 *
 */
public class PlainStringHiddenTag extends HiddenTag {
	
	/** For serialization */
	private static final long serialVersionUID = -4312902082330987437L;
	private static final String TO_PLAIN_STRING = ".toPlainString()";

	@Override
	public void setName(String name) {
		super.setName(name);
		setValue("%{" + name + TO_PLAIN_STRING + "}");
	}
}
