/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.RicercaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.business.utility.capitolo.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Action per la gestione della Ricerca per il Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 07/08/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCapitoloUscitaGestioneAction extends CapitoloUscitaAction<RicercaCapitoloUscitaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -22901851698660064L;
	
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		if(almenoUnaCodificaGiaCaricata()){
			caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		} else {
			caricaListaCodificheDiBase(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		}
		
		log.debugEnd(methodName, "");
	}
	
	@Override
	protected void caricaListaCodificheDiBase(BilConstants codiceTipoElementoBilancio) {
		super.caricaListaCodificheDiBase(codiceTipoElementoBilancio);
		caricaListaClassificatoriGenerici(codiceTipoElementoBilancio);
		caricaListaCodificheUscita();
	}
	
	@Override
	protected boolean almenoUnaCodificaGiaCaricata() {
		List<Missione> listaMissione = sessionHandler.getParametro(BilSessionParameter.LISTA_MISSIONE);
		return listaMissione != null && !listaMissione.isEmpty();
	}
	
	@Override
	protected List<TitoloSpesa> ottieniListaTitoloSpesaDaSessione() {
		BilSessionParameter bsp = model.getProgramma() != null && model.getProgramma().getUid() != 0
				? BilSessionParameter.LISTA_TITOLO_SPESA
				: BilSessionParameter.LISTA_TITOLO_SPESA_ORIGINALE;
		return sessionHandler.getParametro(bsp);
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca con operazioni del Capitolo di Uscita Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * @throws IllegalArgumentException nel caso in cui la creazione della Request fallisca
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName="ricercaConOperazioniCDU";
		
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		RicercaSinteticaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaCapitoloUscitaGestione(false);
		logServiceRequest(request);
			
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
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
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE_IMPORTI, response.getTotaleImporti());

		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca come dato aggiuntivo del Capitolo di Uscita Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * @throws IllegalArgumentException nel caso in cui la creazione della Request fallisca
	 */
	public String ricercaComeDatoAggiuntivo() {
		final String methodName = "ricercaComeDatoAggiuntivo";
		
		log.debug(methodName, "Validazione dei campi");
		CapitoloUscitaGestione capitoloUscitaGestione = model.getCapitoloUscitaGestione();
		if(capitoloUscitaGestione == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitolo"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo"));
		} else {
			if(capitoloUscitaGestione.getNumeroCapitolo() == null || capitoloUscitaGestione.getNumeroCapitolo().intValue() == 0) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitolo"));
			}
			if(capitoloUscitaGestione.getNumeroArticolo() == null) {
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
		RicercaSinteticaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaCapitoloUscitaGestione(true);
		logServiceRequest(request);
		
		// Invocazione del servizio
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		log.debug(methodName, "Richiamato il WebService di Ricerca Sintetica");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "La ricerca ha riportato degli errori");
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");
		if(response.getTotaleElementi() == 0) {
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
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca di dettaglio per il Capitolo di Uscita Gestione.
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
		RicercaPuntualeCapitoloUscitaGestione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaGestione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		RicercaPuntualeCapitoloUscitaGestioneResponse responseRicercaPuntuale = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(requestRicercaPuntuale);
		logServiceResponse(responseRicercaPuntuale);
		
		if(responseRicercaPuntuale.hasErrori()) {
			log.debug(methodName, "La ricerca non ha avuto esito positivo");
			addErrori(responseRicercaPuntuale);
			// Ritorno comunque SUCCESS sì da ottenere gli errori nella risposta JSON
			return SUCCESS;
		}
		
		
		// Effettuo la ricerca di dettaglio a partire dall'uid ottenuto dalla ricerca puntuale
		log.debug(methodName, "Creazione della request per la ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaGestione(responseRicercaPuntuale.getCapitoloUscitaGestione().getUid());
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il servizio per la ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestioneResponse responseRicercaDettaglio = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(requestRicercaDettaglio);
		logServiceResponse(responseRicercaDettaglio);
		//SIAC-6881 - TEST
		// FIXME 	responseRicercaDettaglio.getCapitoloUscita().setComponentiCapitolo(TestComponentiCapitoloModel.lcc);
		
		// Imposto il capitolo nel model
		model.setCapitoloUscitaGestione(responseRicercaDettaglio.getCapitoloUscita());
		return SUCCESS;
	}
	
	
	
	//SIAC-6884 filtro capitoli per variazione //WIP 13/12/2019
	public String ricercaDettaglioPerVariazione() {
		
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
		RicercaPuntualeCapitoloUscitaGestioneResponse responseRicercaPuntuale = ricercaSinteticaCapitolo();
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
		RicercaDettaglioCapitoloUscitaGestioneResponse res = ricercaDettaglioCapitolo(responseRicercaPuntuale.getCapitoloUscitaGestione().getUid());

					
		//SIAC-6884-RicercaCapitolo da Variazione ---> Informazioni relativi al capitolo che si vuole associare	da confrontare con quelli gia associati	
		CapitoloUscitaGestione cugDaAssociare = res.getCapitoloUscita();
		StrutturaAmministrativoContabile sacDaAssociare = (cugDaAssociare.getStrutturaAmministrativoContabile() !=null) ? cugDaAssociare.getStrutturaAmministrativoContabile(): null;
		TipoFinanziamento tipoFinanziamentoDaAssociare = (cugDaAssociare.getTipoFinanziamento() != null) ? cugDaAssociare.getTipoFinanziamento() : null;
		TitoloSpesa titoloSpesaDaAssociare = (cugDaAssociare.getTitoloSpesa() != null) ? cugDaAssociare.getTitoloSpesa(): null;
		Missione missioneDaAssociare = cugDaAssociare.getMissione() != null ? cugDaAssociare.getMissione() : null ;
		Macroaggregato macroaggregatoDaAssociare = cugDaAssociare.getMacroaggregato() != null ? cugDaAssociare.getMacroaggregato() : null;
		Programma programmaDaAssociare = cugDaAssociare.getProgramma() != null ? cugDaAssociare.getProgramma() : null;
		boolean isFondiRegionali = ( (tipoFinanziamentoDaAssociare != null) && tipoFinanziamentoDaAssociare.getCodice().equals("R")) ? true : false;
				
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse responseDettagliV = ricercaDettaglioPrimoCapitoloNellaVariazione(uidVar, BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		
		List<StrutturaAmministrativoContabile> struttureAccount = sessionHandler.getAccount().getStruttureAmministrativeContabili();
		
		
		boolean firstChapter = checkFirstChapter(responseDettagliV.getListaDettaglioVariazioneImportoCapitolo());
		
		boolean isSacAfferente = sacAfferente(sacDaAssociare, struttureAccount, ricercaDettaglioVariazione(uidVar).getVariazioneImportoCapitolo().getDirezioneProponente().getCodice());
		
		//SIAC-7629 (SIAC-7811) VG
		Boolean isDecentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
		String tipoVarCode="";
		RicercaDettaglioAnagraficaVariazioneBilancioResponse anagraficaVar = ricercaDettaglioVariazione(uidVar);
		if(anagraficaVar!= null && anagraficaVar.getVariazioneImportoCapitolo()!= null
				&& anagraficaVar.getVariazioneImportoCapitolo().getTipoVariazione()!=null){
			tipoVarCode = anagraficaVar.getVariazioneImportoCapitolo().getTipoVariazione().getCodice();
		}
		
		//boolean isSameSac = ( dirPropVar.getCodice().equals(sac.getCodice()) ? true : false);
		if(model.isRegionePiemonte()){
			if(firstChapter && isSacAfferente && isFondiRegionali){ //stessa sac e fondi regionali, mostra il capitolo da aggiungere				
				model.setCapitoloUscitaGestione(cugDaAssociare);
				return SUCCESS;
			}
			if (!firstChapter){
				//RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse responseDettagli = ricercaDettaglioPrimoCapitoloNellaVariazione(uidVar, BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
				int uidPrimoCapitoloInserito = responseDettagliV.getListaDettaglioVariazioneImportoCapitolo().get(0).getCapitolo().getUid();
				
				RicercaDettaglioCapitoloUscitaGestioneResponse resPrimoCapitolo = ricercaDettaglioCapitolo(uidPrimoCapitoloInserito);
				CapitoloUscitaGestione primoCapDaAssociare = resPrimoCapitolo.getCapitoloUscita();
				//StrutturaAmministrativoContabile primoCapitoloSac = (primoCapDaAssociare.getStrutturaAmministrativoContabile() !=null) ? primoCapDaAssociare.getStrutturaAmministrativoContabile(): null;
				TipoFinanziamento tipoFinanziamentoPrimoCapitolo = (primoCapDaAssociare.getTipoFinanziamento() != null) ? primoCapDaAssociare.getTipoFinanziamento() : null;
				TitoloSpesa titoloSpesaPrimoCapitolo = (primoCapDaAssociare.getTitoloSpesa() != null) ? primoCapDaAssociare.getTitoloSpesa(): null;
				Missione missionePrimoCapitolo= primoCapDaAssociare.getMissione() != null ? primoCapDaAssociare.getMissione() : null ;			
				Programma programmaPrimoCapitolo = (primoCapDaAssociare.getProgramma() != null) ? primoCapDaAssociare.getProgramma()  : null;
				//if(primoCapitoloSac != null && sacDaAssociare != null && primoCapitoloSac.getCodice() != null && sacDaAssociare.getCodice() != null 
				//&&  primoCapitoloSac.getCodice().equals(sacDaAssociare.getCodice()) &&

				
				/*
				 * BEFORE SIAC-7629 (SIAC-7811) VG
				 */
//				if(isFondiRegionali && isSacAfferente && 
//						tipoFinanziamentoPrimoCapitolo != null && tipoFinanziamentoDaAssociare != null && tipoFinanziamentoDaAssociare.getCodice() != null 
//						&& tipoFinanziamentoPrimoCapitolo.getCodice() != null && tipoFinanziamentoPrimoCapitolo.getCodice().equals(tipoFinanziamentoDaAssociare.getCodice()) &&
//								titoloSpesaPrimoCapitolo != null && titoloSpesaDaAssociare != null && titoloSpesaPrimoCapitolo.getCodice()!=null && titoloSpesaDaAssociare.getCodice()!=null
//								&& titoloSpesaPrimoCapitolo.getCodice().equals(titoloSpesaDaAssociare.getCodice()) && 
//								missioneDaAssociare !=null && missionePrimoCapitolo !=null && 
//										missionePrimoCapitolo.getCodice()!=null && missioneDaAssociare.getCodice()!=null &&
//												missionePrimoCapitolo.getCodice().equals(missioneDaAssociare.getCodice()) &&
//														programmaPrimoCapitolo !=null && programmaDaAssociare !=null && 
//														programmaPrimoCapitolo.getCodice()!=null && programmaDaAssociare.getCodice()!=null &&
//																programmaPrimoCapitolo.getCodice().equals(programmaDaAssociare.getCodice()) 
//						){
//					
//					model.setCapitoloUscitaGestione(cugDaAssociare);
//					return SUCCESS;
//				}else{
//					responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
//					addErrori(responseRicercaPuntuale);
//					return SUCCESS;
//				}
				
				/*
				 * SIAC-7629 (SIAC-7811) VG
				 * SE DECENTRATO E SE LA TIPOLOGIA DELLA VARIAZIONE E' VL
				 * IL CONTROLLO SU TITOLO, MISSIONE E PROGRAMMA NON VIENE EFFETTUATO 
				 */
				//CHECK FONDO REGIONALE E SAC
				if(isFondiRegionali && isSacAfferente){
					//SE DECENTRATO E VL CONTROLLIAMO MISSIONE TIPOLOGIA E PROGRAMMA
					if(!isDecentrato || !TipoVariazione.VARIAZIONE_DECENTRATA_LEGGE.getCodice().equals(tipoVarCode)){
						//CHECK SU MISSIONE TIPOLOGIA E PROGRAMMA
						if(tipoFinanziamentoPrimoCapitolo != null && tipoFinanziamentoDaAssociare != null && tipoFinanziamentoDaAssociare.getCodice() != null 
						&& tipoFinanziamentoPrimoCapitolo.getCodice() != null && tipoFinanziamentoPrimoCapitolo.getCodice().equals(tipoFinanziamentoDaAssociare.getCodice()) &&
								titoloSpesaPrimoCapitolo != null && titoloSpesaDaAssociare != null && titoloSpesaPrimoCapitolo.getCodice()!=null && titoloSpesaDaAssociare.getCodice()!=null
								&& titoloSpesaPrimoCapitolo.getCodice().equals(titoloSpesaDaAssociare.getCodice()) && 
								missioneDaAssociare !=null && missionePrimoCapitolo !=null && 
										missionePrimoCapitolo.getCodice()!=null && missioneDaAssociare.getCodice()!=null &&
												missionePrimoCapitolo.getCodice().equals(missioneDaAssociare.getCodice()) &&
														programmaPrimoCapitolo !=null && programmaDaAssociare !=null && 
														programmaPrimoCapitolo.getCodice()!=null && programmaDaAssociare.getCodice()!=null &&
																programmaPrimoCapitolo.getCodice().equals(programmaDaAssociare.getCodice()) 
						){
					
							model.setCapitoloUscitaGestione(cugDaAssociare);
							return SUCCESS;
						}
						responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
						addErrori(responseRicercaPuntuale);
						return SUCCESS;
					}
					//DECENTRATO E TIPOLOGIA VL
					model.setCapitoloUscitaGestione(cugDaAssociare);
					return SUCCESS;
				}
				//CHECK FONDO REGIONALE E SAC
				responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
				addErrori(responseRicercaPuntuale);
				return SUCCESS;
			}					
			responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
			addErrori(responseRicercaPuntuale);
			return SUCCESS;							

		}
		if(firstChapter && isSacAfferente){ //stessa sac e fondi regionali, mostra il capitolo da aggiungere				
			model.setCapitoloUscitaGestione(cugDaAssociare);
			return SUCCESS;
			
		}
		if (!firstChapter){
			//RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse responseDettagli = ricercaDettaglioPrimoCapitoloNellaVariazione(uidVar, BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			int uidPrimoCapitoloInserito = responseDettagliV.getListaDettaglioVariazioneImportoCapitolo().get(0).getCapitolo().getUid();
			RicercaDettaglioCapitoloUscitaGestioneResponse resPrimoCapitolo = ricercaDettaglioCapitolo(uidPrimoCapitoloInserito);
			CapitoloUscitaGestione primoCapAssociato = resPrimoCapitolo.getCapitoloUscita();
			//StrutturaAmministrativoContabile primoCapitoloAssociatoSac = (primoCapAssociato.getStrutturaAmministrativoContabile() !=null) ? primoCapAssociato.getStrutturaAmministrativoContabile(): null;
			TitoloSpesa titoloSpesaPrimoCapitoloAssociato = (primoCapAssociato.getTitoloSpesa() != null) ? primoCapAssociato.getTitoloSpesa(): null;
			Missione missionePrimoCapitoloAssociato= primoCapAssociato.getMissione() != null ? primoCapAssociato.getMissione() : null ;
			Macroaggregato macroaggregatoPrimoCapitoloAssociato = (primoCapAssociato.getMacroaggregato() != null) ? primoCapAssociato.getMacroaggregato() : null;
			Programma programmaPrimoCapitolo = (primoCapAssociato.getProgramma() != null) ? primoCapAssociato.getProgramma()  : null;
			
			/*
			 * BEFORE SIAC-7629 (SIAC-7811) VG
			 */
//				if(isSacAfferente &&
//								titoloSpesaPrimoCapitoloAssociato != null && titoloSpesaDaAssociare != null && titoloSpesaPrimoCapitoloAssociato.getCodice()!=null && titoloSpesaDaAssociare.getCodice()!=null
//								&& titoloSpesaPrimoCapitoloAssociato.getCodice().equals(titoloSpesaDaAssociare.getCodice()) && 
//								missionePrimoCapitoloAssociato !=null && missioneDaAssociare !=null && 
//										missionePrimoCapitoloAssociato.getCodice()!=null && missioneDaAssociare.getCodice()!=null &&
//												missionePrimoCapitoloAssociato.getCodice().equals(missioneDaAssociare.getCodice()) &&
//												macroaggregatoPrimoCapitoloAssociato !=null && macroaggregatoDaAssociare !=null && 
//														macroaggregatoPrimoCapitoloAssociato.getCodice()!=null && macroaggregatoDaAssociare.getCodice()!=null &&
//																macroaggregatoPrimoCapitoloAssociato.getCodice().equals(macroaggregatoDaAssociare.getCodice()) &&
//																programmaPrimoCapitolo !=null && programmaDaAssociare !=null && 
//																		programmaPrimoCapitolo.getCodice()!=null && programmaDaAssociare.getCodice()!=null &&
//																				programmaPrimoCapitolo.getCodice().equals(programmaDaAssociare.getCodice())){					
//					model.setCapitoloUscitaGestione(cugDaAssociare);
//					return SUCCESS;
//				}else{
//					responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
//					addErrori(responseRicercaPuntuale);
//					return SUCCESS;
//				}	
			
			
			/*
			 * SIAC-7629 (SIAC-7811) VG
			 * SE DECENTRATO E SE LA TIPOLOGIA DELLA VARIAZIONE E' VL
			 * IL CONTROLLO SU TITOLO, MISSIONE E PROGRAMMA NON VIENE EFFETTUATO 
			 */
			//CHECK SAC AFFERENTE
			if(isSacAfferente){
				//SE DECENTRATO E VL CONTROLLIAMO MISSIONE TIPOLOGIA E PROGRAMMA
				if(!isDecentrato || !TipoVariazione.VARIAZIONE_DECENTRATA_LEGGE.getCodice().equals(tipoVarCode)){
					//CHECK SU MISSIONE TIPOLOGIA E PROGRAMMA
					if(titoloSpesaPrimoCapitoloAssociato != null && titoloSpesaDaAssociare != null && titoloSpesaPrimoCapitoloAssociato.getCodice()!=null && titoloSpesaDaAssociare.getCodice()!=null
							&& titoloSpesaPrimoCapitoloAssociato.getCodice().equals(titoloSpesaDaAssociare.getCodice()) && 
							missionePrimoCapitoloAssociato !=null && missioneDaAssociare !=null && 
							missionePrimoCapitoloAssociato.getCodice()!=null && missioneDaAssociare.getCodice()!=null &&
							missionePrimoCapitoloAssociato.getCodice().equals(missioneDaAssociare.getCodice()) &&
							macroaggregatoPrimoCapitoloAssociato !=null && macroaggregatoDaAssociare !=null && 
							macroaggregatoPrimoCapitoloAssociato.getCodice()!=null && macroaggregatoDaAssociare.getCodice()!=null &&
							macroaggregatoPrimoCapitoloAssociato.getCodice().equals(macroaggregatoDaAssociare.getCodice()) &&
							programmaPrimoCapitolo !=null && programmaDaAssociare !=null && 
							programmaPrimoCapitolo.getCodice()!=null && programmaDaAssociare.getCodice()!=null &&
							programmaPrimoCapitolo.getCodice().equals(programmaDaAssociare.getCodice())){					
						model.setCapitoloUscitaGestione(cugDaAssociare);
						return SUCCESS;
					}
					responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
					addErrori(responseRicercaPuntuale);
					return SUCCESS;
					
				}
				//DECENTRATO E TIPOLOGIA VL
				model.setCapitoloUscitaGestione(cugDaAssociare);
				return SUCCESS;
			}
			responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
			addErrori(responseRicercaPuntuale);
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
				checkStringaValorizzata(model.getFlagFunzioniDelegate(), "FlagFunzioniDelegate") ||
				checkStringaValorizzata(model.getFlagRilevanteIva(), "FlagRilevanteIva") || 
				checkPresenzaIdEntita(model.getRisorsaAccantonata());
		
		checkCondition(formValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		log.debug(methodName, "Ricerca valida? " + formValido);
	}
	
	/**
	 * Controlla il SIOPE
	 */
	private void checkSiope() {
		final String methodName = "checkSiope";
		if(model.getSiopeSpesa() == null || model.getSiopeSpesa().getUid() != 0 || StringUtils.isBlank(model.getSiopeSpesa().getCodice())) {
			return;
		}
		try {
			SiopeSpesa ss = findClassificatoreByCodice(model.getSiopeSpesa(), TipologiaClassificatore.SIOPE_SPESA_I, "Siope");
			model.setSiopeSpesa(ss);
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
		CapitoloUscitaGestione capitoloDaValidare = model.getCapitoloUscitaGestione();
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
		caricaListaClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		return SUCCESS;
	}
	/**
	 * Carica la lista dei classificatori gerarchici.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassificatoriGerarchici(){
		caricaListaClassificatoriGerarchici(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		return SUCCESS;
	}
	
	//SIAC-6881
	public String ricercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
		
		RicercaComponenteImportiCapitoloResponse response =  componenteImportiCapitoloService.ricercaComponenteImportiCapitolo(req);
		
		//SIAC-8010 controllo formale per errori del servizio
		if(!response.getErrori().isEmpty()) {
			log.debug("ricercaComponenteImportiCapitolo", response.getErrori());
			addErrori(response.getErrori());
			return INPUT;
		}
		
		//IMPOSTARE OGGETTI COMPETENZA, CASSA, RESIDUO da mettere nel model (anche in un metodo privato qui dentro)
		//COMPETENZA
//		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnno(response.getListaImportiCapitolo());
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnno(response.getListaImportiCapitolo(),
				response.getImportiCapitoloResiduo(), response.getImportiCapitoloAnniSuccessivi());


		//SIAC-7227
//		if(response.getListaImportiCapitolo().get(0) != null) {
//			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedente(importiComponentiCapitolo, response.getListaImportiCapitolo().get(0));
//		}
//		//
		//SIAC-7349 - Start - SR210 - MR - 08/05/2020 ->devo clonare l'array per evitare il problema del riferimento dell'array in sessione
		if(!importiComponentiCapitolo.isEmpty()){
			List<ImportiCapitoloPerComponente> cloneImportiComponentiCapitolo1 = ReflectionUtil.deepClone(importiComponentiCapitolo);
			sessionHandler.setParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO_DA_RICERCA, cloneImportiComponentiCapitolo1);
		}else{
			//SIAC-7349 - Start - SR200 - MR - 08/05/2020 ->Sposto prima dell'aver calcolato gli anni precedenti e le componenti non associate
			sessionHandler.setParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO_DA_RICERCA, importiComponentiCapitolo.isEmpty()? null : importiComponentiCapitolo);
					//End - SIAC-7349			
		}
		//END SIAC-7349
		
		//SIAC-8010 aggiunti controlli formali
		if(response.getListaImportiCapitolo() != null && !response.getListaImportiCapitolo().isEmpty() && response.getListaImportiCapitolo().get(0) != null ) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedenteNew(importiComponentiCapitolo, response.getListaImportiCapitolo().get(0));
		}
		//SIAC-7349 - SR210 - MR - Start - 12/05/2020 - Nuovo metodo per mostrare componenti negli anni successivi senza stanziamento
		if(response.getListaImportiCapitoloAnniSuccessiviNoStanz() != null &&  !response.getListaImportiCapitoloAnniSuccessiviNoStanz().isEmpty()) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnniSuccNoStanz(importiComponentiCapitolo, response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		}
		//SIAC-7349 - End
		
		//SIAC-7349 - GS - Start - 21/07/2020 - Nuovo metodo per mostrare componenti nel triennio senza stanziamento
		if(response.getListaImportiCapitoloTriennioNoStanz() != null &&  !response.getListaImportiCapitoloTriennioNoStanz().isEmpty()) {

			int countDefault = 0;
			for (ImportiCapitoloPerComponente i : importiComponentiCapitolo) {
				if (i.isPropostaDefault()) 
					countDefault++;
			}  
			int addIndex = importiComponentiCapitolo.size() - countDefault;
		
			Integer annoEsercizio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerTriennioNoStanz(importiComponentiCapitolo, response.getListaImportiCapitoloTriennioNoStanz(), annoEsercizio, addIndex);
		}
		//SIAC-7349 - End
		
		importiComponentiCapitolo = impostaDatiInMaschera(importiComponentiCapitolo);	
		model.setImportiComponentiCapitolo(importiComponentiCapitolo);
		//sessionHandler.setParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO_DA_RICERCA, importiComponentiCapitolo.isEmpty()? null : importiComponentiCapitolo);
		
		List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUG(response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(),competenzaComponenti);
		//SIAC-7349 - SR210 - MR - Start 12.05.2020
		//per mantenere la retrocompatibilita nel sistema, rimuovo fuori dal metodo la disponibilita ad impegnare della competenza
		//in quanto non deve essere visualizzata nei casi di CAP-UG		
		for(int i=0; i<competenzaComponenti.size(); i++){
			if(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE.equals(competenzaComponenti.get(i).getTipoDettaglioComponenteImportiCapitolo())){
				competenzaComponenti.remove(competenzaComponenti.get(i));
			}
		}
		model.setCompetenzaComponenti(competenzaComponenti);

		//RESIDUI
		List<ImportiCapitoloPerComponente> residuiComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow( response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(), residuiComponenti);
		model.setResiduoComponenti(residuiComponenti);
		//CASSA
		List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow( response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(),cassaComponenti);
		model.setCassaComponenti(cassaComponenti);
		model.setTipologiaCapitolo("CAP-UG");
		return SUCCESS;
	}
	
	/*SIAC-6884*/
	/*Metodi di appoggio per filtro*/
	/*Ricerca sintetica del capitolo che si vuole cercare*/
	protected RicercaPuntualeCapitoloUscitaGestioneResponse ricercaSinteticaCapitolo (){
		final String methodName = "ricercaSinteticaCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca puntuale");
		RicercaPuntualeCapitoloUscitaGestione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaGestione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		return capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(requestRicercaPuntuale);
	}
	
	/*Ricerca dettagliata del capitolo che si vuole cercare*/
	protected RicercaDettaglioCapitoloUscitaGestioneResponse ricercaDettaglioCapitolo (int idCapitolo){
		final String methodName = "ricercaDettaglioCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca");
		RicercaDettaglioCapitoloUscitaGestione req = model.creaRequestRicercaDettaglioCapitoloUscitaGestione(idCapitolo);
		logServiceRequest(req);
		log.debug(methodName, "Richiamo il servizio per la ricerca");
		return capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(req);
	}
	
	private RicercaDettaglioAnagraficaVariazioneBilancioResponse ricercaDettaglioVariazione(int uidVar) {
		final String methodName ="ricercaDettaglioVariazione";
		RicercaDettaglioAnagraficaVariazioneBilancio request = model.creaRequestRicercaDettaglioAnagraficaVariazioneBilancio(uidVar);
		logServiceRequest(request);
		log.debug(methodName, "Invocazione del servizio di ricerca");		
		return variazioneDiBilancioService.ricercaDettaglioAnagraficaVariazioneBilancio(request);	

	}
	
	private RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettaglioPrimoCapitoloNellaVariazione(int uidVar, String tipoCapitolo) {
		final String methodName ="ricercaPrimoCapitoloNellaVariazione";
		RicercaDettagliVariazioneImportoCapitoloNellaVariazione requestDettagli = model
				.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione(uidVar, tipoCapitolo);
		log.debug(methodName, "Invocazione del servizio di ricerca");	
		return variazioneDiBilancioService
				.ricercaDettagloVariazionePrimoCapitoloNellaVariazione(requestDettagli); //se response dettagli non ha trovato nessuna entity, posso consentire di aggiungere un capitolo che abbia la stessa direzione proponente ricavata dall'anagrafica della variazione
	}
	
	private boolean checkFirstChapter(ListaPaginata<DettaglioVariazioneImportoCapitolo> lista) {
		
		if(lista.isEmpty()){
			return true;	
		}
		return false;
	}
	
	private RicercaPuntualeCapitoloUscitaGestioneResponse setNullInResponse(
			RicercaPuntualeCapitoloUscitaGestioneResponse responseRicercaPuntuale) {
		
			CapitoloUscitaGestione cupNull = null;
			responseRicercaPuntuale.setCapitoloUscitaGestione(cupNull);
			//FIXME
			Errore errore = new Errore();
			errore.setCodice("COR_ERR_01");
			errore.setDescrizione("Capitolo non associabile alla Variazione");
			List<Errore> errori = new ArrayList<Errore>();
			errori.add(errore);
			responseRicercaPuntuale.addErrori(errori);	
			return responseRicercaPuntuale;
	}
	
	//SIAC-7349 - MR - SR210 - 12.05.2020 Questo metodo permette di visualizzare correttamente
	//i valori di impegnato, disp Variare e disp a Impegnare di ogni singola componente
	//nella maschera di consultazione
	private List<ImportiCapitoloPerComponente> impostaDatiInMaschera(List<ImportiCapitoloPerComponente> importiComponentiCapitolo){
		int sizeImporti = importiComponentiCapitolo.size();
			
		List<Integer> indiciDettagliDispImpVar= new ArrayList<Integer>();
		List<ImportiCapitoloPerComponente> listaDisponibilitaImpegnare = new ArrayList<ImportiCapitoloPerComponente>();
		List<ImportiCapitoloPerComponente> listaDisponibilitaVariare = new ArrayList<ImportiCapitoloPerComponente>();
		for(int i=0; i<sizeImporti; i++){
			TipoDettaglioComponenteImportiCapitolo dettaglioComponente = importiComponentiCapitolo.get(i).getTipoDettaglioComponenteImportiCapitolo();
			if(dettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE)
					||dettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE)){
				indiciDettagliDispImpVar.add(i);							
			}
		}
		for(Integer index : indiciDettagliDispImpVar){
			TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponente = importiComponentiCapitolo.get(index).getTipoDettaglioComponenteImportiCapitolo();
				
			if(tipoDettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE)){
				listaDisponibilitaImpegnare.add(ReflectionUtil.deepClone(importiComponentiCapitolo.get(index)));				
			} else if(tipoDettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE)){
				listaDisponibilitaVariare.add(ReflectionUtil.deepClone(importiComponentiCapitolo.get(index)));				
			}
		}
		int j=indiciDettagliDispImpVar.size()-1;
		while (j>=0) {
			importiComponentiCapitolo.remove(importiComponentiCapitolo.get(indiciDettagliDispImpVar.get(j)));
			j--;			
		}
			//SIAC-7349 - Start - Sr210 MR 16/04/2020 Setting delle disponibilita impegnare nel model
		//model.setDisponibilitaImpegnareComponentiCapitolo(listaDisponibilitaImpegnare);
			//SIAC-7349
					
			//SIAC-7349 - Start - Sr210 MR 16/04/2020 Setting delle disponibilita a variare nel model
		//model.setDisponibilitaVariareComponentiCapitolo(listaDisponibilitaVariare);
			//SIAC-7349
			
		return importiComponentiCapitolo;

		}
	
	
//	private boolean sacAfferente(StrutturaAmministrativoContabile sac, List<StrutturaAmministrativoContabile> struttureAccount, String direzioneProponenteVarSel) {		
//		boolean isAssociato = false;		
//		for(StrutturaAmministrativoContabile sacUser : struttureAccount){
//			if(sac.getCodice().equals(sacUser.getCodice())){
//				isAssociato = true;
//				break;
//			}	
//		}
//		
//		
//		//SECONDA PARTE
//		
//		List<StrutturaAmministrativoContabile> listSac = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
//		StrutturaAmministrativoContabile direzioneProponenteVariazione = getSacDirezioneProponente(direzioneProponenteVarSel, listSac);
//		
//		
//		
//		boolean isFiglio = isSubStruttura(sac, direzioneProponenteVariazione);
//
//		if(isAssociato && isFiglio){
//			return true;
//		}
//		return false;
//	}

	
//	private List<Map<String, String>> getListaSettoriDirezione(StrutturaAmministrativoContabile direzioneProponenteVariazione, List<Map<String, String>> listaSettori) {
//		List<StrutturaAmministrativoContabile> figlioSub = direzioneProponenteVariazione.getSubStrutture();
//		
//		if(figlioSub != null && figlioSub.isEmpty()){
//			Map<String, String> mMap = new HashMap<String, String>();
//			mMap.put(direzioneProponenteVariazione.getTipoClassificatore().getCodice(), direzioneProponenteVariazione.getCodice());
//			listaSettori.add(mMap);
//		}else{
//			for(int i=0; i<figlioSub.size(); i++){
//				getListaSettoriDirezione(figlioSub.get(i), listaSettori);
//			}
//			
//			
//		}
//		return listaSettori;
//	}
	
	//Controlla se la sac si trova nella sottostruttura della proponente
//	private boolean isSubStruttura(StrutturaAmministrativoContabile sac,
//			StrutturaAmministrativoContabile direzioneProponenteVariazione) {
//		
//		List<Map<String, String>> listaSettori = new ArrayList<Map<String, String>>();
//		List<Map<String, String>> listaSettoriDaDirezione = getListaSettoriDirezione(direzioneProponenteVariazione, listaSettori);
//		
//		
//		String tipoClassificatoreDirezione = sac.getTipoClassificatore() != null ? sac.getTipoClassificatore().getCodice() : null;
//		String tipoDirezione = direzioneProponenteVariazione.getTipoClassificatore() != null ? direzioneProponenteVariazione.getTipoClassificatore().getCodice() : null;
//		if(direzioneProponenteVariazione != null && sac.getCodice().equals(direzioneProponenteVariazione.getCodice()) && tipoClassificatoreDirezione.equals(tipoDirezione)){
//			return true;
//		}
//		
//		for(Map<String, String> item: listaSettoriDaDirezione){
//			for(Map.Entry<String, String> entry: item.entrySet()){
//				if(sac.getCodice().equals(entry.getValue()) && sac.getTipoClassificatore().getCodice().equals(entry.getKey())){
//					return true;
//				}
//			}
//	
//		} 		
//		return false;
//	}
	
	
	
//	private StrutturaAmministrativoContabile getSacDirezioneProponente(String direzioneProponenteVarSel, List<StrutturaAmministrativoContabile> listaSac) {		
//		for(StrutturaAmministrativoContabile sacDaOttenere : listaSac){
//			if(sacDaOttenere.getCodice().equals(direzioneProponenteVarSel)){
//				return sacDaOttenere;
//			}
//		}
//		return null;
//	}
	
}
