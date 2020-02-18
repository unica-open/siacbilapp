/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Ordine;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;

/**
 * Model per la visualizzazione dei risultati di ricerca per il Documento di Spesa.
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 06/02/2014
 * @author Marchino Alessandro
 * @version 1.1.0 - 21/09/2015
 * 
 */
public class RisultatiRicercaDocumentoSpesaModel extends GenericBilancioModel {

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

	//per visualizzare i totali della pagina di ricerca
	private BigDecimal importoTotale = BigDecimal.ZERO;

	private String riepilogoRicerca;
	
	// Lotto M
	private List<Ordine> listaOrdine = new ArrayList<Ordine>();
	private Ordine ordine;
	// SIAC-3965
	private BigDecimal totaleQuote = BigDecimal.ZERO;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaDocumentoSpesaModel() {
		super();
		setTitolo("Risultati di ricerca Documento Spesa");
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
	 * @return the listaOrdine
	 */
	public List<Ordine> getListaOrdine() {
		return listaOrdine;
	}

	/**
	 * @param listaOrdine the listaOrdine to set
	 */
	public void setListaOrdine(List<Ordine> listaOrdine) {
		this.listaOrdine = listaOrdine != null ? listaOrdine : new ArrayList<Ordine>();
	}
	
	/**
	 * @return the ordine
	 */
	public Ordine getOrdine() {
		return ordine;
	}

	/**
	 * @param ordine the ordine to set
	 */
	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
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
	
	/* Requests */
	
	/**
	 * Crea una request per il servizio di Annulla DocumentoSpesa.
	 * 
	 * @return la request creata
	 */
	public AnnullaDocumentoSpesa creaRequestAnnullaDocumentoSpesa() {
		AnnullaDocumentoSpesa request = creaRequest(AnnullaDocumentoSpesa.class);
		request.setBilancio(getBilancio());
		
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setUid(uidDaAnnullare);
		documento.setEnte(getEnte());
		
		request.setDocumentoSpesa(documento);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoSpesa creaRicercaSinteticaModulareQuoteByDocumentoSpesa() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoSpesa.class);
		
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setUid(uidDaConsultare);
		documento.setEnte(getEnte());
		request.setDocumentoSpesa(documento);
		
		request.setParametriPaginazione(creaParametriPaginazione(5));
		request.setSubdocumentoSpesaModelDetails(
				SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,
				SubdocumentoSpesaModelDetail.AttoAmm,
				SubdocumentoSpesaModelDetail.Liquidazione,
				SubdocumentoSpesaModelDetail.Ordinativo);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaOrdiniDocumento}.
	 * 
	 * @return la request creata
	 */
	public RicercaOrdiniDocumento creaRequestRicercaOrdiniDocumento() {
		RicercaOrdiniDocumento request = creaRequest(RicercaOrdiniDocumento.class);
		
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setUid(getUidDaConsultare());
		request.setDocumento(documento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaOrdine}.
	 * 
	 * @return la request creata
	 */
	public AggiornaOrdine creaRequestAggiornaOrdine() {
		AggiornaOrdine request = creaRequest(AggiornaOrdine.class);
		request.setOrdine(getOrdine());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaOrdine}.
	 * 
	 * @return la request creata
	 */
	public EliminaOrdine creaRequestEliminaOrdine() {
		EliminaOrdine request = creaRequest(EliminaOrdine.class);
		request.setOrdine(getOrdine());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceOrdine}.
	 * 
	 * @return la request creata
	 */
	public InserisceOrdine creaRequestInserisceOrdine() {
		InserisceOrdine request = creaRequest(InserisceOrdine.class);
		request.setOrdine(getOrdine());
		return request;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link AttivaRegistrazioniContabiliSpesa}.
	 * 
	 * @return la request creata
	 */
	public AttivaRegistrazioniContabiliSpesa creaRequestAttivaRegistrazioniContabiliSpesa() {
		AttivaRegistrazioniContabiliSpesa request = creaRequest(AttivaRegistrazioniContabiliSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(getUidDaAggiornare());
		// SIAC-4501
		documentoSpesa.setFlagDisabilitaRegistrazioneResidui(Boolean.FALSE);
		request.setDocumentoSpesa(documentoSpesa);
		request.setBilancio(getBilancio());
		
		return request;
	}

}
