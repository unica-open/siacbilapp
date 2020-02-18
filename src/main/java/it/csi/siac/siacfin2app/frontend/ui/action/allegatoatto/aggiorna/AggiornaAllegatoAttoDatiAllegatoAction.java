/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.aggiorna;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AggiornaAllegatoAttoModel.TabVisualizzazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Classe di Action per l'aggiornamento dell'allegato atto, per i dati allegato.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/ott/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AggiornaAllegatoAttoDatiAllegatoAction extends AggiornaAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1909411460783101603L;

	/**
	 * Preparazione per il metodo {@link #effettuaAggiornamento()}.
	 */
	public void prepareEffettuaAggiornamento() {
		// Non sbianco l'intero allegato atto (non voglio perdere tutti i dati collegati).
		// Pulisco solo i dati di tipo String e Boolean
		AllegatoAtto aa = model.getAllegatoAtto();
		cleanStringAndBooleansAllegatoAtto(aa);
	}
	
	/**
	 * Pulisce (alcuni de) i campi {@link String} e {@link Boolean} dell'Allegato.
	 * 
	 * @param allegatoAtto l'allegato da pulire
	 */
	private void cleanStringAndBooleansAllegatoAtto (AllegatoAtto allegatoAtto) {
		// String fields
		allegatoAtto.setCausale(null);
		allegatoAtto.setDataScadenza(null);
		allegatoAtto.setResponsabileContabile(null);
		allegatoAtto.setAnnotazioni(null);
		allegatoAtto.setResponsabileAmministrativo(null);
		allegatoAtto.setPratica(null);
		allegatoAtto.setAltriAllegati(null);
		allegatoAtto.setNote(null);
		// Boolean fields
		allegatoAtto.setDatiSensibili(null);
		allegatoAtto.setFlagRitenute(null);
	}

	/**
	 * Effettua l'aggiornamento dell'Allegato Atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaAggiornamento() {
		final String methodName = "effettuaAggiornamento";
		// Recupero dalla sessione le liste
		recuperaDatiDaSessione();
		// Invoco il servizio di aggiornamento
		AggiornaAllegatoAtto req = model.creaRequestAggiornaAllegatoAtto();
		logServiceRequest(req);
		AggiornaAllegatoAttoResponse res = allegatoAttoService.aggiornaAllegatoAtto(req);
		logServiceResponse(res);
		
		//Se ho errori, esco
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Aggiornato Allegato Atto con uid " + model.getAllegatoAtto().getUid());
		// Reimposto i dati nel model
		model.setAllegatoAtto(res.getAllegatoAtto());
		// Forzo il rientro sulla prima pagina
		model.setTabVisualizzazione(TabVisualizzazione.DATI);
		// Imposto il messaggio di successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Recupera i dati dalla sessione.
	 */
	private void recuperaDatiDaSessione() {
		final String methodName = "recuperaDatiDaSessione";
		log.debug(methodName, "Recupero dalla sessione le liste di ElencoDocumentiAllegato e di DatiSoggettoAllegato");
		List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = sessionHandler.getParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO);
		List<DatiSoggettoAllegato> listaDatiSoggettoAllegato = sessionHandler.getParametro(BilSessionParameter.LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO);
		
		model.getAllegatoAtto().setElenchiDocumentiAllegato(listaElencoDocumentiAllegato);
		model.getAllegatoAtto().setDatiSoggettiAllegati(listaDatiSoggettoAllegato);
	}

	/**
	 * Validazione per il metodo {@link #effettuaAggiornamento()}.
	 */
	public void validateEffettuaAggiornamento() {
		AllegatoAtto aa = model.getAllegatoAtto();
		checkCondition(aa != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato atto"));
		checkCondition(StringUtils.isNotBlank(aa.getCausale()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Causale"));
		
		if(aa.getFlagRitenute() == null) {
			aa.setFlagRitenute(Boolean.FALSE);
		}
	}
	
	/**
	 * Stampa dell'allegato atto
	 * @return una Stringa corrispondente al risultato dell'invocazione 
	 */
	public String stampa() {
		final String methodName = "stampa";
		
		// Creazione request
		StampaAllegatoAtto req = model.creaRequestStampaAllegatoAtto();
		logServiceRequest(req);
		// Invocazione del servizio asincrono
		StampaAllegatoAttoResponse res = allegatoAttoService.stampaAllegatoAttoAsync(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("stampa Allegato all'atto", "il risultato sara' disponibile sul Cruscotto"));
		model.setStampaAbilitato(false);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #stampa()}.
	 */
	public void validateStampa() {
		checkNotNullNorInvalidUid(model.getAllegatoAtto(), "allegato");
	}
	
	/**
	 * Invio dell'allegato atto a FLUX
	 * @return una Stringa corrispondente al risultato dell'invocazione 
	 */
	public String invio() {
		final String methodName = "invio";
		
		InviaAllegatoAtto req = model.creaRequestInviaAllegatoAtto();
		logServiceRequest(req);
		
		// Invocazione del servizio asincrono
		AzioneRichiesta azioneRichiesta = AzioniConsentite.ALLEGATO_ATTO_INVIA.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
		AsyncServiceResponse res = allegatoAttoService.inviaAllegatoAttoAsync(wrapRequestToAsync(req, azioneRichiesta));
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("invio Allegato all'atto", ""));
		model.setInvioFluxAbilitato(false);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #invio()}.
	 */
	public void validateInvio() {
		checkNotNullNorInvalidUid(model.getAllegatoAtto(), "allegato");
	}
	
	// SIAC-5172
	/**
	 * Preparazione per il metodo {@link #sospendiTuttoAllegato()}
	 */
	public void prepareSospendiTuttoAllegato() {
		model.setDatiSoggettoAllegato(null);
	}
	
	/**
	 * Sospensione dei dati dei soggetti per l'intero allegato atto
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String sospendiTuttoAllegato() {
		final String methodName = "sospendiTuttoAllegato";
		AggiornaMassivaDatiSoggettoAllegatoAtto req = model.creaRequestAggiornaMassivaDatiSoggettoAllegatoAtto(null);
		AggiornaMassivaDatiSoggettoAllegatoAttoResponse res = allegatoAttoService.aggiornaMassivaDatiSoggettoAllegatoAtto(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return SUCCESS;
		}
		
		log.debug(methodName, "Aggiornamento dei dati sospensione avvenuta con successo");
		
		try {
			caricaListaDatiSoggettoAllegato();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return SUCCESS;
		}
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #sospendiTuttoAllegato()}
	 */
	public void validateSospendiTuttoAllegato() {
		validazioneModificheDatiSoggettoAllegato();
	}
	
	/**
	 * Preparazione per il metodo {@link #ricercaDatiSospensioneAllegato()}
	 */
	public void prepareRicercaDatiSospensioneAllegato() {
		model.setDatiSoggettoAllegato(null);
		model.setDatiSoggettoAllegatoDeterminatiUnivocamente(false);
	}
	
	/**
	 * Ricerca dei dati di sospensione per l'allegato atto
	 * @return i dati di sospensione
	 */
	public String ricercaDatiSospensioneAllegato() {
		final String methodName = "ricercaDatiSospensioneAllegato";
		RicercaDatiSospensioneAllegatoAtto req = model.creaRequestRicercaDatiSospensioneAllegatoAtto(null);
		RicercaDatiSospensioneAllegatoAttoResponse res = allegatoAttoService.ricercaDatiSospensioneAllegatoAtto(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		
		impostaDatiSoggettoAllegatoDaRicerca(res);
		
		return SUCCESS;
	}

}
