/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.cespite;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.cespite.RisultatiRicercaCespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite.ElementoCespite;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RisultatiRicercaCespiteAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public abstract class BaseRisultatiRicercaCespiteAjaxAction extends PagedDataTableAjaxAction<ElementoCespite, RisultatiRicercaCespiteAjaxModel, Cespite, RicercaSinteticaCespite, RicercaSinteticaCespiteResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -6721874855319781354L;
	
	@Autowired private transient CespiteService cespiteService;

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCespite req,ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoCespite getInstance(Cespite e) throws FrontEndBusinessException {
		return new ElementoCespite(e);
	}

	@Override
	protected RicercaSinteticaCespiteResponse getResponse(RicercaSinteticaCespite req) {
		return cespiteService.ricercaSinteticaCespite(req);
	}

	@Override
	protected ListaPaginata<Cespite> ottieniListaRisultati(RicercaSinteticaCespiteResponse response) {
		return response.getListaCespite();
	}
}
