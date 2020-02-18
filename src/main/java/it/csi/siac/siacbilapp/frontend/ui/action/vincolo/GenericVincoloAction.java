/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.vincolo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.vincolo.GenericVincoloModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.VincoloCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.GenereVincolo;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * Action astratta per il Vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/01/2014
 *
 * @param <M> la parametrizzazione del Model
 */
public class GenericVincoloAction<M extends GenericVincoloModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2251780291217335107L;
	
	/** Serviz&icirc; del vincolo */
	@Autowired protected transient VincoloCapitoloService vincoloCapitoloService;
	/** Serviz&icirc; del capitolo di uscita previsione */
	@Autowired protected transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	/** Serviz&icirc; del capitolo di uscita gestione */
	@Autowired protected transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	/** Serviz&icirc; del capitolo di entrata previsione */
	@Autowired protected transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	/** Serviz&icirc; del capitolo di entrata gestione */
	@Autowired protected transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	/** Serviz&icirc; delle codifiche */
	@Autowired protected transient CodificheService codificheService;
	
	/**
	 * Caricamento del genere vincolo
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaGenereVincolo() throws WebServiceInvocationFailureException {
		final String methodName = "caricaGenereVincolo";
		List<GenereVincolo> listaGenereVincolo = sessionHandler.getParametro(BilSessionParameter.LISTA_GENERE_VINCOLO);
		if(listaGenereVincolo == null) {
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(GenereVincolo.class);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(req, res);
				log.info(methodName, msg);
				throw new WebServiceInvocationFailureException(msg);
			}
			listaGenereVincolo = res.getCodifiche(GenereVincolo.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_GENERE_VINCOLO, listaGenereVincolo);
		}
		model.setListaGenereVincolo(listaGenereVincolo);
	}
	
	/**
	 * Effettua una ricerca per i capitoli di Entrata e pone i risultati all'interno della lista dei capitoli di entrata.
	 * 
	 * @param responseRicercaDettaglioVincolo la response per la ricerca di dettaglio per il vincolo
	 * 
	 * @return una lista di capitoli di entrata
	 * 
	 * @throws ApplicationException nel caso in cui almeno una delle ricerche di dettaglio non vada a buon fine
	 */
	protected List<Capitolo<?, ?>> ricercaCapitoliEntrata(RicercaDettaglioVincoloResponse responseRicercaDettaglioVincolo) throws ApplicationException {
		final String methodName = "ricercaCapitoliEntrata";
		
		log.debug(methodName, "Effettuo le ricerche di dettaglio");
		List<Capitolo<?, ?>> result = new ArrayList<Capitolo<?,?>>();
		
		List<CapitoloEntrataGestione> listaCapitoliEntrataGestione = responseRicercaDettaglioVincolo.getVincoloCapitoli().getCapitoliEntrataGestione();
		List<CapitoloEntrataPrevisione> listaCapitoliEntrataPrevisione = responseRicercaDettaglioVincolo.getVincoloCapitoli().getCapitoliEntrataPrevisione();
		
		log.debug(methodName, "Capitoli di gestione: #" + listaCapitoliEntrataGestione.size());
		for(CapitoloEntrataGestione c : listaCapitoliEntrataGestione) {
			RicercaDettaglioCapitoloEntrataGestione request = model.creaRequestRicercaDettaglioCapitoloEntrataGestione(c);
			RicercaDettaglioCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorString = "Errore nella ricerca di dettaglio per capitolo di entrata gestione con uid " + c.getUid();
				log.debug(methodName, errorString);
				addErrori(response);
				throw new ApplicationException();
			}
			result.add(response.getCapitoloEntrataGestione());
		}
		
		log.debug(methodName, "Capitoli di previsione: #" + listaCapitoliEntrataPrevisione.size());
		for(CapitoloEntrataPrevisione c : listaCapitoliEntrataPrevisione) {
			RicercaDettaglioCapitoloEntrataPrevisione request = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(c);
			RicercaDettaglioCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorString = "Errore nella ricerca di dettaglio per capitolo di entrata previsione con uid " + c.getUid();
				log.debug(methodName, errorString);
				addErrori(response);
				throw new ApplicationException();
			}
			result.add(response.getCapitoloEntrataPrevisione());
		}
		
		return result;
	}
	
	/**
	 * Effettua una ricerca per i capitoli di Entrata e pone i risultati all'interno della lista dei capitoli di entrata.
	 * 
	 * @param responseRicercaDettaglioVincolo la response per la ricerca di dettaglio per il vincolo
	 * 
	 * @return una lista di capitoli di entrata
	 * 
	 * @throws ApplicationException nel caso in cui almeno una delle ricerche di dettaglio non vada a buon fine
	 */
	protected List<Capitolo<?, ?>> ricercaCapitoliUscita(RicercaDettaglioVincoloResponse responseRicercaDettaglioVincolo) throws ApplicationException {
		final String methodName = "ricercaCapitoliUscita";
		
		log.debug(methodName, "Effettuo le ricerche di dettaglio");
		List<Capitolo<?, ?>> result = new ArrayList<Capitolo<?,?>>();
		
		List<CapitoloUscitaGestione> listaCapitoliUscitaGestione = responseRicercaDettaglioVincolo.getVincoloCapitoli().getCapitoliUscitaGestione();
		List<CapitoloUscitaPrevisione> listaCapitoliUscitaPrevisione = responseRicercaDettaglioVincolo.getVincoloCapitoli().getCapitoliUscitaPrevisione();
		
		log.debug(methodName, "Capitoli di gestione: #" + listaCapitoliUscitaGestione.size());
		for(CapitoloUscitaGestione c : listaCapitoliUscitaGestione) {
			RicercaDettaglioCapitoloUscitaGestione request = model.creaRequestRicercaDettaglioCapitoloUscitaGestione(c);
			RicercaDettaglioCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorString = "Errore nella ricerca di dettaglio per capitolo di uscita gestione con uid " + c.getUid();
				log.debug(methodName, errorString);
				addErrori(response);
				throw new ApplicationException();
			}
			result.add(response.getCapitoloUscita());
		}
		
		log.debug(methodName, "Capitoli di previsione: #" + listaCapitoliUscitaPrevisione.size());
		for(CapitoloUscitaPrevisione c : listaCapitoliUscitaPrevisione) {
			RicercaDettaglioCapitoloUscitaPrevisione request = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione(c);
			RicercaDettaglioCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorString = "Errore nella ricerca di dettaglio per capitolo di uscita previsione con uid " + c.getUid();
				log.debug(methodName, errorString);
				addErrori(response);
				throw new ApplicationException();
			}
			result.add(response.getCapitoloUscitaPrevisione());
		}
		
		return result;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
