/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.previmpacc;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.previmpacc.ConsultaPrevisioneImpegnatoCapUGModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestioneResponse;


/**
 * The Class RisultatiRicercaPrevisioneImpegnatoAccertatoBaseAction.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 * @param <MODEL> the generic type
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaPrevisioneImpegnatoCapUGAction extends ConsultaPrevisioneImpegnatoAccertatoBaseAction<ConsultaPrevisioneImpegnatoCapUGModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8135095427954473966L;
	
	@Autowired private CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		caricaImporti();
		caricaDettaglioCapitolo();
		return SUCCESS;
	}


	private void caricaDettaglioCapitolo() {
		RicercaDettaglioModulareCapitoloUscitaGestione req = model.creaRequestRicercaDettaglioModulareCapitoloUscitaGestione();
		RicercaDettaglioModulareCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaDettaglioModulareCapitoloUscitaGestione(req);
		if(res.hasErrori()) {
			res.addErrori(res.getErrori());
			return;
		}
		model.setCapitolo(res.getCapitoloUscitaGestione());
		model.setElementoPianoDeiConti(model.getCapitolo().getElementoPianoDeiConti());
		model.setStrutturaAmministrativoContabile(model.getCapitolo().getStrutturaAmministrativoContabile());
	}
	

}
