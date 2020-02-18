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
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.attodilegge.AggiornaAttoDiLeggeModel;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per la gestione dell'aggiornamento dell'Atto di Legge.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaAttoDiLeggeAction extends GenericBilancioAction<AggiornaAttoDiLeggeModel> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -2505964309521588912L;
	
	@Autowired private transient AttoDiLeggeService attoDiLeggeService;
	
	// Il metodo execute non è mai richiamato, in quanto la Action viene invocata via AJAX
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Aggiorna l'Atto di Legge.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		AggiornaAttoDiLegge request = model.creaRequestAggiornaAttoDiLegge();
		logServiceRequest(request);
		
		// Invocazione del servizio
		log.debug(methodName, "Richiamo il WebService di inserimento");
		AggiornaAttoDiLeggeResponse response = attoDiLeggeService.aggiornaAttoDiLegge(request);
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
	
	@Override
	public void validate() {
		AttoDiLegge attoDiLegge = model.getAttoDiLegge();
		
		checkNotNull(attoDiLegge, "Atto di legge", true);
		checkNotNull(attoDiLegge.getAnno(), "Anno");
		checkNotNull(attoDiLegge.getNumero(), "Numero");
		checkNotNullNorInvalidUid(attoDiLegge.getTipoAtto(), "Tipo Atto");
	}
	
}
