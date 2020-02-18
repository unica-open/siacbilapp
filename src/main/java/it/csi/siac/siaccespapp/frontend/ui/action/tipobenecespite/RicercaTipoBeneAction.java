/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.tipobenecespite;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite.RicercaTipoBeneModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaTipoBeneAction extends GenericTipoBeneAction<RicercaTipoBeneModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 2080613373812405659L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaCategoria();
		caricaCodifiche();
		caricaListaTitoli();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setTipoBeneCespite(null);
		return SUCCESS;		
	}
	
	/**
	 * Carica categoria.
	 *
	 * @throws WebServiceInvocationFailureException the web service invocation failure exception
	 */
	private void caricaCategoria() throws WebServiceInvocationFailureException {
		List<CategoriaCespiti> listaCategoriaCespiti = sessionHandler.getParametro(BilSessionParameter.LISTA_CATEGORIA_CESPITI);
	
		if(listaCategoriaCespiti == null) {
			RicercaSinteticaCategoriaCespiti req = model.creaRequestRicercaSinteticaCategoriaCespiti(Boolean.FALSE);
			RicercaSinteticaCategoriaCespitiResponse res = classificazioneCespiteService.ricercaSinteticaCategoriaCespiti(req);
			if(res.hasErrori()) {
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			listaCategoriaCespiti = res.getListaCategoriaCespiti();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CATEGORIA_CESPITI, listaCategoriaCespiti);
		}
		
		model.setListaCategoriaCespiti(listaCategoriaCespiti);
	}

	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateEffettuaRicerca() {
		TipoBeneCespite tipoBeneCespite = model.getTipoBeneCespite();
		boolean indicatoUnCriterioDiRicerca = tipoBeneCespite != null &&
				(StringUtils.isNotBlank(tipoBeneCespite.getCodice()) ||
				StringUtils.isNotBlank(tipoBeneCespite.getDescrizione()) ||
				model.idEntitaPresente(tipoBeneCespite.getCategoriaCespiti()) ||
				(
					// Conto patrimoniale
						tipoBeneCespite.getContoPatrimoniale() != null
						&& StringUtils.isNotBlank((tipoBeneCespite.getContoPatrimoniale().getCodice()))
				));
		
		checkCondition(indicatoUnCriterioDiRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
	/**
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String effettuaRicerca(){
		final String methodName = "effettuaRicerca";
		RicercaSinteticaTipoBeneCespite request = model.creaRequestRicercaSinteticaTipoBene();
		RicercaSinteticaTipoBeneCespiteResponse response = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(request);
		if(response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(request, response);
		
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaTipoBeneCespite request, RicercaSinteticaTipoBeneCespiteResponse response) {
				
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_TIPO_BENE_CESPITE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_BENE_CESPITE, response.getListaTipoBeneCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
}
