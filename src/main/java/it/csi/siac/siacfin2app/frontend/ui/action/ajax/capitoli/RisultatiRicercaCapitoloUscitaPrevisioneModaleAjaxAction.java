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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.capitoli.RisultatiRicercaCapitoloUscitaPrevisioneAjaxModel;

/**
 * Action per i risultati di ricerca del capitoli di uscita gestione .
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 06/07/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaPrevisioneModaleAjaxAction extends PagedDataTableAjaxAction<ElementoCapitolo,
		RisultatiRicercaCapitoloUscitaPrevisioneAjaxModel, CapitoloUscitaPrevisione, RicercaSinteticaCapitoloUscitaPrevisione, RicercaSinteticaCapitoloUscitaPrevisioneResponse> {
		
	/** Per la serializzazione */

	private static final long serialVersionUID = -8236501490334809585L;

	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaPrevisioneModaleAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_USCITA_PREVISIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_USCITA_PREVISIONE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaCapitoloUscitaPrevisione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaCapitoloUscitaPrevisione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
		
	}

	@Override
	protected ElementoCapitolo getInstance(CapitoloUscitaPrevisione e) throws FrontEndBusinessException {
		return ElementoCapitoloFactory.getInstance(e, false, model.isGestioneUEB());
	}

	@Override
	protected RicercaSinteticaCapitoloUscitaPrevisioneResponse getResponse(RicercaSinteticaCapitoloUscitaPrevisione request) {
		return capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaPrevisione> ottieniListaRisultati(RicercaSinteticaCapitoloUscitaPrevisioneResponse response) {
 
		return response.getCapitoli();
	}

	@Override
	protected int getElementiPerPaginaDefault() {
		return 5;
	}
}