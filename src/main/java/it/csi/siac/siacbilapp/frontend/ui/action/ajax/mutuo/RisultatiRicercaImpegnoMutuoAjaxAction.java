/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoImpegno;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoImpegnoFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuoResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.model.Impegno;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaImpegnoMutuoAjaxAction 
	extends RisultatiRicercaMovimentoGestioneMutuoAjaxAction<Impegno, RicercaImpegniAssociabiliMutuo, RicercaImpegniAssociabiliMutuoResponse> {

	private static final long serialVersionUID = -3194239561518709415L;

	@Override
	protected RicercaImpegniAssociabiliMutuoResponse getResponse(RicercaImpegniAssociabiliMutuo req) {
		return mutuoService.ricercaImpegniAssociabiliMutuo(req);
	}

	@Override
	protected ListaPaginata<Impegno> ottieniListaRisultati(RicercaImpegniAssociabiliMutuoResponse res) {
		return res.getImpegni();
	}

	@Override
	protected ElementoImpegno getInstance(Impegno e) throws FrontEndBusinessException {
		return ElementoImpegnoFactory.getInstance(e);
	}

}
