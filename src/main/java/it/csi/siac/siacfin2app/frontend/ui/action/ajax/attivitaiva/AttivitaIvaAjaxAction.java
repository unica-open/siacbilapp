/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.attivitaiva;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva.AttivitaIvaAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

/**
 * Classe per la ricerca delle AttivitaIva via Ajax.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 05/06/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AttivitaIvaAjaxAction extends GenericBilancioAction<AttivitaIvaAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8471975686613317108L;
	
	@Autowired private transient DocumentoIvaService documentoIvaService;

	@Override
	public String execute() {
		final String methodName = "execute";
		// Effettuo una validazione dei dati
		validateExecute();
		if(hasErrori()) {
			log.info(methodName, "Errore di validazione nella ricerca");
			return SUCCESS;
		}
		// Ricerco le attività iva
		RicercaAttivitaIva request = model.creaRequestRicercaAttivitaIva();
		logServiceRequest(request);
		RicercaAttivitaIvaResponse response = documentoIvaService.ricercaAttivitaIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaAttivitaIva");
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "AttivitaIva trovate: " + response.getCardinalitaComplessiva());
		model.setListaAttivitaIva(response.getListaAttivitaIva());
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca dell'AttivitaIva siano validi.
	 */
	private void validateExecute() {
		AttivitaIva ai = model.getAttivitaIva();
		checkCondition(StringUtils.isNotBlank(ai.getCodice()) || StringUtils.isNotBlank(ai.getDescrizione()), ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
}
