/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo.pagamento;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettereResponse;

/**
 * Classe di action per l'emissione di ordinativi di pagamento, gestione dell'elenco.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericEmissioneOrdinativiAction.SESSION_MODEL_NAME_PAGAMENTO)
public class EmissioneOrdinativiPagamentoElencoAction extends EmissioneOrdinativiPagamentoAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 970413837541765320L;

	@Override
	public String completeStep1() {
		final String methodName = "completeStep1";
		
		// Effettuo la ricerca degli elenchi
		RicercaElencoDaEmettere req = model.creaRequestRicercaElencoDaEmettere();
		logServiceRequest(req);
		RicercaElencoDaEmettereResponse res = allegatoAttoService.ricercaElencoDaEmettere(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		// Dati trovati. Imposto i dati in sessione
		log.debug(methodName, "Imposto in sessione la request e lista dei risultati");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE, res.getElenchiDocumentiAllegato());
		
		// Imposto i dati nel model
		model.setNumeroElenchi(res.getTotaleElementi());
		model.setTotaleElenchi(res.getTotaleSpeseCollegate());
		model.setTotaleSpeseCollegate(res.getTotaleDaPagare());
		
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
		
		// SIAC-5133: popolo la SAC
		popolaStrutturaAmministrativoContabile();
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
	@AnchorAnnotation(value = GenericEmissioneOrdinativiAction.ANCHOR_VALUE_PAGAMENTO, name = "Emissione Ordinativi Pagamento Elenco STEP2")
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
		EmetteOrdinativiDiPagamentoDaElenco req = model.creaRequestEmetteOrdinativiDiPagamentoDaElencoByElenco();
		logServiceRequest(req);
		AsyncServiceResponse res = emissioneOrdinativiService.emetteOrdinativiDiPagamentoDaElenco(wrapRequestToAsync(req));
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione asincrona attivata con uid " + res.getIdOperazioneAsincrona());
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());
		model.popolaIdsElenchiElaborati();
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Emissione ordinativi di pagamento", ""));
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkCondition(model.getListElenchi() != null && !model.getListElenchi().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco"));
		checkDataScadenza("data esecuzione pagamento", "Nessuna Data");
	}
	
}
