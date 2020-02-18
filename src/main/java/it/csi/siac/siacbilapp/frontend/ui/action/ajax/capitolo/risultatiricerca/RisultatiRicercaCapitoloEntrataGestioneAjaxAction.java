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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del capitolo di entrata gestione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataGestioneAjaxAction extends GenericRisultatiRicercaCapitoloAjaxAction<CapitoloEntrataGestione, 
	RicercaSinteticaCapitoloEntrataGestione, RicercaSinteticaCapitoloEntrataGestioneResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8342781867665138930L;
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataGestioneAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO);
		setNomeAzione(WrapperAzioniConsentite.ACTION_NAME_ENTRATA_GESTIONE);
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
	protected RicercaSinteticaCapitoloEntrataGestioneResponse ottieniResponse(RicercaSinteticaCapitoloEntrataGestione request) {
		return capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataGestione> ottieniListaRisultati(RicercaSinteticaCapitoloEntrataGestioneResponse response) {
		return response.getCapitoli();
	}

}
