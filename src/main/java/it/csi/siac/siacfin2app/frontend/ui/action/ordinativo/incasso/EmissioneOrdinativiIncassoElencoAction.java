/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.incasso;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.GenericEmissioneOrdinativiAction;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettereResponse;

/**
 * Classe di action per l'emissione di ordinativi di incasso, gestione dell'elenco.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericEmissioneOrdinativiAction.SESSION_MODEL_NAME_INCASSO)
public class EmissioneOrdinativiIncassoElencoAction extends EmissioneOrdinativiIncassoAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5863849999636665274L;

	@Override
	public String completeStep1() {
		final String methodName = "completeStep1";
		
		// Effettuo la ricerca degli elenchi
		RicercaElencoDaEmettere request = model.creaRequestRicercaElencoDaEmettere();
		logServiceRequest(request);
		RicercaElencoDaEmettereResponse response = allegatoAttoService.ricercaElencoDaEmettere(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaElencoDaEmettere.class, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		// Dati trovati. Imposto i dati in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE, response.getElenchiDocumentiAllegato());
		
		// Imposto i dati nel model
		model.setNumeroElenchi(response.getTotaleElementi());
		model.setTotaleElenchi(response.getTotaleEntrateCollegate());
		model.setTotaleEntrateCollegate(response.getTotaleDaIncassare());
		
		// Svuoto la distinta
		model.setDistinta(null);
		model.setNota(null);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		checkCondition(model.getTipoEmissioneOrdinativo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo emissione"), true);
		// Se ho errori, esco subito
		if(hasErrori()) {
			return;
		}
		// Completo la validazione
		checkAttoAmministrativo(StatoOperativoAtti.DEFINITIVO.name());
	}
	
	/**
	 * Ingresso nello step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = GenericEmissioneOrdinativiAction.ANCHOR_VALUE_INCASSO, name = "Emissione Ordinativi Incasso Elenco STEP2")
	public String step2() {
		final String methodName = "step2";
		// Caricamento lista conto tesoreria
		try {
			caricaListaContoTesoreria();
			caricaListaBollo();
			caricaListaClassificatoreStipendi();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento delle liste: " + wsife.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		// Svuoto la lista degli elenchi
		model.setListElenchi(null);
		model.setNota(null);
		model.setDistinta(null);
		model.setContoTesoreria(null);
		model.setCodiceBollo(null);
	}
	
	/**
	 * Completamento dello step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		EmetteOrdinativiDiIncassoDaElenco request = model.creaRequestEmetteOrdinativiDiIncassoDaElencoByElenco();
		logServiceRequest(request);
		AsyncServiceResponse response = emissioneOrdinativiService.emetteOrdinativiDiIncassoDaElenco(wrapRequestToAsync(request));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(EmetteOrdinativiDiIncassoDaElenco.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione asincrona attivata con uid " + response.getIdOperazioneAsincrona());
		model.setIdOperazioneAsincrona(response.getIdOperazioneAsincrona());
		model.popolaIdsElenchiElaborati();
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Emissione ordinativi di incasso", ""));
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkCondition(model.getListElenchi() != null && !model.getListElenchi().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco"));
		checkDataScadenza("data scadenza", "Nessuna Data");
	}

	
}
