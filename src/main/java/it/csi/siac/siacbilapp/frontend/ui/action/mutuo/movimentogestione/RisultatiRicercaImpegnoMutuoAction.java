/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.movimentogestione;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.movimentogestione.RisultatiRicercaImpegnoMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuoResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.model.Impegno;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaImpegnoMutuoAction extends RisultatiRicercaMovimentoGestioneMutuoAction<RisultatiRicercaImpegnoMutuoModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2745593032590721433L;

	@SuppressWarnings("unchecked")
	@Override
	protected ListaPaginata<Impegno> ricercaMovimentiGestione() throws WebServiceInvocationFailureException {
		RicercaImpegniAssociabiliMutuoResponse res = 
				mutuoService.ricercaImpegniAssociabiliMutuo(sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_GESTIONE_MUTUO, RicercaImpegniAssociabiliMutuo.class));
		logServiceResponse(res);
	
		if (res.hasErrori()) {
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaImpegniAssociabiliMutuo.class, res));
		}

		return res.getImpegni();
	}
}
