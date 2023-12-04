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
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.DettaglioImpegnoMutuoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoImpegno;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoImpegnoFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegno;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegnoResponse;
import it.csi.siac.siacfinser.model.Impegno;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioImpegnoMutuoAjaxAction 
	extends DettaglioMovimentoGestioneMutuoAjaxAction<
		DettaglioImpegnoMutuoAjaxModel, 
		Impegno, 
		RicercaDettaglioImpegno, 
		RicercaDettaglioImpegnoResponse> {

	private static final long serialVersionUID = -3194239561518709415L;

	@Override
	protected RicercaDettaglioImpegnoResponse getResponse(RicercaDettaglioImpegno req) {
		return movimentoGestioneBilService.ricercaDettaglioImpegno(req);
	}

	@Override
	protected List<Impegno> getResponseList(RicercaDettaglioImpegnoResponse res) {
		return res.getImpegno().getDettagliPerBilancio();
	}

	@Override
	protected ElementoImpegno getInstance(Impegno e) throws FrontEndBusinessException {
		return ElementoImpegnoFactory.getInstanceDettaglio(e);
	}

}
