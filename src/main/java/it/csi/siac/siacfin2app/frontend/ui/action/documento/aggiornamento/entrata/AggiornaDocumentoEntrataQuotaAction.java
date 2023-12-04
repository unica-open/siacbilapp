/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.VerificaBloccoRORHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di action per l'aggiornamento del Documento di entrata, sezione Quote.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataQuotaAction extends AggiornaDocumentoEntrataBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	/** Il suffisso */
	private static final String SUFFIX = "_QUOTE";
	
	/**
	 * Restituisce la lista delle quote relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaQuoteEntrata() {
		return SUCCESS;
	}
	
	/**
	 * Definisce se i campi siano disabilitati.
	 * 
	 * @return the campi disabilitati
	 */
	public String campiDisabilitati() {
		return SUCCESS;
	}

	/**
	 * A seguito della SIAC-6888 si controlla che l'ente sia abilitato a gestire l'accertamento automatico
	 * 
	 * @return boolean
	 */
	public boolean enteAbilitatoInserimentoAutomaticoAccertamento() {
		boolean isAbilitato = false;
		if(model.getEnte().getGestioneLivelli().containsKey(TipologiaGestioneLivelli.ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO)) {
			isAbilitato = true;
		}
		
		return isAbilitato;
	}
	
	/**
	 * Pulisce i campi relativi all'inserimento di una nuova quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserimentoNuovaQuota() {
		// Pulisco i dati
		cleanQuote();
		model.setImpegnoQuotaDisabilitato(Boolean.FALSE);
		model.setImportoQuotaDisabilitato(Boolean.FALSE);
		model.setProvvedimentoQuotaDisabilitato(Boolean.FALSE);
		Date dataScadenzaDocumento = model.getDocumento().getDataScadenza();
		model.getSubdocumento().setDataScadenza(dataScadenzaDocumento);
		
		// Incremento il progressivo
		model.checkAndIncrementProgressivoSubdocumento();
		// Imposto il clone come quota da aggiornare
		SubdocumentoEntrata clone =ReflectionUtil.deepClone(model.getSubdocumento());
		model.impostaQuotaDaInserire(clone);
		// Imposto il suffisso
		model.setSuffisso(SUFFIX);
		
		// Impostazione conto tesoreria (JIRA-2324)
		impostaContoTesororeriaSeUnivoco();
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
		//SIAC-6645
		model.setForzaDisponibilitaAccertamento(false);
		model.setMessaggioConfermaSfondamentoAccertamento(null);
		return SUCCESS;
	}
	
	/**
	 * Se il conto tesoreria &eacute; univoco, lo si assuma di default.
	 */
	private void impostaContoTesororeriaSeUnivoco() {
		if(model.getListaContoTesoreria().size() != 1) {
			return;
		}
		// Se ho un'unica occorrenza, la seleziono
		ContoTesoreria contoTesoreria = model.getListaContoTesoreria().get(0);
		model.setContoTesoreria(contoTesoreria);
	}

	/**
	 * Prepare per l'aggiornamento della quota. Pulisco i campi del model
	 * relativi.
	 */
	public void prepareInizioAggiornamentoNuovaQuota() {
		cleanQuotePerAggiornamento();
	}

	/**
	 * Popola i campi relativi all'aggiornamento della quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornamentoNuovaQuota() {
		
		model.setImportoQuotaDisabilitato(Boolean.FALSE);
		model.setImpegnoQuotaDisabilitato(Boolean.FALSE);
		model.setProvvedimentoQuotaDisabilitato(Boolean.FALSE);
		model.setCapitoloQuotaDisabilitato(Boolean.FALSE);
		
		model.setSuffisso(SUFFIX);

		// Ricerco il subdocumento per uid
		SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
		subdoc.setUid(model.getUidQuota());
		subdoc = ComparatorUtils.searchByUid(model.getListaSubdocumentoEntrata(), subdoc);

		// Imposto il clone come quota da aggiornare
		SubdocumentoEntrata clone = ReflectionUtil.deepClone(subdoc);
		clone.setAttoAmministrativo(subdoc.getAttoAmministrativo());
		model.impostaQuotaDaAggiornare(clone);
		controllaProvvedimento();
		
		//SIAC-4391
		model.setFlagConvalidaManualeQuota(FormatUtils.formatNullableBoolean(subdoc.getFlagConvalidaManuale(), "M", "A"));
		//SIAC-6645
		model.setForzaDisponibilitaAccertamento(false);
		model.setMessaggioConfermaSfondamentoAccertamento(null);
		return SUCCESS;
	}

	/**
	 * Popola i campi relativi alla ripetizione dell'inserimento della quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioRipetiNuovaQuota() {
		// Cerco il subdocumento per uid
		SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
		subdoc.setUid(model.getUidQuota());
		subdoc = ComparatorUtils.searchByUid(model.getListaSubdocumentoEntrata(), subdoc);
		SubdocumentoEntrata clone = ReflectionUtil.deepClone(subdoc);
		// Pulisco i dati
		cleanQuote();
		model.setImpegnoQuotaDisabilitato(Boolean.FALSE);
		model.setImportoQuotaDisabilitato(Boolean.FALSE);
		model.setProvvedimentoQuotaDisabilitato(Boolean.FALSE);
		
		model.impostaQuotaDaRipetere(clone);
		model.checkAndIncrementProgressivoSubdocumento();
		// Imposto il suffisso
		model.setSuffisso(SUFFIX);
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
		//SIAC-6645
		model.setForzaDisponibilitaAccertamento(false);
		model.setMessaggioConfermaSfondamentoAccertamento(null);

		return SUCCESS;
	}

	/**
	 * Prepare per l'inserimento della nuova quota. <br>
	 * Workaround per il salvataggio del movimento di gestione
	 */
	public void prepareInserimentoNuovaQuota() {
		puliziaPreparePerQuote();
	}

	/**
	 * Inserisce la quota fornita dall'utente in quelle associate al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimentoNuovaQuota() {
		final String methodName = "inserimentoNuovaQuota";
		validazioneInserimentoNuovaQuota();
		if (hasErrori()) {
			// Validazione fallita
			log.info(methodName, "Errori di validazione presenti per l'inserimento");
			return SUCCESS;
		}
		if(hasMessaggi()) {
			log.info(methodName, "Messaggi di conferma presenti");
			return ASK;
		}
		InserisceQuotaDocumentoEntrata request = model.creaRequestInserisceQuotaDocumentoEntrata();
		logServiceRequest(request);

		InserisceQuotaDocumentoEntrataResponse response = documentoEntrataService.inserisceQuotaDocumentoEntrata(request);
		logServiceResponse(response);
		
		model.setMessaggioConfermaSfondamentoAccertamento(null);
		if (response.hasErrori()) {
			for (Errore errore : response.getErrori()) {
				if(ErroreFin.DISPONIBILITA_MOVIMENTO_INSUFFICIENTE_MA_ADEGUABILE.getCodice().equals(errore.getCodice())) {
					model.setMessaggioConfermaSfondamentoAccertamento(new Messaggio(errore.getCodice(), errore.getDescrizione()));
					return SUCCESS;
				}
			}
			
			log.info(methodName, createErrorInServiceInvocationString(InserisceQuotaDocumentoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}

		// Nuova quota inserita. Faccio una ricerca di dettaglio delle quote per ottenere le quote aggiornate
		// (per evitare problemi dovuti alla concorrenza di inserimento)
		ricalcolaLeQuoteAssociate();
		model.calcoloImporti();
		// Importo i dati relativi a stato e data inizio validita'
		model.getDocumento().setStatoOperativoDocumento(response.getSubdocumentoEntrata().getDocumento().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(response.getSubdocumentoEntrata().getDocumento().getDataInizioValiditaStato());
		checkAttivazioneRegContabili();
		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Elimina la quota.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaQuota() {
		final String methodName = "eliminaQuota";
		// Effettuo la validazione della quota
		validaEliminaQuota();

		if (hasErrori()) {
			// Errore di validazione
			log.info(methodName, "Errore nella validazione del metodo");
			return SUCCESS;
		}

		EliminaQuotaDocumentoEntrata request = model.creaRequestEliminaQuotaDocumentoEntrata();
		logServiceRequest(request);
		EliminaQuotaDocumentoEntrataResponse response = documentoEntrataService.eliminaQuotaDocumentoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Errore nell'invocazione
			log.info(methodName, createErrorInServiceInvocationString(EliminaQuotaDocumentoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "Quota eliminata");

		// Ricalcolo quote e importi
		ricalcolaLeQuoteAssociate();
		model.calcoloImporti();
		
		// Importo i dati relativi a stato e data inizio validita'
		model.getDocumento().setStatoOperativoDocumento(response.getSubdocumentoEntrata().getDocumento().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(response.getSubdocumentoEntrata().getDocumento().getDataInizioValiditaStato());
		checkAttivazioneRegContabili();
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Prepare per l'aggiornamento della quota. <br>
	 * Workaround per il salvataggio del movimento di gestione
	 */
	public void prepareAggiornamentoQuota() {
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
		if (hasErrori()) {
			// Errore di validazione
			log.info(methodName, "Errori di validazione presenti per l'aggiornamento");
			return SUCCESS;
		}
		if(hasMessaggi()) {
			log.info(methodName, "Messaggi di conferma presenti");
			return ASK;
		}
		log.debug(methodName, "Validazione terminata senza errori");

		AggiornaQuotaDocumentoEntrata request = model.creaRequestAggiornaQuotaDocumentoEntrata();
		logServiceRequest(request);
		AggiornaQuotaDocumentoEntrataResponse response = documentoEntrataService.aggiornaQuotaDocumentoEntrata(request);
		logServiceResponse(response);

		model.setMessaggioConfermaSfondamentoAccertamento(null);
		
		if (response.hasErrori()) {
			
			for (Errore errore : response.getErrori()) {
				if(ErroreFin.DISPONIBILITA_MOVIMENTO_INSUFFICIENTE_MA_ADEGUABILE.getCodice().equals(errore.getCodice())) {
					model.setMessaggioConfermaSfondamentoAccertamento(new Messaggio(errore.getCodice(), errore.getDescrizione()));
					return SUCCESS;
				}
			}
			
			// Errore di invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(AggiornaQuotaDocumentoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "Quota aggiornata");

		// Ricalcolo le quote
		ricalcolaLeQuoteAssociate();
		model.calcoloImporti();
		// Importo i dati relativi a stato e data inizio validita'
		model.getDocumento().setStatoOperativoDocumento(response.getSubdocumentoEntrata().getDocumento().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(response.getSubdocumentoEntrata().getDocumento().getDataInizioValiditaStato());
		checkAttivazioneRegContabili();
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Effettua una pulizia per il prepare delle quote.
	 */
	private void puliziaPreparePerQuote() {
		// Pulisco tutti i campi
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.setCapitolo(null);
		model.setAttoAmministrativo(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setTipoAtto(null);
		model.setProseguireConElaborazione(null);
		model.setSedeSecondariaSoggetto(null);
		model.setProseguireConElaborazioneAttoAmministrativo(Boolean.FALSE);
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
	}

	/**
	 * Valida l'inserimento di una nuova quota.
	 */
	private void validazioneInserimentoNuovaQuota() {
		validazioneNuovaQuota(false);
		checkCoerenzaTotaliQuoteInserimento();
	}

	/**
	 * Valida l'aggiornamento di una nuova quota.
	 */
	private void validazioneAggiornamentoNuovaQuota() {
		validazioneNuovaQuota(true);
		checkCoerenzaTotaliQuoteAggiornamento();
	}

	/**
	 * Valida la quota.
	 */
	private void validazioneNuovaQuota(boolean isAggiornamento) {
		SubdocumentoEntrata subdocumento = model.getSubdocumento();
		DocumentoEntrata documento = model.getDocumento();
		
		// SIAC-4391
		subdocumento.setFlagConvalidaManuale(FormatUtils.parseBoolean(model.getFlagConvalidaManualeQuota(), "M", "A"));

		// Controllo formale
//		checkNotNull(subdocumento.getDataScadenza(), "Data scadenza");
		checkNotNullNorEmpty(subdocumento.getDescrizione(), "Descrizione");

		// L'importo deve essere > 0 se abilitato
		// SIAC-5044
		checkNotNull(subdocumento.getImporto(), "Importo");		
		if (subdocumento.getImporto() == null) {
			subdocumento.setImporto(BigDecimal.ZERO);			
		}
		
		checkCondition(Boolean.TRUE.equals(model.getImportoQuotaDisabilitato()) || BigDecimal.ZERO.compareTo(subdocumento.getImporto()) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore o uguale a zero"));
		// La data di scadenza non deve essere precedente la data di emissione del documento

		//TODO DA VEDERE appena aperta la jira
		//checkCondition(subdocumento.getDataScadenza() == null || documento.getDataEmissione() == null || subdocumento.getDataScadenza().after(documento.getDataEmissione()),
		//		ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
		
		checkCondition(	documento.getDataEmissione() == null ||
				subdocumento.getDataScadenza() == null  || 
				subdocumento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());


		
		AttoAmministrativo atto = model.getAttoAmministrativo();
		checkCondition(checkProvvedimentoEsistente()
				|| atto == null
				|| (atto.getNumero() == 0 && atto.getAnno() == 0 && (model.getTipoAtto() == null || model.getTipoAtto().getUid() == 0)),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("specificare anno e numero atto oppure anno e tipo atto"));
		// Accertamento
		validaAccertamentoPerInserimentoNuovaQuota();

		//Provvisorio. Per ora carico dati senza effettuare controlli
		caricaProvvisorioPerInserimentoNuovaQuota();
		
		// Provvedimento
		if (checkProvvedimentoEsistente()) {
			validaProvvedimentoPerInserimentoNuovaQuota(BilConstants.GESTISCI_DOCUMENTO_ENTRATA_DECENTRATO.getConstant());
		}
		
		if(isAggiornamento && Boolean.TRUE.equals(documento.getContabilizzaGenPcc()) && isUnicaQuotaConMovimento(documento.getListaSubdocumenti(), model.getSubdocumento())){
			checkCondition(model.getMovimentoGestione()!=null 
					&& model.getMovimentoGestione().getAnnoMovimento()!=0 
					&& model.getMovimentoGestione().getNumeroBigDecimal()!=null, 
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Le registrazioni GEN e PCC sul documento sono state attivate. "
							+ "E' obbligatoria almeno una quota con l'accertamento."));
		}
	}
	
	private boolean isUnicaQuotaConMovimento(List<SubdocumentoEntrata> listaSubdocumenti, SubdocumentoEntrata subdocInAggiornamento) {
		int count = 0;
		SubdocumentoEntrata subUnicoConMovimento = null;
		for (SubdocumentoEntrata ss : listaSubdocumenti) {
			if(ss.getAccertamento() != null && ss.getAccertamento().getAnnoMovimento() != 0 && ss.getAccertamento().getNumeroBigDecimal() != null){
				count++;
				subUnicoConMovimento = ss;
				if(count > 1){
					return false;
				}
			}
		}
		
		return count <= 1 && subUnicoConMovimento!=null && subUnicoConMovimento.getUid() == subdocInAggiornamento.getUid();
	}
	
	/**
	 * Carica il provvisorio per l'inserimento della nuova quota.
	 */
	private void caricaProvvisorioPerInserimentoNuovaQuota() {
		if(model.getSubdocumento().getProvvisorioCassa() == null ||
				(model.getSubdocumento().getProvvisorioCassa().getNumero() == null && model.getSubdocumento().getProvvisorioCassa().getAnno() == null)){
			return;
		}
		
		if(model.getSubdocumento().getProvvisorioCassa().getNumero() == null || model.getSubdocumento().getProvvisorioCassa().getAnno() == null){
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("numero e data provvisorio di cassa", ": non possono essere valorizzati singolarmente"));
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		checkCondition(provvisorioDiCassa != null , ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", model.getSubdocumento().getProvvisorioCassa().getAnno()
				 +  "/" + model.getSubdocumento().getProvvisorioCassa().getNumero()));
		model.getSubdocumento().setProvvisorioCassa(provvisorioDiCassa);
		
	}
	
	/**
	 * Ricerca il provvisorio di cassa.
	 * 
	 * @return il provvisorio trovato
	 */
	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
		
		RicercaProvvisorioDiCassaPerChiave reqRPK = model.creaRequestProvvisorioCassa();
		RicercaProvvisorioDiCassaPerChiaveResponse resRPK = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRPK);
		
		return resRPK.getProvvisorioDiCassa();
	}

	/**
	 * Effettua il controllo di coerenza sul totale delle quote.
	 */
	private void checkCoerenzaTotaliQuoteInserimento() {
		//TOTALE QUOTE <= IMPORTO + ARROTONDAMENTO
		BigDecimal nuovoTotaleQuote = model.getTotaleQuote().add(model.getSubdocumento().getImporto());
		BigDecimal totalePerCoerenza =nuovoTotaleQuote.subtract(model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()));
		checkCondition(totalePerCoerenza.signum() <= 0, ErroreFin.IMPORTO_QUOTE_E_IMPORTO_DOCUMENTO_INCONGRUENTI.getErrore());
		//TOTALE QUOTE >= TOTALE NOTE
		checkCondition(nuovoTotaleQuote.subtract(model.getTotaleImportoDaDedurreSuFattura()).signum()>= 0, 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo quota", ": il totale delle quote deve essere maggiore o uguale al totale delle note credito"));
	}

	/**
	 * Effettua il controllo di coerenza sul totale delle quote.
	 */
	private void checkCoerenzaTotaliQuoteAggiornamento() {
		
		SubdocumentoEntrata quotaOld = new SubdocumentoEntrata();
		quotaOld.setUid(model.getUidQuota());
		int index = ComparatorUtils.getIndexByUid(model.getListaSubdocumentoEntrata(), quotaOld);
		quotaOld = model.getListaSubdocumentoEntrata().get(index);
		
		BigDecimal nuovoTotaleQuote = model.getTotaleQuote().add(model.getSubdocumento().getImporto()).subtract(quotaOld.getImporto());
			
//		TOTALE QUOTE <= IMPORTO + ARROTONDAMENTO
		BigDecimal totalePerCoerenzaTotale =nuovoTotaleQuote.subtract(model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()));
		checkCondition(totalePerCoerenzaTotale.signum() <= 0, ErroreFin.IMPORTO_QUOTE_E_IMPORTO_DOCUMENTO_INCONGRUENTI.getErrore());
		
//		IMPORTO QUOTA >= IMPORTO DA DEDURRE
		BigDecimal totalePerCoerenzaImporto = model.getSubdocumento().getImporto().subtract(model.getSubdocumento().getImportoDaDedurre());
		checkCondition(totalePerCoerenzaImporto.signum() >= 0, 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo quota", ": l'importo della quota deve essere maggiore dell'importo da dedurre"));
		
		//TOTALE QUOTE >= TOTALE NOTE
		checkCondition(nuovoTotaleQuote.subtract(model.getTotaleImportoDaDedurreSuFattura()).signum()>= 0, 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo quota", ": il totale delle quote deve essere maggiore o uguale al totale delle note credito"));
		
	}

	/**
	 * Validazione dell'Accertamento (Movimento di Gestione)
	 */
	private void validaAccertamentoPerInserimentoNuovaQuota() {
		final String methodName = "validaAccertamentoPerInserimentoNuovaQuota";

		Accertamento accertamento = model.getMovimentoGestione();
	
		boolean accertamentoDaRicercare = accertamento != null && accertamento.getAnnoMovimento() > 0 && accertamento.getNumeroBigDecimal() != null;
		
		checkCondition( accertamento == null || !(accertamento.getAnnoMovimento() == 0 ^ accertamento.getNumeroBigDecimal() == null) ,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno accertamento e numero accertamento",
						": devono essere entrambi selezionati o entrambi non selezionati"));
		// L'anno dell'accertamento deve essere coerente con l'anno di esercizio
		checkCondition( accertamento == null || accertamento.getAnnoMovimento() <= model.getAnnoEsercizioInt(),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno accertamento", ": non deve essere superiore all'anno di esercizio"));

		//SIAC-7470-bloccoROR: verifico se serve ricaricare l'accertamento con la sua lista di MovimentiModifica
		if(!accertamentoDaRicercare){
			accertamentoDaRicercare = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.BLOCCO_SU_INCASSI_RESIDUI, sessionHandler.getAzioniConsentite());
		}
		if (accertamentoDaRicercare) {
			try {
				Accertamento a = ricercaAccertamentoPerChiaveOttimizzato(accertamentoDaRicercare);
				model.setMovimentoGestione(a);
				validazioneAccertamentoDaResponse(a);
			} catch(WebServiceInvocationFailureException wsife) {
				log.info(methodName, wsife.getMessage());
			}
		}
	}
	
	/**
	 * Ricerca l'accertamento per chiave chiamando il servizio ottimizzato.
	 * 
	 * @return l'accertamento trovato
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private Accertamento ricercaAccertamentoPerChiaveOttimizzato(boolean forceToSearch) throws WebServiceInvocationFailureException {
		Accertamento accertamento = sessionHandler.getParametro(BilSessionParameter.ACCERTAMENTO);
		
		if(isNecessarioRicaricareAccertamento(accertamento) || forceToSearch) {
			RicercaAccertamentoPerChiaveOttimizzato request = null;
			if(forceToSearch){
				request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato_BloccoROR(model.getMovimentoGestione());
			}else{
				request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(model.getMovimentoGestione());
			}
			logServiceRequest(request);
			
			RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(RicercaAccertamentoPerChiaveOttimizzato.class, response);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(response.isFallimento()) {
				String errorMsg = "Risultato ottenuto dal servizio RicercaAccertamentoPerChiave: FALLIMENTO";
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(response.getAccertamento() == null) {
				String chiaveAccertamento = request.getpRicercaAccertamentoK().getAnnoEsercizio()
						+ "/" + request.getpRicercaAccertamentoK().getAnnoAccertamento()
						+ "/" + request.getpRicercaAccertamentoK().getNumeroAccertamento();
				String errorMsg = "Accertamento non presente per chiave " + chiaveAccertamento;
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", chiaveAccertamento));
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			accertamento = response.getAccertamento();
			// Default per le liste
			accertamento.setElencoSubAccertamenti(defaultingList(accertamento.getElencoSubAccertamenti()));
			// Inizializzo il capitolo se non gia' presente
			if(accertamento.getCapitoloEntrataGestione() == null) {
				// Se il capitolo non e' stato impostato dal servizio, lo imposto io
				accertamento.setCapitoloEntrataGestione(response.getCapitoloEntrataGestione());
			}
			sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamento);
		}
		
		return accertamento;
	}

	/**
	 * Valida l'accertamento o il subaccertamento ottenuto dalla response.
	 * 
	 * @param accertamento l'accertamento da validare
	 */
	private void validazioneAccertamentoDaResponse(Accertamento accertamento) {
		
		SubAccertamento subaccertamento = model.getSubMovimentoGestione();
		Soggetto soggettoCollegatoAllAccertamento = accertamento.getSoggetto();
		Soggetto soggettoCollegatoAlDocumento = model.getSoggetto();
		
		checkCondition(subaccertamento != null || StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata()),
				ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));

		// Check del subaccertamento nella lista degli accertamenti
		if (subaccertamento != null && subaccertamento.getNumeroBigDecimal() != null) {
			SubAccertamento sub = null;
			for (SubAccertamento s : accertamento.getElencoSubAccertamenti()) {
				if (s.getNumeroBigDecimal().compareTo(subaccertamento.getNumeroBigDecimal()) == 0) {
					sub = s;
					soggettoCollegatoAllAccertamento = s.getSoggetto();
					break;
				}
			}
			checkCondition(sub != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", subaccertamento.getNumeroBigDecimal()+""));
			if(sub != null){
				model.setSubMovimentoGestione(sub);
				checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(sub.getStatoOperativoMovimentoGestioneEntrata()),
						ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			}
			
		}

		// Il soggetto collegato all'accertamento deve essere lo stesso legato al documento
		checkCondition(soggettoCollegatoAllAccertamento == null 
						|| soggettoCollegatoAllAccertamento.getUid() == 0
						|| (soggettoCollegatoAllAccertamento.getUid() == soggettoCollegatoAlDocumento.getUid()),
				ErroreFin.SOGGETTO_DIVERSO_DA_QUELLO_DEL_DOCUMENTO.getErrore("accertamento"));

		/// Check sulla classe del soggetto
		//Vedi jira 1245 e jira 1816
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione()) 
				//SIAC-7529
				//il controllo avviene solo quando il codice del soggetto non è uguale
				&& !soggettoCollegatoAlDocumento.getCodiceSoggetto().equals(soggettoCollegatoAllAccertamento.getCodiceSoggetto())) {
			checkClasseSoggettoAccertamento(accertamento, soggettoCollegatoAlDocumento);
		}
		
		//SIAC-7470
		//SIAC-6997-bloccoROR:
		boolean test = checkBloccoRor(accertamento, model.getAnnoEsercizioInt());
		//se l'impegno ha superato i test, verifico gli eventuali subImpegni
		if(!test && accertamento.getElencoSubAccertamenti() != null && !accertamento.getElencoSubAccertamenti().isEmpty()){
			for(int k = 0; k < accertamento.getElencoSubAccertamenti().size(); k++){
				checkBloccoRor(accertamento.getElencoSubAccertamenti().get(k), model.getAnnoEsercizioInt());
			}
		}
		
		model.setProseguireConElaborazione(Boolean.FALSE);
	}

	private boolean checkBloccoRor(Accertamento accertamento, Integer annoEsercizio){
		//il controllo viene demandato ad apposita classe di utilità
		boolean checkBloccoROR = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento, model.getAnnoEsercizioInt());
		//la gestione della messaggistica deve avvenire nella presente action
		if(checkBloccoROR){
			checkCondition(!checkBloccoROR, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
		}
		return checkBloccoROR;
	}
	
	/**
	 * Riottiene le quote associate al Documento a partire dal servizio di
	 * ricerca delle quote. <br>
	 * Sebbene questo controllo possa sembrare inutile, esso permette di non
	 * perdere quote scritte da un utente differente in contemporanea alla
	 * scrittura delle quote scritte dall'utente attuale.
	 */
	private void ricalcolaLeQuoteAssociate() {
		RicercaQuoteByDocumentoEntrata request = model.creaRequestRicercaQuoteByDocumentoEntrata();
		logServiceRequest(request);
		RicercaQuoteByDocumentoEntrataResponse response = documentoEntrataService.ricercaQuoteByDocumentoEntrata(request);
		logServiceResponse(response);

		// Pulisce il model
		cleanQuote();

		// Imposta la lista dei subdocumenti
		model.setListaSubdocumentoEntrata(response.getSubdocumentiEntrata());
		model.getDocumento().setListaSubdocumenti(response.getSubdocumentiEntrata());

		// Ricalcola i totali
		model.ricalcolaTotaliQuote();

		// Ricalcola i flag
		model.ricalcolaFlagIva();
	}
	
	@Override
	protected void cleanQuote() {
		super.cleanQuote();
		
		// Creo il nuovo capitolo
		CapitoloEntrataGestione c = new CapitoloEntrataGestione();
		c.setAnnoCapitolo(model.getAnnoEsercizioInt());
		// Ottengo la data di scadenza del subdocumento
		
		// Creo il nuovo subdocumento di entrata
		SubdocumentoEntrata se = new SubdocumentoEntrata();
		
		model.setCapitolo(c);
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.setSubdocumento(se);
		model.setSedeSecondariaSoggetto(null);
	}
	
	@Override
	protected void cleanQuotePerAggiornamento() {
		super.cleanQuotePerAggiornamento();
		model.setMovimentoGestione(null);
		model.setSedeSecondariaSoggetto(null);
		
		// Creo il nuovo capitolo
		CapitoloEntrataGestione c = new CapitoloEntrataGestione();
		c.setAnnoCapitolo(model.getAnnoEsercizioInt());
		model.setCapitolo(c);
		
		// SIAC-4391
		model.setFlagConvalidaManualeQuota(null);
	}
	
	
	/**
	 * Effettua il controllo di uguaglianza tra le classi di soggetto del movimento di gestione e del soggetto.
	 * 
	 * @param accertamento      l'accertamento da cui ottenere la classe
	 * @param soggetto          il soggetto di cui controllare le classi
	 */
	private void checkClasseSoggettoAccertamento(Accertamento accertamento, Soggetto soggetto) {
		final String methodName = "checkClasseSoggettoAccertamento";
		ClasseSoggetto classeSoggettoMovimentoGestione = accertamento.getClasseSoggetto();
		if(classeSoggettoMovimentoGestione == null) {
			log.debug(methodName, "Classe soggetto non presente per il movimento di gestione");
			return;
		}
		List<ClassificazioneSoggetto> listaClasseSoggettoSoggetto = soggetto.getElencoClass();
		if(listaClasseSoggettoSoggetto == null) {
			log.debug(methodName, "Classi soggetto non presenti sul soggetto");
			addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore("accertamento"));
			return;
		}
		for(ClassificazioneSoggetto cs : listaClasseSoggettoSoggetto) {
			if(cs.getSoggettoClasseCode().equalsIgnoreCase(classeSoggettoMovimentoGestione.getCodice())) {
				log.debug(methodName, "ho trovato un codice classe corrispondente: " + cs.getSoggettoClasseCode());
				return;
			}
		}
		addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore("accertamento"));
		model.setProseguireConElaborazione(Boolean.TRUE);
	}

	
	/**
	 * Valuta se e' necessario richiamare il servizio per caricare l'impegno.
	 * @return <code>true</code> se il servizio deve essere richiamato, <code>false</code> altrimenti.
	 * */
	private boolean isNecessarioRicaricareAccertamento(Accertamento accertamentoDaSessione) {
		//se ho il numero del submovimento, devo ricaricarlo sempre!
		return model.getSubMovimentoGestione() != null && model.getSubMovimentoGestione().getNumeroBigDecimal() !=null ||
				 !ValidationUtil.isValidMovimentoGestioneFromSession(accertamentoDaSessione, model.getMovimentoGestione());
	}
}
