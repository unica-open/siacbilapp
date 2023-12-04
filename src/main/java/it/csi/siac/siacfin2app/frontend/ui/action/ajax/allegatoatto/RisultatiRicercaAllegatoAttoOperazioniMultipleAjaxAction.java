/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaAllegatoAttoOperazioniMultipleAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAllegatoAttoOperazioniMultipleAjaxAction extends PagedDataTableAjaxAction<ElementoAllegatoAtto, 
		RisultatiRicercaAllegatoAttoOperazioniMultipleAjaxModel, AllegatoAtto, RicercaAllegatoAtto, RicercaAllegatoAttoResponse> {
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = -6433910410480294671L;
	
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoOperazioniMultipleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ALLEGATO_ATTO_MULT);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO_MULT);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaAllegatoAtto request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaAllegatoAtto request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoAllegatoAtto getInstance(AllegatoAtto e) throws FrontEndBusinessException {
		return new ElementoAllegatoAtto(e);
	}
	
	@Override
	protected RicercaAllegatoAttoResponse getResponse(RicercaAllegatoAtto request) {
		return allegatoAttoService.ricercaAllegatoAtto(request);
	}
	
	@Override
	protected ListaPaginata<AllegatoAtto> ottieniListaRisultati(RicercaAllegatoAttoResponse response) {
		return response.getAllegatoAtto();
	}
}