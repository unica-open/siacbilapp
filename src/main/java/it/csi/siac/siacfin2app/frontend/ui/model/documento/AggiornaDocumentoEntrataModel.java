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

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model per l'aggiornamento del documento di entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 12/03/2014
 *
 */
public class AggiornaDocumentoEntrataModel extends AggiornaDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5642500162717912789L;
	
	private Accertamento movimentoGestione;
	private SubAccertamento subMovimentoGestione;
	private CapitoloEntrataGestione capitolo;
	private ContoTesoreria contoTesoreria;
		
	private DocumentoEntrata documento;
	private SubdocumentoEntrata subdocumento;
	
	private DocumentoEntrata notaCredito;
	
	private SubdocumentoIvaEntrata subdocumentoIva;
	
	// Quote
	private List<SubdocumentoEntrata> listaSubdocumentoEntrata = new ArrayList<SubdocumentoEntrata>();
	// Documenti collegati
	private List<ElementoDocumento> listaDocumentoSpesa = new ArrayList<ElementoDocumento>();
	// Note credito
	private List<ElementoDocumento> listaDocumentoEntrata = new ArrayList<ElementoDocumento>();
	// Quote rilevanti Iva
	private List<ElementoSubdocumentoIvaEntrata> listaQuoteRilevantiIva = new ArrayList<ElementoSubdocumentoIvaEntrata>();
	
	// lista tipi finanziamento
	private List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();

	private BigDecimal documentiCollegatiDocumento = BigDecimal.ZERO;
	
	private BigDecimal totaleDocumentiCollegati = BigDecimal.ZERO;
	private BigDecimal totaleSpesa = BigDecimal.ZERO;
	
	private Boolean flagDocumentiCollegatiAccessibile = Boolean.TRUE;
	
	//LOTTO O
	private TipoDocumento tipoDocumentoNotaCredito;
	
	private boolean attivaRegistrazioniContabiliVisible = false;
	//SIAC-5808
	private boolean esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota = false;
	private TipoEvento tipoEventoAccertamento; 
	
	//SIAC-6645
	private boolean forzaDisponibilitaAccertamento = false;
	private Messaggio messaggioConfermaSfondamentoAccertamento;
	
	//SIAC-7562 - 25/06/2020 - CM e GM
	private String statoSDIdescrizione;
	//SIAC-7562 - 06/07/2020 - CM
	private Date dataCambioStatoFel;
	
	//SIAC-6988 - 06/10/2020 FL
	private Boolean flagStatoSDIInviatoFEL = Boolean.FALSE;
	

	/** Costruttore vuoto di default */
	public AggiornaDocumentoEntrataModel() {
		setTitolo("Aggiorna Documenti di Entrata");
	}

	
	
	
	/**
	 * @return the flagStatoSDIInviatoFEL
	 */
	public Boolean getFlagStatoSDIInviatoFEL()
	{
		return flagStatoSDIInviatoFEL;
	}




	/**
	 * @param flagStatoSDIInviatoFEL the flagStatoSDIInviatoFEL to set
	 */
	public void setFlagStatoSDIInviatoFEL(Boolean flagStatoSDIInviatoFEL)
	{
		this.flagStatoSDIInviatoFEL = flagStatoSDIInviatoFEL;
	}




	/**
	 * @return the movimentoGestione
	 */
	public Accertamento getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(Accertamento movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubAccertamento getSubMovimentoGestione() {
		return subMovimentoGestione;
	}

	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(SubAccertamento subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}
	
	/**
	 * @return the stato
	 */
	public String getStato() {
		if(getDocumento() == null || getDocumento().getStatoOperativoDocumento() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Stato documento: ")
			.append(documento.getStatoOperativoDocumento().getDescrizione())
			.append(" dal: ")
			.append(FormatUtils.formatDate(documento.getDataInizioValiditaStato()));
		return sb.toString();
	}
	/**
	 * @return the capitolo
	 */
	public CapitoloEntrataGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(CapitoloEntrataGestione capitolo) {
		this.capitolo = capitolo;
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
	 * @return the subdocumento
	 */
	public SubdocumentoEntrata getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento the subdocumento to set
	 */
	public void setSubdocumento(SubdocumentoEntrata subdocumento) {
		this.subdocumento = subdocumento;
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
	 * @return the subdocumentoIva
	 */
	public SubdocumentoIvaEntrata getSubdocumentoIva() {
		return subdocumentoIva;
	}

	/**
	 * @param subdocumentoIva the subdocumentoIva to set
	 */
	public void setSubdocumentoIva(SubdocumentoIvaEntrata subdocumentoIva) {
		this.subdocumentoIva = subdocumentoIva;
	}

	/**
	 * @return the listaSubdocumentoEntrata
	 */
	public List<SubdocumentoEntrata> getListaSubdocumentoEntrata() {
		return listaSubdocumentoEntrata;
	}

	/**
	 * @param listaSubdocumentoEntrata the listaSubdocumentoEntrata to set
	 */
	public void setListaSubdocumentoEntrata(
			List<SubdocumentoEntrata> listaSubdocumentoEntrata) {
		this.listaSubdocumentoEntrata = listaSubdocumentoEntrata;
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
		this.listaDocumentoEntrata = listaDocumentoEntrata;
	}
	
	/**
	 * @return the listaQuoteRilevantiIva
	 */
	public List<ElementoSubdocumentoIvaEntrata> getListaQuoteRilevantiIva() {
		return listaQuoteRilevantiIva;
	}

	/**
	 * @param listaQuoteRilevantiIva the listaQuoteRilevantiIva to set
	 */
	public void setListaQuoteRilevantiIva(List<ElementoSubdocumentoIvaEntrata> listaQuoteRilevantiIva) {
		this.listaQuoteRilevantiIva = listaQuoteRilevantiIva;
	}

	/**
	 * @return the listaTipiFinanziamento
	 */
	public List<TipoFinanziamento> getListaTipiFinanziamento() {
		return listaTipiFinanziamento;
	}

	/**
	 * @param listaTipiFinanziamento the listaTipiFinanziamento to set
	 */
	public void setListaTipiFinanziamento(List<TipoFinanziamento> listaTipiFinanziamento) {
		this.listaTipiFinanziamento = listaTipiFinanziamento != null ? listaTipiFinanziamento : new ArrayList<TipoFinanziamento>();
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
		this.listaDocumentoSpesa = listaDocumentoSpesa;
	}

	/**
	 * @return the documentiCollegatiDocumento
	 */
	public BigDecimal getDocumentiCollegatiDocumento() {
		return documentiCollegatiDocumento;
	}

	/**
	 * @param documentiCollegatiDocumento the documentiCollegatiDocumento to set
	 */
	public void setDocumentiCollegatiDocumento(
			BigDecimal documentiCollegatiDocumento) {
		this.documentiCollegatiDocumento = documentiCollegatiDocumento;
	}

	/**
	 * @return the totaleDocumentiCollegati
	 */
	public BigDecimal getTotaleDocumentiCollegati() {
		return totaleDocumentiCollegati;
	}

	/**
	 * @return the totaleSpesa
	 */
	public BigDecimal getTotaleSpesa() {
		return totaleSpesa;
	}

	/**
	 * @param totaleSpesa the totaleSpesa to set
	 */
	public void setTotaleSpesa(BigDecimal totaleSpesa) {
		this.totaleSpesa = totaleSpesa;
	}

	/**
	 * @param totaleDocumentiCollegati the totaleDocumentiCollegati to set
	 */
	public void setTotaleDocumentiCollegati(BigDecimal totaleDocumentiCollegati) {
		this.totaleDocumentiCollegati = totaleDocumentiCollegati;
	}
	
	/**
	 * @return the flagDocumentiCollegatiAccessibile
	 */
	public Boolean getFlagDocumentiCollegatiAccessibile() {
		return flagDocumentiCollegatiAccessibile;
	}

	/**
	 * @param flagDocumentiCollegatiAccessibile the flagDocumentiCollegatiAccessibile to set
	 */
	public void setFlagDocumentiCollegatiAccessibile(Boolean flagDocumentiCollegatiAccessibile) {
		this.flagDocumentiCollegatiAccessibile = flagDocumentiCollegatiAccessibile;
	}

	
	
	/**
	 * @return <code>true</code> se il documento &eacute; gi&agrave; legato a un subdocumento iva
	 *   (direttamente o tramite quote); <code>false</code> in caso contrario
	 */
	public Boolean getLegameConIvaPresente() {
		// Se ho il documento iva legato al capitolo, va già bene
		if(Boolean.TRUE.equals(getDocumentoIvaLegatoDocumentoPresente())) {
			return Boolean.TRUE;
		}
		// Cerco sulle quote
		for(SubdocumentoEntrata se : getListaSubdocumentoEntrata()) {
			if(se.getSubdocumentoIva() != null && se.getSubdocumentoIva().getUid() != 0) {
				return Boolean.TRUE;
			}
		}
		// Non ho legami
		return Boolean.FALSE;
	}

	/**
	 * Controlla se lo Stato Operativo del Documento sia incompleto.
	 * 
	 * @return <code>true</code> se lo stato operativo &eacute; INCOMPLETO; <code>false</code> in caso contrario
	 */
	public Boolean getDocumentoIncompleto() {
		return getDocumento() != null && StatoOperativoDocumento.INCOMPLETO.equals(getDocumento().getStatoOperativoDocumento());
	}
	
	/**
	 * Controlla se il documento sia un documento di regolarizzazione figlio di un documento di entrata.
	 * 
	 * @return <code>true</code> se il documento &eacute; di regolarizzazione e figlio di un'entrata; <code>false</code> in caso contrario
	 */
	public Boolean getDocumentoRegolarizzazioneFiglioDiEntrata() {
		return getDocumento() != null && getDocumento().getTipoDocumento() != null
				&& Boolean.TRUE.equals(getDocumento().getTipoDocumento().getFlagRegolarizzazione())
				&& Boolean.TRUE.equals(getFiglioDiDocumentoEntrata());
	}
	
	/**
	 * Controlla se lo stato del documento sia valido per l'inserimento di una nota di credito.
	 * 
	 * @return <code>true</code> se lo stato &eacute; valido; <code>false</code> in caso contrario 
	 */
	public Boolean getCheckStatoValidoPerInserimentoNota() {
		if(getDocumento() == null) {
			return Boolean.FALSE;
		}
		
		//SIAC-6988 Inizio FL
		if (StatoSDIDocumento.DECORR_TERMINI.getCodice().equalsIgnoreCase(getDocumento().getStatoSDI())  ||
			StatoSDIDocumento.ACCET_CONSEG.getCodice().equalsIgnoreCase(getDocumento().getStatoSDI())  ||
			StatoSDIDocumento.INVIATA_FEL.getCodice().equalsIgnoreCase(getDocumento().getStatoSDI())
			) {
			return Boolean.TRUE;
		}
		//SIAC-6988 Fine FL		
		StatoOperativoDocumento stato = getDocumento().getStatoOperativoDocumento();
		return StatoOperativoDocumento.INCOMPLETO.equals(stato) ||
				StatoOperativoDocumento.VALIDO.equals(stato) ||
				StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.equals(stato) ||
				StatoOperativoDocumento.PARZIALMENTE_EMESSO.equals(stato)  ;
	}
	
	/**
	 * Ottiene l'importo del documento non rilevante per l'IVA.
	 * 
	 * @return l'importo non rilevante per l'IVA
	 */
	public BigDecimal getImportoNonRilevanteIva() {
		DocumentoEntrata doc = getDocumento();
		BigDecimal result = BigDecimal.ZERO;
		if(doc != null) {
			result = doc.getImporto().subtract(doc.calcolaImportoTotaleRilevanteIVASubdoumenti());
		}
		return result;
	}
	
	/**
	 * Ottiene l'importo del documento rilevante per l'IVA.
	 * 
	 * @return l'importo rilevante per l'IVA
	 */
	public BigDecimal getImportoRilevanteIva() {
		DocumentoEntrata doc = getDocumento();
		BigDecimal result = BigDecimal.ZERO;
		if(doc != null) {
			result = doc.calcolaImportoTotaleRilevanteIVASubdoumenti();
		}
		return result;
	}
	
	/**
	 * Controlla quale possa essere in progressivo del subdocumento e imposta il progressivo all'intero successivo, partendo da 1.
	 */
	public void checkAndIncrementProgressivoSubdocumento() {
		Integer max = Integer.valueOf(0);
		for(SubdocumentoEntrata s : listaSubdocumentoEntrata) {
			if(max.compareTo(s.getNumero()) < 0) {
				max = s.getNumero();
			}
		}
		setProgressivoNumeroSubdocumento(Integer.valueOf(max.intValue() + 1));
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
	
	//SIAC-5808
	/**
	 * @return the esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota
	 */
	public boolean isEsisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota() {
		return esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota;
	}

	/**
	 * @param esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota the esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota to set
	 */
	public void setEsisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota(
			boolean esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota) {
		this.esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota = esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota;
	}
	

	/**
	 * @return the tipoEventoAccertamento
	 */
	public TipoEvento getTipoEventoAccertamento() {
		return tipoEventoAccertamento;
	}

	/**
	 * @param tipoEventoAccertamento the tipoEventoAccertamento to set
	 */
	public void setTipoEventoAccertamento(TipoEvento tipoEventoAccertamento) {
		this.tipoEventoAccertamento = tipoEventoAccertamento;
	}
	/**
	 * @return the forzaDisponibilitaAccertamento
	 */
	public boolean isForzaDisponibilitaAccertamento() {
		return forzaDisponibilitaAccertamento;
	}

	/**
	 * @param forzaDisponibilitaAccertamento the forzaDisponibilitaAccertamento to set
	 */
	public void setForzaDisponibilitaAccertamento(boolean forzaDisponibilitaAccertamento) {
		this.forzaDisponibilitaAccertamento = forzaDisponibilitaAccertamento;
	}
	
	/**
	 * @return the messaggioConfermaSfondamentoAccertamento
	 */
	public Messaggio getMessaggioConfermaSfondamentoAccertamento() {
		return messaggioConfermaSfondamentoAccertamento;
	}

	/**
	 * @param messaggioConfermaSfondamentoAccertamento the messaggioConfermaSfondamentoAccertamento to set
	 */
	public void setMessaggioConfermaSfondamentoAccertamento(Messaggio messaggioConfermaSfondamentoAccertamento) {
		this.messaggioConfermaSfondamentoAccertamento = messaggioConfermaSfondamentoAccertamento;
	}
	
	
	/* Requests */	
	

	/**
	 * Crea una request per il servizio di {@link AggiornaDocumentoDiEntrata}.
	 *  
	 * @return la request creata
	 */
	public AggiornaDocumentoDiEntrata creaRequestAggiornaDocumentoDiEntrata() {
		AggiornaDocumentoDiEntrata request = new AggiornaDocumentoDiEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		DocumentoEntrata documentoEntrata = getDocumento();
		documentoEntrata.setSoggetto(getSoggetto());
		request.setDocumentoEntrata(documentoEntrata);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * 
	 * @param accertamento l'accertamento da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiave creaRequestRicercaAccertamentoPerChiave(Accertamento accertamento) {
		RicercaAccertamentoPerChiave request = new RicercaAccertamentoPerChiave();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setpRicercaAccertamentoK(creaRicercaAccertamentoK(accertamento));
		request.setRichiedente(getRichiedente());
		
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
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
		request.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceQuotaDocumentoEntrata}.
	 *  
	 * @return la request creata
	 */
	public InserisceQuotaDocumentoEntrata creaRequestInserisceQuotaDocumentoEntrata() {
		InserisceQuotaDocumentoEntrata request = new InserisceQuotaDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		request.setCapitolo(impostaEntitaFacoltativa(getCapitolo()));
		
		//per il ripeti
		getSubdocumento().setUid(0);
		
		subdocumento.setDocumento(creaDocumentoPerInserimentoQuota());
		subdocumento.setEnte(getEnte());
		AttoAmministrativo atto = getAttoAmministrativo();
		atto.setStrutturaAmmContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		atto.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		subdocumento.setAttoAmministrativo(impostaEntitaFacoltativa(atto));
		
		getSubdocumento().setContoTesoreria(impostaEntitaFacoltativa(getContoTesoreria()));
		getSubdocumento().setNoteTesoriere(impostaEntitaFacoltativa(getNoteTesoriere()));
		getSubdocumento().setTipoAvviso(impostaEntitaFacoltativa(getTipoAvviso()));
		
		subdocumento.setAccertamento(movimentoGestione);
		getSubdocumento().setSubAccertamento(impostaEntitaFacoltativa(subMovimentoGestione));
		
		if(subdocumento.getProvvisorioCassa() != null && subdocumento.getProvvisorioCassa().getUid() == 0) {
			subdocumento.setProvvisorioCassa(null);
		}
	
		request.setCapitolo(getCapitolo());
		
		request.setSubdocumentoEntrata(subdocumento);
		
		//SIAC-6645
		request.setGestisciModificaImporto(isForzaDisponibilitaAccertamento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaQuotaDocumentoEntrata}.
	 *  
	 * @return la request creata
	 */
	public AggiornaQuotaDocumentoEntrata creaRequestAggiornaQuotaDocumentoEntrata() {
		AggiornaQuotaDocumentoEntrata request = new AggiornaQuotaDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		subdocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		
		getSubdocumento().setContoTesoreria(impostaEntitaFacoltativa(getContoTesoreria()));
		getSubdocumento().setNoteTesoriere(impostaEntitaFacoltativa(getNoteTesoriere()));
		getSubdocumento().setTipoAvviso(impostaEntitaFacoltativa(getTipoAvviso()));
		getSubdocumento().setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		
		subdocumento.setAccertamento(movimentoGestione);
		getSubdocumento().setSubAccertamento(impostaEntitaFacoltativa(subMovimentoGestione));
		
		if(subdocumento.getProvvisorioCassa() != null && subdocumento.getProvvisorioCassa().getUid() == 0) {
			subdocumento.setProvvisorioCassa(null);
		}
		
		subdocumento.setEnte(getEnte());
		subdocumento.setDocumento(creaDocumentoPerInserimentoQuota());
		
		request.setCapitolo(getCapitolo());

		request.setSubdocumentoEntrata(subdocumento);
		
		//SIAC-6645
		request.setGestisciModificaImporto(isForzaDisponibilitaAccertamento());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaQuotaDocumentoEntrata}.
	 * 
	 * @param subdoc il subdocumento per cui creare la request
	 *  
	 * @return la request creata
	 */
	public AggiornaQuotaDocumentoEntrata creaRequestAggiornaQuotaDocumentoEntrata(SubdocumentoEntrata subdoc) {
		AggiornaQuotaDocumentoEntrata request = new AggiornaQuotaDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		request.setSubdocumentoEntrata(subdoc);
		
		return request;
	}
	

	/**
	 * Crea una request per il servizio di {@link InserisceNotaCreditoEntrata} necessaria per l'introduzione della nota di credito.
	 * 
	 * @return la request creata
	 */
	public InserisceNotaCreditoEntrata creaRequestInserisceNotaCreditoEntrata() {
		InserisceNotaCreditoEntrata request = new InserisceNotaCreditoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setDocumentoEntrata(notaCredito);
//		notaCredito.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
		for(DocumentoEntrata docPadre : notaCredito.getListaDocumentiEntrataPadre()){
			if(docPadre.getUid() == documento.getUid()){
				docPadre.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
			}
		}
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaQuotaDocumentoEntrata}.
	 *  
	 * @return la request creata
	 */
	public EliminaQuotaDocumentoEntrata creaRequestEliminaQuotaDocumentoEntrata() {
		EliminaQuotaDocumentoEntrata request = new EliminaQuotaDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setBilancio(getBilancio());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentoEntrata(listaSubdocumentoEntrata.get(getRigaDaEliminare().intValue()));
		
		return request;
	}
	

	
	/**
	 * Crea una request per il servizio di {@link AnnullaDocumentoEntrata}.
	 * 
	 * @param uid l'uid della nota da annullare
	 *  
	 * @return la request creata
	 */
	public AnnullaNotaCreditoEntrata creaRequestAnnullaNotaCredito(Integer uid) {
		AnnullaNotaCreditoEntrata request = new AnnullaNotaCreditoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		
		DocumentoEntrata nota = new DocumentoEntrata();
		nota.setEnte(getEnte());
		nota.setUid(uid);
		
		request.setDocumentoEntrata(nota);

		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaNotaCreditoEntrata}.
	 *  
	 * @return la request creata
	*/
	
	public AggiornaNotaCreditoEntrata creaRequestAggiornaNotaCreditoEntrata() {
		AggiornaNotaCreditoEntrata request = new AggiornaNotaCreditoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		notaCredito.setEnte(getEnte());
//		notaCredito.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
		for(DocumentoEntrata docPadre : notaCredito.getListaDocumentiEntrataPadre()){
			if(docPadre.getUid() == documento.getUid()){
				docPadre.setImportoDaDedurreSuFattura(getImportoDaDedurreSuFattura());
			}
		}
		request.setBilancio(getBilancio());
		
		request.setDocumentoEntrata(notaCredito);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoEntrata}.
	 *  
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoEntrata creaRequestRicercaQuoteByDocumentoEntrata() {
		RicercaQuoteByDocumentoEntrata request = new RicercaQuoteByDocumentoEntrata();
		request.setDataOra(new Date());
		request.setDocumentoEntrata(documento);
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaImportiQuoteDocumentoEntrata}.
	 *  
	 * @return la request creata
	 */
	public AggiornaImportiQuoteDocumentoEntrata creaRequestAggiornaImportiQuoteDocumentoEntrata() {
		AggiornaImportiQuoteDocumentoEntrata request = new AggiornaImportiQuoteDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentiEntrata(getListaSubdocumentoEntrata());
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioSubdocumentoIvaEntrata}.
	 * 
	 * @param sdie il subdocumentoIva rispetto cui creare la request
	 *  
	 * @return la request creata
	 */
	public RicercaDettaglioSubdocumentoIvaEntrata creaRequestRicercaDettaglioSubdocumentoIvaEntrata(SubdocumentoIvaEntrata sdie) {
		RicercaDettaglioSubdocumentoIvaEntrata request = new RicercaDettaglioSubdocumentoIvaEntrata();
		
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		request.setSubdocumentoIvaEntrata(sdie);
		
		return request;
	}
	
	/* *************************************************************************************************************************** */
	
	/**
	 * Imposta i dati ottenuti dalla ricerca di dettaglio del documento.
	 * 
	 * @param doc il documento tramite cui caricare il model
	 */
	public void impostaDatiDocumento(DocumentoEntrata doc) {
		setDocumento(doc);
		
		// Imposta le liste
		setListaSubdocumentoEntrata(doc.getListaSubdocumenti());
		// i documenti di entrata possono essere solo note di credito
		setListaDocumentoEntrata(ElementoDocumentoFactory.getInstances(doc.getListaDocumentiEntrataFiglio(), doc.getSoggetto(), doc.getStatoOperativoDocumento()));
	
		// i documenti di spesa possono essere solo documenti collegati
		setListaDocumentoSpesa(ElementoDocumentoFactory.getInstances(doc.getListaDocumentiSpesaFiglio(), doc.getSoggetto()));
		
		calcoloImporti();
		
		setSoggetto(doc.getSoggetto());
		
		setProgressivoNumeroSubdocumento(doc.getListaSubdocumenti().size());
		
		// Imposta dati subdocumentoIVA
		if(!doc.getListaSubdocumentoIva().isEmpty()) {
			impostaSubdocumentoIva(doc.getListaSubdocumentoIva());
		}
		
		//SIAC-7562 - 06/07/2020 - CM
		setDataCambioStatoFel(doc.getDataCambioStatoFel());
		
		//SIAC-7562 - 25/06/2020 - CM e GM
		if(doc.getStatoSDI() != null && !doc.getStatoSDI().equals("")){
			this.setStatoSDIdescrizione(StatoSDIDocumento.getDescrizioneFromCodice(doc.getStatoSDI()));
			
			//SIAC-6988
			if (doc.getStatoSDI().equals(StatoSDIDocumento.INVIATA_FEL.getCodice())
					|| doc.getStatoSDI().equals(StatoSDIDocumento.DECORR_TERMINI.getCodice())
					|| doc.getStatoSDI().equals(StatoSDIDocumento.ACCET_CONSEG.getCodice())) {
				setFlagStatoSDIInviatoFEL(true);
			}
				
			
		}
	}
	
	/**
	 * Imposta il subdocumento iva con i parametri corretti.
	 * 
	 * @param listaSubdocumentoIva la lista da cui estrarre il subdocumento corretto
	 */
	private void impostaSubdocumentoIva(List<SubdocumentoIvaEntrata> listaSubdocumentoIva) {
		// TODO: Prendo il primo, controllare che non ce ne sia mai più di uno
		SubdocumentoIvaEntrata sie = listaSubdocumentoIva.get(0);
		setSubdocumentoIva(sie);
		setDocumentoIvaLegatoDocumentoPresente(Boolean.TRUE);
	}

	/**
	 * Imposta i dati ottenuti dalla ricerca di dettaglio del soggetto.
	 * 
	 * @param soggetto               il soggetto tramite cui caricare il model
	 * @param listaSediSecondarie    la lista delle sedi secondarie
	 * @param listaModalitaPagamento la lista delle modalit&agrave; di pagamento
	 */
	public void impostaDatiSoggetto(Soggetto soggetto, List<SedeSecondariaSoggetto> listaSediSecondarie, List<ModalitaPagamentoSoggetto> listaModalitaPagamento) {
		setSoggetto(soggetto);
		setListaSedeSecondariaSoggetto(listaSediSecondarie);
	}
	
	/**
	 * Imposta i flags per l'apertura dei var&icirc; tabs.
	 * 
	 * @param azioneIvaGestibile se l'azione di gestione dell'IVA sia gestibile
	 */
	public void impostaFlags(Boolean azioneIvaGestibile) {
		setAzioneIvaGestibile(azioneIvaGestibile);
		DocumentoEntrata documentoEntrata = getDocumento();
		TipoDocumento tipoDocumento = documentoEntrata.getTipoDocumento();
		setFlagDocumentiCollegatiAccessibile(tipoDocumento.getFlagSpesaCollegata());
		setFlagNoteCreditoAccessibile(tipoDocumento.getFlagNotaCredito());
		
		impostaFlagIvaGestibile(documentoEntrata, documentoEntrata.getListaSubdocumenti());
	}
	
	/**
	 * Imposta i dati della quota per l'aggiornamento.
	 * 
	 * @param subdocumentoEntrata il subdocumento tramite cui popolare i dati
	 */
	public void impostaQuotaDaInserire(SubdocumentoEntrata subdocumentoEntrata) {
		
		setProvvedimentoQuotaDisabilitato(!isProvvedimentoQuotaDigitabile());
		
	}

	/**
	 * Controlla se il provvedimento della quota sia digitabile
	 * @return true se il provedimento &eacute; digitabile; false altrimenti
	 */
	private boolean isProvvedimentoQuotaDigitabile() {
		//SE AttivaGenPcc TRUE o il documento non e' di tipo da registrare in GEN posso sempre inserire il provvedimento
		//ALTRIMENTI il provvedimento e' digitabile solo se indico il capitolo di entrata. 
		return Boolean.TRUE.equals(documento.getContabilizzaGenPcc()) 
				|| !Boolean.TRUE.equals(documento.getTipoDocumento().getFlagAttivaGEN()) 
				|| (capitolo!=null && capitolo.getAnnoCapitolo()!=null && capitolo.getNumeroCapitolo()!=null && capitolo.getNumeroArticolo()!=null);
	}
	
	/**
	 * Imposta i dati della quota per la ripetizione.
	 * 
	 * @param subdocumentoEntrata il subdocumento tramite cui popolare i dati
	 */
	public void impostaQuotaDaRipetere(SubdocumentoEntrata subdocumentoEntrata) {
		setProgressivoNumeroSubdocumento(subdocumentoEntrata.getNumero());
		
		subdocumentoEntrata.setImporto(BigDecimal.ZERO);
		subdocumentoEntrata.setOrdinativo(null);
		setSubdocumento(subdocumentoEntrata);
		//setMovimentoGestione(subdocumentoEntrata.getAccertamento());
		//setSubMovimentoGestione(subdocumentoEntrata.getSubAccertamento());
		
		
		setAttoAmministrativo(subdocumentoEntrata.getAttoAmministrativo());
		if(getAttoAmministrativo() != null) {
			setTipoAtto(getAttoAmministrativo().getTipoAtto());
			setStrutturaAmministrativoContabile(getAttoAmministrativo().getStrutturaAmmContabile());
		}
		
		setTipoAvviso(subdocumentoEntrata.getTipoAvviso());
		setNoteTesoriere(subdocumentoEntrata.getNoteTesoriere());
		setContoTesoreria(subdocumentoEntrata.getContoTesoreria());
		setSedeSecondariaSoggetto(subdocumentoEntrata.getSedeSecondariaSoggetto());
		
		setCapitoloRilevanteIvaVisibile(subdocumentoEntrata.getFlagRilevanteIVA());
		
//		CR 3022
//		if(subdocumentoEntrata.getNumeroRegistrazioneIVA()!= null && StringUtils.isNotBlank(subdocumentoEntrata.getNumeroRegistrazioneIVA())){
//			setImportoQuotaDisabilitato(Boolean.TRUE);
//		}
		
		setProvvedimentoQuotaDisabilitato(!isProvvedimentoQuotaDigitabile());

	}
	
	
	/**
	 * Imposta i dati della quota per l'aggiornamento.
	 * 
	 * @param subdocumentoEntrata il subdocumento tramite cui popolare i dati
	 */
	public void impostaQuotaDaAggiornare(SubdocumentoEntrata subdocumentoEntrata) {
		setProgressivoNumeroSubdocumento(subdocumentoEntrata.getNumero());
		
		setSubdocumento(subdocumentoEntrata);
		setMovimentoGestione(subdocumentoEntrata.getAccertamento());
		setSubMovimentoGestione(subdocumentoEntrata.getSubAccertamento());
		
		setAttoAmministrativo(subdocumentoEntrata.getAttoAmministrativo());
		if(getAttoAmministrativo() != null) {
			setTipoAtto(getAttoAmministrativo().getTipoAtto());
			setStrutturaAmministrativoContabile(getAttoAmministrativo().getStrutturaAmmContabile());
		}
		
		setTipoAvviso(subdocumentoEntrata.getTipoAvviso());
		setNoteTesoriere(subdocumentoEntrata.getNoteTesoriere());
		setContoTesoreria(subdocumentoEntrata.getContoTesoreria());
		setSedeSecondariaSoggetto(subdocumentoEntrata.getSedeSecondariaSoggetto());
		
		setCapitoloRilevanteIvaVisibile(subdocumentoEntrata.getFlagRilevanteIVA());
		
//		CR 3022
//		if(subdocumentoEntrata.getNumeroRegistrazioneIVA()!= null && StringUtils.isNotBlank(subdocumentoEntrata.getNumeroRegistrazioneIVA())){
//			setImportoQuotaDisabilitato(Boolean.TRUE);
//		}
		
		setProvvedimentoQuotaDisabilitato(!isProvvedimentoQuotaDigitabile());
		
		if(subdocumentoEntrata.getOrdinativo() != null && subdocumentoEntrata.getOrdinativo().getUid()!=0){
			setImportoQuotaDisabilitato(Boolean.TRUE);
			setImpegnoQuotaDisabilitato(Boolean.TRUE);
			setCapitoloQuotaDisabilitato(Boolean.TRUE);
			setProvvedimentoQuotaDisabilitato(Boolean.TRUE);
		}
		
	}
	
	/**
	 * Rieffettua il calcolo del totale delle quote del documento.
	 */
	public void ricalcolaTotaliQuote() {
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
		
		for(SubdocumentoEntrata sds : listaSubdocumentoEntrata) {
			totaleQuote = totaleQuote.add(sds.getImporto());
			totaleDaPagareQuote = totaleDaPagareQuote.add(sds.getImportoDaIncassare());
		}
		
		setTotaleQuote(totaleQuote);
		setTotaleDaPagareQuote(totaleDaPagareQuote);
		setImportoDaAttribuire(getDocumento().getImporto().add(getDocumento().getArrotondamento()).subtract(totaleQuote));
	}
	
	/**
	 * Ricalcola il flag per i dati iva
	 */
	public void ricalcolaFlagIva() {
		impostaFlagIvaGestibile(getDocumento(), getListaSubdocumentoEntrata());
	}
	
	/**
	 * Imposta i dati della nota credito per l'aggiornamento.
	 * 
	 * @param nota la nota credito tramite cui popolare i dati
	 */
	public void impostaNotaCreditoDaAggiornare(DocumentoEntrata nota) {
		// Popolo i dati presenti nei classificatori esterni
		
		setAttoAmministrativo(nota.getListaSubdocumenti().get(0).getAttoAmministrativo());
		if(getAttoAmministrativo() != null) {
			setTipoAtto(getAttoAmministrativo().getTipoAtto());
			setStrutturaAmministrativoContabile(getAttoAmministrativo().getStrutturaAmmContabile());
		}
		
		//la nota credito potrebbe essere collegata a piu' documenti, devo individuare l'importo relativo al padre su cui sto lavorando adesse
		for(DocumentoEntrata docPadre : nota.getListaDocumentiEntrataPadre()){
			if(docPadre.getUid() ==getDocumento().getUid()){
				setImportoDaDedurreSuFattura(docPadre.getImportoDaDedurreSuFattura());
			}
		}
	}
	
	/* *************************************************************************************************************************************** */

	/**
	 * Crea un'utility per la ricerca dell'accertamento.
	 * 
	 * @param accertamento l'accertamento da cercare
	 * 
	 * @return l'utility creata
	 */
	private RicercaAccertamentoK creaRicercaAccertamentoK(Accertamento accertamento) {
		RicercaAccertamentoK utility = new RicercaAccertamentoK();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoAccertamento(accertamento.getAnnoMovimento());
		utility.setNumeroAccertamento(accertamento.getNumeroBigDecimal());
		
		return utility;
	}
	
	/**
	 * Crea un documento di spesa per l'inserimento della quota, avente valorizzato soltanto il parametro <code>uid</code>.
	 * 
	 * @return il documento per l'injezione nella request
	 */
	private DocumentoEntrata creaDocumentoPerInserimentoQuota() {
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(getDocumento().getUid());
		return documentoEntrata;
	}
	
	
	/**
	 * Popola il documentoSpesa nota per  l'inserimento della nota di credito.
	 */
	public void popolaNotaCreditoPerInserimento() {
		TipoDocumento tipo = new TipoDocumento();
		tipo.setCodiceGruppo(BilConstants.CODICE_NOTE_ACCREDITO.toString());
		tipo.setCodice(BilConstants.CODICE_NOTE_ACCREDITO.toString());

		// creo il collegamento con il documento
		notaCredito.addDocumentoEntrataPadre(getDocumento());
		notaCredito.setSoggetto(getDocumento().getSoggetto());
		notaCredito.setEnte(getEnte());
		// la nota credito nasce in stato valido (rif. analisi 2.5.2 Servizio Inserisce Documento Spesa per le Note credito)
		notaCredito.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
				
		List<SubdocumentoEntrata> listaSubdocumenti = new ArrayList<SubdocumentoEntrata>();
		SubdocumentoEntrata s = new SubdocumentoEntrata();
		s.setAttoAmministrativo(getAttoAmministrativo());
		s.setEnte(getEnte());
		// la lista avrà sempre un solo elemento
		listaSubdocumenti.add(s);
		notaCredito.setListaSubdocumenti(listaSubdocumenti);
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
		BigDecimal totaleNote= BigDecimal.ZERO;
		BigDecimal totaleDaDedurreSuFattura = BigDecimal.ZERO;
	
		List<BigDecimal> vRet = new ArrayList<BigDecimal>();
		
		for(SubdocumentoEntrata s: getListaSubdocumentoEntrata()){
			totale=totale.add(s.getImporto());
			totaleImpoDaDedurre=totaleImpoDaDedurre.add(s.getImportoDaDedurreNotNull());
		}
		
		for(DocumentoEntrata ds : documento.getListaDocumentiEntrataFiglio()) {
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
		
		BigDecimal totaleImportoDaDedurre = BigDecimal.ZERO;
		BigDecimal netto = BigDecimal.ZERO;
		BigDecimal totaleEntrata = BigDecimal.ZERO;
		BigDecimal totaleDocumentiSpesa = BigDecimal.ZERO;
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleNoteCredito = BigDecimal.ZERO;
		BigDecimal totaleImportoDaDedurraSuFattura = BigDecimal.ZERO;
		
		for(DocumentoEntrata de : documento.getListaDocumentiEntrataFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(de.getTipoDocumento().getCodiceGruppo())
				&& !StatoOperativoDocumento.ANNULLATO.equals(de.getStatoOperativoDocumento())	) {
				totaleNoteCredito = totaleNoteCredito.add(de.getImporto());
				totaleImportoDaDedurraSuFattura = totaleImportoDaDedurraSuFattura.add(de.getImportoDaDedurreSuFattura());
			}
			totaleEntrata = totaleEntrata.add(de.getImporto());
		}
		
		for(DocumentoSpesa ds : documento.getListaDocumentiSpesaFiglio()) {
			if(!StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento())){
				totaleDocumentiSpesa = totaleDocumentiSpesa.add(ds.getImporto());
			}
		}
		
		for(SubdocumentoEntrata sub : documento.getListaSubdocumenti()) {
			totaleQuote = totaleQuote.add(sub.getImporto());
			totaleImportoDaDedurre = totaleImportoDaDedurre.add(sub.getImportoDaDedurreNotNull());
			//vedere come valorizzare importo da incassare
			totaleDaPagareQuote = totaleDaPagareQuote.add(sub.getImportoDaIncassare());
			if(sub.getOrdinativo() == null || sub.getOrdinativo().getUid() ==0){
				totaleDaPagareQuoteSenzaOrdinativo = totaleDaPagareQuoteSenzaOrdinativo.add(sub.getImportoDaIncassare());
			}
		}
		
		
		setTotaleImportoDaDedurre(totaleImportoDaDedurre);
		setTotaleDaPagareQuoteSenzaOrdinativo(totaleDaPagareQuoteSenzaOrdinativo);
		setTotaleDaPagareQuote(totaleDaPagareQuote);
		// netto = importo + arrotondamento - noteCredito
		netto = documento.getImporto().add(documento.getArrotondamento()).subtract(totaleImportoDaDedurraSuFattura);
		impostaNetto(netto);
		// importoDaAttribuire = netto - totaleQuote
		setTotaleQuote(totaleQuote);
		setImportoDaAttribuire(documento.getImporto().add(documento.getArrotondamento()).subtract(totaleQuote));
		setTotaleDocumentiCollegati(totaleDocumentiSpesa);
//		setNoteCreditoDocumento(totaleNoteCredito);
		setTotaleNoteCredito(totaleNoteCredito);
		setTotaleImportoDaDedurreSuFattura(totaleImportoDaDedurraSuFattura);
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
	 * Crea una request per il servizio di {@link AttivaRegistrazioniContabiliEntrata}.
	 * 
	 * @return la request creata
	 */
	public AttivaRegistrazioniContabiliEntrata creaRequestAttivaRegistrazioniContabiliEntrata() {
		AttivaRegistrazioniContabiliEntrata request = creaRequest(AttivaRegistrazioniContabiliEntrata.class);
		
		DocumentoEntrata documentoEntrata= new DocumentoEntrata();
		documentoEntrata.setUid(documento.getUid());
		request.setDocumentoEntrata(documentoEntrata);
		//SIAC-5333
		request.setSaltaInserimentoPrimaNota(getValidazionePrimaNotaDaDocumento());
		request.setBilancio(getBilancio());
		return request;
	}
	
	/**
	 * Crea request comprensiva dei dati di ModificaMovimento da caricare per il bloccoROR ed da utilizzare per il servizio {@link RicercaAccertamentoPerChiaveOttimizzato}
	 * @param accertamento l'accertamento per cui effettuare la ricerca
	 * @return la request creata
	 * */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato_BloccoROR(Accertamento accertamento) {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		RicercaAccertamentoK pRicercaAccertamentoK = creaRicercaAccertamentoK(accertamento);
		pRicercaAccertamentoK.setNumeroSubDaCercare(getSubMovimentoGestione() != null ? getSubMovimentoGestione().getNumeroBigDecimal() : null);
		request.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null);
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaElencoModificheMovGest(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		return request;
	}
	
	/**
	 * Cre a request da utilizzare per il servizio {@link RicercaAccertamentoPerChiaveOttimizzato}
	 * @param accertamento l'accertamento per cui effettuare la ricerca
	 * @return la request creata
	 * */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento) {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		
		RicercaAccertamentoK pRicercaAccertamentoK = creaRicercaAccertamentoK(accertamento);
		pRicercaAccertamentoK.setNumeroSubDaCercare(getSubMovimentoGestione() != null ? getSubMovimentoGestione().getNumeroBigDecimal() : null);
		request.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
	
	//SIAC-5808
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaRegistrazioneMovFin}.
	 *
	 * @param annoMovimento the anno movimento
	 * @param numeroMovimento the numero movimento
	 * @param numeroSubMovimento the numero submovimento
	 * @param tipoEvento the tipo evento
	 * @return la request creata
	 */
	public RicercaSinteticaRegistrazioneMovFin creaRequestRicercaSinteticaRegistrazioneMovFin(Integer annoMovimento, BigDecimal numeroMovimento, BigDecimal numeroSubMovimento, TipoEvento tipoEvento) {
		RicercaSinteticaRegistrazioneMovFin req = creaRequest(RicercaSinteticaRegistrazioneMovFin.class);
		
		RegistrazioneMovFin reg = new RegistrazioneMovFin();
		reg.setBilancio(getBilancio());
		reg.setAmbito(Ambito.AMBITO_FIN);
		req.setRegistrazioneMovFin(reg);
		
		req.setAnnoMovimento(annoMovimento);
		req.setNumeroMovimento(numeroMovimento != null ? numeroMovimento.toString(): null);
		req.setNumeroSubmovimento(numeroSubMovimento != null? Integer.valueOf(numeroSubMovimento.toString()) : null);
		req.setTipoEvento(tipoEvento);
		
		//voglio controllare che non ne esista nessuna
		req.setParametriPaginazione(new ParametriPaginazione(0, 100));
		
		return req;
	}

	public String getStatoSDIdescrizione() {
		return statoSDIdescrizione;
	}

	public void setStatoSDIdescrizione(String statoSDIdescrizione) {
		this.statoSDIdescrizione = statoSDIdescrizione;
	}
	
	public Date getDataCambioStatoFel() {
		return dataCambioStatoFel;
	}

	public void setDataCambioStatoFel(Date dataCambioStatoFel) {
		this.dataCambioStatoFel = dataCambioStatoFel;
	}
	
}
