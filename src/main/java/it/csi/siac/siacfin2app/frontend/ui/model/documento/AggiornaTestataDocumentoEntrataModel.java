/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di model per l'aggiornamento della testata del documento di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/07/2014
 *
 */
public class AggiornaTestataDocumentoEntrataModel extends AggiornaDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -264558055841991908L;
	
	private DocumentoEntrata documento;
	private SubdocumentoIvaEntrata subdocumentoIva;
	
	// Quote
	private List<SubdocumentoEntrata> listaSubdocumentoEntrata = new ArrayList<SubdocumentoEntrata>();
	// Quote rilevanti Iva
	private List<ElementoSubdocumentoIvaEntrata> listaQuoteRilevantiIva = new ArrayList<ElementoSubdocumentoIvaEntrata>();
	
	/** Costruttore vuoto di default */
	public AggiornaTestataDocumentoEntrataModel() {
		setTitolo("Aggiorna Documenti iva Entrata");
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
	public void setListaSubdocumentoEntrata(List<SubdocumentoEntrata> listaSubdocumentoEntrata) {
		this.listaSubdocumentoEntrata = listaSubdocumentoEntrata != null ? listaSubdocumentoEntrata : new ArrayList<SubdocumentoEntrata>();
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
		this.listaQuoteRilevantiIva = listaQuoteRilevantiIva != null ? listaQuoteRilevantiIva : new ArrayList<ElementoSubdocumentoIvaEntrata>();
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
		return StatoOperativoDocumento.INCOMPLETO.equals(getDocumento().getStatoOperativoDocumento());
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
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaDocumentoDiEntrata}.
	 *  
	 * @return la request creata
	 */
	public AggiornaDocumentoDiEntrata creaRequestAggiornaDocumentoDiEntrata() {
		AggiornaDocumentoDiEntrata request = creaRequest(AggiornaDocumentoDiEntrata.class);
		
		DocumentoEntrata documentoEntrata = getDocumento();
		documentoEntrata.setSoggetto(getSoggetto());
		
		request.setDocumentoEntrata(documentoEntrata);
		request.setBilancio(getBilancio());
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
	 * @param documento il documento tramite cui caricare il model
	 */
	public void impostaDatiDocumento(DocumentoEntrata documento) {
		setDocumento(documento);
		
		// Imposta le liste
		setListaSubdocumentoEntrata(documento.getListaSubdocumenti());
		// Calcola gli importi
		calcoloImporti();
		// Imposta il soggetto
		setSoggetto(documento.getSoggetto());
		// Imposta il progressivo dei numeri del subdocumento
		setProgressivoNumeroSubdocumento(documento.getListaSubdocumenti().size());
		
		// Imposta dati subdocumentoIVA
		if(!documento.getListaSubdocumentoIva().isEmpty()) {
			impostaSubdocumentoIva(documento.getListaSubdocumentoIva());
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
	}
	
	/**
	 * Imposta i flags per l'apertura dei var&icirc; tabs.
	 */
	public void impostaFlags() {
		setAzioneIvaGestibile(Boolean.TRUE);
		impostaFlagIvaGestibile(getDocumento(), getDocumento().getListaSubdocumenti());
	}
	
	/**
	 * Rieffettua il calcolo del totale delle quote del documento.
	 */
	public void ricalcolaTotaliQuote() {
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
		
		for(SubdocumentoEntrata sds : listaSubdocumentoEntrata) {
			totaleQuote = totaleQuote.add(sds.getImporto());
			totaleDaPagareQuote = totaleDaPagareQuote.add(sds.getImporto()).subtract(sds.getImportoDaDedurre());
		}
		
		setTotaleQuote(totaleQuote);
		setTotaleDaPagareQuote(totaleDaPagareQuote);
		setImportoDaAttribuire(getNetto().subtract(totaleQuote));
	}
	
	/**
	 * Ricalcola il flag per i dati iva
	 */
	public void ricalcolaFlagIva() {
		impostaFlagIvaGestibile(getDocumento(), getListaSubdocumentoEntrata());
	}
	
	/* ****************************************** Utility *************************************************************************** */

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
		utility.setNumeroAccertamento(accertamento.getNumero());
		
		return utility;
	}
	
	/**
	 * Imposta i totali delle note e afferenti
	 */
	public void impostaTotaliNoteQuote() {
		setTotaleQuoteNotaCredito(calcolaTotaleQuote().get(0));
		setTotaleImportoDaDedurre(calcolaTotaleQuote().get(1));
		setTotaleNoteCredito(calcolaTotaleQuote().get(2));
	}
	
	/**
	 * Calcola i totali da restituire alla pagina
	 * 
	 * @return  una lista con i due totali
	 */
	private List<BigDecimal> calcolaTotaleQuote() {	
		BigDecimal totale = BigDecimal.ZERO;
		BigDecimal totaleImpoDaDedurre = BigDecimal.ZERO;
		BigDecimal totaleNote = BigDecimal.ZERO;
	
		List<BigDecimal> vRet = new ArrayList<BigDecimal>();
		for(SubdocumentoEntrata s : getListaSubdocumentoEntrata()){
			totale = totale.add(s.getImporto());
			totaleImpoDaDedurre = totaleImpoDaDedurre.add(s.getImportoDaDedurre());
		}
		
		for(DocumentoEntrata ds : documento.getListaDocumentiEntrataFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(ds.getTipoDocumento().getCodiceGruppo())) {
				totaleNote = totaleNote.add(ds.getImporto());
			}
		}
		
		vRet.add(totale);
		vRet.add(totaleImpoDaDedurre);
		vRet.add(totaleNote);
		return vRet;
	}
	
	
	@Override
	public void calcoloImporti() {
		BigDecimal totaleDaPagareQuote = BigDecimal.ZERO;
		
		// ANAGRAFICA
		BigDecimal noteCreditoDocumento = BigDecimal.ZERO;
		BigDecimal netto = BigDecimal.ZERO;
		
		// TABS SEGUENTI
		BigDecimal totaleEntrataEscluseNoteCredito = BigDecimal.ZERO;
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleNoteCredito = BigDecimal.ZERO;
		
		for(DocumentoEntrata de : documento.getListaDocumentiEntrataFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(de.getTipoDocumento().getCodiceGruppo())) {
				totaleNoteCredito = totaleNoteCredito.add(de.getImporto());
			}else {
				totaleEntrataEscluseNoteCredito = totaleEntrataEscluseNoteCredito.add(de.getImporto());
			}
		}
		
		for(SubdocumentoEntrata sub : documento.getListaSubdocumenti()) {
			totaleQuote = totaleQuote.add(sub.getImporto());
			noteCreditoDocumento = noteCreditoDocumento.add(sub.getImportoDaDedurre());
			
			totaleDaPagareQuote = totaleDaPagareQuote.add(sub.getImporto()).subtract(sub.getImportoDaDedurre());
		}
		
		setTotaleDaPagareQuote(totaleDaPagareQuote);
		// netto = importo + arrotondamento - noteCredito
		netto = documento.getImporto().add(documento.getArrotondamento()).subtract(totaleNoteCredito);
		impostaNetto(netto);
		// importoDaAttribuire = netto - totaleQuote
		setImportoDaAttribuire(netto.subtract(totaleQuote));
		setTotaleQuote(totaleQuote);
		setTotaleNoteCredito(totaleNoteCredito);
	}
	
}
