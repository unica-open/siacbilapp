/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.result;


/**
 * Result for working with JSON data. Gives sensible defaults for some of the properties, and returns only errors, warnings and infos.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 *
 */
public class CustomMessagesJSONResult extends CustomJSONResult {
	
	/** For serialization purpose */
	private static final long serialVersionUID = 5007655031236727633L;

	/** Empty default constructor */
	public CustomMessagesJSONResult() {
		super();
		setIncludeProperties("errori.*, messaggi.*, informazioni.*");
	}
	
}
