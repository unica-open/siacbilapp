/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.attivitaiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.attivitaiva.RegistroIvaAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe per la ricerca del RegistroIva via Ajax.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 12/06/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RegistroIvaAjaxAction extends GenericBilancioAction<RegistroIvaAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8471975686613317108L;
	
	@Autowired private transient RegistroIvaService registroIvaService;

	@Override
	public String execute() {
		final String methodName = "execute";
		// Effettuo una validazione dei dati
		validateExecute();
		if(hasErrori()) {
			log.info(methodName, "Errore di validazione nella ricerca");
			return SUCCESS;
		}
		// Ricerco le attivita' iva
		RicercaRegistroIva request = model.creaRequestRicercaRegistroIva();
		logServiceRequest(request);
		RicercaRegistroIvaResponse response = registroIvaService.ricercaRegistroIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaRegistroIva");
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "RegistriIva trovati: " + response.getCardinalitaComplessiva());
		model.setListaRegistroIva(response.getListaRegistroIva());
		
		// Imposto la lista in sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_REGISTRO_IVA, response.getListaRegistroIva());
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca dell'AttivitaIva siano validi.
	 */
	private void validateExecute() {
		RegistroIva ri = model.getRegistroIva();
		checkCondition(ri != null && ri.getTipoRegistroIva() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo registro iva"));
	}
	
}
