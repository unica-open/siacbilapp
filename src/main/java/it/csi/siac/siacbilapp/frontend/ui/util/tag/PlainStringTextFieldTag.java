/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.tag;

import org.apache.struts2.views.jsp.ui.TextFieldTag;

/**
 * Extension to Struts2's $lt;s:textfield /&gt; that shows the datum as a plain string
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/02/2019
 *
 */
public class PlainStringTextFieldTag extends TextFieldTag {
	
	/** For serialization */
	private static final long serialVersionUID = 1058037380698428469L;
	private static final String TO_PLAIN_STRING = ".toPlainString()";

	@Override
	public void setName(String name) {
		super.setName(name);
		setValue("%{" + name + TO_PLAIN_STRING + "}");
	}
}
