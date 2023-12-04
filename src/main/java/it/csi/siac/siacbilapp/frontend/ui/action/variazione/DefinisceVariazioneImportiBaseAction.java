/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.DefinisceVariazioneImportiModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReportResponse;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincronaResponse;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.DettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;


/**
 * Classe di Action per la gestione della consultazione della Variazione Importi.
 * 
 * @author Daniele Argiolas
 * @author Elisa Chiari
 * @version 1.0.0 05/11/2013
 *
 */
public abstract class DefinisceVariazioneImportiBaseAction extends DefinisceVariazioneBaseAction<DefinisceVariazioneImportiModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	

	@Autowired private transient OperazioneAsincronaService operazioneAsincronaService;
	
	@Override
	public void prepareExecute() throws Exception{
		setModel(null);
		super.prepare();
	}
	
	/**
	 * Controlla se la gestione sia delle UEB o meno.
	 * @return la stringa <code>true</code> se la gestione &eacute; con UEB; la stringa <code>false</code> altrimenti
	 */
	public String chooseGestioneUEB() {
		return Boolean.toString(model.isGestioneUEB());
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		log.debug(methodName, "Ottengo l'azione richiesta dalla sessione");
		AzioneRichiesta azioneRichiesta = sessionHandler.getAzioneRichiesta();
		log.debug(methodName, "Injetto le variabili del processo");
		model.impostaDatiNelModel(azioneRichiesta);
//		model.injettaVariabiliProcesso(azioneRichiesta);

		log.debug(methodName, "Creo la request per la ricerca del capitolo");
		if(model.getUidVariazione() == null) {
			log.debug(methodName, "Errore: non presente uid variazione da consultare");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		RicercaDettaglioAnagraficaVariazioneBilancio request = model.creaRequestRicercaDettaglioAnagraficaVariazioneBilancio();
		logServiceRequest(request);

		log.debug(methodName, "Invocazione del servizio di ricerca");
		
		RicercaDettaglioAnagraficaVariazioneBilancioResponse response = variazioneDiBilancioService.ricercaDettaglioAnagraficaVariazioneBilancio(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAnagraficaVariazioneBilancio.class, response));
			addErrori(response);
			throwExceptionFromErrori(model.getErrori());
		}

		//SIAC-7555
		controllaStatoOperativoVariazione(response.getVariazioneImportoCapitolo());
		//
		
		log.debug(methodName, "Impostazione dei dati nel model e in sessione");
		model.impostaDatiDaResponseESessione(response, sessionHandler);
		log.debug(methodName, "Dati impostati nel model e nella sessione");
		
		return SUCCESS;
	}
	
	/**
	 * Carica la lista delle UEB nel model, per la visualizzazione sulla JSP.
	 * 
	 * @return SUCCESS
	 */
	@SkipValidation
	public String caricaListaUEB() {
		final String methodName = "caricaListaUEB";
		log.debug(methodName, "Caricamento metodo");

		Integer idx = model.getIdxCapitoloSelezionato();
		List<ElementoCapitoloVariazione> listaVariazioniCollapsed = sessionHandler.getParametro(BilSessionParameter.RISULTATI_CONSULTA_DETTAGLIO_VARIAZIONI);
		
		model.setListaUEB(listaVariazioniCollapsed.get(idx).getListaSottoElementi());

		return SUCCESS;
	}
	
	/**
	 * Chiama il servizio di definizione della variazione
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String definisciVariazione() {
		final String methodName = "definisciVariazione";
	
		cleanErrori();
		
		if(!isControllaCoerenzaApplicazioneFase(model.getApplicazioneVariazione())) {
			log.debug(methodName, "Mancanza di coerenza tra fase di bilancio e applicazione della variazione");
			addErrore(ErroreBil.DEFINIZIONE_NON_POSSIBILE_PERCHE_FASE_DI_BILANCIO_INCONGRUENTE.getErrore("Definizione"));
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		
		VariazioneImportoCapitolo variazione = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONI, VariazioneImportoCapitolo.class);
		
		model.setVariazioneImportoCapitolo(variazione);
		log.debug(methodName, "Creazione request per la definizione");
		DefinisceAnagraficaVariazioneBilancio request = model.creaRequestDefinisceAnagraficaVariazioneBilancio();
		AsyncServiceResponse response = variazioneDiBilancioService.definisceAnagraficaVariazioneBilancioAsync(wrapRequestToAsync(request));
			
		log.debug(methodName, "Operazione asincrona avviata. IdOperazioneAsincrona: "+ response.getIdOperazioneAsincrona());
		
		if (response.hasErrori()) {
			log.debug(methodName, "Invocazione servizio aggiornaAnagraficaVariazioneBilancioAsync terminata con fallimento");
			addErrori(response);
			return INPUT;
		}
		//SIAC-8261
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione di definizione e' stata correttamente avviata. &Egrave; possibile rimanere sulla pagina oppure tornare alla home: il risultato sar&agrave; disponibile dal cruscotto delle operazioni asincrone o ricercando la variazione."));

		model.setIdOperazioneAsincrona(response.getIdOperazioneAsincrona());

		return SUCCESS;
	}
	
	/**
	 * Metodo utilizzato per controllare se il servizio asincrono di definizione abbia o meno terminato l'esecuzione.
	 * In caso di risposta affermativa, vengono impostati gli eventuali errori, messaggi e informazioni.
	 * @return il risultato dell'invocazione (SUCCESS)
	 */
	public String definisciAsyncResponse(){
		String methodName = "definisciAsyncResponse";
		
		model.setIsAsyncResponsePresent(Boolean.FALSE);
		Integer idOperazioneAsincrona = model.getIdOperazioneAsincrona();
		
		GetDettaglioOperazioneAsincrona reqDOA = new GetDettaglioOperazioneAsincrona();
		reqDOA.setOpAsincId(idOperazioneAsincrona);
		reqDOA.setCodice("SERVICE_RESPONSE");
		reqDOA.setParametriPaginazione(new ParametriPaginazione(0, Integer.MAX_VALUE));
		reqDOA.setRichiedente(model.getRichiedente());
		GetDettaglioOperazioneAsincronaResponse resDOA = operazioneAsincronaService.getDettaglioOperazioneAsincrona(reqDOA);
		
		if (resDOA.hasErrori()) {
			log.debug(methodName, "Invocazione servizio getDettaglioOperazioneAsincrona terminata con fallimento");
			addErrori(resDOA);
			return INPUT;
		}
		
		AggiornaAnagraficaVariazioneBilancioResponse res = null;
		
		if(resDOA.getElencoPaginato()!=null){
			for(DettaglioOperazioneAsincrona doa : resDOA.getElencoPaginato()){
				res = JAXB.unmarshal(new StringReader(doa.getServiceResponse()), AggiornaAnagraficaVariazioneBilancioResponse.class);
				break;
			}
		}
		
		if(res==null){
			log.debug(methodName, "Il servizio asincrono non ha ancora risposto. Continuare il polling.");
			return SUCCESS;
		}
		
		log.debug(methodName, "Il servizio asincrono ha ancora risposto.");
		model.setIsAsyncResponsePresent(Boolean.TRUE);
		
		//Il servizio asincrono ha dato risposta.
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}
		
		log.debug(methodName, "Invocazione terminata con successo");
		cleanErroriMessaggiInformazioni();
	    addInformazione(new Informazione("COR_INF_0006", "Operazione di definizione effettuata correttamente."));
	
	    return SUCCESS;
	}
	
	/**
	 * Metodo per ottenere la lista dei capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliNellaVariazione() {

		final String methodName = "leggiCapitoliNellaVariazione";

		log.debug(methodName, "Richiamo il webService di ricercaDettagliVariazioneImportoCapitoloNellaVariazione");

		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione();
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse response = variazioneDiBilancioService.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(request);
		
		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato di ricerca per i dati forniti");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			
			return SUCCESS;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE, response.getListaDettaglioVariazioneImportoCapitolo());
		
		model.setTotaleStanziamentiCassaEntrata(response.getTotaleStanziamentiCassaEntrata());
		model.setTotaleStanziamentiCassaSpesa(response.getTotaleStanziamentiCassaSpesa());
		model.setTotaleStanziamentiEntrata(response.getTotaleStanziamentiEntrata());
		model.setTotaleStanziamentiSpesa(response.getTotaleStanziamentiSpesa());
		model.setTotaleStanziamentiResiduiEntrata(response.getTotaleStanziamentiResiduiEntrata());
		model.setTotaleStanziamentiResiduiSpesa(response.getTotaleStanziamentiResiduiSpesa());
		
		//SIAC-6883
		model.setTotaleStanziamentiEntrata1(response.getTotaleStanziamentiEntrata1());
		model.setTotaleStanziamentiEntrata2(response.getTotaleStanziamentiEntrata2());
		
		model.setTotaleStanziamentiSpesa1(response.getTotaleStanziamentiSpesa1());
		model.setTotaleStanziamentiSpesa2(response.getTotaleStanziamentiSpesa2());

		return SUCCESS;
	}
	
	// SIAC-5016
	/**
	 * Preparazione per il metodo {@link #download()}
	 */
	public void prepareDownload() {
		model.setIsXlsx(null);
		model.setContentType(null);
		model.setContentLength(null);
		model.setFileName(null);
		model.setInputStream(null);
	}
	
	/**
	 * Download dell'excel dei dati della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String download() {
		final String methodName = "download";
		
		VariazioneBilancioExcelReport req = model.creaRequestStampaExcelVariazioneDiBilancio();
		VariazioneBilancioExcelReportResponse res = variazioneDiBilancioService.variazioneBilancioExcelReport(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, "Errori nel reperimento dell'excel della variazione");
			return INPUT;
		}
		
		byte[] bytes = res.getReport();
		model.setContentType(res.getContentType() == null ? null : res.getContentType().getMimeType());
		model.setContentLength(Long.valueOf(bytes.length));
		model.setFileName("esportazioneVariazione" + model.getAnnoCompetenza() + "_" + model.getNumeroVariazione() + "." + res.getExtension());
		model.setInputStream(new ByteArrayInputStream(bytes));
		
		return SUCCESS;
	}
	
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapUscitaPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaPrevisione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapUscitaGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloUscitaGestione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapEntrataPrevisione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataPrevisione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	/**
	 * Effettua ricerca nella variazione cap entrata previsione.
	 *
	 * @return the string
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaNellaVariazioneCapEntrataGestione() {
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneCapitoloEntrataGestione();
		return effettuaRicercaCapitoloNellaVariazione(request);
	}
	
	
	/**
	 * Effettua ricerca capitolo nella variazione.
	 *
	 * @param req la request da inviare
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	//SIAC-5016
	public String effettuaRicercaCapitoloNellaVariazione(RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione req) {
		final String methodName = "effettuaRicercaCapitoloNellaVariazione";
		RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse res = variazioneDiBilancioService.ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori durante l'esecuzione del servizio di ricerca singolo dettaglio variazione importo capitolo nella variazione");
			addErrori(res);
			return INPUT;
		}
		ElementoCapitoloVariazione elementoCapitoloVariazioneTrovato = ElementoCapitoloVariazioneFactory.getInstanceFromSingoloDettaglio(res.getDettaglioVariazioneImportoCapitolo(), model.isGestioneUEB());
		model.setElementoCapitoloVariazioneTrovatoNellaVariazione(elementoCapitoloVariazioneTrovato);
		return SUCCESS;
	}

}
