/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RisultatiRicercaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolatiResponse;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 15/set/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAllegatoAttoAction extends RisultatiRicercaAllegatoAttoBaseAction<RisultatiRicercaAllegatoAttoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3015528792486862396L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Ottengo i messaggi
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		// Imposto i dati in sessione per ricaricare la lista al rientro
		log.debug(methodName, "Uid del registro da aggiornare: " + model.getUidAllegatoAtto());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	
	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		// Redirigo semplicemente
		log.debug(methodName, "Uid del registro da consultare: " + model.getUidAllegatoAtto());
		return SUCCESS;
	}

	/**
	 * Annullamento dell'allegato.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		
		// Creo la request per l'annullamento dell'allegato atto
		AnnullaAllegatoAtto req = model.creaRequestAnnullaAllegatoAtto();
		logServiceRequest(req);
		// Invocazione del servizio
		AnnullaAllegatoAttoResponse response = allegatoAttoService.annullaAllegatoAtto(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AnnullaAllegatoAtto.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Annullato registro con uid " + model.getUidAllegatoAtto());
		// Imposto il parametro RIENTRO per avvertire la action di ricaricare la lista
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		// Imposto il successo in sessione
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	
	/**
	 * Controllo importi impegni vincolati.
	 *
	 * @return the string
	 */
	public String controlloImportiImpegniVincolati() {
		final String methodName = "controlloImportiImpegniVincolati";
		
		// Creo la request per l'annullamento dell'allegato atto
		ControlloImportiImpegniVincolati req = model.creaRequestControlloImportiImpegniVincolati();
		logServiceRequest(req);
		// Invocazione del servizio
		ControlloImportiImpegniVincolatiResponse response = allegatoAttoService.controlloImportiImpegniVincolati(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(ControlloImportiImpegniVincolati.class, response));
			addErrori(response);
			return INPUT;
		}
		
		model.addMessaggi(response.getMessaggi());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #annulla()}.
	 */
	public void validateAnnulla() {
		// L'uid deve essere valorizzato
		checkCondition(model.getUidAllegatoAtto() != null && model.getUidAllegatoAtto().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato"));
	}
	
	/**
	 * Completamento dell'allegato.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String completa() {
		final String methodName = "completa";
		
		// Creo la request sincrona
		CompletaAllegatoAtto req = model.creaRequestCompletaAllegatoAtto();
		logServiceRequest(req);
		
		// Devo modificare l'azione richiesta (JIRA SIAC-1944)
		AzioneRichiesta azioneRichiesta = AzioneConsentitaEnum.ALLEGATO_ATTO_COMPLETA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		// Invoco il servizio asincrono
		AsyncServiceResponse response = allegatoAttoService.completaAllegatoAttoAsync(wrapRequestToAsync(req, azioneRichiesta));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(CompletaAllegatoAtto.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Completamento del registro con uid " + model.getUidAllegatoAtto() + " in corso");
		// Imposto il parametro RIENTRO per avvertire la action di ricaricare la lista
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		// Imposto il successo in sessione
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Completamento dell'allegato atto", ""));
		setInformazioniInSessionePerActionSuccessiva();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completa()}.
	 */
	public void validateCompleta() {
		// L'uid deve essere valorizzato
		checkCondition(model.getUidAllegatoAtto() != null && model.getUidAllegatoAtto().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato"));
	}
	
	/**
	 * Stampa dell'allegato atto.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String stampa() {
		final String methodName = "stampa";
		
		// Creo la request di stampa
		StampaAllegatoAtto req = model.creaRequestStampaAllegatoAtto();
		logServiceRequest(req);
		// Invocazione del servizio asincrono
		StampaAllegatoAttoResponse res = allegatoAttoService.stampaAllegatoAttoAsync(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(StampaAllegatoAtto.class, res));
			addErrori(res);
			return INPUT;
		}
		
		// Imposto il messaggio di elaborazione attivata in sessione
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("stampa Allegato all'atto", "il risultato sara' disponibile sul Cruscotto"));
		setInformazioniInSessionePerActionSuccessiva();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #stampa()}.
	 */
	public void validateStampa() {
		// L'uid deve essere valorizzato
		checkCondition(model.getUidAllegatoAtto() != null && model.getUidAllegatoAtto().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato"));
	}
	
	/** 
	 * Invio dell'allegato atto per firma.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String invia() {
		final String methodName = "invia";
		
		// Creo la request del servizio
		InviaAllegatoAtto req = model.creaRequestInviaAllegatoAtto();
		logServiceRequest(req);
		
		// Invocazione del servizio asincrono
		AzioneRichiesta azioneRichiesta = AzioneConsentitaEnum.ALLEGATO_ATTO_INVIA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse response = allegatoAttoService.inviaAllegatoAttoAsync(wrapRequestToAsync(req, azioneRichiesta));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InviaAllegatoAtto.class, response));
			addErrori(response);
			return INPUT;
		}
		
		// Imposto il messaggio di elaborazione attivata in sessione
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("invio Allegato all'atto", ""));
		setInformazioniInSessionePerActionSuccessiva();
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #invia()}.
	 */
	public void validateInvia() {
		// L'uid deve essere valorizzato
		checkCondition(model.getUidAllegatoAtto() != null && model.getUidAllegatoAtto().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato"));
	}
	
	/**
	 * Convalida dell'allegato atto
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String convalida() {
		// Forzo il ricaricamento dei dati
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #convalida()}.
	 */
	public void validateConvalida() {
		// L'uid deve essere valorizzato
		checkCondition(model.getUidAllegatoAtto() != null && model.getUidAllegatoAtto().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato"));
	}
}
