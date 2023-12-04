/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException.Level;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazioneFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReportResponse;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincronaResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.DettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;


/**
 * Classe per la gestione dell'aggiornamento della variazione degli importi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 20/11/2013
 * 
 */
public abstract class AggiornaVariazioneImportiBaseAction extends AggiornaVariazioneBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2252533479814374690L;
	
	private static final String SUCCESS_FROM_INSERIMENTO = "success_from_inserimento";
	private static final String SUCCESS_DISABLED = "success_disabled";
	
	@Autowired private transient OperazioneAsincronaService operazioneAsincronaService;
	@Autowired protected TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;
	
	/**
	 * Preparazione per il metodo {@link #chooseGestioneUEB()}
	 */
	public void prepareChooseGestioneUEB() {
		try {
			// Riciclo il prepare execute
			super.prepareExecute();
		} catch(Exception e) {
			throw new GenericFrontEndMessagesException("Impossibile effettuare l'operazione selezionata", e, Level.ERROR);
		}
	}
	/**
	 * Controlla se la gestione sia delle UEB o meno.
	 * @return la stringa <code>true</code> se la gestione &eacute; con UEB; la stringa <code>false</code> altrimenti
	 */
	public String chooseGestioneUEB() {
		return Boolean.toString(model.isGestioneUEB());
	}
	
	//SIAC-6884
	public boolean isRegionePiemonte(Map<TipologiaGestioneLivelli, String> gestioneLivelli){
		for(Map.Entry<TipologiaGestioneLivelli, String> entry : gestioneLivelli.entrySet()){
			if(entry.getKey().equals(TipologiaGestioneLivelli.REGIONE_PIEMONTE_INS_CAP_VAR_DEC)){
				return true;
			}
		}		
		return false;
	}

	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		final String methodName = "prepareExecute";
		log.debug(methodName, "model settato a null");
		super.prepareExecute();
		Account account = sessionHandler.getAccount();
		Map<TipologiaGestioneLivelli, String> gestioneLivelli = account.getEnte().getGestioneLivelli();
		
		//SIAC-6884
		boolean regionePiemonte =  isRegionePiemonte(gestioneLivelli);
		boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
		
		model.setDecentrato(decentrato);
		
		model.setRegionePiemonte(regionePiemonte);
		
		log.debugStart(methodName, "");

		

		caricaListaTipiAtto();
		log.debug(methodName, "Lista tipi atto caricata. Carico il bilancio");

		caricaBilancio();
		log.debug(methodName, "Bilancio caricato");

		// Inizializzazione delle liste nei model
		prepareExecuteImpostaParametroInserimentoNuovaUEB();

		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE, null);
		
		log.debugEnd(methodName, "");
	}

	/**
	 * Preparazione per l'esecizione dell'impostazione parametro nella nuova UEB
	 */
	protected void prepareExecuteImpostaParametroInserimentoNuovaUEB() {
		// Vuoto in questa implementazione.
	}

	@Override
	@SkipValidation
	public String execute() throws Exception {
		final String methodName = "execute";
		super.execute();

		checkCasoDUsoApplicabile(model.getTitolo());

		
		if(!model.getFromInserimento()) {
			log.debug(methodName, "Ottengo l'azione richiesta dalla sessione");
			AzioneRichiesta azioneRichiesta = sessionHandler.getAzioneRichiesta();
			log.debug(methodName, "Injetto le variabili del processo");
			//injetta uidVariazione, idAttivita, invioOrganoAmministrativo, invioConsiglio
			model.impostaDatiNelModel(azioneRichiesta);
//			model.injettaVariabiliProcesso(azioneRichiesta);
		}
		

		log.debug(methodName, "Creo la request per la ricerca del capitolo");
		
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
		
		//SIAC-7530
		controllaStatoOperativoVariazione(response.getVariazioneImportoCapitolo());
		//

		model.popolaModel(response.getVariazioneImportoCapitolo());

		controllaAbilitazioneOperazioni();

		caricaListaTipoComponentiImportiCapitolo();
		
		setIdAzioneReportVariazioni();
		
		return SUCCESS;
	}
	
	/**
	 * Carica lista tipo componenti importi capitolo.
	 */
	private void caricaListaTipoComponentiImportiCapitolo() {
		model.setListaTipoComponente(null);
		RicercaSinteticaTipoComponenteImportiCapitolo request = model.creaRequestRicercaSinteticaTipoComponentiImportoCapitolo();
		RicercaSinteticaTipoComponenteImportiCapitoloResponse response = tipoComponenteImportiCapitoloService.ricercaSinteticaTipoComponenteImportiCapitolo(request);
		if(response.hasErrori()) {
			addErrori(response);
			return;
		}
		
		model.setListaTipoComponente(response.getListaTipoComponenteImportiCapitolo());
		model.setListaTipoComponenteDefault(filtraTipiComponentiDefault(model.getListaTipoComponente()));
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	@SkipValidation
	public String enterPage() {
		// Controllo di non essere rientrato dall'inserimento di una nuova UEB
		Boolean nuovaUEB = sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB);
		model.getSpecificaUEB().setRientroDaInserimentoNuovaUEB(nuovaUEB);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB, null);

		//SIAC-8332: controllare
		if(!model.getFromInserimento() && model.isDecentrato() && model.getIsDecentrata() && model.getDataChiusuraProposta() != null) {
			model.setAnnullaAbilitato(Boolean.FALSE);
			model.setConcludiAbilitato(Boolean.FALSE);
			model.setSalvaAbilitato(Boolean.FALSE);
			return SUCCESS_DISABLED;
		}
		
		return model.getFromInserimento()? SUCCESS_FROM_INSERIMENTO: SUCCESS;
	}

	@Override
	public String salva()  {
		final String methodName = "salva";

		if (!innerValidate(methodName)) {
			return INPUT;
		}
		
		AggiornaAnagraficaVariazioneBilancio req = model.creaRequestAggiornaAnagraficaVariazioneBilancioPerSalvataggio();
		
		
		
		AsyncServiceResponse res = variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancioAsync(wrapRequestToAsync(req));
		log.debug(methodName, "Operazione asincrona avviata. IdOperazioneAsincrona: "+ res.getIdOperazioneAsincrona());
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione servizio aggiornaAnagraficaVariazioneBilancioAsync terminata con fallimento");
			addErrori(res);
			return INPUT;
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione di salvataggio e' stata correttamente avviata. &Egrave; possibile rimanere sulla pagina oppure tornare alla home:  il risultato sar&agrave; disponibile dal cruscotto delle operazioni asincrone."));
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());
		return SUCCESS;
		
		
	}
	
	

	/**
	 * Metodo utilizzato per controllare se il servizio asincrono di salvataggio abbia o meno terminato l'esecuzione.
	 * In caso di risposta affermativa, vengono impostati gli eventuali errori, messaggi e informazioni.
	 * @return il risultato dell'invocazione (SUCCESS)
	 */
	public String salvaAsyncResponse(){
		String methodName = "salvaAsyncResponse";
		
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
			return INPUT;
		}
		
		log.debug(methodName, "Il servizio asincrono ha risposto.");
		model.setIsAsyncResponsePresent(Boolean.TRUE);
		
		
		//Il servizio asincrono ha dato risposta.
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		
		
		if (!Boolean.TRUE.equals(res.getIsQuadraturaCorretta())) {
			addMessaggio(ErroreBil.PROSECUZIONE_NONOSTANTE_QUADRATURA_NON_CORRETTA.getErrore());
		}
		//SIAC-6884
		if(!Boolean.TRUE.equals(res.getIsCodiciElenchiCongrui())){
			addMessaggio(ErroreBil.CODICE_ELENCHI_NON_CONFORME.getErrore());
		}
		if(!Boolean.TRUE.equals(res.getIsProvvedimentoPresenteDefinitivo())) {
			addMessaggio(ErroreBil.PROVVEDIMENTO_VARIAZIONE_NON_PRESENTE.getErrore("PEG"));
		}
		
		log.debug(methodName, "Invocazione terminata con successo");
	    addInformazione(new Informazione("COR_INF_0006", "Operazione di salvataggio effettuata correttamente."));
	    
		if(hasMessaggi()) {
			setMessaggiInSessionePerActionSuccessiva();
		}
		if(hasInformazioni()){
			setInformazioniInSessionePerActionSuccessiva();			
		}
		
		
		model.popolaStringaProvvedimento(res.getVariazioneImportoCapitolo().getAttoAmministrativo(), "provvedimento variazione di PEG");
		model.popolaStringaProvvedimentoAggiuntivo(res.getVariazioneImportoCapitolo().getAttoAmministrativoVariazioneBilancio());
		model.setSalvaAbilitato(Boolean.FALSE);
		return SUCCESS;
	}

	@Override
	public String annulla() throws ApplicationException {
		final String methodName = "annulla";
		
		AggiornaAnagraficaVariazioneBilancio req = null;
		
		//SIAC-8332
		req = model.creaRequestAggiornaAnagraficaVariazioneBilancioPerAnnullamento();
				
//		if(model.isDecentrato()){
//			req = model.creaRequestAggiornaAnagraficaVariazioneBilancioPerAnnullamentoDecentrato();
//		}else{
//			req = model.creaRequestAggiornaAnagraficaVariazioneBilancioPerAnnullamento();
//		}
		
		
		
		AsyncServiceResponse res = variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancioAsync(wrapRequestToAsync(req));
		log.debug(methodName, "Operazione asincrona avviata. IdOperazioneAsincrona: "+ res.getIdOperazioneAsincrona());
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione servizio aggiornaAnagraficaVariazioneBilancioAsync terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione di annullamento e' stata correttamente avviata."));
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());
		
		model.setAnnullaAbilitato(Boolean.FALSE);
		model.setConcludiAbilitato(Boolean.FALSE);
		model.setSalvaAbilitato(Boolean.FALSE);
		return SUCCESS;
		
		
	
		
	}
	
	
	/**
	  * Metodo Ajax utilizzato per controllare se il servizio asincrono di annullamento abbia o meno terminato l'esecuzione.
	 * In caso di risposta affermativa, vengono impostati gli eventuali errori, messaggi e informazioni.
	 * @return il risultato dell'invocazione (SUCCESS)
	 */
	public String annullaAsyncResponse(){
		String methodName = "annullaAsyncResponse";
		
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
			return SUCCESS;
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
		
		model.popolaStringaProvvedimento(res.getVariazioneImportoCapitolo().getAttoAmministrativo(), "provvedimento variazione di PEG");
		model.popolaStringaProvvedimentoAggiuntivo(res.getVariazioneImportoCapitolo().getAttoAmministrativoVariazioneBilancio());
		
		//Il servizio asincrono ha dato risposta.
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			setErroriInSessionePerActionSuccessiva();
			return SUCCESS;
		}
		
		if(res.getVariazioneImportoCapitolo()!=null){
			model.setStatoOperativoVariazioneDiBilancio(res.getVariazioneImportoCapitolo().getStatoOperativoVariazioneDiBilancio());
			model.setElementoStatoOperativoVariazione(ElementoStatoOperativoVariazioneFactory.getInstance(model.getEnte().getGestioneLivelli(),model.getStatoOperativoVariazioneDiBilancio()));
		}
		// Disabilito i pulsanti

		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		
		if(hasMessaggi()) {
			setMessaggiInSessionePerActionSuccessiva();
		}
		if(hasInformazioni()){
			setInformazioniInSessionePerActionSuccessiva();			
		}
		
		model.setAnnullaAbilitato(Boolean.FALSE);
		model.setConcludiAbilitato(Boolean.FALSE);
		model.setSalvaAbilitato(Boolean.FALSE);		
		
		return SUCCESS;
	}
	
	

	@Override
	public String concludi() throws ApplicationException {
		final String methodName = "concludi";

		if (!innerValidate(methodName)) {
			//return createSuffix(INPUT);
			return SUCCESS;
		}
		//3403
		
		AggiornaAnagraficaVariazioneBilancio req = model.creaRequestAggiornaAnagraficaVariazioneBilancioPerConclusione();
			
		
		AsyncServiceResponse res = variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancioAsync(wrapRequestToAsync(req));
		log.debug(methodName, "Operazione asincrona avviata. IdOperazioneAsincrona: "+ res.getIdOperazioneAsincrona());
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione servizio aggiornaAnagraficaVariazioneBilancioAsync terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}
		
//		model.addMessaggio(null);
		//SIAC-8261
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione di aggiornamento e' stata correttamente avviata. &Egrave; possibile rimanere sulla pagina oppure tornare alla home:  il risultato sar&agrave; disponibile dal cruscotto delle operazioni asincrone o ricercando la variazione."));
		
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());

		return SUCCESS;
	}
	
	/** * Metodo utilizzato per controllare se il servizio asincrono di conclusione abbia o meno terminato l'esecuzione.
	 * In caso di risposta affermativa, vengono impostati gli eventuali errori, messaggi e informazioni.
	 * @return il risultato dell'invocazione (SUCCESS)
	 */
	public String concludiAsyncResponse(){
		String methodName = "concludiAsyncResponse";
		
		model.setIsAsyncResponsePresent(Boolean.FALSE);
		Integer idOperazioneAsincrona = model.getIdOperazioneAsincrona();
		
		log.debug(methodName, "Richiamo il servizio di ricerca dettaglio della variazione asincrona per ottenere la response");
		
		GetDettaglioOperazioneAsincrona reqDOA = new GetDettaglioOperazioneAsincrona();
		reqDOA.setOpAsincId(idOperazioneAsincrona);
		reqDOA.setCodice("SERVICE_RESPONSE");
		reqDOA.setParametriPaginazione(new ParametriPaginazione(0, Integer.MAX_VALUE));
		reqDOA.setRichiedente(model.getRichiedente());
		GetDettaglioOperazioneAsincronaResponse resDOA = operazioneAsincronaService.getDettaglioOperazioneAsincrona(reqDOA);
		
		if (resDOA.hasErrori()) {
			log.debug(methodName, "Invocazione servizio getDettaglioOperazioneAsincrona terminata con fallimento");
			addErrori(resDOA);
			return SUCCESS;
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
		
		log.debug(methodName, "Il servizio asincrono ha risposto.");
		model.setIsAsyncResponsePresent(Boolean.TRUE);
		model.popolaStringaProvvedimento(res.getVariazioneImportoCapitolo().getAttoAmministrativo(), "provvedimento variazione di PEG");
		model.popolaStringaProvvedimentoAggiuntivo(res.getVariazioneImportoCapitolo().getAttoAmministrativoVariazioneBilancio());
		
		//Il servizio asincrono ha dato risposta.
		// Ottengo l'id dell'attività
		extractIdAttivitaIfNotNull(res.getIdTask());
		
		Boolean richiediConfermaQuadraturaCassa = res.verificatoErrore(ErroreBil.QUADRATURA_NON_CORRETTA.getCodice()) && Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento()) && Boolean.FALSE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa()) && StringUtils.isBlank(model.getSaltaCheckStanziamentoCassa()); //model.getSaltaCheckStanziamentoCassa()); 
		model.setRichiediConfermaQuadratura(richiediConfermaQuadraturaCassa);
		
		//SIAC-4737 e SIAC-3597
		boolean richiediConfermaMancanzaProvvedimentoVariazioneBilancio = res.isFallimento() && Boolean.FALSE.equals(res.getIsAttoAmministrativoVariazioneDiBilancioPresenteSeNecessario()) && !model.isSaltaCheckProvvedimentoVariazioneBilancio();
		model.setRichiediConfermaMancanzaProvvedimentoVariazioneBilancio(richiediConfermaMancanzaProvvedimentoVariazioneBilancio);
		
		if (res.isFallimento() || res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			//SIAC-3597			
			for(Errore e : res.getErrori()){
				if(Boolean.TRUE.equals(richiediConfermaQuadraturaCassa) && ErroreBil.QUADRATURA_NON_CORRETTA.getCodice().equals(e.getCodice())){
					continue;
				}
				addErrore(e);
			}		
			setErroriInSessionePerActionSuccessiva();
			
			return INPUT;
		}
		
		
		log.debug(methodName, "Invocazione terminata con successo");
		 //*/

		if(hasErrori()) {
			setErroriInSessionePerActionSuccessiva();			
		}else {
			addInformazione(new Informazione("COR_INF_0006", "Operazione di aggiornamento &egrave; stata completata correttamente"));
		}
		
	    if(hasMessaggi()) {
			setMessaggiInSessionePerActionSuccessiva();
		}
		
		if(hasInformazioni()){
			setInformazioniInSessionePerActionSuccessiva();			
		}
		
//		model.popolaStringaProvvedimento(res.getVariazioneImportoCapitolo().getAttoAmministrativo(), "provvedimento variazione di PEG");
//		model.popolaStringaProvvedimentoAggiuntivo(res.getVariazioneImportoCapitolo().getAttoAmministrativoVariazioneBilancio());
		model.setStatoOperativoVariazioneDiBilancio(res.getVariazioneImportoCapitolo().getStatoOperativoVariazioneDiBilancio());
		model.setElementoStatoOperativoVariazione(ElementoStatoOperativoVariazioneFactory.getInstance(model.getEnte().getGestioneLivelli(),model.getStatoOperativoVariazioneDiBilancio()));
				
		model.setAnnullaAbilitato(Boolean.FALSE);
		model.setConcludiAbilitato(Boolean.FALSE);
		model.setSalvaAbilitato(Boolean.FALSE);
		
		
		return SUCCESS;
	}

	@Override
	public void validate() {
		// NON DEVO FARE NULLA!
		// Mi restituirebbe INPUT in caso di errore, e non posso tollerare tale valore (come scelgo UEB, noUEB?)
	}

	/**
	 * Validazione interna dei dati.
	 * 
	 * @param methodName il nome del metodo invocante
	 * 
	 * @return <code>true</code> se la validazione ha avuto successo; <code>false</code> in caso contrario
	 */
	private boolean innerValidate(final String methodName) {
		log.debug(methodName, "Validazione dei dati");
		super.validate();
		return !hasActionErrors();
	}

	/* **************************************************************************
	 * **************************************************************************
	 * *** Interazioni AJAX con la pagina di specificazione della variazione ****
	 * **************************************************************************
	 * **************************************************************************/

	/* ******** Con gestione UEB ******** */
	
	/**
	 * Valuta la response ottenuta dal servizio di eliminazione del capitolo provvisorio.
	 * 
	 * @param response la response
	 * @param elemento il capitolo provvisorio
	 * 
	 * @throws ApplicationException nel caso in cui vi sia stato un errore nell'invocazione del servizio
	 */
	protected void valutaResponseEliminazioneCapitoloProvvisorio(ServiceResponse response, ElementoCapitoloVariazione elemento)
			throws ApplicationException {
		final String methodName = "valutaResponseEliminazioneCapitoloProvvisorio";
		log.debug(methodName, "É possibile eliminare il capitolo provvisorio? " + (response != null && !response.hasErrori()));
		if (response == null) {
			Errore errore = ErroreCore.ERRORE_DI_SISTEMA.getErrore("Invocazione non possibile per il capitolo " + elemento.getAnnoCapitolo() + " / "
					+ elemento.getNumeroCapitolo() + "/" + elemento.getNumeroArticolo());
			addErrore(errore);
			throw new ApplicationException(errore.getTesto());
		} else if (response.hasErrori()) {
			addErrori(response);
			throw new ApplicationException("Fallimento della response");
		}
	}


	/**
	 * Elimina un capitolo in stato Provvisorio.
	 * @param <RES> la tipizzazione della response
	 * 
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * @return la response di eliminazione del capitolo
	 */
	protected <RES extends ServiceResponse> RES eliminaCapitoloProvvisorio(Capitolo<?, ?> capitoloDaEliminare) {
		// Popolamento dei campi comuni
		capitoloDaEliminare.setBilancio(model.getBilancio());
		capitoloDaEliminare.setEnte(model.getEnte());
		capitoloDaEliminare.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.PROVVISORIO);

		RES result = null;

		TipoCapitolo tipoCapitolo = capitoloDaEliminare.getTipoCapitolo();
		switch (tipoCapitolo) {
		case CAPITOLO_USCITA_PREVISIONE:
			result = eliminaCapitoloUscitaPrevisione(capitoloDaEliminare);
			break;
		case CAPITOLO_USCITA_GESTIONE:
			result = eliminaCapitoloUscitaGestione(capitoloDaEliminare);
			break;
		case CAPITOLO_ENTRATA_PREVISIONE:
			result = eliminaCapitoloEntrataPrevisione(capitoloDaEliminare);
			break;
		case CAPITOLO_ENTRATA_GESTIONE:
			result = eliminaCapitoloEntrataGestione(capitoloDaEliminare);
			break;
		default:
			break;
		}

		return result;
	}

	/**
	 * Verifica l'annullabilit&agrave; di un capitolo.
	 * @param <RES> la tipizzazione della response
	 * 
	 * @param capitoloDaAnnullare il capitolo da eliminare
	 * @return la response di controllo di annullabilit&agrave;
	 */
	protected <RES extends ServiceResponse> RES verificaAnnullabilitaCapitolo(Capitolo<?, ?> capitoloDaAnnullare) {
		// Popolamento dei campi comuni
		capitoloDaAnnullare.setBilancio(model.getBilancio());
		capitoloDaAnnullare.setEnte(model.getEnte());

		RES result = null;

		TipoCapitolo tipoCapitolo = capitoloDaAnnullare.getTipoCapitolo();
		switch (tipoCapitolo) {
		case CAPITOLO_USCITA_PREVISIONE:
			result = verificaAnnullabilitaCapitoloUscitaPrevisione(capitoloDaAnnullare);
			break;
		case CAPITOLO_USCITA_GESTIONE:
			result = verificaAnnullabilitaCapitoloUscitaGestione(capitoloDaAnnullare);
			break;
		case CAPITOLO_ENTRATA_PREVISIONE:
			result = verificaAnnullabilitaCapitoloEntrataPrevisione(capitoloDaAnnullare);
			break;
		case CAPITOLO_ENTRATA_GESTIONE:
			result = verificaAnnullabilitaCapitoloEntrataGestione(capitoloDaAnnullare);
			break;
		default:
			break;
		}

		return result;
	}

	/**
	 * Valuta se l'inserimento del capitolo nella variazione &eacute; legittimo.
	 * 
	 * @param capitoloDaInserireInVariazione il capitolo di cui controllare l'inserimento
	 * @param ignoraValidazione              se la validazione sia da ignorare
	 * @param ignoraValidazioneImportiDopoDefinizione se ignorare la validazione importi dopo la definizione
	 * 
	 * @return se l'inserimento sia valido
	 */
	protected boolean validaInserimentoCapitoloNellaVariazione(ElementoCapitoloVariazione capitoloDaInserireInVariazione,/*, ElementoCapitoloVariazione capitoloOriginale,*/ Boolean ignoraValidazione, Boolean ignoraValidazioneImportiDopoDefinizione) {
		final String methodName = "validaInserimentoCapitoloNellaVariazione";
		// Valido la variazione inserita
		log.debug(methodName, "Controllo i vari importi che non devono essere nulli");
		// Controlla se la variazione si riferisce all'anno di bilancio
		//CR-SIAC-3666 e CR SIAC-2934, modifiche eliminate con la SIAC-6883
		
		boolean importiInvalidi = (capitoloDaInserireInVariazione.getCompetenza() == null && capitoloDaInserireInVariazione.getCompetenza1() == null && capitoloDaInserireInVariazione.getCompetenza2() == null)
				|| (capitoloDaInserireInVariazione.getResiduo() == null)
				|| (capitoloDaInserireInVariazione.getCassa() == null);

		if (importiInvalidi) {
			log.debug(methodName, "Almeno una validazione sugli importi ha dato esito negativo");
			// Per evitare che gli errori si accumulino
			ErroreBil erroreDaInserire = model.isGestioneUEB() ? ErroreBil.NON_TUTTI_I_CAMPI_DELL_UEB_RELATIVA_ALLA_VARIAZIONE_DI_BILANCIO_SONO_STATI_VALORIZZATI
					: ErroreBil.NON_TUTTI_I_CAMPI_DI_UN_CAPITOLO_ASSOCIATO_AD_UNA_VARIAZIONE_SONO_STATI_VALORIZZATI;

			addErrore(erroreDaInserire.getErrore());
			return false;
		}
		// Impostazione a zero degli importi nel caso in cui non siano originariamente editabili
		
		//CR-4330
		log.debug(methodName, "Gli importi valorizzabili sono stati injettati e la cassa e' modificabile. Validazione dell'importo cassa");
		impostaFlagCassaIncongruente(ignoraValidazione.booleanValue());
		impostaFlagCassaIncongruenteDopoDefinizione(false);
		if(!Boolean.TRUE.equals(ignoraValidazione) 
				&& capitoloDaInserireInVariazione.getCassa().compareTo(
						capitoloDaInserireInVariazione.getCompetenza()
						.add(capitoloDaInserireInVariazione.getResiduo()
								))
				> 0
			) {
				log.debug(methodName, "La cassa inserita è maggiore della somma tra competenza e residuo");
				impostaFlagCassaIncongruente(true);
				impostaFlagIgnoraValidazione(false);
				addMessaggio(MessaggioBil.STANZIAMENTO_DI_CASSA_INCONGRUENTE.getMessaggio());
				return false;
			}
		
		//CR-4330: controllo anche che l'effetto della variazione non sia quello di squadrare la cassa del capitolo 
		if(!Boolean.TRUE.equals(ignoraValidazioneImportiDopoDefinizione) && !isStanziamentoCassaCoerenteSeApplicatoAlCapitolo(capitoloDaInserireInVariazione)){
			log.debug(methodName, "Attualmente, la cassa inseritaporterebbe il capitolo ad avere una cassa maggiore della somma degli stanziamenti");
			
			addMessaggio(MessaggioBil.STANZIAMENTO_DI_CASSA_INCONGRUENTE_DOPO_DEFINIZIONE_VARIAZIONE.getMessaggio());
			
			impostaFlagCassaIncongruenteDopoDefinizione(true);
			impostaFlagIgnoraValidazioneImportiDopoDefinizione(false);
			return false;
		}
		
		return !hasErrori();
	}
	/**
	 * Applicando la variazione agli stanziamenti il risultato dello stanziamento di cassa non deve superiore alla somma dei due nuovi stanziamenti.
	 * Controlla che la somma dello stanziamento attuale del capitolo e dello stanziamento residuo sia maggiore od uguale alla competenza anche con i nuovi importi inseriti dall'utente.
	 * @return <code>true</code> se (StanziamentoCapitolo + stanziamentoVariazione) + (Residuo + ResiduoVariazione)  >= (cassaCapitolo + cassaVariazione), <br/> <code>false</code> altrimenti
	 * */
	private boolean isStanziamentoCassaCoerenteSeApplicatoAlCapitolo(ElementoCapitoloVariazione capitoloDaInserireInVariazione){
		//4330
		BigDecimal sommaStanziamenti = capitoloDaInserireInVariazione.getCompetenzaOriginale()
				.add(capitoloDaInserireInVariazione.getCompetenza())
				.add(capitoloDaInserireInVariazione.getResiduoOriginale())
				.add(capitoloDaInserireInVariazione.getResiduo());
		BigDecimal sommaCassa = capitoloDaInserireInVariazione.getCassaOriginale()
				.add(capitoloDaInserireInVariazione.getCassa());
		return sommaStanziamenti.compareTo(sommaCassa) >= 0;
	}
	
	/* ******************************************************************************************
	 * ************************************...................***********************************
	 * ************************************ Senza gestione UEB **********************************
	 * ************************************ ...................**********************************
	 *  ***************************************************************************************** */

	
		
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
		
		model.setTotaleStanziamentiEntrata1(response.getTotaleStanziamentiEntrata1());
		model.setTotaleStanziamentiSpesa1(response.getTotaleStanziamentiSpesa1());
		
		model.setTotaleStanziamentiEntrata2(response.getTotaleStanziamentiEntrata2());
		model.setTotaleStanziamentiSpesa2(response.getTotaleStanziamentiSpesa2());
		
		model.setTotaleStanziamentiResiduiEntrata(response.getTotaleStanziamentiResiduiEntrata());
		model.setTotaleStanziamentiResiduiSpesa(response.getTotaleStanziamentiResiduiSpesa());
				
		
		
		
		return SUCCESS;
	}

	/**
	 * Metodo per il controllo delle azioni consentite all'utente.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String controllaAzioniConsentiteAllUtente() {
		TipoCapitolo tipoCapitolo = model.getSpecificaImporti().getElementoCapitoloVariazione().getTipoCapitolo();
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		model.getSpecificaImporti().setUtenteAbilitatoAdAnnullamento(AzioniConsentiteFactory.isAnnullaConsentito(tipoCapitolo, listaAzioniConsentite));
		model.getSpecificaImporti().setUtenteAbilitatoAdInserimento(AzioniConsentiteFactory.isInserisciConsentito(tipoCapitolo, listaAzioniConsentite));
		return SUCCESS;
	}

	

	
	/**
	 * Metodo per scollegare i capitoli dalla variazione. Nel caso in cui il capitolo si un capitolo collegato 
	 * alla variazione tramite la funzione "nuovo capitolo", l'occorrenza sul db verragrave; cancellata
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String eliminaCapitoliNellaVariazione() {
		final String methodName = "eliminaCapitoliNellaVariazione";
		log.debug(methodName, "Modifico la lista eliminando l'elemento");

		
		EliminaDettaglioVariazioneImportoCapitolo req = model.creaRequestEliminaDettaglioVariazioneImportoCapitolo();
		
		log.debug(methodName, "richiamo il webservice di eliminazione capitolo nella variazione");
		EliminaDettaglioVariazioneImportoCapitoloResponse response = variazioneDiBilancioService.eliminaDettaglioVariazioneImportoCapitolo(req);
		if(response.hasErrori()){
			log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo nella variazione");
			addErrori(response);
		}
		
		// Controllo se l'elemento è stato inserito in stato provvisorio
		if (Boolean.TRUE.equals(model.getSpecificaImporti().getElementoCapitoloVariazione().getDaInserire())) {
			log.debug(methodName, "Il capitolo è provvisorio: eliminarlo");
			ServiceResponse responseEliminazione = eliminaCapitoloProvvisorio(model.getSpecificaImporti().getElementoCapitoloVariazione().unwrap());
			if(responseEliminazione.hasErrori()){
				log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo");
				addErrori(responseEliminazione);
			}
		}
		
		return SUCCESS;

	}

	/**
	 * Redirige verso la creazione di un nuovo capitolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	@AnchorAnnotation(name = "Redirezione nuovo capitolo", value = "%{cdu}", afterAction = true)
	public String redirezioneVersoNuovoCapitolo() {
		final String methodName = "redirezioneVersoNuovoCapitolo";

		model.getSpecificaImporti().setListaCapitoliNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		model.getSpecificaUEB().setListaUEBNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		model.getSpecificaUEB().setListaUEBDaInserireNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		model.getSpecificaUEB().setListaUEBNellaVariazioneCollassate(new ArrayList<ElementoCapitoloVariazione>());
		// Comunico all'inserimento l'arrivo dall'aggiornamento
		sessionHandler.setParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, Boolean.TRUE);

		String tipoCapitolo = model.getSpecificaImporti().getTipoCapitolo().toLowerCase(getLocale());
		String tipoApplicazione = model.getApplicazione().getDescrizione().toLowerCase(getLocale());
		String compoundName = tipoCapitolo + "_" + tipoApplicazione;
		log.debug(methodName, "Redirezione verso inserimento: " + compoundName);
		return compoundName;
	}

	/**
	 * Imposta eventuali errori e messaggi e permette di redirezionare l'utente ad una pagina disabilitata nel caso fosse necessario
	 * @return il risultato dell'invocazione
	 */
	public String redirezioneVersoPaginaDisabilitata() {
		final String methodName = "redirezioneVersoPaginaDisabilitata";
		boolean toPaginaAbilitataAModifiche = false;
		
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		for (Errore err : model.getErrori()){
			if(ErroreBil.QUADRATURA_NON_CORRETTA.getCodice().equals(err.getCodice()) || ErroreBil.PROVVEDIMENTO_VARIAZIONE_NON_PRESENTE.getCodice().equals(err.getCodice())){
				toPaginaAbilitataAModifiche = true;
				continue;
			}
		}
		
		
		Boolean daRientro = sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class);
		if (daRientro != null) {
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		sessionHandler.cleanAllSafely();
		log.debug(methodName,"Redireziono verso la pagina disabilitata");
		return toPaginaAbilitataAModifiche? INPUT : SUCCESS;
	}
	
	
	/**
	 * Controlla se un capitolo puograve; essere annullato e, in caso affermativo, lo collega alla variazione come capitolo "DA ANNULLARE" in fase di definizione
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String annullaCapitolo() {
		final String methodName = "annullaCapitolo";
		
		//SIAC-6884
		boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite());
		
		if(decentrato){			
			log.debug(methodName, "Operatore decentrato: operazione non consentita");
			List<Errore> listaErrori = new ArrayList<Errore>();
			Errore err = new Errore();
			err.setCodice("COR_ERR_0044");
			err.setDescrizione("Operazione non consentita: Operatore decentrato");
			listaErrori.add(err);
			addErrori(listaErrori);
			
			return INPUT;
		}

		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare = model.getSpecificaImporti().getElementoCapitoloVariazione();
		
		// Unwrap del wrapper
		Capitolo<?, ?> capitoloDaAnnullare = elementoCapitoloVariazioneDaAnnullare.unwrap();

		log.debug(methodName, "Invocazione del servizio di verifica annullabilità");
		ServiceResponse responseAnnullabilita = verificaAnnullabilitaCapitolo(capitoloDaAnnullare);
		if (responseAnnullabilita == null) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Invocazione non possibile per il capitolo"
					+ elementoCapitoloVariazioneDaAnnullare.getAnnoCapitolo() + " / " + elementoCapitoloVariazioneDaAnnullare.getNumeroCapitolo() + "/"
					+ elementoCapitoloVariazioneDaAnnullare.getNumeroArticolo()));
		} else if (Boolean.FALSE.equals(ReflectionUtil.getBooleanField(responseAnnullabilita, "annullabilitaCapitolo"))) {
			addErrori(responseAnnullabilita);
		} else {
			elementoCapitoloVariazioneDaAnnullare.setDatiAccessorii("DA ANNULLARE");
			elementoCapitoloVariazioneDaAnnullare.setDaAnnullare(Boolean.TRUE);
		}
		if(hasErrori()){
			return SUCCESS;
		}
		
		
		log.debug(methodName, "Prendo il capitolo da annullare");
		InserisciDettaglioVariazioneImportoCapitolo request = model.creaRequestInserisciDettaglioVariazioneImportoAnnullaCapitolo();
		
		log.debug(methodName, "Richiamo il Webservice");
		///*
		InserisciDettaglioVariazioneImportoCapitoloResponse response = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(request);
		if(response.hasErrori()){
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori durante i servizi di inserimento");
		}//*/
		return SUCCESS;
	}
	/**
	 * Imposta il valore del flag flagCassaIncongruente
	 * @param flagCassaIncongruente il flag cassa incongruente
	 * */
	protected abstract void impostaFlagCassaIncongruente(boolean flagCassaIncongruente);
	/**
	 * Imposta il valore del flag flagCassaIncongruentDopoLaDefinizione secondo il valore passato come parametro
	 * @param flagCassaIncongruenteDopoDefinizione il flag cassa incongruente dopo definizione
	 * */
	protected abstract void impostaFlagCassaIncongruenteDopoDefinizione(boolean flagCassaIncongruenteDopoDefinizione);
	/**
	 * imposta il flagIgnoraValidazione  secondo il valore passato come parametro
	 * @param flagIgnoraValidazione il flag ignora validazione
	 * */
	protected abstract void impostaFlagIgnoraValidazione(boolean flagIgnoraValidazione);
	/**
	 * imposta il flagIgnoraValidazioneImportiDopoDefinizione
	 * @param flagIgnoraValidazioneImportiDopoDefinizione il flag ignora validazione importi dopo definizione
	 * */
	protected abstract void impostaFlagIgnoraValidazioneImportiDopoDefinizione(boolean flagIgnoraValidazioneImportiDopoDefinizione);
	
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
		
		if(res.hasErrori()) {
			addErrori(res);
			log.info(methodName, "Errori nel reperimento dell'excel della variazione");
			return INPUT;
		}
		
		byte[] bytes = res.getReport();
		model.setContentType(res.getContentType() == null ? null : res.getContentType().getMimeType());
		model.setContentLength(Long.valueOf(bytes.length));
		model.setFileName("esportazioneVariazione" + model.getAnnoEsercizio() + "_" + model.getNumeroVariazione() + "." + res.getExtension());
		model.setInputStream(new ByteArrayInputStream(bytes));
		
		return SUCCESS;
	}
	
	
	//XXX:portare su
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
		if(res.hasErrori()){
			log.debug(methodName, "Si sono verificati errori durante l'esecuzione del servizio di ricerca singolo dettaglio variazione importo capitolo nella variazione");
			addErrori(res);
			return INPUT;
		}
		ElementoCapitoloVariazione elementoCapitoloVariazioneTrovato = ElementoCapitoloVariazioneFactory.getInstanceFromSingoloDettaglio(res.getDettaglioVariazioneImportoCapitolo(), model.isGestioneUEB());
		model.setElementoCapitoloVariazioneTrovatoNellaVariazione(elementoCapitoloVariazioneTrovato);
		return SUCCESS;
	}
	
	/**
	 * Filtra tipi componenti default.
	 *
	 * @param listaDaFiltrare the lista da filtrare
	 * @return the list
	 */
	private List<TipoComponenteImportiCapitolo> filtraTipiComponentiDefault(List<TipoComponenteImportiCapitolo> listaDaFiltrare) {
		List<TipoComponenteImportiCapitolo> lista = new ArrayList<TipoComponenteImportiCapitolo>();
		if(listaDaFiltrare == null) {
			return lista;
		}
		for (TipoComponenteImportiCapitolo tc : listaDaFiltrare) {
			PropostaDefaultComponenteImportiCapitolo p = tc.getPropostaDefaultComponenteImportiCapitolo();
			if(model.isPropostaDefaultCompatibile(p)) {
				lista.add(tc);
			}
		}
		return lista;
	}
	
	
	
	/*
	 * SIAC-6884 per variazioni decentrate
	 * CHIUSURA PROPOSTA 
	 */
	public String chiudiProposta() {
		final String methodName = "concludi";
		
		if (!innerValidate(methodName)) {
			return SUCCESS;
		}
		//3403
		AggiornaAnagraficaVariazioneBilancio req = model.creaRequestAggiornaAnagraficaVariazioneBilancioDecentrato(); 
		//SIAC-7629
//		if(req.getVariazioneImportoCapitolo().getTipoVariazione().equals(TipoVariazione.VARIAZIONE_DECENTRATA_LEGGE) && false) {
//			req.setAggiornamentoDaVariazioneConfermaQuadraturaFromAction(true);
//		}
		AsyncServiceResponse res = variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancioAsync(wrapRequestToAsync(req));
		
		log.debug(methodName, "Operazione asincrona avviata. IdOperazioneAsincrona: "+ res.getIdOperazioneAsincrona());
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione servizio aggiornaAnagraficaVariazioneBilancioAsync terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());
		return SUCCESS;
	}
	
	/*
	 * SIAC-6884 per variazioni decentrate
	 * CHIUSURA PROPOSTA 
	 */
	public String chiudiPropostaAsyncResponse(){
		String methodName = "chiudiPropostaAsyncResponse";
		model.setIsAsyncResponsePresent(Boolean.FALSE);
		Integer idOperazioneAsincrona = model.getIdOperazioneAsincrona();
		log.debug(methodName, "Richiamo il servizio di ricerca dettaglio della variazione asincrona per ottenere la response");
		GetDettaglioOperazioneAsincrona reqDOA = new GetDettaglioOperazioneAsincrona();
		reqDOA.setOpAsincId(idOperazioneAsincrona);
		reqDOA.setCodice("SERVICE_RESPONSE");
		reqDOA.setParametriPaginazione(new ParametriPaginazione(0, Integer.MAX_VALUE));
		reqDOA.setRichiedente(model.getRichiedente());
		GetDettaglioOperazioneAsincronaResponse resDOA = operazioneAsincronaService.getDettaglioOperazioneAsincrona(reqDOA);
		if (resDOA.hasErrori()) {
			log.debug(methodName, "Invocazione servizio getDettaglioOperazioneAsincrona terminata con fallimento");
			addErrori(resDOA);
			return SUCCESS;
		}
		AggiornaAnagraficaVariazioneBilancioResponse res = null;
		
		if(resDOA.getElencoPaginato()!=null){
			for(DettaglioOperazioneAsincrona doa : resDOA.getElencoPaginato()){
				res = JAXB.unmarshal(new StringReader(doa.getServiceResponse()), AggiornaAnagraficaVariazioneBilancioResponse.class);
				break;
			}
		}
		//res = new AggiornaAnagraficaVariazioneBilancioResponse();
		
		if(res==null){
			log.debug(methodName, "Il servizio asincrono non ha ancora risposto. Continuare il polling.");
			return SUCCESS;
		}
		
		log.debug(methodName, "Il servizio asincrono ha risposto.");
		model.setIsAsyncResponsePresent(Boolean.TRUE);
		
		//Il servizio asincrono ha dato risposta.
		// Ottengo l'id dell'attività
		extractIdAttivitaIfNotNull(res.getIdTask());
		//SIAC-7629 inizio FL
		Boolean richiediConfermaQuadraturaCassa =  res.verificatoErrore(ErroreBil.QUADRATURA_NON_CORRETTA.getCodice());// && Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento()) && Boolean.FALSE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa()) && StringUtils.isBlank(model.getSaltaCheckStanziamentoCassa()); //model.getSaltaCheckStanziamentoCassa()); 
		model.setRichiediConfermaQuadraturaCP(richiediConfermaQuadraturaCassa);
		
		
		if (res.isFallimento() || res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
						
			for(Errore e : res.getErrori()){
				if(Boolean.TRUE.equals(richiediConfermaQuadraturaCassa) && ErroreBil.QUADRATURA_NON_CORRETTA.getCodice().equals(e.getCodice()) && res.getVariazioneImportoCapitolo().getTipoVariazione().equals(TipoVariazione.VARIAZIONE_DECENTRATA_LEGGE) ){
					continue;
				}
				addErrore(e);
			}		
			setErroriInSessionePerActionSuccessiva();
			
			return INPUT;
		}
		
		//
//		if (res.isFallimento() || res.hasErrori()) {
//			log.debug(methodName, "Invocazione terminata con fallimento");
//			addErrori(res);
//			setErroriInSessionePerActionSuccessiva();
//			return INPUT;
//		}
		//SIAC-7629 fine FL
		
		
		
		
		
		log.debug(methodName, "Invocazione terminata con successo");
	    
		 //*/
	    //SIAC-7629 inizio  FL
		if (!Boolean.TRUE.equals(res.getIsQuadraturaCorretta())) {
			addMessaggio(ErroreBil.PROSECUZIONE_NONOSTANTE_QUADRATURA_NON_CORRETTA.getErrore());
		}
		//SIAC-7629 fine FL
	    if(hasMessaggi()) {
			setMessaggiInSessionePerActionSuccessiva();
		}
		if(hasInformazioni()){
			setInformazioniInSessionePerActionSuccessiva();			
		}
		
		if(hasErrori()) {
			setErroriInSessionePerActionSuccessiva();			
		}else {
			addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		}
		
		if(res.getVariazioneImportoCapitolo()!= null && res.getVariazioneImportoCapitolo().getAttoAmministrativo()!= null){
			model.popolaStringaProvvedimento(res.getVariazioneImportoCapitolo().getAttoAmministrativo(), "provvedimento variazione di PEG");
		}
		if(res.getVariazioneImportoCapitolo()!= null && 
				res.getVariazioneImportoCapitolo().getAttoAmministrativoVariazioneBilancio()!= null){
			model.popolaStringaProvvedimentoAggiuntivo(res.getVariazioneImportoCapitolo().getAttoAmministrativoVariazioneBilancio());
		}
		
		model.setStatoOperativoVariazioneDiBilancio(res.getVariazioneImportoCapitolo().getStatoOperativoVariazioneDiBilancio());
		model.setElementoStatoOperativoVariazione(ElementoStatoOperativoVariazioneFactory.getInstance(model.getEnte().getGestioneLivelli(),model.getStatoOperativoVariazioneDiBilancio()));
				
		model.setDataChiusuraProposta(new Date());
		
		
		model.setAnnullaAbilitato(Boolean.FALSE);
		model.setConcludiAbilitato(Boolean.FALSE);
		model.setSalvaAbilitato(Boolean.FALSE);
		return SUCCESS;
	}
	
}
