/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRisultatiRicercaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomaleResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;

/**
 * Classe base di action per i risultati della ricerca della richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/02/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public class BaseRisultatiRicercaRichiestaEconomaleAction<M extends BaseRisultatiRicercaRichiestaEconomaleModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4415241577728659327L;
	
	/** Serviz&icirc; della richiesta economale */
	@Autowired private transient RichiestaEconomaleService richiestaEconomaleService;
	/** Serviz&icirc; della stampa cassa economale */
	@Autowired protected transient StampaCassaEconomaleService stampaCassaEconomaleService;

	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Imposto la posizione die start
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		// Leggo i messaggi delle azioni precedenti
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		Integer startPosition = Integer.valueOf(0);
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			startPosition = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "startPosition = " + startPosition);
		
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid della richiesta da aggiornare: " + model.getUidRichiesta());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiorna()}.
	 */
	public void validateAggiorna() {
		checkNotNull(model.getUidRichiesta(), "richiesta economale", true);
		
		controllaAggiornabilita();
	}
	
	/**
	 * Controllo dell'aggiornabilit&agrave; tramite invocazione al servizio
	 */
	private void controllaAggiornabilita() {
		final String methodName = "controllaAggiornabilita";
		
		ControllaAggiornabilitaRichiestaEconomale request = model.creaRequestControllaAggiornabilitaRichiestaEconomale();
		logServiceRequest(request);
		ControllaAggiornabilitaRichiestaEconomaleResponse response = richiestaEconomaleService.controllaAggiornabilitaRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errori nella response: la richesta non e' aggiornabile");
			addErrori(response);
			return;
		}
	}

	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid della richiesta da consultare: " + model.getUidRichiesta());
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di rendicontazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String rendiconta() {
		final String methodName = "rendiconta";
		log.debug(methodName, "Uid della richiesta da rendicontare: " + model.getUidRichiesta());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di aggiornamento del rendiconto.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiornaRendiconto() {
		final String methodName = "aggiornaRendiconto";
		log.debug(methodName, "Uid del rendiconto da aggiornare: " + model.getUidRichiesta());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di consultazione del rendiconto.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consultaRendiconto() {
		final String methodName = "consultaRendiconto";
		log.debug(methodName, "Uid del rendiconto da consultare: " + model.getUidRichiesta());
		return SUCCESS;
	}
	
	/**
	 * Annullamento della richiesta economale
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Uid della richiesta da annullare: " + model.getUidRichiesta());
		
		AnnullaRichiestaEconomale request = model.creaRequestAnnullaRichiestaEconomale();
		logServiceRequest(request);
		AnnullaRichiestaEconomaleResponse response = richiestaEconomaleService.annullaRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AnnullaRichiestaEconomale.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Annullata richiesta economale con uid " + model.getUidRichiesta());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #annulla()}.
	 */
	public void validateAnnulla() {
		checkNotNull(model.getUidRichiesta(), "Richiesta da annullare");
	}
	
	/**
	 * azione stampa ricevuta
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String stampaRicevuta() {
		final String methodName = "stampaRicevuta";
		StampaRicevutaRichiestaEconomale request = model.creaRequestStampaRicevutaRichiestaEconomale();
		logServiceRequest(request);
		StampaRicevutaRichiestaEconomaleResponse response = stampaCassaEconomaleService.stampaRicevutaRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(StampaRicevutaRichiestaEconomale.class, response));
			addErrori(response);
			return INPUT;
		}
		// verifico se la response riporta il file 
		if(response.getReport() == null) {
			log.debug(methodName, "Nessun file presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		handleFileExtraction(response.getReport());
		return SUCCESS;
	}

	/**
	 * Gestione dell'estrazione del file dalla response
	 * @param il file da estrarre
	 */
	private void handleFileExtraction(File file) {
		final String methodName = "handleFileExtraction";
		// Creazione dei dati per il file pdf
		model.setContentType(file.getMimeType());
		model.setContentLength(file.getDimensione());
		model.setFileName(file.getNome());
		
		InputStream inputStream = new ByteArrayInputStream(file.getContenuto());
		model.setInputStream(inputStream);
		
		log.debug(methodName, "generazione pdf in corso...");
	}
	
	/**
	 * Azione stampa ricevuta del rendiconto
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String stampaRicevutaRendiconto() {
		final String methodName = "stampaRicevutaRendiconto";
		StampaRicevutaRendicontoRichiestaEconomale req = model.creaRequestStampaRicevutaRendicontoRichiestaEconomale();
		logServiceRequest(req);
		StampaRicevutaRendicontoRichiestaEconomaleResponse res = stampaCassaEconomaleService.stampaRendicontoRicevutaRichiestaEconomale(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(StampaRicevutaRendicontoRichiestaEconomale.class, res));
			addErrori(res);
			return INPUT;
		}
		// verifico se la response riporta il file 
		if(res.getReport() == null) {
			log.debug(methodName, "Nessun file presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		handleFileExtraction(res.getReport());
		return SUCCESS;
	}
	
}
