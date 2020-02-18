/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.risultatiricerca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaCapitoloMassivoAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.WrapperAzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca massiva del capitolo di uscita previsione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaPrevisioneMassivoAjaxAction extends GenericRisultatiRicercaCapitoloMassivoAjaxAction<CapitoloUscitaPrevisione, 
	RicercaSinteticaMassivaCapitoloUscitaPrevisione, RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1795774409224122411L;
	
	@Autowired
	private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaPrevisioneMassivoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_MASSIVA_CAPITOLO);
		setNomeAzione(WrapperAzioniConsentite.ACTION_NAME_USCITA_PREVISIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMassivaCapitoloUscitaPrevisione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMassivaCapitoloUscitaPrevisione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse ottieniResponse(RicercaSinteticaMassivaCapitoloUscitaPrevisione request) {
		return capitoloUscitaPrevisioneService.ricercaSinteticaMassivaCapitoloUscitaPrevisione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaPrevisione> ottieniListaRisultati(RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse response) {
		return response.getCapitoli();
	}

	@Override
	protected void impostaLaPaginaRemote(RicercaSinteticaMassivaCapitoloUscitaPrevisione request) {
		request.setPaginaRemote(0);
	}

}
