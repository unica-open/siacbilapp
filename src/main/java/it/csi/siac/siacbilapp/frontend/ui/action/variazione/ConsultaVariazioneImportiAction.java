/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;

/**
 * Classe di Action per la gestione della consultazione della Variazione Importi.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 * @author Elisa Chiari
 * @version 2.0.0 28/02/2017
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaVariazioneImportiAction extends ConsultaVariazioneImportiBaseAction {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -6547183084829531739L;


	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}
	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
	
		return super.execute();
	}
	
	/**
	 * Ricerca componenti importo capitolo in variazione.
	 *
	 * @return the string
	 */
	public String ricercaComponentiImportoCapitoloInVariazione() {
		RicercaDettaglioVariazioneComponenteImportoCapitolo req = model.creaRequestRicercaDettaglioVariazioneComponenteImportoCapitolo();
		RicercaDettaglioVariazioneComponenteImportoCapitoloResponse response = variazioneDiBilancioService.ricercaDettaglioVariazioneComponenteImportoCapitolo(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		List<ElementoComponenteVariazione> componentiDelCapitolo = ElementoComponenteVariazioneFactory.getInstancesFromListeDettagliSui3Anni(response.getListaDettaglioVariazioneComponenteImportoCapitolo(),  response.getListaDettaglioVariazioneComponenteImportoCapitolo1(), response.getListaDettaglioVariazioneComponenteImportoCapitolo2(), model.getApplicazione());
		model.setComponentiCapitolo(componentiDelCapitolo);
		return SUCCESS;
	}
	
}
