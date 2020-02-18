/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
 * Classe di model per la consultazione del Documento di Entrata
 * 
 * @author Ahmad Nazha, Valentina Triolo
 * @version 1.0.0 - 21/03/2014
 * 
 */
public class ConsultaDocumentoEntrataModel extends GenericDocumentoModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7159559274581585054L;

	private Integer uidDocumento;
	private DocumentoEntrata documento;
	
	private BigDecimal totaleQuote = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaAttribuire = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaDedurre = BigDecimal.ZERO;
	private BigDecimal totaleImportoDocumentiCollegati = BigDecimal.ZERO;
	
	private List<SubdocumentoEntrata> listaQuote = new ArrayList<SubdocumentoEntrata>();
	private List<ElementoDocumentoCollegato> listaDocumentiCollegati = new ArrayList<ElementoDocumentoCollegato>();

	private String idWorkaround;
	private Boolean flagDatiIvaAccessibile = Boolean.TRUE;
	private Boolean registrazioneSuSingolaQuota = Boolean.FALSE;
	
	private SubdocumentoEntrata subdocumentoEntrata;
	
	private Integer uidQuotaIvaDifferita;
	private SubdocumentoIvaEntrata quotaIvaDifferita;
	
	// SIAC-3954
	private Integer uidDocumentoSpesa;
	private Integer uidDocumentoPerRicerche;
	private Integer numeroQuote;
	private BigDecimal totaleQuoteSpesa = BigDecimal.ZERO;
	private DocumentoEntrata notaCredito;
	private boolean listaQuoteCaricata;
	private boolean listaQuoteIvaCaricata;
	private boolean listaOnereCaricata;
	private boolean listaDocumentiCollegatiCaricata;

	/** Costruttore vuoto di default */
	public ConsultaDocumentoEntrataModel(){
		setTitolo("Consultazione Documenti di Entrata");
	}
	
	/**
	 * @return the uidDocumento
	 */
	public Integer getUidDocumento() {
		return uidDocumento;
	}

	/**
	 * @param uidDocumento the uidDocumento to set
	 */
	public void setUidDocumento(Integer uidDocumento) {
		this.uidDocumento = uidDocumento;
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
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
	 * @return the totaleImportoDaAttribuire
	 */
	public BigDecimal getTotaleImportoDaAttribuire() {
		return totaleImportoDaAttribuire;
	}

	/**
	 * @param totaleImportoDaAttribuire the totaleImportoDaAttribuire to set
	 */
	public void setTotaleImportoDaAttribuire(BigDecimal totaleImportoDaAttribuire) {
		this.totaleImportoDaAttribuire = totaleImportoDaAttribuire != null ? totaleImportoDaAttribuire : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleImportoDaDedurre
	 */
	public BigDecimal getTotaleImportoDaDedurre() {
		return totaleImportoDaDedurre;
	}

	/**
	 * @param totaleImportoDaDedurre the totaleImportoDaDedurre to set
	 */
	public void setTotaleImportoDaDedurre(BigDecimal totaleImportoDaDedurre) {
		this.totaleImportoDaDedurre = totaleImportoDaDedurre != null ? totaleImportoDaDedurre : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleImportoDocumentiCollegati
	 */
	public BigDecimal getTotaleImportoDocumentiCollegati() {
		return totaleImportoDocumentiCollegati;
	}

	/**
	 * @param totaleImportoDocumentiCollegati the totaleImportoDocumentiCollegati to set
	 */
	public void setTotaleImportoDocumentiCollegati(BigDecimal totaleImportoDocumentiCollegati) {
		this.totaleImportoDocumentiCollegati = totaleImportoDocumentiCollegati != null ? totaleImportoDocumentiCollegati : BigDecimal.ZERO;
	}

	/**
	 * @return the listaQuote
	 */
	public List<SubdocumentoEntrata> getListaQuote() {
		return listaQuote;
	}

	/**
	 * @param listaQuote the listaQuote to set
	 */
	public void setListaQuote(List<SubdocumentoEntrata> listaQuote) {
		this.listaQuote = listaQuote != null ? listaQuote : new ArrayList<SubdocumentoEntrata>();
	}

	/**
	 * @return the listaDocumentiCollegati
	 */
	public List<ElementoDocumentoCollegato> getListaDocumentiCollegati() {
		return listaDocumentiCollegati;
	}

	/**
	 * @param listaDocumentiCollegati the listaDocumentiCollegati to set
	 */
	public void setListaDocumentiCollegati(List<ElementoDocumentoCollegato> listaDocumentiCollegati) {
		this.listaDocumentiCollegati = listaDocumentiCollegati != null ? listaDocumentiCollegati : new ArrayList<ElementoDocumentoCollegato>();
	}

	/**
	 * @return the idWorkaround
	 */
	public String getIdWorkaround() {
		return idWorkaround;
	}

	/**
	 * @param idWorkaround the idWorkaround to set
	 */
	public void setIdWorkaround(String idWorkaround) {
		this.idWorkaround = idWorkaround;
	}

	/**
	 * @return the flagDatiIvaAccessibile
	 */
	public Boolean getFlagDatiIvaAccessibile() {
		return flagDatiIvaAccessibile;
	}

	/**
	 * @param flagDatiIvaAccessibile the flagDatiIvaAccessibile to set
	 */
	public void setFlagDatiIvaAccessibile(Boolean flagDatiIvaAccessibile) {
		this.flagDatiIvaAccessibile = flagDatiIvaAccessibile != null ? flagDatiIvaAccessibile : Boolean.FALSE;
	}

	/**
	 * @return the registrazioneSuSingolaQuota
	 */
	public Boolean getRegistrazioneSuSingolaQuota() {
		return registrazioneSuSingolaQuota;
	}

	/**
	 * @param registrazioneSuSingolaQuota the registrazioneSuSingolaQuota to set
	 */
	public void setRegistrazioneSuSingolaQuota(Boolean registrazioneSuSingolaQuota) {
		this.registrazioneSuSingolaQuota = registrazioneSuSingolaQuota != null ? registrazioneSuSingolaQuota : Boolean.FALSE;
	}

	/**
	 * @return the subdocumentoEntrata
	 */
	public SubdocumentoEntrata getSubdocumentoEntrata() {
		return subdocumentoEntrata;
	}

	/**
	 * @param subdocumentoEntrata the subdocumentoEntrata to set
	 */
	public void setSubdocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		this.subdocumentoEntrata = subdocumentoEntrata;
	}

	/**
	 * @return the uidQuotaIvaDifferita
	 */
	public Integer getUidQuotaIvaDifferita() {
		return uidQuotaIvaDifferita;
	}

	/**
	 * @param uidQuotaIvaDifferita the uidQuotaIvaDifferita to set
	 */
	public void setUidQuotaIvaDifferita(Integer uidQuotaIvaDifferita) {
		this.uidQuotaIvaDifferita = uidQuotaIvaDifferita;
	}

	/**
	 * @return the quotaIvaDifferita
	 */
	public SubdocumentoIvaEntrata getQuotaIvaDifferita() {
		return quotaIvaDifferita;
	}

	/**
	 * @param quotaIvaDifferita the quotaIvaDifferita to set
	 */
	public void setQuotaIvaDifferita(SubdocumentoIvaEntrata quotaIvaDifferita) {
		this.quotaIvaDifferita = quotaIvaDifferita;
	}

	/**
	 * @return the uidDocumentoSpesa
	 */
	public Integer getUidDocumentoSpesa() {
		return uidDocumentoSpesa;
	}

	/**
	 * @param uidDocumentoSpesa the uidDocumentoSpesa to set
	 */
	public void setUidDocumentoSpesa(Integer uidDocumentoSpesa) {
		this.uidDocumentoSpesa = uidDocumentoSpesa;
	}

	/**
	 * @return the uidDocumentoPerRicerche
	 */
	public Integer getUidDocumentoPerRicerche() {
		return uidDocumentoPerRicerche;
	}

	/**
	 * @param uidDocumentoPerRicerche the uidDocumentoPerRicerche to set
	 */
	public void setUidDocumentoPerRicerche(Integer uidDocumentoPerRicerche) {
		this.uidDocumentoPerRicerche = uidDocumentoPerRicerche;
	}

	/**
	 * @return the numeroQuote
	 */
	public Integer getNumeroQuote() {
		return numeroQuote;
	}

	/**
	 * @param numeroQuote the numeroQuote to set
	 */
	public void setNumeroQuote(Integer numeroQuote) {
		this.numeroQuote = numeroQuote;
	}

	/**
	 * @return the totaleQuoteSpesa
	 */
	public BigDecimal getTotaleQuoteSpesa() {
		return totaleQuoteSpesa;
	}

	/**
	 * @param totaleQuoteSpesa the totaleQuoteSpesa to set
	 */
	public void setTotaleQuoteSpesa(BigDecimal totaleQuoteSpesa) {
		this.totaleQuoteSpesa = totaleQuoteSpesa != null ? totaleQuoteSpesa : BigDecimal.ZERO;
	}

	/**
	 * @return the notaCredito
	 */
	public DocumentoEntrata getNotaCredito() {
		return notaCredito;
	}

	/**
	 * @param notaCredito the notaCredito to set
	 */
	public void setNotaCredito(DocumentoEntrata notaCredito) {
		this.notaCredito = notaCredito;
	}

	/**
	 * @return the listaQuoteCaricata
	 */
	public boolean isListaQuoteCaricata() {
		return listaQuoteCaricata;
	}

	/**
	 * @param listaQuoteCaricata the listaQuoteCaricata to set
	 */
	public void setListaQuoteCaricata(boolean listaQuoteCaricata) {
		this.listaQuoteCaricata = listaQuoteCaricata;
	}

	/**
	 * @return the listaQuoteIvaCaricata
	 */
	public boolean isListaQuoteIvaCaricata() {
		return listaQuoteIvaCaricata;
	}

	/**
	 * @param listaQuoteIvaCaricata the listaQuoteIvaCaricata to set
	 */
	public void setListaQuoteIvaCaricata(boolean listaQuoteIvaCaricata) {
		this.listaQuoteIvaCaricata = listaQuoteIvaCaricata;
	}

	/**
	 * @return the listaOnereCaricata
	 */
	public boolean isListaOnereCaricata() {
		return listaOnereCaricata;
	}

	/**
	 * @param listaOnereCaricata the listaOnereCaricata to set
	 */
	public void setListaOnereCaricata(boolean listaOnereCaricata) {
		this.listaOnereCaricata = listaOnereCaricata;
	}

	/**
	 * @return the listaDocumentiCollegatiCaricata
	 */
	public boolean isListaDocumentiCollegatiCaricata() {
		return listaDocumentiCollegatiCaricata;
	}

	/**
	 * @param listaDocumentiCollegatiCaricata the listaDocumentiCollegatiCaricata to set
	 */
	public void setListaDocumentiCollegatiCaricata(boolean listaDocumentiCollegatiCaricata) {
		this.listaDocumentiCollegatiCaricata = listaDocumentiCollegatiCaricata;
	}
	
	/**
	 * @return the totaleRilevanteIva
	 */
	public BigDecimal getTotaleRilevanteIva(){
		return getDocumento().getTotaleImportoRilevanteIvaQuote();
	}
	
	/**
	 * @return the totaleNonRilevanteIva
	 */
	public BigDecimal getTotaleNonRilevanteIva(){
		return documento.getTotaleImportoNonRilevanteIvaQuote();
	}
	
	/**
	 * @return the listaQuoteConDatiIva
	 */
	public List<SubdocumentoEntrata> getListaQuoteConDatiIva() {
		List<SubdocumentoEntrata> listaQuoteConDatiIva = new ArrayList<SubdocumentoEntrata>();
		for(SubdocumentoEntrata quota: getListaQuote()){
			if(Boolean.TRUE.equals(quota.getFlagRilevanteIVA()) && quota.getSubdocumentoIva() != null){
				listaQuoteConDatiIva.add(quota);
			}
		}
		return listaQuoteConDatiIva;
	}
	

	/**
	 * @return the uidDocumentoIvaSuInteroDocumento
	 */
	public Integer getUidDocumentoIvaSuInteroDocumento(){
		return Integer.valueOf(getDocumento().getListaSubdocumentoIva() != null && !getDocumento().getListaSubdocumentoIva().isEmpty() ? getDocumento().getListaSubdocumentoIva().get(0).getUid() : 0);
	}
	
	/**
	 * @return the documentoIvaSuInteroDocumento
	 */
	public SubdocumentoIvaEntrata getDocumentoIvaSuInteroDocumento(){
		return getDocumento().getListaSubdocumentoIva() != null && !getDocumento().getListaSubdocumentoIva().isEmpty() ? getDocumento().getListaSubdocumentoIva().get(0) : null;
	}
	
	/**
	 * @param netto the netto to set
	 */
	public void setNetto(BigDecimal netto) {
		impostaNetto(netto);
	}
	
	// Requests
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoSpesa creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesa() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoSpesa.class);
		
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(getUidDocumentoSpesa().intValue());
		request.setDocumentoSpesa(ds);
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		request.setSubdocumentoSpesaModelDetails(
			SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,
			SubdocumentoSpesaModelDetail.AttoAmm,
			SubdocumentoSpesaModelDetail.Liquidazione,
			SubdocumentoSpesaModelDetail.Ordinativo);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoEntrata creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrata() {
		RicercaSinteticaModulareQuoteByDocumentoEntrata request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoEntrata.class);
		
		request.setDocumentoEntrata(getDocumento());
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		request.setSubdocumentoEntrataModelDetails(
			SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,
			SubdocumentoEntrataModelDetail.AttoAmm,
			SubdocumentoEntrataModelDetail.Ordinativo,
			SubdocumentoEntrataModelDetail.ProvvisorioDiCassa);
		
		return request;
	}
	
	/**
	 * JIRA - 5154
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoEntrata creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrataCollegato() {
		RicercaSinteticaModulareQuoteByDocumentoEntrata request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoEntrata.class);
		
		DocumentoEntrata de = new DocumentoEntrata();
		de.setUid(getUidDocumento().intValue());
		request.setDocumentoEntrata(de);
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		request.setSubdocumentoEntrataModelDetails(
				SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,
				SubdocumentoEntrataModelDetail.AttoAmm,
				SubdocumentoEntrataModelDetail.Ordinativo,
				SubdocumentoEntrataModelDetail.ProvvisorioDiCassa);
		
		return request;
	}	
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoEntrata} per l'iva.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoEntrata creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrataPerIva() {
		RicercaSinteticaModulareQuoteByDocumentoEntrata request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoEntrata.class);
		
		request.setDocumentoEntrata(getDocumento());
		request.setParametriPaginazione(creaParametriPaginazione(5));
		request.setRilevanteIva(Boolean.TRUE);
		
		request.setSubdocumentoEntrataModelDetails(
			SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,
			SubdocumentoEntrataModelDetail.SubdocumentoIva);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoEntrata} per la nota di credito.
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoEntrata creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrataPerNotaCredito() {
		RicercaSinteticaModulareQuoteByDocumentoEntrata request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoEntrata.class);
		
		request.setDocumentoEntrata(getNotaCredito());
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		request.setSubdocumentoEntrataModelDetails(
			SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,
			SubdocumentoEntrataModelDetail.AttoAmm,
			SubdocumentoEntrataModelDetail.ProvvisorioDiCassa,
			SubdocumentoEntrataModelDetail.Ordinativo);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @param accertamento    l'accertamento per cui creare la request
	 * @param subAccertamento il subaccertamento per cui creare la request
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento, SubAccertamento subAccertamento) {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		request.setCaricaSub(subAccertamento != null && subAccertamento.getNumero() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
		
		//SIAC-4345: devo cercare l'impegno con il bilancio giusto, non necessariamente quello corrente
		Bilancio bilancio = accertamento.getCapitoloEntrataGestione() != null && accertamento.getCapitoloEntrataGestione().getBilancio() != null? accertamento.getCapitoloEntrataGestione().getBilancio() : getBilancio();
		pRicercaAccertamentoK.setAnnoEsercizio(Integer.valueOf(bilancio.getAnno()));

		pRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumero());
		pRicercaAccertamentoK.setNumeroSubDaCercare(subAccertamento != null ? subAccertamento.getNumero() : null);
		request.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioQuotaEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioQuotaEntrata creaRequestRicercaDettaglioQuotaEntrata() {
		RicercaDettaglioQuotaEntrata request = creaRequest(RicercaDettaglioQuotaEntrata.class);
		request.setSubdocumentoEntrata(getSubdocumentoEntrata());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaModulareDocumentoEntrata}.
	 * @return la request creata
	 */
	public RicercaModulareDocumentoEntrata creaRequestRicercaModulareDocumentoEntrata() {
		RicercaModulareDocumentoEntrata request = creaRequest(RicercaModulareDocumentoEntrata.class);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(getUidDocumento().intValue());
		request.setDocumentoEntrata(documentoEntrata);
		
		request.setDocumentoEntrataModelDetails(
				DocumentoEntrataModelDetail.Attr,
				DocumentoEntrataModelDetail.Sogg,
				DocumentoEntrataModelDetail.Classif,
				//DocumentoEntrataModelDetail.CodiceBollo,
				DocumentoEntrataModelDetail.DataInizioValiditaStato,
				DocumentoEntrataModelDetail.Stato,
				DocumentoEntrataModelDetail.SubdocumentoIva,
				DocumentoEntrataModelDetail.TipoDocumento,
				DocumentoEntrataModelDetail.TotaliImportiQuote,
				DocumentoEntrataModelDetail.TotaleImportoDocumentiEntrataFiglio,
				DocumentoEntrataModelDetail.TotaleImportoDocumentiSpesaFiglio,
				DocumentoEntrataModelDetail.TotaliImportiNoteCredito,
				DocumentoEntrataModelDetail.TotaliImportiRilevantiIvaQuote,
				DocumentoEntrataModelDetail.EsisteQuotaRilevanteIva);
		// Mancano: DocumentiCollegati, SubdocumentoEntrata
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaModulareDocumentoEntrata} per la nota credito.
	 * @return la request creata
	 */
	public RicercaModulareDocumentoEntrata creaRequestRicercaModulareDocumentoEntrataPerNotaCredito() {
		RicercaModulareDocumentoEntrata request = creaRequest(RicercaModulareDocumentoEntrata.class);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(getUidDocumento().intValue());
		request.setDocumentoEntrata(documentoEntrata);
		
		request.setDocumentoEntrataModelDetails(
				DocumentoEntrataModelDetail.TotaliImportiNoteCredito,
				DocumentoEntrataModelDetail.TotaliImportiQuote);
		return request;
	}

	/* LOTTO M - Documenti Bozze - i campi non esistono ancora sulle entita quindi li metto come null/FALSE **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaEntrata}.
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaEntrata creaRequestRicercaDettaglioSubdocumentoIvaEntrata() {
		RicercaDettaglioSubdocumentoIvaEntrata request = creaRequest(RicercaDettaglioSubdocumentoIvaEntrata.class);
		SubdocumentoIvaEntrata subdocumentoIvaEntrata = new SubdocumentoIvaEntrata();
		subdocumentoIvaEntrata.setUid(getUidQuotaIvaDifferita().intValue());
		request.setSubdocumentoIvaEntrata(subdocumentoIvaEntrata);
		return request;
	}
		
}
