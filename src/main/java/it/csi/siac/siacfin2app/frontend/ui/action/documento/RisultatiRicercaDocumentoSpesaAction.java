/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RisultatiRicercaDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.OrdineService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdineResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdineResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdineResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;

/**
 * Action per i risultati di ricerca del documento di spesa
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 06/02/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDocumentoSpesaAction extends GenericBilancioAction<RisultatiRicercaDocumentoSpesaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2271333474213144286L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient OrdineService ordineService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		if(posizioneStart == null) {
			posizioneStart = Integer.valueOf(0);
		}
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		
		model.setRiepilogoRicerca((String) sessionHandler.getParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO));
		
		model.setImportoTotale((BigDecimal) sessionHandler.getParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA));
		log.debug(methodName, "StartPosition = " + startPosition);
		
		return SUCCESS;
	}

	

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid del documento da aggiornare: " + model.getUidDaAggiornare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}

	/**
	 * Annullamento del documento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Annullamento del documento avente uid=" + model.getUidDaAnnullare());
		
		log.debug(methodName, "Creazione della request ed invocazione del servizio");
		
		AnnullaDocumentoSpesa request = model.creaRequestAnnullaDocumentoSpesa();
		AnnullaDocumentoSpesaResponse response = documentoSpesaService.annullaDocumentoSpesa(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Documento annullato: imposto il valore TRUE al parametro RIENTRO e torno alla pagina");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
	
	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid del documento da consultare: " + model.getUidDaConsultare());
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di consultazione quote.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	
	public String ottieniListaQuoteSpesa(){
		final String methodName = "ottieniListaQuoteSpesa";
		log.debug(methodName, "Uid del documento di cui consultare le quote: " + model.getUidDaConsultare());
		
		RicercaSinteticaModulareQuoteByDocumentoSpesa request = model.creaRicercaSinteticaModulareQuoteByDocumentoSpesa();
		logServiceRequest(request);
		
		RicercaSinteticaModulareQuoteByDocumentoSpesaResponse response = documentoSpesaService.ricercaSinteticaModulareQuoteByDocumentoSpesa(request);
		logServiceResponse(response);
		
		if(response.isFallimento()) {
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Imposto i dati in sessione");
		model.setTotaleQuote(response.getTotale());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_QUOTE_DOCUMENTO_SPESA, response.getSubdocumentiSpesa());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ottieniListaQuoteSpesa()}.
	 */
	public void validateOttieniListaQuoteSpesa() {
		checkCondition(model.getUidDaConsultare() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Documento"));
	}
	
	/**
	 * Ottiene la lista degli ordini
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	
	public String ottieniListaOrdine(){
		final String methodName = "ottieniListaOrdine";
		log.debug(methodName, "Uid del documento di cui ottenere gli ordini: " + model.getUidDaConsultare());
		
		RicercaOrdiniDocumento request = model.creaRequestRicercaOrdiniDocumento();
		logServiceRequest(request);
		
		RicercaOrdiniDocumentoResponse response = ordineService.ricercaOrdiniDocumento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaOrdine(response.getOrdini());
		return SUCCESS;
	}
	
	/**
	 * Aggiorna l'ordine.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiornaOrdine(){
		final String methodName = "aggiornaOrdine";
		log.debug(methodName, "Uid dell'ordine: " + model.getOrdine().getUid());
		
		AggiornaOrdine request = model.creaRequestAggiornaOrdine();
		logServiceRequest(request);
		
		AggiornaOrdineResponse response = ordineService.aggiornaOrdine(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ordine con uid " + model.getOrdine().getUid() + " aggiornato");
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	
	/**
	 * Elimina l'ordine.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String eliminaOrdine(){
		final String methodName = "eliminaOrdine";
		log.debug(methodName, "Uid dell'ordine: " + model.getOrdine().getUid());
		
		EliminaOrdine request = model.creaRequestEliminaOrdine();
		logServiceRequest(request);
		
		EliminaOrdineResponse response = ordineService.eliminaOrdine(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ordine con uid " + model.getOrdine().getUid() + " eliminato.");
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #eliminaOrdine()}.
	 */
	public void validateEliminaOrdine() {
		checkNotNullNorInvalidUid(model.getOrdine(), "Ordine");
	}
	
	/**
	 * Inserisce l'ordine.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String inserisceOrdine(){
		final String methodName = "inserisceOrdine";
		log.debug(methodName, "Uid dell'ordine: " + model.getOrdine().getUid());
		
		InserisceOrdine request = model.creaRequestInserisceOrdine();
		logServiceRequest(request);
		
		InserisceOrdineResponse response = ordineService.inserisceOrdine(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ordine con uid " + model.getOrdine().getUid() + " inserito.");
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	
	
	/**
	 * Validazione per il metodo {@link #aggiornaOrdine()}.
	 */
	public void validateAggiornaOrdine() {
		checkNotNullNorInvalidUid(model.getOrdine(), "ordine", true);
		checkNotNullNorInvalidUid(model.getOrdine().getDocumento(), "documento");
		checkNotNullNorEmpty(model.getOrdine().getNumeroOrdine(), "numero ordine");
	}
	
	/**
	 * Attivazione delle registrazioni contabili.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String attivaRegistrazioniContabili() {
		final String methodName = "attivaRegistrazioniContabili";
		log.debug(methodName, "Uid del documento per cui attivare le registrazioni contabili da aggiornare: " + model.getUidDaAggiornare());
		
		AttivaRegistrazioniContabiliSpesa request = model.creaRequestAttivaRegistrazioniContabiliSpesa();
		logServiceRequest(request);
		
		AttivaRegistrazioniContabiliSpesaResponse response = documentoSpesaService.attivaRegistrazioniContabiliSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Registrazioni contabili attivate per il documento " + model.getUidDaAggiornare());
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #attivaRegistrazioniContabili()}.
	 */
	public void validateAttivaRegistrazioniContabili() {
		checkCondition(model.getUidDaAggiornare() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("documento"));
	}
}
