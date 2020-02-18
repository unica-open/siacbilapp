/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.ajax.cassaeconomale.stampe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.stampe.RisultatiRicercaRendicontoCassaDaStampareAjaxModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe.ElementoMovimentoStampa;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe.ElementoMovimentoStampaRendiconto;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe.ElementoMovimentoStampaRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.StampaCassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampare;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampareResponse;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di action per la gestione AJAX dei rendiconti da stampare
 * @author Marchino Alessandro
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRendicontoCassaDaStampareAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoMovimentoStampa,
		RisultatiRicercaRendicontoCassaDaStampareAjaxModel, Movimento, RicercaSinteticaRendicontoCassaDaStampare, RicercaSinteticaRendicontoCassaDaStampareResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7595641767876418861L;
	
	@Autowired private transient StampaCassaEconomaleService stampaCassaEconomaleService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaRendicontoCassaDaStampareAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_RENDICONTO_CASSA_DA_STAMPARE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_RENDICONTO_CASSA_DA_STAMPARE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaRendicontoCassaDaStampare req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaRendicontoCassaDaStampare req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoMovimentoStampa ottieniIstanza(Movimento e) throws FrontEndBusinessException {
		return e == null
			? null
			: e.getRendicontoRichiesta() != null
				? new ElementoMovimentoStampaRendiconto(e)
				: new ElementoMovimentoStampaRichiesta(e);
	}

	@Override
	protected RicercaSinteticaRendicontoCassaDaStampareResponse ottieniResponse(RicercaSinteticaRendicontoCassaDaStampare req) {
		return stampaCassaEconomaleService.ricercaSinteticaRendicontoCassaDaStampare(req);
	}

	@Override
	protected ListaPaginata<Movimento> ottieniListaRisultati(RicercaSinteticaRendicontoCassaDaStampareResponse response) {
		return response.getListaMovimenti();
	}

}
