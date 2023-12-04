/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.RisultatiRicercaPianoDeiContiAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di action per i risultati di ricerca dei conti figlio.
 * 
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaContoAjaxAction extends PagedDataTableAjaxAction<Conto,
RisultatiRicercaPianoDeiContiAjaxModel, Conto, RicercaSinteticaConto, RicercaSinteticaContoResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6244090919129582326L;
	
	@Autowired private transient ContoService contoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaContoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CONTO_COMP);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CONTO_COMP);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaConto request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaConto request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected Conto getInstance(Conto e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaContoResponse getResponse(RicercaSinteticaConto request) {
		return contoService.ricercaSinteticaConto(request);
	}

	@Override
	protected ListaPaginata<Conto> ottieniListaRisultati(RicercaSinteticaContoResponse response) {
		return response.getConti();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		Boolean daRientro = sessionHandler.getParametro(BilSessionParameter.RIENTRO);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return Boolean.TRUE.equals(daRientro);
	}

}
