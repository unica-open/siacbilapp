/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaProgettoMutuoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgetto;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgettoFactory;
import it.csi.siac.siacbilser.frontend.webservice.MutuoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaProgettoMutuoAjaxAction 
	 extends PagedDataTableAjaxAction<ElementoProgetto, RisultatiRicercaProgettoMutuoAjaxModel, Progetto, RicercaProgettiAssociabiliMutuo, RicercaProgettiAssociabiliMutuoResponse>
	{

	private static final long serialVersionUID = -3194239561518709415L;
	@Autowired protected MutuoService mutuoService;
	
	

	@Override
	protected RicercaProgettiAssociabiliMutuoResponse getResponse(RicercaProgettiAssociabiliMutuo req) {
		return mutuoService.ricercaProgettiAssociabiliMutuo(req);
	}

	@Override
	protected ListaPaginata<Progetto> ottieniListaRisultati(RicercaProgettiAssociabiliMutuoResponse res) {
		return res.getProgetti();
	}

	@Override
	protected ElementoProgetto getInstance(Progetto e) throws FrontEndBusinessException {
		return ElementoProgettoFactory.getInstance(e);
	}
	
	
	public RisultatiRicercaProgettoMutuoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PROGETTI_MUTUO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PROGETTI_MUTUO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaProgettiAssociabiliMutuo request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaProgettiAssociabiliMutuo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}	
	
	
	

}
