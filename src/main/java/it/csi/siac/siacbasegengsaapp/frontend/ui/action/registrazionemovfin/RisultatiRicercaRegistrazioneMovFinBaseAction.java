/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.AssegnaContoEPRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Action per i risultati di ricerca della registrazione
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 * @param <M> la tipizzazione del Model
 * 
 */
public abstract class RisultatiRicercaRegistrazioneMovFinBaseAction<M extends RisultatiRicercaRegistrazioneMovFinBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6822766880526256358L;
	
	/** Il servizio della registrazione movfin */
	@Autowired protected transient RegistrazioneMovFinService registrazioneMovFinService;
	
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
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		model.setRiepilogoRicerca((String) sessionHandler.getParametro(BilSessionParameter.RIEPILOGO_RICERCA_REGISTRAZIONE_GEN));
		
		log.debug(methodName, "StartPosition = " + startPosition);
		
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		
		return SUCCESS;
	}

	
	/**
	 * Annullamento della registrazione
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Annullamento della registrazione avente uid = " + model.getUidDaAnnullare());
		
		log.warn(methodName, "NON ANCORA SVILUPPATO");
		//TODO da non sviluppare in V1
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
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di completa.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String completa() {
		final String methodName = "completa";
		log.debug(methodName, "Uid del documento da completare: " + model.getUidDaCompletare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di completa e valida.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String completaEValida() {
		final String methodName = "completaEValida";
		log.debug(methodName, "Uid del documento da completare: " + model.getUidDaCompletare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validazione della registrazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String valida() {
		final String methodName = "valida";
		log.debug(methodName, "Uid del documento da validare: " + model.getUidDaValidare());
		
		AssegnaContoEPRegistrazioneMovFin request = model.creaRequestAssegnaContoEPRegistrazioneMovFin();
		logServiceRequest(request);
		AssegnaContoEPRegistrazioneMovFinResponse response = registrazioneMovFinService.assegnaContoEPRegistrazioneMovFin(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		if(hasContoAssegnato(response.getRegistrazioneMovFin())) {
			log.debug(methodName, "Assegnato conto ep alla registrazione con uid " + model.getUidDaValidare()
					+ ". Conto assegnato: " + response.getRegistrazioneMovFin().getConto().getUid());
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
			impostaInformazioneSuccesso();
		} else {
			log.debug(methodName, "Servizio AssegnaContoEPRegistrazioneMovFin richiamato con successo ma nessun conto associato alla registrazione");
			addMessaggio(ErroreCore.OPERAZIONE_EFFETTUATA.getErrore("assegnazione conto alla registrazione", "ma nessun conto e' stato associato"));
		}
		
		return SUCCESS;
	}
	
	/**
	 * Controlla se la registrazione mov fin ha un conto assegnato.
	 * 
	 * @param registrazioneMovFin la registrazione
	 * 
	 * @return <code>true</code> se la registrazione ha un conto; <code>false</code> altrimenti
	 */
	private boolean hasContoAssegnato(RegistrazioneMovFin registrazioneMovFin) {
		return registrazioneMovFin != null && registrazioneMovFin.getConto() != null && registrazioneMovFin.getConto().getUid() != 0;
	}

	/**
	 * Validazione per il metodo {@link #valida()}.
	 */
	public void validateValida() {
		checkCondition(model.getUidDaValidare() > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("movimento da validare"));
	}
}
