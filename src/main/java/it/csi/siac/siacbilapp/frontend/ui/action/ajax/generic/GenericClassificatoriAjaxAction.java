/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.ClassificatoriAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneCodifiche;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe centralizzante le chiamate <em>AJAX</em>.
 * La classe fornisce quattro differenti metodi legabili per l'ottenimento della risposta alla chiamata <em>AJAX</em>.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 04/07/2013
 * 
 * @param <REQ> la parametrizzazione della Request
 * @param <RES> la parametrizzazione della Response
 *
 */
public abstract class GenericClassificatoriAjaxAction<REQ extends ServiceRequest, RES extends ServiceResponse> extends GenericBilancioAction<ClassificatoriAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8471975686613317108L;

	@Override
	public String execute() {
		final String methodName = "execute";
		Integer id = model.getId();
		log.debug(methodName, "id padre: " + id);
		
		if(gestisciEventualeIdVuoto(id)) {
			log.trace(methodName, "Id non fornito");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore padre"));
			if(model.isUseDefaults()) {
				setDefaultValues();
			}
			return SUCCESS;
		}
		
		// Creazione della request
		REQ request = definisciRequest(id);
		logServiceRequest(request);
		log.debug(methodName, "Richiamo il WebService");
		RES response = ottieniResponse(request);
		log.debug(methodName, "Richiamato il WebService");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errori nella response");
			// Se ho degli errori, loggo si' da averne evidenza
			addErrori(response);
			return SUCCESS;
		}
		// Eventuale modifica della response
		if(model.isUseDefaults()) {
			responseTransform(request, response);
		}
		
		// Sorting dei risultati
		sortDeiRisultati(response);
		
		// Carico in sessione la lista ordinata
		impostaLaResponseInSessione(response);
		impostaPerInjezioneInVariazione(response);
		
		injettaResponseNelModel(response);
		
		return SUCCESS;
	}
	
	/**
	 * Esecuzione della action con l'uso dei default
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String executeWithDefaults() {
		model.setUseDefaults(true);
		return execute();
	}
	
	/**
	 * Impostazione dei valori di default nel model
	 */
	protected void setDefaultValues() {
		// Implementazione vuota, da overridare se necessario
	}

	/**
	 * Trasformazione della response nel caso sia necessario.
	 * 
	 * @param request la request inviata
	 * @param response la response ricevuta
	 */
	protected void responseTransform(REQ request, RES response) {
		// Implementazione vuota, da overridare se necessario
	}

	/**
	 * Injetta la lista nel model della variazione.
	 * 
	 * @param tipologiaClassificatore la tipologia dei classificatori
	 * @param lista                   la lista da injettare
	 */
	protected void injettaInVariazione(TipologiaClassificatore tipologiaClassificatore, List<? extends Codifica> lista) {
		BilSessionParameter parametroInSessione;
		if(Boolean.TRUE.equals(model.getDaInjettareInserimentoVariazione())) {
			parametroInSessione = BilSessionParameter.MODEL_INSERISCI_VARIAZIONE_CODIFICHE;
		} else if(Boolean.TRUE.equals(model.getDaInjettareAggiornamentoVariazione())) {
			parametroInSessione = BilSessionParameter.MODEL_AGGIORNA_VARIAZIONE_CODIFICHE;
		} else {
			// Se non è il caso delle variazioni, esco subito
			return;
		}
		Object modelVariazione = sessionHandler.getParametro(parametroInSessione);
		Method getSpecifica = ReflectionUtils.findMethod(modelVariazione.getClass(), "getSpecificaCodifiche");
		SpecificaVariazioneCodifiche modelSpecifica = (SpecificaVariazioneCodifiche) ReflectionUtils.invokeMethod(getSpecifica, modelVariazione);
		Map<TipologiaClassificatore, List<? extends Codifica>> mappaClassificatori = modelSpecifica.getMappaClassificatori();
		mappaClassificatori.put(tipologiaClassificatore, lista);
		sessionHandler.setParametro(parametroInSessione, modelVariazione);
	}
	
	/**
	 * Gestisce, eventualmente, l'assenza di un id nel model.
	 * 
	 * @param id l'id da controllare
	 * 
	 * @return il booleano corrispondente all'azione da eseguire: <code>true</code> nel caso in cui si debba bloccare l'esecuzione, <code>false</code> in caso contrario
	 */
	protected boolean gestisciEventualeIdVuoto(Integer id) {
		return id == null;
	}
	
	/* Metodi da overridare */
	
	/**
	 * Definisce la request a partire dall'id fornito.
	 * 
	 * @param id l'id fornito dal client
	 * 
	 * @return la request corrispondente
	 */
	protected abstract REQ definisciRequest(Integer id);
	
	/**
	 * Ottiene la response dal servizio.
	 * 
	 * @param request la request del servizio
	 * 
	 * @return la response corrispondente
	 */
	protected abstract RES ottieniResponse(REQ request);
	
	/**
	 * Imposta la response in sessione.
	 * 
	 * @param response la response
	 */
	protected abstract void impostaLaResponseInSessione(RES response);
	
	/**
	 * Imposta la chiamata per l'injezione nella variazione.
	 * 
	 * @param response la response da cui ottenere i dati da injettare
	 */
	protected abstract void impostaPerInjezioneInVariazione(RES response);
	
	/**
	 * Injetta la lista nella response nel model.
	 * 
	 * @param response la response da injettare
	 */
	protected abstract void injettaResponseNelModel(RES response);
	
	/**
	 * Effettua il sort dei risultati ottenuti dal servizio.
	 * 
	 * @param response la response del servizio
	 */
	protected void sortDeiRisultati(RES response) {
		// Implementazione vuota, da overridare se necessario
	}
	
}
