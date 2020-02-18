/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.result;

import org.apache.struts2.dispatcher.StreamResult;

/**
 * Result per lo stream della stampa
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/04/2017
 */
public class StampaStreamResult extends StreamResult {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1821334476671759764L;

	/** Costruttore di default */
	public StampaStreamResult() {
		setContentType("${contentType}");
		setContentLength("${contentLength}");
		setInputName("inputStream");
		setContentDisposition("filename=\"${fileName}\"");
		setBufferSize(1024);
	}
	
}
