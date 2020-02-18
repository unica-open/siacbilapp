/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentprev;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.RicercaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la gestione della Ricerca per il Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 07/08/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCapitoloEntrataPrevisioneAction extends CapitoloEntrataAction<RicercaCapitoloEntrataPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1570976087649772709L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		if(almenoUnaCodificaGiaCaricata()){
			caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		} else {
			caricaListaCodificheDiBase(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		}
		
		log.debugEnd(methodName, "");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca con operazioni del Capitolo di Entrata Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName="ricercaConOperazioniCDU";
		
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		RicercaSinteticaCapitoloEntrataPrevisione request = model.creaRequestRicercaSinteticaCapitoloEntrataPrevisione();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaSinteticaCapitoloEntrataPrevisione(request);
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
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE, response.getCapitoli());
		
		model.setListaCapitoli(ElementoCapitoloFactory.getInstances(response.getCapitoli(), false, model.isGestioneUEB()));
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);

		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE_IMPORTI, response.getTotaleImporti());

		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca come dato aggiuntivo del Capitolo di Uscita Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String ricercaComeDatoAggiuntivo() {
		final String methodName = "ricercaComeDatoAggiuntivo";
		
		log.debug(methodName, "Validazione dei campi");
		boolean valido = validaRicercaComeDatoAggiuntivo();
		if(!valido) {
			log.debug(methodName, "Errore nella validazione dei campi");
			// Posso ritornare SUCCESS, in quanto tale risultato contiene la serializzazione degli errori
			return SUCCESS;
		}
		log.debug(methodName, "Creazione della request");
		RicercaSinteticaCapitoloEntrataPrevisione request = model.creaRequestRicercaSinteticaCapitoloEntrataPrevisione();
		// Impostazione dello stato VALIDO per il capitolo
		request.getRicercaSinteticaCapitoloEPrev().setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca sintetica");
		RicercaSinteticaCapitoloEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.ricercaSinteticaCapitoloEntrataPrevisione(request);
		log.debug(methodName, "Richiamato il WebService di ricerca sintetica");
		
		// Controllo il fallimento
		if(response.hasErrori()) {
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		
		// Ho i dati nella response
		List<ElementoCapitolo> listaCapitoli = new ArrayList<ElementoCapitolo>();
		// Converto i capitolo nei loro wrapper
		for(CapitoloEntrataPrevisione capitoloEntrataPrevisione : response.getCapitoli()) {
			listaCapitoli.add(ElementoCapitoloFactory.getInstance(capitoloEntrataPrevisione, false, model.isGestioneUEB()));
		}
		// Injetto la lista dei wrapper
		model.setListaCapitoli(listaCapitoli);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca di dettaglio per il Capitolo di Entrata Previsione.
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
		RicercaPuntualeCapitoloEntrataPrevisione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataPrevisione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		RicercaPuntualeCapitoloEntrataPrevisioneResponse responseRicercaPuntuale = capitoloEntrataPrevisioneService.ricercaPuntualeCapitoloEntrataPrevisione(requestRicercaPuntuale);
		logServiceResponse(responseRicercaPuntuale);
		
		if(responseRicercaPuntuale.hasErrori()) {
			log.debug(methodName, "La ricerca non ha avuto esito positivo");
			addErrori(responseRicercaPuntuale);
			// Ritorno comunque SUCCESS sì da ottenere gli errori nella risposta JSON
			return SUCCESS;
		}
		
		// Effettuo la ricerca di dettaglio a partire dall'uid ottenuto dalla ricerca puntuale
		log.debug(methodName, "Creazione della request per la ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(responseRicercaPuntuale.getCapitoloEntrataPrevisione().getUid());
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il servizio per la ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataPrevisioneResponse responseRicercaDettaglio = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(requestRicercaDettaglio);
		logServiceResponse(responseRicercaDettaglio);
		//SIAC-6881 - TEST
		// FIXME responseRicercaDettaglio.getCapitoloEntrataPrevisione().setComponentiCapitolo(TestComponentiCapitoloModel.lcc);
		// Imposto il capitolo nel model
		model.setCapitoloEntrataPrevisione(responseRicercaDettaglio.getCapitoloEntrataPrevisione());
		
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
		RicercaPuntualeCapitoloEntrataPrevisioneResponse responseRicercaPuntuale = ricercaSinteticaCapitolo();
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
		RicercaDettaglioCapitoloEntrataPrevisioneResponse res = ricercaDettaglioCapitolo(responseRicercaPuntuale.getCapitoloEntrataPrevisione().getUid());
	
		CapitoloEntrataPrevisione cup = res.getCapitoloEntrataPrevisione();
		StrutturaAmministrativoContabile sac = (cup.getStrutturaAmministrativoContabile() !=null) ? cup.getStrutturaAmministrativoContabile(): null;	

		List<StrutturaAmministrativoContabile> struttureAccount = sessionHandler.getAccount().getStruttureAmministrativeContabili();
		

		boolean isSacAfferente = sacAfferente(sac, struttureAccount, ricercaDettaglioVariazione(uidVar).getVariazioneImportoCapitolo().getDirezioneProponente().getCodice());

		//SE PRIMO CAPITOLO LO FACCIO ASSOCIARE, CONTROLLANDO SAC E FONDI REGIONALI						
		if(isSacAfferente){ 			
			model.setCapitoloEntrataPrevisione(cup);
			return SUCCESS;							
		}else{
			responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
			addErrori(responseRicercaPuntuale);
			return SUCCESS;							
		}					

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
			checkCapitolo(model.getCapitoloEntrataPrevisione()) ||
				// Controllo i classificatori
			checkPresenzaIdEntita(model.getTitoloEntrata()) ||
			checkPresenzaIdEntita(model.getTipologiaTitolo()) ||
			checkPresenzaIdEntita(model.getCategoriaTipologiaTitolo()) ||
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
		CapitoloEntrataPrevisione capitoloDaValidare = model.getCapitoloEntrataPrevisione();
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
		caricaListaClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		return SUCCESS;
	}
	/**
	 * Carica la lista dei classificatori gerarchici.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassificatoriGerarchici(){
		caricaListaClassificatoriGerarchici(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		return SUCCESS;
	}
	/*SIAC-6884*/
	/*Metodi di appoggio per filtro*/
	/*Ricerca sintetica del capitolo che si vuole cercare*/
	protected RicercaPuntualeCapitoloEntrataPrevisioneResponse ricercaSinteticaCapitolo (){
		final String methodName = "ricercaSinteticaCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca puntuale");
		RicercaPuntualeCapitoloEntrataPrevisione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloEntrataPrevisione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		return capitoloEntrataPrevisioneService.ricercaPuntualeCapitoloEntrataPrevisione(requestRicercaPuntuale);
	}
	
	/*Ricerca sintetica del capitolo che si vuole cercare*/
	protected RicercaDettaglioCapitoloEntrataPrevisioneResponse ricercaDettaglioCapitolo (int idCapitolo){
		final String methodName = "ricercaDettaglioCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca");
		RicercaDettaglioCapitoloEntrataPrevisione req = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione(idCapitolo);
		logServiceRequest(req);
		log.debug(methodName, "Richiamo il servizio per la ricerca");
		return capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(req);
	}
	
	private RicercaDettaglioAnagraficaVariazioneBilancioResponse ricercaDettaglioVariazione(int uidVar) {
		final String methodName ="ricercaDettaglioVariazione";
		RicercaDettaglioAnagraficaVariazioneBilancio request = model.creaRequestRicercaDettaglioAnagraficaVariazioneBilancio(uidVar);
		logServiceRequest(request);
		log.debug(methodName, "Invocazione del servizio di ricerca");		
		return variazioneDiBilancioService.ricercaDettaglioAnagraficaVariazioneBilancio(request);	

	}
	
//	private RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettaglioPrimoCapitoloNellaVariazione(int uidVar, String tipoCapitolo) {
//		final String methodName ="ricercaPrimoCapitoloNellaVariazione";
//		RicercaDettagliVariazioneImportoCapitoloNellaVariazione requestDettagli = model
//				.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione(uidVar, tipoCapitolo);
//		log.debug(methodName, "Invocazione del servizio di ricerca");	
//		return variazioneDiBilancioService
//				.ricercaDettagloVariazionePrimoCapitoloNellaVariazione(requestDettagli); //se response dettagli non ha trovato nessuna entity, posso consentire di aggiungere un capitolo che abbia la stessa direzione proponente ricavata dall'anagrafica della variazione
//	}
	
	private RicercaPuntualeCapitoloEntrataPrevisioneResponse setNullInResponse(
			RicercaPuntualeCapitoloEntrataPrevisioneResponse responseRicercaPuntuale) {
		
			CapitoloEntrataPrevisione cupNull = null;
			responseRicercaPuntuale.setCapitoloEntrataPrevisione(cupNull);
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
