/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

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
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.RicercaMassivaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la gestione della Ricerca Massiva per il Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 17/09/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMassivaCapitoloUscitaPrevisioneAction extends CapitoloUscitaAction<RicercaMassivaCapitoloUscitaPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 140502931627282346L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatori = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		ControllaAttributiModificabiliCapitoloResponse responseAttributi = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, ControllaAttributiModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseClassificatori != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaClassificatori( responseClassificatori, true);
		}
		if(responseAttributi != null) {
			log.debug(methodName, "Valuto l'editabilita degli attributi");
			model.valutaModificabilitaAttributi(responseAttributi, true);
		}
		log.debugEnd(methodName, "");
	}
	
	@Override
	@SkipValidation
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Controllo per la prima volta l'aggiornabilità dei campi
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloUscitaPrevisione(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili = ottieniResponseControllaAttributiModificabiliCapitolo(model.getCapitoloUscitaPrevisione(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE);
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, true);
		model.valutaModificabilitaAttributi(responseAttributiModificabili, true);
		
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, responseAttributiModificabili);
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca con operazioni del Capitolo di Uscita Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName = "ricercaConOperazioniCDU";
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		// Compilazione dei classificatori dalla sessione
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		// Creazione della request
		RicercaSinteticaMassivaCapitoloUscitaPrevisione request = model.creaRequestRicercaSinteticaMassivaCapitoloUscitaPrevisione();
		logServiceRequest(request);
		
		// Richiama il servizio di ricercaSinteticaCapitoloUscitaPrevisione()
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica Massiva");
		
		RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaSinteticaMassivaCapitoloUscitaPrevisione(request);
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
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
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
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE, response.getCapitoli());
		log.debug(methodName, "Lista caricata in sessione: " + response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE_IMPORTI, response.getTotaleImporti());

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
		checkNotNull(model.getCapitoloUscitaPrevisione().getNumeroCapitolo(), "numero capitolo");
		checkNotNull(model.getCapitoloUscitaPrevisione().getNumeroArticolo(), "numero articolo");
		checkNotNull(model.getCapitoloUscitaPrevisione().getTipoCapitolo(), "tipo capitolo");
		
		if(hasErrori()) {
			// Vi sono degli errori: torno indietro
			return SUCCESS;
		}
		
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		// Creazione della request
		RicercaDettaglioMassivaCapitoloUscitaPrevisione request = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaPrevisione();
		logServiceRequest(request);
		
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaDettaglioMassivaCapitoloUscitaPrevisione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getCapitoloMassivaUscitaPrevisione() == null) {
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
		checkNotNull(model.getCapitoloUscitaPrevisione().getAnnoCapitolo(), "Anno capitolo");
		checkNotNull(model.getCapitoloUscitaPrevisione().getNumeroCapitolo(), "Numero capitolo");
		checkNotNull(model.getCapitoloUscitaPrevisione().getNumeroArticolo(), "Numero articolo");
		if(hasErrori()) {
			log.debug(methodName, "La validazione per la ricerca ha sortito risultato negativo");
			return SUCCESS;
		}
		
		// La validazione ha avuto successo: continuo con l'invocazione del servizio
		log.debug(methodName, "Creo la request per la ricerca di dettaglio");
		RicercaDettaglioMassivaCapitoloUscitaPrevisione request = model.creaRequestRicercaDettaglioMassivaCapitoloUscitaPrevisione();
		logServiceRequest(request);
		
		log.debug(methodName, "Invoco il servizio per la ricerca di dettaglio");
		RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaDettaglioMassivaCapitoloUscitaPrevisione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento dell'invocazione");
			addErrori(response);
			return SUCCESS;
		} else if(response.getCapitoloMassivaUscitaPrevisione() == null) {
			log.debug(methodName, "Errore nell'invocazione al servizio: nessun capitolo presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		model.setCapitoloMassivaUscitaPrevisione(response.getCapitoloMassivaUscitaPrevisione());
		model.setListaCapitoloVariazione(ElementoCapitoloVariazioneFactory.getInstances(response.getCapitoloMassivaUscitaPrevisione().getElencoCapitoli(), Boolean.TRUE));
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MASSIVA_COME_DATO_AGGIUNTIVO, response.getCapitoloMassivaUscitaPrevisione().getElencoCapitoli());
		
		// Injetto il massimo valore della UEB
		Integer maxUEB = ComparatorUtils.getMaxUEB(response.getCapitoloMassivaUscitaPrevisione().getElencoCapitoli());
		model.setMaxUEB(maxUEB);
		
		// La response è stata un successo
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		super.validate();
		final String methodName = "validate";
		log.debugStart(methodName, "Chiamata alla validate()");
		
		boolean formValido = 
					// Controllo il capitolo
				checkCapitolo(model.getCapitoloUscitaPrevisione())
					// Controllo i classificatori
				|| checkPresenzaIdEntita(model.getMissione())
				|| checkPresenzaIdEntita(model.getProgramma())
				|| checkPresenzaIdEntita(model.getClassificazioneCofog())
				|| checkPresenzaIdEntita(model.getTitoloSpesa())
				|| checkPresenzaIdEntita(model.getMacroaggregato())
				|| checkPresenzaIdEntita(model.getElementoPianoDeiConti())
				|| checkPresenzaIdEntita(model.getStrutturaAmministrativoContabile())
				|| checkPresenzaIdEntita(model.getTipoFinanziamento())
				|| checkPresenzaIdEntita(model.getTipoFondo())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico1())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico2())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico3())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico4())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico5())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico6())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico7())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico8())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico9())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico10())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico11())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico12())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico13())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico14())
				|| checkPresenzaIdEntita(model.getClassificatoreGenerico15())
				|| checkPresenzaIdEntita(model.getSiopeSpesa())
				|| checkPresenzaIdEntita(model.getRicorrenteSpesa())
				|| checkPresenzaIdEntita(model.getPerimetroSanitarioSpesa())
				|| checkPresenzaIdEntita(model.getTransazioneUnioneEuropeaSpesa())
				|| checkPresenzaIdEntita(model.getPoliticheRegionaliUnitarie())
				|| checkPresenzaIdEntita(model.getCapitoloUscitaPrevisione().getCategoriaCapitolo())
					// Validazione per l'atto di legge
				|| checkAttoDiLegge(model.getAttoDiLegge())
					// Controllo i flags
				|| checkStringaValorizzata(model.getFlagFondoPluriennaleVincolato(), "FlagFondoPluriennaleVincolato")
				|| checkStringaValorizzata(model.getFlagFondoSvalutazioneCrediti(), "FlagFondoSvalutazioneCrediti")
				|| checkStringaValorizzata(model.getFlagFunzioniDelegate(), "FlagFunzioniDelegate")
				|| checkStringaValorizzata(model.getFlagPerMemoria(), "FlagPerMemoria")
				|| checkStringaValorizzata(model.getFlagRilevanteIva(), "FlagRilevanteIva");
		
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
