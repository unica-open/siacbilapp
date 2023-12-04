/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.RisultatiRicercaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;

/**
 * Classe di Action per i risultati della ricerca della prima nota integrata manuale (comune tra ambito FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 * @param <M> la tipizzazione del model
 */
public class RisultatiRicercaPrimaNotaIntegrataManualeBaseAction<M extends RisultatiRicercaPrimaNotaIntegrataManualeBaseModel> extends GenericBilancioAction <M>{


	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -84536314205150951L;
	@Autowired private transient PrimaNotaService primaNotaService;
	
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
		// Leggo i messaggi delle azioni precedenti
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid della prima nota libera da aggiornare: " + model.getPrimaNotaLibera().getUid());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Consultazione della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid della prima nota libera da consultare: " + model.getPrimaNotaLibera().getUid());
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #annulla()}.
	 */
	public void validateAnnulla() {
		checkNotNullNorInvalidUid(model.getPrimaNotaLibera(), "Prima Nota Libera da annullare");
	}
	
	/**
	 * Annullamento della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		
		AnnullaPrimaNota request = model.creaRequestAnnullaPrimaNota();
		logServiceRequest(request);
		AnnullaPrimaNotaResponse response = primaNotaService.annullaPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(AnnullaPrimaNota.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Annullata la prima nota libera con uid " + model.getPrimaNotaLibera().getUid());
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	
	/**
	 * Validazione per il metodo {@link #valida()}.
	 */
	public void validateValida() {
		checkNotNullNorInvalidUid(model.getPrimaNotaLibera(), "Causale da validare");
	}
	
	/**
	 * Validazione della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String valida() {
		final String methodName = "valida";
		
		ValidaPrimaNota request = model.creaRequestValidaPrimaNota();
		logServiceRequest(request);
		ValidaPrimaNotaResponse response = primaNotaService.validaPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(ValidaPrimaNota.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Validata la causale con uid " + model.getPrimaNotaLibera().getUid());
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
}
