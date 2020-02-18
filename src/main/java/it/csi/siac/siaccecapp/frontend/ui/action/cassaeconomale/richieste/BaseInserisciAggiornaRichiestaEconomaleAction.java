/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action base per l'inserimento dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/01/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaRichiestaEconomaleAction<M extends BaseInserisciAggiornaRichiestaEconomaleModel> extends BaseRiepilogoRichiestaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 290347351895876932L;
	
	@Autowired private transient CassaEconomaleService cassaEconomaleService;
	@Autowired private transient CodificheService codificheService;

	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Qui presente solo poiche' richiamando super.prepare non posso inserirlo nelle sottoclassi
		setModel(null);
		super.prepare();
	}
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// SIAC-5623: Controlla se il caso d'uso sia abilitato
		checkCasoDUsoApplicabile();
		// Caricamento liste
		try {
			log.debug(methodName, "Caricamento delle liste per la visualizzazione della pagina");
			impostaCassaEconomale();
			// SIAC-4584
			checkCassa("la gestione della richiesta economale");
			caricaListe();
			log.debug(methodName, "Impostazione dei valori di default");
			impostazioneValoriDefaultStep1();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento dei dati : " + wsife.getMessage());
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		
		AzioniConsentite[] azioniRichieste = retrieveAzioniConsentite();
		boolean consentito = AzioniConsentiteFactory.isConsentitoAll(azioniConsentite, azioniRichieste);
		if(!consentito) {
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non si dispone dei permessi necessari per l'esecuzione").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	/**
	 * Ottiene le azioni consentite richieste per l'attivazione della funzionalit&agrave;
	 * @return le azioni richieste
	 */
	protected abstract AzioniConsentite[] retrieveAzioniConsentite();

	/**
	 * Compone il testo per il check della cassa economale
	 * @return il testo del check
	 */
	protected String getTestoCheckCassa() {
		return "la gestione della richiesta economale";
	}
	
	/**
	 * Caricamento delle liste per l'interfaccia.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListe() throws WebServiceInvocationFailureException {
		// Implementazione di default: non carico nulla
	}
	
	/**
	 * Impostazione dei valori di default per il primo step.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void impostazioneValoriDefaultStep1() throws WebServiceInvocationFailureException {
		// Implementazione di default: non carico nulla
	}
	
	/**
	 * Metodo di ingresso nello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step1() {
		// Segnaposto
		return SUCCESS;
	}
	
	/**
	 * Completa il primo step della funzionalit&agrave; per la richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		final String methodName = "completeStep1";
		// Ingresso nello step2. Carico solo le liste di classificatori necessarii
		try {
			log.debug(methodName, "Caricamento delle liste per il secondo step");
			caricaListeStep2();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento delle liste: " + wsife.getMessage());
			return INPUT;
		}
		log.debug(methodName, "Caricamento dei valori di default per il secondo step");
		impostazioneValoriDefaultStep2();
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle liste per il secondo step.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListeStep2() throws WebServiceInvocationFailureException {
		caricaListaModalitaPagamentoCassa();
		caricaListaModalitaPagamentoDipendente();
	}
	
	/**
	 * Impostazione dei valori di default per lo step2
	 */
	protected void impostazioneValoriDefaultStep2() {
		final String methodName = "impostazioneValoriDefaultStep2";
		if(model.getIsAggiornamento()) {
			// Se sono in aggiornamento, non faccio nulla
			log.debug(methodName, "Reimposto il movimento");
			// Imposto solo il movimento
			Movimento movimentoClone = ReflectionUtil.deepClone(model.getRichiestaEconomaleCopia().getMovimento());
			model.getRichiestaEconomale().setMovimento(movimentoClone);
			return;
		}
		inizializzaDatiMovimento();
	}
	
	/**
	 * Inizializzazione dei dati del movimento-
	 */
	protected void inizializzaDatiMovimento() {
		final String methodName = "inizializzaDatiMovimento";
		Movimento movimento = ottieniMovimentoDalModel();
		if(movimento == null) {
			log.debug(methodName, "Inizializzazione del movimento");
			movimento = new Movimento();
			impostaMovimentoNelModel(movimento);
		}
		
		Date now = new Date();
		log.debug(methodName, "Impostazione del default per la data movimento: " + now);
		movimento.setDataMovimento(now);
		TipoDiCassa tipoDiCassa = model.getCassaEconomale().getTipoDiCassa();
		log.debug(methodName, "Tipo di cassa economale: " + tipoDiCassa);
		if(TipoDiCassa.CONTANTI.equals(tipoDiCassa) || TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(tipoDiCassa)) {
			setModalitaDiPagamentoByTipoDiCassa(movimento, tipoDiCassa);
			impostaModalitaPagamentoDipendente(movimento, tipoDiCassa);
		}
		
	}

	/**
	 * Imposta il movimento nel model.
	 * 
	 * @param movimento il movimento da impostare
	 */
	protected void impostaMovimentoNelModel(Movimento movimento) {
		model.getRichiestaEconomale().setMovimento(movimento);
	}

	/**
	 * Imposta la modalit&agrave; di pagamento nel movimento a partire dal tipo di cassa economale.
	 * 
	 * @param movimento   il movimento da popolare
	 * @param tipoDiCassa il tipo di cassa da cui ottenere il dato
	 */
	protected void setModalitaDiPagamentoByTipoDiCassa(Movimento movimento, TipoDiCassa tipoDiCassa) {
		final String methodName = "setModalitaDiPagamentoByTipoDiCassa";
		List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa = model.getListaModalitaPagamentoCassa();
		for(ModalitaPagamentoCassa modalitaPagamentoCassa : listaModalitaPagamentoCassa) {
			if(tipoDiCassa.equals(modalitaPagamentoCassa.getTipoDiCassa())) {
				log.debug(methodName, "Impostazione del default per la modalita pagamento cassa: uid " + modalitaPagamentoCassa.getUid());
				movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
			}
		}
	}
	
	
	
	/**
	 * Impostazione della modalit&agrave; di pagamento del dipendente.
	 * <br/>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa non &eacute; contanti, occorre preimpostare il campo con il valore della modalit&agrave: di pagamento del dipendente
	 *     presente sull'anagrafica soggetto (se compilata).
	 * </p>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa &eacute; 'Contanti' impostare il campo fisso a Contanti senza lasciare la possibilit&agrave; di modificarlo
	 * </p>
	 * 
	 * @param movimento   il movimento da popolare
	 * @param tipoDiCassa il tipo di cassa
	 */
	protected void impostaModalitaPagamentoDipendente(Movimento movimento, TipoDiCassa tipoDiCassa) {
		final String methodName = "impostaModalitaPagamentoDipendente";
		if(TipoDiCassa.CONTANTI.equals(tipoDiCassa)) {
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), "CON");
			log.debug(methodName, "Cassa 'CONTANTI'. Ricercata la modalita' di pagamento di tipo 'CON'. Trovata? "
					+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
			movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
			return;
		}
		
		log.debug(methodName, "Cassa non contanti. Derivo la modalita' dal soggetto");
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = model.getListaModalitaPagamentoSoggetto();
		if(listaModalitaPagamentoSoggetto == null || listaModalitaPagamentoSoggetto.isEmpty() || listaModalitaPagamentoSoggetto.size() > 1) {
			log.debug(methodName, "Impossibile determinare univocamente la modalita' di pagamento del soggetto");
			return;
		}
		ModalitaPagamentoSoggetto mps = listaModalitaPagamentoSoggetto.get(0);
		if(mps == null || mps.getModalitaAccreditoSoggetto() == null) {
			log.debug(methodName, "Modalita' di pagamento non valorizzata correttamente");
			return;
		}
		
		ModalitaAccreditoSoggetto mas = mps.getModalitaAccreditoSoggetto();
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), mas.getCodice());
		log.debug(methodName, "Cassa non 'CONTANTI'. Ricercata la modalita' di pagamento per il tipo " + mas.getCodice() + " associata al soggetto. Trovata? "
				+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
		movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		
		// Imposto anche il dettaglio del pagamento
		impostaDettaglioDelPagamento(movimento, modalitaPagamentoDipendente);
	}
	/**
	 * Preparazione per il metodo {@link #caricaModalitaPagamentoDipendenteDaCassa()}.
	 */
	public void prepareCaricaDettaglioPagamentoDipendenteDaCassa() {
		ottieniMovimentoDalModel().setModalitaPagamentoDipendente(null);
	}
	
	/**
	 * Caricamento dei dati dal dettaglio del pagamento nella cassa economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaModalitaPagamentoDipendenteDaCassa() {
		final String methodName = "caricaModalitaPagamentoDipendenteDaCassa";
		Movimento movimento = ottieniMovimentoDalModel();
		ModalitaPagamentoCassa modalitaPagamentoCassa = movimento.getModalitaPagamentoCassa();
		
		log.debug(methodName, "Modalita cassa selezionata? " + (modalitaPagamentoCassa == null ? "false" : "true, con uid " + modalitaPagamentoCassa.getUid()));
		if(modalitaPagamentoCassa == null) {
			// Non ho la modalita' di pagamento. Esco
			// TODO: svuotare i dati?
			return SUCCESS;
		}
		// Ricerco la modalita' di pagamento
		modalitaPagamentoCassa = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoCassa(), modalitaPagamentoCassa);
		log.debug(methodName, "Modalita trovata? " + (modalitaPagamentoCassa != null));
		if(modalitaPagamentoCassa == null) {
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("modalita' di pagamento della cassa", "non e' presente tra quelle elencate"));
			return SUCCESS;
		}
		// Ho il dato. Estraggo il necessario
		impostaModalitaPagamentoDipendenteDaCassa(movimento, modalitaPagamentoCassa);
		return SUCCESS;
	}
	/**
	 * Impostazione della modalit&agrave; di pagamento del dipendente.
	 * <br/>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa non &eacute; contanti, occorre preimpostare il campo con il valore della modalit&agrave: di pagamento del dipendente
	 *     presente sull'anagrafica soggetto (se compilata).
	 * </p>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa &eacute; 'Contanti' impostare il campo fisso a Contanti senza lasciare la possibilit&agrave; di modificarlo
	 * </p>
	 * 
	 * @param movimento         il movimento da popolare
	 * @param modPagamentoCassa la modalita di pagamento cassa
	 */
	protected void impostaModalitaPagamentoDipendenteDaCassa(Movimento movimento, ModalitaPagamentoCassa modPagamentoCassa) {
		final String methodName = "impostaModalitaPagamentoDipendenteDaCassa";
		
		if(TipoDiCassa.CONTANTI.equals(modPagamentoCassa.getTipoDiCassa())) {
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), "CON");
			log.debug(methodName, "Cassa 'CONTANTI'. Ricercata la modalita' di pagamento di tipo 'CON'. Trovata? "
					+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
			movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
			return;
		}
		if(TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(modPagamentoCassa.getTipoDiCassa())) {
			log.debug(methodName, "Cassa non contanti. Derivo la modalita' dal soggetto");
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = model.getListaModalitaPagamentoSoggetto();
			if(listaModalitaPagamentoSoggetto == null || listaModalitaPagamentoSoggetto.isEmpty() || listaModalitaPagamentoSoggetto.size() > 1) {
				log.debug(methodName, "Impossibile determinare univocamente la modalita' di pagamento del soggetto");
				return;
			}
			ModalitaPagamentoSoggetto mps = listaModalitaPagamentoSoggetto.get(0);
			if(mps == null || mps.getModalitaAccreditoSoggetto() == null) {
				log.debug(methodName, "Modalita' di pagamento non valorizzata correttamente");
				return;
			}
			
			ModalitaAccreditoSoggetto mas = mps.getModalitaAccreditoSoggetto();
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), mas.getCodice());
			log.debug(methodName, "Cassa non 'CONTANTI'. Ricercata la modalita' di pagamento per il tipo " + mas.getCodice() + " associata al soggetto. Trovata? "
					+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
			movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
			
			// Imposto anche il dettaglio del pagamento
			//impostaDettaglioDelPagamento(movimento, modalitaPagamentoDipendente);
			return;
		}
	}
		
	
	/**
	 * Caricamento della lista delle modalit&agrave; di pagamento della cassa.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaModalitaPagamentoCassa() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaModalitaPagamentoCassa";
		List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_CASSA_ECONOMALE);
		if(listaModalitaPagamentoCassa == null) {
			log.debug(methodName, "Lista di ModalitaPagamentoCassa non presente in sessione. Caricamento da servizio");
			RicercaModalitaPagamentoCassa request = model.creaRequestRicercaModalitaPagamentoCassa();
			logServiceRequest(request);
			RicercaModalitaPagamentoCassaResponse response = cassaEconomaleService.ricercaModalitaPagamentoCassa(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String msgErrore = createErrorInServiceInvocationString(request, response);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			listaModalitaPagamentoCassa = response.getModalitaPagamentoCassa();
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_CASSA_ECONOMALE, listaModalitaPagamentoCassa);
		}
		model.setListaModalitaPagamentoCassa(listaModalitaPagamentoCassa);
	}
	
	/**
	 * Caricamento della lista delle modalit&agrave; di pagamento del dipendente.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaModalitaPagamentoDipendente() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaModalitaPagamentoDipendente";
		List<ModalitaPagamentoDipendente> listaModalitaPagamentoDipendente = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_DIPENDENTE);
		if(listaModalitaPagamentoDipendente == null) {
			log.debug(methodName, "Lista di ModalitaPagamentoCassa non presente in sessione. Caricamento da servizio");
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(ModalitaPagamentoDipendente.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String msgErrore = createErrorInServiceInvocationString(request, response);
				throw new WebServiceInvocationFailureException(msgErrore);
			}
			listaModalitaPagamentoDipendente = response.getCodifiche(ModalitaPagamentoDipendente.class);
			// Devo ancora farne il sort
			ComparatorUtils.sortByCodice(listaModalitaPagamentoDipendente);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_DIPENDENTE, listaModalitaPagamentoDipendente);
		}
		model.setListaModalitaPagamentoDipendente(listaModalitaPagamentoDipendente);
	}
	
	/**
	 * Preparazione per il metodo {@link #copiaRichiesta()}.
	 */
	public void prepareCopiaRichiesta() {
		// Pulisco il model
		model.setRichiestaEconomaleCopia(null);
	}
	
	/**
	 * Copia la richiesta economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String copiaRichiesta() {
		final String methodName = "copiaRichiesta";
		
		RicercaDettaglioRichiestaEconomale request = model.creaRequestRicercaDettaglioRichiestaEconomale(model.getRichiestaEconomaleCopia());
		logServiceRequest(request);
		RicercaDettaglioRichiestaEconomaleResponse response = richiestaEconomaleService.ricercaDettaglioRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		RichiestaEconomale richiestaEconomale = response.getRichiestaEconomale();
		
		if(!isValidTipoRichiestaEconomale(richiestaEconomale)) {
			addErrore(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la richiesta economale selezionata non e' della tipologia corretta."));
			return INPUT;
		}
		
		impostaDatiCopia(richiestaEconomale);
		
		return SUCCESS;
	}
	
	/**
	 * Controlla se il tipo della richiesta economale sia valida.
	 * 
	 * @param richiestaEconomale la richiesta da controllare
	 * 
	 * @return <code>true</code> se il tipo della richiesta %eacute; valido; <code>false</code> altrimenti
	 */
	protected boolean isValidTipoRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		// Implementazione di default: non permetto la copia
		return false;
	}
	
	/**
	 * Impostazione della matricola per l'aggiornamento.
	 * 
	 * @param richiestaEconomale la richiesta in cui impostare la matricola del soggetto
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	public void impostaMatricolaAggiornamento (RichiestaEconomale richiestaEconomale) throws WebServiceInvocationFailureException{
		//impostosoggetto 
		if (model.isGestioneHR()) {
			//devo ricalcolare la matricola collegandomi a HR per impostarla correttamente nel soggetto e non perdere dei dati
		//	model.setRichiestaEconomale(richiestaEconomale);
			ricercaDettaglioSoggetto();
			
		} else {
			if ((richiestaEconomale.getSoggetto()!=null) && (richiestaEconomale.getSoggetto().getMatricola()==null)) {
				richiestaEconomale.getSoggetto().setMatricola(richiestaEconomale.getMatricola());
			}
		}
		
	}
	
	/**
	 * Impostazione della matricola per l'aggiornamento.
	 * 
	 * @param richiestaEconomale la richiesta in cui impostare la matricola del soggetto
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	public void impostaSoggettoAggiornamento (RichiestaEconomale richiestaEconomale) throws WebServiceInvocationFailureException{
		//impostosoggetto 
		if (model.isGestioneHR()) {
			//devo ricalcolare la matricola collegandomi a HR per impostarla correttamente nel soggetto e non perdere dei dati
		//	model.setRichiestaEconomale(richiestaEconomale);
			ricercaDettaglioSoggetto();
			
		} else {
			if ((richiestaEconomale.getSoggetto()!=null) && (richiestaEconomale.getSoggetto().getMatricola()==null)) {
				richiestaEconomale.getSoggetto().setMatricola(richiestaEconomale.getMatricola());
			}
		}
		
	}
	

	/**
	 * Impostazione dei dati di copia della richiesta economale.
	 * 
	 * @param richiestaEconomale la richiesta i cui dati devono essere copiati
	 */
	protected void impostaDatiCopia(RichiestaEconomale richiestaEconomale) {
		// Imposto una richiesta vuota
		model.setRichiestaEconomale(new RichiestaEconomale());
		RichiestaEconomale richiestaEconomaleClone = ReflectionUtil.deepClone(richiestaEconomale);
		model.setRichiestaEconomaleCopia(richiestaEconomaleClone);
		//imposto i dati richiedente
		model.getRichiestaEconomale().setSoggetto(richiestaEconomale.getSoggetto()!=null ? richiestaEconomale.getSoggetto() : null);
		model.getRichiestaEconomale().setStrutturaDiAppartenenza(richiestaEconomale.getStrutturaDiAppartenenza()!=null ? richiestaEconomale.getStrutturaDiAppartenenza() : "");
		//dati Richiesta
		model.getRichiestaEconomale().setDescrizioneDellaRichiesta(richiestaEconomale.getDescrizioneDellaRichiesta()!=null ? richiestaEconomale.getDescrizioneDellaRichiesta() : "");
		model.getRichiestaEconomale().setDelegatoAllIncasso(richiestaEconomale.getDelegatoAllIncasso()!=null ? richiestaEconomale.getDelegatoAllIncasso() : "");
		model.getRichiestaEconomale().setNote(richiestaEconomale.getNote()!=null ? richiestaEconomale.getNote() : "");
		
		
		//non vogliono settare movimenti etc
		
		//model.setMovimentoGestione(richiestaEconomale.getImpegno());
		//model.setSubMovimentoGestione(richiestaEconomale.getSubImpegno());
		
		impostaClassificatoriGenerici(richiestaEconomale.getClassificatoriGenerici());
		
		
	}
	
	/**
	 * Impostazione dei dati di aggiornamento della richiesta economale.
	 * 
	 * @param richiestaEconomale la richiesta i cui dati devono essere aggiornati
	 */
	protected void impostaDatiAggiornamento(RichiestaEconomale richiestaEconomale) {
		model.setRichiestaEconomale(richiestaEconomale);
		model.getRichiestaEconomale().setDataCreazione(richiestaEconomale != null && richiestaEconomale.getDataCreazione() != null ? richiestaEconomale.getDataCreazione() : new Date());
		
		RichiestaEconomale richiestaEconomaleClone = ReflectionUtil.deepClone(richiestaEconomale);
		model.setRichiestaEconomaleCopia(richiestaEconomaleClone);
		model.setMovimentoGestione(richiestaEconomale != null ? richiestaEconomale.getImpegno() : null);
		model.setSubMovimentoGestione(richiestaEconomale != null ? richiestaEconomale.getSubImpegno() : null);
		
		impostaClassificatoriGenerici(richiestaEconomale != null ? richiestaEconomale.getClassificatoriGenerici() : null);
	}
	
	/**
	 * Impostazione dei classificatori generici.
	 * 
	 * @param classificatoriGenerici i classificatori da impostare
	 */
	private void impostaClassificatoriGenerici(List<ClassificatoreGenerico> classificatoriGenerici) {
		final String methodName = "impostaClassificatoriGenerici";
		if(classificatoriGenerici == null) {
			log.info(methodName, "Classificatori generici e' null");
			return;
		}
		for(ClassificatoreGenerico cg : classificatoriGenerici) {
			if(BilConstants.CODICE_CLASSIFICATORE51.getConstant().equals(cg.getTipoClassificatore().getCodice())) {
				model.setClassificatoreGenerico1(cg);
				log.debug(methodName, "CLASSIFICATORE51 uid " + model.getClassificatoreGenerico1().getUid());
			} else if(BilConstants.CODICE_CLASSIFICATORE52.getConstant().equals(cg.getTipoClassificatore().getCodice())) {
				model.setClassificatoreGenerico2(cg);
				log.debug(methodName, "CLASSIFICATORE52 uid " + model.getClassificatoreGenerico2().getUid());
			} else if(BilConstants.CODICE_CLASSIFICATORE53.getConstant().equals(cg.getTipoClassificatore().getCodice())) {
				model.setClassificatoreGenerico3(cg);
				log.debug(methodName, "CLASSIFICATORE53 uid " + model.getClassificatoreGenerico3().getUid());
			}
		
		}
	}
	
	/**
	 * Validazione per il metodo {@link #copiaRichiesta()}.
	 */
	public void validateCopiaRichiesta() {
		checkCondition(model.getRichiestaEconomaleCopia() != null && model.getRichiestaEconomaleCopia().getNumeroRichiesta() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Richiesta da copiare"), true);
		// Imposto la cassa economale
		model.getRichiestaEconomaleCopia().setCassaEconomale(model.getCassaEconomale());
	}
	
	/**
	 * Ingresso nel secondo step dell'inserimento della richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		// Segnaposto
		cleanImpegnoFromSession();
		return SUCCESS;
	}
	
	/**
	 * Ingresso nel terzo step dell'inserimento della richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step3() {
		// Segnaposto
		// Leggo l'informazione di successo
		leggiEventualiInformazioniAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Caricamento dei dati dei classificatori dalle corrispondenti liste
	 */
	protected void caricaClassificatoriDaLista() {
		// Modalita' pagamento cassa
		ModalitaPagamentoCassa mpc = model.getRichiestaEconomale().getMovimento().getModalitaPagamentoCassa();
		if(mpc != null) {
			ModalitaPagamentoCassa modalitaPagamentoCassa = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoCassa(), mpc);
			model.getRichiestaEconomale().getMovimento().setModalitaPagamentoCassa(modalitaPagamentoCassa);
		}
		
		// Modalita' pagamento dipendente
		ModalitaPagamentoDipendente mpd = model.getRichiestaEconomale().getMovimento().getModalitaPagamentoDipendente();
		if(mpc != null) {
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoDipendente(), mpd);
			model.getRichiestaEconomale().getMovimento().setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		}
	}
	
	/**
	 * Ottiene il movimento dal model.
	 * 
	 * @return il movimento impostato nel model
	 */
	protected Movimento ottieniMovimentoDalModel() {
		return model.getRichiestaEconomale().getMovimento();
	}
	
	/**
	 * Preparazione per il metodo {@link #caricaDettaglioPagamento()}.
	 */
	public void prepareCaricaDettaglioPagamento() {
		ottieniMovimentoDalModel().setModalitaPagamentoDipendente(null);
	}
	
	/**
	 * Caricamento dei dati dal dettaglio del pagamento nella cassa economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaDettaglioPagamento() {
		final String methodName = "caricaDettaglioPagamento";
		Movimento movimento = ottieniMovimentoDalModel();
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = movimento.getModalitaPagamentoDipendente();
		log.debug(methodName, "Modalita selezionata? " + (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
		if(modalitaPagamentoDipendente == null) {
			// Non ho la modalita' di pagamento. Esco
			// TODO: svuotare i dati?
			return SUCCESS;
		}
		// Ricerco la modalita' di pagamento
		modalitaPagamentoDipendente = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoDipendente(), modalitaPagamentoDipendente);
		log.debug(methodName, "Modalita trovata? " + (modalitaPagamentoDipendente != null));
		if(modalitaPagamentoDipendente == null) {
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("modalita' di pagamento del dipendente", "non e' presente tra quelle elencate"));
			return SUCCESS;
		}
		// Ho il dato. Estraggo il necessario
		impostaDettaglioDelPagamento(movimento, modalitaPagamentoDipendente);
		
		return SUCCESS;
	}

	/**
	 * Impostazione del dettaglio del pagamento.
	 * 
	 * @param movimento                   il movimento da popolare
	 * @param modalitaPagamentoDipendente la modalit&agrave; tramite cui effettuare il popolamento
	 */
	protected void impostaDettaglioDelPagamento(Movimento movimento, ModalitaPagamentoDipendente modalitaPagamentoDipendente) {
		if(modalitaPagamentoDipendente == null) {
			return;
		}
		String dettaglioPagamento = "";
		String bic = "";
		String contoCorrente = "";
		
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoByCodice = findModalitaPagamentoSoggettoByModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		
		if(isASB(modalitaPagamentoDipendente) || isASC(modalitaPagamentoDipendente)) {
			// 'Assegno bancario o assegno circolare' (ASB o ASC) non sara' preimpostato in quanto modalita' non presente sul soggetto,
			// e quindi sara' compilata dall'utente e conterra' il numero dell'assegno.
			
			// Non faccio nulla
		
		} else if(isACP(modalitaPagamentoDipendente)) {
			// Se viene scelto 'ACP' Accredito postale non sara' preimpostato ma sara' compilato dall'utente con il numero del conto postale.
			
			// Non faccio nulla
		}  else {
			//JIRA 2933 richeisto di ignorare i tipi e prendere i dati compilati
			// if(isCCB(modalitaPagamentoDipendente) || isCCP(modalitaPagamentoDipendente)) {
			
			// Se viene scelta la modalita' 'accredito bancario' (CCB) o 'accredito postale' (CCP) il campo va impostato con il codice IBAN del soggetto dipendente.
			// Se 'conto corrente postale' (CCP) il soggetto potrebbe non avere l'IBAN ma solo il numero del conto corrente postale.
			// Nel caso in cui la modalita' non sia presente sul soggetto, sara' l'utente a dover inserire l'IBAN
			if(hasMoreThanOneIBAN(listaModalitaPagamentoSoggettoByCodice)) {
				// Se viene selezionato 'Accredito bancario' (CCB) o 'Accredito postale' (CCP) e il soggetto ha piu' IBAN disponibili, presentare l'elenco con gli IBAN
				// e far scegliere all'utente quello da usare per il pagamento, e quindi quello che verra' salvato nel campo dettaglio.
				
				// Solo come segnaposto per la gestione dell'AJAX
				addMessaggio(ErroreCore.ENTITA_NON_SELEZIONATA.getErrore());
				
				// Vedere se restituire direttamente il modalino
				populateListModalitaPagamentoDiversoIban(listaModalitaPagamentoSoggettoByCodice);
			} else if(!listaModalitaPagamentoSoggettoByCodice.isEmpty()) {
				// Per le modalita' CCB e CCP, nel caso in cui venga recuperato l'IBAN dal soggetto dipendente impostare anche il campo BIC e il campo NUMERO CONTO CORRENTE
				// che vengono visualizzati sulla maschera.
				
				// E' equivalente ad avere un'unica MPS nella lista, se e' popolata
				ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = listaModalitaPagamentoSoggettoByCodice.get(0);
				
				dettaglioPagamento = modalitaPagamentoSoggetto.getIban();
				
				// Imposto BIC e Numero Conto Corrente
				bic = modalitaPagamentoSoggetto.getBic();
				contoCorrente = modalitaPagamentoSoggetto.getContoCorrente();
				movimento.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
			}
		}
		movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		movimento.setDettaglioPagamento(dettaglioPagamento);
		movimento.setBic(bic);
		movimento.setContoCorrente(contoCorrente);
	}

	/**
	 * Controlla se la modalit&agrave; &eacute; ASB (Assegno bancario)
	 * 
	 * @param modalitaPagamentoDipendente la modalit&agrave; da controllare
	 * 
	 * @return <code>true</code> se &eacute; ASB; <code>false</code> altrimenti
	 */
	private boolean isASB(ModalitaPagamentoDipendente modalitaPagamentoDipendente) {
		final String methodName = "isASB";
		boolean isASB = BilConstants.CODICE_MODALITA_PAGAMENTO_DIPENDENTE_ASSEGNO_BANCARIO.getConstant().equals(modalitaPagamentoDipendente.getCodice());
		log.debug(methodName, "isASB? " + isASB);
		return isASB;
	}

	/**
	 * Controlla se la modalit&agrave; &eacute; ASC (Assegno circolare)
	 * 
	 * @param modalitaPagamentoDipendente la modalit&agrave; da controllare
	 * 
	 * @return <code>true</code> se &eacute; ASC; <code>false</code> altrimenti
	 */
	private boolean isASC(ModalitaPagamentoDipendente modalitaPagamentoDipendente) {
		final String methodName = "isASC";
		boolean isASC = BilConstants.CODICE_MODALITA_PAGAMENTO_DIPENDENTE_ASSEGNO_CIRCOLARE.getConstant().equals(modalitaPagamentoDipendente.getCodice());
		log.debug(methodName, "isASC? " + isASC);
		return isASC;
	}

	/**
	 * Controlla se il soggetto ha pi&ugrave; di un IBAN.
	 * 
	 * @param listaModalitaPagamentoSoggetto la lista delle modalit&agrave; di pagamento da controllare
	 * 
	 * @return <code>true</code> se ha pi&uacute; di un IBAN; <code>false</code> altrimenti
	 */
	private boolean hasMoreThanOneIBAN(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		final String methodName = "hasMoreThanOneIBAN";
		
		final Set<String> ibans = new HashSet<String>();
		
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamentoSoggetto) {
			if(StringUtils.isNotBlank(mps.getIban())) {
				ibans.add(mps.getIban());
			}
		}
		final int ibanNumbers = ibans.size();
		
		log.debug(methodName, "How many IBANs does it have? " + ibanNumbers);
		return ibanNumbers > 1;
	}

	/**
	 * Controlla se la modalit&agrave; &eacute; ACP (Accredito postale)
	 * 
	 * @param modalitaPagamentoDipendente la modalit&agrave; da controllare
	 * 
	 * @return <code>true</code> se &eacute; ACP; <code>false</code> altrimenti
	 */
	private boolean isACP(ModalitaPagamentoDipendente modalitaPagamentoDipendente) {
		final String methodName = "isACP";
		boolean isACP = BilConstants.CODICE_MODALITA_PAGAMENTO_DIPENDENTE_ACCREDITO_POSTALE.getConstant().equals(modalitaPagamentoDipendente.getCodice());
		log.debug(methodName, "isACP? " + isACP);
		return isACP;
	}
	
	/**
	 * Ricerca le modalit&agrave; di pagamento del soggetto a partire dalla modalit&agrave; di pagamento del dipendente.
	 * 
	 * @param modalitaPagamentoDipendente la modalit&agrave; di pagamento del dipendente selezionata
	 * 
	 * @return le modalit&agrave; di pagamento del soggetto corrispondenti
	 */
	private List<ModalitaPagamentoSoggetto> findModalitaPagamentoSoggettoByModalitaPagamentoDipendente(ModalitaPagamentoDipendente modalitaPagamentoDipendente) {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>();
		String codiceModalitaPagamentoDipendente = modalitaPagamentoDipendente.getCodice();
		for(ModalitaPagamentoSoggetto mps : model.getListaModalitaPagamentoSoggetto()) {
			if(mps.getModalitaAccreditoSoggetto() != null && mps.getModalitaAccreditoSoggetto().getCodice() != null
					&& mps.getModalitaAccreditoSoggetto().getCodice().equals(codiceModalitaPagamentoDipendente)) {
				result.add(mps);
			}
		}
		return result;
	}
	
	/**
	 * Popolamento delle modalit&agrave; di pagamento con differente IBAN.
	 * 
	 * @param listaModalitaPagamentoSoggetto la lista delle modalit&agrave;
	 */
	private void populateListModalitaPagamentoDiversoIban(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		Map<String, ModalitaPagamentoSoggetto> map = new HashMap<String, ModalitaPagamentoSoggetto>();
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamentoSoggetto) {
			if(StringUtils.isNotBlank(mps.getIban())) {
				map.put(mps.getIban(), mps);
			}
		}
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoModel = model.getListaModalitaPagamentoSoggettoDifferenteIban();
		modalitaPagamentoSoggettoModel.clear();
		modalitaPagamentoSoggettoModel.addAll(map.values());
	}
	
	/**
	 * Preparazione per il metodo {@link #selezionaIban()}.
	 */
	public void prepareSelezionaIban() {
		model.setModalitaPagamentoSoggetto(null);
	}
	
	/**
	 * Selezione l'IBAN e imposta i dati nel model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String selezionaIban() {
		final String methodName = "selezionaIban";
		ModalitaPagamentoSoggetto mps = model.getModalitaPagamentoSoggetto();
		if(mps == null || mps.getUid() == 0) {
			log.debug(methodName, "Modalita non selezionata");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("IBAN"));
			return SUCCESS;
		}
		
		mps = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoSoggettoDifferenteIban(), mps);
		log.debug(methodName, "Modalita trovata? " + (mps == null));
		if(mps == null) {
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("IBAN", "non e' presente tra quelli elencate"));
			return SUCCESS;
		}
		
		// Popolo i dati del model
		Movimento movimento = ottieniMovimentoDalModel();
		
		log.debug(methodName, "IBAN: " + mps.getIban());
		movimento.setIban(mps.getIban());
		movimento.setDettaglioPagamento(mps.getIban());
		log.debug(methodName, "BIC: " + mps.getBic());
		movimento.setBic(mps.getBic());
		log.debug(methodName, "C/C: " + mps.getContoCorrente());
		movimento.setContoCorrente(mps.getContoCorrente());
		
		movimento.setModalitaPagamentoSoggetto(mps);
		
		return SUCCESS;
	}
	
	/**
	 * Imposta il messaggio di successp per lo step 3
	 */
	protected void impostaMessaggioSuccessoPerStep3(){
		List<Informazione> lista = new ArrayList<Informazione>();
		lista.add(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		sessionHandler.setParametro(BilSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, lista);
	}
	
	/**
	 * Impostazioni del dati clonati per l'annullamento.
	 * 
	 * @param richiestaEconomale la richiesta da cui clonare i dati
	 */
	protected void impostaDatiClonePerAnnulla(RichiestaEconomale richiestaEconomale) {
		model.setUidRichiesta(richiestaEconomale.getUid());
		model.setMovimentoGestioneCopia(SerializationUtils.clone(richiestaEconomale.getImpegno()));
		model.setSubMovimentoGestioneCopia(SerializationUtils.clone(richiestaEconomale.getSubImpegno()));
		for(ClassificatoreGenerico cg : richiestaEconomale.getClassificatoriGenerici()) {
			if(BilConstants.CODICE_CLASSIFICATORE51.getConstant().equals(cg.getTipoClassificatore().getCodice())) {
				model.setClassificatoreGenerico1Copia(SerializationUtils.clone(cg));
			} else if(BilConstants.CODICE_CLASSIFICATORE52.getConstant().equals(cg.getTipoClassificatore().getCodice())) {
				model.setClassificatoreGenerico2Copia(SerializationUtils.clone(cg));
			} else if(BilConstants.CODICE_CLASSIFICATORE53.getConstant().equals(cg.getTipoClassificatore().getCodice())) {
				model.setClassificatoreGenerico3Copia(SerializationUtils.clone(cg));
			}
		}
	}
	
	/**
	 * Impostazione dei dati clonati per annullamento rendiconto.
	 * 
	 * @param rendicontoRichiesta il rendiconto da cui clonare i dati
	 */
	protected void impostaDatiClonePerAnnullaRendiconto(RendicontoRichiesta rendicontoRichiesta) {
		model.setUidRendiconto(rendicontoRichiesta.getUid());
		model.setMovimentoGestioneCopia(SerializationUtils.clone(rendicontoRichiesta.getImpegno()));
		model.setSubMovimentoGestioneCopia(SerializationUtils.clone(rendicontoRichiesta.getSubImpegno()));
		
	}
	
	/**
	 * Controlla il dettaglio del pagamento.
	 * <br/>
	 * Il dettaglio &eacute; obbligatorio quando la modalit&agrave; di pagamento &eacute; differente da <code>CONTANTI</code>
	 * 
	 * @param movimento il movimento
	 */
	protected void checkDettaglioPagamento(Movimento movimento) {
		ModalitaPagamentoDipendente mdp = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoDipendente(), movimento.getModalitaPagamentoDipendente());
		checkCondition(StringUtils.isNotBlank(movimento.getDettaglioPagamento())
				|| (mdp != null && BilConstants.CODICE_MODALITA_PAGAMENTO_DIPENDENTE_CONTANTI.getConstant().equals(mdp.getCodice())),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Dettaglio del pagamento"));
	}
	
	/**
	 * Completamento del terzo step dell'inserimento.
	 * @data_modifica 31/03/2015
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep3() {
		final String methodName = "completeStep3";
		StampaRicevutaRichiestaEconomale request = model.creaRequestStampaRicevutaRichiestaEconomale(model.getRichiestaEconomale());
		logServiceRequest(request);
		StampaRicevutaRichiestaEconomaleResponse response = stampaCassaEconomaleService.stampaRicevutaRichiestaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		//verifico se la response riporta il file
		if(response.getReport() == null) {
			log.debug(methodName, "Nessun file presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		//Creazione dei dati per il file pdf
		File file = response.getReport();
		model.setContentType(file.getMimeType());
		model.setContentLength(file.getDimensione());
		model.setFileName(file.getNome());
		
		InputStream inputStream = new ByteArrayInputStream(file.getContenuto());
		model.setInputStream(inputStream);
		
		log.debug(methodName, "generazione pdf in corso...");
		return SUCCESS;
	}
	
	/**
	 * Completamento del terzo step dell'inserimento con eventuale stampa.
	 * 
	 * @param rendicontoRichiesta il rendiconto da stampare
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	protected String innerCompleteStep3Rendiconto(RendicontoRichiesta rendicontoRichiesta){
		final String methodName = "innerCompleteStep3Rendiconto";
		StampaRicevutaRendicontoRichiestaEconomale request = model.creaRequestStampaRicevutaRendicontoRichiestaEconomale(rendicontoRichiesta);
		logServiceRequest(request);
		StampaRicevutaRendicontoRichiestaEconomaleResponse response = stampaCassaEconomaleService.stampaRendicontoRicevutaRichiestaEconomale(request);
		logServiceResponse(response);

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		//verifico se la response riporta il file
		if(response.getReport() == null) {
			log.debug(methodName, "Nessun file presente");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		//Creazione dei dati per il file pdf
		// Prendo il primo file
		File file = response.getReport();
		model.setContentType(file.getMimeType());
		model.setContentLength(file.getDimensione());
		model.setFileName(file.getNome());
		
		InputStream inputStream = new ByteArrayInputStream(file.getContenuto());
		model.setInputStream(inputStream);
		
		log.debug(methodName, "generazione pdf in corso...");
		return SUCCESS;
	}
	
	/**
	 * Visualizzazione degli importi della cassa economale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String importiCassa() {
		final String methodName = "importiCassa";
		
		log.debug(methodName, "Calcolo importi cassa economale per cassa con uid " + model.getCassaEconomale().getUid());
		try {
			CassaEconomale cassaEconomaleConImporti = calcolaImportiCassaEconomale();
			model.getCassaEconomale().setDisponibilitaCassaContanti(cassaEconomaleConImporti.getDisponibilitaCassaContantiNotNull());
			model.getCassaEconomale().setDisponibilitaCassaContoCorrente(cassaEconomaleConImporti.getDisponibilitaCassaContoCorrenteNotNull());
		} catch(WebServiceInvocationFailureException wsife) {
			// Errore nel calcolo... Non importa: ritorno quanto ho in pancia
			log.warn(methodName, "Errore nel calcolo degli importi. Restituisco i valori precedenti. Errore riscontrato: " + wsife.getMessage());
		}
		
		return SUCCESS;
	}
	
	/**
	 * Calcolo degli importi della cassa economale.
	 * 
	 * @return la cassa economale con gli importi calcolati
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected CassaEconomale calcolaImportiCassaEconomale() throws WebServiceInvocationFailureException {
		return calcolaImportiCassaEconomale(model.getCassaEconomale());
	}
	
	/**
	 * Calcolo degli importi della cassa economale.
	 * 
	 * @param cassaEconomale la cassa da cui calcolare gli importi
	 * 
	 * @return la cassa economale con gli importi calcolati
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected CassaEconomale calcolaImportiCassaEconomale(CassaEconomale cassaEconomale) throws WebServiceInvocationFailureException {
		final String methodName = "calcolaImportiCassaEconomale";
		
		CalcolaDisponibilitaCassaEconomale request = model.creaRequestCalcolaDisponibilitaCassaEconomale(cassaEconomale);
		logServiceRequest(request);
		CalcolaDisponibilitaCassaEconomaleResponse response = cassaEconomaleService.calcolaDisponibilitaCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String msg = createErrorInServiceInvocationString(request, response);
			log.info(methodName, msg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(msg);
		}
		
		return response.getCassaEconomale();
	}
	
	/**
	 * Ottiene il soggetto dal model.
	 * 
	 * @return il soggetto nel model
	 */
	protected Soggetto getSoggettoDaModel() {
		return model.getRichiestaEconomale().getSoggetto();
	}
	/**
	 * Ricerca il dettaglio del soggetto della richiesta economale.
	 * @param richiestaEconomale la richiesta economale per cui effettuare la ricerca
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void ricercaDettaglioSoggetto(RichiestaEconomale richiestaEconomale) throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioSoggetto";
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		Soggetto soggettoNelModel = getSoggettoDaModel();
		
		if(soggettoNelModel==null || soggetto == null || listaSedeSecondariaSoggetto == null || listaModalitaPagamentoSoggetto == null ||  !soggettoNelModel.getMatricola().equals(soggetto.getMatricola())) {
			String codiceSoggetto = calcolaCodiceSoggetto(soggettoNelModel, model.getRichiestaEconomale());
			log.debug(methodName, "Caricamento dei dati da servizio per soggetto " + codiceSoggetto);
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(model.getRichiestaEconomale());
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			if(response.getSoggetto() == null) {
				String errorMsg = "Nessun soggetto corrispondente al codice " + codiceSoggetto + " trovato";
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", codiceSoggetto));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			soggetto = response.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		model.getRichiestaEconomale().setSoggetto(soggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	/**
	 * Ricerca il dettaglio del soggetto della richiesta economale.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void ricercaDettaglioSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioSoggetto";
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		Soggetto soggettoNelModel = getSoggettoDaModel();
		
		if(soggettoNelModel == null || soggetto == null || listaSedeSecondariaSoggetto == null || listaModalitaPagamentoSoggetto == null || !soggettoNelModel.getMatricola().equals(soggetto.getMatricola())) {
			String codiceSoggetto = calcolaCodiceSoggetto(soggettoNelModel, model.getRichiestaEconomale());
			log.debug(methodName, "Caricamento dei dati da servizio per soggetto " + codiceSoggetto);
			
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(model.getRichiestaEconomale());
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			if(response.getSoggetto() == null) {
				String errorMsg = "Nessun soggetto corrispondente al codice " + codiceSoggetto + " trovato";
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", codiceSoggetto));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			soggetto = response.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		model.getRichiestaEconomale().setSoggetto(soggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Calcolo del codice del soggetto.
	 * @param soggettoModel il soggetto presente nel model
	 * @param richiestaEconomale la richiesta economale
	 * 
	 * @return il codice del soggetto
	 */
	protected String calcolaCodiceSoggetto(Soggetto soggettoModel, RichiestaEconomale richiestaEconomale) {
		if(soggettoModel == null) {
			return richiestaEconomale.getMatricola();
		}
		if(StringUtils.isNotBlank(soggettoModel.getMatricola())) {
			return soggettoModel.getMatricola();
		}
		return soggettoModel.getCodiceSoggetto();
	}
	
	/**
	 * Calcolo del dettaglio del pagamento del rendiconto
	 * @param importoDaIntegrare l'importo da integrare
	 * @return il dettaglio del pagamento
	 */
	protected String calcolaDettaglioPagamentoRendiconto(BigDecimal importoDaIntegrare) {
		// SIAC-4715
		// SIAC-5192: se e' una restituzione imposto il numero del conto della cassa; se e' un'integrazione uso il conto corrente della richiesta
		if(importoDaIntegrare != null && importoDaIntegrare.signum() > 0) {
			return model.getRichiestaEconomaleCopia().getMovimento().getDettaglioPagamento();
		}
		return model.getCassaEconomale().getNumeroContoCorrente();
	}
}
