/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.RicercaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la gestione della Ricerca per il Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 08/08/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCapitoloEntrataGestioneAction extends CapitoloEntrataAction<RicercaCapitoloEntrataGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3239926375169556435L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();

		if(!almenoUnaCodificaGiaCaricata()){
			caricaListaCodificheDiBase(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
		}
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
		
		log.debugEnd(methodName, "");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca con operazioni del Capitolo di Entrata Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName="ricercaConOperazioniCDU";
		
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		RicercaSinteticaCapitoloEntrataGestione request = model.creaRequestRicercaSinteticaCapitoloEntrataGestione(false);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		logServiceResponse(response);
		 
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
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
		
		log.debug(methodName, "Imposto in sessione la request");
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		model.setListaCapitoli(ElementoCapitoloFactory.getInstances(response.getCapitoli(), false, model.isGestioneUEB()));
		
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_GESTIONE_IMPORTI, response.getTotaleImporti());

		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca come dato aggiuntivo del Capitolo di Entrata Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaComeDatoAggiuntivo() {
		final String methodName = "ricercaComeDatoAggiuntivo";
		
		log.debug(methodName, "Validazione dei campi");
		CapitoloEntrataGestione capitoloEntrataGestione = model.getCapitoloEntrataGestione();
		if(capitoloEntrataGestione == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitolo"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo"));
		} else {
			if(capitoloEntrataGestione.getNumeroCapitolo() == null || capitoloEntrataGestione.getNumeroCapitolo().intValue() == 0) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitolo"));
			}
			if(capitoloEntrataGestione.getNumeroArticolo() == null) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo"));
			}
		}
		
		if(hasErrori()) {
			log.debug(methodName, "Errori nella validazione del capitolo");
			// Ritorno SUCCESS anche se ho un fallimento, e gestisco gli errori a livello client
			return SUCCESS;
		}
		
		// Creazione della request
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		RicercaSinteticaCapitoloEntrataGestione request = model.creaRequestRicercaSinteticaCapitoloEntrataGestione(true);
		logServiceRequest(request);
		
		// Invocazione del servizio
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloEntrataGestioneResponse response = 
		 		capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		logServiceResponse(response);
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
		} else {
			log.debug(methodName, "Ricerca effettuata con successo");
			if(response.getCardinalitaComplessiva() == 0) {
				log.debug(methodName, "Nessun risultato trovato");
				addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			}
			
			log.debug(methodName, "Sono presenti delle liste nella risposta");
			
			/*
			 * Mette in sessione:
			 * 		1.	La lista dei risultati : per poter visualizzare i dati trovati nella action di risultatiRicerca
			 * 		2. 	La request usata per chiamare il servizio di ricerca : per poterla riusare allo scopo di 
			 * 				reperire un nuovo blocco di risultati
			 */
			log.debug(methodName, "Imposto in sessione la request");
			// Workaround per sopperire al fatto che le requests non siano serializzabili
			sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, request);
			
			log.debug(methodName, "Imposto in sessione la lista");
			sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE, response.getCapitoli());
			
			model.setListaCapitoli(ElementoCapitoloFactory.getInstances(response.getCapitoli(), false, model.isGestioneUEB()));
		}
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca di dettaglio per il Capitolo di Entrata Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		
		log.debug(methodName, "Validazione dei campi");
		boolean valido = validaRicercaComeDatoAggiuntivo();
		if(!valido) {
			log.debug(methodName, "Errore nella validazione dei campi");
			// Posso ritornare SUCCESS, in quanto tale risultato contiene la serializzazione degli errori
			return SUCCESS;
		}

		// Effettuo la ricerca puntuale per ottenere l'uid del capitolo da ottenere
		log.debug(methodName, "Creazione della request per la ricerca puntuale");
		RicercaPuntualeCapitoloEntrataGestione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataGestione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		RicercaPuntualeCapitoloEntrataGestioneResponse responseRicercaPuntuale = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(requestRicercaPuntuale);
		logServiceResponse(responseRicercaPuntuale);
		
		if(responseRicercaPuntuale.hasErrori()) {
			log.debug(methodName, "La ricerca non ha avuto esito positivo");
			addErrori(responseRicercaPuntuale);
			// Ritorno comunque SUCCESS sì da ottenere gli errori nella risposta JSON
			return SUCCESS;
		}
		
		// Effettuo la ricerca di dettaglio a partire dall'uid ottenuto dalla ricerca puntuale
		log.debug(methodName, "Creazione della request per la ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataGestione(responseRicercaPuntuale.getCapitoloEntrataGestione().getUid());
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il servizio per la ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataGestioneResponse responseRicercaDettaglio = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(requestRicercaDettaglio);
		logServiceResponse(responseRicercaDettaglio);
		//SIAC-6881 - TEST
		// FIXME responseRicercaDettaglio.getCapitoloEntrataGestione().setComponentiCapitolo(TestComponentiCapitoloModel.lcc);
		
		// Imposto il capitolo nel model
		model.setCapitoloEntrataGestione(responseRicercaDettaglio.getCapitoloEntrataGestione());
		return SUCCESS;
	}
	
	
	
	
	public String ricercaDettaglioDaVariazione() {
		
		final String methodName = "ricercaDettaglio";
		model.caricaClassificatoriDaSessione(sessionHandler);		
		log.debug(methodName, "Validazione dei campi");
		boolean valido = validaRicercaComeDatoAggiuntivo();
		if(!valido) {
			log.debug(methodName, "Errore nella validazione dei campi");
			// Posso ritornare SUCCESS, in quanto tale risultato contiene la serializzazione degli errori
			return SUCCESS;
		}

		// Effettuo la ricerca puntuale per ottenere l'uid del capitolo da ottenere
		RicercaPuntualeCapitoloEntrataGestioneResponse responseRicercaPuntuale = ricercaSinteticaCapitolo();
		logServiceResponse(responseRicercaPuntuale);
		if(responseRicercaPuntuale.hasErrori()) {
			log.debug(methodName, "La ricerca non ha avuto esito positivo");
			addErrori(responseRicercaPuntuale);
			// Ritorno comunque SUCCESS sì da ottenere gli errori nella risposta JSON
			return SUCCESS;
		}
		
		int uidVar = model.getUidVariazione();

		// Effettuo la ricerca di dettaglio a partire dall'uid ottenuto dalla ricerca puntuale			
		log.debug(methodName, "Richiamo il servizio per la ricerca dettaglio");			
		log.debug(methodName, "Creazione della request per la ricerca dettaglio");			
		RicercaDettaglioCapitoloEntrataGestioneResponse res = ricercaDettaglioCapitolo(responseRicercaPuntuale.getCapitoloEntrataGestione().getUid());

		
		//SIAC-6884-RicercaCapitolo da Variazione ---> Informazioni relativi al capitolo che si vuole associare	da confrontare con quelli gia associati	
		CapitoloEntrataGestione cup = res.getCapitoloEntrataGestione();
		StrutturaAmministrativoContabile sacDaAssociare = (cup.getStrutturaAmministrativoContabile() !=null) ? cup.getStrutturaAmministrativoContabile(): null;	
		

		List<StrutturaAmministrativoContabile> struttureAccount = sessionHandler.getAccount().getStruttureAmministrativeContabili();
				
		boolean isSacAfferente = sacAfferente(sacDaAssociare, struttureAccount, ricercaDettaglioVariazione(uidVar).getVariazioneImportoCapitolo().getDirezioneProponente().getCodice());

		//SE PRIMO CAPITOLO LO FACCIO ASSOCIARE, CONTROLLANDO SAC E FONDI REGIONALI						
		if(isSacAfferente){ 			
			model.setCapitoloEntrataGestione(cup);
			return SUCCESS;							
		}					
		responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
		addErrori(responseRicercaPuntuale);
		return SUCCESS;							

	}
	
	/**
	 * Validazione per il metodo {@link #ricercaConOperazioniCDU()}
	 */
	public void validateRicercaConOperazioniCDU() {
		final String methodName = "validateRicercaConOperazioniCDU";
		
		log.debug(methodName, "Check per il SIOPE");
		checkSiope();
		log.debug(methodName, "Verifico presenza criterio di ricerca getCapitoloUscitaGestione() ");
		boolean formValido = 
				// Controllo il capitolo
			checkCapitolo(model.getCapitoloEntrataGestione()) ||
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
			checkPresenzaIdEntita(model.getCapitoloEntrataGestione().getCategoriaCapitolo()) ||
				// Controllo l'atto di legge
			checkAttoDiLegge(model.getAttoDiLegge()) ||
				// Controllo i flags
			checkStringaValorizzata(model.getFlagRilevanteIva(), "FlagRilevanteIva") ||
			//SIAC-7858 CM 19/05/2021 Inizio
			checkCampoValorizzato(model.getFlagEntrataDubbiaEsigFCDE(), "FlagEntrataDubbiaEsigFCDE");
			//SIAC-7858 CM 19/05/2021 Fine
		
		checkCondition(formValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		log.debug(methodName, "Ricerca valida? " + formValido);
	}
	
	/**
	 * Controlla il SIOPE
	 */
	private void checkSiope() {
		final String methodName = "checkSiope";
		if(model.getSiopeEntrata() == null || model.getSiopeEntrata().getUid() != 0 || StringUtils.isBlank(model.getSiopeEntrata().getCodice())) {
			return;
		}
		try {
			SiopeEntrata ss = findClassificatoreByCodice(model.getSiopeEntrata(), TipologiaClassificatore.SIOPE_ENTRATA_I, "Siope");
			model.setSiopeEntrata(ss);
			model.setCodiceTipoClassificatoreSiope(ss.getTipoClassificatore().getCodice());
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
		}
	}
	
	/**
	 * Effettua una validazione per la ricerca come dato aggiuntivo
	 * 
	 * @return <code>true</code> se i dati inseriti sono validi per la ricerca; <code>false</code> altrimenti
	 */
	private boolean validaRicercaComeDatoAggiuntivo() {
		CapitoloEntrataGestione capitoloDaValidare = model.getCapitoloEntrataGestione();
		if(capitoloDaValidare == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitolo"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo"));
		} else {
			checkNotNull(capitoloDaValidare.getAnnoCapitolo(), "Anno");
			checkNotNull(capitoloDaValidare.getNumeroCapitolo(), "Capitolo");
			checkNotNull(capitoloDaValidare.getNumeroArticolo(), "Articolo");
		}
		return !hasErrori();
	}
	
	/**
	 * Carica la lista dei classificatori generici.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassificatoriGenerici(){
		caricaListaClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
		return SUCCESS;
	}
	
	/**
	 * Carica la lista dei classificatori gerarchici.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassificatoriGerarchici(){
		caricaListaClassificatoriGerarchici(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
		return SUCCESS;
	}
	
	/*SIAC-6884*/
	/*Metodi di appoggio per filtro*/
	/*Ricerca sintetica del capitolo che si vuole cercare*/
	protected RicercaPuntualeCapitoloEntrataGestioneResponse ricercaSinteticaCapitolo (){
		final String methodName = "ricercaSinteticaCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca puntuale");
		RicercaPuntualeCapitoloEntrataGestione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataGestione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		return capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(requestRicercaPuntuale);
	}
	
	/*Ricerca dettagliata del capitolo che si vuole cercare*/
	protected RicercaDettaglioCapitoloEntrataGestioneResponse ricercaDettaglioCapitolo (int idCapitolo){
		final String methodName = "ricercaDettaglioCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca");
		RicercaDettaglioCapitoloEntrataGestione req = model.creaRequestRicercaDettaglioCapitoloEntrataGestione(idCapitolo);
		logServiceRequest(req);
		log.debug(methodName, "Richiamo il servizio per la ricerca");
		return capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(req);
	}
	
	private RicercaDettaglioAnagraficaVariazioneBilancioResponse ricercaDettaglioVariazione(int uidVar) {
		final String methodName ="ricercaDettaglioVariazione";
		RicercaDettaglioAnagraficaVariazioneBilancio request = model.creaRequestRicercaDettaglioAnagraficaVariazioneBilancio(uidVar);
		logServiceRequest(request);
		log.debug(methodName, "Invocazione del servizio di ricerca");		
		return variazioneDiBilancioService.ricercaDettaglioAnagraficaVariazioneBilancio(request);	

	}
//	
//	private RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettaglioPrimoCapitoloNellaVariazione(int uidVar, String tipoCapitolo) {
//		final String methodName ="ricercaPrimoCapitoloNellaVariazione";
//		RicercaDettagliVariazioneImportoCapitoloNellaVariazione requestDettagli = model
//				.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione(uidVar, tipoCapitolo);
//		log.debug(methodName, "Invocazione del servizio di ricerca");	
//		return variazioneDiBilancioService
//				.ricercaDettagloVariazionePrimoCapitoloNellaVariazione(requestDettagli); //se response dettagli non ha trovato nessuna entity, posso consentire di aggiungere un capitolo che abbia la stessa direzione proponente ricavata dall'anagrafica della variazione
//	}
	
	
	private RicercaPuntualeCapitoloEntrataGestioneResponse setNullInResponse(
			RicercaPuntualeCapitoloEntrataGestioneResponse responseRicercaPuntuale) {
		
			CapitoloEntrataGestione cupNull = null;
			responseRicercaPuntuale.setCapitoloEntrataGestione(cupNull);
			//FIXME
			Errore errore = new Errore();
			errore.setCodice("COR_ERR_01");
			errore.setDescrizione("Capitolo non associabile alla Variazione");
			List<Errore> errori = new ArrayList<Errore>();
			errori.add(errore);
			responseRicercaPuntuale.addErrori(errori);	
			return responseRicercaPuntuale;
	}
	
	
	
}
