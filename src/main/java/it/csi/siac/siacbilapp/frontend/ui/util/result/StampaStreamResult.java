/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.result;

import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.struts2.result.StreamResult;

/**
 * Result per lo stream della stampa
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/04/2017
 */
public class StampaStreamResult extends StreamResult {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1821334476671759764L;
	private Set<Cookie> cookies;

	/** Costruttore di default */
	public StampaStreamResult() {
		setContentType("${contentType}");
		setContentLength("${contentLength}");
		setInputName("inputStream");
		setContentDisposition("filename=\"${fileName}\"");
		setBufferSize(1024);
	}

	/**
	 * @return the cookies
	 */
	public Set<Cookie> getCookies() {
		return this.cookies;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(Set<Cookie> cookies) {
		this.cookies = cookies;
	}
}
