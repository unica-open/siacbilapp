/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;

/**
 * Classe di Action per la gestione della consultazione della Variazione Importi.
 * 
 * @author Daniele Argiolas
 * @author Elisa Chiari
 * @version 1.0.0 05/11/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class DefinisceVariazioneImportiAction extends DefinisceVariazioneImportiBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	
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
