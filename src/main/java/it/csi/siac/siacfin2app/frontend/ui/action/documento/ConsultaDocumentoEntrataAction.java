/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
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
import it.csi.siac.siacfin2app.frontend.ui.model.documento.ConsultaDocumentoEntrataModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoCollegatoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di Action per la gestione della consultazione del Documento di Entrata.
 * 
 * @author Ahmad Nazha, Valentina Triolo
 * @version 1.0.0 - 21/03/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaDocumentoEntrataAction extends GenericBilancioAction<ConsultaDocumentoEntrataModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -4031798863441071173L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient DocumentoIvaEntrataService documentoIvaEntrataService;
	
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
		
		RicercaModulareDocumentoEntrata req = model.creaRequestRicercaModulareDocumentoEntrata();
		RicercaModulareDocumentoEntrataResponse res = documentoEntrataService.ricercaModulareDocumentoEntrata(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
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
	private void impostaDati(DocumentoEntrata doc){
		model.setDocumento(doc);
		if(doc.getImporto() != null && doc.getArrotondamento() != null){
			model.setNetto(doc.getImporto().add(doc.getArrotondamento()).subtract(doc.getImportoTotaleDaDedurreSuFatturaNoteCollegate()));
		}
		
		BigDecimal totaleQuote = doc.getTotaleImportoQuote() != null ? doc.getTotaleImportoQuote() : BigDecimal.ZERO;
		BigDecimal totaleImportoDaDedurre = doc.getTotaleImportoDaDedurreQuote() != null ? doc.getTotaleImportoDaDedurreQuote() : BigDecimal.ZERO;
		
		model.setTotaleQuote(totaleQuote);
		model.setTotaleImportoDaDedurre(totaleImportoDaDedurre);
		model.setTotaleImportoDaAttribuire(doc.getImporto().add(doc.getArrotondamento()).subtract(totaleQuote));
		model.setTotaleImportoDocumentiCollegati(doc.getTotaleImportoDocumentiEntrataFiglio());
		model.setRegistrazioneSuSingolaQuota(Boolean.valueOf(doc.getListaSubdocumentoIva() == null || doc.getListaSubdocumentoIva().isEmpty()));
	}
	
	/**
	 * Imposta i flags per l'apertura dei var&icirc; tabs.
	 * @param doc il documento
	 */
	private void impostaFlags(DocumentoEntrata doc) {
		Boolean azioneIvaGestibile = AzioniConsentiteFactory.isGestioneIvaConsentito(sessionHandler.getAzioniConsentite());
		TipoDocumento tipoDocumento = doc.getTipoDocumento();
		
		boolean ivaGestibile = Boolean.TRUE.equals(tipoDocumento.getFlagIVA())
			&& Boolean.TRUE.equals(azioneIvaGestibile)
			&& Boolean.TRUE.equals(doc.getEsisteQuotaRilevanteIva());
		model.setFlagDatiIvaAccessibile(Boolean.valueOf(ivaGestibile));
	}
	
	/**
	 * Metodo per ottenere la lista dei documenti collegati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaDocumentiCollegati(){
		final String methodName = "ottieniListaDocumentiCollegati";
		if(model.isListaDocumentiCollegatiCaricata()) {
			log.debug(methodName, "Lista gia' caricata");
			return SUCCESS;
		}
		
		RicercaDocumentiCollegatiByDocumentoEntrata req = model.creaRequestRicercaDocumentiCollegatiByDocumentoEntrata(model.getUidDocumento());
		RicercaDocumentiCollegatiByDocumentoEntrataResponse res = documentoEntrataService.ricercaDocumentiCollegatiByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati nel model");
		List<ElementoDocumentoCollegato> list = new ArrayList<ElementoDocumentoCollegato>();
		
		list.addAll(ElementoDocumentoCollegatoFactory.getInstances(res.getDocumentiSpesaFiglio()));
		list.addAll(ElementoDocumentoCollegatoFactory.getInstances(res.getDocumentiEntrataFiglio()));
		
		// SIAC-1919 + SIAC-4501
		if(isNotaCredito()){
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
	 * Carica da servizio la lista delle quote relative ad un documento di entrata il cui uid &eacute; fornito dal chiamante .
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 * @deprecated utilizzare il metodo {@link #caricamentoQuote()}
	 */
	@Deprecated
	public String popolaDettaglioQuote(){
		// Chiamare il servizio  (temporaneamente il fake) passando come parametro uidDocumentoCollegato
		// Ottenuta la response, popolare il campo model.listaQuote
		RicercaDettaglioDocumentoEntrata request = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumento());
		logServiceRequest(request);
		
		RicercaDettaglioDocumentoEntrataResponse response = documentoEntrataService.ricercaDettaglioDocumentoEntrata(request);
		logServiceResponse(response);
		// TODO
//		model.impostaDati(response);
	  	model.setIdWorkaround("_" + model.getUidDocumento() + "_");
		return SUCCESS;
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
		RicercaSinteticaModulareQuoteByDocumentoEntrata req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrata();
		RicercaSinteticaModulareQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setNumeroQuote(Integer.valueOf(res.getTotaleElementi()));
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA, res.getSubdocumentiEntrata());
		model.setListaQuoteCaricata(true);
		return SUCCESS;
	}
	
	// JIRA - 5154
	/**
	 * Caricamento delle quote di entrata documento collegato
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuoteEntrata() {
		final String methodName = "caricamentoQuoteEntrata";
		RicercaSinteticaModulareQuoteByDocumentoEntrata req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrataCollegato();
		RicercaSinteticaModulareQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuote(res.getTotale());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_COLLEGATO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_COLLEGATO, res.getSubdocumentiEntrata());
		return SUCCESS;
	}	
	
	/**
	 * Preparazione per il metodo {@link #caricamentoQuoteSpesa()}.
	 */
	public void prepareCaricamentoQuoteSpesa() {
		model.setUidDocumentoSpesa(null);
		model.setTotaleQuoteSpesa(null);
	}
	
	/**
	 * Caricamento delle quote di spesa
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricamentoQuoteSpesa() {
		final String methodName = "caricamentoQuoteSpesa";
		RicercaSinteticaModulareQuoteByDocumentoSpesa req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoSpesa();
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuoteSpesa(res.getTotale());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, res.getSubdocumentiSpesa());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #caricamentoQuoteSpesa()}.
	 */
	public void validateCaricamentoQuoteSpesa() {
		checkNotNull(model.getUidDocumentoSpesa(), "Uid documento di spesa");
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
		RicercaSinteticaModulareQuoteByDocumentoEntrata req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrataPerIva();
		RicercaSinteticaModulareQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setNumeroQuote(Integer.valueOf(res.getTotaleElementi()));
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_IVA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_IVA, res.getSubdocumentiEntrata());
		model.setListaQuoteIvaCaricata(true);
		return SUCCESS;
	}
	
	/**
	 * Carica il subdocumento di entrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaSubdocumento() {
		final String methodName = "caricaSubdocumento";
		try {
			SubdocumentoEntrata subdocumentoEntrata = ottieniSubdocumentoEntrata();
			ottieniDettaglioMovimentoGestione(subdocumentoEntrata);
			
			model.setSubdocumentoEntrata(subdocumentoEntrata);
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
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
		
		RicercaDettaglioSubdocumentoIvaEntrata ricercaDettaglioSubdocumentoIvaEntrata = model.creaRequestRicercaDettaglioSubdocumentoIvaEntrata();
		RicercaDettaglioSubdocumentoIvaEntrataResponse ricercaDettaglioSubdocumentoIvaEntrataResponse = documentoIvaEntrataService.ricercaDettaglioSubdocumentoIvaEntrata(ricercaDettaglioSubdocumentoIvaEntrata);
		
		if(ricercaDettaglioSubdocumentoIvaEntrataResponse.hasErrori()){
			addErrori(ricercaDettaglioSubdocumentoIvaEntrataResponse);
			return SUCCESS;
		}
		model.setQuotaIvaDifferita(ricercaDettaglioSubdocumentoIvaEntrataResponse.getSubdocumentoIvaEntrata());
		return SUCCESS;
	}
	
	/**
	 * Ottiene il dettaglio del subdocumento di entrata.
	 * 
	 * @return il subdocumento trovato
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private SubdocumentoEntrata ottieniSubdocumentoEntrata() throws WebServiceInvocationFailureException {
		RicercaDettaglioQuotaEntrata request = model.creaRequestRicercaDettaglioQuotaEntrata();
		logServiceRequest(request);
		RicercaDettaglioQuotaEntrataResponse response = documentoEntrataService.ricercaDettaglioQuotaEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		return response.getSubdocumentoEntrata();
	}
	
	/**
	 * Ottiene il dettaglio del movimento di gestione collegato, se presente.
	 * 
	 * @param subdocumentoEntrata il subdocumento per cui ottenere il dettaglio
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ottieniDettaglioMovimentoGestione(SubdocumentoEntrata subdocumentoEntrata) throws WebServiceInvocationFailureException {
		final String methodName = "ottieniDettaglioMovimentoGestione";
		if(subdocumentoEntrata.getAccertamento() == null) {
			// Non ho alcunche' da caricare
			log.debug(methodName, "Subdocumento " + subdocumentoEntrata.getUid() + " senza accertamento associato");
			return;
		}
		
		Accertamento accertamento = sessionHandler.getParametro(BilSessionParameter.ACCERTAMENTO);
		
		if(!ValidationUtil.isValidMovimentoGestioneFromSession(accertamento, subdocumentoEntrata.getAccertamento())) {
			RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(subdocumentoEntrata.getAccertamento(), subdocumentoEntrata.getSubAccertamento());
			logServiceRequest(request);
			
			RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(response.isFallimento()) {
				String errorMsg = "Risultato ottenuto dal servizio RicercaAccertamentoPerChiave: FALLIMENTO";
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(response.getAccertamento() == null) {
				String chiaveAccertamento = request.getpRicercaAccertamentoK().getAnnoEsercizio()
						+ "/" + request.getpRicercaAccertamentoK().getAnnoAccertamento()
						+ "/" + request.getpRicercaAccertamentoK().getNumeroAccertamento();
				String errorMsg = "Accertamento non presente per chiave " + chiaveAccertamento;
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", chiaveAccertamento));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			accertamento = response.getAccertamento();
			// Default per le liste
			accertamento.setElencoSubAccertamenti(defaultingList(accertamento.getElencoSubAccertamenti()));
			// Inizializzo il capitolo se non gia' presente
			if(accertamento.getCapitoloEntrataGestione() == null) {
				// Se il capitolo non e' stato impostato dal servizio, lo imposto io
				accertamento.setCapitoloEntrataGestione(response.getCapitoloEntrataGestione());
			}
			sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);
		}
		
		subdocumentoEntrata.setAccertamento(accertamento);
		impostaSubAccertamento(subdocumentoEntrata, accertamento);
	}
	
	/**
	 * Impostazione del subaccertamento nel subdocumento, se necessario.
	 * 
	 * @param subdocumentoEntrata il subdocumento di spesa
	 * @param accertamento        l'impegno da cui estrarre il sub
	 */
	private void impostaSubAccertamento(SubdocumentoEntrata subdocumentoEntrata, Accertamento accertamento) {
		final String methodName = "impostaSubAccertamento";
		if(subdocumentoEntrata.getSubAccertamento() == null || subdocumentoEntrata.getSubAccertamento().getNumero() == null) {
			// Non ho alcunche' da caricare
			log.debug(methodName, "Subdocumento " + subdocumentoEntrata.getUid() + " senza subaccertamento associato");
			return;
		}
		
		// Cerco il subaccertamento per numero
		for(SubAccertamento subAccertamento : accertamento.getElencoSubAccertamenti()) {
			if(subAccertamento.getNumero() != null && subdocumentoEntrata.getSubAccertamento().getNumero().equals(subAccertamento.getNumero())) {
				subdocumentoEntrata.setSubAccertamento(subAccertamento);
				return;
			}
		}
		
		log.info(methodName, "Nessun subaccertamento trovato. Tutto questo e' molto imbarazzante");
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
		RicercaModulareDocumentoEntrata req = model.creaRequestRicercaModulareDocumentoEntrataPerNotaCredito();
		RicercaModulareDocumentoEntrataResponse res = documentoEntrataService.ricercaModulareDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
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
		RicercaSinteticaModulareQuoteByDocumentoEntrata req = model.creaRequestRicercaSinteticaModulareQuoteByDocumentoEntrataPerNotaCredito();
		RicercaSinteticaModulareQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaSinteticaModulareQuoteByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_NOTA_CREDITO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_ENTRATA_PER_NOTA_CREDITO, res.getSubdocumentiEntrata());
		return SUCCESS;
	}
}
