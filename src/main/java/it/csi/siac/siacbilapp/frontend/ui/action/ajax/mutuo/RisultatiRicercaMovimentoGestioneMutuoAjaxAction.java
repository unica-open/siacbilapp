/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaMovimentoGestioneMutuoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoMovimentoGestione;
import it.csi.siac.siacbilser.frontend.webservice.MutuoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaMovimentiGestioneMutuoResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class RisultatiRicercaMovimentoGestioneMutuoAjaxAction<
	MG extends MovimentoGestione, 
	RMGMREQ extends RicercaMovimentiGestioneMutuo, 
	RMGMRES extends RicercaMovimentiGestioneMutuoResponse<MG>> 
	extends PagedDataTableAjaxAction<
		ElementoMovimentoGestione, 
		RisultatiRicercaMovimentoGestioneMutuoAjaxModel, 
		MG, 
		RMGMREQ, 
		RMGMRES> {


	private static final long serialVersionUID = -6446846084535980423L;

	
	@Autowired protected MutuoService mutuoService;
	
	
	public RisultatiRicercaMovimentoGestioneMutuoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_GESTIONE_MUTUO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_GESTIONE_MUTUO);
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
	protected ParametriPaginazione ottieniParametriDiPaginazione(RMGMREQ request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RMGMREQ request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	
}
