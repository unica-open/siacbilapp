/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.RisultatiRicercaVariazioneModel;
import it.csi.siac.siaccorser.model.Azione;

/**
 * Action per i risultati ricerca della variazione
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaVariazioneAction extends GenericBilancioAction<RisultatiRicercaVariazioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7025148602016620930L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		Boolean tipoVariazioneCodifica = sessionHandler.getParametro(BilSessionParameter.TIPO_VARIAZIONE);
		model.setTipoVariazioneCodifica(tipoVariazioneCodifica);
	
		Azione azione = findAzione(String.format("OP-REP-ReportVariazioniBilancio-%s", sessionHandler.getAnnoEsercizio()));
		if (azione != null) {
			model.setIdAzioneReportVariazioni(azione.getUid());
		}
	}

    @Override
    @BreadCrumb("%{model.titolo}")
    public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
		    startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START))
			    .intValue();
		}
		model.setSavedDisplayStart(startPosition);
		log.debug(methodName, "startPosition = " + startPosition);
	
		return SUCCESS;
    }

    /**
     * Redirezione al metodo di consultazione codifiche.
     * 
     * @return la String corrispondente al risultato dell'invocazione
     */
    public String consultaCodifiche() {
		final String methodName = "consultaCodifiche";
		log.debug(methodName, "Numero variazione da consultare: " + model.getUidVariazioneDaConsultare());
		return SUCCESS;
    }

    /**
     * Redirezione al metodo di consultazione importi.
     * 
     * @return la String corrispondente al risultato dell'invocazione
     */
    public String consultaImporti() {
    	final String methodName = "consultaImporti";
		log.debug(methodName, "Numero variazione da consultare: " + model.getUidVariazioneDaConsultare());
		return SUCCESS;
    }

    /**
     * Redirezione al metodo di consultazione importi UEB.
     * 
     * @return la String corrispondente al risultato dell'invocazione
     */
    public String consultaImportiUEB() {
		final String methodName = "consultaImportiUEB";
		log.debug(methodName, "Numero variazione da consultare: " + model.getUidVariazioneDaConsultare());
		return SUCCESS;
    }

}
