/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;

/**
 * Classe di wrap per il Progetto.

 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 05/02/2014
 *
 */
public class ElementoProgetto implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7955007686452616649L;
	
	private int uid;
	private String codice;
	private String descrizione;
	private String statoOperativoProgetto;
	private String provvedimento;
	private String ambito;
	
	private String codiceTipoProgetto;
	private String valoreComplessivo;
	
	private String azioni;
	
	/** Costruttore vuoto di default */
	public ElementoProgetto() {
		super();
	}

	
	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}


	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the statoOperativoProgetto
	 */
	public String getStatoOperativoProgetto() {
		return statoOperativoProgetto;
	}

	/**
	 * @param statoOperativoProgetto the statoOperativoProgetto to set
	 */
	public void setStatoOperativoProgetto(
			String statoOperativoProgetto) {
		this.statoOperativoProgetto = statoOperativoProgetto;
	}

	/**
	 * @return the provvedimento
	 */
	public String getProvvedimento() {
		return provvedimento;
	}

	/**
	 * @param provvedimento the provvedimento to set
	 */
	public void setProvvedimento(String provvedimento) {
		this.provvedimento = provvedimento;
	}

	/**
	 * @return the ambito
	 */
	public String getAmbito() {
		return ambito;
	}

	/**
	 * @param ambito the ambito to set
	 */
	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	/**
	 * @return the codiceTipoProgetto
	 */
	public String getCodiceTipoProgetto() {
		return codiceTipoProgetto;
	}


	/**
	 * @param codiceTipoProgetto the codiceTipoProgetto to set
	 */
	public void setCodiceTipoProgetto(String codiceTipoProgetto) {
		this.codiceTipoProgetto = codiceTipoProgetto;
	}


	/**
	 * Controlla se il progetto &eacute; in stato valido.
	 * 
	 * @return <code>true</code> se lo stato operativo del progetto &eacute; pari a VALIDO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoValido() {
		return StatoOperativoProgetto.VALIDO.name().equalsIgnoreCase(statoOperativoProgetto);
	}
	


	/**
	 * Controlla se il progetto &eacute; in stato annullato.
	 * 
	 * @return <code>true</code> se lo stato operativo del progetto &eacute; pari a ANNULLATO; <code>false</code> altrimenti
	 */
	public boolean checkStatoOperativoAnnullato() {
		return StatoOperativoProgetto.ANNULLATO.name().equalsIgnoreCase(statoOperativoProgetto);
	}


	public String getValoreComplessivo() {
		return valoreComplessivo;
	}


	public void setValoreComplessivo(String valoreComplessivo) {
		this.valoreComplessivo = valoreComplessivo;
	}



	
}
