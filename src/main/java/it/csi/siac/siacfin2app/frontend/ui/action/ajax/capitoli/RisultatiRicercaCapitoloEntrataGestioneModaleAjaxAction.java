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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.capitoli.RisultatiRicercaCapitoloEntrataGestioneAjaxModel;

/**
 * Action per i risultati di ricerca del capitoli di uscita gestione .
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 06/07/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataGestioneModaleAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoCapitolo,
		RisultatiRicercaCapitoloEntrataGestioneAjaxModel, CapitoloEntrataGestione, RicercaSinteticaCapitoloEntrataGestione, RicercaSinteticaCapitoloEntrataGestioneResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataGestioneModaleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_ENTRATA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_ENTRATA_GESTIONE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCapitoloEntrataGestione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCapitoloEntrataGestione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}

	@Override
	protected ElementoCapitolo ottieniIstanza(CapitoloEntrataGestione e) throws FrontEndBusinessException {
		return ElementoCapitoloFactory.getInstance(e, false, model.isGestioneUEB());
	}

	@Override
	protected RicercaSinteticaCapitoloEntrataGestioneResponse ottieniResponse(RicercaSinteticaCapitoloEntrataGestione request) {
		return capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataGestione> ottieniListaRisultati(RicercaSinteticaCapitoloEntrataGestioneResponse response) {
 
		return response.getCapitoli();
	}
	
	@Override
	protected int getElementiPerPaginaDefault() {
		return 5;
	}
	
}