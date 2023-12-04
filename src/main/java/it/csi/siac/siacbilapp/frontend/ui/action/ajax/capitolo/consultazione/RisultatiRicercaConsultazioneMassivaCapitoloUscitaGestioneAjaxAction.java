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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;

/**
 * Action per i risultati di ricerca del capitolo di uscita gestione, per la
 * consultazione massiva.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaConsultazioneMassivaCapitoloUscitaGestioneAjaxAction extends
		GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction<CapitoloUscitaGestione, CapitoloMassivaUscitaGestione, 
			RicercaDettaglioMassivaCapitoloUscitaGestione, RicercaDettaglioMassivaCapitoloUscitaGestioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4097417781852051178L;

	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaConsultazioneMassivaCapitoloUscitaGestioneAjaxAction() {
		super();
		setNomeAzione(AzioniConsentiteWrapper.ACTION_NAME_USCITA_GESTIONE);
	}

	@Override
	protected RicercaDettaglioMassivaCapitoloUscitaGestioneResponse ottieniResponse(RicercaDettaglioMassivaCapitoloUscitaGestione request) {
		return capitoloUscitaGestioneService.ricercaDettaglioMassivaCapitoloUscitaGestione(request);
	}

	@Override
	protected CapitoloMassivaUscitaGestione ottieniCapitoloMassivo(RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response) {
		return response.getCapitoloMassivaUscitaGestione();
	}

	@Override
	protected List<CapitoloUscitaGestione> ottieniListaRisultatiDaMassivo(CapitoloMassivaUscitaGestione capitoloMassivo) {
		return capitoloMassivo.getElencoCapitoli();
	}

}