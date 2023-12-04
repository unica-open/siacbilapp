/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.movimentogestione;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.movimentogestione.RisultatiRicercaAccertamentoMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuoResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAccertamentoMutuoAction extends RisultatiRicercaMovimentoGestioneMutuoAction<RisultatiRicercaAccertamentoMutuoModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2745593032590721433L;

	@SuppressWarnings("unchecked")
	@Override
	protected ListaPaginata<Accertamento> ricercaMovimentiGestione() throws WebServiceInvocationFailureException {
		RicercaAccertamentiAssociabiliMutuoResponse res = 
				mutuoService.ricercaAccertamentiAssociabiliMutuo(sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_GESTIONE_MUTUO, RicercaAccertamentiAssociabiliMutuo.class));
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaAccertamentiAssociabiliMutuo.class, res));
		}

		return res.getAccertamenti();
	}
}
