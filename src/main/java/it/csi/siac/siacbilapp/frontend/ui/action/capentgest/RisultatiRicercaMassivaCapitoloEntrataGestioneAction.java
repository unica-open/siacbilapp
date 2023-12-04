/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.RisultatiRicercaMassivaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;

/**
 * Action per i risultati di ricerca massiva del capitolo Entrata Gestione
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 23/09/2013 
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaMassivaCapitoloEntrataGestioneAction extends GenericBilancioAction<RisultatiRicercaMassivaCapitoloEntrataGestioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3236632095547354103L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "startPosition = " + startPosition);
		log.debug(methodName, "AHMAD : "+sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE_IMPORTI, ImportiCapitoloEG.class));

		model.setTotaleImporti(sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE_IMPORTI, ImportiCapitoloEG.class));

		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Capitolo da aggiornare. Anno: " + model.getAnnoCapitoloDaAggiornare() + "; numero capitolo: " + model.getNumeroCapitoloDaAggiornare() +
				"; numero articolo: " + model.getNumeroArticoloDaAggiornare());
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Capitolo da consultare. Anno: " + model.getAnnoCapitoloDaConsultare() + "; numero capitolo: " + model.getNumeroCapitoloDaConsultare() +
				"; numero articolo: " + model.getNumeroArticoloDaConsultare());
		return SUCCESS;
	}

}
