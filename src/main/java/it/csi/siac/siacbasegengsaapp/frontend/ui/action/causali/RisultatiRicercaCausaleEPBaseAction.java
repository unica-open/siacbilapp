/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RisultatiRicercaCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausaleResponse;

/**
 * @author Simona Paggio
 * @version 1.0.0 - 06/05/2015
 * @param <M> la tipizzazione del model
 */
public class RisultatiRicercaCausaleEPBaseAction <M extends RisultatiRicercaCausaleEPBaseModel> extends GenericBilancioAction<M> {

	@Autowired private transient CausaleService causaleService;
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5878844719000652705L;

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
		log.debug(methodName, "Uid della causale EP da aggiornare: " + model.getCausaleEP().getUid());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #annulla()}.
	 */
	public void validateAnnulla() {
		checkNotNullNorInvalidUid(model.getCausaleEP(), "Causale da annullare");
	}
	
	/**
	 * Annullamento della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		
		AnnullaCausale request = model.creaRequestAnnullaCausale();
		logServiceRequest(request);
		AnnullaCausaleResponse response = causaleService.annullaCausale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Annullata la causale con uid " + model.getCausaleEP().getUid());
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Consultazione della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid della causale EP da consultare: " + model.getCausaleEP().getUid());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #elimina()}.
	 */
	public void validateElimina() {
		checkNotNullNorInvalidUid(model.getCausaleEP(), "Causale da eliminare");
	}
	
	/**
	 * Eliminazione della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		
		EliminaCausale request = model.creaRequestEliminaCausale();
		logServiceRequest(request);
		EliminaCausaleResponse response = causaleService.eliminaCausale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Eliminata la causale con uid " + model.getCausaleEP().getUid());
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #valida()}.
	 */
	public void validateValida() {
		checkNotNullNorInvalidUid(model.getCausaleEP(), "Causale da validare");
	}
	
	/**
	 * Validazione della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String valida() {
		final String methodName = "valida";
		
		ValidaCausale request = model.creaRequestValidaCausale();
		logServiceRequest(request);
		ValidaCausaleResponse response = causaleService.validaCausale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Validata la causale con uid " + model.getCausaleEP().getUid());
		
		// Imposto il parametro di rientro, si' da ricalcolare i dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
}
