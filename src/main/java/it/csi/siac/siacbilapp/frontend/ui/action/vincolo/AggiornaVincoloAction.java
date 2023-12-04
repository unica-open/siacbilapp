/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.vincolo;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.vincolo.AggiornaVincoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di Action per la gestione della consultazione del Vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaVincoloAction extends GenericVincoloAction<AggiornaVincoloModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepare() throws Exception {
		// Pulisco errori ed informazioni
		cleanErrori();
		cleanInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
		
		// SIAC-5076: caricamento Genere Vincolo
		caricaGenereVincolo();
		//SIAC-7129
		caricaRisorsaVincolata();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		log.debug(methodName, "Ricerco il vincolo");
		RicercaDettaglioVincolo request = model.creaRequestRicercaDettaglioVincolo();
		logServiceRequest(request);
		
		RicercaDettaglioVincoloResponse response = vincoloCapitoloService.ricercaDettaglioVincolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioVincolo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Vincolo ottenuto");
		
		List<Capitolo<?, ?>> listaCapitoliEntrata = null;
		List<Capitolo<?, ?>> listaCapitoliUscita = null;
		
		try {
			listaCapitoliEntrata = ricercaCapitoliEntrata(response);
			listaCapitoliUscita = ricercaCapitoliUscita(response);
		} catch(ApplicationException e) {
			log.error(methodName, e.getMessage());
			return INPUT;
		}
		
		// Imposto i dati nel model
		model.impostaDati(response, listaCapitoliEntrata, listaCapitoliUscita);
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaCDU()}
	 */
	public void prepareAggiornaCDU() {
		if(model.getVincolo() == null) {
			// Se non ho il vincolo, esco direttamente
			return;
		}
		// Devo pulire i campi
		model.getVincolo().setGenereVincolo(null);
	}
	
	/**
	 * Metodo per l'aggiornamento del vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		log.debug(methodName, "Aggiornamento del vincolo");
		
		AggiornaVincoloCapitolo request = model.creaRequestAggiornaVincoloCapitolo();
		logServiceRequest(request);
		
		AggiornaVincoloCapitoloResponse response = vincoloCapitoloService.aggiornaVincoloCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaVincoloCapitolo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Vincolo aggiornato");
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		// Imposto il parametro di rientro a TRUE
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per la lettura dei capitoli associati al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String leggiCapitoliAssociati() {
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'associazione di un capitolo di entrata al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaCapitoloEntrata() {
		final String methodName = "associaCapitoloEntrata";
		String result = associaCapitolo();
		log.debug(methodName, "Risultato dell'associazione: " + result);
		
		// Se ho già un errore, ritorno subito
		if(INPUT.equals(result)) {
			return SUCCESS;
		}
		
		log.debug(methodName, "Effettuo una ricerca di dettaglio per popolare le liste");
		ElementoCapitoloVincolo elementoDaAggiungere = null;
		// Effettuo una ricerca di dettaglio per il capitolo sì da popolare correttamente la lista del capitolo di entrata
		if(FaseBilancio.PREVISIONE.name().equalsIgnoreCase(model.getTipoBilancio())) {
			RicercaDettaglioCapitoloEntrataPrevisione request = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(null);
			RicercaDettaglioCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(request);
			elementoDaAggiungere = ElementoCapitoloVincoloFactory.getInstance(response.getCapitoloEntrataPrevisione(), model.isGestioneUEB());
		} else {
			RicercaDettaglioCapitoloEntrataGestione request = model.creaRequestRicercaDettaglioCapitoloEntrataGestione(null);
			RicercaDettaglioCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(request);
			elementoDaAggiungere = ElementoCapitoloVincoloFactory.getInstance(response.getCapitoloEntrataGestione(), model.isGestioneUEB());
		}
		model.getListaCapitoliEntrata().add(elementoDaAggiungere);
		model.addStanziamentoEntrata(elementoDaAggiungere);
		
		log.debug(methodName, "Capitolo aggiunto alla lista");
		
		// Cancello il capitolo dal model
		model.setCapitolo(null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'associazione di un capitolo di uscita al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaCapitoloUscita() {
		final String methodName = "associaCapitoloUscita";
		String result = associaCapitolo();
		log.debug(methodName, "Risultato dell'associazione: " + result);
		
		// Se ho già un errore, ritorno subito
		if(INPUT.equals(result)) {
			return SUCCESS;
		}
		
		log.debug(methodName, "Effettuo una ricerca di dettaglio per popolare le liste");
		ElementoCapitoloVincolo elementoDaAggiungere = null;
		// Effettuo una ricerca di dettaglio per il capitolo sì da popolare correttamente la lista del capitolo di entrata
		if(FaseBilancio.PREVISIONE.name().equalsIgnoreCase(model.getTipoBilancio())) {
			RicercaDettaglioCapitoloUscitaPrevisione request = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione(null);
			RicercaDettaglioCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(request);
			elementoDaAggiungere = ElementoCapitoloVincoloFactory.getInstance(response.getCapitoloUscitaPrevisione(), model.isGestioneUEB());
		} else {
			RicercaDettaglioCapitoloUscitaGestione request = model.creaRequestRicercaDettaglioCapitoloUscitaGestione(null);
			RicercaDettaglioCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(request);
			elementoDaAggiungere = ElementoCapitoloVincoloFactory.getInstance(response.getCapitoloUscita(), model.isGestioneUEB());
		}
		model.getListaCapitoliUscita().add(elementoDaAggiungere);
		model.addStanziamentoUscita(elementoDaAggiungere);
		
		log.debug(methodName, "Capitolo aggiunto alla lista");
		
		// Cancello il capitolo dal model
		model.setCapitolo(null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per lo scollegamento di un capitolo di entrata al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String scollegaCapitoloEntrata() {
		final String methodName = "scollegaCapitoloEntrata";
		String result = scollegaCapitolo();
		
		// In caso di errore, ritorno immediatamente
		if(INPUT.equals(result)) {
			return SUCCESS;
		}
		
		log.debug(methodName, "Elimino il capitolo dalla lista");
		ElementoCapitoloVincolo elementoCapitoloVincoloDaEliminare = new ElementoCapitoloVincolo();
		elementoCapitoloVincoloDaEliminare.setUid(model.getCapitolo().getUid());
		int indice = ComparatorUtils.getIndexByUid(model.getListaCapitoliEntrata(), elementoCapitoloVincoloDaEliminare);
		
		log.debug(methodName, "Indice trovato: " + indice);
		ElementoCapitoloVincolo elementoEliminato = model.getListaCapitoliEntrata().remove(indice);
		
		// Sottraggo gli importi
		model.subtractStanziamentoEntrata(elementoEliminato);
		
		// Cancello il capitolo dal model
		model.setCapitolo(null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per lo scollegamento di un capitolo di uscita al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String scollegaCapitoloUscita() {
		final String methodName = "scollegaCapitoloUscita";
		String result = scollegaCapitolo();
		
		// In caso di errore, ritorno immediatamente
		if(INPUT.equals(result)) {
			return SUCCESS;
		}
		
		log.debug(methodName, "Elimino il capitolo dalla lista");
		ElementoCapitoloVincolo elementoCapitoloVincoloDaEliminare = new ElementoCapitoloVincolo();
		elementoCapitoloVincoloDaEliminare.setUid(model.getCapitolo().getUid());
		int indice = ComparatorUtils.getIndexByUid(model.getListaCapitoliUscita(), elementoCapitoloVincoloDaEliminare);
		
		log.debug(methodName, "Indice trovato: " + indice);
		ElementoCapitoloVincolo elementoEliminato = model.getListaCapitoliUscita().remove(indice);
		
		// Sottraggo gli importi
		model.subtractStanziamentoUscita(elementoEliminato);
		
		// Cancello il capitolo dal model
		model.setCapitolo(null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per il controllo delle associazioni sui capitoli.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String controllaCapitoliAssociati() {
		final String methodName = "controllaCapitoliAssociati";
		
		List<ElementoCapitoloVincolo> capitoliUscita = model.getListaCapitoliUscita();
		List<ElementoCapitoloVincolo> capitoliEntrata = model.getListaCapitoliEntrata();
		log.debug(methodName, "Vi sono "+ capitoliEntrata.size() + " capitoli di entrata e " + capitoliUscita.size() + " capitoli di uscita");
		if(!capitoliEntrata.isEmpty() && capitoliUscita.isEmpty()) {
			addErrore(ErroreBil.CAPITOLO_DI_SPESA_ASSENTE.getErrore());
		}
		// Controllo della presenza di almeno un capitolo di entrata qualora vi sia un capitolo di uscita
		if(capitoliEntrata.isEmpty() && !capitoliUscita.isEmpty()) {
			addErrore(ErroreBil.CAPITOLO_DI_ENTRATA_ASSENTE.getErrore());
		}
		
		return SUCCESS;
	}
	
	/* Validazioni */
	
	/**
	 * Validazione per il metodo di aggiornamento.
	 */
	public void validateAggiornaCDU() {
		List<ElementoCapitoloVincolo> capitoliUscita = model.getListaCapitoliUscita();
		List<ElementoCapitoloVincolo> capitoliEntrata = model.getListaCapitoliEntrata();
		
		// Controllo della presenza di almeno un capitolo di uscita qualora vi sia un capitolo di entrata
		if(!capitoliEntrata.isEmpty() && capitoliUscita.isEmpty()) {
			addErrore(ErroreBil.CAPITOLO_DI_SPESA_ASSENTE.getErrore());
		}
		// Controllo della presenza di almeno un capitolo di entrata qualora vi sia un capitolo di uscita
		if(capitoliEntrata.isEmpty() && !capitoliUscita.isEmpty()) {
			addErrore(ErroreBil.CAPITOLO_DI_ENTRATA_ASSENTE.getErrore());
		}
		
		//SIAC-7550
		if(model.getVincolo().getGenereVincolo() == null || model.getVincolo().getGenereVincolo().getUid() == 0) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Omesso Tipo Vincolo"));
		}

		//SIAC-7525
		if(model.getVincolo().getRisorsaVincolata() == null || model.getVincolo().getRisorsaVincolata().getUid() == 0) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Omesso Risorse Vincolate"));
		}
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Scollega il capitolo dal vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String scollegaCapitolo() {
		final String methodName = "scollegaCapitolo";
		
		ScollegaCapitoloAlVincolo request = model.creaRequestScollegaCapitoloAlVincolo();
		logServiceRequest(request);
		
		ScollegaCapitoloAlVincoloResponse response = vincoloCapitoloService.scollegaCapitoloAlVincolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(ScollegaCapitoloAlVincolo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Scollegamento avvenuto con successo");
		
		// Imposto il parametro di rientro a TRUE
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * Associa il capitolo al vincolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String associaCapitolo() {
		final String methodName = "associaCapitolo";
		
		AssociaCapitoloAlVincolo requestAssociazione = model.creaRequestAssociaCapitoloAlVincolo();
		logServiceRequest(requestAssociazione);
		
		AssociaCapitoloAlVincoloResponse responseAssociazione = vincoloCapitoloService.associaCapitoloAlVincolo(requestAssociazione);
		logServiceResponse(responseAssociazione);
		
		if(responseAssociazione.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AssociaCapitoloAlVincolo.class, responseAssociazione));
			addErrori(responseAssociazione);
			return INPUT;
		}
		
		log.debug(methodName, "Associazione avvenuta con successo");
		
		// Imposto il parametro di rientro a TRUE
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
}
