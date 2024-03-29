/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.risultatiricerca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaCapitoloAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del capitolo di entrata previsione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataPrevisioneAjaxAction extends GenericRisultatiRicercaCapitoloAjaxAction<CapitoloEntrataPrevisione, 
	RicercaSinteticaCapitoloEntrataPrevisione, RicercaSinteticaCapitoloEntrataPrevisioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 145630873560758166L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataPrevisioneAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO);
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_ENTRATA_PREVISIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCapitoloEntrataPrevisione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCapitoloEntrataPrevisione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaCapitoloEntrataPrevisioneResponse getResponse(RicercaSinteticaCapitoloEntrataPrevisione request) {
		return capitoloEntrataPrevisioneService.ricercaSinteticaCapitoloEntrataPrevisione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataPrevisione> ottieniListaRisultati(RicercaSinteticaCapitoloEntrataPrevisioneResponse response) {
		return response.getCapitoli();
	}

}
