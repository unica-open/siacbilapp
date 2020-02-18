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
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigli;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigliResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di action per i risultati di ricerca dei conti figlio.
 * 
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPianoDeiContiAjaxAction extends GenericRisultatiRicercaAjaxAction<Conto,
RisultatiRicercaPianoDeiContiAjaxModel, Conto, RicercaSinteticaContoFigli, RicercaSinteticaContoFigliResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6244090919129582326L;
	
	@Autowired private transient ContoService contoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPianoDeiContiAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_FIGLI_CONTO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CONTO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaContoFigli request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaContoFigli request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected Conto ottieniIstanza(Conto e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaContoFigliResponse ottieniResponse(RicercaSinteticaContoFigli request) {
		return contoService.ricercaSinteticaContoFigli(request);
	}

	@Override
	protected ListaPaginata<Conto> ottieniListaRisultati(RicercaSinteticaContoFigliResponse response) {
		return response.getContiFiglio();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		Boolean daRientro = sessionHandler.getParametro(BilSessionParameter.RIENTRO);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return Boolean.TRUE.equals(daRientro);
	}

}
