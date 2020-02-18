/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.variazionecespite;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.variazionecespite.RisultatiRicercaVariazioneCespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.variazionecespite.ElementoVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RisultatiRicercaVariazioneCespiteAjaxAction.
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 */
public abstract class BaseRisultatiRicercaVariazioneCespiteAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoVariazioneCespite, RisultatiRicercaVariazioneCespiteAjaxModel, VariazioneCespite,
		RicercaSinteticaVariazioneCespite, RicercaSinteticaVariazioneCespiteResponse> {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3261678354276967255L;
	@Autowired private transient CespiteService cespiteService;
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaVariazioneCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaVariazioneCespite req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoVariazioneCespite ottieniIstanza(VariazioneCespite e) throws FrontEndBusinessException {
		return new ElementoVariazioneCespite(e);
	}

	@Override
	protected RicercaSinteticaVariazioneCespiteResponse ottieniResponse(RicercaSinteticaVariazioneCespite req) {
		return cespiteService.ricercaSinteticaVariazioneCespite(req);
	}

	@Override
	protected ListaPaginata<VariazioneCespite> ottieniListaRisultati(RicercaSinteticaVariazioneCespiteResponse response) {
		return response.getListaVariazioneCespite();
	}

}
