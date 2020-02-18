/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.attodilegge;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.attodilegge.RicercaAttoDiLeggeModel;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per la ricerca dell'Atto di Legge.
 * 
 * @author Marchino Alessandro, Gallo Luciano
 * @version 1.0.0 - 11/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaAttoDiLeggeAction extends GenericBilancioAction<RicercaAttoDiLeggeModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6857593243472960479L;
	
	@Autowired private transient AttoDiLeggeService attoDiLeggeService;

	/**
	 * Ricerca l'Atto di Legge.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaCDU() {
		final String methodName = "ricercaCDU";
		
		// Validazione dei dati da inserire
		validationRicerca();
		
		if (hasErrori()) {
			return SUCCESS;
		}
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		RicercaAttoDiLegge request = model.creaRequestRicercaAttoDiLegge();
		logServiceRequest(request);
		
		// Invocazione del servizio
		log.debug(methodName, "Richiamo il WebService di ricerca");
		RicercaAttoDiLeggeResponse response = attoDiLeggeService.ricercaAttoDiLegge(request);
		log.debug(methodName, "Richiamato il WebService di ricerca");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response.getErrori());
			return SUCCESS;
		}
		if(response.getAttiTrovati() == null || response.getAttiTrovati().isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato dalla ricerca");
			return SUCCESS;
		}
		List<AttoDiLegge> listaAtti = new ArrayList<AttoDiLegge>();
		for (AttoDiLegge atto : response.getAttiTrovati()) {
			if (atto.getStato().equals(StatoEntita.VALIDO)) {
				listaAtti.add(atto);
			}
		}
		
		model.setListaAttoDiLegge(listaAtti);
		
		return SUCCESS;
	}

	/**
	 * Ricerca Puntuale per uid Atto di Legge.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaPuntuale() {
		final String methodName = "ricercaPuntuale";
		
	
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		RicercaAttoDiLegge request = model.creaRequestRicercaAttoDiLegge();
		logServiceRequest(request);
		
		// Invocazione del servizio
		log.debug(methodName, "Richiamo il WebService di ricerca puntuale");
		RicercaAttoDiLeggeResponse response = attoDiLeggeService.ricercaPuntualeAttoDiLegge(request);
		log.debug(methodName, "Richiamato il WebService di ricerca puntuale");
		logServiceResponse(response);
		
		if(response.getAttiTrovati() == null || response.getAttiTrovati().isEmpty()) {
			log.debug(methodName, "Nessun risultato trovato dalla ricerca");
			return SUCCESS;
		}
		List<AttoDiLegge> listaAtti = new ArrayList<AttoDiLegge>();
		for (AttoDiLegge atto : response.getAttiTrovati()) {
			if (atto.getStato().equals(StatoEntita.VALIDO)) {
				listaAtti.add(atto);
			}
		}
		
		model.setListaAttoDiLegge(listaAtti);
		
		return SUCCESS;
	}
	/**
	 * Cancella l'Atto di Legge.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String cancellaCDU() {
		final String methodName = "cancellaCDU";
		CancellaAttoDiLeggeResponse response = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		CancellaAttoDiLegge request = model.creaRequestCancellaAttoDiLegge();
		logServiceRequest(request);
		
		// Invocazione del servizio
		// Workaround per evitare problemi in caso di richiamo del servizio
		try {
			log.debug(methodName, "Richiamo il WebService di cancellazione");
			response = attoDiLeggeService.cancellaAttoDiLegge(request);
			log.debug(methodName, "Richiamato il WebService di cancellazione");
			logServiceResponse(response);
		} catch(Exception e) {
			log.error(methodName, e, e);
			Errore err = new Errore();
			
			err.setCodice("");
			err.setDescrizione("Si è verificato un errore nella chiamata del Servizio di annullamento");
			listaErrori.add(err);
			addErrori(listaErrori);
			return SUCCESS;
		}

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response.getErrori());
		} else {
			log.debug(methodName, "Atto di Legge anullato correttamente");
			addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		}
		
		return SUCCESS;
	}

	/**
	 * Validazione della ricerca.
	 */
	public void validationRicerca() {
		checkNotNull(model.getAnnoLegge(), "Anno");
		checkNotNull(model.getNumeroLegge(), "Numero");
		checkNotNullNorInvalidUid(model.getTipoAtto(), "Tipo Atto");
	}
	
}
