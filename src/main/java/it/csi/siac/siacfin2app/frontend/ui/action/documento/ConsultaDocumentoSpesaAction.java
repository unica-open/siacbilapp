/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.ConsultaDocumentoSpesaModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegatoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.OrdineService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;

/**
 * Classe di Action per la gestione della consultazione del Documento di Spesa.
 * 
 * @author Ahmad Nazha, Valentina Triolo
 * @version 1.0.0 - 21/03/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaDocumentoSpesaAction extends GenericBilancioAction<ConsultaDocumentoSpesaModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9157748833471208658L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient OrdineService ordineService;
	@Autowired private transient DocumentoIvaSpesaService documentoIvaSpesaService;
	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;	
	
	@Override
	public void prepare() throws Exception {
		if(model != null) {
			cleanErroriMessaggiInformazioni();
		}
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		log.debug(methodName, "Ricerco il documento");
		
		RicercaModulareDocumentoSpesa req = model.creaRequestRicercaModulareDocumentoSpesa();
		RicercaModulareDocumentoSpesaResponse res = documentoSpesaService.ricercaModulareDocumentoSpesa(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaModulareDocumentoSpesa.class, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Documento ottenuto");
		impostaDati(res.getDocumento());
		impostaFlags(res.getDocumento());
		
		return SUCCESS;
	
	}
	
	/**
	 * Imposta i dati del Documento.
	 * @param doc il documento
	 */
	private void impostaDati(DocumentoSpesa doc){
		model.setDocumento(doc);
		if(doc.getImporto() != null && doc.getArrotondamento() != null){
			model.setNetto(doc.getImporto().add(doc.getArrotondamento()).subtract(doc.getImportoTotaleDaDedurreSuFatturaNoteCollegate()));
		}
		
		BigDecimal totaleQuote = doc.getTotaleImportoQuote() != null ? doc.getTotaleImportoQuote() : BigDecimal.ZERO;
		BigDecimal totaleImportoDaDedurre = doc.getTotaleImportoDaDedurreQuote() != null ? doc.getTotaleImportoDaDedurreQuote() : BigDecimal.ZERO;
		
		model.setTotaleQuote(totaleQuote);
		model.setTotaleImportoDaDedurre(totaleImportoDaDedurre);
		model.setTotaleDaPagareQuote(totaleQuote.subtract(totaleImportoDaDedurre));
		model.setTotaleImportoDaAttribuire(doc.getImporto().add(doc.getArrotondamento()).subtract(totaleQuote));
		model.setTotaleImportoDocumentiCollegati(doc.getTotaleImportoDocumentiEntrataFiglio());
		model.setRegistrazioneSuSingolaQuota(Boolean.valueOf(doc.getListaSubdocumentoIva() == null || doc.getListaSubdocumentoIva().isEmpty()));
		
		BigDecimal totaleRitenute = BigDecimal.ZERO;
		if(doc.getRitenuteDocumento() != null) {
			totaleRitenute = doc.getRitenuteDocumento().getImportoEsente()
				.add(doc.getRitenuteDocumento().getImportoRivalsa())
				.add(doc.getRitenuteDocumento().getImportoCassaPensioni())
				.add(doc.getRitenuteDocumento().getImportoIVA());
		}
		model.setTotaleRitenute(totaleRitenute);
	}
	
	/**
	 * Imposta i flags per l'apertura dei var&icirc; tabs.
	 * @param doc il documento
	 */
	private void impostaFlags(DocumentoSpesa doc) {
		Boolean azioneIvaGestibile = AzioniConsentiteFactory.isGestioneIvaConsentito(sessionHandler.getAzioniConsentite());
		TipoDocumento tipoDocumento = doc.getTipoDocumento();
		model.setFlagRitenuteAccessibile(tipoDocumento.getFlagRitenute());
		
		boolean ivaGestibile = Boolean.TRUE.equals(tipoDocumento.getFlagIVA())
			&& Boolean.TRUE.equals(azioneIvaGestibile)
			&& Boolean.TRUE.equals(doc.getEsisteQuotaRilevanteIva());
		model.setFlagDatiIvaAccessibile(Boolean.valueOf(ivaGestibile));
	}
	
	/**
	 * Metodo per l'ottenimento della lista dei Documenti collegati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaDocumentiCollegati(){
		final String methodName = "ottieniListaDocumentiCollegati";
		if(model.isListaDocumentiCollegatiCaricata()) {
			log.debug(methodName, "Lista gia' caricata");
			return SUCCESS;
		}
		
		RicercaDocumentiCollegatiByDocumentoSpesa req = model.creaRequestRicercaDocumentiCollegatiByDocumentoSpesa(model.getUidDocumento());
		RicercaDocumentiCollegatiByDocumentoSpesaResponse res = documentoSpesaService.ricercaDocumentiCollegatiByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaDocumentiCollegatiByDocumentoSpesa.class, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati nel model");
		List<ElementoDocumentoCollegato> list = new ArrayList<ElementoDocumentoCollegato>();
		
		list.addAll(ElementoDocumentoCollegatoFactory.getInstances(res.getDocumentiSpesaFiglio()));
		list.addAll(ElementoDocumentoCollegatoFactory.getInstances(res.getDocumentiEntrataFiglio()));
		
		// SIAC-1919 + SIAC-4501
		if(isNotaCredito()) {
			list.addAll(ElementoDocumentoCollegatoFactory.getInstances(res.getDocumentiSpesaPadre()));
			list.addAll(ElementoDocumentoCollegatoFactory.getInstances(res.getDocumentiEntrataPadre()));
		}
		
		model.setListaDocumentiCollegati(list);
		model.setListaDocumentiCollegatiCaricata(true);
		return SUCCESS;
	}
	
	/**
	 * Controlla se il tipo del documento sia nota di credito
	 * @return se il documento abbia tipo 'nota di credito'
	 */
	private boolean isNotaCredito() {
		return model.getDocumento().getTipoDocumento().isNotaCredito();
	}

	/**
	 * Caricamento della pagina.
	 * <br/>
	 * Segnaposto per caricare la JSP senza rieffettuare la ricerca
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String loadPage() {
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle quote
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuote() {
		final String methodName = "caricamentoQuote";
		if(model.isListaQuoteCaricata()) {
			log.debug(methodName, "Lista gia' caricata");
			return SUCCESS;
		}
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesa();
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaModulareQuoteByDocumentoSpesa.class, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setNumeroQuote(Integer.valueOf(res.getTotaleElementi()));
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, res.getSubdocumentiSpesa());
		model.setListaQuoteCaricata(true);
		return SUCCESS;
	}
	
	// JIRA - 5154
	/**
	 * Caricamento delle quote di spesa
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuoteSpesa() {
		final String methodName = "caricamentoQuoteSpesa";
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesaCollegato();
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaModulareQuoteByDocumentoSpesa.class, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuote(res.getTotale());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_COLLEGATO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_COLLEGATO, res.getSubdocumentiSpesa());
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #caricamentoQuoteEntrata()}.
	 */
	public void prepareCaricamentoQuoteEntrata() {
		model.setUidDocumentoEntrata(null);
		model.setTotaleQuoteEntrata(null);
	}
	
	/**
	 * Caricamento delle quote di entrata
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuoteEntrata() {
		final String methodName = "caricamentoQuoteEntrata";
		RicercaSinteticaModulareQuoteByDocumentoEntrata req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrata();
		RicercaSinteticaModulareQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaModulareQuoteByDocumentoEntrata.class, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuoteEntrata(res.getTotale());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA, res.getSubdocumentiEntrata());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #caricamentoQuoteEntrata()}.
	 */
	public void validateCaricamentoQuoteEntrata() {
		checkNotNull(model.getUidDocumentoEntrata(), "Uid documento di entrata");
	}
	
	/**
	 * Caricamento delle quote iva
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuoteIva() {
		final String methodName = "caricamentoQuoteIva";
		if(model.isListaQuoteIvaCaricata()) {
			log.debug(methodName, "Lista gia' caricata");
			return SUCCESS;
		}
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesaPerIva();
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaModulareQuoteByDocumentoSpesa.class, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setNumeroQuote(Integer.valueOf(res.getTotaleElementi()));
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_IVA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_IVA, res.getSubdocumentiSpesa());
		model.setListaQuoteIvaCaricata(true);
		return SUCCESS;
	}
	
	/**
	 * Carica le ritenute del documento.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoRitenuteDocumento() {
		final String methodName = "caricamentoRitenuteDocumento";
		if(model.isListaOnereCaricata()) {
			log.debug(methodName, "Lista gia' caricata");
			return SUCCESS;
		}
		
		RicercaOnereByDocumentoSpesa req = model.creaRequestRicercaOnereByDocumentoSpesa();
		RicercaOnereByDocumentoSpesaResponse res = documentoSpesaService.ricercaOnereByDocumentoSpesa(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaOnereByDocumentoSpesa.class, res));
			return INPUT;
		}
		
		model.setListaOnere(res.getListaDettagliOnere());
		model.setListaOnereCaricata(true);
		return SUCCESS;
	}
	
	/**
	 * Carica il subdocumento di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaSubdocumento() {
		final String methodName = "caricaSubdocumento";
		try {
			SubdocumentoSpesa subdocumentoSpesa = ottieniSubdocumentoSpesa();
			ottieniDettaglioMovimentoGestione(subdocumentoSpesa);
			
			model.setSubdocumentoSpesa(subdocumentoSpesa);
			model.setIdWorkaround(Integer.toString(subdocumentoSpesa.getUid()));
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			//SIAC-4345: gestisco l'errore
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene il dettaglio della registrazione IVA.
	 * 
	 * @return il dettaglio della quota iva differita
	 */
	public String caricaDettaglioRegistrazioneIva() {
		
		RicercaDettaglioSubdocumentoIvaSpesa ricercaDettaglioSubdocumentoIvaSpesa = model.creaRequestRicercaDettaglioSubdocumentoIvaSpesa();
		RicercaDettaglioSubdocumentoIvaSpesaResponse ricercaDettaglioSubdocumentoIvaSpesaResponse = documentoIvaSpesaService.ricercaDettaglioSubdocumentoIvaSpesa(ricercaDettaglioSubdocumentoIvaSpesa);
		
		if(ricercaDettaglioSubdocumentoIvaSpesaResponse.hasErrori()){
			addErrori(ricercaDettaglioSubdocumentoIvaSpesaResponse);
			return SUCCESS;
		}
		model.setQuotaIvaDifferita(ricercaDettaglioSubdocumentoIvaSpesaResponse.getSubdocumentoIvaSpesa());
		return SUCCESS;
	}

	/**
	 * Ottiene il dettaglio del subdocumento di spesa.
	 * 
	 * @return il subdocumento trovato
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private SubdocumentoSpesa ottieniSubdocumentoSpesa() throws WebServiceInvocationFailureException {
		RicercaDettaglioQuotaSpesa req = model.creaRequestRicercaDettaglioQuotaSpesa();
		logServiceRequest(req);
		RicercaDettaglioQuotaSpesaResponse res = documentoSpesaService.ricercaDettaglioQuotaSpesa(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioQuotaSpesa.class, res));
		}
		return res.getSubdocumentoSpesa();
	}

	/**
	 * Ottiene il dettaglio del movimento di gestione collegato, se presente.
	 * 
	 * @param subdocumentoSpesa il subdocumento per cui ottenere il dettaglio
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ottieniDettaglioMovimentoGestione(SubdocumentoSpesa subdocumentoSpesa) throws WebServiceInvocationFailureException {
		final String methodName = "ottieniDettaglioMovimentoGestione";
		if(subdocumentoSpesa.getImpegno() == null) {
			// Non ho alcunche' da caricare
			log.debug(methodName, "Subdocumento " + subdocumentoSpesa.getUid() + " senza impegno associato");
			return;
		}
		
		Impegno impegno = sessionHandler.getParametro(BilSessionParameter.IMPEGNO);
		
		if(!ValidationUtil.isValidMovimentoGestioneFromSession(impegno, subdocumentoSpesa.getImpegno())) {
			RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(subdocumentoSpesa.getImpegno(), subdocumentoSpesa.getSubImpegno());
			logServiceRequest(req);
			
			RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(RicercaImpegnoPerChiaveOttimizzato.class, res);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(res.isFallimento()) {
				String errorMsg = "Risultato ottenuto dal servizio RicercaImpegnoPerChiave: FALLIMENTO";
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(res.getImpegno() == null) {
				String chiaveImpegno = req.getpRicercaImpegnoK().getAnnoEsercizio() + "/" + req.getpRicercaImpegnoK().getAnnoImpegno() + "/" + req.getpRicercaImpegnoK().getNumeroImpegno();
				String errorMsg = "Impegno non presente per chiave " + chiaveImpegno;
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", chiaveImpegno));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			impegno = res.getImpegno();
			// Default per le liste
			impegno.setElencoSubImpegni(defaultingList(impegno.getElencoSubImpegni()));

			// Inizializzo il capitolo se non gia' presente
			if(impegno.getCapitoloUscitaGestione() == null) {
				// Se il capitolo non e' stato impostato dal servizio, lo imposto io
				impegno.setCapitoloUscitaGestione(res.getCapitoloUscitaGestione());
			}
			sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegno);
		}
		
		subdocumentoSpesa.setImpegno(impegno);
		impostaSubImpegno(subdocumentoSpesa, impegno);
	}
	
	/**
	 * Impostazione del subimpegno nel subdocumento, se necessario.
	 * 
	 * @param subdocumentoSpesa il subdocumento di spesa
	 * @param impegno           l'impegno da cui estrarre il sub
	 */
	private void impostaSubImpegno(SubdocumentoSpesa subdocumentoSpesa, Impegno impegno) {
		final String methodName = "impostaSubImpegno";
		if(subdocumentoSpesa.getSubImpegno() == null || subdocumentoSpesa.getSubImpegno().getNumeroBigDecimal() == null) {
			// Non ho alcunche' da caricare
			log.debug(methodName, "Subdocumento " + subdocumentoSpesa.getUid() + " senza subimpegno associato");
			return;
		}
		
		// Cerco il subimpegno per numero
		for(SubImpegno subImpegno : impegno.getElencoSubImpegni()) {
			if(subImpegno.getNumeroBigDecimal() != null && subdocumentoSpesa.getSubImpegno().getNumeroBigDecimal().equals(subImpegno.getNumeroBigDecimal())) {
				subdocumentoSpesa.setSubImpegno(subImpegno);
				return;
			}
		}
		
		log.info(methodName, "Nessun subimpegno trovato. Tutto questo e' molto imbarazzante");
	}
	
	/**
	 * Caricamento degli ordini.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaOrdini() {
		final String methodName = "caricaOrdini";
		log.debug(methodName, "Uid del documento di cui ottenere gli ordini: " + model.getDocumento().getUid());
		
		RicercaOrdiniDocumento req = model.creaRequestRicercaOrdiniDocumento();
		logServiceRequest(req);
		
		RicercaOrdiniDocumentoResponse res = ordineService.ricercaOrdiniDocumento(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaOrdiniDocumento.class, res));
			addErrori(res);
			return SUCCESS;
		}
		
		model.setListaOrdine(res.getOrdini());
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #caricaNotaCredito()}
	 */
	public void prepareCaricaNotaCredito() {
		model.setUidDocumentoPerRicerche(null);
		model.setNotaCredito(null);
	}
	
	/**
	 * Caricamento dati nota credito.
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaNotaCredito() {
		final String methodName = "caricaNotaCredito";
		RicercaModulareDocumentoSpesa req = model.creaRequestRicercaModulareDocumentoSpesaPerNotaCredito();
		RicercaModulareDocumentoSpesaResponse res = documentoSpesaService.ricercaModulareDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaModulareDocumentoSpesa.class, res));
			addErrori(res);
			return INPUT;
		}
		model.setNotaCredito(res.getDocumento());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #caricaNotaCredito()}
	 */
	public void validateCaricaNotaCredito() {
		checkNotNull(model.getUidDocumentoPerRicerche(), "Uid nota credito");
	}
	
	/**
	 * Caricamento delle quote della nota di credito
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuoteNotaCredito() {
		final String methodName = "caricamentoQuoteNotaCredito";
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesaPerNotaCredito();
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaModulareQuoteByDocumentoSpesa.class, res));
			addErrori(res);
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_NOTA_CREDITO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA_PER_NOTA_CREDITO, res.getSubdocumentiSpesa());
		return SUCCESS;
	}
	
	/**
	 * Caricamento dei dati della fattura FEL se presente
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaFatturaFEL() {
		final String methodName = "caricaFatturaFEL";
		if(model.getFatturaFEL() != null && model.getFatturaFEL().getIdFattura() != null) {
			// Ho gia' la fattura valorizzata
			log.debug(methodName, "Fattura FEL gia' valorizzata");
			return SUCCESS;
		}
		
		RicercaDettaglioFatturaElettronica req = model.creaRequestRicercaDettaglioFatturaElettronica();
		logServiceRequest(req);
		RicercaDettaglioFatturaElettronicaResponse res = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioFatturaElettronica.class, res));
			return INPUT;
		}
		
		model.setFatturaFEL(res.getFatturaFEL());
		
		return SUCCESS;
	}

}
