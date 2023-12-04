/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.consultazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataPrevisione;

/**
 * Action per i risultati di ricerca del capitolo di entrata previsione, per la
 * consultazione massiva.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaConsultazioneMassivaCapitoloEntrataPrevisioneAjaxAction extends
		GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction<CapitoloEntrataPrevisione, CapitoloMassivaEntrataPrevisione, 
			RicercaDettaglioMassivaCapitoloEntrataPrevisione, RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4097417781852051178L;

	@Autowired
	private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaConsultazioneMassivaCapitoloEntrataPrevisioneAjaxAction() {
		super();
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_ENTRATA_PREVISIONE);
	}

	@Override
	protected RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse ottieniResponse(RicercaDettaglioMassivaCapitoloEntrataPrevisione request) {
		return capitoloEntrataPrevisioneService.ricercaDettaglioMassivaCapitoloEntrataPrevisione(request);
	}

	@Override
	protected CapitoloMassivaEntrataPrevisione ottieniCapitoloMassivo(RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse response) {
		return response.getCapitoloMassivaEntrataPrevisione();
	}

	@Override
	protected List<CapitoloEntrataPrevisione> ottieniListaRisultatiDaMassivo(CapitoloMassivaEntrataPrevisione capitoloMassivo) {
		return capitoloMassivo.getElencoCapitoli();
	}

}