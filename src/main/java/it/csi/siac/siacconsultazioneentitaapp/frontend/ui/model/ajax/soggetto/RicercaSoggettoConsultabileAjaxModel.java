/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.soggetto;

import java.util.Locale;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.RicercaEntitaConsultabileBaseAjaxModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaSoggettoConsultabile;

/**
 * Classe di model per la ricerca del soggetto consultabile (cruscottino) come entit&agrave di partenza
 * @author Elisa Chiari
 * @version 1.0.0 - 02/03/2016
 *
 */
public class RicercaSoggettoConsultabileAjaxModel extends RicercaEntitaConsultabileBaseAjaxModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7902311124334723842L;
	private String codiceSoggetto;
	private String denominazioneSoggetto;
	private String codiceFiscaleSoggetto;
	private String partitaIvaSoggetto;
	
	
	/** Costruttore */
	public RicercaSoggettoConsultabileAjaxModel() {
		super();
		setTitolo("Ricerca Soggetto Consultabile - AJAX");
	}

	/**
	 * @return the request ottieniElencoEntitaDiPartenzaCapitoloEntrataConsultabile
	 */
	public RicercaSinteticaEntitaConsultabile creaRequestRicercaSinteticaEntitaConsultabile() {
		RicercaSinteticaEntitaConsultabile request = new RicercaSinteticaEntitaConsultabile();
		ParametriRicercaSoggettoConsultabile parametriRicercaEntitaConsultabile = new ParametriRicercaSoggettoConsultabile();
		
		parametriRicercaEntitaConsultabile.setCodiceSoggetto(codiceSoggetto);
		parametriRicercaEntitaConsultabile.setDenominazione(denominazioneSoggetto);
		parametriRicercaEntitaConsultabile.setCodiceFiscale(codiceFiscaleSoggetto.toUpperCase(Locale.ITALY));
		parametriRicercaEntitaConsultabile.setPartitaIVA(partitaIvaSoggetto);
		
		request.setParametriRicercaEntitaConsultabile(parametriRicercaEntitaConsultabile);
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	/**
	 * @return the codiceSoggetto
	 */
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	/**
	 * @param codiceSoggetto the codiceSoggetto to set
	 */
	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}

	/**
	 * @param denominazioneSoggetto the denominazioneSoggetto to set
	 */
	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}

	/**
	 * @return the codiceFiscaleSoggetto
	 */
	public String getCodiceFiscaleSoggetto() {
		return codiceFiscaleSoggetto;
	}

	/**
	 * @param codiceFiscaleSoggetto the codiceFiscaleSoggetto to set
	 */
	public void setCodiceFiscaleSoggetto(String codiceFiscaleSoggetto) {
		this.codiceFiscaleSoggetto = codiceFiscaleSoggetto;
	}

	/**
	 * @return the partitaIvaSoggetto
	 */
	public String getPartitaIvaSoggetto() {
		return partitaIvaSoggetto;
	}

	/**
	 * @param partitaIvaSoggetto the partitaIvaSoggetto to set
	 */
	public void setPartitaIvaSoggetto(String partitaIvaSoggetto) {
		this.partitaIvaSoggetto = partitaIvaSoggetto;
	}

}
