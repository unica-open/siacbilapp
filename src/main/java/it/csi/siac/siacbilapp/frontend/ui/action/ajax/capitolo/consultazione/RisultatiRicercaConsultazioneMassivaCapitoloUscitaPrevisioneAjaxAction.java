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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;

/**
 * Action per i risultati di ricerca del capitolo di uscita previsione, per la consultazione massiva.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaConsultazioneMassivaCapitoloUscitaPrevisioneAjaxAction 
	extends GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction<CapitoloUscitaPrevisione, CapitoloMassivaUscitaPrevisione, 
		RicercaDettaglioMassivaCapitoloUscitaPrevisione, RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4097417781852051178L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaConsultazioneMassivaCapitoloUscitaPrevisioneAjaxAction() {
		super();
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_USCITA_PREVISIONE);
	}

	@Override
	protected RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse ottieniResponse(RicercaDettaglioMassivaCapitoloUscitaPrevisione request) {
		return capitoloUscitaPrevisioneService.ricercaDettaglioMassivaCapitoloUscitaPrevisione(request);
	}

	@Override
	protected CapitoloMassivaUscitaPrevisione ottieniCapitoloMassivo(RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response) {
		return response.getCapitoloMassivaUscitaPrevisione();
	}

	@Override
	protected List<CapitoloUscitaPrevisione> ottieniListaRisultatiDaMassivo(CapitoloMassivaUscitaPrevisione capitoloMassivo) {
		return capitoloMassivo.getElencoCapitoli();
	}

}
