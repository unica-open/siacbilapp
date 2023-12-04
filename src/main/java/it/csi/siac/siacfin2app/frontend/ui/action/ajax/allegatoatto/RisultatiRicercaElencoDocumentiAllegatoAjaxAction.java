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
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaElencoDocumentiAllegatoAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Action per i risultati di ricerca dell'ElencoDocumentiAllegato da emettere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 13/11/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaElencoDocumentiAllegatoAjaxAction extends PagedDataTableAjaxAction<ElencoDocumentiAllegato, 
		RisultatiRicercaElencoDocumentiAllegatoAjaxModel, ElencoDocumentiAllegato, RicercaElenco, RicercaElencoResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaElencoDocumentiAllegatoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaElenco request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaElenco request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElencoDocumentiAllegato getInstance(ElencoDocumentiAllegato e) throws FrontEndBusinessException {
		return e;
	}
	
	@Override
	protected RicercaElencoResponse getResponse(RicercaElenco request) {
		return allegatoAttoService.ricercaElenco(request);
	}
	
	@Override
	protected ListaPaginata<ElencoDocumentiAllegato> ottieniListaRisultati(RicercaElencoResponse response) {
		return response.getElenchiDocumentiAllegato();
	}
	
}