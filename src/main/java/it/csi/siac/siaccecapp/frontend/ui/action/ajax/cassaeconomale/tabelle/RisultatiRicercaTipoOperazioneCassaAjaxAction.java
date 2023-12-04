/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.ajax.cassaeconomale.tabelle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.tabelle.RisultatiRicercaTipoOperazioneCassaAjaxModel;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di action per i risultati di ricerca dei tipi di operazione di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoOperazioneCassaAjaxAction extends PagedDataTableAjaxAction<TipoOperazioneCassa, 
	RisultatiRicercaTipoOperazioneCassaAjaxModel, TipoOperazioneCassa, RicercaSinteticaTipoOperazioneDiCassa, RicercaSinteticaTipoOperazioneDiCassaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6244090919129582326L;
	
	@Autowired private transient CassaEconomaleService cassaEconomaleService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoOperazioneCassaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TIPO_OPERAZIONE_DI_CASSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TIPO_OPERAZIONE_DI_CASSA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaTipoOperazioneDiCassa request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaTipoOperazioneDiCassa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected TipoOperazioneCassa getInstance(TipoOperazioneCassa e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaTipoOperazioneDiCassaResponse getResponse(RicercaSinteticaTipoOperazioneDiCassa request) {
		return cassaEconomaleService.ricercaSinteticaTipoOperazioneDiCassa(request);
	}

	@Override
	protected ListaPaginata<TipoOperazioneCassa> ottieniListaRisultati(RicercaSinteticaTipoOperazioneDiCassaResponse response) {
		return response.getTipiOperazione();
	}

}
