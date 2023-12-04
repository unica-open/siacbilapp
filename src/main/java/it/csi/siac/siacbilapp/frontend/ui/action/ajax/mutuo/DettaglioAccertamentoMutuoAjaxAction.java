/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.DettaglioAccertamentoMutuoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoAccertamento;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoAccertamentoFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamentoResponse;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioAccertamentoMutuoAjaxAction 
	extends DettaglioMovimentoGestioneMutuoAjaxAction<
		DettaglioAccertamentoMutuoAjaxModel, 
		Accertamento, 
		RicercaDettaglioAccertamento, 
		RicercaDettaglioAccertamentoResponse> {

	private static final long serialVersionUID = -3194239561518709415L;

	@Override
	protected RicercaDettaglioAccertamentoResponse getResponse(RicercaDettaglioAccertamento req) {
		return movimentoGestioneBilService.ricercaDettaglioAccertamento(req);
	}

	@Override
	protected List<Accertamento> getResponseList(RicercaDettaglioAccertamentoResponse res) {
		return res.getAccertamento().getDettagliPerBilancio();
	}

	@Override
	protected ElementoAccertamento getInstance(Accertamento e) throws FrontEndBusinessException {
		return ElementoAccertamentoFactory.getInstanceDettaglio(e);
	}

}
