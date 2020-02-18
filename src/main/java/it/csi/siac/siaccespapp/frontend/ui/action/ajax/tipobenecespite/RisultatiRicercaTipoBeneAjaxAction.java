/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.tipobenecespite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.tipobenecespite.RisultatiRicercaTipoBeneAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.tipobenecespite.ElementoTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RisultatiRicercaTipoBeneAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoBeneAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoTipoBeneCespite, RisultatiRicercaTipoBeneAjaxModel, TipoBeneCespite, RicercaSinteticaTipoBeneCespite, RicercaSinteticaTipoBeneCespiteResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 1322968983653310422L;
	
	private static final String AZIONI_CONSENTITE_BEGIN =  new StringBuilder()
			.append("<div class='btn-group'> ")
			.append("<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>")
			.append("<ul class='dropdown-menu pull-right' >").toString();

	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a href='risultatiRicercaTipoBene_aggiorna.do?uidTipoBeneCespite=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = "<li><a class='eliminaTipoBene' data-toggle='modal'>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a class='annullaTipoBene' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a href='risultatiRicercaTipoBene_consulta.do?uidTipoBeneCespite=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	@Autowired private transient ClassificazioneCespiteService classificazioneCespiteService;
	
	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaTipoBeneAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TIPO_BENE_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TIPO_BENE_CESPITE);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaTipoBeneCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaTipoBeneCespite req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoTipoBeneCespite ottieniIstanza(TipoBeneCespite e) throws FrontEndBusinessException {
		return new ElementoTipoBeneCespite(e);
	}

	@Override
	protected RicercaSinteticaTipoBeneCespiteResponse ottieniResponse(RicercaSinteticaTipoBeneCespite req) {
		return classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);
	}

	@Override
	protected ListaPaginata<TipoBeneCespite> ottieniListaRisultati(RicercaSinteticaTipoBeneCespiteResponse response) {
		return response.getListaTipoBeneCespite();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoTipoBeneCespite instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		boolean aggiornaConsentito = AzioniConsentiteFactory.isConsentito(AzioniConsentite.TIPO_BENE_CESPITE_AGGIORNA, listaAzioniConsentite);
		StringBuilder azioniBuilder = new StringBuilder()
		    .append(AZIONI_CONSENTITE_BEGIN)
		    .append(aggiornaConsentito ? AZIONI_CONSENTITE_AGGIORNA : "")
		    .append(AZIONI_CONSENTITE_ELIMINA)
		    .append(aggiornaConsentito && !instance.isTipoBeneAnnullato() ? AZIONI_CONSENTITE_ANNULLA : "" )
		    .append(AZIONI_CONSENTITE_CONSULTA)
		    .append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}

}
