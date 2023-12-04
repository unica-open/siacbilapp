/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.dubbiaesigibilita.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.ajax.RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxModel;
import it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per la gestione dei risultati di ricerca dei fondi accantonamento dubbia esigibilita
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
// FIXME: SIAC-7858 - da eliminare?
@Deprecated
public class RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxAction extends PagedDataTableAjaxAction<AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxModel, AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		RicercaAccantonamentoFondiDubbiaEsigibilita, RicercaAccantonamentoFondiDubbiaEsigibilitaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3720115772675193497L;
	@Autowired private transient FondiDubbiaEsigibilitaService fondiDubbiaEsigibilitaService;
	
	/**
	 * Costruttore vuoto di default
	 */
	public RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxAction(){
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaAccantonamentoFondiDubbiaEsigibilita request) {
//		return request.getParametriPaginazione();
		return new ParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaAccantonamentoFondiDubbiaEsigibilita request, ParametriPaginazione parametriPaginazione) {
//		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilitaRendiconto getInstance(AccantonamentoFondiDubbiaEsigibilitaRendiconto e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaAccantonamentoFondiDubbiaEsigibilitaResponse getResponse(RicercaAccantonamentoFondiDubbiaEsigibilita request) {
		return fondiDubbiaEsigibilitaService.ricercaAccantonamentoFondiDubbiaEsigibilita(request);
	}

	@Override
	protected ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaRendiconto> ottieniListaRisultati(RicercaAccantonamentoFondiDubbiaEsigibilitaResponse response) {
		return new ListaPaginataImpl<AccantonamentoFondiDubbiaEsigibilitaRendiconto>(response.extractByType(AccantonamentoFondiDubbiaEsigibilitaRendiconto.class));
	}

}
