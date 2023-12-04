/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentprev;

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
import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.RicercaMassivaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la gestione della Ricerca Massiva per il Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 17/09/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMassivaCapitoloEntrataPrevisioneAction extends CapitoloEntrataAction<RicercaMassivaCapitoloEntrataPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 140502931627282346L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		
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
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloEntrataPrevisione(), TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, true);
		
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca con operazioni del Capitolo di Entrata Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName = "ricercaConOperazioniCDU";
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		// Compilazione dei classificatori dalla sessione
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		// Creazione della request
		RicercaSinteticaMassivaCapitoloEntrataPrevisione request = model.creaRequestRicercaSinteticaMassivaCapitoloEntrataPrevisione();
		logServiceRequest(request);
		
		// Richiama il servizio di ricercaSinteticaCapitoloEntrataPrevisione()
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica Massiva");
		
		RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaSinteticaMassivaCapitoloEntrataPrevisione(request);
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
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE, response.getCapitoli());
		log.debug(methodName, "Lista caricata in sessione: " + response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE_IMPORTI, response.getTotaleImporti());

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
		checkNotNull(model.getCapitoloEntrataPrevisione().getNumeroCapitolo(), "numero capitolo");
		checkNotNull(model.getCapitoloEntrataPrevisione().getNumeroArticolo(), "numero articolo");
		checkNotNull(model.getCapitoloEntrataPrevisione().getTipoCapitolo(), "tipo capitolo");
		
		if(hasErrori()) {
			// Vi sono degli errori: torno indietro
			return SUCCESS;
		}
		
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		// Creazione della request
		RicercaDettaglioMassivaCapitoloEntrataPrevisione request = model.creaRequestRicercaDettaglioMassivaCapitoloEntrataPrevisione();
		logServiceRequest(request);
		
		RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaDettaglioMassivaCapitoloEntrataPrevisione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getCapitoloMassivaEntrataPrevisione() == null) {
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
		checkNotNull(model.getCapitoloEntrataPrevisione().getAnnoCapitolo(), "Anno capitolo");
		checkNotNull(model.getCapitoloEntrataPrevisione().getNumeroCapitolo(), "Numero capitolo");
		checkNotNull(model.getCapitoloEntrataPrevisione().getNumeroArticolo(), "Numero articolo");
		if(hasErrori()) {
			log.debug(methodName, "La validazione per la ricerca ha sortito risultato negativo");
			return SUCCESS;
		}
		
		// La validazione ha avuto successo: continuo con l'invocazione del servizio
		log.debug(methodName, "Creo la request per la ricerca di dettaglio");
		RicercaDettaglioMassivaCapitoloEntrataPrevisione request = model.creaRequestRicercaDettaglioMassivaCapitoloEntrataPrevisione();
		logServiceRequest(request);
		
		log.debug(methodName, "Invoco il servizio per la ricerca di dettaglio");
		RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaDettaglioMassivaCapitoloEntrataPrevisione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento dell'invocazione");
			addErrori(response);
			return SUCCESS;
		} else if(response.getCapitoloMassivaEntrataPrevisione() == null) {
			log.debug(methodName, "Errore nell'invocazione al servizio: nessun capitolo presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		model.setCapitoloMassivaEntrataPrevisione(response.getCapitoloMassivaEntrataPrevisione());
		model.setListaCapitoloVariazione(ElementoCapitoloVariazioneFactory.getInstances(response.getCapitoloMassivaEntrataPrevisione().getElencoCapitoli(), Boolean.TRUE));
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MASSIVA_COME_DATO_AGGIUNTIVO, response.getCapitoloMassivaEntrataPrevisione().getElencoCapitoli());
		// Injetto il massimo valore della UEB
		Integer maxUEB = ComparatorUtils.getMaxUEB(response.getCapitoloMassivaEntrataPrevisione().getElencoCapitoli());
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
				checkCapitolo(model.getCapitoloEntrataPrevisione()) ||
					// Controllo i classificatori
				checkPresenzaIdEntita(model.getTitoloEntrata()) ||
				checkPresenzaIdEntita(model.getTipologiaTitolo()) ||
				checkPresenzaIdEntita(model.getCategoriaTipologiaTitolo()) ||
				checkPresenzaIdEntita(model.getElementoPianoDeiConti()) ||
				checkPresenzaIdEntita(model.getStrutturaAmministrativoContabile()) ||
				checkPresenzaIdEntita(model.getTipoFinanziamento()) ||
				checkPresenzaIdEntita(model.getTipoFondo()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico36()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico37()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico38()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico39()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico40()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico41()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico42()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico43()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico44()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico45()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico46()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico47()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico48()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico49()) ||
				checkPresenzaIdEntita(model.getClassificatoreGenerico50()) ||
				checkPresenzaIdEntita(model.getSiopeEntrata()) ||
				checkPresenzaIdEntita(model.getRicorrenteEntrata()) ||
				checkPresenzaIdEntita(model.getPerimetroSanitarioEntrata()) ||
				checkPresenzaIdEntita(model.getTransazioneUnioneEuropeaEntrata()) ||
				checkPresenzaIdEntita(model.getCapitoloEntrataPrevisione().getCategoriaCapitolo()) ||
					// Controllo l'atto di legge
				checkAttoDiLegge(model.getAttoDiLegge()) ||
					// Controllo i flags
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
