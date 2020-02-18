/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RicercaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.RicercaPrimaNotaIntegrataFINModel;

/**
 * Classe di action per la ricerca della prima nota libera. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/05/2015
 * @version 1.0.1 - 13/05/2015
 * @version 1.1.0 - 08/10/2015 - gestione GEN/GSA
 * @version 1.1.1 - 21/12/2015 - JIRA-2739 - caricamento asincrono delle liste
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPrimaNotaIntegrataFINAction extends RicercaPrimaNotaIntegrataBaseAction<RicercaPrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8172807815409126474L;

	@Override
	protected void caricaListe() {
		final String methodName = "caricaListe";
		caricaListaTipoEventoDaSessione();
		caricaListaEvento();
		caricaListaStatoOperativoRegistrazioneMovFin();
		caricaListaClassiDaSessione();
		caricaListaTitoliDaSessione();
		try {
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
			// SIAC-5292
			// Carico la lista dei tipi finanziamento
			caricaListaTipoFinanziamento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// TODO: fare qualcosa?
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Aggiungo il breadcrumb
		return super.execute();
	}
	
	@Override
	protected boolean checkPianoDeiConti() {
		final String methodName = "checkPianoDeiConti";
		if(model.getRegistrazioneMovFin() == null || model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato() == null
				|| StringUtils.isBlank(model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice())) {
			// Non ho il campo: trivialmente il controllo non e' valido
			return false;
		}
		
		// Ricerca del conto
		LeggiElementoPianoDeiContiByCodiceAndAnno req = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato());
		logServiceRequest(req);
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse res = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Se ho errori esco
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}
		
		try {
			// 1. Il conto deve esistere
			checkCondition(res.getElementoPianoDeiConti() != null,
				ErroreCore.ENTITA_INESISTENTE.getErrore("Il conto finanziario", model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice()),
				true);
			
			ElementoPianoDeiConti epdc = res.getElementoPianoDeiConti();
			
			// 2. Deve essere valido nell'anno di bilancio
			checkCondition(ValidationUtil.isEntitaValidaPerAnnoEsercizio(epdc, model.getAnnoEsercizioInt()),
				ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " non ha un'istanza valida nell'anno di bilancio"),
				true);
			
			// 3. Deve essere di quinto livello
			checkCondition(epdc.getLivello() == 5,
				ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " e' di livello " + epdc.getLivello()),
				true);
			
			model.getRegistrazioneMovFin().setElementoPianoDeiContiAggiornato(epdc);
		} catch(ParamValidationException pve) {
			// Errore di validazione
			log.info(methodName, "Errore di validazione per il piano dei conti: " + pve.getMessage());
			return false;
		}
		
		return true;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_GEN;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_GEN;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassePiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GEN;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRiepilogo() {
		return BilSessionParameter.RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_GEN;
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	private void caricaListaTitoliDaSessione() {
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata != null) {
			model.setListaTitoloEntrata(listaTitoloEntrata);
		}
		if(listaTitoloSpesa != null) {
			model.setListaTitoloSpesa(listaTitoloSpesa);
		}
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaTitoli() {
		final String methodName = "caricaListaTitoli";
		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
		
		if(listaTitoloEntrata == null || listaTitoloSpesa == null) {
			LeggiClassificatoriByTipoElementoBilResponse responseEntrata;
			LeggiClassificatoriByTipoElementoBilResponse responseSpesa;
			
			try {
				responseEntrata = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
				responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			} catch(WebServiceInvocationFailureException wsife) {
				log.info(methodName, wsife.getMessage());
				return INPUT;
			}
			
			listaTitoloEntrata = responseEntrata.getClassificatoriTitoloEntrata();
			listaTitoloSpesa = responseSpesa.getClassificatoriTitoloSpesa();
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, listaTitoloEntrata);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, listaTitoloSpesa);
		}
		
		model.setListaTitoloEntrata(listaTitoloEntrata);
		model.setListaTitoloSpesa(listaTitoloSpesa);
		return SUCCESS;
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		return response;
	}

}
