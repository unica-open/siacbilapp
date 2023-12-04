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
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di model per l'aggiornamento della testata del documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/07/2014
 *
 */
public class AggiornaTestataDocumentoSpesaModel extends AggiornaDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9156607678208055644L;
	
	private DocumentoSpesa documento;
	private SubdocumentoIvaSpesa subdocumentoIva;
	
	// Quote
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	// Quote rilevanti Iva
	private List<ElementoSubdocumentoIvaSpesa> listaQuoteRilevantiIva = new ArrayList<ElementoSubdocumentoIvaSpesa>();
	
	//SIAC-6186
	private List<SiopeDocumentoTipo> listaSiopeDocumentoTipo = new ArrayList<SiopeDocumentoTipo>();
	private List<SiopeDocumentoTipoAnalogico> listaSiopeDocumentoTipoAnalogico = new ArrayList<SiopeDocumentoTipoAnalogico>();

	
	
	/** Costruttore vuoto di default */
	public AggiornaTestataDocumentoSpesaModel() {
		setTitolo("Aggiorna Documenti iva Spesa");
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
	 * @return <code>true</code> se il documento &eacute; gi&agrave; legato a un subdocumento iva
	 *   (direttamente o tramite quote); <code>false</code> in caso contrario
	 */
	public Boolean getLegameConIvaPresente() {
		// Se ho il documento iva legato al capitolo, va già bene
		if(Boolean.TRUE.equals(getDocumentoIvaLegatoDocumentoPresente())) {
			return Boolean.TRUE;
		}
		// Cerco sulle quote
		for(SubdocumentoSpesa ss : getListaSubdocumentoSpesa()) {
			if(ss.getSubdocumentoIva() != null && ss.getSubdocumentoIva().getUid() != 0) {
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
	 * Controlla se lo stato del documento sia valido per l'inserimento di una nota di credito.
	 * 
	 * @return <code>true</code> se lo stato &eacute; valido; <code>false</code> in caso contrario 
	 */
	public Boolean getCheckStatoValidoPerInserimentoNota() {
		if(getDocumento() == null) {
			return Boolean.FALSE;
		}
		StatoOperativoDocumento stato = getDocumento().getStatoOperativoDocumento();
		return StatoOperativoDocumento.INCOMPLETO.equals(stato) ||
				StatoOperativoDocumento.VALIDO.equals(stato) ||
				StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.equals(stato) ||
				StatoOperativoDocumento.PARZIALMENTE_EMESSO.equals(stato);
	}
	
	/**
	 * Ottiene l'importo del documento non rilevante per l'IVA.
	 * 
	 * @return l'importo non rilevante per l'IVA
	 */
	public BigDecimal getImportoNonRilevanteIva() {
		DocumentoSpesa doc = getDocumento();
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
		DocumentoSpesa doc = getDocumento();
		BigDecimal result = BigDecimal.ZERO;
		if(doc != null) {
			result = doc.calcolaImportoTotaleRilevanteIVASubdoumenti();
		}
		return result;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link AggiornaDocumentoDiSpesa}.
	 *  
	 * @return la request creata
	 */
	public AggiornaDocumentoDiSpesa creaRequestAggiornaDocumentoDiSpesa() {
		AggiornaDocumentoDiSpesa request = creaRequest(AggiornaDocumentoDiSpesa.class);
		
		DocumentoSpesa documentoSpesa = getDocumento();
		documentoSpesa.setSoggetto(getSoggetto());
		
		request.setDocumentoSpesa(documentoSpesa);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @param impegno l'impegno rispetto cui creare la request
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiave creaRequestRicercaImpegnoPerChiave(Impegno impegno) {
		RicercaImpegnoPerChiave request = creaRequest(RicercaImpegnoPerChiave.class);
		
		request.setEnte(getEnte());
		request.setpRicercaImpegnoK(creaRicercaImpegnoK(impegno));
		
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
	
	/* *************************************************************************************************************************** */
	
	/**
	 * Imposta i dati ottenuti dalla ricerca di dettaglio del documento.
	 * 
	 * @param documento il documento tramite cui caricare il model
	 */
	public void impostaDatiDocumento(DocumentoSpesa documento) {
		setDocumento(documento);
		// Imposta le liste
		setListaSubdocumentoSpesa(documento.getListaSubdocumenti());
		// Imposto gli importi
		calcoloImporti();
		// Imposto il soggetto
		setSoggetto(documento.getSoggetto());
		// Imposto il progressivo per il numero dei subdocumenti
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
	private void impostaSubdocumentoIva(List<SubdocumentoIvaSpesa> listaSubdocumentoIva) {
		// TODO: Prendo il primo, controllare che non ce ne sia mai più di uno
		SubdocumentoIvaSpesa sis = listaSubdocumentoIva.get(0);
		setSubdocumentoIva(sis);
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
		DocumentoSpesa documentoSpesa = getDocumento();
		TipoDocumento tipoDocumento = documentoSpesa.getTipoDocumento();
		setFlagNoteCreditoAccessibile(tipoDocumento.getFlagNotaCredito());
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
		impostaFlagIvaGestibile(getDocumento(), getListaSubdocumentoSpesa());
	}
	
	/* ************************************************* Utility ******************************************************************* */

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
	 * Imposta i totali delle note e afferenti
	 * 
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
		for(SubdocumentoSpesa s : getListaSubdocumentoSpesa()){
			if(s.getImporto() == null){
				s.setImporto(BigDecimal.ZERO);
			}
			if(s.getImportoDaDedurre() == null){
				s.setImportoDaDedurre(BigDecimal.ZERO);
			}
			totale = totale.add(s.getImporto());
			totaleImpoDaDedurre = totaleImpoDaDedurre.add(s.getImportoDaDedurre());
		}
		
		for(DocumentoSpesa ds : documento.getListaDocumentiSpesaFiglio()) {
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
		BigDecimal totaleQuote = BigDecimal.ZERO;
		BigDecimal totaleNoteCredito = BigDecimal.ZERO;
		
		for(DocumentoSpesa ds : documento.getListaDocumentiSpesaFiglio()) {
			if(BilConstants.CODICE_NOTE_CREDITO.getConstant().equalsIgnoreCase(ds.getTipoDocumento().getCodiceGruppo())) {
				totaleNoteCredito = totaleNoteCredito.add(ds.getImporto());
			}
		}
		
		for(SubdocumentoSpesa sub : documento.getListaSubdocumenti()) {
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
	

	// SIAC-6186
	
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
	
}
