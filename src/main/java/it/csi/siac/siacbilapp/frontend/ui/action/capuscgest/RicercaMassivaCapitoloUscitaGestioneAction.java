/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLeggeResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.RicercaMassivaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la gestione della Ricerca Massiva per il Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 17/09/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMassivaCapitoloUscitaGestioneAction extends CapitoloUscitaAction<RicercaMassivaCapitoloUscitaGestioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 140502931627282346L;
	
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		
		ControllaClassificatoriModificabiliCapitoloResponse response = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(response != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaClassificatori(response, true);
		}
		log.debugEnd(methodName, "");
	}
	
	@Override
	@SkipValidation
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Controllo per la prima volta l'aggiornabilità dei campi
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloUscitaGestione(), TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, true);
		
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca con operazioni del Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName = "ricercaConOperazioniCDU";
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		// Compilazione dei classificatori dalla sessione
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		// Creazione della request
		RicercaSinteticaMassivaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaMassivaCapitoloUscitaGestione();
		logServiceRequest(request);
		
		// Richiama il servizio di ricercaSinteticaCapitoloUscitaGestione()
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica Massiva");
		
		RicercaSinteticaMassivaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaMassivaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		 
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica Massiva");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Sono presenti delle liste nella risposta");
		
		log.debug(methodName, "Pulisco la sessione");
		// Ricordare pulire correttamente la sessione 
		sessionHandler.cleanAllSafely();
		
		// Imposto nella request i dati remoti
		request.setPaginaRemote(response.getPaginaRemote());
		request.setPosizionePaginaRemote(response.getPosizionePaginaRemote());

		// Mette in sessione :
		// La lista dei risultati : per poter visualizzare i dati trovati nella action di risultatiRicerca 
		// La request usata per chiamare il servizio di ricerca : per poterla riusare allo scopo di reperire un nuovo blocco di risultati
		log.debug(methodName, "Imposto in sessione la request");
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_MASSIVA_CAPITOLO, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE, response.getCapitoli());
		log.debug(methodName, "Lista caricata in sessione: " + response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE_IMPORTI, response.getTotaleImporti());

		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca per la variazione codifiche.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	@SkipValidation
	public String ricercaVariazioneCodifiche() {
		final String methodName = "ricercaVariazioneCodifiche";
		log.debug(methodName, "Validazione della request");
		checkNotNull(model.getCapitoloUscitaGestione().getNumeroCapitolo(), "numero capitolo");
		checkNotNull(model.getCapitoloUscitaGestione().getNumeroArticolo(), "numero articolo");
		checkNotNull(model.getCapitoloUscitaGestione().getTipoCapitolo(), "tipo capitolo");
		
		if(hasErrori()) {
			// Vi sono degli errori: torno indietro
			return SUCCESS;
		}
		
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		// Creazione della request
		RicercaDettaglioMassivaCapitoloUscitaGestione request = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione();
		logServiceRequest(request);
		
		RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioMassivaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getCapitoloMassivaUscitaGestione() == null) {
			log.debug(methodName, "Nessun capitolo trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		log.debug(methodName, "Ricerca effettuata con successo");
		
		model.impostaDatiDaRicercaDettaglio(response);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca di dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		checkNotNull(model.getCapitoloUscitaGestione().getAnnoCapitolo(), "Anno capitolo");
		checkNotNull(model.getCapitoloUscitaGestione().getNumeroCapitolo(), "Numero capitolo");
		checkNotNull(model.getCapitoloUscitaGestione().getNumeroArticolo(), "Numero articolo");
		if(hasErrori()) {
			log.debug(methodName, "La validazione per la ricerca ha sortito risultato negativo");
			return SUCCESS;
		}
		
		// La validazione ha avuto successo: continuo con l'invocazione del servizio
		log.debug(methodName, "Creo la request per la ricerca di dettaglio");
		RicercaDettaglioMassivaCapitoloUscitaGestione request = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaGestione();
		logServiceRequest(request);
		
		log.debug(methodName, "Invoco il servizio per la ricerca di dettaglio");
		RicercaDettaglioMassivaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioMassivaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento dell'invocazione");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getCapitoloMassivaUscitaGestione() == null) {
			log.debug(methodName, "Errore nell'invocazione al servizio: nessun capitolo presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		model.setCapitoloMassivaUscitaGestione(response.getCapitoloMassivaUscitaGestione());
		model.setListaCapitoloVariazione(ElementoCapitoloVariazioneFactory.getInstances(response.getCapitoloMassivaUscitaGestione().getElencoCapitoli(), Boolean.TRUE));
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MASSIVA_COME_DATO_AGGIUNTIVO, response.getCapitoloMassivaUscitaGestione().getElencoCapitoli());
		// La response è stata un successo
		
		// Injetto il massimo valore della UEB
		Integer maxUEB = ComparatorUtils.getMaxUEB(response.getCapitoloMassivaUscitaGestione().getElencoCapitoli());
		model.setMaxUEB(maxUEB);
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		super.validate();
		final String methodName = "validate";
		log.debugStart(methodName, "Chiamata alla validate()");
		
		boolean formValido = 
					// Controllo il capitolo
				checkCapitolo(model.getCapitoloUscitaGestione()) ||
					// Controllo i classificatori
				checkPresenzaIdEntita(model.getMissione()) ||
				checkPresenzaIdEntita(model.getProgramma()) ||
				checkPresenzaIdEntita(model.getClassificazioneCofog()) ||
				checkPresenzaIdEntita(model.getTitoloSpesa()) ||
				checkPresenzaIdEntita(model.getMacroaggregato()) ||
				checkPresenzaIdEntita(model.getElementoPianoDeiConti()) ||
				checkPresenzaIdEntita(model.getStrutturaAmministrativoContabile()) ||
				checkPresenzaIdEntita(model.getTipoFinanziamento()) ||
				checkPresenzaIdEntita(model.getTipoFondo()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico1()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico2()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico3()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico4()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico5()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico6()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico7()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico8()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico9()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico10()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico11()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico12()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico13()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico14()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico15()) ||
				checkPresenzaIdEntita(model.getSiopeSpesa()) ||
				checkPresenzaIdEntita(model.getRicorrenteSpesa()) ||
				checkPresenzaIdEntita(model.getPerimetroSanitarioSpesa()) ||
				checkPresenzaIdEntita(model.getTransazioneUnioneEuropeaSpesa()) ||
				checkPresenzaIdEntita(model.getPoliticheRegionaliUnitarie()) ||
				checkPresenzaIdEntita(model.getCapitoloUscitaGestione().getCategoriaCapitolo()) ||
					// Validazione per l'atto di legge
				checkAttoDiLegge(model.getAttoDiLegge()) ||
					// Controllo i flags
				checkStringaValorizzata(model.getFlagFondoPluriennaleVincolato(), "FlagFondoPluriennaleVincolato") ||
				checkStringaValorizzata(model.getFlagFondoSvalutazioneCrediti(), "FlagFondoSvalutazioneCrediti") ||
				checkStringaValorizzata(model.getFlagFunzioniDelegate(), "FlagFunzioniDelegate") ||
				checkStringaValorizzata(model.getFlagPerMemoria(), "FlagPerMemoria") ||
				checkStringaValorizzata(model.getFlagRilevanteIva(), "FlagRilevanteIva");

		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}
		
		log.debugEnd(methodName, "Fine validate() - formValido = " + formValido);
	}
	
	/* Metodi di utilita' */
	
	@Override
	protected void caricaListaCodifiche(BilConstants codiceTipoElementoBilancio) {
		super.caricaListaCodifiche(codiceTipoElementoBilancio);
		List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
		RicercaTipiAttoDiLegge request = model.creaRequestRicercaTipiAttoDiLegge();
		RicercaTipiAttoDiLeggeResponse response = attoDiLeggeService.getTipiAttoLegge(request);
		if(!response.hasErrori()) {
			listaTipoAtto = response.getElencoTipi();
		}
		model.setListaTipoAtto(listaTipoAtto);
	}
	
}
