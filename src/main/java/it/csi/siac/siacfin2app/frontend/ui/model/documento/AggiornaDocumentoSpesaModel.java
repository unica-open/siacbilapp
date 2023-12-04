/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.CausaleSospensione;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPA;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.siopeplus.SiopeScadenzaMotivo;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.NaturaFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

/**
 * Classe di model per l'aggiornamento del documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/03/2014
 *
 */
public class AggiornaDocumentoSpesaModel extends AggiornaDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5642500162717912789L;
	
	private DocumentoSpesa documento;
	private SubdocumentoSpesa subdocumento;
	
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private ContoTesoreria contoTesoreria;
	
	private Impegno movimentoGestione;
	private SubImpegno subMovimentoGestione;
	
	private DettaglioOnere dettaglioOnere;
	private AttivitaOnere attivitaOnere;
	
	private DocumentoSpesa notaCredito;
	private SubdocumentoIvaSpesa subdocumentoIva;
	private FatturaFEL fatturaFEL;
	
	// Quote
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	// Ritenute
	private List<DettaglioOnere> listaDettaglioOnere = new ArrayList<DettaglioOnere>();
	// Penale/Altro
	private List<ElementoDocumento> listaDocumentoEntrata = new ArrayList<ElementoDocumento>();
	// Note credito
	private List<ElementoDocumento> listaDocumentoSpesa = new ArrayList<ElementoDocumento>();
	// Quote rilevanti Iva
	private List<ElementoSubdocumentoIvaSpesa> listaQuoteRilevantiIva = new ArrayList<ElementoSubdocumentoIvaSpesa>();
	
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	private List<CodicePCC> listaCodicePCC = new ArrayList<CodicePCC>();
	private List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC = new ArrayList<CodiceUfficioDestinatarioPCC>();
	
	private List<TipoImpresa> listaTipoImpresa = new ArrayList<TipoImpresa>();
	private List<NaturaOnere> listaNaturaOnere = new ArrayList<NaturaOnere>();
	// Codice tributo
	private List<TipoOnere> listaTipoOnere = new ArrayList<TipoOnere>();
	// Attività
	private List<AttivitaOnere> listaAttivitaOnere = new ArrayList<AttivitaOnere>();
	private List<CodiceSommaNonSoggetta> listaSommeNonSoggette = new ArrayList<CodiceSommaNonSoggetta>();
	
	private BigDecimal penaleAltroDocumento = BigDecimal.ZERO;
	private BigDecimal totaleRitenuteDocumento = BigDecimal.ZERO;
	
	private Boolean flagRitenuteAccessibile = Boolean.TRUE;
	private Boolean flagPenaleAltroAccessibile = Boolean.TRUE;
	private Boolean collegatoCECEditabile = Boolean.TRUE;
	private Boolean flagEditabilitaRitenute = Boolean.TRUE;
	
	
	
	
	private Boolean flagDatiSospensioneEditabili = Boolean.FALSE;
	
	// Per filtrare le modalitaPagamentoSoggetto
	private Integer uidSedeSecondariaSoggetto;
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoFiltrate = new ArrayList<ModalitaPagamentoSoggetto>();
	
	//Lotto L
	private List<TipoIvaSplitReverse> listaTipoIvaSplitReverse = new ArrayList<TipoIvaSplitReverse>();
	private BigDecimal importoSplitReverse;
	private BigDecimal importoEsenteProposto;
	
	private boolean attivaRegistrazioniContabiliVisible = false;
	private boolean flagAutoCalcoloImportoSplitQuote = true;
	private boolean flagSplitQuotePresente = false;
	
	
	//LOTTO O
	private TipoDocumento tipoDocumentoNotaCredito;
	// SIAC-4405
	private List<StatoOperativoAtti> listaStatoOperativoAtti = new ArrayList<StatoOperativoAtti>();
	
	// SIAC-4679
	private List<CausaleSospensione> listaCausaleSospensione = new ArrayList<CausaleSospensione>();
	
	// SIAC-4749
	private boolean datiFatturaPagataIncassataEditabili;
	// SIAC-5072
	private boolean flagNoDatiSospensioneDecentrato;
	
	// SIAC-5311 SIOPE+
	private Date dataScadenzaOriginale;
	private Date dataScadenzaDopoSospensioneOriginale;
	private List<SiopeDocumentoTipo> listaSiopeDocumentoTipo = new ArrayList<SiopeDocumentoTipo>();
	private List<SiopeDocumentoTipoAnalogico> listaSiopeDocumentoTipoAnalogico = new ArrayList<SiopeDocumentoTipoAnalogico>();
	private List<SiopeScadenzaMotivo> listaSiopeScadenzaMotivo = new ArrayList<SiopeScadenzaMotivo>();
	private List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione = new ArrayList<SiopeAssenzaMotivazione>();
	// SIAC-5115
	private List<SospensioneSubdocumento> listaSospensioneSubdocumento = new ArrayList<SospensioneSubdocumento>();
	private SospensioneSubdocumento sospensioneSubdocumento;
	private Integer indexSospensioneQuota;
	private boolean abilitatoGestioneAcquisti = false;
	private boolean noDatiSospensioneDec = false;
	
	//SIAC-5469
	private boolean showSiopeAssenzaMotivazione = false;
	
	//SIAC-5346
	private boolean inibisciModificaDatiImportatiFEL;
	private Integer uidCodiceUfficioDestinatarioPccToFilter;
	private List<CodicePCC> listaCodicePCCFiltered = new ArrayList<CodicePCC>();
	
	
	//SIAC-6261
	private List<Messaggio> messaggiSenzaRichiestaConferma = new ArrayList<Messaggio>();

	/** Costruttore vuoto di default */
	public AggiornaDocumentoSpesaModel() {
		setTitolo("Aggiorna Documenti di Spesa");
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
	 * @return the subdocumento
	 */
	public SubdocumentoSpesa getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(SubdocumentoSpesa subdocumento) {
		this.subdocumento = subdocumento;
	}

	/**
	 * Ottiene la stringa di definizione dello stato a partire dallo stato del documento e dalla data di inizio validita.
	 * 
	 * @return the stato
	 */
	public String getStato() {
		if(getDocumento() == null || getDocumento().getStatoOperativoDocumento() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Stato documento: ")
			.append(documento.getStatoOperativoDocumento().getDescrizione())
			.append(" dal ")
			.append(FormatUtils.formatDate(documento.getDataInizioValiditaStato()));
		return sb.toString();
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the movimentoGestione
	 */
	public Impegno getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(Impegno movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubImpegno getSubMovimentoGestione() {
		return subMovimentoGestione;
	}

	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(SubImpegno subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}


	/**
	 * @return the dettaglioOnere
	 */
	public DettaglioOnere getDettaglioOnere() {
		return dettaglioOnere;
	}

	/**
	 * @param dettaglioOnere the dettaglioOnere to set
	 */
	public void setDettaglioOnere(DettaglioOnere dettaglioOnere) {
		this.dettaglioOnere = dettaglioOnere;
	}

	/**
	 * @return the attivitaOnere
	 */
	public AttivitaOnere getAttivitaOnere() {
		return attivitaOnere;
	}

	/**
	 * @param attivitaOnere the attivitaOnere to set
	 */
	public void setAttivitaOnere(AttivitaOnere attivitaOnere) {
		this.attivitaOnere = attivitaOnere;
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
	 * @return the subdocumentoIva
	 */
	public SubdocumentoIvaSpesa getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @param subdocumentoIva the subdocumentoIva to set
	 */
	public void setSubdocumentoIva(SubdocumentoIvaSpesa subdocumentoIva) {
		this.subdocumentoIva = subdocumentoIva;
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
	 * @return the listaSubdocumentoSpesa
	 */
	public List<SubdocumentoSpesa> getListaSubdocumentoSpesa() {
		return listaSubdocumentoSpesa;
	}

	/**
	 * @param listaSubdocumentoSpesa the listaSubdocumentoSpesa to set
	 */
	public void setListaSubdocumentoSpesa(List<SubdocumentoSpesa> listaSubdocumentoSpesa) {
		this.listaSubdocumentoSpesa = listaSubdocumentoSpesa != null ? listaSubdocumentoSpesa : new ArrayList<SubdocumentoSpesa>();
	}

	/**
	 * @return the listaDettaglioOnere
	 */
	public List<DettaglioOnere> getListaDettaglioOnere() {
		return listaDettaglioOnere;
	}

	/**
	 * @param listaDettaglioOnere the listaDettaglioOnere to set
	 */
	public void setListaDettaglioOnere(List<DettaglioOnere> listaDettaglioOnere) {
		this.listaDettaglioOnere = listaDettaglioOnere != null ? listaDettaglioOnere : new ArrayList<DettaglioOnere>();
	}

	/**
	 * @return the listaDocumentoEntrata
	 */
	public List<ElementoDocumento> getListaDocumentoEntrata() {
		return listaDocumentoEntrata;
	}

	/**
	 * @param listaDocumentoEntrata the listaDocumentoEntrata to set
	 */
	public void setListaDocumentoEntrata(List<ElementoDocumento> listaDocumentoEntrata) {
		this.listaDocumentoEntrata = listaDocumentoEntrata != null ? listaDocumentoEntrata : new ArrayList<ElementoDocumento>();
	}

	/**
	 * @return the listaDocumentoSpesa
	 */
	public List<ElementoDocumento> getListaDocumentoSpesa() {
		return listaDocumentoSpesa;
	}

	/**
	 * @param listaDocumentoSpesa the listaDocumentoSpesa to set
	 */
	public void setListaDocumentoSpesa(List<ElementoDocumento> listaDocumentoSpesa) {
		this.listaDocumentoSpesa = listaDocumentoSpesa != null ? listaDocumentoSpesa : new ArrayList<ElementoDocumento>();
	}

	/**
	 * @return the listaQuoteRilevantiIva
	 */
	public List<ElementoSubdocumentoIvaSpesa> getListaQuoteRilevantiIva() {
		return listaQuoteRilevantiIva;
	}

	/**
	 * @param listaQuoteRilevantiIva the listaQuoteRilevantiIva to set
	 */
	public void setListaQuoteRilevantiIva(List<ElementoSubdocumentoIvaSpesa> listaQuoteRilevantiIva) {
		this.listaQuoteRilevantiIva = listaQuoteRilevantiIva != null ? listaQuoteRilevantiIva : new ArrayList<ElementoSubdocumentoIvaSpesa>();
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto != null ? listaModalitaPagamentoSoggetto : new ArrayList<ModalitaPagamentoSoggetto>();
	}

	/**
	 * @return the listaCodicePCC
	 */
	public List<CodicePCC> getListaCodicePCC() {
		return listaCodicePCC;
	}

	/**
	 * @param listaCodicePCC the listaCodicePCC to set
	 */
	public void setListaCodicePCC(List<CodicePCC> listaCodicePCC) {
		this.listaCodicePCC = listaCodicePCC != null ? listaCodicePCC : new ArrayList<CodicePCC>();
	}

	/**
	 * @return the listaCodiceUfficioDestinatarioPCC
	 */
	public List<CodiceUfficioDestinatarioPCC> getListaCodiceUfficioDestinatarioPCC() {
		return listaCodiceUfficioDestinatarioPCC;
	}

	/**
	 * @param listaCodiceUfficioDestinatarioPCC the listaCodiceUfficioDestinatarioPCC to set
	 */
	public void setListaCodiceUfficioDestinatarioPCC(List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC) {
		this.listaCodiceUfficioDestinatarioPCC = listaCodiceUfficioDestinatarioPCC != null ? listaCodiceUfficioDestinatarioPCC : new ArrayList<CodiceUfficioDestinatarioPCC>();
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
	 * @return the listaTipoImpresa
	 */
	public List<TipoImpresa> getListaTipoImpresa() {
		return listaTipoImpresa;
	}

	/**
	 * @param listaTipoImpresa the listaTipoImpresa to set
	 */
	public void setListaTipoImpresa(List<TipoImpresa> listaTipoImpresa) {
		this.listaTipoImpresa = listaTipoImpresa != null ? listaTipoImpresa : new ArrayList<TipoImpresa>();
	}

	/**
	 * @return the listaNaturaOnere
	 */
	public List<NaturaOnere> getListaNaturaOnere() {
		return listaNaturaOnere;
	}

	/**
	 * @param listaNaturaOnere the listaNaturaOnere to set
	 */
	public void setListaNaturaOnere(List<NaturaOnere> listaNaturaOnere) {
		this.listaNaturaOnere = listaNaturaOnere != null ? listaNaturaOnere : new ArrayList<NaturaOnere>();
	}

	/**
	 * @return the listaTipoOnere
	 */
	public List<TipoOnere> getListaTipoOnere() {
		return listaTipoOnere;
	}

	/**
	 * @param listaTipoOnere the listaTipoOnere to set
	 */
	public void setListaTipoOnere(List<TipoOnere> listaTipoOnere) {
		this.listaTipoOnere = listaTipoOnere != null ? listaTipoOnere : new ArrayList<TipoOnere>();
	}

	/**
	 * @return the listaAttivitaOnere
	 */
	public List<AttivitaOnere> getListaAttivitaOnere() {
		return listaAttivitaOnere;
	}

	/**
	 * @param listaAttivitaOnere the listaAttivitaOnere to set
	 */
	public void setListaAttivitaOnere(List<AttivitaOnere> listaAttivitaOnere) {
		this.listaAttivitaOnere = listaAttivitaOnere != null ? listaAttivitaOnere : new ArrayList<AttivitaOnere>();
	}
	

	/**
	 * @return the listaSommeNonSoggette
	 */
	public List<CodiceSommaNonSoggetta> getListaSommeNonSoggette() {
		return listaSommeNonSoggette;
	}

	/**
	 * @param listaSommeNonSoggette the listaSommeNonSoggette to set
	 */
	public void setListaSommeNonSoggette(
			List<CodiceSommaNonSoggetta> listaSommeNonSoggette) {
		this.listaSommeNonSoggette = listaSommeNonSoggette;
	}

	/**
	 * @return the penaleAltroDocumento
	 */
	public BigDecimal getPenaleAltroDocumento() {
		return penaleAltroDocumento;
	}

	/**
	 * @param penaleAltroDocumento the penaleAltroDocumento to set
	 */
	public void setPenaleAltroDocumento(BigDecimal penaleAltroDocumento) {
		this.penaleAltroDocumento = penaleAltroDocumento != null ? penaleAltroDocumento : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleRitenuteDocumento
	 */
	public BigDecimal getTotaleRitenuteDocumento() {
		return totaleRitenuteDocumento;
	}

	/**
	 * @param totaleRitenuteDocumento the totaleRitenuteDocumento to set
	 */
	public void setTotaleRitenuteDocumento(BigDecimal totaleRitenuteDocumento) {
		this.totaleRitenuteDocumento = totaleRitenuteDocumento != null ? totaleRitenuteDocumento : BigDecimal.ZERO;
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
	 * @return the flagPenaleAltroAccessibile
	 */
	public Boolean getFlagPenaleAltroAccessibile() {
		return flagPenaleAltroAccessibile;
	}

	/**
	 * @param flagPenaleAltroAccessibile the flagPenaleAltroAccessibile to set
	 */
	public void setFlagPenaleAltroAccessibile(Boolean flagPenaleAltroAccessibile) {
		this.flagPenaleAltroAccessibile = flagPenaleAltroAccessibile != null ? flagPenaleAltroAccessibile : Boolean.FALSE;
	}
	
	/**
	 * @return the collegatoCECEditabile
	 */
	public Boolean getCollegatoCECEditabile() {
		return collegatoCECEditabile;
	}
	
	/**
	 * @param flagEditabilitaRitenute the flagEditabilitaRitenute to set
	 */
	public void setFlagEditabilitaRitenute(Boolean flagEditabilitaRitenute) {
		this.flagEditabilitaRitenute = flagEditabilitaRitenute;
	}
	
	/**
	 * @return the flagEditabilitaRitenute
	 */
	public Boolean getFlagEditabilitaRitenute() {
		return flagEditabilitaRitenute;
	}

	/**
	 * @param collegatoCECEditabile the collegatoCECEditabile to set
	 */
	public void setCollegatoCECEditabile(Boolean collegatoCECEditabile) {
		this.collegatoCECEditabile = collegatoCECEditabile;
	}

	/**
	 * @return the flagDatiSospensioneEditabili
	 */
	public Boolean getFlagDatiSospensioneEditabili() {
		return flagDatiSospensioneEditabili;
	}

	/**
	 * @param flagDatiSospensioneEditabili the flagDatiSospensioneEditabili to set
	 */
	public void setFlagDatiSospensioneEditabili(Boolean flagDatiSospensioneEditabili) {
		this.flagDatiSospensioneEditabili = flagDatiSospensioneEditabili != null ? flagDatiSospensioneEditabili : Boolean.FALSE;
	}
	

	/**
	 * @return the uidSedeSecondariaSoggetto
	 */
	public Integer getUidSedeSecondariaSoggetto() {
		return uidSedeSecondariaSoggetto;
	}

	/**
	 * @param uidSedeSecondariaSoggetto the uidSedeSecondariaSoggetto to set
	 */
	public void setUidSedeSecondariaSoggetto(Integer uidSedeSecondariaSoggetto) {
		this.uidSedeSecondariaSoggetto = uidSedeSecondariaSoggetto;
	}

	/**
	 * @return the listaModalitaPagamentoSoggettoFiltrate
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoFiltrate() {
		return listaModalitaPagamentoSoggettoFiltrate;
	}

	/**
	 * @param listaModalitaPagamentoSoggettoFiltrate the listaModalitaPagamentoSoggettoFiltrate to set
	 */
	public void setListaModalitaPagamentoSoggettoFiltrate(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoFiltrate) {
		this.listaModalitaPagamentoSoggettoFiltrate = listaModalitaPagamentoSoggettoFiltrate != null ? listaModalitaPagamentoSoggettoFiltrate : new ArrayList<ModalitaPagamentoSoggetto>();
	}

	/**
	 * @return the listaTipoIvaSplitReverse
	 */
	public final List<TipoIvaSplitReverse> getListaTipoIvaSplitReverse() {
		return listaTipoIvaSplitReverse;
	}

	/**
	 * @param listaTipoIvaSplitReverse the listaTipoIvaSplitReverse to set
	 */
	public final void setListaTipoIvaSplitReverse(List<TipoIvaSplitReverse> listaTipoIvaSplitReverse) {
		this.listaTipoIvaSplitReverse = listaTipoIvaSplitReverse == null ? new ArrayList<TipoIvaSplitReverse>() : listaTipoIvaSplitReverse;
	}

	/**
	 * @return the importoSplitReverse
	 */
	public final BigDecimal getImportoSplitReverse() {
		return importoSplitReverse;
	}

	/**
	 * @param importoSplitReverse the importoSplitReverse to set
	 */
	public final void setImportoSplitReverse(BigDecimal importoSplitReverse) {
		this.importoSplitReverse = importoSplitReverse;
	}

	/**
	 * @return the importoEsenteProposto
	 */
	public final BigDecimal getImportoEsenteProposto() {
		return importoEsenteProposto;
	}

	/**
	 * @param importoEsenteProposto the importoEsenteProposto to set
	 */
	public void setImportoEsenteProposto(BigDecimal importoEsenteProposto) {
		this.importoEsenteProposto = importoEsenteProposto;
	}

	/**
	 * @return the attivaRegistrazioniContabiliVisible
	 */
	public boolean isAttivaRegistrazioniContabiliVisible() {
		return attivaRegistrazioniContabiliVisible;
	}

	/**
	 * @param attivaRegistrazioniContabiliVisible the attivaRegistrazioniContabiliVisible to set
	 */
	public void setAttivaRegistrazioniContabiliVisible(boolean attivaRegistrazioniContabiliVisible) {
		this.attivaRegistrazioniContabiliVisible = attivaRegistrazioniContabiliVisible;
	}
	
	/**
	 * @return the flagQuotaConImportoDaDedurre
	 */
	public boolean getFlagQuotaConImportoDaDedurre() {
		return getTotaleImportoDaDedurre() != null && getTotaleImportoDaDedurre().compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Controlla se le ritenute siano editabili.
	 */
	public void impostaFlagEditabilitaRitenute() {
		if(StatoOperativoDocumento.EMESSO.equals(getDocumento().getStatoOperativoDocumento()) || StatoOperativoDocumento.ANNULLATO.equals(getDocumento().getStatoOperativoDocumento())){
			setFlagEditabilitaRitenute(Boolean.FALSE);
			return;
		}
		if(getListaSubdocumentoSpesa() == null || getListaSubdocumentoSpesa().isEmpty()){
			setFlagEditabilitaRitenute(Boolean.TRUE);
			return;
		}
		for(SubdocumentoSpesa sub :getListaSubdocumentoSpesa()){
			if(sub.getLiquidazione() != null && !StatoOperativoLiquidazione.PROVVISORIO.equals(sub.getLiquidazione().getStatoOperativoLiquidazione())){
				setFlagEditabilitaRitenute(Boolean.FALSE);
				return;
			}
		}
		setFlagEditabilitaRitenute(Boolean.TRUE);
	}
	
	

	/**
	 * @return the flagAutoCalcoloImportoSplitQuote
	 */
	public boolean isFlagAutoCalcoloImportoSplitQuote() {
		return flagAutoCalcoloImportoSplitQuote;
	}

	/**
	 * @param flagAutoCalcoloImportoSplitQuote the flagAutoCalcoloImportoSplitQuote to set
	 */
	public void setFlagAutoCalcoloImportoSplitQuote(
			boolean flagAutoCalcoloImportoSplitQuote) {
		this.flagAutoCalcoloImportoSplitQuote = flagAutoCalcoloImportoSplitQuote;
	}

	/**
	 * @return the flagSplitQuotePresente
	 */
	public boolean isFlagSplitQuotePresente() {
		return flagSplitQuotePresente;
	}

	/**
	 * @param flagSplitQuotePresente the flagSplitQuotePresente to set
	 */
	public void setFlagSplitQuotePresente(boolean flagSplitQuotePresente) {
		this.flagSplitQuotePresente = flagSplitQuotePresente;
	}
	
	/**
	 * Controlla se le posso calcolare importo split/reverse in automatico
	 */
	public void impostaFlagAutoCalcoloImportoSplitQuote() {
		if(getListaSubdocumentoSpesa() == null || getListaSubdocumentoSpesa().isEmpty()){
			setFlagAutoCalcoloImportoSplitQuote(false);
			return;
		}
		//if((!hasLiquidazione || source.liquidazione.statoOperativoLiquidazione == 'PROVVISORIO') && numRegIVANonValorizzato && !quotaPagatoCEC){
        
		for(SubdocumentoSpesa sub :getListaSubdocumentoSpesa()){
			boolean numRegIVANonValorizzato = sub.getNumeroRegistrazioneIVA()==null || sub.getNumeroRegistrazioneIVA().isEmpty();
			
			setFlagSplitQuotePresente(isFlagSplitQuotePresente() || !BigDecimal.ZERO.equals(sub.getImportoSplitReverseNotNull()));
			boolean quotaAggiornabile = (sub.getLiquidazione() == null 
					|| StatoOperativoLiquidazione.PROVVISORIO.equals(sub.getLiquidazione().getStatoOperativoLiquidazione()))
					&& Boolean.FALSE.equals(sub.getPagatoInCEC())
					&& numRegIVANonValorizzato;
			//non devo metterlo a true se le altre sono aggiornabili e non devo uscire perchè devo finire i controlli per presenza
			if (!quotaAggiornabile) {
				setFlagAutoCalcoloImportoSplitQuote(false);
			}
			
			
		}

		
	}
	
	/**
	 * @return <code>true</code> se il documento &eacute; gi&agrave; legato a un subdocumento iva
	 *   (direttamente o tramite quote); <code>false</code> in caso contrario
	 */
	public boolean getLegameConIvaPresente() {
		// Se ho il documento iva legato al capitolo, va già bene
		if(Boolean.TRUE.equals(getDocumentoIvaLegatoDocumentoPresente())) {
			return true;
		}
		// Cerco sulle quote
		for(SubdocumentoSpesa ss : getListaSubdocumentoSpesa()) {
			if(ss.getSubdocumentoIva() != null && ss.getSubdocumentoIva().getUid() != 0) {
				return true;
			}
		}
		// Non ho legami
		return false;
	}

	/**
	 * Controlla se lo Stato Operativo del Documento sia incompleto.
	 * 
	 * @return <code>true</code> se lo stato operativo &eacute; INCOMPLETO; <code>false</code> in caso contrario
	 */
	public boolean getDocumentoIncompleto() {
		return getDocumento() != null && StatoOperativoDocumento.INCOMPLETO.equals(getDocumento().getStatoOperativoDocumento());
	}
	
	/**
	 * Controlla se il documento sia un documento di regolarizzazione figlio di un documento di entrata.
	 * 
	 * @return <code>true</code> se il documento &eacute; di regolarizzazione e figlio di un'entrata; <code>false</code> in caso contrario
	 */
	public boolean getDocumentoRegolarizzazioneFiglioDiEntrata() {
		return getDocumento() != null && getDocumento().getTipoDocumento() != null
				&& Boolean.TRUE.equals(getDocumento().getTipoDocumento().getFlagRegolarizzazione())
				&& Boolean.TRUE.equals(getFiglioDiDocumentoEntrata());
	}
	
	/**
	 * Controlla se lo stato del documento sia valido per l'inserimento di una nota di credito.
	 * 
	 * @return <code>true</code> se lo stato &eacute; valido; <code>false</code> in caso contrario 
	 */
	public boolean getCheckStatoValidoPerInserimentoNota() {
		if(getDocumento() == null) {
			return false;
		}
		StatoOperativoDocumento stato = getDocumento().getStatoOperativoDocumento();
		return StatoOperativoDocumento.INCOMPLETO.equals(stato) || StatoOperativoDocumento.VALIDO.equals(stato) || StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.equals(stato)
				|| StatoOperativoDocumento.PARZIALMENTE_EMESSO.equals(stato);
	}
	
	/**
	 * Ottiene l'importo del documento non rilevante per l'IVA.
	 * 
	 * @return l'importo non rilevante per l'IVA
	 */
	public BigDecimal getImportoNonRilevanteIva() {
		DocumentoSpesa doc = getDocumento();
		return doc == null ? BigDecimal.ZERO : doc.getImporto().subtract(doc.calcolaImportoTotaleRilevanteIVASubdoumenti());
	}
	
	/**
	 * Ottiene l'importo del documento rilevante per l'IVA.
	 * 
	 * @return l'importo rilevante per l'IVA
	 */
	public BigDecimal getImportoRilevanteIva() {
		DocumentoSpesa doc = getDocumento();
		return doc == null ? BigDecimal.ZERO : doc.calcolaImportoTotaleRilevanteIVASubdoumenti();
	}
	
	/**
	 * Controlla quale possa essere in progressivo del subdocumento e imposta il progressivo all'intero successivo, partendo da 1.
	 */
	public void checkAndIncrementProgressivoSubdocumento() {
		int max = 0;
		for(SubdocumentoSpesa s : listaSubdocumentoSpesa) {
			max = Math.max(max, s.getNumero().intValue());
		}
		
		setProgressivoNumeroSubdocumento(Integer.valueOf(max + 1));
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
	 * Forzo il calcolo dell'importo esente proposto. Da usare in seguito alle modifiche su un onere di natura esente.
	 */
	public void forzaCalcoloImportoEsenteProposto() {
		BigDecimal sum = BigDecimal.ZERO;
		for(DettaglioOnere don : getListaDettaglioOnere()) {
			if(don.getTipoOnere() != null && don.getTipoOnere().getNaturaOnere() != null
					&& BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant().equals(don.getTipoOnere().getNaturaOnere().getCodice())
					&& don.getImportoImponibile() != null) {
				sum = sum.add(don.getImportoImponibile());
			}
		}
		setImportoEsenteProposto(sum);
	}
	
	/**
	 * @return the totaleImportoOneriReverseChange
	 */
	public BigDecimal getTotaleImportoImponibileOneriReverseChange() {
		// Caso base
		if(getListaDettaglioOnere() == null) {
			return BigDecimal.ZERO;
		}
		BigDecimal sum = BigDecimal.ZERO;
		for(DettaglioOnere don : getListaDettaglioOnere()) {
			if(don.getTipoOnere() != null && TipoIvaSplitReverse.REVERSE_CHANGE.equals(don.getTipoOnere().getTipoIvaSplitReverse())) {
				sum = sum.add(don.getImportoImponibile());
			}
		}
		
		return sum;
	}
	
	/**
	 * @return the codiceEsenzione
	 */
	public String getCodiceEsenzione() {
		return BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant();
	}
	
	/**
	 * @return the codiceSplitReverse
	 */
	public String getCodiceSplitReverse() {
		return BilConstants.CODICE_NATURA_ONERE_SPLIT_REVERSE.getConstant();
	}
	
	/**
	 * @return the codiceReverseChange
	 */
	public String getCodiceReverseChange() {
		return TipoIvaSplitReverse.REVERSE_CHANGE.getCodice();
	}
	
	/**
	 * @return the statoOperativoDocumentoCompleto
	 */
	public boolean isStatoOperativoDocumentoCompleto() {
		return getDocumento() != null
				&& !StatoOperativoDocumento.INCOMPLETO.equals(getDocumento().getStatoOperativoDocumento())
				&& !StatoOperativoDocumento.ANNULLATO.equals(getDocumento().getStatoOperativoDocumento());
	}
	
	
	/**
	 * @return the tipoDocumentoNotaCredito
	 */
	public TipoDocumento getTipoDocumentoNotaCredito() {
		return tipoDocumentoNotaCredito;
	}

	/**
	 * @param tipoDocumentoNotaCredito the tipoDocumentoNotaCredito to set
	 */
	public void setTipoDocumentoNotaCredito(TipoDocumento tipoDocumentoNotaCredito) {
		this.tipoDocumentoNotaCredito = tipoDocumentoNotaCredito;
	}

	
	/**
	 * @return the listaStatoOperativoAtti
	 */
	public List<StatoOperativoAtti> getListaStatoOperativoAtti() {
		return listaStatoOperativoAtti;
	}

	/**
	 * @param listaStatoOperativoAtti the listaStatoOperativoAtti to set
	 */
	public void setListaStatoOperativoAtti(List<StatoOperativoAtti> listaStatoOperativoAtti) {
		this.listaStatoOperativoAtti = listaStatoOperativoAtti != null ? listaStatoOperativoAtti : new ArrayList<StatoOperativoAtti>();
	}

	/**
	 * @return the listaCausaleSospensione
	 */
	public List<CausaleSospensione> getListaCausaleSospensione() {
		return listaCausaleSospensione;
	}

	/**
	 * @param listaCausaleSospensione the listaCausaleSospensione to set
	 */
	public void setListaCausaleSospensione(List<CausaleSospensione> listaCausaleSospensione) {
		this.listaCausaleSospensione = listaCausaleSospensione != null ? listaCausaleSospensione : new ArrayList<CausaleSospensione>();
	}

	/**
	 * @return the datiFatturaPagataIncassataEditabili
	 */
	public boolean isDatiFatturaPagataIncassataEditabili() {
		return datiFatturaPagataIncassataEditabili;
	}

	/**
	 * @param datiFatturaPagataIncassataEditabili the datiFatturaPagataIncassataEditabili to set
	 */
	public void setDatiFatturaPagataIncassataEditabili(boolean datiFatturaPagataIncassataEditabili) {
		this.datiFatturaPagataIncassataEditabili = datiFatturaPagataIncassataEditabili;
	}

	/**
	 * @return the flagNoDatiSospensioneDecentrato
	 */
	public boolean isFlagNoDatiSospensioneDecentrato() {
		return flagNoDatiSospensioneDecentrato;
	}

	/**
	 * @param flagNoDatiSospensioneDecentrato the flagNoDatiSospensioneDecentrato to set
	 */
	public void setFlagNoDatiSospensioneDecentrato(boolean flagNoDatiSospensioneDecentrato) {
		this.flagNoDatiSospensioneDecentrato = flagNoDatiSospensioneDecentrato;
	}

	/**
	 * @return the dataScadenzaOriginale
	 */
	public Date getDataScadenzaOriginale() {
		return dataScadenzaOriginale;
	}

	/**
	 * @param dataScadenzaOriginale the dataScadenzaOriginale to set
	 */
	public void setDataScadenzaOriginale(Date dataScadenzaOriginale) {
		this.dataScadenzaOriginale = dataScadenzaOriginale;
	}

	/**
	 * @return the dataScadenzaDopoSospensioneOriginale
	 */
	public Date getDataScadenzaDopoSospensioneOriginale() {
		return dataScadenzaDopoSospensioneOriginale;
	}

	/**
	 * @param dataScadenzaDopoSospensioneOriginale the dataScadenzaDopoSospensioneOriginale to set
	 */
	public void setDataScadenzaDopoSospensioneOriginale(Date dataScadenzaDopoSospensioneOriginale) {
		this.dataScadenzaDopoSospensioneOriginale = dataScadenzaDopoSospensioneOriginale;
	}

	/**
	 * @return the listaSiopeDocumentoTipo
	 */
	public List<SiopeDocumentoTipo> getListaSiopeDocumentoTipo() {
		return listaSiopeDocumentoTipo;
	}

	/**
	 * @param listaSiopeDocumentoTipo the listaSiopeDocumentoTipo to set
	 */
	public void setListaSiopeDocumentoTipo(List<SiopeDocumentoTipo> listaSiopeDocumentoTipo) {
		this.listaSiopeDocumentoTipo = listaSiopeDocumentoTipo != null ? listaSiopeDocumentoTipo : new ArrayList<SiopeDocumentoTipo>();
	}

	/**
	 * @return the listaSiopeDocumentoTipoAnalogico
	 */
	public List<SiopeDocumentoTipoAnalogico> getListaSiopeDocumentoTipoAnalogico() {
		return listaSiopeDocumentoTipoAnalogico;
	}

	/**
	 * @param listaSiopeDocumentoTipoAnalogico the listaSiopeDocumentoTipoAnalogico to set
	 */
	public void setListaSiopeDocumentoTipoAnalogico(List<SiopeDocumentoTipoAnalogico> listaSiopeDocumentoTipoAnalogico) {
		this.listaSiopeDocumentoTipoAnalogico = listaSiopeDocumentoTipoAnalogico != null ? listaSiopeDocumentoTipoAnalogico : new ArrayList<SiopeDocumentoTipoAnalogico>();
	}

	/**
	 * @return the listaSiopeScadenzaMotivo
	 */
	public List<SiopeScadenzaMotivo> getListaSiopeScadenzaMotivo() {
		return listaSiopeScadenzaMotivo;
	}

	/**
	 * @param listaSiopeScadenzaMotivo the listaSiopeScadenzaMotivo to set
	 */
	public void setListaSiopeScadenzaMotivo(List<SiopeScadenzaMotivo> listaSiopeScadenzaMotivo) {
		this.listaSiopeScadenzaMotivo = listaSiopeScadenzaMotivo != null ? listaSiopeScadenzaMotivo : new ArrayList<SiopeScadenzaMotivo>();
	}

	/**
	 * @return the listaSiopeAssenzaMotivazione
	 */
	public List<SiopeAssenzaMotivazione> getListaSiopeAssenzaMotivazione() {
		return listaSiopeAssenzaMotivazione;
	}

	/**
	 * @param listaSiopeAssenzaMotivazione the listaSiopeAssenzaMotivazione to set
	 */
	public void setListaSiopeAssenzaMotivazione(List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione) {
		this.listaSiopeAssenzaMotivazione = listaSiopeAssenzaMotivazione != null ? listaSiopeAssenzaMotivazione : new ArrayList<SiopeAssenzaMotivazione>();
	}

	/**
	 * @return the listaSospensioneSubdocumento
	 */
	public List<SospensioneSubdocumento> getListaSospensioneSubdocumento() {
		return listaSospensioneSubdocumento;
	}

	/**
	 * @param listaSospensioneSubdocumento the listaSospensioneSubdocumento to set
	 */
	public void setListaSospensioneSubdocumento(List<SospensioneSubdocumento> listaSospensioneSubdocumento) {
		this.listaSospensioneSubdocumento = listaSospensioneSubdocumento != null ? listaSospensioneSubdocumento : new ArrayList<SospensioneSubdocumento>();
	}

	/**
	 * @return the sospensioneSubdocumento
	 */
	public SospensioneSubdocumento getSospensioneSubdocumento() {
		return sospensioneSubdocumento;
	}

	/**
	 * @param sospensioneSubdocumento the sospensioneSubdocumento to set
	 */
	public void setSospensioneSubdocumento(SospensioneSubdocumento sospensioneSubdocumento) {
		this.sospensioneSubdocumento = sospensioneSubdocumento;
	}

	/**
	 * @return the indexSospensioneQuota
	 */
	public Integer getIndexSospensioneQuota() {
		return indexSospensioneQuota;
	}

	/**
	 * @param indexSospensioneQuota the indexSospensioneQuota to set
	 */
	public void setIndexSospensioneQuota(Integer indexSospensioneQuota) {
		this.indexSospensioneQuota = indexSospensioneQuota;
	}

	/**
	 * @return the abilitatoGestioneAcquisti
	 */
	public boolean isAbilitatoGestioneAcquisti() {
		return abilitatoGestioneAcquisti;
	}

	/**
	 * @param abilitatoGestioneAcquisti the abilitatoGestioneAcquisti to set
	 */
	public void setAbilitatoGestioneAcquisti(boolean abilitatoGestioneAcquisti) {
		this.abilitatoGestioneAcquisti = abilitatoGestioneAcquisti;
	}

	/**
	 * @return the noDatiSospensioneDec
	 */
	public boolean isNoDatiSospensioneDec() {
		return noDatiSospensioneDec;
	}

	/**
	 * @param noDatiSospensioneDec the noDatiSospensioneDec to set
	 */
	public void setNoDatiSospensioneDec(boolean noDatiSospensioneDec) {
		this.noDatiSospensioneDec = noDatiSospensioneDec;
	}

	/**
	 * Checks if is show siope assenza motivazione.
	 *
	 * @return the hideSiopeAssenzaMotivazione
	 */
	public boolean isShowSiopeAssenzaMotivazione() {
		return showSiopeAssenzaMotivazione;
	}

	/**
	 * Sets the show siope assenza motivazione.
	 *
	 * @param showSiopeAssenzaMotivazione the new show siope assenza motivazione
	 */
	public void setShowSiopeAssenzaMotivazione(boolean showSiopeAssenzaMotivazione) {
		this.showSiopeAssenzaMotivazione = showSiopeAssenzaMotivazione;
	}

	/**
	 * @return the inibisciModificaDatiImportatiFEL
	 */
	public boolean isInibisciModificaDatiImportatiFEL() {
		return inibisciModificaDatiImportatiFEL;
	}

	/**
	 * @param inibisciModificaDatiImportatiFEL the inibisciModificaDatiImportatiFEL to set
	 */
	public void setInibisciModificaDatiImportatiFEL(boolean inibisciModificaDatiImportatiFEL) {
		this.inibisciModificaDatiImportatiFEL = inibisciModificaDatiImportatiFEL;
	}

	/**
	 * @return the uidCodiceUfficioDestinatarioPccToFilter
	 */
	public Integer getUidCodiceUfficioDestinatarioPccToFilter() {
		return uidCodiceUfficioDestinatarioPccToFilter;
	}

	/**
	 * @param uidCodiceUfficioDestinatarioPccToFilter the uidCodiceUfficioDestinatarioPccToFilter to set
	 */
	public void setUidCodiceUfficioDestinatarioPccToFilter(Integer uidCodiceUfficioDestinatarioPccToFilter) {
		this.uidCodiceUfficioDestinatarioPccToFilter = uidCodiceUfficioDestinatarioPccToFilter;
	}

	/**
	 * @return the listaCodicePCCFiltered
	 */
	public List<CodicePCC> getListaCodicePCCFiltered() {
		return listaCodicePCCFiltered;
	}

	/**
	 * @param listaCodicePCCFiltered the listaCodicePCCFiltered to set
	 */
	public void setListaCodicePCCFiltered(List<CodicePCC> listaCodicePCCFiltered) {
		this.listaCodicePCCFiltered = listaCodicePCCFiltered;
	}
	
	/**
	 * @return the messaggiSenzaRichiestaConferma
	 */
	public List<Messaggio> getMessaggiSenzaRichiestaConferma() {
		return messaggiSenzaRichiestaConferma;
	}

	/**
	 * @param messaggiSenzaRichiestaConferma the messaggiSenzaRichiestaConferma to set
	 */
	public void setMessaggiSenzaRichiestaConferma(List<Messaggio> messaggiSenzaRichiestaConferma) {
		this.messaggiSenzaRichiestaConferma = messaggiSenzaRichiestaConferma;
	}
	
	/**
	 * Adds the messaggio senza richiesta conferma.
	 *
	 * @param messaggio the messaggio
	 */
	public void addMessaggioSenzaRichiestaConferma(Messaggio messaggio) {
		if(getMessaggiSenzaRichiestaConferma() == null) {
			setMessaggiSenzaRichiestaConferma(new ArrayList<Messaggio>());
		}
		getMessaggiSenzaRichiestaConferma().add(messaggio);
	}

	// CR-2550
	/**
	 * @return the codicePccObbligatorio
	 */
	public boolean isCodicePccObbligatorio() {
		return getDocumento() != null && getDocumento().getTipoDocumento() != null && Boolean.TRUE.equals(getDocumento().getTipoDocumento().getFlagComunicaPCC());
	}
	
	/**
	 * @return the contabilizza
	 */
	public boolean isContabilizza(){
		return Boolean.TRUE.equals(getDocumento().getTipoDocumento().getFlagComunicaPCC()) && Boolean.TRUE.equals(getDocumento().getContabilizzaGenPcc());
	}
	
	// SIAC-4680
	/**
	 * @return nomeAzioneSAC the nomeAzioneSAC to set
	 */
	public String getNomeAzioneSAC() {
		return AzioneConsentitaEnum.DOCUMENTO_SPESA_AGGIORNA.getNomeAzione();
	}
	
	/**
	 * @return the tipoDocumentoSiopeAnalogico
	 */
	public boolean isTipoDocumentoSiopeAnalogico() {
		// Deve avere codice 'A'
		return getDocumento() != null
				&& getDocumento().getSiopeDocumentoTipo() != null
				&& BilConstants.CODICE_SIOPE_DOCUMENTO_TIPO_ANALOGICO.getConstant().equals(getDocumento().getSiopeDocumentoTipo().getCodice());
	}
	
	// SIAC-5115
	/**
	 * @return the datiSospensioneQuotaEditabili
	 */
	public boolean isDatiSospensioneQuotaEditabili() {
		return isAbilitatoGestioneAcquisti()
				&& getSubdocumento() != null
				&& getSubdocumento().getImpegnoOSubImpegno() != null
				&& getSubdocumento().getImpegnoOSubImpegno().getUid() != 0
				&& (
					!isNoDatiSospensioneDec()
					|| getSubdocumento().getLiquidazione() == null
					|| getSubdocumento().getLiquidazione().getUid() == 0
					|| StatoOperativoLiquidazione.ANNULLATO.equals(getSubdocumento().getLiquidazione().getStatoOperativoLiquidazione())
				);
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
	
	/* ***************** Requests ***************** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaDocumentoDiSpesa}.
	 *  
	 * @return la request creata
	 */
	public AggiornaDocumentoDiSpesa creaRequestAggiornaDocumentoDiSpesa() {
		AggiornaDocumentoDiSpesa request = new AggiornaDocumentoDiSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		DocumentoSpesa documentoSpesa = getDocumento();
		documentoSpesa.setSoggetto(getSoggetto());
		// Il tipo di impresa e' separato in quanto facoltativo
		documentoSpesa.setTipoImpresa(impostaEntitaFacoltativa(documento.getTipoImpresa()));
		// SIAC-4679
		documentoSpesa.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(documentoSpesa.getStrutturaAmministrativoContabile()));
		
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
	
	/**
	 * Crea una resuest per la ricerca impegno per chiave utilizzando il servizio {@link RicercaImpegnoPerChiaveOttimizzato}
	 * @param impegno l'impegno per cui comporre la request
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno impegno) {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		
		RicercaImpegnoK pRicercaImpegnoK = creaRicercaImpegnoK(impegno);
		pRicercaImpegnoK.setNumeroSubDaCercare(getSubMovimentoGestione()!= null? getSubMovimentoGestione().getNumeroBigDecimal() : null);
		request.setpRicercaImpegnoK( pRicercaImpegnoK);
		
		//il default e' carica sub -> true
		//se non ho indicato il subimpegno, non devo Caricare niente: 
		request.setCaricaSub(getSubMovimentoGestione()!= null && getSubMovimentoGestione().getNumeroBigDecimal()!= null);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		// SIAC-5546
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibilePagare(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO, TipologiaClassificatore.MACROAGGREGATO));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisorioDiCassaPerChiave creaRequestProvvisorioCassa() {
		RicercaProvvisorioDiCassaPerChiave request = new RicercaProvvisorioDiCassaPerChiave();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(getSubdocumento().getProvvisorioCassa().getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(getSubdocumento().getProvvisorioCassa().getNumero());
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
		request.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceQuotaDocumentoSpesa}.
	 *  
	 * @return la request creata
	 */
	public InserisceQuotaDocumentoSpesa creaRequestInserisceQuotaDocumentoSpesa() {
		InserisceQuotaDocumentoSpesa request = new InserisceQuotaDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentoSpesa(subdocumento);
		request.setBilancio(getBilancio());
		
		//per il ripeti
		getSubdocumento().setUid(0);
		
		getSubdocumento().setDocumento(creaDocumentoPerInserimentoQuota());
		getSubdocumento().setEnte(getEnte());
		AttoAmministrativo atto = getAttoAmministrativo();
		atto.setStrutturaAmmContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		atto.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		getSubdocumento().setAttoAmministrativo(impostaEntitaFacoltativa(atto));
		getSubdocumento().setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
		getSubdocumento().setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		
		getSubdocumento().setNoteTesoriere(impostaEntitaFacoltativa(getNoteTesoriere()));
		getSubdocumento().setContoTesoreria(impostaEntitaFacoltativa(getContoTesoreria()));
		getSubdocumento().setTipoAvviso(impostaEntitaFacoltativa(getTipoAvviso()));
		
		getSubdocumento().setImpegno(movimentoGestione);
		getSubdocumento().setSubImpegno(impostaEntitaFacoltativa(subMovimentoGestione));
		// Forzo a null per evitare problemi di mapping
		getSubdocumento().setProvvisorioCassa(impostaEntitaFacoltativa(subdocumento.getProvvisorioCassa()));
		
		getSubdocumento().setContoTesoreria(impostaEntitaFacoltativa(getContoTesoreria()));
		
		//SIAC-5469
		Impegno movgest = getSubMovimentoGestione() != null && getSubMovimentoGestione().getUid() != 0 ? getSubMovimentoGestione() : getMovimentoGestione();
		getSubdocumento().setSiopeTipoDebito(impostaEntitaFacoltativa(movgest.getSiopeTipoDebito()));
		
		if(getSubdocumento().getLiquidazione() != null && getSubdocumento().getLiquidazione().getUid() == 0){
			getSubdocumento().setLiquidazione(null);
		}
		// SIAC-5115
		request.setGestisciSospensioni(true);
		getSubdocumento().setSospensioni(getListaSospensioneSubdocumento());
		
		//SIAC-8153
		gestisciStrutturaContabileQuota(movgest);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceNotaCreditoSpesa} necessaria per l'introduzione della nota di credito.
	 * 
	 * @return la request creata
	 */
	public InserisceNotaCreditoSpesa creaRequestInserisceNotaCreditoSpesa() {
		InserisceNotaCreditoSpesa request = new InserisceNotaCreditoSpesa();
		
		for(DocumentoSpesa docPadre : notaCredito.getListaDocumentiSpesaPadre()){
			if(docPadre.getUid() == documento.getUid()){
				docPadre.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
			}
		}
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoSpesa(notaCredito);
		request.setBilancio(getBilancio());
		
		if(getAttoAmministrativo() != null && getAttoAmministrativo().getUid() != 0) {
			notaCredito.getListaSubdocumenti().get(0).setAttoAmministrativo(getAttoAmministrativo());
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaQuotaDocumentoSpesa}.
	 *  
	 * @return la request creata
	 */
	public EliminaQuotaDocumentoSpesa creaRequestEliminaQuotaDocumentoSpesa() {
		EliminaQuotaDocumentoSpesa request = new EliminaQuotaDocumentoSpesa();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentoSpesa(listaSubdocumentoSpesa.get(getRigaDaEliminare().intValue()));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaQuotaDocumentoSpesa}.
	 *  
	 * @return la request creata
	 */
	public AggiornaQuotaDocumentoSpesa creaRequestAggiornaQuotaDocumentoSpesa() {
		AggiornaQuotaDocumentoSpesa request = new AggiornaQuotaDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		AttoAmministrativo atto = getAttoAmministrativo();
		atto.setStrutturaAmmContabile(getStrutturaAmministrativoContabile());
		atto.setTipoAtto(getTipoAtto());
		
		getSubdocumento().setAttoAmministrativo(impostaEntitaFacoltativa(atto));
		getSubdocumento().setModalitaPagamentoSoggetto(getModalitaPagamentoSoggetto());
		
		request.setSubdocumentoSpesa(getSubdocumento());
		getSubdocumento().setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		getSubdocumento().setNoteTesoriere(impostaEntitaFacoltativa(getNoteTesoriere()));
		getSubdocumento().setContoTesoreria(impostaEntitaFacoltativa(getContoTesoreria()));
		getSubdocumento().setTipoAvviso(impostaEntitaFacoltativa(getTipoAvviso()));
		
		getSubdocumento().setImpegno(getMovimentoGestione());
		getSubdocumento().setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		
		// Campi da reinserire se cancellati
		getSubdocumento().setDocumento(creaDocumentoPerInserimentoQuota());
		getSubdocumento().setEnte(getEnte());
		
		//SIAC-5469
		Impegno movgest = getSubMovimentoGestione() != null && getSubMovimentoGestione().getUid()!= 0 ? getSubMovimentoGestione() : getMovimentoGestione();
		getSubdocumento().setSiopeTipoDebito(impostaEntitaFacoltativa(movgest.getSiopeTipoDebito()));
		
		getSubdocumento().setProvvisorioCassa(impostaEntitaFacoltativa(getSubdocumento().getProvvisorioCassa()));
		
		if(getSubdocumento().getLiquidazione() != null && getSubdocumento().getLiquidazione().getUid() == 0){
			getSubdocumento().setLiquidazione(null);
		}
		// SIAC-5115
		request.setGestisciSospensioni(true);
		getSubdocumento().setSospensioni(getListaSospensioneSubdocumento());
		
		//SIAC-8153
		gestisciStrutturaContabileQuota(movgest);
		
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaQuotaDocumentoSpesa}.
	 * 
	 * @param subdoc il subdocumento per cui creare la request
	 *  
	 * @return la request creata
	 */
	public AggiornaQuotaDocumentoSpesa creaRequestAggiornaQuotaDocumentoSpesa(SubdocumentoSpesa subdoc) {
		AggiornaQuotaDocumentoSpesa request = new AggiornaQuotaDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		request.setSubdocumentoSpesa(subdoc);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoSpesa}.
	 *  
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoSpesa creaRequestRicercaQuoteByDocumentoSpesa() {
		RicercaQuoteByDocumentoSpesa request = new RicercaQuoteByDocumentoSpesa();
		request.setDataOra(new Date());
		request.setDocumentoSpesa(documento);
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisciOnereSpesa}.
	 *  
	 * @return la request creata
	 */
	public InserisciOnereSpesa creaRequestInserisciOnereSpesa() {
		InserisciOnereSpesa request = new InserisciOnereSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setDettaglioOnere(creaDettaglioOnerePerInserimento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaOnereSpesa}.
	 *  
	 * @return la request creata
	 */
	public AggiornaOnereSpesa creaRequestAggiornaOnereSpesa() {
		AggiornaOnereSpesa request = new AggiornaOnereSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDettaglioOnere(creaDettaglioOnerePerAggiornamento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaOnereSpesa}.
	 *  
	 * @return la request creata
	 */
	public EliminaOnereSpesa creaRequestEliminaOnereSpesa() {
		EliminaOnereSpesa request = new EliminaOnereSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDettaglioOnere(dettaglioOnere);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaOnereByDocumentoSpesa}.
	 *  
	 * @return la request creata
	 */
	public RicercaOnereByDocumentoSpesa creaRequestRicercaOnereByDocumentoSpesa() {
		RicercaOnereByDocumentoSpesa request = new RicercaOnereByDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(documento.getUid());
		request.setDocumentoSpesa(documentoSpesa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AnnullaDocumentoSpesa}.
	 * 
	 * @param uid l'uid della nota da annullare
	 *  
	 * @return la request creata
	 */
	public AnnullaNotaCreditoSpesa creaRequestAnnullaNotaCredito(Integer uid) {
		AnnullaNotaCreditoSpesa request = new AnnullaNotaCreditoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		DocumentoSpesa nota = new DocumentoSpesa();
		nota.setEnte(getEnte());
		nota.setUid(uid);
		
		request.setDocumentoSpesa(nota);

		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaNotaCreditoSpesa}.
	 *  
	 * @return la request creata
	 */
	public AggiornaNotaCreditoSpesa creaRequestAggiornaNotaCreditoSpesa() {
		AggiornaNotaCreditoSpesa request = new AggiornaNotaCreditoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		notaCredito.setEnte(getEnte());
		notaCredito.getListaSubdocumenti().get(0).setAttoAmministrativo(getAttoAmministrativo());
//		notaCredito.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
		for(DocumentoSpesa docPadre : notaCredito.getListaDocumentiSpesaPadre()){
			if(docPadre.getUid() == documento.getUid()){
				docPadre.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
			}
		}
		request.setBilancio(getBilancio());
		
		request.setDocumentoSpesa(notaCredito);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaImportiQuoteDocumentoSpesa}.
	 *  
	 * @return la request creata
	 */
	public AggiornaImportiQuoteDocumentoSpesa creaRequestAggiornaImportiQuoteDocumentoSpesa() {
		AggiornaImportiQuoteDocumentoSpesa request = new AggiornaImportiQuoteDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentiSpesa(listaSubdocumentoSpesa);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaSpesa}.
	 * 
	 * @param sdis il subdocumentoIva rispetto cui creare la request
	 *  
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaSpesa creaRequestRicercaDettaglioSubdocumentoIvaSpesa(SubdocumentoIvaSpesa sdis) {
		RicercaDettaglioSubdocumentoIvaSpesa request = new RicercaDettaglioSubdocumentoIvaSpesa();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentoIvaSpesa(sdis);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodiceUfficioDestinatarioPCC}.
	 * 
	 * @param struttureAmministrativoContabili la lista delle strutture per cui filtrare
	 * @return la request creata
	 */
	public RicercaCodiceUfficioDestinatarioPCC creaRequestRicercaCodiceUfficioDestinatarioPCC(List<StrutturaAmministrativoContabile> struttureAmministrativoContabili) {
		RicercaCodiceUfficioDestinatarioPCC request = creaRequest(RicercaCodiceUfficioDestinatarioPCC.class);
		
		request.setStruttureAmministrativoContabili(struttureAmministrativoContabili);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodicePCC}.
	 * 
	 * @param struttureAmministrativoContabili la lista delle strutture per cui filtrare
	 * @return la request creata
	 */
	public RicercaCodicePCC creaRequestRicercaCodicePCC(List<StrutturaAmministrativoContabile> struttureAmministrativoContabili) {
		RicercaCodicePCC request = creaRequest(RicercaCodicePCC.class);
		
		request.setStruttureAmministrativoContabili(struttureAmministrativoContabili);
		
		CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC = new CodiceUfficioDestinatarioPCC();
		//sara' sempre null tranne quando viene richiamato dal javascript
		codiceUfficioDestinatarioPCC.setUid(getUidCodiceUfficioDestinatarioPccToFilter()!= null? getUidCodiceUfficioDestinatarioPccToFilter().intValue() : 0 );
		request.setCodiceUfficioDestinatarioPCC(codiceUfficioDestinatarioPCC);
		
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
	
	/* *************************************************************************************************************************** */
	
	/**
	 * Imposta i flags per l'apertura dei var&icirc; tabs.
	 * 
	 * @param azioneIvaGestibile         se l'azione di gestione dell'IVA sia gestibile
	 * @param datiSospensioneEditabili   se i dati di sospensione siano editabili
	 * @param datiFatturaPagataEditabili se i dati della fattura pagata siano editabili
	 */
	public void impostaFlags(Boolean azioneIvaGestibile, Boolean datiSospensioneEditabili, boolean datiFatturaPagataEditabili) {
		setAzioneIvaGestibile(azioneIvaGestibile);
		DocumentoSpesa documentoSpesa = getDocumento();
		TipoDocumento tipoDocumento = documentoSpesa.getTipoDocumento();
		setFlagRitenuteAccessibile(tipoDocumento.getFlagRitenute());
		setFlagPenaleAltroAccessibile(tipoDocumento.getFlagPenaleAltro());
		setFlagNoteCreditoAccessibile(tipoDocumento.getFlagNotaCredito());
		
		setFlagDatiSospensioneEditabili(datiSospensioneEditabili);
		// SIAC-4749
		setDatiFatturaPagataIncassataEditabili(datiFatturaPagataEditabili);
		
		impostaFlagIvaGestibile(documentoSpesa, documentoSpesa.getListaSubdocumenti());
	}
	
	/**
	 * Rieffettua il calcolo del totale delle quote del documento.
	 */
	public void ricalcolaTotaliQuote() {
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
		
		for(SubdocumentoSpesa sds : listaSubdocumentoSpesa) {
			totaleQuote = totaleQuote.add(sds.getImporto());
			totaleDaPagareQuote = totaleDaPagareQuote.add(sds.getImportoDaPagare());
		}
		setTotaleQuote(totaleQuote);
		setTotaleDaPagareQuote(totaleDaPagareQuote);
		setImportoDaAttribuire(getDocumento().getImporto().add(getDocumento().getArrotondamento()).add(ricalcolaTotaleImponibileOneriRC()).subtract(totaleQuote));
	}
	
	/**
	 * Ricalcola il flag per i dati iva
	 */
	public void ricalcolaFlagIva() {
		impostaFlagIvaGestibile(getDocumento(), getListaSubdocumentoSpesa());
	}
	
	/* *************************************************************************************************************************************** */

	
	
	/**
	 * Calcolo del totale imponibile importo degli oneri con TipoIvaSplitReverse=RC
	 * 
	 * @return il totale calcolato
	 */
	public BigDecimal ricalcolaTotaleImponibileOneriRC() {
		BigDecimal totaleOneriRC = BigDecimal.ZERO;
		for (DettaglioOnere don : listaDettaglioOnere) {
			if (don.getTipoOnere() != null 
				&& don.getTipoOnere().getTipoIvaSplitReverse() != null 
				&& TipoIvaSplitReverse.REVERSE_CHANGE.equals(don.getTipoOnere().getTipoIvaSplitReverse())
				&& don.getImportoCaricoSoggetto() != null) {
				
				totaleOneriRC = totaleOneriRC.add(don.getImportoCaricoSoggetto());
			}
		}
		return totaleOneriRC;
	}
	
	
	/**
	 * Crea un'utility per la ricerca dell'impegno.
	 * 
	 * @param impegno l'impegno rispetto cui creare l'utility
	 * 
	 * @return l'utility creata
	 */
	private RicercaImpegnoK creaRicercaImpegnoK(Impegno impegno) {
		RicercaImpegnoK utility = new RicercaImpegnoK();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoImpegno(impegno.getAnnoMovimento());
		utility.setNumeroImpegno(impegno.getNumeroBigDecimal());
		
		return utility;
	}
	
	/**
	 * Crea un documento di spesa per l'inserimento della quota, avente valorizzato soltanto il parametro <code>uid</code>.
	 * 
	 * @return il documento per l'injezione nella request
	 */
	private DocumentoSpesa creaDocumentoPerInserimentoQuota() {
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(getDocumento().getUid());
		return documentoSpesa;
	}
	
	/**
	 * SIAC-8153
	 * 
	 * Gestisce la creazione o meno della Struttura competente associata alla quota.
	 * L'entita' e' facoltativa, se presente un impegno e per la quota non e' specificata
	 * una struttura, eredito quella dell'impegno.
	 * In assenza di entrambe la quota non eredita nulla.
	 * 
	 * @param movgest
	 */
	private void gestisciStrutturaContabileQuota(Impegno movgest) {
		if((movgest != null && StringUtils.isNotBlank(movgest.getStrutturaCompetente()) 
				&& movgest.getStrutturaCompetenteLetta() != null) || (getStrutturaCompetenteQuota() != null &&
				getStrutturaCompetenteQuota().getUid() != 0)) {
			getSubdocumento().setStrutturaCompetenteQuota(creaStrutturaAmministrativoContabileQuota(movgest));
		}
	}

	/**
	 * SIAC-8153
	 * 
	 * Crea una struttura amministrativa contabile per l'inserimento della quota 
	 *  
	 * @return la struttura amministrativa contabile per l'injezione nella request
	 */
	private StrutturaAmministrativoContabile creaStrutturaAmministrativoContabileQuota(Impegno impegno) {
		StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
		if(getStrutturaCompetenteQuota() != null && getStrutturaCompetenteQuota().getUid() != 0) {
			struttura = getStrutturaCompetenteQuota();
		} else {
			struttura = impegno.getStrutturaCompetenteLetta();
			struttura.setUid(new Integer(impegno.getStrutturaCompetente()));
		}
		return struttura;
	}
	
	/* Utility */
	/**
	 * Imposta i totali delle note e afferenti
	 * 
	 */
	public void impostaTotaliNoteQuote() {
		this.setTotaleQuoteNotaCredito(calcolaTotaleQuote().get(0));
		this.setTotaleImportoDaDedurre(calcolaTotaleQuote().get(1));
		this.setTotaleNoteCredito(calcolaTotaleQuote().get(2));
		this.setTotaleImportoDaDedurreSuFattura(calcolaTotaleQuote().get(3));
	}
	
	/**
	 * Calcola i totali da restituire alla pagina
	 * 
	 * @return  una lista con i due totali
	 */
	private List<BigDecimal> calcolaTotaleQuote() {	
		BigDecimal totale= BigDecimal.ZERO;
		BigDecimal totaleImpoDaDedurre= BigDecimal.ZERO;
		BigDecimal totaleNote = BigDecimal.ZERO;
		BigDecimal totaleDaDedurreSuFattura = BigDecimal.ZERO;
	
		List<BigDecimal> vRet = new ArrayList<BigDecimal>();
		for(SubdocumentoSpesa s: getListaSubdocumentoSpesa()){
			if(s.getImporto() == null){
				s.setImporto(BigDecimal.ZERO);
			}
			if(s.getImportoDaDedurre() == null){
				s.setImportoDaDedurre(BigDecimal.ZERO);
			}
			totale = totale.add(s.getImporto());
			totaleImpoDaDedurre = totaleImpoDaDedurre.add(s.getImportoDaDedurreNotNull());
		}
		
		for(DocumentoSpesa ds : documento.getListaDocumentiSpesaFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(ds.getTipoDocumento().getCodiceGruppo())
				 && !StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento())	) {
				totaleNote = totaleNote.add(ds.getImporto());
				totaleDaDedurreSuFattura = totaleDaDedurreSuFattura.add(ds.getImportoDaDedurreSuFattura());
			}
		}
		
		vRet.add(totale);
		vRet.add(totaleImpoDaDedurre);
		vRet.add(totaleNote);
		vRet.add(totaleDaDedurreSuFattura);
		return vRet;
	}
	
	
	@Override
	public void calcoloImporti() {
		BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
		BigDecimal totaleDaPagareQuoteSenzaOrdinativo = BigDecimal.ZERO;
		
		
		// ANAGRAFICA
		BigDecimal penaleAltro = BigDecimal.ZERO;
		BigDecimal netto = BigDecimal.ZERO;
		
		// TABS SEGUENTI
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleNoteCredito = BigDecimal.ZERO;
		BigDecimal totaleDaDedurre = BigDecimal.ZERO;
		BigDecimal totaleImportoDaDedurraSuFattura = BigDecimal.ZERO;
		
		for(DocumentoSpesa ds : documento.getListaDocumentiSpesaFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(ds.getTipoDocumento().getCodiceGruppo())
				&& !StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento())	) {
				totaleNoteCredito = totaleNoteCredito.add(ds.getImporto());
				totaleImportoDaDedurraSuFattura = totaleImportoDaDedurraSuFattura.add(ds.getImportoDaDedurreSuFattura());
			}
		}
		for(DocumentoEntrata de : documento.getListaDocumentiEntrataFiglio()) {
			if(!StatoOperativoDocumento.ANNULLATO.equals(de.getStatoOperativoDocumento())){
				penaleAltro = penaleAltro.add(de.getImporto());
			}
		}
		
		for(SubdocumentoSpesa sub : documento.getListaSubdocumenti()) {
			totaleQuote = totaleQuote.add(sub.getImporto());
			totaleDaDedurre = totaleDaDedurre.add(sub.getImportoDaDedurreNotNull());
			//vedere come valorizzare importo da pagare, campo derivato
			totaleDaPagareQuote = totaleDaPagareQuote.add(sub.getImportoDaPagare());
			if(sub.getOrdinativo() == null || sub.getOrdinativo().getUid() ==0){
				totaleDaPagareQuoteSenzaOrdinativo = totaleDaPagareQuoteSenzaOrdinativo.add(sub.getImportoDaPagare());
			}
			
		}
		
		setTotaleDaPagareQuote(totaleDaPagareQuote);
		setTotaleDaPagareQuoteSenzaOrdinativo(totaleDaPagareQuoteSenzaOrdinativo);
//		setNoteCreditoDocumento(totaleNoteCredito);
		setPenaleAltroDocumento(penaleAltro);
		// netto = importo + arrotondamento - noteCredito
		netto = documento.getImporto().add(documento.getArrotondamento()).add(ricalcolaTotaleImponibileOneriRC()).subtract(totaleImportoDaDedurraSuFattura);
		impostaNetto(netto);
		// importoDaAttribuire = netto - totaleQuote + totimponibile oneri se splireverse=RC
		setImportoDaAttribuire(getDocumento().getImporto().add(getDocumento().getArrotondamento()).add(ricalcolaTotaleImponibileOneriRC()).subtract(totaleQuote));
		setTotaleQuote(totaleQuote);
		setTotaleNoteCredito(totaleNoteCredito);
		setTotaleImportoDaDedurre(totaleDaDedurre);
		setTotaleImportoDaDedurreSuFattura(totaleImportoDaDedurraSuFattura);
		
		
		// Ritenute
		RitenuteDocumento ritenuteDocumento = documento.getRitenuteDocumento();
		if(ritenuteDocumento != null) {
			// Totali
			BigDecimal totaleRitenute = BigDecimal.ZERO;
			// totale = esente + rivalsa + cassa pensioni + iva 
			totaleRitenute = totaleRitenute.add(ritenuteDocumento.getImportoEsente())
				.add(ritenuteDocumento.getImportoRivalsa())
				.add(ritenuteDocumento.getImportoCassaPensioni())
				.add(ritenuteDocumento.getImportoIVA());
			setTotaleRitenuteDocumento(totaleRitenute);
		}
	}
	
	
	/**
	 * Crea un dettaglio onere per l'inserimento dello stesso.
	 * 
	 * @return il dettaglio creato
	 */
	private DettaglioOnere creaDettaglioOnerePerInserimento() {
		DettaglioOnere dettaglio = this.dettaglioOnere;
		
		dettaglio.setEnte(getEnte());
		dettaglio.getTipoOnere().setEnte(getEnte());
		
		if(attivitaOnere != null && attivitaOnere.getUid() != 0) {
			dettaglio.setAttivitaOnere(attivitaOnere);
		}
		
		if(getCausale770() != null && getCausale770().getUid() != 0) {
			dettaglio.setCausale770(getCausale770());
		}
		
		if(getTipoSommaNonSoggetta() != null && getTipoSommaNonSoggetta().getUid() != 0){
			dettaglio.setCodiceSommaNonSoggetta(getTipoSommaNonSoggetta());
		}
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(documento.getUid());
		dettaglio.setDocumentoSpesa(documentoSpesa);
		
		return dettaglio;
	}
	
	/**
	 * Crea un dettaglio onere per l'aggiornamento dello stesso.
	 * 
	 * @return il dettaglio creato
	 */
	private DettaglioOnere creaDettaglioOnerePerAggiornamento() {
		DettaglioOnere dettaglio = this.dettaglioOnere;
		
		if(attivitaOnere != null && attivitaOnere.getUid() != 0) {
			dettaglio.setAttivitaOnere(attivitaOnere);
		}
		
		if(getCausale770() != null && getCausale770().getUid() != 0) {
			dettaglio.setCausale770(getCausale770());
		}
		
		if(getTipoSommaNonSoggetta() != null && getTipoSommaNonSoggetta().getUid() != 0){
			dettaglio.setCodiceSommaNonSoggetta(getTipoSommaNonSoggetta());
		}
		
		return dettaglio;
	}
	
	/**
	 * Crea una request per il servizio di {@link AttivaRegistrazioniContabiliSpesa}.
	 * 
	 * @return la request creata
	 */
	public AttivaRegistrazioniContabiliSpesa creaRequestAttivaRegistrazioniContabiliSpesa() {
		AttivaRegistrazioniContabiliSpesa request = creaRequest(AttivaRegistrazioniContabiliSpesa.class);
		request.setDocumentoSpesa(getDocumento());
		//SIAC-5333
		request.setSaltaInserimentoPrimaNota(getValidazionePrimaNotaDaDocumento());
		request.setBilancio(getBilancio());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ProporzionaImportiSplitReverse}.
	 * 
	 * @return la request creata
	 */
	public ProporzionaImportiSplitReverse creaRequestProporzionaImportiSplitReverse() {
		ProporzionaImportiSplitReverse request = creaRequest(ProporzionaImportiSplitReverse.class);
		request.setDocumentoSpesa(getDocumento());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSommaNonSoggetta}.
	 * @param tipoOnere il tipo di onere per cui creare la request
	 * 
	 * @return la request creata
	 */
	public RicercaSommaNonSoggetta creaRequestRicercaSommaNonSoggetta(TipoOnere tipoOnere) {
		RicercaSommaNonSoggetta req = creaRequest(RicercaSommaNonSoggetta.class);
		req.setTipoOnere(tipoOnere);
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodifiche}.
	 * 
	 * @param codifiche le codifiche da cercare
	 * @return la request creata
	 */
	public RicercaCodifiche creaRequestRicercaCodifiche(Class<? extends Codifica>... codifiche) {
		RicercaCodifiche req = creaRequest(RicercaCodifiche.class);
		req.addTipiCodifica(codifiche);
		return req;
	}

/*
	public void setEditDateteFromPccActive(boolean editDateteFromPccActive) {
		this.editDateteFromPccActive = editDateteFromPccActive;
	}
	
	public boolean isEditDateteFromPccActive() {
		boolean ris = false;
		boolean contabilizzaGenPcc = getDocumento().getContabilizzaGenPcc();// se è true devo editare le date 		
		log.info("controllaQuotaConDataSospensioneDaAbilitareCausaGenPcc", "contabilizzaGenPcc "+ contabilizzaGenPcc);		
		boolean flagPcc = getDocumento().getTipoDocumento().getFlagComunicaPCC(); // false devo editare le date 	
		
		if (contabilizzaGenPcc || flagPcc){
			ris = true;
		}
		return ris;
	}
	*/
	
	/**
	 * @return the editDateFromPccActive
	 */
	public boolean isEditDateFromPccActive() {
		boolean ris = false;

		// false devo editare le date perchè il documento non puo' essere contabilizzato
		boolean documentoContabilizabile = Boolean.TRUE.equals(getDocumento().getTipoDocumento().getFlagComunicaPCC()); 	
		log.info("isEditDateFromPccActive", "documentoContabilizabile "+ documentoContabilizabile);

		// se e' true devo editare le date
		boolean contabilizzaGenPcc = Boolean.TRUE.equals(getDocumento().getContabilizzaGenPcc()); 		
		log.info("isEditDateFromPccActive", "contabilizzaGenPcc "+ contabilizzaGenPcc);

		
		// se il documento NON è contabilizzabile la data è sempre editabile
		if (!documentoContabilizabile || contabilizzaGenPcc ){
			ris = true;
		}
		return ris;
		//return editDateteFromPccActive;
	}
	
	//SIAC-8853
	public AccreditoTipoOilIsPagoPA creaRequestAccreditoTipoOilIsPagoPA() {
		return creaRequest(AccreditoTipoOilIsPagoPA.class);
	}
}
