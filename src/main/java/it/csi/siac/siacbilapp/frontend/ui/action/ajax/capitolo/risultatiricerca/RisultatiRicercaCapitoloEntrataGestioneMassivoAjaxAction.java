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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca massiva del capitolo di entrata gestione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataGestioneMassivoAjaxAction extends GenericRisultatiRicercaCapitoloMassivoAjaxAction<CapitoloEntrataGestione, 
	RicercaSinteticaMassivaCapitoloEntrataGestione, RicercaSinteticaMassivaCapitoloEntrataGestioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5368123274498947651L;
	
	@Autowired
	private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataGestioneMassivoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_MASSIVA_CAPITOLO);
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_ENTRATA_GESTIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMassivaCapitoloEntrataGestione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMassivaCapitoloEntrataGestione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaMassivaCapitoloEntrataGestioneResponse getResponse(RicercaSinteticaMassivaCapitoloEntrataGestione request) {
		return capitoloEntrataGestioneService.ricercaSinteticaMassivaCapitoloEntrataGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataGestione> ottieniListaRisultati(RicercaSinteticaMassivaCapitoloEntrataGestioneResponse response) {
		return response.getCapitoli();
	}
		
	@Override
	protected void impostaLaPaginaRemote(RicercaSinteticaMassivaCapitoloEntrataGestione request) {
		request.setPaginaRemote(0);
	}
	
}
