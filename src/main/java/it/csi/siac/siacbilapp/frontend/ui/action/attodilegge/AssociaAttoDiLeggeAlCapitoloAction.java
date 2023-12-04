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

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.attodilegge.AssociaAttoDiLeggeAlCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.attodilegge.ElementoRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.attodilegge.ElementoRelazioneAttoDiLeggeCapitoloFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteWrapper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per l'associazione dell'Atto di Legge al Capitolo.
 * 
 * @author Marchino Alessandro, Gallo Luciano
 * @version 1.0.0 - 11/09/2013
 * 
 * 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AssociaAttoDiLeggeAlCapitoloAction extends GenericBilancioAction<AssociaAttoDiLeggeAlCapitoloModel> {
	
	/**
	 * Per la serializzazione 
	 */
	private static final long serialVersionUID = 7113718223228921671L;
		
	@Autowired private transient CapitoloService capitoloService;

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\"> "+
			"<button type=\"button\" class=\"btn dropdown-toggle\" data-placement=\"left\" data-toggle=\"dropdown\">Azioni <span class=\"caret\"></span></button>" +
			"<ul class=\"dropdown-menu pull-right\">";
	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a href=\"#\" data-aggiorna>aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a href=\"#\" data-annulla data-target=\"#divAnnullaRelazione\" data-toggle=\"modal\">annulla</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
	
	//task-65
	public boolean gerarchiPresente(String gerarchia) {
		RicercaRelazioneAttoDiLeggeCapitoloResponse response = null;
		RicercaRelazioneAttoDiLeggeCapitolo request = model.creaRequestRicercaRelazioneAttoDiLeggeCapitolo();
		logServiceRequest(request);
		response = capitoloService.ricercaRelazioneAttoDiLeggeCapitolo(request);
		logServiceResponse(response);
		
		if(response.getElencoAttiLeggeCapitolo().size()>0) {
			for(int i=0; i<response.getElencoAttiLeggeCapitolo().size();i++) {
				if(response.getElencoAttiLeggeCapitolo().get(i).getGerarchia().equals(model.getGerarchia())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Inserisce la relazione tra Atto di Legge e Capitolo.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisci() {
		final String methodName = "inserisci";
		boolean gerarchiaPresente = gerarchiPresente(model.getGerarchia());
		
		InserisceRelazioneAttoDiLeggeCapitoloResponse response = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Validazione 
		validation();
		
		if (hasErrori()) {
			return SUCCESS;
		}
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		InserisceRelazioneAttoDiLeggeCapitolo request = model.creaRequestInserisciRelazioneAttoDiLeggeCapitolo();
		logServiceRequest(request);
		
		// Invocazione del servizio
		// Workaround per evitare problemi in caso di richiamo del servizio
		try {
			//task-65
			if(gerarchiaPresente == false) {
				log.debug(methodName, "Richiamo il WebService di inserimento relazione atto di legge - capitolo");
				response = capitoloService.inserisceRelazioneAttoDiLeggeCapitolo(request);
				log.debug(methodName, "Richiamato il WebService di inserimento relazione atto di legge - capitolo");
				logServiceResponse(response);
			}else {
				//task-65
				response.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Gerarchia gia' presente"));
			}
			
		} catch(Exception e) {
			Errore err = new Errore();
			log.error(methodName, e, e);
			//task-65
			if(gerarchiaPresente == true) {
				err.setCodice("");
				err.setDescrizione("Si è verificato un errore nell'inserimento della relazione atto di legge - capito. Gerarchia gia' presente");
			} else {
				err.setCodice("");
				err.setDescrizione("Si è verificato un errore nella chiamata del Servizio di inserimento relazione atto di legge - capitolo");
			}
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
			log.debug(methodName, "Relazione Atto di Legge - Capitolo inserita correttamente");
			addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		}
		return SUCCESS;
	}
	
	/**
	 * Ricerca la relazione tra Atto di Legge e Capitolo.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		RicercaRelazioneAttoDiLeggeCapitoloResponse response = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		RicercaRelazioneAttoDiLeggeCapitolo request = model.creaRequestRicercaRelazioneAttoDiLeggeCapitolo();
		logServiceRequest(request);
		
		// Invocazione del servizio
		// Workaround per evitare problemi in caso di richiamo del servizio
		try {
			log.debug(methodName, "Richiamo il WebService di ricerca relazione atto di legge - capitolo");
			response = capitoloService.ricercaRelazioneAttoDiLeggeCapitolo(request);
			log.debug(methodName, "Richiamato il WebService di ricerca relazione atto di legge - capitolo");
			logServiceResponse(response);
		} catch(Exception e) {
			log.error(methodName, e, e);
			Errore err = new Errore();
			
			err.setCodice("");
			err.setDescrizione("Si è verificato un errore nella chiamata del Servizio di ricerca relazione atto di legge - capitolo");
			listaErrori.add(err);
			addErrori(listaErrori);
			return SUCCESS;
		}

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response.getErrori());
			return SUCCESS;
		}
		log.debug(methodName, "Relazione Atto di Legge - Capitolo ricercata correttamente");
		
		List<AttoDiLeggeCapitolo> listaRisultati = response.getElencoAttiLeggeCapitolo();
		List<ElementoRelazioneAttoDiLeggeCapitolo> aaData = new ArrayList<ElementoRelazioneAttoDiLeggeCapitolo>();
		
		model.setAaData(aaData);
		// Non ho risultati. Esco subito prima di fare qualsiasi altro calcolo
		if(listaRisultati == null) {
			return SUCCESS;
		}
		
		// Valuta la lista delle azioni consentite per
		// costruire il pannello di operazioni utente sul capitolo 
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		AzioniConsentiteWrapper wpAzioniConsentite = new AzioniConsentiteWrapper("RelazioneAttoCapitolo", listaAzioniConsentite);
		
		boolean isAggiornaAbilitato = wpAzioniConsentite.isAggiornaAbilitato();
		boolean isAnnullaAbilitato = wpAzioniConsentite.isAnnullaAbilitato();

		for (AttoDiLeggeCapitolo item : listaRisultati) {
			// Esclude le relazioni non VALIDE
			if (item.getStato() != StatoEntita.VALIDO) {
				// Se non è valido, lo escludo
				continue;
			}
				
			ElementoRelazioneAttoDiLeggeCapitolo el = ElementoRelazioneAttoDiLeggeCapitoloFactory.getInstance(item);
			StringBuilder strBuilderAzioni = new StringBuilder();
			String strAzioni = "";
			if (isAnnullaAbilitato || isAggiornaAbilitato) {
				strBuilderAzioni.append(AZIONI_CONSENTITE_BEGIN);
				if (isAggiornaAbilitato) {
					strBuilderAzioni.append(AZIONI_CONSENTITE_AGGIORNA);
				}

				if (isAnnullaAbilitato ) {
					strBuilderAzioni.append(AZIONI_CONSENTITE_ANNULLA);
				}

				strBuilderAzioni.append(AZIONI_CONSENTITE_END);
				
				String sourceParams = new StringBuilder(Integer.toString(el.getUid()))
						.append(", ")
						.append(Integer.toString(item.getAttoDiLegge().getUid()))
						.toString();
				
				log.debug(methodName, sourceParams);
				
				strAzioni = strBuilderAzioni.toString()
						.replaceAll("%source.params%", sourceParams);
			}
			el.setAzioni(strAzioni);
			aaData.add(el);
		}
		model.setAaData(aaData);
		
		return SUCCESS;

	}

	/**
	 * Ricerca PUNTUALE per idAttoDiLeggeCapitolo la relazione tra Atto di Legge e Capitolo.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaPuntuale() {
		final String methodName = "ricercaPuntuale";
		RicercaRelazioneAttoDiLeggeCapitoloResponse response = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		RicercaRelazioneAttoDiLeggeCapitolo request = model.creaRequestRicercaPuntualeRelazioneAttoDiLeggeCapitolo();
		logServiceRequest(request);
		
		// Invocazione del servizio
		// Workaround per evitare problemi in caso di richiamo del servizio
		try {
			log.debug(methodName, "Richiamo il WebService di ricerca puntuale relazione atto di legge - capitolo");
			response = capitoloService.ricercaPuntualeRelazioneAttoDiLeggeCapitolo(request);
			log.debug(methodName, "Richiamato il WebService di ricerca puntuale relazione atto di legge - capitolo");
			logServiceResponse(response);
		} catch(Exception e) {
			log.error(methodName, e, e);
			Errore err = new Errore();
			
			err.setCodice("");
			err.setDescrizione("Si è verificato un errore nella chiamata del Servizio di ricerca puntuale relazione atto di legge - capitolo");
			listaErrori.add(err);
			addErrori(listaErrori);
			return SUCCESS;
		}

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");

			addErrori(response.getErrori());
			return SUCCESS;
		}
		log.debug(methodName, "Relazione Atto di Legge  - Capitolo ricercata correttamente");
		
		List<AttoDiLeggeCapitolo> listaRisultati = response.getElencoAttiLeggeCapitolo();
		List<ElementoRelazioneAttoDiLeggeCapitolo> aaData = new ArrayList<ElementoRelazioneAttoDiLeggeCapitolo>();
		
		// Valuta la lista delle azioni consentite per
		// costruire il pannello di operazioni utente sul capitolo 
		
		if (listaRisultati != null && !listaRisultati.isEmpty()) {
			AttoDiLeggeCapitolo item = listaRisultati.get(0);
			ElementoRelazioneAttoDiLeggeCapitolo el = ElementoRelazioneAttoDiLeggeCapitoloFactory.getInstance(item);
			aaData.add(el);
		}
		model.setAaData(aaData);
		
		return SUCCESS;

	}
	/**
	 * Cancella la relazione tra Atto di Legge e Capitolo.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String cancella() {
		final String methodName = "cancella";
		CancellaRelazioneAttoDiLeggeCapitoloResponse response = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		CancellaRelazioneAttoDiLeggeCapitolo request = model.creaRequestCancellaRelazioneAttoDiLeggeCapitolo();
		logServiceRequest(request);
		
		// Invocazione del servizio
		// Workaround per evitare problemi in caso di richiamo del servizio
		try {
			log.debug(methodName, "Richiamo il WebService di cancellazione relazione atto di legge - capitolo");
			response = capitoloService.cancellaRelazioneAttoDiLeggeCapitolo(request);
			log.debug(methodName, "Richiamato il WebService di cancellazione relazione atto di legge - capitolo");
			logServiceResponse(response);
		} catch(Exception e) {
			log.error(methodName, e, e);
			Errore err = new Errore();
			
			err.setCodice("");
			err.setDescrizione("Si è verificato un errore nella chiamata del Servizio di cancellazione relazione atto di legge - capitolo");
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
			log.debug(methodName, "Relazione Atto di Legge - Capitolo cancellata correttamente");
			addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		}
		return SUCCESS;
	}
	
	/**
	 * Aggiorna la relazione tra Atto di Legge e Capitolo.
	 * 
	 * @return la Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		boolean gerarchiaPresente = gerarchiPresente(model.getGerarchia());
		
		AggiornaRelazioneAttoDiLeggeCapitoloResponse response = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Validazione 
		validation();
		
		if (hasErrori()) {
			return SUCCESS;
		}
		
		// Creazione della request
		log.debug(methodName, "Creazione della request");
		AggiornaRelazioneAttoDiLeggeCapitolo request = model.creaRequestAggiornaRelazioneAttoDiLeggeCapitolo();
		logServiceRequest(request);
		
		// Invocazione del servizio
		// Workaround per evitare problemi in caso di richiamo del servizio
		try {
			//task-65
			if(gerarchiaPresente == false ) {
				log.debug(methodName, "Richiamo il WebService di aggiornamento relazione atto di legge - capitolo");
				response = capitoloService.aggiornaRelazioneAttoDiLeggeCapitolo(request);
				log.debug(methodName, "Richiamato il WebService di aggiornamento relazione atto di legge - capitolo");
				logServiceResponse(response);
			}else {
				//task-65
				response.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("Gerarchia gia' presente"));
			}
		} catch(Exception e) {
			Errore err = new Errore();
			log.error(methodName, e, e);
			//task-65
			if(gerarchiaPresente == true) {
				err.setCodice("");
				err.setDescrizione("Si è verificato un errore nell'aggiornamento della relazione atto di legge - capito. Gerarchia gia' presente");
			} else {
				err.setCodice("");
				err.setDescrizione("Si è verificato un errore nella chiamata del Servizio di aggiornamento relazione atto di legge - capitolo");
			}
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
			log.debug(methodName, "Relazione Atto di Legge - Capitolo aggiornata correttamente");
			addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		}
		return SUCCESS;
	}
	
	/**
	 * Validazione dei campi
	 */
	private void validation() {
		// TODO: nessuna validazione
	}
	
}
