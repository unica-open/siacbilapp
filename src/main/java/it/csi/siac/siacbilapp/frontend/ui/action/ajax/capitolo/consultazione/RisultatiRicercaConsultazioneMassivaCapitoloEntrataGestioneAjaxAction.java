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
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.WrapperAzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataGestione;

/**
 * Action per i risultati di ricerca del capitolo di entrata gestione, per la
 * consultazione massiva.
 * 
 * @author LG, AM
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaConsultazioneMassivaCapitoloEntrataGestioneAjaxAction extends
		GenericRisultatiRicercaConsultazioneMassivaCapitoloAjaxAction<CapitoloEntrataGestione, CapitoloMassivaEntrataGestione, 
			RicercaDettaglioMassivaCapitoloEntrataGestione, RicercaDettaglioMassivaCapitoloEntrataGestioneResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4097417781852051178L;

	@Autowired
	private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	/** Costruttore vuoto di default */
	public RisultatiRicercaConsultazioneMassivaCapitoloEntrataGestioneAjaxAction() {
		super();
		setNomeAzione(WrapperAzioniConsentite.ACTION_NAME_ENTRATA_GESTIONE);
	}

	@Override
	protected RicercaDettaglioMassivaCapitoloEntrataGestioneResponse ottieniResponse(RicercaDettaglioMassivaCapitoloEntrataGestione request) {
		return capitoloEntrataGestioneService.ricercaDettaglioMassivaCapitoloEntrataGestione(request);
	}

	@Override
	protected CapitoloMassivaEntrataGestione ottieniCapitoloMassivo(RicercaDettaglioMassivaCapitoloEntrataGestioneResponse response) {
		return response.getCapitoloMassivaEntrataGestione();
	}

	@Override
	protected List<CapitoloEntrataGestione> ottieniListaRisultatiDaMassivo(CapitoloMassivaEntrataGestione capitoloMassivo) {
		return capitoloMassivo.getElencoCapitoli();
	}

}