/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta;

import java.io.Serializable;

/**
 * Abilitazione delle sottosezioni della richiesta per la cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/12/2014
 *
 */
public class AbilitazioneRichiestaCassaEconomale implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5508386078087254308L;
	
	private String url;
	private String nome;
	private boolean abilitato;
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the abilitato
	 */
	public boolean isAbilitato() {
		return abilitato;
	}
	/**
	 * @param abilitato the abilitato to set
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	
}
