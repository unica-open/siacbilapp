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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca massiva del capitolo di entrata previsione.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCapitoloEntrataPrevisioneMassivoAjaxAction extends GenericRisultatiRicercaCapitoloMassivoAjaxAction<CapitoloEntrataPrevisione, 
	RicercaSinteticaMassivaCapitoloEntrataPrevisione, RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8903366659432799711L;
	
	@Autowired
	private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaCapitoloEntrataPrevisioneMassivoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_MASSIVA_CAPITOLO);
		setNomeAzione(WrapperAzioniConsentite.ACTION_NAME_ENTRATA_PREVISIONE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMassivaCapitoloEntrataPrevisione request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMassivaCapitoloEntrataPrevisione request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse ottieniResponse(RicercaSinteticaMassivaCapitoloEntrataPrevisione request) {
		return capitoloEntrataPrevisioneService.ricercaSinteticaMassivaCapitoloEntrataPrevisione(request);
	}

	@Override
	protected ListaPaginata<CapitoloEntrataPrevisione> ottieniListaRisultati(RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse response) {
		return response.getCapitoli();
	}

	@Override
	protected void impostaLaPaginaRemote(RicercaSinteticaMassivaCapitoloEntrataPrevisione request) {
		request.setPaginaRemote(0);
	}

}
