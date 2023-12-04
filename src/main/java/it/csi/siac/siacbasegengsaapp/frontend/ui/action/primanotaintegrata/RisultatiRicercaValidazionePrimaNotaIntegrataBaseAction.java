/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;
/**
 * Classe di action per i risultati di ricerca per la validazione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 * @param <M> la tipizzazione del model
 */
public abstract class RisultatiRicercaValidazionePrimaNotaIntegrataBaseAction<M extends RisultatiRicercaValidazionePrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4521493196654743774L;
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
	public String execute() throws Exception {
		// Leggo i messaggi delle azioni precedenti
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		impostaMessaggioDiRiepilogoRicerca();
		return SUCCESS;
	}
	
	private void impostaMessaggioDiRiepilogoRicerca() {
		String riepilogo = sessionHandler.getParametro(getBilSessionParameterRiepilogo());
		sessionHandler.setParametro(getBilSessionParameterRiepilogo(), null);
		model.setRiepilogoRicerca("");
		if(riepilogo != null) {
			model.setRiepilogoRicerca(" per " + riepilogo);
		}
	}

	
	/**
	 * Validazione massiva della prima nota integrata
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String validaTutto() {
		final String methodName = "validaTutto";
		
		// Prendo la request di ricerca dalla sessione per poter costruire la nuova request di validazione
		RicercaSinteticaPrimaNotaIntegrataValidabile requestRicercaSintetica = sessionHandler.getParametroXmlType(getBilSessionParameterRequest(),
				RicercaSinteticaPrimaNotaIntegrataValidabile.class);
		
		ValidazioneMassivaPrimaNotaIntegrata req = model.creaRequestValidazioneMassivaPrimaNotaIntegrata(requestRicercaSintetica);
		logServiceRequest(req);
		// Il servizio e' asincrono: devo wrappare la request
		AsyncServiceRequestWrapper<ValidazioneMassivaPrimaNotaIntegrata> asyncRequest = wrapRequestToAsync(req);
		
		AsyncServiceResponse res = primaNotaService.validazioneMassivaPrimaNotaIntegrataAsync(asyncRequest);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(ValidazioneMassivaPrimaNotaIntegrata.class, res));
			addErrori(res);
			return INPUT;
		}
		
		// Successo - ritorno nella pagina, ma senza il pulsante per la validazione massiva
		log.debug(methodName, "Validazione massiva: elaborazione asincrona inizializzata con successo. Id dell'operazione asincrona: " + res.getIdOperazioneAsincrona());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Validazione massiva prima nota", "con successo: id dell'operazione " + res.getIdOperazioneAsincrona()));
		return SUCCESS;
	}
	
	/**
	 * Ottiene il parametro di sessione con la request
	 * @return il parametro in sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterRequest();
	/**
	 * Ottiene il parametro di sessione con il riepilogo
	 * @return il parametro in sessione
	 */
	protected abstract BilSessionParameter getBilSessionParameterRiepilogo();
	
}
