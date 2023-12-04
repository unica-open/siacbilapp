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
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.ajax.RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxModel;
import it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.RicercaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per la gestione dei risultati di ricerca dei fondi accantonamento dubbia esigibilita
 * @author Elisa Chiari
 * @version 1.0.0 - 02/11/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
// FIXME: SIAC-7858 - da eliminare?
@Deprecated
public class RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxAction extends PagedDataTableAjaxAction<AccantonamentoFondiDubbiaEsigibilita, RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxModel, AccantonamentoFondiDubbiaEsigibilita, RicercaAccantonamentoFondiDubbiaEsigibilita, RicercaAccantonamentoFondiDubbiaEsigibilitaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3720115772675193497L;
	@Autowired private transient FondiDubbiaEsigibilitaService fondiDubbiaEsigibilitaService;
	
	/**
	 * Costruttore
	 */
	public RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxAction(){
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaAccantonamentoFondiDubbiaEsigibilita request) {
		return new ParametriPaginazione();
//		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaAccantonamentoFondiDubbiaEsigibilita request, ParametriPaginazione parametriPaginazione) {
//		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilita getInstance(AccantonamentoFondiDubbiaEsigibilita e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaAccantonamentoFondiDubbiaEsigibilitaResponse getResponse(RicercaAccantonamentoFondiDubbiaEsigibilita request) {
		return fondiDubbiaEsigibilitaService.ricercaAccantonamentoFondiDubbiaEsigibilita(request);
	}

	@Override
	protected ListaPaginata<AccantonamentoFondiDubbiaEsigibilita> ottieniListaRisultati(RicercaAccantonamentoFondiDubbiaEsigibilitaResponse response) {
		return new ListaPaginataImpl<AccantonamentoFondiDubbiaEsigibilita>(response.extractByType(AccantonamentoFondiDubbiaEsigibilita.class));
	}

}
