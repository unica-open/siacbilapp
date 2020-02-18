/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.categoriacespiti;

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
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.categoriacespiti.RisultatiRicercaCategoriaCespitiAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.categoriacespiti.ElementoCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RisultatiRicercaCategoriaCespitiAjaxAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCategoriaCespitiAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoCategoriaCespiti, RisultatiRicercaCategoriaCespitiAjaxModel, CategoriaCespiti, RicercaSinteticaCategoriaCespiti, RicercaSinteticaCategoriaCespitiResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 5748224382213088260L;
	
	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN =  new StringBuilder()
			.append("<div class='btn-group'> ")
			.append("<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>")
			.append("<ul class='dropdown-menu pull-right' >").toString();

	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a href='risultatiRicercaCategoriaCespiti_aggiorna.do?uidCategoriaCespiti=%UID%'>aggiorna</a></li>";
	
	private static final String AZIONI_CONSENTITE_ELIMINA = "<li><a class='eliminaCategoria' data-toggle='modal'>elimina</a></li>";
	
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a class='annullaCategoria' data-toggle='modal'>annulla</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a href='risultatiRicercaCategoriaCespiti_consulta.do?uidCategoriaCespiti=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	/** Serviz&icirc; della categoria */
	@Autowired protected transient ClassificazioneCespiteService classificazioneCespiteService;
	/** Costruttore vuoto di default */
	public RisultatiRicercaCategoriaCespitiAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CATEGORIA_CESPITI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CATEGORIA_CESPITI);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCategoriaCespiti req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCategoriaCespiti req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoCategoriaCespiti ottieniIstanza(CategoriaCespiti e) throws FrontEndBusinessException {
		return new ElementoCategoriaCespiti(e);
	}

	@Override
	protected RicercaSinteticaCategoriaCespitiResponse ottieniResponse(RicercaSinteticaCategoriaCespiti req) {
		return classificazioneCespiteService.ricercaSinteticaCategoriaCespiti(req);
	}

	@Override
	protected ListaPaginata<CategoriaCespiti> ottieniListaRisultati(RicercaSinteticaCategoriaCespitiResponse response) {
		return response.getListaCategoriaCespiti();
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoCategoriaCespiti instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		boolean aggiornaConsentito = AzioniConsentiteFactory.isConsentito(AzioniConsentite.CATEGORIA_CESPITI_AGGIORNA, listaAzioniConsentite);
		StringBuilder azioniBuilder = new StringBuilder()
		    .append(AZIONI_CONSENTITE_BEGIN)
		    .append(aggiornaConsentito ? AZIONI_CONSENTITE_AGGIORNA : "")
		    .append(AZIONI_CONSENTITE_ELIMINA)
		    .append(aggiornaConsentito && !instance.isAnnullato()? AZIONI_CONSENTITE_ANNULLA : "")
		    .append(AZIONI_CONSENTITE_CONSULTA)
		    .append(AZIONI_CONSENTITE_END);
		String azioni = azioniBuilder.toString().replaceAll("%UID%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}

}
