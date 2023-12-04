/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.Ordine;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.NaturaFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

/**
 * Classe di model per la consultazione del Documento di Spesa.
 * 
 * @author Ahmad Nazha, Valentina Triolo
 * @version 1.0.0 - 24/03/2014
 *
 */
public class ConsultaDocumentoSpesaModel extends GenericDocumentoModel{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1294948158973919518L;

	private Integer uidDocumento;
	private DocumentoSpesa documento;
	
	private BigDecimal totaleQuote = BigDecimal.ZERO;
	private BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
	private BigDecimal totaleRitenute = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaAttribuire = BigDecimal.ZERO;
	private BigDecimal totaleImportoDocumentiCollegati = BigDecimal.ZERO;
	private BigDecimal totaleImportoDaDedurre = BigDecimal.ZERO;
	
	private List<SubdocumentoSpesa> listaQuote = new ArrayList<SubdocumentoSpesa>();
	private List<DettaglioOnere> listaOnere = new ArrayList<DettaglioOnere>();
	private List<ElementoDocumentoCollegato> listaDocumentiCollegati = new ArrayList<ElementoDocumentoCollegato>();
	
	private String idWorkaround = "";
	private Boolean flagDatiIvaAccessibile = Boolean.TRUE;
	private Boolean flagRitenuteAccessibile = Boolean.TRUE;
	private Boolean registrazioneSuSingolaQuota = Boolean.FALSE;
	
	private SubdocumentoSpesa subdocumentoSpesa;
	
	// Lotto M
	private List<Ordine> listaOrdine = new ArrayList<Ordine>();
	
	// Lotto O
	private String subdocIndex;
	
	private Integer uidQuotaIvaDifferita;
	private SubdocumentoIvaSpesa quotaIvaDifferita;
	
	// SIAC-3954
	private Integer uidDocumentoEntrata;
	private Integer uidDocumentoPerRicerche;
	private Integer numeroQuote;
	private BigDecimal totaleQuoteEntrata = BigDecimal.ZERO;
	private DocumentoSpesa notaCredito;
	private boolean listaQuoteCaricata;
	private boolean listaQuoteIvaCaricata;
	private boolean listaOnereCaricata;
	private boolean listaDocumentiCollegatiCaricata;
	
	// SIAC-1514
	private FatturaFEL fatturaFEL;
	
	/** Costruttore vuoto di dafault */
	public ConsultaDocumentoSpesaModel() {
		setTitolo("Consultazione Documenti di Spesa");
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
	public DocumentoSpesa getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoSpesa documento) {
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
	 * @return the totaleDaPagareQuote
	 */
	public BigDecimal getTotaleDaPagareQuote() {
		return totaleDaPagareQuote;
	}

	/**
	 * @param totaleDaPagareQuote the totaleDaPagareQuote to set
	 */
	public void setTotaleDaPagareQuote(BigDecimal totaleDaPagareQuote) {
		this.totaleDaPagareQuote = totaleDaPagareQuote != null ? totaleDaPagareQuote : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleRitenute
	 */
	public BigDecimal getTotaleRitenute() {
		return totaleRitenute;
	}

	/**
	 * @param totaleRitenute the totaleRitenute to set
	 */
	public void setTotaleRitenute(BigDecimal totaleRitenute) {
		this.totaleRitenute = totaleRitenute != null ? totaleRitenute : BigDecimal.ZERO;
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
	 * @return the listaQuote
	 */
	public List<SubdocumentoSpesa> getListaQuote() {
		return listaQuote;
	}

	/**
	 * @param listaQuote the listaQuote to set
	 */
	public void setListaQuote(List<SubdocumentoSpesa> listaQuote) {
		this.listaQuote = listaQuote != null ? listaQuote : new ArrayList<SubdocumentoSpesa>();
	}

	/**
	 * @return the listaOnere
	 */
	public List<DettaglioOnere> getListaOnere() {
		return listaOnere;
	}

	/**
	 * @param listaOnere the listaOnere to set
	 */
	public void setListaOnere(List<DettaglioOnere> listaOnere) {
		this.listaOnere = listaOnere != null ? listaOnere : new ArrayList<DettaglioOnere>();
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
	 * @return the flagRitenuteAccessibile
	 */
	public Boolean getFlagRitenuteAccessibile() {
		return flagRitenuteAccessibile;
	}

	/**
	 * @param flagRitenuteAccessibile the flagRitenuteAccessibile to set
	 */
	public void setFlagRitenuteAccessibile(Boolean flagRitenuteAccessibile) {
		this.flagRitenuteAccessibile = flagRitenuteAccessibile != null ? flagRitenuteAccessibile : Boolean.FALSE;
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
	 * @return the subdocumentoSpesa
	 */
	public SubdocumentoSpesa getSubdocumentoSpesa() {
		return subdocumentoSpesa;
	}

	/**
	 * @param subdocumentoSpesa the subdocumentoSpesa to set
	 */
	public void setSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		this.subdocumentoSpesa = subdocumentoSpesa;
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
	 * @return the subdocIndex
	 */
	public String getSubdocIndex() {
		return subdocIndex;
	}

	/**
	 * @param subdocIndex the subdocIndex to set
	 */
	public void setSubdocIndex(String subdocIndex) {
		this.subdocIndex = subdocIndex;
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
	public SubdocumentoIvaSpesa getQuotaIvaDifferita() {
		return quotaIvaDifferita;
	}

	/**
	 * @param quotaIvaDifferita the quotaIvaDifferita to set
	 */
	public void setQuotaIvaDifferita(SubdocumentoIvaSpesa quotaIvaDifferita) {
		this.quotaIvaDifferita = quotaIvaDifferita;
	}

	/**
	 * @return the uidDocumentoEntrata
	 */
	public Integer getUidDocumentoEntrata() {
		return uidDocumentoEntrata;
	}

	/**
	 * @param uidDocumentoEntrata the uidDocumentoEntrata to set
	 */
	public void setUidDocumentoEntrata(Integer uidDocumentoEntrata) {
		this.uidDocumentoEntrata = uidDocumentoEntrata;
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
	 * @return the totaleQuoteEntrata
	 */
	public BigDecimal getTotaleQuoteEntrata() {
		return totaleQuoteEntrata;
	}

	/**
	 * @param totaleQuoteEntrata the totaleQuoteEntrata to set
	 */
	public void setTotaleQuoteEntrata(BigDecimal totaleQuoteEntrata) {
		this.totaleQuoteEntrata = totaleQuoteEntrata != null ? totaleQuoteEntrata : BigDecimal.ZERO;
	}

	/**
	 * @return the notaCredito
	 */
	public DocumentoSpesa getNotaCredito() {
		return notaCredito;
	}

	/**
	 * @param notaCredito the notaCredito to set
	 */
	public void setNotaCredito(DocumentoSpesa notaCredito) {
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
	
	@Override
	public FatturaFEL getFatturaFEL() {
		return fatturaFEL;
	}


	@Override
	public void setFatturaFEL(FatturaFEL fatturaFEL) {
		this.fatturaFEL = fatturaFEL;
	}
	
	/**
	 * Controlla se la fattura FEL sia presente.
	 * 
	 * @return the fatturaFELPresente
	 */
	public boolean isFatturaFELPresente() {
		return getDocumento() != null && getDocumento().getFatturaFEL() != null && getDocumento().getFatturaFEL().getIdFattura() != null;
	}
	

	
	/**
	 * @return the totaleImponibileFEL
	 */
	public BigDecimal getTotaleImponibileFEL() {
		BigDecimal sum = BigDecimal.ZERO;
		if(getFatturaFEL() != null) {
			for(RiepilogoBeniFEL rbf : getFatturaFEL().getRiepiloghiBeni()) {
				if(!NaturaFEL.ESENTI.equals(rbf.getNaturaFEL())) {
					sum = sum.add(rbf.getImponibileImportoNotNull());
				}
			}
		}
		return sum;
	}
	
	/**
	 * @return the totaleImpostaFEL
	 */
	public BigDecimal getTotaleImpostaFEL() {
		BigDecimal sum = BigDecimal.ZERO;
		if(getFatturaFEL() != null) {
			for(RiepilogoBeniFEL rbf : getFatturaFEL().getRiepiloghiBeni()) {
				if(!NaturaFEL.ESENTI.equals(rbf.getNaturaFEL())) {
					sum = sum.add(rbf.getImpostaNotNull());
				}
			}
		}
		return sum;
	}
	
	/**
	 * @return the totaleEsenteFEL
	 */
	public BigDecimal getTotaleEsenteFEL() {
		BigDecimal sum = BigDecimal.ZERO;
		if(getFatturaFEL() != null) {
			for(RiepilogoBeniFEL rbf : getFatturaFEL().getRiepiloghiBeni()) {
				if(NaturaFEL.ESENTI.equals(rbf.getNaturaFEL())) {
					sum = sum.add(rbf.getImponibileImportoNotNull());
				}
			}
		}
		return sum;
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
	public SubdocumentoIvaSpesa getDocumentoIvaSuInteroDocumento(){
		return getDocumento().getListaSubdocumentoIva() != null && !getDocumento().getListaSubdocumentoIva().isEmpty() ? getDocumento().getListaSubdocumentoIva().get(0) : null;
	}
	
	/**
	 * @return the denominazioneModalitaPagamentoSoggetto
	 */
	public String getDenominazioneModalitaPagamentoSoggetto() {
		if(getSubdocumentoSpesa() == null || getSubdocumentoSpesa().getModalitaPagamentoSoggetto() == null) {
			return "";
		}
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = getSubdocumentoSpesa().getModalitaPagamentoSoggetto();

		List<String> listModalitaPagamentoSoggettoPartials = new ArrayList<String>();
		StringBuilder sbModalitaPagamentoSoggetto = new StringBuilder();

		if(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto() != null) {
			StringBuilder sbModalitaAccreditoSoggetto = new StringBuilder();
			sbModalitaAccreditoSoggetto.append("(")
				.append(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto().getCodice())
				.append(" - ")
				.append(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto().getDescrizione())
				.append(" ) ");
	
			sbModalitaPagamentoSoggetto.append(sbModalitaAccreditoSoggetto.toString());
		}

		computeDataForModalitaPagamentoSoggetto(modalitaPagamentoSoggetto, listModalitaPagamentoSoggettoPartials);

		if(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() != null && modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid() != 0) {
			computeDataForModalitaPagamentoSoggetto(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2(), listModalitaPagamentoSoggettoPartials);
		}
		sbModalitaPagamentoSoggetto.append(StringUtils.join(listModalitaPagamentoSoggettoPartials, " - "));

		return sbModalitaPagamentoSoggetto.toString();
	}
	
	/**
	 * @param netto the netto to set
	 */
	public void setNetto(BigDecimal netto) {
		impostaNetto(netto);
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
	public List<SubdocumentoSpesa> getListaQuoteConDatiIva() {
		List<SubdocumentoSpesa> listaQuoteConDatiIva = new ArrayList<SubdocumentoSpesa>();
		for(SubdocumentoSpesa quota: getListaQuote()){
			if(Boolean.TRUE.equals(quota.getFlagRilevanteIVA()) && quota.getSubdocumentoIva()!= null){
				listaQuoteConDatiIva.add(quota);
			}
		}
		return listaQuoteConDatiIva;
	}
	
	/**
	 * @return the stringaDescrizioneSAC
	 */
	public String getStringaDescrizioneSAC() {
		return getDocumento() == null || getDocumento().getStrutturaAmministrativoContabile() == null
			? ""
			: getDocumento().getStrutturaAmministrativoContabile().getCodice() + "-" + getDocumento().getStrutturaAmministrativoContabile().getDescrizione();
	}
	
	// SIAC-4749
	
	/**
	 * @return isDocumentoPagatoIncassato
	 */
	public String getIsDocumentoPagatoIncassato() {
		return getDocumento() != null && getDocumento().getDatiFatturaPagataIncassata() != null && Boolean.TRUE.equals(getDocumento().getDatiFatturaPagataIncassata().getFlagPagataIncassata())
				? "S&iacute;"
				: "No";
	}
	
	// SIAC-5311 SIOPE+
	
	/**
	 * @return the tipoDocumentoSiope
	 */
	public String getTipoDocumentoSiope() {
		return getDocumento() != null ? FormatUtils.formatCodificaCodiceDescrizione(getDocumento().getSiopeDocumentoTipo()) : "";
	}
	
	/**
	 * @return the tipoDocumentoSiopeAnalogico
	 */
	public String getTipoDocumentoSiopeAnalogico() {
		return getDocumento() != null ? FormatUtils.formatCodificaCodiceDescrizione(getDocumento().getSiopeDocumentoTipoAnalogico()) : "";
	}
	
	/**
	 * @return the motivoScadenzaSiope
	 */
	public String getMotivoScadenzaSiope() {
		return getSubdocumentoSpesa() != null ? FormatUtils.formatCodificaCodiceDescrizione(getSubdocumentoSpesa().getSiopeScadenzaMotivo()) : "";
	}
	
	/**
	 * @return the motivoAssenzaCigSiope
	 */
	public String getMotivoAssenzaCigSiope() {
		return getSubdocumentoSpesa() != null ? FormatUtils.formatCodificaCodiceDescrizione(getSubdocumentoSpesa().getSiopeAssenzaMotivazione()) : "";
	}
	
	/**
	 * @return the tipoDebitoSiopeImpegno
	 */
	public String getTipoDebitoSiopeImpegno() {
		return getSubdocumentoSpesa() != null && getSubdocumentoSpesa().getImpegnoOSubImpegno() != null
			? FormatUtils.formatCodificaCodiceDescrizione(getSubdocumentoSpesa().getImpegnoOSubImpegno().getSiopeTipoDebito())
			: "";
	}
	
	/**
	 * @return the allegatoAttoQuota
	 */
	public String getAllegatoAttoQuota() {
		if(getSubdocumentoSpesa() == null
				|| getSubdocumentoSpesa().getElencoDocumenti() == null
				|| getSubdocumentoSpesa().getElencoDocumenti().getAllegatoAtto() == null
				|| getSubdocumentoSpesa().getElencoDocumenti().getAllegatoAtto().getAttoAmministrativo() == null
				|| getSubdocumentoSpesa().getElencoDocumenti().getAllegatoAtto().getAttoAmministrativo().getTipoAtto() == null) {
			return "";
		}
		AttoAmministrativo aa = getSubdocumentoSpesa().getElencoDocumenti().getAllegatoAtto().getAttoAmministrativo();
		return aa.getAnno() + "/" + aa.getTipoAtto().getCodice() + "/" + aa.getNumero();
	}
	
	//SIAC-5346
		/**
		 * Gets the prestatore FEL della fattura FEL, se presente.
		 *
		 * @return the prestatore FEL se presente, null altrimenti
		 */
		private PrestatoreFEL getPrestatoreFEL() {
			return isFatturaFELPresente() ? getDocumento().getFatturaFEL().getPrestatore() : null;
		}
		
		/**
		 * Gets the identificativo fiscale FEL.
		 *
		 * @return the identificativo fiscale FEL
		 */
		public String getIdentificativoFiscaleFEL() {
			PrestatoreFEL prestatore = getPrestatoreFEL();
			return prestatore != null? 
					prestatore.getCodicePaese() + " - " +  prestatore.getCodicePrestatore() 
					: "";
		}
		
		/**
		 * Gets the identificativo fiscale FEL.
		 *
		 * @return the identificativo fiscale FEL
		 */
		public String getCodiceFiscaleFEL() {
			PrestatoreFEL prestatore = getPrestatoreFEL();
			return prestatore != null? prestatore.getCodicePrestatore() : "";
		}
	
	// Requests
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoSpesa creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesa() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoSpesa.class);
		
		request.setDocumentoSpesa(getDocumento());
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		request.setSubdocumentoSpesaModelDetails(
			SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,
			SubdocumentoSpesaModelDetail.AttoAmm,
			SubdocumentoSpesaModelDetail.Liquidazione,
			SubdocumentoSpesaModelDetail.Ordinativo,
			SubdocumentoSpesaModelDetail.Predocumento);
		
		return request;
	}
	
	/**
	 * JIRA - 5154
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoSpesa creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesaCollegato() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoSpesa.class);
		
		DocumentoSpesa ds = new DocumentoSpesa();
		ds.setUid(getUidDocumento().intValue());
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
		
		DocumentoEntrata de = new DocumentoEntrata();
		de.setUid(getUidDocumentoEntrata().intValue());
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
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoSpesa} per l'iva.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoSpesa creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesaPerIva() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoSpesa.class);
		
		request.setDocumentoSpesa(getDocumento());
		request.setParametriPaginazione(creaParametriPaginazione(5));
		request.setRilevanteIva(Boolean.TRUE);
		
		request.setSubdocumentoSpesaModelDetails(
			SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,
			SubdocumentoSpesaModelDetail.SubdocumentoIva);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaModulareQuoteByDocumentoSpesa} per la nota di credito.
	 * @return la request creata
	 */
	public RicercaSinteticaModulareQuoteByDocumentoSpesa creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesaPerNotaCredito() {
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = creaRequest(RicercaSinteticaModulareQuoteByDocumentoSpesa.class);
		
		request.setDocumentoSpesa(getNotaCredito());
		request.setParametriPaginazione(creaParametriPaginazione(5));
		
		request.setSubdocumentoSpesaModelDetails(
			SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,
			SubdocumentoSpesaModelDetail.AttoAmm,
			SubdocumentoSpesaModelDetail.Liquidazione,
			SubdocumentoSpesaModelDetail.Ordinativo);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaOnereByDocumentoSpesa creaRequestRicercaOnereByDocumentoSpesa() {
		RicercaOnereByDocumentoSpesa request = creaRequest(RicercaOnereByDocumentoSpesa.class);
		request.setDocumentoSpesa(getDocumento());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioQuotaSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioQuotaSpesa creaRequestRicercaDettaglioQuotaSpesa() {
		RicercaDettaglioQuotaSpesa request = creaRequest(RicercaDettaglioQuotaSpesa.class);
		request.setSubdocumentoSpesa(getSubdocumentoSpesa());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @param impegno    l'impegno per cui creare la request
	 * @param subImpegno il subimpegno per cui creare la request
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno impegno, SubImpegno subImpegno) {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(subImpegno != null && subImpegno.getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		// SIAC-5546
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibilePagare(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO, TipologiaClassificatore.MACROAGGREGATO));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		RicercaImpegnoK pRicercaImpegnoK = new  RicercaImpegnoK();
		//SIAC-4345: devo cercare l'impegno con il bilancio giusto, non necessariamente quello corrente
		Bilancio bilancio = impegno.getCapitoloUscitaGestione() != null && impegno.getCapitoloUscitaGestione().getBilancio() != null? impegno.getCapitoloUscitaGestione().getBilancio() : getBilancio();
		pRicercaImpegnoK.setAnnoEsercizio(Integer.valueOf(bilancio.getAnno()));
		
		pRicercaImpegnoK.setAnnoImpegno(Integer.valueOf(impegno.getAnnoMovimento()));
		pRicercaImpegnoK.setNumeroImpegno(impegno.getNumeroBigDecimal());
		pRicercaImpegnoK.setNumeroSubDaCercare(subImpegno != null ? subImpegno.getNumeroBigDecimal() : null);
		request.setpRicercaImpegnoK(pRicercaImpegnoK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaOrdiniDocumento}.
	 * 
	 * @return la request creata
	 */
	public RicercaOrdiniDocumento creaRequestRicercaOrdiniDocumento() {
		RicercaOrdiniDocumento request = creaRequest(RicercaOrdiniDocumento.class);
		request.setDocumento(getDocumento());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaModulareDocumentoSpesa}.
	 * @return la request creata
	 */
	public RicercaModulareDocumentoSpesa creaRequestRicercaModulareDocumentoSpesa() {
		RicercaModulareDocumentoSpesa request = creaRequest(RicercaModulareDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(getUidDocumento().intValue());
		request.setDocumentoSpesa(documentoSpesa);
		
		request.setDocumentoSpesaModelDetails(
				DocumentoSpesaModelDetail.Attr,
				DocumentoSpesaModelDetail.Soggetto,
				DocumentoSpesaModelDetail.Classif,
				//DocumentoSpesaModelDetail.CodiceBollo,
				DocumentoSpesaModelDetail.CodicePCC,
				DocumentoSpesaModelDetail.CodiceUfficioDestinatarioPCC,
				DocumentoSpesaModelDetail.CollegatoAdAllegatoAtto,
				DocumentoSpesaModelDetail.DataInizioValiditaStato,
				DocumentoSpesaModelDetail.FatturaFEL,
				DocumentoSpesaModelDetail.ImportoDaPagareNonPagatoInCassaEconomale,
				DocumentoSpesaModelDetail.RegistroComunicazioniPCC,
				DocumentoSpesaModelDetail.RegistroUnico,
				DocumentoSpesaModelDetail.Stato,
				DocumentoSpesaModelDetail.SubdocumentoIva,
				DocumentoSpesaModelDetail.TipoDocumento,
				DocumentoSpesaModelDetail.SiopeDocumentoTipo,
				DocumentoSpesaModelDetail.SiopeDocumentoTipoAnalogico,
				DocumentoSpesaModelDetail.TotaliImportiQuote,
				DocumentoSpesaModelDetail.TotaleImportoDocumentiEntrataFiglio,
				DocumentoSpesaModelDetail.TotaleImportoDocumentiSpesaFiglio,
				DocumentoSpesaModelDetail.TotaliImportiNoteCredito,
				DocumentoSpesaModelDetail.TotaliImportiRilevantiIvaQuote,
				DocumentoSpesaModelDetail.EsisteQuotaRilevanteIva);
		// Mancano: DocumentiCollegati, Onere, Ordini, SubdocumentoSpesa
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaModulareDocumentoSpesa} per la nota credito.
	 * @return la request creata
	 */
	public RicercaModulareDocumentoSpesa creaRequestRicercaModulareDocumentoSpesaPerNotaCredito() {
		RicercaModulareDocumentoSpesa request = creaRequest(RicercaModulareDocumentoSpesa.class);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(getUidDocumento().intValue());
		request.setDocumentoSpesa(documentoSpesa);
		
		request.setDocumentoSpesaModelDetails(
				DocumentoSpesaModelDetail.TotaliImportiNoteCredito,
				DocumentoSpesaModelDetail.TotaliImportiQuote);
		return request;
	}

	/* LOTTO M - Documenti Bozze - i campi non esistono ancora sulle entita quindi li metto come null/FALSE **** */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaSpesa}.
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaSpesa creaRequestRicercaDettaglioSubdocumentoIvaSpesa() {
		RicercaDettaglioSubdocumentoIvaSpesa request = creaRequest(RicercaDettaglioSubdocumentoIvaSpesa.class);
		SubdocumentoIvaSpesa subdocumentoIvaSpesa = new SubdocumentoIvaSpesa();
		subdocumentoIvaSpesa.setUid(getUidQuotaIvaDifferita().intValue());
		request.setSubdocumentoIvaSpesa(subdocumentoIvaSpesa);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioFatturaElettronica}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioFatturaElettronica creaRequestRicercaDettaglioFatturaElettronica() {
		RicercaDettaglioFatturaElettronica request = creaRequest(RicercaDettaglioFatturaElettronica.class);
		
		request.setFatturaFEL(getDocumento().getFatturaFEL());
		
		return request;
	}

	/**
	 * Computa i dati parziali per la modalita di pagamento soggetto-
	 * 
	 * @param modalitaPagamentoSoggetto             la modalita di pagamento
	 * @param listModalitaPagamentoSoggettoPartials i parziali
	 */
	private void computeDataForModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto, List<String> listModalitaPagamentoSoggettoPartials) {
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getIban())) {
			listModalitaPagamentoSoggettoPartials.add("iban: " + modalitaPagamentoSoggetto.getIban());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getBic())) {
			listModalitaPagamentoSoggettoPartials.add("bic: " + modalitaPagamentoSoggetto.getBic());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getContoCorrente())) {
			listModalitaPagamentoSoggettoPartials.add("conto: " + modalitaPagamentoSoggetto.getContoCorrente());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getIntestazioneConto())) {
			listModalitaPagamentoSoggettoPartials.add("intestato a " + modalitaPagamentoSoggetto.getIntestazioneConto());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getSoggettoQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("quietanzante: " + modalitaPagamentoSoggetto.getSoggettoQuietanzante());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getCodiceFiscaleQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("CF: " + modalitaPagamentoSoggetto.getCodiceFiscaleQuietanzante());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getDataNascitaQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("nato il " + modalitaPagamentoSoggetto.getDataNascitaQuietanzante());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getLuogoNascitaQuietanzante()) && StringUtils.isNotBlank(modalitaPagamentoSoggetto.getStatoNascitaQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("a: " + modalitaPagamentoSoggetto.getLuogoNascitaQuietanzante() + ", " + modalitaPagamentoSoggetto.getStatoNascitaQuietanzante());
		}
	}
}
