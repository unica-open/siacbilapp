/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.attodilegge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.attodilegge.InserisciAttoDiLeggeModel;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per la gestione dell'inserimento dell'Atto di Legge.
 * 
 * @author Marchino Alessandro, Gallo Luciano
 * 
 * @version 1.0.0 - 11/09/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciAttoDiLeggeAction extends GenericBilancioAction<InserisciAttoDiLeggeModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -460282765131089113L;
	
	@Autowired private transient AttoDiLeggeService attoDiLeggeService;

	/**
	 * Inserisce l'Atto di Legge.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciCDU() {
		final String methodName = "inserisciCDU";
		
		// Validazione dei dati da inserire
		validation();
		
		if (hasErrori()) {
			return SUCCESS;
		}
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		InserisceAttoDiLegge request = model.creaRequestInserisceAttoDiLegge();
		logServiceRequest(request);
		
		// Invocazione del servizio
		log.debug(methodName, "Richiamo il WebService di inserimento");
		InserisceAttoDiLeggeResponse response = attoDiLeggeService.inserisceAttoDiLegge(request);
		log.debug(methodName, "Richiamato il WebService di inserimento");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response.getErrori());
		} else {
			log.debug(methodName, "Atto di Legge inserito correttamente");
			addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		}
		
		return SUCCESS;
	}
	
	/**
	 * Validazione dei dati.
	 */
	public void validation() {
		AttoDiLegge attoDiLegge = model.getAttoDiLegge();
		
		checkNotNull(attoDiLegge, "Atto di legge", true);
		checkNotNull(attoDiLegge.getAnno(), "Anno");
		checkNotNull(attoDiLegge.getNumero(), "Numero");
		checkNotNullNorInvalidUid(attoDiLegge.getTipoAtto(), "Tipo Atto");
	}
	
}
