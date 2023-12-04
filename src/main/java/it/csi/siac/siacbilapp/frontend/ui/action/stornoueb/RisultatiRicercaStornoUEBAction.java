/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.stornoueb;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.stornoueb.RisultatiRicercaStornoUEBModel;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action relativa ai risultati della ricerca per lo storno UEB.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStornoUEBAction extends GenericBilancioAction<RisultatiRicercaStornoUEBModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5105620900852446646L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		// Controllo che non vi sia una pagina salvata in sessione
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			// Imposto il numero di pagina dalla sessione
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "startPosition = " + startPosition);
		return SUCCESS;
	}
	
	/**
	 * Redirezione verso l'action di aggiornamento.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		if(model.getUidDaAggiornare() == null || model.getUidDaAggiornare().intValue() == 0) {
			log.info(methodName, "Uid non specificato");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Storno da aggiornare"));
			return INPUT;
		}
		
		log.debug(methodName, "Uid dello storno da aggiornare: " + model.getUidDaAggiornare());
		return SUCCESS;
	}
	
}
