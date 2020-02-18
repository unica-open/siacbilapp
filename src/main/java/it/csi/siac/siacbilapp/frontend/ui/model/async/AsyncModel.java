/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.async;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;

/**
 * Classe di model per le azioni asincrone.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 07/06/2014
 *
 */
public class AsyncModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2374019419784517469L;
	
	private Integer idOperazioneAsync;
	
	private String messaggio;
	private String stato;
	
	/** Costruttore vuoto di default */
	public AsyncModel() {
		super();
	}
	
	/**
	 * @return the idOperazioneAsync
	 */
	public Integer getIdOperazioneAsync() {
		return idOperazioneAsync;
	}

	/**
	 * @param idOperazioneAsync the idOperazioneAsync to set
	 */
	public void setIdOperazioneAsync(Integer idOperazioneAsync) {
		this.idOperazioneAsync = idOperazioneAsync;
	}

	/**
	 * @return the messaggio
	 */
	public String getMessaggio() {
		return messaggio;
	}

	/**
	 * @param messaggio the messaggio to set
	 */
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}
	
}
