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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del capitolo di uscita gestione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaGestioneAjaxAction extends GenericRisultatiRicercaCapitoloAjaxAction<CapitoloUscitaGestione, 
	RicercaSinteticaCapitoloUscitaGestione, RicercaSinteticaCapitoloUscitaGestioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 536370268562100192L;
	
	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaGestioneAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO);
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_USCITA_GESTIONE);
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
	protected RicercaSinteticaCapitoloUscitaGestioneResponse getResponse(RicercaSinteticaCapitoloUscitaGestione request) {
		return capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaGestione> ottieniListaRisultati(RicercaSinteticaCapitoloUscitaGestioneResponse response) {
		return response.getCapitoli();
	}

}
