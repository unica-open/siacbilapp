/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;

/**
 * Classe racchiudente i campi comuni dei form di specifica delle variazioni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 24/10/2013
 *
 */
public abstract class SpecificaVariazioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 563332368620192983L;
	
	// Per il ripopolamento del form di ricerca
	private Integer annoCapitolo;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	
	// Campi ottenuti dal servizio
	private Integer numeroVariazione;
	private String note;
	
	// Abilitazioni dell'utente
	private Boolean utenteAbilitatoAdInserimento = Boolean.FALSE;
	private Boolean utenteAbilitatoAdAnnullamento = Boolean.FALSE;
	//SIAC-8332
	private StatoOperativoVariazioneBilancio statoVariazione;
	
	
	/**
	 * @return the annoCapitolo
	 */
	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}

	/**
	 * @param annoCapitolo the annoCapitolo to set
	 */
	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}

	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}

	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	/**
	 * @return the numeroVariazione
	 */
	public Integer getNumeroVariazione() {
		return numeroVariazione;
	}

	/**
	 * @param numeroVariazione the numeroVariazione to set
	 */
	public void setNumeroVariazione(Integer numeroVariazione) {
		this.numeroVariazione = numeroVariazione;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the utenteAbilitatoAdInserimento
	 */
	public Boolean getUtenteAbilitatoAdInserimento() {
		return utenteAbilitatoAdInserimento;
	}

	/**
	 * @param utenteAbilitatoAdInserimento the utenteAbilitatoAdInserimento to set
	 */
	public void setUtenteAbilitatoAdInserimento(Boolean utenteAbilitatoAdInserimento) {
		this.utenteAbilitatoAdInserimento = utenteAbilitatoAdInserimento;
	}

	/**
	 * @return the utenteAbilitatoAdAnnullamento
	 */
	public Boolean getUtenteAbilitatoAdAnnullamento() {
		return utenteAbilitatoAdAnnullamento;
	}

	/**
	 * @param utenteAbilitatoAdAnnullamento the utenteAbilitatoAdAnnullamento to set
	 */
	public void setUtenteAbilitatoAdAnnullamento(
			Boolean utenteAbilitatoAdAnnullamento) {
		this.utenteAbilitatoAdAnnullamento = utenteAbilitatoAdAnnullamento;
	}

	public StatoOperativoVariazioneBilancio getStatoVariazione() {
		return statoVariazione;
	}

	public void setStatoVariazione(StatoOperativoVariazioneBilancio statoVariazione) {
		this.statoVariazione = statoVariazione;
	}
	
	
	
}
