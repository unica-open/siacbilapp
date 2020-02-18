/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Documento di Entrata.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 06/02/2014
 * 
 */
public class RisultatiRicercaDocumentoEntrataModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5713758744308131312L;
	
	// Property necessarie per pilotare la dataTable con  il plugin json
	private int sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private String iDisplayStart;
	private String iDisplayLength;
	
	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private int uidDaAggiornare;
	private int uidDaAnnullare;
	private int uidDaConsultare;
	private int uidVersoFel;
	
	//SIAC-6352
	private ContoTesoreria contoTesoreria;
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();

	
	//per visualizzare i totali della pagina di ricerca
	private BigDecimal importoTotale = BigDecimal.ZERO;

	
	//per l'azione di consultaQuote
	private BigDecimal totaleQuote = BigDecimal.ZERO;

	private String riepilogoRicerca;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoEntrataModel() {
		super();
		setTitolo("Risultati di ricerca Documento Entrata");
	}

	/**
	 * @return the sEcho
	 */
	public int getsEcho() {
		return sEcho;
	}

	/**
	 * @param sEcho the sEcho to set
	 */
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * @return the iTotalRecords
	 */
	public String getiTotalRecords() {
		return iTotalRecords;
	}

	/**
	 * @param iTotalRecords the iTotalRecords to set
	 */
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	/**
	 * @return the iTotalDisplayRecords
	 */
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	/**
	 * @param iTotalDisplayRecords the iTotalDisplayRecords to set
	 */
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	/**
	 * @return the iDisplayStart
	 */
	public String getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * @param iDisplayStart the iDisplayStart to set
	 */
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * @return the iDisplayLength
	 */
	public String getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * @param iDisplayLength the iDisplayLength to set
	 */
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
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
		this.importoTotale = importoTotale;
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
		this.totaleQuote = totaleQuote;
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
	
	/**
	 * @return the uidVersoFel
	 */
	public int getUidVersoFel() {
		return this.uidVersoFel;
	}

	/**
	 * @param uidVersoFel the uidVersoFel to set
	 */
	public void setUidVersoFel(int uidVersoFel) {
		this.uidVersoFel = uidVersoFel;
	}

	/* Requests */

	/**
	 * Crea una request per il servizio di Annulla DocumentoEntrata.
	 * 
	 * @return la request creata
	 */
	public AnnullaDocumentoEntrata creaRequestAnnullaDocumentoEntrata() {
		AnnullaDocumentoEntrata request = creaRequest(AnnullaDocumentoEntrata.class);
		request.setBilancio(getBilancio());
		
		DocumentoEntrata documento = new DocumentoEntrata();
		documento.setUid(uidDaAnnullare);
		documento.setEnte(getEnte());
		
		request.setDocumentoEntrata(documento);
		
		return request;
	}
	
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
		
		request.setSubdocumentoEntrataModelDetails(
				SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,
				SubdocumentoEntrataModelDetail.AttoAmm,
				SubdocumentoEntrataModelDetail.Ordinativo,
				SubdocumentoEntrataModelDetail.ProvvisorioDiCassa);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AttivaRegistrazioniContabiliEntrata}.
	 * 
	 * @return la request creata
	 */
	public AttivaRegistrazioniContabiliEntrata creaRequestAttivaRegistrazioniContabiliEntrata() {
		AttivaRegistrazioniContabiliEntrata request = creaRequest(AttivaRegistrazioniContabiliEntrata.class);
		
		DocumentoEntrata documentoEntrata= new DocumentoEntrata();
		documentoEntrata.setUid(getUidDaAggiornare());
		// SIAC-4504
		documentoEntrata.setFlagDisabilitaRegistrazioneResidui(Boolean.FALSE);
		request.setDocumentoEntrata(documentoEntrata);
		request.setBilancio(getBilancio());
		return request;
	}

	/**
	 * SIAC-6565
	 * Crea una request per il servizio di {@link EmettiFatturaFelEntrata}.
	 * 
	 * @return la request creata
	 */
	public EmettiFatturaFelEntrata creaRequestEmettiFatturaFelEntrata() {
		EmettiFatturaFelEntrata request = creaRequest(EmettiFatturaFelEntrata.class);
		
		DocumentoEntrata documentoEntrata= new DocumentoEntrata();
		documentoEntrata.setUid(getUidVersoFel());
		request.setDocumentoEntrata(documentoEntrata);

		return request;
	}

	/**
	 * Creazione della request per la lettura dei conti di tesoreria
	 * @return la request creata
	 */
	public LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = new LeggiContiTesoreria();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}

	/**
	 * @return the contoTesoreria
	 */
	public ContoTesoreria getContoTesoreria() {
		return contoTesoreria;
	}

	/**
	 * @param contoTesoreria the contoTesoreria to set
	 */
	public void setContoTesoreria(ContoTesoreria contoTesoreria) {
		this.contoTesoreria = contoTesoreria;
	}

	/**
	 * @return the listaContoTesoreria
	 */
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria;
	}

	/**
	 * @param listaContoTesoreria the listaContoTesoreria to set
	 */
	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria;
	}


    
}
