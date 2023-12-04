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
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca massiva del capitolo di uscita gestione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloUscitaGestioneMassivoAjaxAction extends GenericRisultatiRicercaCapitoloMassivoAjaxAction<CapitoloUscitaGestione, 
RicercaSinteticaMassivaCapitoloUscitaGestione, RicercaSinteticaMassivaCapitoloUscitaGestioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -9207160604674920639L;
	
	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloUscitaGestioneMassivoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_MASSIVA_CAPITOLO);
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_USCITA_GESTIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMassivaCapitoloUscitaGestione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMassivaCapitoloUscitaGestione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaMassivaCapitoloUscitaGestioneResponse getResponse(RicercaSinteticaMassivaCapitoloUscitaGestione request) {
		return capitoloUscitaGestioneService.ricercaSinteticaMassivaCapitoloUscitaGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloUscitaGestione> ottieniListaRisultati(RicercaSinteticaMassivaCapitoloUscitaGestioneResponse response) {
		return response.getCapitoli();
	}

	@Override
	protected void impostaLaPaginaRemote(RicercaSinteticaMassivaCapitoloUscitaGestione request) {
		request.setPaginaRemote(0);
	}

}
