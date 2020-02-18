/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilapp.frontend.ui.action.ajax.variazione;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RisultatiRicercaVariazioniAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Action per i risultati di ricerca delle Variazioni importi
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 03/11/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaVariazioniImportiAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoVariazione, 
	RisultatiRicercaVariazioniAjaxModel, VariazioneImportoCapitolo, RicercaVariazioneBilancio, RicercaVariazioneBilancioResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3043483825545339124L;

	/* Servizio */
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaVariazioniImportiAjaxAction() {
		super();
		
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_VARIAZIONI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_VARIAZIONI);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaVariazioneBilancio request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaVariazioneBilancio request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoVariazione ottieniIstanza(VariazioneImportoCapitolo e) throws FrontEndBusinessException {
		return ElementoVariazioneFactory.getInstance(e,model.getEnte().getGestioneLivelli());
	}

	@Override
	protected RicercaVariazioneBilancioResponse ottieniResponse(RicercaVariazioneBilancio request) {
		return variazioneDiBilancioService.ricercaVariazioneBilancio(request);
	}

	@Override
	protected ListaPaginata<VariazioneImportoCapitolo> ottieniListaRisultati(RicercaVariazioneBilancioResponse response) {
		return response.getVariazioniDiBilancio();
	}

}

