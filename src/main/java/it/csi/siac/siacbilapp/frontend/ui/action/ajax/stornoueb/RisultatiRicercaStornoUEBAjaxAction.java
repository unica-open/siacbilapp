/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.stornoueb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaStornoUEBAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.stornoueb.ElementoStornoUEB;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.stornoueb.ElementoStornoUEBFactory;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEB;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEBResponse;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca dello storno UEB.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 19/09/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStornoUEBAjaxAction extends PagedDataTableAjaxAction<ElementoStornoUEB, 
	RisultatiRicercaStornoUEBAjaxModel, StornoUEB, RicercaStornoUEB, RicercaStornoUEBResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3043483825545339124L;

	/* Servizio */
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaStornoUEBAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_STORNO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_STORNO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaStornoUEB request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaStornoUEB request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoStornoUEB getInstance(StornoUEB e) throws FrontEndBusinessException {
		return ElementoStornoUEBFactory.getInstance(e);
	}

	@Override
	protected RicercaStornoUEBResponse getResponse(RicercaStornoUEB request) {
		return variazioneDiBilancioService.ricercaStornoUEB(request);
	}

	@Override
	protected ListaPaginata<StornoUEB> ottieniListaRisultati(RicercaStornoUEBResponse response) {
		return response.getStorniUEB();
	}

}
