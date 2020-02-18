/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conciliazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitoloResponse;
import it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione.RicercaContiByConciliazionePerTitoloModel;

/**
 * Classe di action per la ricerca dei conti collegati alla conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/11/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaContiByConciliazionePerTitoloAction extends GenericBilancioAction<RicercaContiByConciliazionePerTitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4384978181328134198L;
	
	@Autowired private transient ConciliazioneService conciliazioneService;
	
	@Override
	public String execute() throws Exception {
		RicercaContiConciliazionePerTitolo request = model.creaRequestRicercaContiConciliazionePerTitolo();
		RicercaContiConciliazionePerTitoloResponse response = conciliazioneService.ricercaContiConciliazionePerTitolo(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		model.setListaConto(response.getConti());
		return SUCCESS;
	}
	
}
