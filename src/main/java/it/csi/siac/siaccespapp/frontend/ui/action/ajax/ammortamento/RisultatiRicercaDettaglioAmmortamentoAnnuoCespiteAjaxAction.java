/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.ammortamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.ammortamento.RisultatiRicercaDettaglioAmmortamentoAnnuoCespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.ammortamento.ElementoDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RisultatiRicercaDismissioneAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDettaglioAmmortamentoAnnuoCespiteAjaxAction extends PagedDataTableAjaxAction<ElementoDettaglioAmmortamentoAnnuoCespite, RisultatiRicercaDettaglioAmmortamentoAnnuoCespiteAjaxModel, DettaglioAmmortamentoAnnuoCespite, RicercaSinteticaDettaglioAmmortamentoAnnuoCespite, RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -8961798784400008872L;
	
	@Autowired private transient CespiteService cespiteService;
	
	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaDettaglioAmmortamentoAnnuoCespiteAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_AMMORTAMENTO);
		
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DETTAGLIO_AMMORTAMENTO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaDettaglioAmmortamentoAnnuoCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaDettaglioAmmortamentoAnnuoCespite req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoDettaglioAmmortamentoAnnuoCespite getInstance(DettaglioAmmortamentoAnnuoCespite e) throws FrontEndBusinessException {
		return new ElementoDettaglioAmmortamentoAnnuoCespite(e);
	}

	@Override
	protected RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse getResponse(RicercaSinteticaDettaglioAmmortamentoAnnuoCespite req) {
		return cespiteService.ricercaSinteticaDettaglioAmmortamentoAnnuoCespite(req);
	}

	@Override
	protected ListaPaginata<DettaglioAmmortamentoAnnuoCespite> ottieniListaRisultati(RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse response) {
		return response.getListaDettaglioAmmortamentoAnnuoCespite();
	}
	
}
