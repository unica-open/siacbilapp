/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.capitoli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.capitoli.RisultatiRicercaCapitoloEntrataPrevisioneAjaxModel;

/**
 * Action per i risultati di ricerca del capitoli di entrata previsione .
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 06/07/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataPrevisioneModaleAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoCapitolo,
		RisultatiRicercaCapitoloEntrataPrevisioneAjaxModel, CapitoloEntrataPrevisione, RicercaSinteticaCapitoloEntrataPrevisione, RicercaSinteticaCapitoloEntrataPrevisioneResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataPrevisioneModaleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_ENTRATA_PREVISIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_ENTRATA_PREVISIONE);
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
	protected ElementoCapitolo ottieniIstanza(CapitoloEntrataPrevisione e) throws FrontEndBusinessException {
		return ElementoCapitoloFactory.getInstance(e, false, model.isGestioneUEB());
	}

	@Override
	protected RicercaSinteticaCapitoloEntrataPrevisioneResponse ottieniResponse(RicercaSinteticaCapitoloEntrataPrevisione request) {
		return capitoloEntrataPrevisioneService.ricercaSinteticaCapitoloEntrataPrevisione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataPrevisione> ottieniListaRisultati(RicercaSinteticaCapitoloEntrataPrevisioneResponse response) {
 
		return response.getCapitoli();
	}
	
	@Override
	protected int getElementiPerPaginaDefault() {
		return 5;
	}
}