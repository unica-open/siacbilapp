/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import java.io.Serializable;

/**
 * Classe di model generica per le interazioni AJAX per i risultati di ricerca
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/01/2014
 *
 * @param <S> i dati ottenuti per l'AJAX, implementanti {@link Serializable}
 */
public abstract class PagedDataTableAjaxModel<S extends Serializable> extends BaseDataTableAjaxModel<S> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1907325698021102792L;
	
	private int iDisplayStart;
	private int iDisplayLength;
	
	/**
	 * @return the iDisplayStart
	 */
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	
	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	
	/**
	 * @return the iDisplayLength
	 */
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	
	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

}
