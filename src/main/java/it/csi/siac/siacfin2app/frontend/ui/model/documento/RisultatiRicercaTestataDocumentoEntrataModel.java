/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * Model per la visualizzazione dei risultati di ricerca per la testata del Documento di Entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/07/2014
 * 
 */
public class RisultatiRicercaTestataDocumentoEntrataModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4824119912734047977L;

	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private int uidDaAggiornare;
	private int uidDaAnnullare;
	private int uidDaConsultare;
	
	//per visualizzare i totali della pagina di ricerca
	private BigDecimal importoTotale = BigDecimal.ZERO;

	//per l'azione di consultaQuote
	private BigDecimal totaleQuote = BigDecimal.ZERO;

	private String riepilogoRicerca;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTestataDocumentoEntrataModel() {
		super();
		setTitolo("Risultati di ricerca Documento iva Entrata");
	}

	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}

	/**
	 * @return the uidDaAggiornare
	 */
	public int getUidDaAggiornare() {
		return uidDaAggiornare;
	}

	/**
	 * @param uidDaAggiornare the uidDaAggiornare to set
	 */
	public void setUidDaAggiornare(int uidDaAggiornare) {
		this.uidDaAggiornare = uidDaAggiornare;
	}

	/**
	 * @return the uidDaAnnullare
	 */
	public int getUidDaAnnullare() {
		return uidDaAnnullare;
	}

	/**
	 * @param uidDaAnnullare the uidDaAnnullare to set
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		this.uidDaAnnullare = uidDaAnnullare;
	}

	/**
	 * @return the uidDaConsultare
	 */
	public int getUidDaConsultare() {
		return uidDaConsultare;
	}

	/**
	 * @param uidDaConsultare the uidDaConsultare to set
	 */
	public void setUidDaConsultare(int uidDaConsultare) {
		this.uidDaConsultare = uidDaConsultare;
	}
	
	/**
	 * @return the importoTotale
	 */
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	/**
	 * @param importoTotale the importoTotale to set
	 */
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale != null ? importoTotale : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleQuote
	 */
	public BigDecimal getTotaleQuote() {
		return totaleQuote;
	}

	/**
	 * @param totaleQuote the totaleQuote to set
	 */
	public void setTotaleQuote(BigDecimal totaleQuote) {
		this.totaleQuote = totaleQuote != null ? totaleQuote : BigDecimal.ZERO;
	}

	/**
	 * @return the riepilogoRicerca
	 */
	public String getRiepilogoRicerca() {
		return riepilogoRicerca;
	}

	/**
	 * @param riepilogoRicerca the riepilogoRicerca to set
	 */
	public void setRiepilogoRicerca(String riepilogoRicerca) {
		this.riepilogoRicerca = riepilogoRicerca;
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoEntrata creaRequestRicercaSinteticaQuoteByDocumentoEntrata() {
		RicercaSinteticaModulareQuoteByDocumentoEntrata request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoEntrata.class);
		
		DocumentoEntrata documento = new DocumentoEntrata();
		documento.setUid(uidDaConsultare);
		documento.setEnte(getEnte());
		request.setDocumentoEntrata(documento);
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		return request;
	}
	
}
