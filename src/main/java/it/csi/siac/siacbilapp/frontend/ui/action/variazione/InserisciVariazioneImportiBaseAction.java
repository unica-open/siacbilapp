/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoModel;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneModel;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReportResponse;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetDettaglioOperazioneAsincronaResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.DettaglioOperazioneAsincrona;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di action per la variazione degli importi.
 * 
 * @author Marchino Alessandro
 * @author Elisa Chiari
 * @version 1.0.0 18/10/2013
 * 
 */
public abstract class InserisciVariazioneImportiBaseAction extends InserisciVariazioneBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8768814561401283421L;
	
	private static final String GO_TO_AGGIORNAMENTO = "go_to_aggiorna";

	@Autowired private transient OperazioneAsincronaService operazioneAsincronaService;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String enterStep2() {
		// Imposto l'azione cui redirigere
		final String suffix = model.isGestioneUEB()? "UEB" : "";
		model.getDefinisci().setAzioneCuiRedirigere("esecuzioneStep2InserimentoVariazioniImporti" + suffix);
//		model.getDefinisci().setAnnoVariazioneAbilitato(Boolean.TRUE);

		//SIAC-6884
		//da questo momento in poi la variazione sarà considerata decentrata se la data di apertura è valorizzata
		if(model.getIsDecentrato()){
			if(model.getDefinisci().getDataApertura() == null)
				model.getDefinisci().setDataApertura(new java.util.Date(System.currentTimeMillis()));
		}

		return SUCCESS;
	}
	
	@Override
	protected void checkProvvedimento() {
		super.checkProvvedimento();
		AttoAmministrativo attoAmministrativoDaControllare = model.getDefinisci().getAttoAmministrativoAggiuntivo();
		if( attoAmministrativoDaControllare== null || attoAmministrativoDaControllare.getUid() == 0) {
			//non e' stato digitato il provvedimento, esco.
			return;
		}
		AttoAmministrativo attoAmministrativo = checkProvvedimentoPresenteNonAnnullato(attoAmministrativoDaControllare);
		if( attoAmministrativo == null || attoAmministrativo.getUid() == 0) {
			//il provvedimento e' stato digitato ma non puo' essere associato alla variazione perche' non ha superato i controlli
			return;
		}
		// Popolo i dati del model
		model.getDefinisci().setAttoAmministrativoAggiuntivo(attoAmministrativo);
		
	}

	@Override 
	public String executeStep2() {
		// Sposta il dato dell'anno della variazione nel campo dell'anno del capitolo da ricercare
		model.getSpecificaImporti().setAnnoCapitolo(model.getAnnoEsercizioInt());
		if(!model.getDefinisci().isAnagraficaInserita()){
			return inserisciAnagraficaVariazione();
		}
		return aggiornaAnagraficaVariazione();
	}

	/**
	 * @return
	 */
	private String aggiornaAnagraficaVariazione() {
		AggiornaAnagraficaVariazioneBilancio request = model.creaRequestAggiornaAnagraficaVariazioneBilancio();
		
		AggiornaAnagraficaVariazioneBilancioResponse response = variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancio(request);
			
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		return GO_TO_AGGIORNAMENTO;
	}

	/**
	 * @param methodName
	 * @return
	 */
	private String inserisciAnagraficaVariazione() {
		final String methodName ="inserisciAnagraficaVariazione";
		InserisceAnagraficaVariazioneBilancio requestAnagrafica = model.creaRequestInserisceAnagraficaVariazioneDiBilancio();
		InserisceAnagraficaVariazioneBilancioResponse responseAnagrafica = variazioneDiBilancioService.inserisceAnagraficaVariazioneBilancio(requestAnagrafica);

		if (responseAnagrafica.hasErrori()) {
			log.debug(methodName, "La request dell'inserimento dell'anagrafica si è risolta in un fallimento");
			addErrori(responseAnagrafica);
			return INPUT;
		}
		VariazioneImportoCapitolo variazioneInserita = responseAnagrafica.getVariazioneImportoCapitolo();
		if(variazioneInserita == null) {
			log.error(methodName, "La variazione inserita risulta essere null");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile inserire la variazione di bilancio, si e' verificato un errore imprevisto."));
			return INPUT;
		}

		//imposto i dati necessari al proseguimento
		model.setUidVariazione(variazioneInserita.getUid());
		model.setNumeroVariazione(variazioneInserita.getNumero());
		model.setStatoOperativoVariazioneDiBilancio(variazioneInserita.getStatoOperativoVariazioneDiBilancio());
		SpecificaVariazioneModel modelSpecifica = model.isGestioneUEB() ? model.getSpecificaUEB() : model.getSpecificaImporti();
		modelSpecifica.setNote(variazioneInserita.getNote());
		//SIAC-8332
		modelSpecifica.setStatoVariazione(variazioneInserita.getStatoOperativoVariazioneDiBilancio());
		modelSpecifica.setNumeroVariazione(variazioneInserita.getNumero());
		return GO_TO_AGGIORNAMENTO;
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String enterStep3() {
		final String methodName = "enterStep3";

		log.debug(methodName, "Inizializzazione delle liste");

		
		SpecificaVariazioneImportoModel modelImporto = model.getSpecificaImporti();
		if (modelImporto.getListaCapitoliNellaVariazione() == null) {
			log.debug(methodName, "listaUEBNellaVariazione == null");
			modelImporto.setListaCapitoliNellaVariazione(new ArrayList<ElementoCapitoloVariazione>());
		}
		
		//SIAC-6705
		modelImporto.setStatiOperativiElementoDiBilancio(Arrays.asList(StatoOperativoElementoDiBilancio.VALIDO, StatoOperativoElementoDiBilancio.PROVVISORIO));
		

		return SUCCESS;
	}

	@Override
	public String executeStep3() {
		final String methodName = "executeStep3";
		log.debug(methodName, "Richiamo il servizio per l'inserimento della variazione");
		
		AggiornaAnagraficaVariazioneBilancio request = model.creaRequestAggiornaAnagraficaVariazioneBilancio();
		
		AsyncServiceResponse res = variazioneDiBilancioService.aggiornaAnagraficaVariazioneBilancioAsync(wrapRequestToAsync(request));
		log.debug(methodName, "Operazione asincrona avviata. IdOperazioneAsincrona: "+ res.getIdOperazioneAsincrona());
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione servizio aggiornaAnagraficaVariazioneBilancioAsync terminata con fallimento");
			addErrori(res);
			return INPUT;
		}
		
		model.setIdOperazioneAsincrona(res.getIdOperazioneAsincrona());

			
		return SUCCESS;

	}

	
	/**
	 * Metodo utilizzato per controllare se il servizio asincrono di salvataggio abbia o meno terminato l'esecuzione.
	 * In caso di risposta affermativa, vengono impostati gli eventuali errori, messaggi e informazioni.
	 * @return il risultato dell'invocazione (SUCCESS)
	 */
	public String ottieniResponseExecuteStep3Async(){
		String methodName = "ottieniResponseExecuteStep3Async";
		
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
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
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
	
		// Creazione della request
		SpecificaVariazioneModel modelSpecifica = model.isGestioneUEB() ? model.getSpecificaUEB() : model.getSpecificaImporti();
		modelSpecifica.setNumeroVariazione(res.getVariazioneImportoCapitolo().getNumero());
		
		if(hasMessaggi()) {
			setMessaggiInSessionePerActionSuccessiva();
		}
		
		return SUCCESS;
		
	}

	@Override
	public String enterStep4() {
		final String methodName = "enterStep4";
		SpecificaVariazioneModel model3 = model.isGestioneUEB() ? model.getSpecificaUEB() : model.getSpecificaImporti();
		log.debug(methodName, "Impostazione dei dati tra i vari model");
		model.impostaDatiStep4(model3);
		
		impostaInformazioneSuccesso();
		leggiEventualiMessaggiAzionePrecedente();
		
		return SUCCESS;
	}

	/* **************************************************************************
	 * **************************************************************************
	 * *** Interazioni AJAX con la pagina di specificazione della variazione ****
	 * **************************************************************************
	 * **************************************************************************/



	/**
	 * Elimina un capitolo in stato Provvisorio.
	 * @param <RES> la tipizzazione della response
	 * 
	 * @param capitoloDaEliminare il capitolo da eliminare
	 * 
	 * @return la response di eliminazione del capitolo
	 */
	protected <RES extends ServiceResponse> RES eliminaCapitoloProvvisorio(Capitolo<?, ?> capitoloDaEliminare) {
		// Popolamento dei campi comuni
		final String methodName = "eliminaCapitoloProvvisorio";
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

		if(result.hasErrori()){
			log.error(methodName, "Impossibile eliminare il capitolo [uid = " + capitoloDaEliminare.getUid() + "].");
		}
		
		return result;
	}

	/**
	 * Verifica l'annullabilit&agrave; di un capitolo.
	 * @param <RES> la tipizzazione della response
	 * 
	 * @param capitoloDaAnnullare il capitolo da eliminare
	 * @return la response di verifica per l'annullabilit&agrave;
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
	protected boolean validaInserimentoCapitoloNellaVariazione(ElementoCapitoloVariazione capitoloDaInserireInVariazione, /*ElementoCapitoloVariazione capitoloOriginale, */Boolean ignoraValidazione, Boolean ignoraValidazioneImportiDopoDefinizione) {
		final String methodName = "validaInserimentoCapitoloNellaVariazione";
		// Valido la variazione inserita
		log.debug(methodName, "Controllo i vari importi che non devono essere nulli");
		// Controlla se la variazione si riferisce all'anno di bilancio
		boolean variazioneStessoAnno = model.getAnnoEsercizioInt().equals(model.getDefinisci().getAnnoVariazione());
		
		//TODO: javascript?
		boolean residuoEditabile = variazioneStessoAnno;
				//&& !"Assestamento".equalsIgnoreCase(model.getDefinisci().getTipoApplicazione());
		// CR SIAC-2934
//				&& !"Gestione".equalsIgnoreCase(model.getDefinisci().getTipoApplicazione());
		boolean cassaEditabile = variazioneStessoAnno;

		boolean importiInvalidi = capitoloDaInserireInVariazione.getCompetenza() == null
				|| (residuoEditabile && capitoloDaInserireInVariazione.getResiduo() == null)
				|| (cassaEditabile && capitoloDaInserireInVariazione.getCassa() == null);

		if (importiInvalidi) {
			log.debug(methodName, "Almeno una validazione sugli importi ha dato esito negativo");
			// Per evitare che gli errori si accumulino
			ErroreBil erroreDaInserire = model.isGestioneUEB() ? ErroreBil.NON_TUTTI_I_CAMPI_DELL_UEB_RELATIVA_ALLA_VARIAZIONE_DI_BILANCIO_SONO_STATI_VALORIZZATI
					: ErroreBil.NON_TUTTI_I_CAMPI_DI_UN_CAPITOLO_ASSOCIATO_AD_UNA_VARIAZIONE_SONO_STATI_VALORIZZATI;

			addErrore(erroreDaInserire.getErrore());
			return false;
		}
		// Impostazione a zero degli importi nel caso in cui non siano originariamente editabili
		if (!residuoEditabile) {
			capitoloDaInserireInVariazione.setResiduo(BigDecimal.ZERO);
		}
		
		//CR-4330
		if(cassaEditabile){
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
		}
		
//		checkCondition(capitoloDaInserireInVariazione.getCassa().add(capitoloDaInserireInVariazione.getCassaOriginale()).signum() >= 0, ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("cassa"));
//		checkCondition(capitoloDaInserireInVariazione.getCompetenza().add(capitoloDaInserireInVariazione.getCompetenzaOriginale()).signum() >= 0, ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("competenza"));
//		checkCondition(capitoloDaInserireInVariazione.getResiduo().add(capitoloDaInserireInVariazione.getResiduoOriginale()).signum() >= 0, ErroreBil.DISPONIBILITA_INSUFFICIENTE.getErrore("residuo"));
		
		return !hasErrori();
	}
	
	/**
	 * Applicando la variazione agli stanziamenti il risultato dello stanziamento di cassa non deve superiore alla somma dei due nuovi stanziamenti.
	 * Controlla che la somma dello stanziamento attuale del capitolo e dello stanziamento residuo sia maggiore od uguale alla competenza anche con i nuovi importi inseriti dall'utente.
	 * @return <code>true</code> se (StanziamentoCapitolo + stanziamentoVariazione) + (Residuo + ResiduoVariazione)  >= (cassaCapitolo + cassaVariazione), <br/> <code>false</code> altrimenti
	 * */
	private boolean isStanziamentoCassaCoerenteSeApplicatoAlCapitolo(ElementoCapitoloVariazione capitoloDaInserireInVariazione){
		//CR 4330
		BigDecimal sommaStanziamenti = capitoloDaInserireInVariazione.getCompetenzaOriginale()
				.add(capitoloDaInserireInVariazione.getCompetenza())
				.add(capitoloDaInserireInVariazione.getResiduoOriginale())
				.add(capitoloDaInserireInVariazione.getResiduo());
		BigDecimal sommaCassa = capitoloDaInserireInVariazione.getCassaOriginale()
				.add(capitoloDaInserireInVariazione.getCassa());
		return sommaStanziamenti.compareTo(sommaCassa) >= 0;
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
	
	/* ******************************************************************************************
	 * ************************************...................***********************************
	 * ************************************ Senza gestione UEB **********************************
	 * ************************************ ...........******************************************
	 *  ***************************************************************************************** */

	/**
	 * Metodo per ottenere la lista dei capitoli collegati alla variazione.
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
	 * Metodo per eliminare i capitoli nella variazione.
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
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
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
		String tipoCapitolo = model.getSpecificaImporti().getTipoCapitolo().toLowerCase(getLocale());
		String tipoApplicazione = model.getDefinisci().getApplicazione().getDescrizione().toLowerCase(getLocale());
		String compoundName = tipoCapitolo + "_" + tipoApplicazione;
		log.debug(methodName, "Redirezione verso inserimento: " + compoundName);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, Boolean.TRUE);
		return compoundName;
	}

	/**
	 * Controlla l'annullabilit&agrave; di un capitolo. Se il capitolo risulta essere annullabile, viene collegato alla variazione come "DA ANNULLARE"
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String annullaCapitolo() {

		final String methodName = "annullaCapitolo";

		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare = model.getSpecificaImporti().getElementoCapitoloVariazione();
//		// Unwrap del wrapper		
		Capitolo<?, ?> capitoloDaAnnullare = elementoCapitoloVariazioneDaAnnullare.unwrap();
//
		log.debug(methodName, "Invocazione del servizio di verifica annullabilità");
		ServiceResponse responseAnnullabilita = verificaAnnullabilitaCapitolo(capitoloDaAnnullare);
		if (responseAnnullabilita == null) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Invocazione non possibile per il capitolo"
					+ elementoCapitoloVariazioneDaAnnullare.getAnnoCapitolo() + " / " + elementoCapitoloVariazioneDaAnnullare.getNumeroCapitolo() + "/"
					+ elementoCapitoloVariazioneDaAnnullare.getNumeroArticolo()));
			return SUCCESS;
		} else if (Boolean.FALSE.equals(ReflectionUtil.getBooleanField(responseAnnullabilita, "annullabilitaCapitolo"))) {
			addErrori(responseAnnullabilita);
			return SUCCESS;
		} 

		InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitoloPerAnnullamento();
		
		
		log.debug(methodName, "richiamo il servizio di inserimento");
		//*
		 InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(res);
			
		}
		return SUCCESS;
	}

	/**
	 * @return il risultato dell'invocazione
	 */
	public String returnToStep2() {
		// CR- 3736 : posso caricare i residui una e una sola volta
		model.getDefinisci().setShowCaricaResidui(false);
		model.getDefinisci().setAnagraficaInserita(true);
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
		model.setFileName("esportazioneVariazione" + model.getDefinisci().getAnnoEsercizio() + "_" + model.getNumeroVariazione() + "." + res.getExtension());
		model.setInputStream(new ByteArrayInputStream(bytes));
		
		return SUCCESS;
	}


}
