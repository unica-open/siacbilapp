/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.RicercaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.business.utility.helper.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Action per la gestione della Ricerca per il Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino, Luciano Gallo
 * @version 1.1.0 09/07/2013 
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCapitoloUscitaPrevisioneAction extends CapitoloUscitaAction<RicercaCapitoloUscitaPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5221371874273974078L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		if(almenoUnaCodificaGiaCaricata()){
			caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		} else {
			caricaListaCodificheDiBase(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		}
		
		log.debugEnd(methodName, "");
	}
	
	@Override
	protected void caricaListaCodificheDiBase(BilConstants codiceTipoElementoBilancio) {
		super.caricaListaCodificheDiBase(codiceTipoElementoBilancio);
		caricaListaClassificatoriGenerici(codiceTipoElementoBilancio);
	}
	
	@Override
	protected boolean almenoUnaCodificaGiaCaricata() {
		List<Missione> listaMissione = sessionHandler.getParametro(BilSessionParameter.LISTA_MISSIONE);
		return listaMissione != null && !listaMissione.isEmpty();
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
	 */
	public String ricercaConOperazioniCDU() {
		final String methodName="ricercaConOperazioniCDU";
		log.debugStart(methodName, "Chiamata alla action di ricerca");
		
		model.caricaClassificatoriDaSessione(sessionHandler);
				
		RicercaSinteticaCapitoloUscitaPrevisione request = model.creaRequestRicercaSinteticaCapitoloUscitaPrevisione();
		logServiceRequest(request);
		
		//
		// Richiama il servizio di ricercaSinteticaCapitoloUscitaPrevisione()
		//
				
		log.debug(methodName, "Richiamo il WebService di Ricerca Sintetica");
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(request);
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
			addMessaggio(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		
		log.debug(methodName, "Sono presenti delle liste nella risposta");
		
		log.debug(methodName, "Pulisco la sessione");
		// Ricordare pulire correttamente la sessione 
		sessionHandler.cleanAllSafely();

		// Mette in sessione :
		// La lista dei risultati : per poter visualizzare i dati trovati nella action di risultatiRicerca 
		// La request usata per chiamare il servizio di ricerca : per poterla riusare allo scopo di reperire un nuovo blocco di risultati
		log.debug(methodName, "Imposto in sessione la request");
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_CAPITOLO, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE, response.getCapitoli());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE_IMPORTI, response.getTotaleImporti());

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
		boolean valido = validaRicercaComeDatoAggiuntivo();
		if(!valido) {
			log.debug(methodName, "Errore nella validazione dei campi");
			// Posso ritornare SUCCESS, in quanto tale risultato contiene la serializzazione degli errori
			return SUCCESS;
		}
		log.debug(methodName, "Creazione della request");
		RicercaSinteticaCapitoloUscitaPrevisione request = model.creaRequestRicercaSinteticaCapitoloUscitaPrevisione();
		// Impostazione dello stato VALIDO per il capitolo
		request.getRicercaSinteticaCapitoloUPrev().setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca sintetica");
		RicercaSinteticaCapitoloUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.ricercaSinteticaCapitoloUscitaPrevisione(request);
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
		for(CapitoloUscitaPrevisione capitoloUscitaPrevisione : response.getCapitoli()) {
			listaCapitoli.add(ElementoCapitoloFactory.getInstance(capitoloUscitaPrevisione, false, model.isGestioneUEB()));
		}
		// Injetto la lista dei wrapper
		model.setListaCapitoli(listaCapitoli);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo di ricerca di dettaglio per il Capitolo di Uscita Previsione.
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
		RicercaPuntualeCapitoloUscitaPrevisione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaPrevisione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		RicercaPuntualeCapitoloUscitaPrevisioneResponse responseRicercaPuntuale = capitoloUscitaPrevisioneService.ricercaPuntualeCapitoloUscitaPrevisione(requestRicercaPuntuale);
		logServiceResponse(responseRicercaPuntuale);

		if(responseRicercaPuntuale.hasErrori()) {
			log.debug(methodName, "La ricerca non ha avuto esito positivo");
			addErrori(responseRicercaPuntuale);
			// Ritorno comunque SUCCESS sì da ottenere gli errori nella risposta JSON
			return SUCCESS;
		}
		
		// Effettuo la ricerca di dettaglio a partire dall'uid ottenuto dalla ricerca puntuale
		log.debug(methodName, "Creazione della request per la ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione(responseRicercaPuntuale.getCapitoloUscitaPrevisione().getUid());
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il servizio per la ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisioneResponse responseRicercaDettaglio = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(requestRicercaDettaglio);
		logServiceResponse(responseRicercaDettaglio);

		model.setCapitoloUscitaPrevisione(responseRicercaDettaglio.getCapitoloUscitaPrevisione());
		return SUCCESS;
	}
	
	//SIAC-6884 filtro capitoli per variazione //WIP 12/12/2019
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
		RicercaPuntualeCapitoloUscitaPrevisioneResponse responseRicercaPuntuale = ricercaSinteticaCapitolo();
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
		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = ricercaDettaglioCapitolo(responseRicercaPuntuale.getCapitoloUscitaPrevisione().getUid());

			
		//SIAC-6884-RicercaCapitolo da Variazione ---> Informazioni relativi al capitolo che si vuole associare	da confrontare con quelli gia associati	
		CapitoloUscitaPrevisione cupDaAssociare = res.getCapitoloUscitaPrevisione();
		TipoFinanziamento tipoFinanziamentoDaAssociare = (cupDaAssociare.getTipoFinanziamento() != null) ? cupDaAssociare.getTipoFinanziamento() : null;
		StrutturaAmministrativoContabile sacDaAssociare = (cupDaAssociare.getStrutturaAmministrativoContabile() !=null) ? cupDaAssociare.getStrutturaAmministrativoContabile(): null;
		
		/*Servirà per enti diversi da regione piemonte*/
		TitoloSpesa titoloSpesaDaAssociare = (cupDaAssociare.getTitoloSpesa() != null) ? cupDaAssociare.getTitoloSpesa(): null;
		Missione missioneDaAssociare = cupDaAssociare.getMissione() != null ? cupDaAssociare.getMissione() : null ;
		Macroaggregato macroaggregatoDaAssociare = cupDaAssociare.getMacroaggregato() != null ? cupDaAssociare.getMacroaggregato() : null;
		/* ----  */
		
		
		boolean isFondiRegionali = ( (tipoFinanziamentoDaAssociare != null) && tipoFinanziamentoDaAssociare.getCodice().equals("R")) ? true : false;
	
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse responseDettagli = ricercaDettaglioPrimoCapitoloNellaVariazione(uidVar, BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE.getConstant());
		
		List<StrutturaAmministrativoContabile> struttureAccount = sessionHandler.getAccount().getStruttureAmministrativeContabili();
		
		boolean isSacAfferente = sacAfferente(sacDaAssociare, struttureAccount, ricercaDettaglioVariazione(uidVar).getVariazioneImportoCapitolo().getDirezioneProponente().getCodice());
		
		boolean firstChapter = checkFirstChapter(responseDettagli.getListaDettaglioVariazioneImportoCapitolo());

			
		//SE PRIMO CAPITOLO LO FACCIO ASSOCIARE, CONTROLLANDO SAC E FONDI REGIONALI
		if(model.isRegionePiemonte()){	
			if(isSacAfferente && isFondiRegionali){ //stessa sac e fondi regionali, mostra il capitolo da aggiungere				
				model.setCapitoloUscitaPrevisione(cupDaAssociare);
				return SUCCESS;							
			}else{
				responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
				addErrori(responseRicercaPuntuale);
				return SUCCESS;							
			}					
		}else{
			
			if(firstChapter){
				if(isSacAfferente){ //stessa sac e fondi regionali, mostra il capitolo da aggiungere				
					model.setCapitoloUscitaPrevisione(cupDaAssociare);
					return SUCCESS;							
				}else{
					responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
					addErrori(responseRicercaPuntuale);
					return SUCCESS;							
				}

			}else if(!firstChapter){
					
				int uidPrimoCapitoloInserito = responseDettagli.getListaDettaglioVariazioneImportoCapitolo().get(0).getCapitolo().getUid();
				RicercaDettaglioCapitoloUscitaPrevisioneResponse resPrimoCapitolo = ricercaDettaglioCapitolo(uidPrimoCapitoloInserito);
				CapitoloUscitaPrevisione capAssociato = resPrimoCapitolo.getCapitoloUscitaPrevisione();
				//StrutturaAmministrativoContabile primoCapitoloSac = (capAssociato.getStrutturaAmministrativoContabile() !=null) ? capAssociato.getStrutturaAmministrativoContabile(): null;
				TitoloSpesa titoloSpesaPrimoCapitolo = (capAssociato.getTitoloSpesa() != null) ? capAssociato.getTitoloSpesa(): null;
				Missione missionePrimoCapitolo = capAssociato.getMissione() != null ? capAssociato.getMissione() : null ;			
				Macroaggregato macroaggregatoPrimoCapitolo = (capAssociato.getMacroaggregato() != null) ? capAssociato.getMacroaggregato() : null;
				
				if(isSacAfferente &&
								titoloSpesaPrimoCapitolo != null && titoloSpesaDaAssociare != null && titoloSpesaPrimoCapitolo.getCodice()!=null && titoloSpesaDaAssociare.getCodice()!=null
								&& titoloSpesaPrimoCapitolo.getCodice().equals(titoloSpesaDaAssociare.getCodice()) && 
								missionePrimoCapitolo !=null && missioneDaAssociare !=null && 
										missionePrimoCapitolo.getCodice()!=null && missioneDaAssociare.getCodice()!=null &&
												missionePrimoCapitolo.getCodice().equals(missioneDaAssociare.getCodice()) &&
												macroaggregatoPrimoCapitolo !=null && macroaggregatoDaAssociare !=null && 
														macroaggregatoPrimoCapitolo.getCodice()!=null && macroaggregatoDaAssociare.getCodice()!=null &&
																macroaggregatoPrimoCapitolo.getCodice().equals(macroaggregatoDaAssociare.getCodice())){
					
					model.setCapitoloUscitaPrevisione(cupDaAssociare);
					return SUCCESS;
						
						
				}else{
						
					responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
					addErrori(responseRicercaPuntuale);
					return SUCCESS;
						
				}

			}else{
				responseRicercaPuntuale = setNullInResponse(responseRicercaPuntuale);
				addErrori(responseRicercaPuntuale);
				return SUCCESS;
			}
		}
				
	}
	
	
	
	
	/**
	 * Validate ricerca componente importi capitolo.
	 */
	public void validateRicercaComponenteImportiCapitolo() {
		checkCondition(model.getUidCapitoloPerRicercaComponenti() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave fisica capitolo per ricerca delle componenti"));
	}
	
	/**
	 * Ricerca degli importi capitolo per componente
	 *
	 * @return the string
	 */
	//SIAC-6881
	public String ricercaComponenteImportiCapitolo() {
		final String methodName="ricercaComponenteImportiCapitolo";
		RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
		RicercaComponenteImportiCapitoloResponse response =  componenteImportiCapitoloService.ricercaComponenteImportiCapitolo(req);
		if(response.hasErrori()) {
			log.info(methodName, "errori durante la chiamata al servizio di ricerca delle componenti del capitolo.");
			addErrori(response);
			return SUCCESS;
		}
		//COMPETENZA
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper
				.toComponentiImportiCapitoloPerAnno(response.getListaImportiCapitolo(),
						response.getImportiCapitoloResiduo(), response.getImportiCapitoloAnniSuccessivi());//ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnno(response.getListaImportiCapitolo());
		//SIAC-7227
		//passo la nuova lista al model con il match delle varie componenti
		if(response.getListaImportiCapitolo() != null & !response.getListaImportiCapitolo().isEmpty() &&  response.getListaImportiCapitolo().get(0) != null) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedente(importiComponentiCapitolo, response.getListaImportiCapitolo().get(0));
		}
		model.setImportiComponentiCapitolo(importiComponentiCapitolo);
		sessionHandler.setParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO_DA_RICERCA, importiComponentiCapitolo.isEmpty()? null : importiComponentiCapitolo);
		
		List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUP(response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(),competenzaComponenti);
		model.setCompetenzaComponenti(competenzaComponenti);
		//RESIDUI
		List<ImportiCapitoloPerComponente> residuiComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow( response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(), residuiComponenti);
		model.setResiduoComponenti(residuiComponenti);
		//CASSA
		List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow( response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(),cassaComponenti);
		model.setCassaComponenti(cassaComponenti);
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
				checkCapitolo(model.getCapitoloUscitaPrevisione()) ||
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
				checkPresenzaIdEntita(model.getCapitoloUscitaPrevisione().getCategoriaCapitolo()) ||
					// Validazione per l'atto di legge
				checkAttoDiLegge(model.getAttoDiLegge()) ||
					// Controllo i flags
				checkStringaValorizzata(model.getFlagFunzioniDelegate(), "FlagFunzioniDelegate") ||
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
		CapitoloUscitaPrevisione capitoloDaValidare = model.getCapitoloUscitaPrevisione();
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
		caricaListaClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		return SUCCESS;
	}
	
	/**
	 * Carica la lista dei classificatori gerarchici.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaListaClassificatoriGerarchici(){
		caricaListaClassificatoriGerarchici(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		return SUCCESS;
	}
	
	/*SIAC-6884*/
	/*Metodi di appoggio per filtro*/
	/*Ricerca sintetica del capitolo che si vuole cercare*/
	protected RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaSinteticaCapitolo (){
		final String methodName = "ricercaSinteticaCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca puntuale");
		RicercaPuntualeCapitoloUscitaPrevisione requestRicercaPuntuale = model.creaRequestRicercaPuntualeCapitoloUscitaPrevisione();
		logServiceRequest(requestRicercaPuntuale);
		log.debug(methodName, "Richiamo il servizio per la ricerca puntuale");
		return capitoloUscitaPrevisioneService.ricercaPuntualeCapitoloUscitaPrevisione(requestRicercaPuntuale);
	}
	
	/*Ricerca dettagliata del capitolo che si vuole cercare*/
	protected RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioCapitolo (int idCapitolo){
		final String methodName = "ricercaDettaglioCapitolo";
		log.debug(methodName, "Creazione della request per la ricerca");
		RicercaDettaglioCapitoloUscitaPrevisione req = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione(idCapitolo);
		logServiceRequest(req);
		log.debug(methodName, "Richiamo il servizio per la ricerca");
		return capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(req);
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
	
	private RicercaPuntualeCapitoloUscitaPrevisioneResponse setNullInResponse(
			RicercaPuntualeCapitoloUscitaPrevisioneResponse responseRicercaPuntuale) {
		
			CapitoloUscitaPrevisione cupNull = null;
			responseRicercaPuntuale.setCapitoloUscitaPrevisione(cupNull);
			//FIXME
			Errore errore = new Errore();
			errore.setCodice("COR_ERR_01");
			errore.setDescrizione("Capitolo non associabile alla Variazione");
			List<Errore> errori = new ArrayList<Errore>();
			errori.add(errore);
			responseRicercaPuntuale.addErrori(errori);	
			return responseRicercaPuntuale;
	}

	private boolean checkFirstChapter(ListaPaginata<DettaglioVariazioneImportoCapitolo> lista) {		
		if(lista.isEmpty()){
			return true;	
		}
		return false;
	}
	
	

	
	
}

