/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.capitoli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.capitoli.RisultatiRicercaCapitoloUscitaGestioneAjaxModel;

/**
 * Action per i risultati di ricerca del capitoli di uscita gestione .
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 06/07/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaGestioneModaleAjaxAction extends PagedDataTableAjaxAction<ElementoCapitolo,
		RisultatiRicercaCapitoloUscitaGestioneAjaxModel, CapitoloUscitaGestione, RicercaSinteticaCapitoloUscitaGestione, RicercaSinteticaCapitoloUscitaGestioneResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;

	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaGestioneModaleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_USCITA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_USCITA_GESTIONE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCapitoloUscitaGestione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCapitoloUscitaGestione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}

	@Override
	protected ElementoCapitolo getInstance(CapitoloUscitaGestione e) throws FrontEndBusinessException {
		return ElementoCapitoloFactory.getInstance(e, false, model.isGestioneUEB());
	}

	@Override
	protected RicercaSinteticaCapitoloUscitaGestioneResponse getResponse(RicercaSinteticaCapitoloUscitaGestione request) {
		return capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaGestione> ottieniListaRisultati(RicercaSinteticaCapitoloUscitaGestioneResponse response) {
 
		return response.getCapitoli();
	}

	@Override
	protected int getElementiPerPaginaDefault() {
		return 5;
	}
}