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
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.ammortamento.RisultatiRicercaDettaglioAnteprimaAmmortamentoAnnuoCespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.ammortamento.ElementoDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RisultatiRicercaDismissioneAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDettaglioAnteprimaAmmortamentoAnnuoAjaxAction extends PagedDataTableAjaxAction<ElementoDettaglioAnteprimaAmmortamentoAnnuoCespite, RisultatiRicercaDettaglioAnteprimaAmmortamentoAnnuoCespiteAjaxModel, DettaglioAnteprimaAmmortamentoAnnuoCespite, RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite, RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -6038106311054796799L;
	
	@Autowired private transient CespiteService cespiteService;
	
	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaDettaglioAnteprimaAmmortamentoAnnuoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ANTEPRIMA_AMMORTAMENTO);
		
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ANTEPRIMA_AMMORTAMENTO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoDettaglioAnteprimaAmmortamentoAnnuoCespite getInstance(DettaglioAnteprimaAmmortamentoAnnuoCespite e) throws FrontEndBusinessException {
		return new ElementoDettaglioAnteprimaAmmortamentoAnnuoCespite(e);
	}

	@Override
	protected RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse getResponse(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req) {
		return cespiteService.ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(req);
	}

	@Override
	protected ListaPaginata<DettaglioAnteprimaAmmortamentoAnnuoCespite> ottieniListaRisultati(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse response) {
		return response.getListaDettaglioAnteprimaAmmortamentoAnnuoCespite();
	}
	
}
