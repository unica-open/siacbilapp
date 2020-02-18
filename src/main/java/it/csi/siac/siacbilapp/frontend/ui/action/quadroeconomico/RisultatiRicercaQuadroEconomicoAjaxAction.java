/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.quadroeconomico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.quadroeconomico.RisultatiRicercaQuadroEconomicoAjaxModel;
import it.csi.siac.siacbilser.frontend.webservice.QuadroEconomicoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.quadroeconomico.ElementoQuadroEconomico;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Elisa Chiari 
 * @version 1.0.0 - 04/01/2018
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaQuadroEconomicoAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoQuadroEconomico, 
RisultatiRicercaQuadroEconomicoAjaxModel, QuadroEconomico, RicercaSinteticaQuadroEconomico, RicercaSinteticaQuadroEconomicoResponse> {
	    
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = -9158819714906330183L;
		//Services
		@Autowired private transient QuadroEconomicoService quadroEconomicoService;
	/** Costruttore vuoto di default */
	public RisultatiRicercaQuadroEconomicoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_QUADRO_ECONOMICO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_QUADRO_ECONOMICO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaQuadroEconomico request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaQuadroEconomico request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoQuadroEconomico ottieniIstanza(QuadroEconomico e) throws FrontEndBusinessException {
		return new ElementoQuadroEconomico(e);
	}
	
	@Override
	protected RicercaSinteticaQuadroEconomicoResponse ottieniResponse(RicercaSinteticaQuadroEconomico request) {
		RicercaSinteticaQuadroEconomicoResponse ris = quadroEconomicoService.ricercaSinteticaQuadroEconomico(request);
		return  ris;
	}
	
	@Override
	protected ListaPaginata<QuadroEconomico> ottieniListaRisultati(RicercaSinteticaQuadroEconomicoResponse response) {
		return response.getListQuadroEconomico();
	}
}