/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.dismissionecespite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.dismissionecespite.RisultatiRicercaDismissioneCespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.dismissionecespite.ElementoDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.StatoDismissioneCespite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * The Class RisultatiRicercaDismissioneAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDismissioneCespiteAjaxAction extends PagedDataTableAjaxAction<ElementoDismissioneCespite, RisultatiRicercaDismissioneCespiteAjaxModel, DismissioneCespite, RicercaSinteticaDismissioneCespite, RicercaSinteticaDismissioneCespiteResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 1322968983653310422L;
	
	private static final String AZIONI_CONSENTITE_BEGIN =  new StringBuilder()
			.append("<div class='btn-group'> ")
			.append("<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>")
			.append("<ul class='dropdown-menu pull-right' >").toString();

	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a href='risultatiRicercaDismissioneCespite_aggiorna.do?uidDismissioneCespite=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = "<li><a class='eliminaDismissioneCespite' data-toggle='modal'>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a href='risultatiRicercaDismissioneCespite_consulta.do?uidDismissioneCespite=%UID%'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_EFFETTUA_SCRITTURE = "<li><a class='effettuaScritture' data-toggle='modal'>effettua scritture</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	
	@Autowired private transient CespiteService cespiteService;
	
	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaDismissioneCespiteAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DISMISSIONE_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DISMISSIONE_CESPITE);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaDismissioneCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaDismissioneCespite req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoDismissioneCespite getInstance(DismissioneCespite e) throws FrontEndBusinessException {
		return new ElementoDismissioneCespite(e);
	}

	@Override
	protected RicercaSinteticaDismissioneCespiteResponse getResponse(RicercaSinteticaDismissioneCespite req) {
		return cespiteService.ricercaSinteticaDismissioneCespite(req);
	}

	@Override
	protected ListaPaginata<DismissioneCespite> ottieniListaRisultati(RicercaSinteticaDismissioneCespiteResponse response) {
		return response.getListaDismissioneCespite();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoDismissioneCespite instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		boolean aggiornaConsentito = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DISMISSIONE_CESPITE_AGGIORNA, listaAzioniConsentite);
		boolean dismissioneDefinitiva = instance.getStatoDismissioneCespite() != null && instance.getStatoDismissioneCespite().equals(StatoDismissioneCespite.DEFINITIVO);
		StringBuilder azioniBuilder = new StringBuilder()
				.append(AZIONI_CONSENTITE_BEGIN)
				.append(aggiornaConsentito ? AZIONI_CONSENTITE_AGGIORNA : "")
				.append(!dismissioneDefinitiva? AZIONI_CONSENTITE_ELIMINA : "")
				.append(AZIONI_CONSENTITE_CONSULTA)
				.append(aggiornaConsentito && !dismissioneDefinitiva? AZIONI_CONSENTITE_EFFETTUA_SCRITTURE : "")
				.append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}

}
