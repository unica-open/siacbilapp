/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// ANTO punto di partenza

package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RicercaRegistrazioneMovFinBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.RicercaRegistrazioneMovFinFINModel;

/**
 * Ricerca della registrazione MovFin per l'ambito FIN (GEN standard)
 * @author Valentina
 * @author Marchino Alessandro
 * @version 1.1.0 - 05/10/2015
 * @version 1.1.1 - 21/12/2015 - JIRA-2738 - caricamento asincrono delle liste
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class RicercaRegistrazioneMovFinFINAction extends RicercaRegistrazioneMovFinBaseAction<RicercaRegistrazioneMovFinFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8308061045648954000L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}

	@Override
	public void validateRicercaRegistrazioneMovFin() {
		checkNotNullNorInvalidUid(model.getTipoEvento(), "Tipo Evento");
		checkCondition((model.getAnnoMovimento() == null && StringUtils.isBlank(model.getNumeroMovimento()))
				|| (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero movimento", ": i campi devono essere entrambi valorizzati o entrambi non valorizzati"));
		checkCondition(model.getNumeroSubmovimento() == null || model.getNumeroSubmovimento() == 0 || (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero submovimento", ": valorizzare anche anno e numero movimento"));
//		if(model.getAnnoMovimento() == null && StringUtils.isBlank(model.getNumeroMovimento())){
//			checkNotNullNorInvalidUid(model.getTipoEvento(), "Tipo Evento");
//		}
		

		validaContoFinanziario();
		checkCapitolo();
		checkSoggetto();
		checkMovimentoGestione();
		checkCondition(checkAttoAmministrativo(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
		if (hasErrori()) {
			model.impostoDatiNelModel();
		}
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_REGISTRAZIONI_MOV_FIN_GEN;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_REGISTRAZIONI_MOV_FIN_GEN;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRicerca() {
		return BilSessionParameter.RIEPILOGO_RICERCA_REGISTRAZIONE_GEN;
	}

	@Override
	protected void caricaListe() {
		caricaListaStati();
	}
	
//	/**
//	 * Caricamento della lista dei titoli.
//	 */
//	protected void caricaListaTitoliDaSessione() {
//		List<TitoloEntrata> listaTitoloEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA);
//		List<TitoloSpesa> listaTitoloSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA);
//		
//		if(listaTitoloEntrata != null) {
//			model.setListaTitoloEntrata(listaTitoloEntrata);
//		}
//		if(listaTitoloSpesa != null) {
//			model.setListaTitoloSpesa(listaTitoloSpesa);
//		}
//	}
	
	/**
	 * Caricamento della lista dei titoli.
	 * 
	 * @return uns stringa corrispondente al risultato dell'invocazione
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
