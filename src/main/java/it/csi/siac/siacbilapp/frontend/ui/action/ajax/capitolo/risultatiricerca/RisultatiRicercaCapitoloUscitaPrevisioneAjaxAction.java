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
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.WrapperAzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del capitolo di uscita previsione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaPrevisioneAjaxAction extends GenericRisultatiRicercaCapitoloAjaxAction<CapitoloUscitaPrevisione, 
	RicercaSinteticaCapitoloUscitaPrevisione, RicercaSinteticaCapitoloUscitaPrevisioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5150191041574257131L;
	
	@Autowired
	private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaPrevisioneAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO);
		setNomeAzione(WrapperAzioniConsentite.ACTION_NAME_USCITA_PREVISIONE);
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
	protected RicercaSinteticaCapitoloUscitaPrevisioneResponse ottieniResponse(RicercaSinteticaCapitoloUscitaPrevisione request) {
		return capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaPrevisione> ottieniListaRisultati(RicercaSinteticaCapitoloUscitaPrevisioneResponse response) {
		return response.getCapitoli();
	}

}
