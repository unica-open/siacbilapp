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
import it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.tabelle.RisultatiRicercaOperazioneCassaAjaxModel;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di action per i risultati di ricerca dei tipi di giustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaOperazioneCassaAjaxAction extends PagedDataTableAjaxAction<OperazioneCassa,
	RisultatiRicercaOperazioneCassaAjaxModel, OperazioneCassa, RicercaSinteticaOperazioneDiCassa, RicercaSinteticaOperazioneDiCassaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6244090919129582326L;
	
	@Autowired private transient CassaEconomaleService cassaEconomaleService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaOperazioneCassaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_OPERAZIONE_CASSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_OPERAZIONE_CASSA);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaOperazioneDiCassa request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaOperazioneDiCassa request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected OperazioneCassa getInstance(OperazioneCassa e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaOperazioneDiCassaResponse getResponse(RicercaSinteticaOperazioneDiCassa request) {
		return cassaEconomaleService.ricercaSinteticaOperazioneDiCassa(request);
	}

	@Override
	protected ListaPaginata<OperazioneCassa> ottieniListaRisultati(RicercaSinteticaOperazioneDiCassaResponse response) {
		return response.getOperazioniCassa();
	}

}
