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
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaElencoDocumentiAllegatoDaEmettereAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettereResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Action per i risultati di ricerca dell'ElencoDocumentiAllegato.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaElencoDocumentiAllegatoDaEmettereAjaxAction extends PagedDataTableAjaxAction<ElementoElencoDocumentiAllegatoDaEmettere,
  RisultatiRicercaElencoDocumentiAllegatoDaEmettereAjaxModel, ElencoDocumentiAllegato, RicercaElencoDaEmettere, RicercaElencoDaEmettereResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -4480323597572711738L;
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaElencoDocumentiAllegatoDaEmettereAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ELENCO_DOCUMENTI_ALLEGATO_DA_EMETTERE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaElencoDaEmettere request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaElencoDaEmettere request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoElencoDocumentiAllegatoDaEmettere getInstance(ElencoDocumentiAllegato e) throws FrontEndBusinessException {
		return new ElementoElencoDocumentiAllegatoDaEmettere(e);
	}
	
	@Override
	protected RicercaElencoDaEmettereResponse getResponse(RicercaElencoDaEmettere request) {
		return allegatoAttoService.ricercaElencoDaEmettere(request);
	}
	
	@Override
	protected ListaPaginata<ElencoDocumentiAllegato> ottieniListaRisultati(RicercaElencoDaEmettereResponse response) {
		return response.getElenchiDocumentiAllegato();
	}
	
}