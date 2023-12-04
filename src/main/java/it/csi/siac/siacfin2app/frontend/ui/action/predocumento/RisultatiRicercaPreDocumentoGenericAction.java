/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonapp.handler.session.SessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.RisultatiRicercaPreDocumentoGenericModel;
import it.csi.siac.siacfin2app.frontend.ui.util.async.AsyncOperationHelper;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Action generica per i risultati di ricerca del preDocumento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 04/12/2014
 * 
 * @param <M> la tipizzazione del model
 * 
 */
public class RisultatiRicercaPreDocumentoGenericAction<M extends RisultatiRicercaPreDocumentoGenericModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7070155498005379818L;

	/** Servizio del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient AsyncOperationHelper asyncOperationHelper;
	@Autowired private transient SoggettoService soggettoService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		if(posizioneStart == null) {
			posizioneStart = Integer.valueOf(0);
		}
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
		model.setRiepilogoRicerca((String) sessionHandler.getParametro(BilSessionParameter.RIEPILOGO_RICERCA_PREDOCUMENTO));
		// SIAC-4280
		// Caricamento liste
		caricaListaTipoAtto();
		caricaListaClasseSoggetto();
		caricaListaTipoFinanziamento();
	}

	/**
	 * Caricamento della lista del tipo atto da sessione
	 */
	private void caricaListaTipoAtto() {
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		model.setListaTipoAtto(listaTipoAtto);
	}

	/**
	 * Caricamento della lista della classe soggetto da sessione
	 */
	private void caricaListaClasseSoggetto() {
		List<CodificaFin> listaClasseSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		model.setListaClasseSoggetto(listaClasseSoggetto);
	}

	/**
	 * Caricamento della lista del tipo finanziamento da sessione
	 */
	private void caricaListaTipoFinanziamento() {
		List<TipoFinanziamento> listaTipiFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		model.setListaTipiFinanziamento(listaTipiFinanziamento);
	}

	/**
	 * Caricamento della fase di bilancio.
	 */
	protected void caricaFaseBilancio() {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
		
		/* La ricerca puo' abilitare i tasti funzione Associa Imputazioni Contabili e Definisce se il Bilancio e' in una di queste fasi:
		 *     Provvisorio
		 *     Gestione
		 *     Assestamento
		 *     Predisposizione a consuntivo
		 */
		boolean operazioniAbilitate = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		model.setFaseBilancioAbilitata(Boolean.valueOf(operazioniAbilitate));
	}
	
	/**
	 * Non abilitare le funzioni se nella ricerca sono stati impostati i criteri di ricerca:
	 * <ul>
	 *     <li>CAUSALEMANCANTE (non deve essere selezionato)</li>
	 *     <li>STATOOP diverso da nullo o dal valore INCOMPLETO (Stato Operativo PreDocumento Mod FIN)</il>
	 * </ul>
	 * Per Predisposizioni di Pagamento
	 * <ul>
	 *     <li>CONTOMANCANTE (non deve essere selezionato)</li>
	 * </ul>
	 * 
	 * @param spesa se il controllo sia da fare per la spesa
	 * @param statoOperativoPreDocumento lo stato da controllare
	 * 
	 * @return <code>true</code> se i criteri sono validi per l'abilitazione dell'operazione; <code>false</code> altrimenti
	 */
	protected boolean controllaCriteriRicerca(boolean spesa, StatoOperativoPreDocumento statoOperativoPreDocumento) {
		Boolean causaleMancante = sessionHandler.getParametro(BilSessionParameter.PREDOCUMENTO_CAUSALE_MANCANTE);
		Boolean contoMancante = sessionHandler.getParametro(BilSessionParameter.PREDOCUMENTO_CONTO_MANCANTE);
		StatoOperativoPreDocumento statoOperativo = sessionHandler.getParametro(BilSessionParameter.PREDOCUMENTO_STATO_OPERATIVO);
		
		boolean nonAbilitabili = Boolean.TRUE.equals(causaleMancante)
				|| (statoOperativo != null && !statoOperativoPreDocumento.equals(statoOperativo))
				|| (spesa && Boolean.TRUE.equals(contoMancante));
		return !nonAbilitabili;
	}

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid del documento da aggiornare: " + model.getUidDaAggiornare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.TRUE);
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid del predocumento da consultare: " + model.getUidDaConsultare());
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di inserimento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String inserisci() {
		final String methodName = "inserisci";
		log.debug(methodName, "Inserimento di un nuovo preDocumento");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di ripetizione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String ripeti() {
		final String methodName = "ripeti";
		log.debug(methodName, "Uid del documento da ripeti: " + model.getUidDaAggiornare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.TRUE);
		return SUCCESS;
	}
	
	// Metodi asincroni
	
	/**
	 * Inserimento dell'operazione asincrona.
	 * 
	 * @return l'id dell'operazione asincrona
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione
	 */
	protected Integer inserimentoOperazioneAsincrona() throws WebServiceInvocationFailureException {
		final String methodName = "inserimentoOperazioneAsincrona";
		// Inizio con il richiedere il token
		InserisciOperazioneAsincResponse response = asyncOperationHelper.inserisciOperazioneAsincrona(model.getAccount(),
				sessionHandler.getAzioneRichiesta(), model.getEnte(), model.getRichiedente());
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = "Errori nell'invocazione del servizio di inserisciOperazioneAsincrona";
			log.info(methodName, errorMessage);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMessage);
		}
		
		Integer idOperazioneAsincrona = response.getIdOperazione();
		log.debug(methodName, "Id operazione asincrona: " + idOperazioneAsincrona);
		model.setIdAzioneAsync(idOperazioneAsincrona);
		return idOperazioneAsincrona;
	}
	
	/**
	 * Controlla se la modifica dell'associazione imputazioni contabili sia abilitato
	 * @param sessionParameter il parametro di sessione
	 */
	protected void controlloModificaAssociazioneAbilitato(SessionParameter sessionParameter) {
		Boolean modificaAssociazioniContabiliAbilitato = sessionHandler.getParametro(sessionParameter);
		model.setModificaAssociazioniContabiliAbilitato(modificaAssociazioniContabiliAbilitato);
	}
	
	/**
	 * Effettua una validazione del Soggetto fornito in input.
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void validazioneSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "validazioneSoggetto";
		// Se non ho il soggetto, sono a posto
		if(model.getSoggetto() == null || StringUtils.isBlank(model.getSoggetto().getCodiceSoggetto())) {
			return;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		
		// Se ho errori, esco subito dopo aver caricato gli errori
		if(response.hasErrori()) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errori riscontrati nel servizio RicercaSoggettoPerChiave"));
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errori riscontrati nel servizio RicercaSoggettoPerChiave");
		}
		
		if(response.isFallimento() || response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto fornito dal servizio");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			throw new WebServiceInvocationFailureException("Nessun soggetto fornito dal servizio");
		}
		
		Soggetto soggetto = response.getSoggetto();
		checkCondition(!StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_ANNULLATO.getErrore());
		checkCondition(!StatoOperativoAnagrafica.BLOCCATO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_BLOCCATO.getErrore());
		
		// Aggiorno i dati del soggetto
		model.setSoggetto(soggetto);
	}
	
	/**
	 * Effettua una validazione dell'atto amministrativo fornito in input.
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void validazioneAttoAmministrativo() throws WebServiceInvocationFailureException {
		// Se non ho l'atto amministrativo sono a posto
		if(!checkProvvedimentoEsistente()) {
			return;
		}
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaProvvedimento");
		}
		
		// Il provvedimento deve esistere
		if(response.getListaAttiAmministrativi().isEmpty()) {
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
			return;
		}
		
		// Deve esistere un unico provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getStrutturaAmministrativoContabileAttoAmministrativo(), false);
		model.setAttoAmministrativo(response.getListaAttiAmministrativi().get(0));
	}
	
	/**
	 * Controlla se il provvedimento sia esistente, e pertanto da cercare.
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; esistente; <code>false</code> in caso contrario
	 */
	protected boolean checkProvvedimentoEsistente() {
		return model.getAttoAmministrativo() != null
			&& (
				model.getAttoAmministrativo().getAnno() != 0
				|| model.getAttoAmministrativo().getNumero() != 0
				|| (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0)
				|| (model.getAttoAmministrativo().getStrutturaAmmContabile() != null && model.getAttoAmministrativo().getStrutturaAmmContabile().getUid() != 0)
			);
	}

	/**
	 * Trova il submovimento di gestione nell'elenco, se presente.
	 * @param <SMG> la tipizzazione del submovimento di gestione
	 * 
	 * @param list la lista in cui cercare il submovimento
	 * @param sub  il submovimento da cercare
	 * 
	 * @return il submovimento legato, se presente; <code>null</code> in caso contrario
	 */
	protected <SMG extends MovimentoGestione> SMG findSubMovimentoByNumero(List<SMG> list, SMG sub) {
		if(list != null) {
			for(SMG smg : list) {
				if(smg.getNumeroBigDecimal().compareTo(sub.getNumeroBigDecimal()) == 0) {
					return smg;
				}
			}
		}
		return null;
	}
	
	/**
	 * Controlla se il capitolo sia valorizzato
	 * @param capitolo il capitolo
	 * @return true se il capitolo NON &eacute; valorizzato; false altrimenti
	 */
	protected boolean isCapitoloNonValorizzato(Capitolo<?, ?> capitolo) {
		return capitolo == null
			|| isCapitoloNonValorizzatoUEB(capitolo)
			|| isCapitoloNonValorizzatoNoUEB(capitolo);
	}
	
	/**
	 * Controlla se il capitolo sia valorizzato per l'UEB. Deve aver valorizzato numero capitolo, articolo e UEB
	 * @param capitolo il capitolo da controllare
	 * @return true se il capitolo NON &eacute; valorizzato e si sia in gestione UEB
	 */
	private boolean isCapitoloNonValorizzatoUEB(Capitolo<?, ?> capitolo) {
		return model.isGestioneUEB()
			&& capitolo.getNumeroCapitolo() == null
			&& capitolo.getNumeroArticolo() == null
			&& capitolo.getNumeroUEB() == null;
	}
	
	/**
	 * Controlla se il capitolo sia valorizzato per la gestione senza UEB. Deve aver valorizzato numero capitolo e articolo
	 * @param capitolo il capitolo da controllare
	 * @return true se il capitolo NON &eacute; valorizzato e non si sia in gestione UEB
	 */
	private boolean isCapitoloNonValorizzatoNoUEB(Capitolo<?, ?> capitolo) {
		return !model.isGestioneUEB()
				&& capitolo.getNumeroCapitolo() == null
				&& capitolo.getNumeroArticolo() == null;
	}
	
	/**
	 * JSON result per l'invocazione asincrona
	 * @author Marchino Alessandro
	 *
	 */
	public static class InvocazioneAsincronaJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 5908149589122868873L;
		private static final String INCLUDE_PROPERTIES = "errori.*, idAzioneAsync";
		
		/** Costruttore di default */
		public InvocazioneAsincronaJSONResult() {
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
