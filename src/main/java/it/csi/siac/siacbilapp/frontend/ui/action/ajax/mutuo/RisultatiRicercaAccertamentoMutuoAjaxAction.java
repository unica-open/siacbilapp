/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoAccertamento;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoAccertamentoFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuoResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAccertamentoMutuoAjaxAction 
	extends RisultatiRicercaMovimentoGestioneMutuoAjaxAction<Accertamento, RicercaAccertamentiAssociabiliMutuo, RicercaAccertamentiAssociabiliMutuoResponse> {

	private static final long serialVersionUID = -3194239561518709415L;

	@Override
	protected RicercaAccertamentiAssociabiliMutuoResponse getResponse(RicercaAccertamentiAssociabiliMutuo req) {
		return mutuoService.ricercaAccertamentiAssociabiliMutuo(req);
	}

	@Override
	protected ListaPaginata<Accertamento> ottieniListaRisultati(RicercaAccertamentiAssociabiliMutuoResponse res) {
		return res.getAccertamenti();
	}

	@Override
	protected ElementoAccertamento getInstance(Accertamento e) throws FrontEndBusinessException {
		return ElementoAccertamentoFactory.getInstance(e);
	}

}
