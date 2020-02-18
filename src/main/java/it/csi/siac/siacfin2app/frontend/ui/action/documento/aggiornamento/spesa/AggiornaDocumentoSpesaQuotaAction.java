/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.messaggio.MessaggioBil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverseResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiCertificazioneCrediti;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action per l'aggiornamento del Documento di spesa, sezione Quote.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 * @version 1.0.1 - 22/10/2015 - CR 2463 (gestione GEN/PCC per blocco provvedimento)
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaQuotaAction extends AggiornaDocumentoSpesaBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6688139370700008296L;
	/** Il suffisso per le action */
	private static final String SUFFIX = "_QUOTE";
	/** Pattern per il CIG */
	private static final Pattern CIG_PATTERN = Pattern.compile("[A-Z0-9]{10}");
	/** Pattern per il Cup */
	private static final Pattern CUP_PATTERN = Pattern.compile("[A-Z][A-Z0-9]{2}[A-Z][A-Z0-9]{11}");
	
	/**
	 * Restituisce la lista delle quote relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaQuote() {
		//imposyp nel model i dati relativi alle quote
		model.impostaFlagAutoCalcoloImportoSplitQuote();
		model.impostaFlagEditabilitaRitenute();
		return SUCCESS;
	}
	
	/**
	 * Filtra le modalita di pagamento a partire dalla sede secondaria.
	 * 
	 * @return una stringa corrispondente al risultato dellinvocazione
	 */
	public String filtraModalitaPagamentoSoggetto() {
		//imposto le modelita' di pagamento del soggetto filtrando quelle non valide
		impostaListaModalitaPagamentoFiltrate();
		return SUCCESS;
	}
	
	/**
	 * Imposta tutte le modalita' di pagamento senza filtri.
	 * 
	 * @return una stringa corrispondente al risultato dellinvocazione
	 */
	public String impostaModalitaPagamentoSoggettoNonFiltrate() {
		//imposto le modelita' di pagamento del soggetto senza filtrare quelle valide
		impostaModalitaPagamentoNonFiltrate();
		return SUCCESS;
	}
	
	/**
	 * Carica la lista del TipoIvaSplitReverse nel model.
	 */
	protected void caricaListaTipoIvaSplitReverse() {
		//e' possibile selezionare tutti i tipi di iva split reverse
		List<TipoIvaSplitReverse> lista = Arrays.asList(TipoIvaSplitReverse.values());
		model.setListaTipoIvaSplitReverse(lista);
	}
	
	/**
	 * Pulisce i campi relativi all'inserimento di una nuova quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserimentoNuovaQuota() {
		//pulisco i dati relativi alle quote
		cleanQuote();
		
		model.setImpegnoQuotaDisabilitato(Boolean.FALSE);
		model.setImportoQuotaDisabilitato(Boolean.FALSE);
		model.setProvvedimentoQuotaDisabilitato(Boolean.FALSE);
		
		model.checkAndIncrementProgressivoSubdocumento();
		
		// Preimposto il valore di default
		model.getSubdocumento().setCommissioniDocumento(CommissioniDocumento.ESENTE);
		caricaListaTipoIvaSplitReverse();
		DocumentoSpesa documento = model.getDocumento();
		// Costruisco la causale
		String causale = creaCausaleOrdinativo(documento);
		model.getSubdocumento().setCausaleOrdinativo(causale);
		model.getSubdocumento().setDataScadenza(model.getDocumento().getDataScadenza());
		// Imposto la lista delle causali filtrate
		impostaListaModalitaPagamentoFiltrate();
		
		//SIAC-4406 imposto la descrizione della quota a quella del documento
		model.getSubdocumento().setDescrizione(documento.getDescrizione()!= null? documento.getDescrizione() : "");
		
		// Impostazione conto tesoreria (JIRA-2324)
		impostaContoTesororeriaSeUnivoco();
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
		
		// SIAC-5115
		model.setListaSospensioneSubdocumento(new ArrayList<SospensioneSubdocumento>());
		
		// Clono il subdocumento
		SubdocumentoSpesa clone = ReflectionUtil.deepClone(model.getSubdocumento());
		impostaQuotaDaInserire();
		
		// SIAC-5311 SIOPE+
		model.setDataScadenzaOriginale(clone.getDataScadenza());
		model.setDataScadenzaDopoSospensioneOriginale(clone.getDataScadenzaDopoSospensione());
		
		//SIAC-5469
		model.setShowSiopeAssenzaMotivazione(false);
		
		model.setSuffisso(SUFFIX);
		return SUCCESS;
	}

	/**
	 * Se il conto tesoreria &eacute; univoco, lo si assuma di default.
	 */
	private void impostaContoTesororeriaSeUnivoco() {
		if(model.getListaContoTesoreria().size() != 1) {
			//il conto tesoreria non risulta essere univoco, esco
			return;
		}
		// Se ho un'unica occorrenza, la seleziono
		ContoTesoreria contoTesoreria = model.getListaContoTesoreria().get(0);
		model.setContoTesoreria(contoTesoreria);
	}

	/**
	 * Popola i campi relativi all'aggiornamento della quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornamentoNuovaQuota() {
		final String methodName = "inizioAggiornamentoNuovaQuota";
		//imposto i valori di default
		model.setImpegnoQuotaDisabilitato(Boolean.FALSE);
		model.setImportoQuotaDisabilitato(Boolean.FALSE);
		model.setProvvedimentoQuotaDisabilitato(Boolean.FALSE);
		
		//carico la lista di tipo iva split reverse
		caricaListaTipoIvaSplitReverse();
		
		model.setSuffisso(SUFFIX);
		// SIAC-5115 inizio con il pulire i dati
		model.setListaSospensioneSubdocumento(new ArrayList<SospensioneSubdocumento>());
		
		// Ottengo il subdoc per id
		SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
		subdoc.setUid(model.getUidQuota());
		subdoc = ComparatorUtils.searchByUid(model.getListaSubdocumentoSpesa(), subdoc);
		
		// Clono il subdocumento
		SubdocumentoSpesa clone = ReflectionUtil.deepClone(subdoc);
		
		// Se non ho la causale dell'ordinativo, la creo ex novo
		if(StringUtils.isBlank(clone.getCausaleOrdinativo())){
			DocumentoSpesa documento = model.getDocumento();
			// Creo la causale
			String causale = creaCausaleOrdinativo(documento);
			clone.setCausaleOrdinativo(causale);
		}
		
		log.debug(methodName, "Importo originale: " + subdoc.getImporto() + " || Importo clonato: " + clone.getImporto());
		log.debug(methodName, "Importo da dedurre originale: " + subdoc.getImporto() + " || Importo da dedurre clonato: " + clone.getImporto());
		// Imposto il clone nel model
		impostaQuotaDaAggiornare(clone);
		
		//devo effettuare dei controlli sul provvedimento
		controllaProvvedimento();

		//imposto la lista di modalita' pagamento a partire da quella filtrata
		impostaListaModalitaPagamentoFiltrate();
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(FormatUtils.formatNullableBoolean(subdoc.getFlagConvalidaManuale(), "M", "A"));
		// SIAC-5311 SIOPE+
		model.setDataScadenzaOriginale(subdoc.getDataScadenza());
		model.setDataScadenzaDopoSospensioneOriginale(subdoc.getDataScadenzaDopoSospensione());
		
		return SUCCESS;
	}
	
	/**
	 * Crea la causale dell'ordinativo per il documento.
	 * 
	 * @param documento il documento
	 * 
	 * @return la causale standard
	 */
	private String creaCausaleOrdinativo(DocumentoSpesa documento) {
		//utilizzo dello stringbuilder necessrio
		StringBuilder sb = new StringBuilder();
		sb.append("DOCUMENTO N. ")
			.append(documento.getNumero())
			.append(" DEL ")
			.append(FormatUtils.formatDate(documento.getDataEmissione()));
		return sb.toString();
	}
	
	/**
	 * Popola i campi relativi alla ripetizione dell'inserimento della quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioRipetiNuovaQuota() {
		SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
		subdoc.setUid(model.getUidQuota());
		subdoc = ComparatorUtils.searchByUid(model.getListaSubdocumentoSpesa(), subdoc);
		// Clono il subdocumento
		SubdocumentoSpesa clone = ReflectionUtil.deepClone(subdoc);
		// Pulisco i dati
		cleanQuote();
		
		//imposto i dati nel modello
		model.setImpegnoQuotaDisabilitato(Boolean.FALSE);
		model.setImportoQuotaDisabilitato(Boolean.FALSE);
		model.setProvvedimentoQuotaDisabilitato(Boolean.FALSE);
		model.getSubdocumento().setDataScadenza(model.getDocumento().getDataScadenza());
		
		// SIAC-5115
		model.setListaSospensioneSubdocumento(new ArrayList<SospensioneSubdocumento>());
		
		// Impostro la quota
		impostaQuotaDaRipetere(clone);
		
		// Incremento il progressivo
		model.checkAndIncrementProgressivoSubdocumento();
		model.setSuffisso(SUFFIX);
		
		//imposto la lista di modalita' pagamento filtrate
		impostaListaModalitaPagamentoFiltrate();
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
		
		// SIAC-5311 SIOPE+
		model.setDataScadenzaOriginale(clone.getDataScadenza());
		model.setDataScadenzaDopoSospensioneOriginale(clone.getDataScadenzaDopoSospensione());
		
		//SIAC-5469
		model.setShowSiopeAssenzaMotivazione(false);
		
		return SUCCESS;
	}
	
	/**
	 * Prepare per l'inserimento della nuova quota.
	 * <br>
	 * Workaround per il salvataggio del movimento di gestione
	 */
	public void prepareInserimentoNuovaQuota() {
		//pulisco i dati nel model per le quote
		puliziaPreparePerQuote();
	}
	
	/**
	 * Inserisce la quota fornita dall'utente in quelle associate al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	
	
	
	public String inserimentoNuovaQuota() {
		final String methodName = "inserimentoNuovaQuota";
		// Valido i dati
		validazioneInserimentoNuovaQuota();
		if(hasErrori()) {
			// Ho errori: esco
			log.info(methodName, "Errori di validazione presenti per l'inserimento");
			return SUCCESS;
		}
		if(hasMessaggi()) {
			// Ho dei messaggi di conferma: ritorno all'utente
			log.info(methodName, "Messaggi di conferma presenti");
			return ASK;
		}
		
		//ora posso chiamare il servizio
		InserisceQuotaDocumentoSpesa req = model.creaRequestInserisceQuotaDocumentoSpesa();
		logServiceRequest(req);
		
		InserisceQuotaDocumentoSpesaResponse res = documentoSpesaService.inserisceQuotaDocumentoSpesa(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Ho degli errori: loggo ed esco
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return SUCCESS;
		}
		
		// Nuova quota inserita. Faccio una ricerca di dettaglio delle quote per ottenere le quote aggiornate
		// (per evitare problemi dovuti alla concorrenza di inserimento)
		ricalcolaLeQuoteAssociate();
		
		//calcolo i dati da mostrare a video
		model.calcoloImporti();
		model.impostaFlagAutoCalcoloImportoSplitQuote();
		model.impostaFlagEditabilitaRitenute();
		
		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();
		
		// Ricalcolo stato operativo e data inizio validita stato
		model.getDocumento().setStatoOperativoDocumento(res.getSubdocumentoSpesa().getDocumento().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(res.getSubdocumentoSpesa().getDocumento().getDataInizioValiditaStato());
		
		// SIAC-5072: ricarico i dati di sospensione
		model.setFlagDatiSospensioneEditabili(Boolean.valueOf(isDatiSospensioneEditabili()));
		
		//controllo se sia necessario attivare le registrazioni contabili
		checkAttivazioneRegContabili();
		
		//SIAC-6261
		impostaMessaggioDatiDurc(res.getSubdocumentoSpesa(), res.getSubdocumentoSpesa().getModalitaPagamentoSoggetto());
		
		return SUCCESS;
	}
	
	/**
	 * Elimina la quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaQuota() {
		final String methodName = "eliminaQuota";
		// Valido i dati
		validaEliminaQuota();
		
		if(hasErrori()) {
			// Errore nella validazione dei dati
			log.info(methodName, "Errore nella validazione del metodo");
			return SUCCESS;
		}
		
		//ora posso chiamare il servizio
		EliminaQuotaDocumentoSpesa req = model.creaRequestEliminaQuotaDocumentoSpesa();
		logServiceRequest(req);
		EliminaQuotaDocumentoSpesaResponse res = documentoSpesaService.eliminaQuotaDocumentoSpesa(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return SUCCESS;
		}
		log.debug(methodName, "Quota eliminata");
		
		// Ricalcolo le quote associate
		ricalcolaLeQuoteAssociate();
		model.calcoloImporti();
		model.impostaFlagAutoCalcoloImportoSplitQuote();
		model.impostaFlagEditabilitaRitenute();
		
		// Ricalcolo stato operativo e data inizio validita stato
		model.getDocumento().setStatoOperativoDocumento(res.getSubdocumentoSpesa().getDocumento().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(res.getSubdocumentoSpesa().getDocumento().getDataInizioValiditaStato());
		
		// SIAC-5072: ricarico i dati di sospensione
		model.setFlagDatiSospensioneEditabili(Boolean.valueOf(isDatiSospensioneEditabili()));
		
		//controllo se sia da rendere possibile l'attivazione delle registrazione
		checkAttivazioneRegContabili();
		//imposto un messaggio all'utente che indichi che l'operazione e' avvenuta con successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Prepare per l'aggiornamento della quota.
	 * <br>
	 * Workaround per il salvataggio del movimento di gestione
	 */
	public void prepareAggiornamentoQuota() {
		//pulisco i dati
		cleanQuotePerAggiornamento();
		puliziaPreparePerQuote();
	}
	
	/**
	 * Aggiorna la quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamentoQuota() {
		final String methodName = "aggiornamentoQuota";
		// Stessa validazione del caso dell'inserimento
		validazioneAggiornamentoNuovaQuota();
		
		if(hasErrori()) {
			// Se ho errori di validazione, esco
			log.info(methodName, "Errori di validazione presenti per l'aggiornamento");
			return SUCCESS;
		}
		if(hasMessaggi()) {
			// Ho dei messaggi di conferma: ritorno all'utente
			log.info(methodName, "Messaggi di conferma presenti");
			return ASK;
		}
		log.debug(methodName, "Validazione terminata senza errori");
		
		log.debug(methodName, "il provvisorio e' null? " + model.getSubdocumento().getProvvisorioCassa() == null);
				
		//ora posso chiamare il servizio
		AggiornaQuotaDocumentoSpesa req = model.creaRequestAggiornaQuotaDocumentoSpesa();
		logServiceRequest(req);
		
		log.debug(methodName, "il provvisorio nella request e' null? " + req.getSubdocumentoSpesa().getProvvisorioCassa() == null);
		
		AggiornaQuotaDocumentoSpesaResponse res = documentoSpesaService.aggiornaQuotaDocumentoSpesa(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return SUCCESS;
		}
		log.debug(methodName, "Quota aggiornata");
		
		impostaMessaggioDatiDurc(model.getSubdocumento(), model.getSubdocumento().getModalitaPagamentoSoggetto());
		
		// Ricalcolo gli importi
		ricalcolaLeQuoteAssociate();
		model.calcoloImporti();
		model.impostaFlagAutoCalcoloImportoSplitQuote();
		model.impostaFlagEditabilitaRitenute();
		
		// Ricalcolo stato operativo e data inizio validita stato
		model.getDocumento().setStatoOperativoDocumento(res.getSubdocumentoSpesa().getDocumento().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(res.getSubdocumentoSpesa().getDocumento().getDataInizioValiditaStato());
		
		// SIAC-5072: ricarico i dati di sospensione
		model.setFlagDatiSospensioneEditabili(Boolean.valueOf(isDatiSospensioneEditabili()));
		
		//controllo se sia possibile attivare le registrazioni contabili
		checkAttivazioneRegContabili();
		//imposto un messaggio all'utente che indichi che l'operazione e' avvenuta con successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	private void impostaMessaggioDatiDurc(SubdocumentoSpesa subdocumento, ModalitaPagamentoSoggetto mdp) {
		final String methodName = "impostaMessaggioDatiDurc";
		if(subdocumento.getImpegno() == null || !subdocumento.getImpegno().isFlagSoggettoDurc()) {
			return;
		}
		log.warn(methodName, "modalita pagamento che ho associato al subdocumento: " + mdp.getUid());
		ModalitaPagamentoSoggetto modalitaPagamentoCessione = filtraMdpCessione(mdp);
		
		Date dataFineValiditaDurc =  modalitaPagamentoCessione != null && modalitaPagamentoCessione.getSoggettoCessione() != null?
				modalitaPagamentoCessione.getSoggettoCessione().getDataFineValiditaDurc() : model.getSoggetto().getDataFineValiditaDurc();

		Date now = new Date();
		
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		//SIAC-7239
		String dateFineDurc = dataFineValiditaDurc != null? new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc) : null;
		
		if(dateFineDurc == null || dateNow.compareTo(dateFineDurc)>0) {
			model.addMessaggioSenzaRichiestaConferma(MessaggioBil.DATI_DURC_NON_COERENTI.getMessaggio("Il subdocumento inserito ha un impegno che richiede la conferma durc ma il durc del soggetto risulta essere scaduto."));
		}
		
	}

	/**
	 * Filtra mdp cessione.
	 *
	 * @param mdp the mdp
	 * @return the modalita pagamento soggetto
	 */
	private ModalitaPagamentoSoggetto filtraMdpCessione(ModalitaPagamentoSoggetto mdp) {
		for (ModalitaPagamentoSoggetto mdpLoop : model.getListaModalitaPagamentoSoggetto()) {
			if(mdpLoop.getModalitaPagamentoSoggettoCessione2() != null && mdpLoop.getModalitaPagamentoSoggettoCessione2().getUid() != 0 && mdpLoop.getModalitaPagamentoSoggettoCessione2().getUid() == mdp.getUid()) {
				return mdpLoop;
			}
		}
		return null;
	}

	/**
	 * Effettua una pulizia per il prepare delle quote.
	 */
	private void puliziaPreparePerQuote() {
		// Pulisco tutti i campi
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.setSedeSecondariaSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setAttoAmministrativo(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setTipoAtto(null);
		model.setProseguireConElaborazione(null);
		model.setProseguireConElaborazioneAttoAmministrativo(Boolean.FALSE);
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
		// SIAC-5311 SIOPE+
		model.setDataScadenzaOriginale(null);
		model.setDataScadenzaDopoSospensioneOriginale(null);
	}
	
	/**
	 * Valida l'inserimento di una nuova quota.
	 */
	private void validazioneInserimentoNuovaQuota() {
		//valido i dati per le quote
		validazioneNuovaQuota(false);
		//cpontrollo gli importi
		checkCoerenzaTotaliQuoteInserimento();
	}
	
	/**
	 * Valida l'aggiornamento di una nuova quota.
	 */
	private void validazioneAggiornamentoNuovaQuota() {
		validazioneNuovaQuota(true);
		//cpontrollo gli importi
		checkCoerenzaTotaliQuoteAggiornamento();
	}

	/**
	 * Valida la quota.
	 *
	 * @param isAggiornamento se la validazione sia in aggiornamento
	 */
	private void validazioneNuovaQuota(boolean isAggiornamento) {
		SubdocumentoSpesa subdocumento = model.getSubdocumento();
		DocumentoSpesa documento = model.getDocumento();
		
		// SIAC-4391
		subdocumento.setFlagConvalidaManuale(FormatUtils.parseBoolean(model.getFlagConvalidaManualeQuota(), "M", "A"));
		
		//la data scadenza e' obbligatoria
		checkNotNull(subdocumento.getDataScadenza(), "Data scadenza");
		
		//se ho un provvedimento, e' necessario che esista
		AttoAmministrativo atto = model.getAttoAmministrativo();
		boolean provvedimentoEsistente = checkProvvedimentoEsistente();
		
		checkCondition(provvedimentoEsistente
				|| atto == null 
				|| (atto.getNumero() == 0 && atto.getAnno() == 0 && (model.getTipoAtto() == null || model.getTipoAtto().getUid() == 0)),
				  ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quando valorizzato anno atto bisogna valorizzare o il tipo o il numero atto"));
		
		//la descrizione e' obbligatoria
		checkNotNullNorEmpty(subdocumento.getDescrizione(), "Descrizione");
		// cfr. JIRA SIAC-497, SIAC-1174
		// checkNotNullNorInvalidUid(model.getSedeSecondariaSoggetto(), "Sede secondaria soggetto")
		checkNotNullNorInvalidUid(model.getModalitaPagamentoSoggetto(), "Modalita pagamento soggetto");
		
		// SIAC-6840
		// controllo che il metodo di pagamento sia AVVISO PAGOPA
		// se corrisponde ed il campo "Codice avviso PagoPA" non e' valorizzato, lancio un warning
		// se l'utente indica di proseguire con l'elaborazione, salto il controllo
		//SIAC-7241: se non c'e' la modalita oagamento del soggetto, non devo andare in null pointer!!!
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione()) && model.getModalitaPagamentoSoggetto() != null) {
			boolean condition = !(checkModalitaPagamentoIsPagoPA() && StringUtils.isBlank(model.getDocumento().getCodAvvisoPagoPA()));
			warnCondition(condition, ErroreFin.COD_AVVISO_PAGO_PA_ASSENTE.getErrore("Proseguire con l'operazione?"));
		}
		
		// L'importo deve essere > 0 se abilitato
		// SIAC-5044
		checkNotNull(subdocumento.getImporto(), "Importo");		
		if (subdocumento.getImporto() == null) {
			//il valore di default per l'importo e' zero
			subdocumento.setImporto(BigDecimal.ZERO);			
		}
		
		checkCondition(Boolean.TRUE.equals(model.getImportoQuotaDisabilitato()) || BigDecimal.ZERO.compareTo(subdocumento.getImporto()) <= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore o uguale a zero"));

		// La data di scadenza non deve essere precedente la data di emissione del documento
		//checkCondition(subdocumento.getDataScadenza() == null || documento.getDataEmissione() == null || subdocumento.getDataScadenza().after(documento.getDataEmissione()), 
		//		ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
		
		checkCondition(	documento.getDataEmissione() == null ||
						subdocumento.getDataScadenza() == null  || 
						subdocumento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());

		
		// Impegno
		validaImpegnoPerInserimentoNuovaQuota();
		
		//Provvisorio. Per ora carico dati senza effettuare controlli
		caricaProvvisorioPerInserimentoNuovaQuota();
		
		// Provvedimento
		log.debug("validazioneNuovaQuota", "provvedimento disabilitato: " + model.getProvvedimentoQuotaDisabilitato());
		log.debug("validazioneNuovaQuota", "condizione: " + !Boolean.TRUE.equals(model.getProvvedimentoQuotaDisabilitato()));
		
		if(provvedimentoEsistente) {
			//il provvedimento esiste, lo controllo
			validaProvvedimentoPerInserimentoNuovaQuota(BilConstants.GESTISCI_DOCUMENTO_SPESA_DECENTRATO.getConstant());
		}
		
		//controlli sullo splot reverse
		boolean coerenzaSplitReverse = !(subdocumento.getTipoIvaSplitReverse() != null ^ subdocumento.getImportoSplitReverse() != null);
		checkCondition(subdocumento.getImportoSplitReverse() == null || subdocumento.getImportoSplitReverse().signum() > 0 , ErroreCore.FORMATO_NON_VALIDO.getErrore("importo split/reverse", "deve essere maggiore di zero"));
		checkCondition(coerenzaSplitReverse, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("il tipo iva Split/Reverse e il relativo importo non possono essere valorizzati singolarmente"));
		
		//verifico coerenza tra ritenute equote per split/reverse
		if (coerenzaSplitReverse && 	subdocumento.getImportoSplitReverse() != null){
			checkCoerenzaImportiSplitReverse(false, isAggiornamento);
			// verifico anche che l'importo quota sia >= all'importo split reverse della quota stessa
			checkCondition(subdocumento.getImportoNotNull().subtract(subdocumento.getImportoSplitReverse()).signum() >= 0, ErroreFin.IMPORTO_QUOTA__SPLIT_REVERSE_INCONGRUENTE.getErrore());
		}
		// Altri dati
		checkNotNull(subdocumento.getCommissioniDocumento(), "Commissioni");
		checkNotNullNorEmpty(subdocumento.getCausaleOrdinativo(), "Causale ordinativo");
		
		// Dati certificazione credito
		DatiCertificazioneCrediti datiCertificazioneCrediti = subdocumento.getDatiCertificazioneCrediti();
		// Numero certificazione e data certificazione devono essere o entrambi presenti o entrambi assenti [equivalente a !(A ^ B)]
		checkCondition(!(StringUtils.isBlank(datiCertificazioneCrediti.getNumeroCertificazione()) ^ datiCertificazioneCrediti.getDataCertificazione() == null),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero e Data certificazione credito", ": non possono essere valorizzati singolarmente"));
		
		
		if(isAggiornamento && Boolean.TRUE.equals(documento.getContabilizzaGenPcc()) && isUnicaQuotaConMovimento(documento.getListaSubdocumenti(), model.getSubdocumento())){
			checkCondition(model.getMovimentoGestione()!=null 
					&& model.getMovimentoGestione().getAnnoMovimento()!=0 
					&& model.getMovimentoGestione().getNumero()!=null, 
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Le registrazioni GEN e PCC sul documento sono state attivate. "
							+ "E' obbligatoria almeno una quota con l'impegno."));
		}
		
		// SIAC-5311 SIOPE+
		// Scadenza SIOPE: Si', se la data di scadenza originaria viene modificata o viene modificata la data scadenza dopo sospensione
		checkCondition((!modificataData(subdocumento.getDataScadenza(), model.getDataScadenzaOriginale(), true)
				&& !modificataData(subdocumento.getDataScadenzaDopoSospensione(), model.getDataScadenzaDopoSospensioneOriginale(), false))
				|| (subdocumento.getSiopeScadenzaMotivo() != null && subdocumento.getSiopeScadenzaMotivo().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Motivo scadenza siope"));
		
	}

	
	//SIAC-680
	private boolean checkModalitaPagamentoIsPagoPA() {
		boolean res = false;
		List<ModalitaPagamentoSoggetto> listModPag = model.getSoggetto().getModalitaPagamentoList();
		for (ModalitaPagamentoSoggetto modPag : listModPag) {
			if(modPag.getUid() == model.getModalitaPagamentoSoggetto().getUid() && "APA".equals(modPag.getModalitaAccreditoSoggetto().getCodice())) {
				res = true;											
			}
		}
		return res;
	}
	
	/**
	 * Controlla se la data segnalata sia stata modificata rispetto a quella fornita
	 * @param dataDaControllare la data da controllare
	 * @param dataOriginale la data oridinale
	 * @return <code>true</code> se la data &eacute; stata modificata
	 */
	private boolean modificataData(Date dataDaControllare, Date dataOriginale, boolean allowInitialNull) {
		if(allowInitialNull && dataOriginale == null) {
			// La data e' modificata ma invio false
			return false;
		}
		return (dataOriginale == null && dataDaControllare != null)
				|| (dataOriginale != null && !dataOriginale.equals(dataDaControllare));
	}

	private boolean isUnicaQuotaConMovimento(List<SubdocumentoSpesa> listaSubdocumenti, SubdocumentoSpesa subdocInAggiornamento) {
		int count = 0;
		SubdocumentoSpesa subUnicoConMovimento = null;
		for (SubdocumentoSpesa ss : listaSubdocumenti) {
			if(ss.getImpegno() != null && ss.getImpegno().getAnnoMovimento() != 0 && ss.getImpegno().getNumero() != null){
				count++;
				subUnicoConMovimento = ss;
				if(count > 1){
					//esistono almeno due quote con impegno
					return false;
				}
			}
			
		}
		
		//vi e' una sola quota con impegno e questa e' quella che sto aggiornando: returning true. Altrimenti false
		return count <= 1 && subUnicoConMovimento!=null && subUnicoConMovimento.getUid() == subdocInAggiornamento.getUid();
	}

	/**
	 * Carica il provvisorio di cassa per la quota.
	 */
	private void caricaProvvisorioPerInserimentoNuovaQuota() {
		
		if(model.getSubdocumento().getProvvisorioCassa() == null ||
				(model.getSubdocumento().getProvvisorioCassa().getNumero() == null && model.getSubdocumento().getProvvisorioCassa().getAnno() == null)){
			//non ho un provvisorio, non lo valido
			return;
		}
		
		if(model.getSubdocumento().getProvvisorioCassa().getNumero() == null || model.getSubdocumento().getProvvisorioCassa().getAnno() == null){
			// ho solo uno dei due campi del provvisorio valorizzati
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("numero e anno provvisorio di cassa", ": non possono essere valorizzati singolarmente"));
			return;
		}	
		//SIAC-6048
		if(model.getSubdocumento().getTipoIvaSplitReverse() != null && model.getSubdocumento().getImportoSplitReverse() != null) {
			addErrore(ErroreFin.QUOTA_A_COPERTURA_CON_SPLIT.getErrore());
			return;
		}
		
		//controllo l'esistenza del provvisorio di cassa
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		checkCondition(provvisorioDiCassa != null , ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", model.getSubdocumento().getProvvisorioCassa().getAnno()
				 +  "/" + model.getSubdocumento().getProvvisorioCassa().getNumero()));
		//imposto nel subdocumento i dati del provvisorio di cassa ottenuti dal servizio
		model.getSubdocumento().setProvvisorioCassa(provvisorioDiCassa);
				
	}

	/**
	 * Ricerca il provvisorio di cassa.
	 * 
	 * @return il provvisorio trovato
	 */
	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
		//chiamo il servizio di ricerca del provvisorio
		RicercaProvvisorioDiCassaPerChiave reqRPK = model.creaRequestProvvisorioCassa();
		RicercaProvvisorioDiCassaPerChiaveResponse resRPK = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRPK);
		
		return resRPK.getProvvisorioDiCassa();
	}

	/**
	 * Riottiene le quote associate al Documento a partire dal servizio di ricerca delle quote.
	 * <br>
	 * Sebbene questo controllo possa sembrare inutile, esso permette di non perdere quote scritte da un utente differente in contemporanea alla scrittura
	 * delle quote scritte dall'utente attuale.
	 */
	private void ricalcolaLeQuoteAssociate() {
		//chiamo il servizio per ottenere le quote spesa così come ottenute dal servizio
		RicercaQuoteByDocumentoSpesa req = model.creaRequestRicercaQuoteByDocumentoSpesa();
		logServiceRequest(req);
		RicercaQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaQuoteByDocumentoSpesa(req);
		logServiceResponse(res);
		
		// Pulisco il model
		cleanQuote();
		// Imposto la lista dei subdocumenti
		model.setListaSubdocumentoSpesa(res.getSubdocumentiSpesa());
		model.getDocumento().setListaSubdocumenti(res.getSubdocumentiSpesa());
		
		// Ricalcolo il flag rilevante iva
		model.ricalcolaFlagIva();
	}
	
	/**
	 * Effettua il controllo di coerenza sul totale delle quote.
	 * <br/>
	 * <strong>Lotto L</strong>:
	 * Il totale delle quote deve essere minore o uguale a <code>importo Documento  + arrotondamento + totale imponibile importo degli oneri con TipoIvaSplitReverse=RC se presenti</code>.
	 * In caso negativo il sistema visualizza il messaggio: <code>&lt;FIN_ERR_0086, Importo quote e importo documento incongruenti&gt</code>.
	 */
	private void checkCoerenzaTotaliQuoteInserimento() {
		//TOTALE QUOTE <= IMPORTO + ARROTONDAMENTO + IMPONIBILE ONERI RC
		BigDecimal nuovoTotaleQuote = model.getTotaleQuote().add(model.getSubdocumento().getImporto());
		BigDecimal totaleImportiDocumento = model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()).add(model.getTotaleImportoImponibileOneriReverseChange());
		
		// totale con il nuovo documento - importo documento
		BigDecimal totalePerCoerenza = nuovoTotaleQuote.subtract(totaleImportiDocumento);
		checkCondition(totalePerCoerenza.signum() <= 0, ErroreFin.IMPORTO_QUOTE_E_IMPORTO_DOCUMENTO_INCONGRUENTI.getErrore());
		
		// TOTALE QUOTE >= TOTALE NOTE
		checkCondition(nuovoTotaleQuote.subtract(model.getTotaleImportoDaDedurreSuFattura()).signum()>= 0, 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo quota", ": il totale delle quote deve essere maggiore o uguale al totale delle note credito"));
	}
	
	/**
	 * Effettua il controllo di coerenza sul totale delle quote.
	 * <br/>
	 * <strong>Lotto L</strong>:
	 * Il totale delle quote deve essere minore o uguale a <code>importo Documento  + arrotondamento + totale imponibile importo degli oneri con TipoIvaSplitReverse=RC se presenti</code>.
	 * In caso negativo il sistema visualizza il messaggio: <code>&lt;FIN_ERR_0086, Importo quote e importo documento incongruenti&gt</code>.
	 */
	private void checkCoerenzaTotaliQuoteAggiornamento() {
		SubdocumentoSpesa quotaOld = new SubdocumentoSpesa();
		quotaOld.setUid(model.getUidQuota());
		int index = ComparatorUtils.getIndexByUid(model.getListaSubdocumentoSpesa(), quotaOld);
		quotaOld = model.getListaSubdocumentoSpesa().get(index);
		
		BigDecimal nuovoTotaleQuote = model.getTotaleQuote().add(model.getSubdocumento().getImporto()).subtract(quotaOld.getImporto());
		BigDecimal nuovoTotaleDaPagareQuote = model.getTotaleQuote().add(model.getSubdocumento().getImportoDaPagare()).subtract(quotaOld.getImportoDaPagare());
		
		BigDecimal totaleImportiDocumento = model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()).add(model.getTotaleImportoImponibileOneriReverseChange());
		
//		TOTALE QUOTE <= IMPORTO + ARROTONDAMENTO
		BigDecimal totalePerCoerenzaTotale = nuovoTotaleQuote.subtract(totaleImportiDocumento);
		
//		IMPORTO QUOTA >= IMPORTO DA DEDURRE
		BigDecimal totalePerCoerenzaImporto = model.getSubdocumento().getImporto().subtract(model.getSubdocumento().getImportoDaDedurre());
		
//		TOTALE NOTE CREDITO <= TOTALE DA INCASSARE QUOTE
		BigDecimal totalePerCoerenzaNotaCredito = model.getTotaleImportoDaDedurreSuFattura().subtract(nuovoTotaleDaPagareQuote);
		
		//TOTALE QUOTE >= TOTALE NOTE
		checkCondition(nuovoTotaleQuote.subtract(model.getTotaleImportoDaDedurreSuFattura()).signum()>= 0, 
					ErroreCore.FORMATO_NON_VALIDO.getErrore("importo quota", ": il totale delle quote deve essere maggiore o uguale al totale delle note credito"));
		
		//TOTALE QUOTE <= importo Documento  + arrotondamento + totale importo degli oneri con TipoIvaSplitReverse=RC se presenti.
		BigDecimal totaleSR = model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()).add(model.ricalcolaTotaleImponibileOneriRC());
		
		checkCondition(totalePerCoerenzaTotale.signum() <= 0 && totalePerCoerenzaImporto.signum() >= 0 && totalePerCoerenzaNotaCredito.signum()<= 0 && nuovoTotaleQuote.subtract(totaleSR).signum()<= 0 , ErroreFin.IMPORTO_QUOTE_E_IMPORTO_DOCUMENTO_INCONGRUENTI.getErrore());
		
	}
	
	/**
	 * Validazione dell'Impegno (Movimento di Gestione)
	 */
	private void validaImpegnoPerInserimentoNuovaQuota() {
		final String methodName = "validaImpegnoPerInserimentoNuovaQuota";
		
		
		SubdocumentoSpesa subdocumento = model.getSubdocumento();
		Impegno impegno = model.getMovimentoGestione();
		String cig = subdocumento.getCig();
		String cup = subdocumento.getCup();
		VoceMutuo voceMutuo = model.getVoceMutuo();
		
		// Controllo se l'impoegno sia da cercare
		boolean impegnoDaRicercare = impegno != null && impegno.getAnnoMovimento() > 0 && impegno.getNumero() != null;
		
		// Se l'impegno e' presente, allora anno e numero devono essere entrambi presenti o entrambi assenti
		checkCondition(impegno == null || !(impegno.getAnnoMovimento() == 0 ^ impegno.getNumero() == null), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno impegno e numero impegno", ": devono essere entrambi selezionati o entrambi non selezionati"));
		checkCondition(impegno == null || impegno.getAnnoMovimento() <= model.getAnnoEsercizioInt(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno impegno", ": non deve essere superiore all'anno di esercizio"));
		
		// Check CIG-CUP-NumeroMutuo
		if(!(StringUtils.isBlank(cig) || CIG_PATTERN.matcher(cig).matches())){
			//il cig non rispetta il formato corretto
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("CIG", "Deve essere composto da dieci caratteri"));
			model.setProseguireConElaborazione(Boolean.TRUE);
		}
		if(!(StringUtils.isBlank(cup) || CUP_PATTERN.matcher(cup).matches())){
			//il cup non presenta il formato corretto
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("CUP", "Deve essere composto da 15 caratteri, il primo e il quarto dei quali devono essere una lettera"));
			model.setProseguireConElaborazione(Boolean.TRUE);
		}
		
		if(impegnoDaRicercare) {
			try {
				//devo chiamare il servizio per ottenere  l'impegno
				Impegno i = ricercaImpegnoPerChiaveOttimizzato();
				model.setMovimentoGestione(i);
				//valido l'impegno ottenuto dal servizio
				validazioneImpegnoDaResponse(i, cig, cup, voceMutuo);
			} catch(WebServiceInvocationFailureException wsife) {
				//si sono verificati degli errori. Li loggo ma non lancio un'eccezione, continuando 
				log.info(methodName, wsife.getMessage());
			}
		}
				
	}
	
	/**
	 * Ricerca l'impegno per chiave.
	 * 
	 * @return l'impegno trovato
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private Impegno ricercaImpegnoPerChiaveOttimizzato() throws WebServiceInvocationFailureException {
		Impegno impegno = sessionHandler.getParametro(BilSessionParameter.IMPEGNO);
		
		//controllo l'effettiva necessita' di chiamare il servizio
		if(isNecessarioRicaricareImpegno(impegno)) {
			//in sessione non ho i dati, devo per forza ottenerli da db
			RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(model.getMovimentoGestione());
			logServiceRequest(req);
			
			RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(req, res);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(res.isFallimento()) {
				//il servizio e' fallito: lancio un errore
				String errorMsg = "Risultato ottenuto dal servizio RicercaImpegnoPerChiave: FALLIMENTO";
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(res.getImpegno() == null) {
				//il servizio non ha trovato l'impegno: lancio un errore
				String chiaveImpegno = req.getpRicercaImpegnoK().getAnnoEsercizio() + "/" + req.getpRicercaImpegnoK().getAnnoImpegno() + "/" + req.getpRicercaImpegnoK().getNumeroImpegno();
				String errorMsg = "Impegno non presente per chiave " + chiaveImpegno;
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", chiaveImpegno));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			impegno = res.getImpegno();
			// Default per le liste
			// IL SERVIZIO RESTITUISCE, SE HO SCELTO UN SUBIMPEGNO, UN IMPEGNO CON UN SOLO ELEMENTO 
			//IN impegno.elencoSubImpegni. ALTRIMENTI NON CARICO I SUB
			// GLI IMPEGNI E SUBIMPEGNI TROVATI DEVONO ESSERE IN STATO DEFINITIVO GIA' DAL PRINCIPIO. OPPURE LO CONTROLLO DOPO
			impegno.setElencoSubImpegni(defaultingList(impegno.getElencoSubImpegni()));
			impegno.setListaVociMutuo(defaultingList(impegno.getListaVociMutuo()));
			for(SubImpegno si : impegno.getElencoSubImpegni()) {
				si.setListaVociMutuo(defaultingList(si.getListaVociMutuo()));
			}
			// Inizializzo il capitolo se non gia' presente
			if(impegno.getCapitoloUscitaGestione() == null) {
				// Se il capitolo non e' stato impostato dal servizio, lo imposto io
				impegno.setCapitoloUscitaGestione(res.getCapitoloUscitaGestione());
			}
			sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegno);
		}
		
		return impegno;
	}

	/**
	 * Valuta se e' necessario richiamare il servizio per caricare l'impegno.
	 * @return <code>true</code> se il servizio deve essere richiamato, <code>false</code> altrimenti.
	 * */
	private boolean isNecessarioRicaricareImpegno(Impegno impegnoDaSessione) {
		//se ho il numero del submovimento, devo ricaricarlo sempre!
		return model.getSubMovimentoGestione() != null && model.getSubMovimentoGestione().getNumero() !=null ||
				 !ValidationUtil.isValidMovimentoGestioneFromSession(impegnoDaSessione, model.getMovimentoGestione());
	}
	
	/**
	 * Valida l'impegno o il subimpegno ottenuto dalla response.
	 * 
	 * @param impegno   l'impegno da validare
	 * @param cig       il CIG
	 * @param cup       il CUP
	 * @param voceMutuo la voce di mutuo
	 */
	private void validazioneImpegnoDaResponse(Impegno impegno, String cig, String cup, VoceMutuo voceMutuo) {
		
		SubImpegno subimpegno = model.getSubMovimentoGestione();
		SubdocumentoSpesa subdocumento = model.getSubdocumento();
		
		checkCondition(subimpegno != null || StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa()), 
				ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
		
		Soggetto soggettoCollegatoAllImpegno = impegno.getSoggetto();
		Soggetto soggettoCollegatoAlDocumento = model.getSoggetto();
		
		String cigMovgestCollegato = impegno.getCig();
		String cupDaControllare = impegno.getCup();
		SiopeAssenzaMotivazione samMovgest = impegno.getSiopeAssenzaMotivazione();
		
		ClassificatoreGenerico tipoImpegno = impegno.getTipoImpegno();
		List<VoceMutuo> listaVoceMutuo = impegno.getListaVociMutuo();
		
		SiopeTipoDebito siopeTipoDebito = impegno.getSiopeTipoDebito();
		
		// Check del subimpegno nella lista degli impegni: mantenuto in questo modo per evitare di cambiare troppo codice
		if(subimpegno != null && subimpegno.getNumero() != null && impegno.getElencoSubImpegni() != null) {
			SubImpegno sub = null;
			for(SubImpegno s : impegno.getElencoSubImpegni()) {
				if(s.getNumero().compareTo(subimpegno.getNumero()) == 0) {
					sub = s;
					soggettoCollegatoAllImpegno = s.getSoggetto();
					//ho selezionato il soggetto che mi interessa, esco dal ciclo
					break;
				}
			}
			
			checkCondition(sub != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("subimpegno", subimpegno.getNumero()+""));
			if(sub != null){
				//imposto nel model il submovimento di gestione
				model.setSubMovimentoGestione(sub);
				checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(sub.getStatoOperativoMovimentoGestioneSpesa()),
						ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
				
				cigMovgestCollegato = sub.getCig();
				cupDaControllare = sub.getCup();
				
				listaVoceMutuo = sub.getListaVociMutuo();
				tipoImpegno = sub.getTipoImpegno();
				siopeTipoDebito = sub.getSiopeTipoDebito();
				samMovgest = sub.getSiopeAssenzaMotivazione();
			}
			
		}
		
		checkCondition(soggettoCollegatoAllImpegno == null || soggettoCollegatoAllImpegno.getUid() == 0 ||
			(soggettoCollegatoAllImpegno.getUid() == soggettoCollegatoAlDocumento.getUid()),
			ErroreFin.SOGGETTO_DIVERSO_DA_QUELLO_DEL_DOCUMENTO.getErrore("impegno"));
		
		// Check sulla classe del soggetto
		//Vedi jira 1245 e jira 1816
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione())) {
			checkClasseSoggettoImpegno(impegno, soggettoCollegatoAlDocumento);
		}
		
		// Check sulla classe del soggetto
		//Vedi jira SIAC-6688
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione())) {
			checkImpegnoConImportoVincolato(impegno);
		}
		
		
		
		// SIAC-6164
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione())) {
			checkCondition(model.getDocumento() == null || model.getDocumento().getTipoDocumento() == null || !"FAT".equals(model.getDocumento().getTipoDocumento().getCodice())
					|| model.getMovimentoGestione().getSiopeTipoDebito() == null || !"NC".equals(model.getMovimentoGestione().getSiopeTipoDebito().getCodice()),
					ErroreFin.IMPEGNO_QUOTA_NC_NON_CONGRUENTE.getErrore());
		}
		
		// Controllo CIG-CUP-NUMERO MUTUO
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione())) {
			warnCondition((StringUtils.isBlank(cig) || cig.equalsIgnoreCase(cigMovgestCollegato)) &&
					(StringUtils.isBlank(cup) || cup.equalsIgnoreCase(cupDaControllare)),
					ErroreFin.CIG_CUP_DIFFERENTI_DA_QUELLI_DEL_MOVIMENTO_SELEZIONATO.getErrore());
		}
		// Ripristino il flag al valore di default
		model.setProseguireConElaborazione(Boolean.FALSE);
		
		
		//CONTROLLI SUL MUTUO
		boolean impegnoNonFinanziatoDaMutuoEMutuoPresente = tipoImpegno != null
				&& !BilConstants.IMPEGNO_FINANZIATO_DA_MUTUO.getConstant().equals(tipoImpegno.getCodice())
				&& voceMutuo != null
				&& StringUtils.isNotBlank(voceMutuo.getNumeroMutuo());
		
		checkCondition(!impegnoNonFinanziatoDaMutuoEMutuoPresente, ErroreFin.IMPEGNO_NON_FINANZIATO_CON_MUTUO.getErrore());
		
		boolean impegnoFinanziatoDaMutuoEMutuoNonPresente = tipoImpegno != null
				&& BilConstants.IMPEGNO_FINANZIATO_DA_MUTUO.getConstant().equals(tipoImpegno.getCodice())
				&& (voceMutuo == null || StringUtils.isBlank(voceMutuo.getNumeroMutuo()));
		checkCondition(!impegnoFinanziatoDaMutuoEMutuoNonPresente, ErroreFin.IMPEGNO_FINANZIATO_DA_MUTUO.getErrore());
		
		if(voceMutuo != null && StringUtils.isNotBlank(voceMutuo.getNumeroMutuo())){
			// Controllo che la voce di mutuo sia tra quelle fornite in input
			if(listaVoceMutuo == null){
				 addErrore(ErroreFin.ENTITA_NON_VALIDA.getErrore("Numero mutuo indicato"));
			} else {
				VoceMutuo voceMutuoImpegno = ComparatorUtils.findVoceMutuoByNumero(listaVoceMutuo, voceMutuo);
				checkCondition(voceMutuoImpegno != null, ErroreFin.ENTITA_NON_VALIDA.getErrore("Numero mutuo indicato"));
				if(voceMutuoImpegno != null) {
					// Imposto la voce di mutuo
					model.setVoceMutuo(voceMutuoImpegno);
				}
			}
		}
		
		//CONTROLLI SUL SIOPE E SUL CIG
		SiopeAssenzaMotivazione samSubdoc = subdocumento.getSiopeAssenzaMotivazione();
	
		if(StringUtils.isBlank(cig) && (samSubdoc == null || samSubdoc.getUid() == 0)) {
			//questo accade quando l'utente mette anno e numero impegno e schiaccia salva senza attendere che vengano preimpostati i campi dell'impegno.
			//il salva triggera il change che carica l'impegno. da qui la SIAC-5454. Per evitare questo, se non viene messo nulla, io forzo il cig (o motivo assenza cig) a quelli dell'impegno.
			//TODO: soluzione non ideale, valutare se questa sia effettivamente l'unica possibile.
			subdocumento.setCig(cigMovgestCollegato);
			subdocumento.setSiopeAssenzaMotivazione(samMovgest);
		}
		
		model.setSubdocumento(subdocumento);
		
		
		// SIAC-5311 SIOPE+
		checkSiope(subdocumento.getCig(), subdocumento.getSiopeAssenzaMotivazione(), siopeTipoDebito, impegno);
		
	}
	
	/**
	 * Controllo dei dati del SIOPE+.
	 * <ul> 
	 * 	<li> Se tipo debito SIOPE = commerciale cig o, in alternativa motivazione assenza cig sono obbligatori.</li>
	 * 	<li> Cig e motivazione assenza cig non possono coesistere</li>
	 * 	<li> Se tipo debito siope == null non vengono effettuati controlli</li> 
	 * </ul>
	 * 
	 * @param cig il cig
	 * @param impegno l'impegno
	 */
	private void checkSiope(String cig, SiopeAssenzaMotivazione siopeAssenzaMotivazione, SiopeTipoDebito siopeTipoDebito, Impegno impegno) {
		final String methodName = "checkSiope"; 
		
		boolean cigValorizzato = StringUtils.isNotBlank(cig);
		boolean motivoAssenzaValorizzato = siopeAssenzaMotivazione != null && siopeAssenzaMotivazione.getUid() != 0;
		
		if(siopeTipoDebito == null || siopeTipoDebito.getUid() == 0) {
			
			//non ho un tipo debito siope. per gestire il pregresso, esco senza lanciare un errore
			log.debug(methodName, "tipo debito siope non presente.");
			return;
		}
		
		//cig e cup non possono essere entrambi valorizzati
		checkCondition(!(cigValorizzato && motivoAssenzaValorizzato), ErroreCore.FORMATO_NON_VALIDO.getErrore("cig o motivo assenza cig", "non possono essere entrambi valorizzati."));
		
		// SIAC-5454: caso in cui i dati sono stati sbiancati dal caricamento js
		if(BilConstants.CODICE_SIOPE_DEBITO_TIPO_COMMERCIALE.getConstant().equals(siopeTipoDebito.getCodice()) && !cigValorizzato && !motivoAssenzaValorizzato){
			motivoAssenzaValorizzato = impegno.getSiopeAssenzaMotivazione()!= null;
			cigValorizzato           = StringUtils.isNotBlank(impegno.getCig());
			
			model.getSubdocumento().setCig(impegno.getCig());
			model.getSubdocumento().setSiopeAssenzaMotivazione(impegno.getSiopeAssenzaMotivazione());
		}
		
		//se tipo debito siope commerciale, almeno uno dei due e' obbligatorio
		checkCondition(!BilConstants.CODICE_SIOPE_DEBITO_TIPO_COMMERCIALE.getConstant().equals(siopeTipoDebito.getCodice())
				|| cigValorizzato
				|| motivoAssenzaValorizzato,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("cig o motivo di assenza CIG"));
		checkSiopeAssenzaMotivazione();
	}

	/**
	 * 
	 */
	private void checkSiopeAssenzaMotivazione() {
		final String methodName = "checkMotivoAssenzaSiope";
		SiopeAssenzaMotivazione assenzaMotivazioneWithCodice = ComparatorUtils.searchByUidEventuallyNull(model.getListaSiopeAssenzaMotivazione(), model.getSubdocumento().getSiopeAssenzaMotivazione());
		if(assenzaMotivazioneWithCodice == null || assenzaMotivazioneWithCodice.getUid() == 0) {
			//se non ho specificato un motivo di assenza cig, non posso e non devo controllarlo
			log.debug(methodName, "motivo assenza cig non presente: non devo effettuare controlli.");
			return;
		}
		//SIAC-5526	
		String siopeAssenzaDaDefinire = BilConstants.CODICE_SIOPE_ASSENZA_MOTIVAZIONE_DA_DEFINIRE_IN_LIQUIDAZIONE.getConstant();
		// a livello di subdocumento non posso avere una motivazione assenza cig da definire, deve essere specificato
		checkCondition(!siopeAssenzaDaDefinire.equals(assenzaMotivazioneWithCodice.getCodice()), 
				ErroreCore.VALORE_NON_VALIDO.getErrore("motivo assenza cig :"  + siopeAssenzaDaDefinire,"selezionare una motivazione valida"));		
		String siopeAssenzaInCorsoDiDefinizione = BilConstants.CODICE_SIOPE_ASSENZA_MOTIVAZIONE_IN_CORSO_DEFINIZIONE.getConstant();
		checkCondition(!siopeAssenzaInCorsoDiDefinizione.equals(assenzaMotivazioneWithCodice.getCodice()), 
				ErroreCore.VALORE_NON_VALIDO.getErrore("motivo assenza cig " + siopeAssenzaInCorsoDiDefinizione,"selezionare una motivazione valida"));
	}
	
	@Override
	protected void cleanQuote() {
		super.cleanQuote();
		
		//pulisco il model
		model.setSubdocumento(new SubdocumentoSpesa());
		model.setSedeSecondariaSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.setVoceMutuo(null);
		model.setAttoAmministrativo(null);
		model.setTipoAtto(null);
		model.setContoTesoreria(null);
	}
	
	@Override
	protected void cleanQuotePerAggiornamento() {
		super.cleanQuotePerAggiornamento();		
		//pulisco il model
		model.setSedeSecondariaSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setMovimentoGestione(null);
	}
	
	/**
	 * Controlla se il provvedimento della quota debba essere disabilitato.
	 * <br/>
	 * <strong>CR-2463</strong>: Il campo provvedimento della quota documento, non deve essere protetto per i documenti con contabilizzaGEN/PCC false se il tipo documento
	 * ha entrambi i flag flagAttivaGEN e flagComunicaPCC FALSE (o se non sono presenti tra gli attributi del tipo)
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; da disabilitare; <code>false</code> altrimenti
	 */
	private boolean controllaQuotaConProvvedimentoDaDisabilitareCausaGenPcc() {
		return !Boolean.TRUE.equals(model.getDocumento().getContabilizzaGenPcc())
				&& model.getDocumento().getTipoDocumento() != null
				&& (Boolean.TRUE.equals(model.getDocumento().getTipoDocumento().getFlagAttivaGEN()) || Boolean.TRUE.equals(model.getDocumento().getTipoDocumento().getFlagComunicaPCC()));
	}

	
/*
	private boolean controllaQuotaConDataSospensioneDaAbilitareCausaGenPcc() {
		boolean ris;
		boolean contabilizzaGenPcc = model.getDocumento().getContabilizzaGenPcc();// se è true devo editare le date 
		//boolean flagPcc = model.getDocumento().getTipoDocumento().getFlagComunicaPCC();
		if (flagPcc && contabilizzaGenPcc){
			ris = true;
		}else{
			ris = false;
		}
		
		return ris;
	}
*/
	
	/**
	 * Imposta i dati della quota per l'aggiornamento.
	 */
	private void impostaQuotaDaInserire() {
		if (controllaQuotaConProvvedimentoDaDisabilitareCausaGenPcc()) {
			//devo disabilitare il provvedimento
			model.setProvvedimentoQuotaDisabilitato(Boolean.TRUE);
		}
	}
	/**
	 * Imposta i dati della quota per il ripeti.
	 * 
	 * @param subdocumentoSpesa il subdocumento tramite cui popolare i dati
	 */
	private void impostaQuotaDaRipetere (SubdocumentoSpesa subdocumentoSpesa) {
		model.setProgressivoNumeroSubdocumento(subdocumentoSpesa.getNumero());
		
		// Popolo i dati presenti nei classificatori esterni
		model.setSubdocumento(subdocumentoSpesa);
		//model.setMovimentoGestione(subdocumentoSpesa.getImpegno());
//		model.setSubMovimentoGestione(subdocumentoSpesa.getSubImpegno());
		//azzero importo
		model.getSubdocumento().setImporto(BigDecimal.ZERO);
		model.getSubdocumento().setLiquidazione(null);
		
		//SIAC-5459
		model.getSubdocumento().setImportoDaDedurre(BigDecimal.ZERO);
	
		//SIAC-5406
		model.getSubdocumento().setCig("");
		model.getSubdocumento().setSiopeAssenzaMotivazione(null);
		model.getSubdocumento().setCup("");
		
		model.setVoceMutuo(subdocumentoSpesa.getVoceMutuo());
		
		AttoAmministrativo attoAmministrativo = subdocumentoSpesa.getAttoAmministrativo();
		model.setAttoAmministrativo(attoAmministrativo);
		if(attoAmministrativo != null) {
			//imposto i dati del provvedimento
			model.setTipoAtto(attoAmministrativo.getTipoAtto());
			model.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		}
		
		model.setSedeSecondariaSoggetto(subdocumentoSpesa.getSedeSecondariaSoggetto());
		model.setModalitaPagamentoSoggetto(subdocumentoSpesa.getModalitaPagamentoSoggetto());
		
		model.setTipoAvviso(subdocumentoSpesa.getTipoAvviso());
		model.setNoteTesoriere(subdocumentoSpesa.getNoteTesoriere());
		model.setContoTesoreria(subdocumentoSpesa.getContoTesoreria());
		
		model.setCapitoloRilevanteIvaVisibile(subdocumentoSpesa.getFlagRilevanteIVA());
//		CR 3022
//		if(subdocumentoSpesa.getNumeroRegistrazioneIVA()!= null && StringUtils.isNotBlank(subdocumentoSpesa.getNumeroRegistrazioneIVA())){
//			model.setImportoQuotaDisabilitato(Boolean.TRUE);
//		}

		if (controllaQuotaConProvvedimentoDaDisabilitareCausaGenPcc()) {
			//i provvedimento deve essere disabilitato
			model.setProvvedimentoQuotaDisabilitato(Boolean.TRUE);
		}
	
	}
	
	/**
	 * Imposta i dati della quota per l'aggiornamento.
	 * 
	 * @param subdocumentoSpesa il subdocumento tramite cui popolare i dati
	 */
	private void impostaQuotaDaAggiornare(SubdocumentoSpesa subdocumentoSpesa) {
		model.setProgressivoNumeroSubdocumento(subdocumentoSpesa.getNumero());
		
		// Popolo i dati presenti nei classificatori esterni
		model.setSubdocumento(subdocumentoSpesa);
		model.setMovimentoGestione(subdocumentoSpesa.getImpegno());
		model.setSubMovimentoGestione(subdocumentoSpesa.getSubImpegno());
		model.setVoceMutuo(subdocumentoSpesa.getVoceMutuo());
		
		if(subdocumentoSpesa.getLiquidazione() != null && subdocumentoSpesa.getLiquidazione().getUid() != 0){
			//ho una liquidazione, non posso 
			model.setImportoQuotaDisabilitato(Boolean.TRUE);
			model.setImpegnoQuotaDisabilitato(Boolean.TRUE);
			model.setProvvedimentoQuotaDisabilitato(Boolean.TRUE);
		}
		
		AttoAmministrativo attoAmministrativo = subdocumentoSpesa.getAttoAmministrativo();
		model.setAttoAmministrativo(attoAmministrativo);
		if(attoAmministrativo != null) {
			//imposto i dati del provvedimento
			model.setTipoAtto(attoAmministrativo.getTipoAtto());
			model.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		}else{
			//pulisco i dati del provvedimento
			model.setTipoAtto(null);
			model.setStrutturaAmministrativoContabile(null);
		}
		
		model.setSedeSecondariaSoggetto(subdocumentoSpesa.getSedeSecondariaSoggetto());
		model.setModalitaPagamentoSoggetto(subdocumentoSpesa.getModalitaPagamentoSoggetto());
		
		model.setTipoAvviso(subdocumentoSpesa.getTipoAvviso());
		model.setNoteTesoriere(subdocumentoSpesa.getNoteTesoriere());
		model.setContoTesoreria(subdocumentoSpesa.getContoTesoreria());
		
		model.setCapitoloRilevanteIvaVisibile(subdocumentoSpesa.getFlagRilevanteIVA());
//		CR 3022
//		if(subdocumentoSpesa.getNumeroRegistrazioneIVA()!= null && StringUtils.isNotBlank(subdocumentoSpesa.getNumeroRegistrazioneIVA())){
//			model.setImportoQuotaDisabilitato(Boolean.TRUE);
//		}

		if (controllaQuotaConProvvedimentoDaDisabilitareCausaGenPcc()) {
			//il provvedimento deve essere disabilitato perche' non ho ancora attivato le registrazioni contabili
			model.setProvvedimentoQuotaDisabilitato(Boolean.TRUE);
		}
		
		if(attoAmministrativo != null && attoAmministrativo.getAllegatoAtto() != null && attoAmministrativo.getAllegatoAtto().getUid() != 0){
			//ho un allegato atto, disabilito il provvedimento
			model.setProvvedimentoQuotaDisabilitato(Boolean.TRUE);
		}
		if(model.getSubdocumento().getCommissioniDocumento() == null){
			//commmisione documento di default e' esente
			model.getSubdocumento().setCommissioniDocumento(CommissioniDocumento.ESENTE);
		}
		// SIAC-5115
		model.setListaSospensioneSubdocumento(subdocumentoSpesa.getSospensioni());
		
		//SIAC-5469
		SiopeTipoDebito siopeTipoDebitoSub = subdocumentoSpesa.getImpegnoOSubImpegno() != null? subdocumentoSpesa.getImpegnoOSubImpegno().getSiopeTipoDebito() : null; 
		model.setShowSiopeAssenzaMotivazione(siopeTipoDebitoSub != null && siopeTipoDebitoSub.getUid()!=0 && BilConstants.CODICE_SIOPE_DEBITO_TIPO_COMMERCIALE.getConstant().equals(siopeTipoDebitoSub.getCodice()));
		
	}
	
	/**
	 * Imposta il filtro sulle modalita di pagamento del soggetto.
	 */
	private void impostaListaModalitaPagamentoFiltrate() {
		List<ModalitaPagamentoSoggetto> filtered = new ArrayList<ModalitaPagamentoSoggetto>();
		//dati necessari a filtrare le modalita di pagamento
		boolean isSedeSecondariaSelected = model.getSedeSecondariaSoggetto() != null && model.getSedeSecondariaSoggetto().getUid() != 0;
		SedeSecondariaSoggetto sss = ComparatorUtils.searchByUid(model.getListaSedeSecondariaSoggettoValide(), model.getSedeSecondariaSoggetto());
		String denominazioneSedeSecondariaSoggetto = isSedeSecondariaSelected ? StringUtils.trimToEmpty(sss.getDenominazione()) : "";
		
		filterModalitaPagamento(filtered, isSedeSecondariaSelected, denominazioneSedeSecondariaSoggetto);
		
		model.setListaModalitaPagamentoSoggettoFiltrate(filtered);
	}

	/**
	 * Filtra le modalita di pagamento valide e relative alla sede secondaria.
	 * 
	 * @param filtered                            le modalit&agrave; filtrate
	 * @param isSedeSecondariaSelected            se la sede secondaria sia selezionata
	 * @param denominazioneSedeSecondariaSoggetto la denominazione della sede secondaria
	 */
	private void filterModalitaPagamento(List<ModalitaPagamentoSoggetto> filtered, boolean isSedeSecondariaSelected, String denominazioneSedeSecondariaSoggetto) {
		//ciclo sulle modalita' di pagamento
		for(ModalitaPagamentoSoggetto mps :  model.getListaModalitaPagamentoSoggetto()) {
			
			if(isStatoOperativoModalitaPagamentoSoggettoValido(mps) &&
				(!isSedeSecondariaSelected || denominazioneSedeSecondariaSoggetto.equalsIgnoreCase(mps.getAssociatoA()))) {
				filtered.add(mps);
			}
		}
	}
	
	/**
	 * Controlla che lo stato operativo della modalita di pagamento sia valido.
	 * 
	 * @param mps la modalita di pagamento
	 * @return <code>true</code> se lo stato &eacute; <code>VALIDO</code>; <code>false</code> altrimenti
	 */
	private boolean isStatoOperativoModalitaPagamentoSoggettoValido(ModalitaPagamentoSoggetto mps) {
		return StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getCodiceStatoModalitaPagamento())
			|| (mps.getModalitaPagamentoSoggettoCessione2() != null
				&& StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getModalitaPagamentoSoggettoCessione2().getCodiceStatoModalitaPagamento()));
	}

	/**
	 * Imposta il filtro sulle modalita di pagamento del soggetto.
	 */
	private void impostaModalitaPagamentoNonFiltrate() {
		List<ModalitaPagamentoSoggetto> filtered = new ArrayList<ModalitaPagamentoSoggetto>();
		filterModalitaPagamento(filtered, false, "");
		model.setListaModalitaPagamentoSoggettoFiltrate(filtered);
	}
	
	//Vedi jira SIAC-6688
	private void checkImpegnoConImportoVincolato(Impegno impegno) {
		final String methodName = "checkImpegnoConImportoVincolato";
		 List<VincoloImpegno> listaVincoli = impegno.getVincoliImpegno();
		 BigDecimal importoSubdocumento = model.getSubdocumento().getImporto();
		 BigDecimal totOrdinativiIncasso = BigDecimal.ZERO;
		 //se non ho un provvedimentoo, oppure ho gia' la liquidazione, salto
		 if(listaVincoli == null || listaVincoli.isEmpty() || idEntitaPresente(model.getSubdocumento().getLiquidazione()) || !checkProvvedimentoEsistente()){
			 return;
		 }
		 
		 
		// SIAC-7020 Null pointer exception se non trova accertamento
		for(VincoloImpegno vincolo : listaVincoli) { 
			 if(vincolo.getAccertamento() != null) {
				 BigDecimal impAttuale = vincolo.getAccertamento().getImportoAttuale(); 
			  	 BigDecimal dispIncassare = vincolo.getAccertamento().getDisponibilitaIncassare(); 
			  	 BigDecimal a = impAttuale.subtract(dispIncassare); 
			  	 totOrdinativiIncasso = totOrdinativiIncasso.add(a); 
			  
			  	 if(importoSubdocumento.compareTo(totOrdinativiIncasso)> 0){
			  		 log.info(methodName, "ERRORE "+ ErroreFin.WARNING_IMPORTO_VINCOLATO.getErrore().getDescrizione());
			  		 addMessaggio(ErroreFin.WARNING_IMPORTO_VINCOLATO.getErrore());
			  		 model.setProseguireConElaborazione(Boolean.TRUE); 
			  	 }
			  }
		 }
		 
	}

	
	/**
	 * Effettua il controllo di uguaglianza tra le classi di soggetto del movimento di gestione e del soggetto.
	 * 
	 * @param impegno l'impegno da cui ottenere la classe
	 * @param soggetto          il soggetto di cui controllare le classi
	 */
	private void checkClasseSoggettoImpegno(Impegno impegno, Soggetto soggetto) {
		final String methodName = "checkClasseSoggettoImpegno";
		ClasseSoggetto classeSoggettoMovimentoGestione = impegno.getClasseSoggetto();
		if(classeSoggettoMovimentoGestione == null) {
			//l'impegno non ha una classe, esco
			log.debug(methodName, "Classe soggetto non presente per il movimento di gestione");
			return;
		}
		List<ClassificazioneSoggetto> listaClasseSoggettoSoggetto = soggetto.getElencoClass();
		if(listaClasseSoggettoSoggetto == null) {
			//il soggetto non ha classi
			log.debug(methodName, "Classi soggetto non presenti sul soggetto");
			addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore("impegno"));
			return;
		}
		for(ClassificazioneSoggetto cs : listaClasseSoggettoSoggetto) {
			if(cs.getSoggettoClasseCode().equalsIgnoreCase(classeSoggettoMovimentoGestione.getCodice())) {
				//il soggetto appertiene alla classe dell'impegno, esco
				log.debug(methodName, "ho trovato un codice classe corrispondente: " + cs.getSoggettoClasseCode());
				return;
			}
		}
		//Il creditore e' stato scelto non appartenente alla classificazione dell'impegno
		addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore("impegno"));
		model.setProseguireConElaborazione(Boolean.TRUE);
	}
	
	/**
	 * Preparazione per il metodo {@link #contabilizzaQuota()}.
	 */
	public void prepareContabilizzaQuota() {
		//pulisco i dati nel model
		model.setSubdocumento(null);
	}
	
	/**
	 * Contabilizzazione della quota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String contabilizzaQuota() {
		final String methodName = "contabilizzaQuota";
		log.debug(methodName, "Contabilizzazione della quota " + model.getSubdocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #contabilizzaQuota()}.
	 */
	public void validateContabilizzaQuota() {
		checkNotNullNorInvalidUid(model.getSubdocumento(), "subdocumento da contabilizzare");
	}

	/**
	 * @return the azioneProvvedimentoConsentita
	 */
	public Boolean getAzioneProvvedimentoConsentita(){
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.PROVVEDIMENTO_SPESA_GESTISCI, sessionHandler.getAzioniConsentite());
	}
	
	/**
	 * Calcolo automatico degli importi iva split sulle quote
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String calcoloAutomaticoImportiIvaSplitQuote() {
		final String methodName = "calcoloAutomaticoImportiIvaSplitQuote";
		
		//chiamo il servizio di proporzionamento degli importi split reverse
		ProporzionaImportiSplitReverse req = model.creaRequestProporzionaImportiSplitReverse();
		logServiceRequest(req);
		
		ProporzionaImportiSplitReverseResponse res = documentoSpesaService.proporzionaImportiSplitReverse(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Ho degli errori: loggo ed esco
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return SUCCESS;
		}
		
		// Nuovi importi salvati. Faccio una ricerca di dettaglio delle quote per ottenere le quote aggiornate
		// (per evitare problemi dovuti alla concorrenza di inserimento)
		ricalcolaLeQuoteAssociate();
		
		//calcolo ed imposto nel model i dati derivati da mostrare a video
		model.calcoloImporti();
		model.impostaFlagAutoCalcoloImportoSplitQuote();
		model.impostaFlagEditabilitaRitenute();
		
		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();

		return SUCCESS;
		
	}
	
	// SIAC-5115
	/**
	 * Caricamento delle sospensioni quota
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniSospensioniQuota() {
		// Placeholder
		return SUCCESS;
	}
	/**
	 * Preparazione per il metodo {@link #addSospensioneQuota()}
	 */
	public void prepareAddSospensioneQuota() {
		model.setSospensioneSubdocumento(null);
	}
	/**
	 * Aggiunge la sospensione della quota
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String addSospensioneQuota() {
		final String methodName = "addSospensioneQuota";
		//aggiungo i dati di sospensione del documento
		model.getListaSospensioneSubdocumento().add(model.getSospensioneSubdocumento());
		log.debug(methodName, "Aggiunta la sospensione della quota");
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #addSospensioneQuota()}
	 */
	public void validateAddSospensioneQuota() {
		//valido i dati di sospensione
		validateSospensione();
	}
	/**
	 * Preparazione per il metodo {@link #updateSospensioneQuota()}
	 */
	public void prepareUpdateSospensioneQuota() {
		model.setSospensioneSubdocumento(null);
		model.setIndexSospensioneQuota(null);
	}
	/**
	 * Aggiorna la sospensione della quota
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String updateSospensioneQuota() {
		final String methodName = "updateSospensioneQuota";
		int index = model.getIndexSospensioneQuota().intValue();
		//aggiorno la lista presente nel model
		model.getListaSospensioneSubdocumento().set(index, model.getSospensioneSubdocumento());
		log.debug(methodName, "Aggiornata la sospensione della quota all'indice " + index);
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #updateSospensioneQuota()}
	 */
	public void validateUpdateSospensioneQuota() {
		checkValidIndex(model.getIndexSospensioneQuota(), model.getListaSospensioneSubdocumento(), "Indice sospensione");
		//valido la sospensione del documento
		validateSospensione();
	}
	/**
	 * Preparazione per il metodo {@link #removeSospensioneQuota()}
	 */
	public void prepareRemoveSospensioneQuota() {
		model.setIndexSospensioneQuota(null);
	}
	/**
	 * Rimuove la sospensione della quota
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String removeSospensioneQuota() {
		final String methodName = "removeSospensioneQuota";
		//rimuovo l'elemento dalla lista presente nel model
		int index = model.getIndexSospensioneQuota().intValue();
		model.getListaSospensioneSubdocumento().remove(index);
		log.debug(methodName, "Rimosso elemento all'indice " + index);
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #removeSospensioneQuota()}
	 */
	public void validateRemoveSospensioneQuota() {
		checkValidIndex(model.getIndexSospensioneQuota(), model.getListaSospensioneSubdocumento(), "Indice sospensione");
	}
	
	/**
	 * Controllo di validit&agrave; per un indice rispetto alla lista cui si riferisce
	 * @param index l'indice
	 * @param list la lista di riferimento
	 * @param fieldName il nome del campo
	 * @param <T> la tipizzazione della lista
	 */
	private <T> void checkValidIndex(Integer index, List<T> list, String fieldName) {
		checkNotNull(index, fieldName, true);
		int intIndex = index.intValue();
		checkCondition(intIndex >= 0 && intIndex < list.size(), ErroreCore.VALORE_NON_VALIDO.getErrore(fieldName, "deve essere compreso tra 0 e " + (list.size() - 1)));
	}
	/**
	 * Validazione della sospensione
	 */
	private void validateSospensione() {
		// Se non ho la sospensione esco subito
		checkNotNull(model.getSospensioneSubdocumento(), "Sospensione", true);
		checkNotNull(model.getSospensioneSubdocumento().getDataSospensione(), "Data sospensione");
		checkNotNullNorEmpty(model.getSospensioneSubdocumento().getCausaleSospensione(), "Causale sospensione");
		
		checkCondition(model.getSospensioneSubdocumento().getDataSospensione() == null
			|| model.getSospensioneSubdocumento().getDataRiattivazione() == null
			|| !model.getSospensioneSubdocumento().getDataRiattivazione().before(model.getSospensioneSubdocumento().getDataSospensione()),
				ErroreCore.VALORE_NON_VALIDO.getErrore("Data riattivazione", ": non puo' essere inferiore alla data di sospensione"));
	}

	/**
	 * Preparazione per il metodo {@link #calcolaDataScadenzaDopoSospensione()}
	 */
	public void prepareCalcolaDataScadenzaDopoSospensione() {
		if(model.getSubdocumento() == null) {
			// Null-safe
			return;
		}
		// Pulisco la data di scadenza
		model.getSubdocumento().setDataScadenza(null);
	}
	/**
	 * Calcolata in automatico in questo modo ma comunque editabile, esempio:
	 * <p>
	 * 60 gg come termine di pagamento:
	 * <ul>
	 *     <li>&rarr; data ricezione fattura: 10 marzo</li>
	 *     <li>&rarr; data sospensione: 13 marzo</li>
	 *     <li>&rarr; data riattivazione: 20 marzo</li>
	 *     <li>&rarr; data scadenza dopo sospensione = 17 maggio</li>
	 * </ul>
	 * In pratica il calcolo &eacute; questo:
	 * <code>Data scadenza + data riattivazione - data sospensione</code>
	 * <br/>
	 * <strong>NOTA</strong>: nel caso di multisospensione (pi&uacute; righe di sospensione) &eacute; necessario
	 * sommare alla data di scadenza tutti i periodi di sospensione inseriti e con data di riattivazione valorizzata
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String calcolaDataScadenzaDopoSospensione() {
		final String methodName = "calcolaDataScadenzaDopoSospensione";
		if(model.getSubdocumento() == null) {
			//non ho un subdocumento, esco
			log.debug(methodName, "Subdocumento non inizializzato: esco");
			return SUCCESS;
		}
		
		if(model.getSubdocumento().getDataScadenza() == null) {
			//impossibile avere data scadenza dopo la sospensione se il subdocumento non e' stato sospeso!
			log.debug(methodName, "Data di scadenza non inizializzata: pulisco la data di scadenza dopo sospensione");
			model.getSubdocumento().setDataScadenzaDopoSospensione(null);
			return SUCCESS;
		}
		log.debug(methodName, "Data di scadenza originaria: " + model.getSubdocumento().getDataScadenza());
		Calendar cal = Calendar.getInstance();
		cal.setTime(model.getSubdocumento().getDataScadenza());
		boolean modifiedDate = false;
		
		for(SospensioneSubdocumento ss : model.getListaSospensioneSubdocumento()) {
			if(ss.getDataRiattivazione() != null) {
				long riattivazione = ss.getDataRiattivazione().getTime();
				long sospensione = ss.getDataSospensione().getTime();
				//ottengo quando tempo sia passato tra la sospensione e la riattivazione
				long diff = TimeUnit.DAYS.convert(riattivazione - sospensione, TimeUnit.MILLISECONDS);
				log.debug(methodName, "Adding " + diff + " days");
				cal.add(Calendar.DAY_OF_YEAR, (int)diff);
				modifiedDate = true;
			}
		}
		// SIAC-5444: se non ho date di riattivazione, non modifico la data di scadenza dopo sospensione
		if(!modifiedDate) {
			//reimposto la data di sospensione originale
			log.debug(methodName, "Data di sospensione non modificata da possibili date di riattivazione. Reimposto l'originale");
			model.getSubdocumento().setDataScadenzaDopoSospensione(model.getDataScadenzaDopoSospensioneOriginale());
			return SUCCESS;
		}
		model.getSubdocumento().setDataScadenzaDopoSospensione(cal.getTime());
		log.debug(methodName, "Data sospensione finale: " + model.getSubdocumento().getDataScadenzaDopoSospensione());
		return SUCCESS;
	}
	/**
	 * Classe di Result specifica per i dati delle quote.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 10/07/2017
	 *
	 */
	public static class DatiQuoteJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 5439496832736670483L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, messaggi.*, informazioni.*, listaSubdocumentoSpesa.*, listaDettaglioOnere.*, messaggiSenzaRichiestaConferma.*, "
				+ "totaleQuote, importoDaAttribuire, totaleDaPagareQuote, documentoIncompleto, flagDatiIvaAccessibile, stato, netto, statoOperativoDocumentoCompleto, "
				+ "flagEditabilitaRitenute, flagSplitQuotePresente, flagAutoCalcoloImportoSplitQuote, contabilizza, attivaRegistrazioniContabiliVisible, flagDatiSospensioneEditabili";
		
		/** Empty default constructor */
		public DatiQuoteJSONResult() {
			super();
			// Workaround per non modificare il codice
			setEnumAsBean(false);
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
	/**
	 * Classe di result specifica per i dati di sospensione della quota
	 * @author Marchino Alessandro
	 */
	public static class DatiSospensioneQuotaJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = -8816938476729899546L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, messaggi.*, informazioni.*, listaSospensioneSubdocumento.*";
		
		/** Empty default constructor */
		public DatiSospensioneQuotaJSONResult() {
			super();
			// Workaround per non modificare il codice
			setEnumAsBean(false);
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	
}
