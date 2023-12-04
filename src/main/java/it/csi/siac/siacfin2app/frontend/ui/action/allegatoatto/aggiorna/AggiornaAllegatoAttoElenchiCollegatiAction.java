/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.aggiorna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe di Action per l'aggiornamento dell'allegato atto, per gli elenchi collegati.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/ott/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AggiornaAllegatoAttoElenchiCollegatiAction extends AggiornaAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4764287116718622096L;
	/** Pattern per il CIG */
	private static final Pattern CIG_PATTERN = Pattern.compile("[A-Z0-9]{10}");
	/** Pattern per il Cup */
	private static final Pattern CUP_PATTERN = Pattern.compile("[A-Z][A-Z0-9]{2}[A-Z][A-Z0-9]{11}");
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient ProvvisorioService provvisorioService;

	/**
	 * Ottiene la lista degli elenchi per l'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaElenchi() {
		// Non faccio nulla: segnaposto per la chiamata AJAX
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaElenco()}.
	 */
	public void prepareEliminaElenco() {
		model.setRow(null);
	}
	
	/**
	 * Elimina l'elenco dall'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaElenco() {
		final String methodName = "eliminaElenco";
		
		checkCondition(model.getUidElencoDaEliminare() != null && model.getUidElencoDaEliminare() > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elenco"),true);
		
		List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = sessionHandler.getParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO);
		ElencoDocumentiAllegato eda = ottieniElencoFromLista(listaElencoDocumentiAllegato);
		
		checkCondition(eda != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("uid Elenco fornito non presente nella lista degli elenchi"),true);
		eda.setUid(model.getUidElencoDaEliminare());
		model.setElencoDocumentiAllegato(eda);
		// Disassocio l'elenco
		DisassociaElenco req = model.creaRequestDisassociaElenco();
		logServiceRequest(req);
		DisassociaElencoResponse res = allegatoAttoService.disassociaElenco(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(DisassociaElenco.class, res));
			addErrori(res);
			return SUCCESS;
		}
		
		// Elimino l'elenco e pulisco i dati
		listaElencoDocumentiAllegato.remove(eda);
		model.setListaElementoElencoDocumentiAllegato(null);
		model.setListaElencoDocumentiAllegato(listaElencoDocumentiAllegato);
		model.setUidElencoDaEliminare(null);
		
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO, listaElencoDocumentiAllegato);
		// Imposto il messaggio di successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * cerca L'elenco
	 * @param listaElencoDocumentiAllegato
	 * @return l'elenco della lista di dato uid
	 */
	private ElencoDocumentiAllegato ottieniElencoFromLista(List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato) {
		for (ElencoDocumentiAllegato el : listaElencoDocumentiAllegato) {
			if(el.getUid() == model.getUidElencoDaEliminare()){
				return el;
			}
		}
		return null;
	}

	/**
	 * Preparazione per il metodo {@link #associaElenco()}.
	 */
	public void prepareAssociaElenco() {
		model.setElencoDocumentiAllegato(null);
	}
	
	/**
	 * Associa l'elenco all'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String associaElenco() {
		final String methodName = "associaElenco";
		
		checkCondition(model.getElencoDocumentiAllegato() != null && model.getElencoDocumentiAllegato().getUid() != 0,
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco"));
		if(hasErrori()) {
			// Errori di validazione del dato
			log.debug(methodName, "Errori di validazione dei dati");
			return SUCCESS;
		}
		
		AssociaElenco req = model.creaRequestAssociaElenco();
		logServiceRequest(req);
		AssociaElencoResponse res = allegatoAttoService.associaElenco(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AssociaElenco.class, res));
			addErrori(res);
			return SUCCESS;
		}
		
		// Elenco associato con successo
		log.debug(methodName, "Associato con successo elenco con uid " + model.getElencoDocumentiAllegato().getUid()
			+ " con allegato con uid " + model.getAllegatoAtto().getUid());
		// Aggiungo il dato nella lista degli elenchi
		List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = sessionHandler.getParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO);
		listaElencoDocumentiAllegato.add(res.getElencoDocumentiAllegato());
		
		// Imposto la lista nel model e in sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO, listaElencoDocumentiAllegato);
		model.setListaElencoDocumentiAllegato(listaElencoDocumentiAllegato);
		
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	// SIAC-5021
	/**
	 * Preparazione per il metodo {@link #caricaDatiAggiornamentoSubdocumentoElenco()}
	 */
	public void prepareCaricaDatiAggiornamentoSubdocumentoElenco() {
		model.setUidSubdocumento(null);
		model.setSubdocumentoEntrata(null);
		model.setSubdocumentoSpesa(null);
		model.setElencoDocumentiAllegato(null);
		model.setModalitaPagamentoSoggetto(null);
	}
	/**
	 * Caricamento dei dati per l'aggiornamento del subdoc
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaDatiAggiornamentoSubdocumentoElenco() {
		String methodName = "caricaDatiAggiornamentoSubdocumentoElenco";
		try {
			impostaDatiAggiorna();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		String suffix = model.getSubdocumentoSpesa() != null ? "_spesa" : "_entrata";
		return SUCCESS + suffix;
	}
	/**
	 * Impostazione dei dati di aggiornamento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nel caricamento dei dati
	 */
	private void impostaDatiAggiorna() throws WebServiceInvocationFailureException {
		List<Subdocumento<?, ?>> subdocs = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO);
		Subdocumento<?, ?> sub = new SubdocumentoSpesa();
		sub.setUid(model.getUidSubdocumento().intValue());
		
		sub = ComparatorUtils.searchByUid(subdocs, sub);
		if(sub instanceof SubdocumentoEntrata) {
			impostaDatiAggiornaEntrata((SubdocumentoEntrata)sub);
		} else if (sub instanceof SubdocumentoSpesa) {
			impostaDatiAggiornaSpesa((SubdocumentoSpesa)sub);
		}
	}
	/**
	 * Impostazione dei dati di aggiornamento dell'entrata
	 * @param sub il subdocumento di entrata
	 */
	private void impostaDatiAggiornaEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		model.setSubdocumentoEntrata(subdocumentoEntrata);
		model.setDocumentoEntrata(subdocumentoEntrata.getDocumento());
	}
	/**
	 * Impostazione dei dati di aggiornamento della spesa
	 * @param sub il subdocumento di spesa
	 * @throws WebServiceInvocationFailureException in caso di fallimento nel caricamento dei dati
	 */
	private void impostaDatiAggiornaSpesa(SubdocumentoSpesa subdocumentoSpesa) throws WebServiceInvocationFailureException {
		String methodName = "impostaDatiAggiornaSpesa";
		RicercaDettaglioQuotaSpesa req = model.creaRequestRicercaDettaglioQuotaSpesa(subdocumentoSpesa);
		RicercaDettaglioQuotaSpesaResponse res = documentoSpesaService.ricercaDettaglioQuotaSpesa(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioQuotaSpesa.class, res));
		}
		
		model.setSubdocumentoSpesa(res.getSubdocumentoSpesa());
		model.setDocumentoSpesa(res.getSubdocumentoSpesa().getDocumento());
		model.setModalitaPagamentoSoggetto(res.getSubdocumentoSpesa().getModalitaPagamentoSoggetto());
		
		log.debug(methodName, "Caricamento della modalita' di pagamento del soggetto");
		caricaModalitaPagamentoSoggetto();
	}
	/**
	 * Validazione per il metodo {@link #caricaDatiAggiornamentoSubdocumentoElenco()}
	 */
	public void validateCaricaDatiAggiornamentoSubdocumentoElenco() {
		checkNotNull(model.getUidSubdocumento(), "uid subdocumento", true);
		
		ElencoDocumentiAllegato eda = sessionHandler.getParametro(BilSessionParameter.ELENCO_DOCUMENTI_ALLEGATO_LIGHT);
		checkNotNullNorInvalidUid(eda, "elenco");
		model.setElencoDocumentiAllegato(eda);
		
		List<Subdocumento<?, ?>> subdocs = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO);
		checkNotNull(subdocs, "subdocumenti");
		
		Subdocumento<?, ?> sub = new SubdocumentoSpesa();
		sub.setUid(model.getUidSubdocumento().intValue());
		
		sub = ComparatorUtils.searchByUid(subdocs, sub);
		checkNotNullNorInvalidUid(sub, "subdocumento");
		checkCondition(sub instanceof SubdocumentoEntrata || sub instanceof SubdocumentoSpesa,
				ErroreCore.ERRORE_DI_SISTEMA.getErrore("il subdocumento selezionato non risulta essere di spesa ne' di entrata"));
	}

	
	// SIAC-5043
	/**
	 * Preparazione per il metodo {@link #caricaDatiSpezzaSubdocumentoElenco()}
	 */
	public void prepareCaricaDatiSpezzaSubdocumentoElenco() {
		model.setUidSubdocumento(null);
		model.setSubdocumentoEntrata(null);
		model.setSubdocumentoSpesa(null);
		model.setNuovoSubdocumentoSpesa(null);
		model.setNuovoSubdocumentoEntrata(null);
		model.setElencoDocumentiAllegato(null);
		model.setModalitaPagamentoSoggetto(null);
	}
	/**
	 * Carica la quota per operazione di spezzamento
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaDatiSpezzaSubdocumentoElenco() {
		String methodName = "spezzaSubdocumentoElenco";
		try {
			impostaDatiSpezza();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		String suffix = model.getSubdocumentoSpesa() != null ? "_spesa" : "_entrata";
		return SUCCESS + suffix;
	}
	/**
	 * Impostazione dei dati per lo spezza
	 * @throws WebServiceInvocationFailureException in caso di errore nel caricamento dati
	 */
	private void impostaDatiSpezza() throws WebServiceInvocationFailureException {
		List<Subdocumento<?, ?>> subdocs = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO);
		Subdocumento<?, ?> sub = new SubdocumentoSpesa();
		sub.setUid(model.getUidSubdocumento().intValue());
		
		sub = ComparatorUtils.searchByUid(subdocs, sub);
		
		// SUAC-5468: se la quota e' collegata a una nota di credito non permettere lo spezzamento
		if(sub.getImportoDaDedurre() != null && sub.getImportoDaDedurre().signum() > 0) {
			Errore errore = ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la quota da spezzare e' collegata a una nota di credito");
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		if(sub instanceof SubdocumentoEntrata) {
			// Caricamento dati di entrata
			impostaDatiSpezzaEntrata((SubdocumentoEntrata)sub);
		} else if (sub instanceof SubdocumentoSpesa) {
			// Caricamento dati di spesa
			impostaDatiSpezzaSpesa((SubdocumentoSpesa)sub);
		}
	}

	/**
	 * Impostazione dei dati di spezzamento per l'entrata
	 * @param subdocumentoEntrata il subdoc di entrata per cui impostare i dati
	 */
	private void impostaDatiSpezzaEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		model.setSubdocumentoEntrata(subdocumentoEntrata);
		model.setDocumentoEntrata(subdocumentoEntrata.getDocumento());
		
		SubdocumentoEntrata nuovoSubdocumentoEntrata = new SubdocumentoEntrata();
		nuovoSubdocumentoEntrata.setImporto(subdocumentoEntrata.getImporto());
		nuovoSubdocumentoEntrata.setProvvisorioCassa(subdocumentoEntrata.getProvvisorioCassa());
		nuovoSubdocumentoEntrata.setDataScadenza(subdocumentoEntrata.getDataScadenza());
		model.setNuovoSubdocumentoEntrata(nuovoSubdocumentoEntrata);
	}
	
	/**
	 * Impostazione dei dati di spezzamento per la spesa
	 * @param subdocumentoEntrata il subdoc di spesa per cui impostare i dati
	 * @throws WebServiceInvocationFailureException in caso di errore nel caricamento dati
	 */
	private void impostaDatiSpezzaSpesa(SubdocumentoSpesa subdocumentoSpesa) throws WebServiceInvocationFailureException {
		final String methodName = "impostaDatiSpezzaSpesa";
		model.setSubdocumentoSpesa(subdocumentoSpesa);
		model.setDocumentoSpesa(subdocumentoSpesa.getDocumento());
		model.setModalitaPagamentoSoggetto(subdocumentoSpesa.getModalitaPagamentoSoggetto());
		
		SubdocumentoSpesa nuovoSubdocumentoSpesa = new SubdocumentoSpesa();
		nuovoSubdocumentoSpesa.setImporto(subdocumentoSpesa.getImporto());
		nuovoSubdocumentoSpesa.setImportoSplitReverse(subdocumentoSpesa.getImportoSplitReverse());
		nuovoSubdocumentoSpesa.setProvvisorioCassa(subdocumentoSpesa.getProvvisorioCassa());
		nuovoSubdocumentoSpesa.setDataScadenza(subdocumentoSpesa.getDataScadenza());
		nuovoSubdocumentoSpesa.setCig(subdocumentoSpesa.getCig());
		nuovoSubdocumentoSpesa.setCup(subdocumentoSpesa.getCup());
		model.setNuovoSubdocumentoSpesa(nuovoSubdocumentoSpesa);
		
		log.debug(methodName, "Caricamento della modalita' di pagamento del soggetto");
		caricaModalitaPagamentoSoggetto();
	}

	/**
	 * Validazione per il metodo {@link #caricaDatiSpezzaSubdocumentoElenco()}
	 */
	public void validateCaricaDatiSpezzaSubdocumentoElenco() {
		checkNotNull(model.getUidSubdocumento(), "uid subdocumento", true);
		
		ElencoDocumentiAllegato eda = sessionHandler.getParametro(BilSessionParameter.ELENCO_DOCUMENTI_ALLEGATO_LIGHT);
		checkNotNullNorInvalidUid(eda, "elenco");
		model.setElencoDocumentiAllegato(eda);
		
		List<Subdocumento<?, ?>> subdocs = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO);
		checkNotNull(subdocs, "subdocumenti");
		
		Subdocumento<?, ?> sub = new SubdocumentoSpesa();
		sub.setUid(model.getUidSubdocumento().intValue());
		
		sub = ComparatorUtils.searchByUid(subdocs, sub);
		checkNotNullNorInvalidUid(sub, "subdocumento");
		checkCondition(sub instanceof SubdocumentoEntrata || sub instanceof SubdocumentoSpesa,
				ErroreCore.ERRORE_DI_SISTEMA.getErrore("il subdocumento selezionato non risulta essere di spesa ne' di entrata"));
	}
	
	/**
	 * Caricamento della modalit&agrave; di pagamento del soggetto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaModalitaPagamentoSoggetto() throws WebServiceInvocationFailureException {
		// Ottengo le liste dalla sessione
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		if(listaModalitaPagamentoSoggetto == null || soggetto == null || soggetto.getUid() != model.getDocumentoSpesa().getSoggetto().getUid()) {
			RicercaSoggettoPerChiave req = model.creaRequestRicercaSoggettoPerChiave();
			// Non ho dati in sessione ovvero sono per un soggetto differente. Ricarico
			RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, res));
			}
			soggetto = res.getSoggetto();
			
			listaModalitaPagamentoSoggetto = res.getListaModalitaPagamentoSoggetto();
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		// Imposto le liste nel model
		model.setListaModalitaPagamentoSoggettoFiltrate(impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto));
	}
	
	@Override
	protected List<ModalitaPagamentoSoggetto> impostaListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> lista) {
		List<ModalitaPagamentoSoggetto> res = new ArrayList<ModalitaPagamentoSoggetto>();
		Date now = new Date();
		
		//per ogni modalita' poagamento controllo lo stato e la data di scadenza.
		for(ModalitaPagamentoSoggetto mps : lista) {
			if(isStatoOperativoModalitaPagamentoSoggettoValido(mps) && (mps.getDataScadenza() == null || !mps.getDataScadenza().before(now))) {
				//la modalita' di pagamento soddisfa i criteri, posso mostrarla 
				res.add(mps);
			}
		}
		
		return res;
	}
	
	/**
	 * Controlla che lo stato operativo della modalita di pagamento sia valido.
	 * 
	 * @param modalitaPagamentoSoggetto la modalita di pagamento
	 * @return <code>true</code> se lo stato &eacute; <code>VALIDO</code>; <code>false</code> altrimenti
	 */
	private boolean isStatoOperativoModalitaPagamentoSoggettoValido(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		//per le modalita pagamento di tipo non- cessione, controllo lo stato della modalita' pagamento. 
		//Altrimenti, devo controllare lo stato della modalita' a cui e' stato "ceduto" il pagamento 
		return StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(modalitaPagamentoSoggetto.getCodiceStatoModalitaPagamento())
			|| (modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() != null
				&& StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getCodiceStatoModalitaPagamento()));
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaSubdocumentoElenco()}.
	 */
	public void prepareAggiornaSubdocumentoElenco() {
		//pulisco i dati nel model
		model.setElencoDocumentiAllegato(null);
		model.setSubdocumentoSpesa(null);
		model.setSubdocumentoEntrata(null);
		model.setDocumentoSpesa(null);
		model.setDocumentoEntrata(null);
		model.setModalitaPagamentoSoggetto(null);
	}
	
	/**
	 * Aggiorna il subdocumento dell'elenco dall'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaSubdocumentoElenco() {
		// Sono in uno solo dei sue casi, spesa o entrata
		if(model.getSubdocumentoSpesa() != null) {
			aggiornaSubdocumentoSpesaElenco();
		} else {
			aggiornaSubdocumentoEntrataElenco();
		}
		return SUCCESS;
	}
	
	/**
	 * Aggiorna il subdocumento di spesa per l'elenco.
	 */
	private void aggiornaSubdocumentoSpesaElenco() {
		final String methodName = "aggiornaSubdocumentoSpesaElenco";
		// Aggiorno l'importo, e quindi gli attributi
		AggiornaImportiQuoteDocumentoSpesa requestImporti = model.creaRequestAggiornaImportiQuoteDocumentoSpesa();
		logServiceRequest(requestImporti);
		AggiornaImportiQuoteDocumentoSpesaResponse responseImporti = documentoSpesaService.aggiornaImportoQuoteDocumentoSpesa(requestImporti);
		logServiceResponse(responseImporti);
		
		// Controllo gli errori
		if(responseImporti.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AggiornaImportiQuoteDocumentoSpesa.class, responseImporti));
			addErrori(responseImporti);
			return;
		}
		log.debug(methodName, "Aggiornati gli importi per la quota " + model.getSubdocumentoSpesa().getUid());
		
		AggiornaAttributiQuotaDocumentoSpesa requestAttributi = model.creaRequestAggiornaAttributiQuotaDocumentoSpesa();
		logServiceRequest(requestAttributi);
		AggiornaAttributiQuotaDocumentoSpesaResponse responseAttributi = documentoSpesaService.aggiornaAttributiQuotaDocumentoSpesa(requestAttributi);
		logServiceResponse(responseAttributi);
		
		// Controllo gli errori
		if(responseAttributi.hasErrori()) {
			//si sono cverificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaAttributiQuotaDocumentoSpesa.class, responseAttributi));
			addErrori(responseAttributi);
			return;
		}
		log.debug(methodName, "Attributi aggiornati correttamente");
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
	}

	/**
	 * Aggiorna il subdocumento di entrata per l'elenco.
	 */
	private void aggiornaSubdocumentoEntrataElenco() {
		final String methodName = "aggiornaSubdocumentoEntrataElenco";
		// Aggiorno l'importo
		AggiornaImportiQuoteDocumentoEntrata req = model.creaRequestAggiornaImportiQuoteDocumentoEntrata();
		logServiceRequest(req);
		AggiornaImportiQuoteDocumentoEntrataResponse res = documentoEntrataService.aggiornaImportoQuoteDocumentoEntrata(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificatoi degli erori:esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaImportiQuoteDocumentoEntrata.class, res));
			addErrori(res);
			return;
		}
		log.debug(methodName, "Aggiornati gli importi per la quota " + model.getSubdocumentoEntrata().getUid());
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
	}

	/**
	 * Validazione per il metodo {@link #aggiornaSubdocumentoElenco()}.
	 */
	@SuppressWarnings("unchecked")
	public void validateAggiornaSubdocumentoElenco() {
		checkNotNull(model.getElencoDocumentiAllegato(), "elenco");
		Subdocumento<?, ?> subdoc = ObjectUtils.firstNonNull(model.getSubdocumentoSpesa(), model.getSubdocumentoEntrata());
		checkNotNull(subdoc, "subdocumento", true);
		// Validazione importi
		checkCondition(subdoc.getImporto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo in atto"));
		checkCondition(subdoc.getImporto() == null || subdoc.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo in atto", "deve essere positivo"));
		if(model.getSubdocumentoSpesa() != null) {
			SubdocumentoSpesa ss = model.getSubdocumentoSpesa();
			// Validazione CIG e CUP
			checkCondition(StringUtils.isBlank(ss.getCig()) || CIG_PATTERN.matcher(ss.getCig()).matches(), 
					ErroreCore.FORMATO_NON_VALIDO.getErrore("CIG", "Deve essere composto da dieci caratteri"));
			checkCondition(StringUtils.isBlank(ss.getCup()) || CUP_PATTERN.matcher(ss.getCup()).matches(), 
					ErroreCore.FORMATO_NON_VALIDO.getErrore("CUP", "Deve essere composto da 15 caratteri, il primo e il quarto dei quali devono essere una lettera"));
			// SIAC-5021
			checkNotNullNorInvalidUid(model.getModalitaPagamentoSoggetto(), "Modalita' pagamento soggetto");
			// SIAC-5043
			checkProvvisorioCassa(model.getSubdocumentoSpesa().getProvvisorioCassa(), TipoProvvisorioDiCassa.S);
			// SIAC-5410: controlli SIOPE+
			checkSIOPE(model.getSubdocumentoSpesa());
		} else if(model.getSubdocumentoEntrata() != null) {
			// SIAC-5043
			checkProvvisorioCassa(model.getSubdocumentoEntrata().getProvvisorioCassa(), TipoProvvisorioDiCassa.E);
		}
	}

	/**
	 * Controllo del provvisorio di cassa
	 * @param provvisorioDiCassa il provvisorio
	 * @param tipoProvvisorioDiCassa il tipo del provvisorio
	 */
	private void checkProvvisorioCassa(ProvvisorioDiCassa provvisorioDiCassa, TipoProvvisorioDiCassa tipoProvvisorioDiCassa) {
		final String methodName = "checkProvvisorioCassa";
		if(provvisorioDiCassa == null) {
			// Nessun provvisorio presente: esco
			log.debug(methodName, "Provvisorio di cassa non presente");
			return;
		}
		checkCondition(!(provvisorioDiCassa.getAnno() == null ^ provvisorioDiCassa.getNumero() == null),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("anno e numero provvisorio", "devono essere contemporaneamente valorizzati o non valorizzati"));
		if(provvisorioDiCassa.getAnno() == null) {
			// Anno non presente, non continuo con la ricerca
			log.debug(methodName, "Anno provvisorio non presente: esco");
			return;
		}
		
		RicercaProvvisorioDiCassaPerChiave req = model.creaRequestRicercaProvvisorioDiCassaPerChiave(provvisorioDiCassa, tipoProvvisorioDiCassa);
		RicercaProvvisorioDiCassaPerChiaveResponse res = provvisorioService.ricercaProvvisorioDiCassaPerChiave(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Ho errori: esco subito
			log.info(methodName, createErrorInServiceInvocationString(RicercaProvvisorioDiCassaPerChiave.class, res));
			addErrori(res);
			return;
		}
		if(res.getProvvisorioDiCassa() == null) {
			// Non ho il provvisorio
			log.info(methodName, "Nessun provvisorio corrispondente ai parametri di richiesta [anno: " + provvisorioDiCassa.getAnno() + ", numero: " + provvisorioDiCassa.getNumero()
					+ ", tipo: " + tipoProvvisorioDiCassa + "]");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", tipoProvvisorioDiCassa + "-" + provvisorioDiCassa.getAnno() + "/" + provvisorioDiCassa.getNumero()));
			return;
		}
		// Copio solo l'uid?
		provvisorioDiCassa.setUid(res.getProvvisorioDiCassa().getUid());
	}
	
	/**
	 * Controlli per il SIOPE
	 * @param subdocumentoSpesa il subdocumento
	 */
	private void checkSIOPE(SubdocumentoSpesa subdocumentoSpesa) {
		Impegno impegno = subdocumentoSpesa.getImpegnoOSubImpegno();
		if(impegno == null || impegno.getUid() == 0) {
			return;
		}
		SiopeTipoDebito siopeTipoDebito = impegno.getSiopeTipoDebito();
		// SIAC-5311 SIOPE+
		// Assenza CIG: Si', se il Tipo debito Siope del movimento selezionato (impegno o subimpegno) e' di tipo COMMERCIALE e non e' presente il campo CIG.
		checkCondition(siopeTipoDebito == null
				|| !BilConstants.CODICE_SIOPE_DEBITO_TIPO_COMMERCIALE.getConstant().equals(siopeTipoDebito.getCodice())
				|| (StringUtils.isNotBlank(subdocumentoSpesa.getCig()) ^ (subdocumentoSpesa.getSiopeAssenzaMotivazione() != null && subdocumentoSpesa.getSiopeAssenzaMotivazione().getUid() != 0)),
				new Errore("", "Occorre inserire CIG o Motivo di assenza CIG, ma non entrambi"));
		
	}

	/**
	 * Preparazione per il metodo {@link #aggiornaSubdocumentoElenco()}.
	 */
	public void prepareSpezzaSubdocumentoElenco() {
		//pulisco il model
		model.setElencoDocumentiAllegato(null);
		model.setSubdocumentoSpesa(null);
		model.setSubdocumentoEntrata(null);
		model.setDocumentoSpesa(null);
		model.setDocumentoEntrata(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setNuovoSubdocumentoSpesa(null);
		model.setNuovoSubdocumentoEntrata(null);
	}
	
	/**
	 * Aggiorna il subdocumento dell'elenco dall'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String spezzaSubdocumentoElenco() {
		// Sono in uno solo dei sue casi, spesa o entrata
		if(model.getSubdocumentoSpesa() != null) {
			spezzaSubdocumentoSpesaElenco();
		} else {
			spezzaSubdocumentoEntrataElenco();
		}
		return SUCCESS;
	}

	/**
	 * Aggiorna il subdocumento di spesa per l'elenco.
	 */
	private void spezzaSubdocumentoSpesaElenco() {
		final String methodName = "spezzaSubdocumentoSpesaElenco";

		SpezzaQuotaSpesa req  = model.creaRequestSpezzaQuotaSpesa();
		logServiceRequest(req);
		SpezzaQuotaSpesaResponse res = documentoSpesaService.spezzaQuotaSpesa(req);
		logServiceResponse(res);
		
		//controllo se si siano verificati degli errori
		if (res.hasErrori()) {
			//si sono verificati dgeli errori: esco
			log.info(methodName, createErrorInServiceInvocationString(SpezzaQuotaSpesa.class, res));
			addErrori(res);
			return;
		}
		
		log.debug(methodName, "Quota spezzata correttamente");
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
	}
	
	/**
	 * Aggiorna il subdocumento di entrata per l'elenco.
	 */
	private void spezzaSubdocumentoEntrataElenco() {
		final String methodName = "spezzaSubdocumentoEntrataElenco";
		
		SpezzaQuotaEntrata req  = model.creaRequestSpezzaQuotaEntrata();		
		logServiceRequest(req);
		SpezzaQuotaEntrataResponse res = documentoEntrataService.spezzaQuotaEntrata(req);
		logServiceResponse(res);
		
		//controllo se si siano verificati degli errori
		if (res.hasErrori()) {
			//si sono verificati degli errori, esco
			log.info(methodName, createErrorInServiceInvocationString(SpezzaQuotaEntrata.class, res));
			addErrori(res);
			return;
		}
		
		log.debug(methodName, "Quota spezzata correttamente");
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaSubdocumentoElenco()}.
	 */
	@SuppressWarnings("unchecked")
	public void validateSpezzaSubdocumentoElenco() {
		Subdocumento<?, ?> subdoc = ObjectUtils.firstNonNull(model.getSubdocumentoSpesa(), model.getSubdocumentoEntrata());
		Subdocumento<?, ?> nuovoSubdoc = ObjectUtils.firstNonNull(model.getNuovoSubdocumentoSpesa(), model.getNuovoSubdocumentoEntrata());
		checkNotNull(nuovoSubdoc, "subdocumento", true);
		checkNotNull(subdoc, "subdocumento", true);
		
		List<Subdocumento<?, ?>> subdocs = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO);
		subdoc = ComparatorUtils.searchByUid(subdocs, subdoc);
		checkNotNull(nuovoSubdoc, "subdocumento", true);
		
		checkNotNullNorInvalidUid(model.getElencoDocumentiAllegato(), "elenco");
		nuovoSubdoc.setElencoDocumenti(model.getElencoDocumentiAllegato());
		
		checkNotNull(nuovoSubdoc.getDataScadenza(), "data scadenza");

		checkImporto(subdoc.getImporto(), nuovoSubdoc.getImporto(), false, "importo", true);
		
		if (subdoc instanceof SubdocumentoSpesa) {
			//controlli sul subdocumento spesa
			SubdocumentoSpesa subdocSpesa = (SubdocumentoSpesa) subdoc;
			SubdocumentoSpesa nuovoSubdocSpesa = (SubdocumentoSpesa) nuovoSubdoc;
			
			// Controlli sullo split/reverse
			checkImporto(subdocSpesa.getImportoSplitReverse(), nuovoSubdocSpesa.getImportoSplitReverse(), true, "importo IVA Split", false);
			checkNotNullNorInvalidUid(model.getModalitaPagamentoSoggetto(), "Modalita' pagamento soggetto");
			nuovoSubdocSpesa.setModalitaPagamentoSoggetto(model.getModalitaPagamentoSoggetto());
			
			// Validazione CIG e CUP
			checkCondition(StringUtils.isBlank(nuovoSubdocSpesa.getCig()) || CIG_PATTERN.matcher(nuovoSubdocSpesa.getCig()).matches(),
					ErroreCore.FORMATO_NON_VALIDO.getErrore("CIG", "Deve essere composto da dieci caratteri"));
			checkCondition(StringUtils.isBlank(nuovoSubdocSpesa.getCup()) || CUP_PATTERN.matcher(nuovoSubdocSpesa.getCup()).matches(),
					ErroreCore.FORMATO_NON_VALIDO.getErrore("CUP", "Deve essere composto da 15 caratteri, il primo e il quarto dei quali devono essere una lettera"));
			// Controllo sul provvisorio
			checkProvvisorioCassa(model.getNuovoSubdocumentoSpesa().getProvvisorioCassa(), TipoProvvisorioDiCassa.S);
		} else if(model.getSubdocumentoEntrata() != null) {
			//controlli sul documento entrata.
			checkProvvisorioCassa(model.getNuovoSubdocumentoEntrata().getProvvisorioCassa(), TipoProvvisorioDiCassa.E);
		}
	}
	
	/**
	 * Controlla se gli importi siano correttamente impostati
	 * @param importoOld il vecchio importo
	 * @param importoNew il nuovo importo
	 * @param mayBeNull se l'importo puo' essere null
	 * @param nomeImporto il nome dell'importo
	 */
	private void checkImporto(BigDecimal importoOld, BigDecimal importoNew, boolean mayBeNull, String nomeImporto, boolean strictIneq) {
		if(!mayBeNull) {
			checkNotNull(importoOld, nomeImporto + " originale");
			checkNotNull(importoNew, nomeImporto + " nuovo");
		} else {
			checkCondition(!(importoOld == null ^ importoNew == null),
					ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeImporto, "l'" + nomeImporto + " deve essere valorizzato se e solo se e' valorizzato quello originale"));
		}
		if(importoOld == null || importoNew == null) {
			return;
		}
		
		if (strictIneq) {
			checkCondition(importoNew.compareTo(BigDecimal.ZERO) > 0, ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeImporto, "(deve essere positivo)"));
			checkCondition(importoOld.compareTo(importoNew) > 0, ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeImporto, "(deve essere inferiore all'" + nomeImporto + " corrente)"));
		} else {
			checkCondition(importoNew.compareTo(BigDecimal.ZERO) >= 0, ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeImporto, "(deve essere positivo)"));
			checkCondition(importoOld.compareTo(importoNew) >= 0, ErroreCore.VALORE_NON_CONSENTITO.getErrore(nomeImporto, "(non deve essere superiore all'" + nomeImporto + " corrente)"));
		}
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaSubdocumentoElenco()}.
	 */
	public void prepareEliminaSubdocumentoElenco() {
		model.setElencoDocumentiAllegato(null);
		model.setSubdocumentoSpesa(null);
		model.setSubdocumentoEntrata(null);
	}
	
	/**
	 * Elimina il subdocumento dall'elenco dall'allegato atto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaSubdocumentoElenco() {
		final String methodName = "eliminaSubdocumentoElenco";
		validazioneEliminaSubdocumentoElenco();
		if(hasErrori()) {
			log.debug(methodName, "Errori di validazione");
			return SUCCESS;
		}
		
		EliminaQuotaDaElenco req = model.creaRequestEliminaQuotaDaElenco();
		logServiceRequest(req);
		EliminaQuotaDaElencoResponse res = allegatoAttoService.eliminaQuotaDaElenco(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(EliminaQuotaDaElenco.class, res));
			addErrori(res);
			return SUCCESS;
		}
		log.debug(methodName, "Eliminata quota con uid " + req.getSubdocumento().getUid()
				+ " dall'elenco con uid " + req.getElencoDocumentiAllegato().getUid());
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #eliminaSubdocumentoElenco()}.
	 */
	private void validazioneEliminaSubdocumentoElenco() {
		checkCondition(model.getElencoDocumentiAllegato() != null && model.getElencoDocumentiAllegato().getUid() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco"));
		@SuppressWarnings("unchecked")
		Subdocumento<?, ?> subdoc = ObjectUtils.firstNonNull(model.getSubdocumentoSpesa(), model.getSubdocumentoEntrata());
		checkCondition(subdoc != null && subdoc.getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("subdocumento"));
	}
	
	/**
	 * Preparazione per il metodo {@link #sospendiPagamentoSoggetto()}.
	 */
	public void prepareSospendiPagamentoSoggetto() {
		//pulisco il model
		model.setDatiSoggettoAllegato(null);
		model.setElencoDocumentiAllegato(null);
	}
	
	/**
	 * Sospendi il pagamento al soggetto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String sospendiPagamentoSoggetto() {
		final String methodName = "sospendiPagamentoSoggetto";
		validazioneModificheDatiSoggettoAllegato();
		//controllo se la validazione precedente abbia dato esito positivo
		if(hasErrori()) {
			//non e' stata superata la validazione precedente
			log.debug(methodName, "Errore di validazione");
			return SUCCESS;
		}
		AggiornaDatiSoggettoAllegatoAtto req = model.creaRequestAggiornaDatiSoggettoAllegatoAtto();
		logServiceRequest(req);
		AggiornaDatiSoggettoAllegatoAttoResponse res = allegatoAttoService.aggiornaDatiSoggettoAllegatoAtto(req);
		logServiceResponse(res);
		
		//controllo se si siano verificati degli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori
			log.info(methodName, createErrorInServiceInvocationString(AggiornaDatiSoggettoAllegatoAtto.class, res));
			addErrori(res);
			return SUCCESS;
		}
		log.debug(methodName, "Aggiornamento dei dati sospensione avvenuta con successo. Uid della relazione: " + res.getDatiSoggettoAllegato().getUid());
		
		try {
			caricaListaDatiSoggettoAllegato();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return SUCCESS;
		}
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #riattivaSoggettoSospeso()}.
	 */
	public void prepareRiattivaSoggettoSospeso() {
		//pulisco il model
		model.setDatiSoggettoAllegato(null);
		model.setElencoDocumentiAllegato(null);
	}
	
	/**
	 * Riattiva il soggetto sospeso
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String riattivaSoggettoSospeso() {
		final String methodName = "riattivaSoggettoSospeso";
		try {
			validazioneModificheDatiSoggettoAllegato();
		} catch(ParamValidationException pve) {
			// Errori di validazione
			log.debug(methodName, "Errore riscontrato in validazione: " + pve.getMessage());
		}
		if(hasErrori()) {
			log.debug(methodName, "Errore di validazione");
			return SUCCESS;
		}
		AggiornaDatiSoggettoAllegatoAtto req = model.creaRequestAggiornaDatiSoggettoAllegatoAtto();
		logServiceRequest(req);
		AggiornaDatiSoggettoAllegatoAttoResponse res = allegatoAttoService.aggiornaDatiSoggettoAllegatoAtto(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaDatiSoggettoAllegatoAtto.class, res));
			addErrori(res);
			return SUCCESS;
		}
		log.debug(methodName, "Aggiornamento dei dati sospensione avvenuta con successo. Uid della relazione: " + res.getDatiSoggettoAllegato().getUid());
		// Ricarico i dati dei soggetti dell'allegato atto
		try {
			caricaListaDatiSoggettoAllegato();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return SUCCESS;
		}
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	@Override
	protected void validazioneModificheDatiSoggettoAllegato() {
		super.validazioneModificheDatiSoggettoAllegato();
		checkNotNullNorInvalidUid(model.getElencoDocumentiAllegato(), "elenco");
	}
	
	// Lotto O
	/**
	 * Ricalcolo della lista degli elenchi.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricalcolaListaElenchi() {
		final String methodName = "ricalcolaListaElenchi";
		RicercaElenchiPerAllegatoAtto req = model.creaRequestRicercaElenchiPerAllegatoAtto();
		logServiceRequest(req);
		RicercaElenchiPerAllegatoAttoResponse res = allegatoAttoService.ricercaElenchiPerAllegatoAtto(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.warn(methodName, createErrorInServiceInvocationString(RicercaElenchiPerAllegatoAtto.class, res));
			addErrori(res);
			return INPUT;
		}
		List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = res.getElenchiDocumentiAllegato();
		model.setListaElencoDocumentiAllegato(listaElencoDocumentiAllegato);
		sessionHandler.setParametro(BilSessionParameter.LISTA_ELENCO_DOCUMENTI_ALLEGATO_ALLEGATO_ATTO, listaElencoDocumentiAllegato);
		return SUCCESS;
	}
	
	// SIAC-5172
	/**
	 * Preparazione per il metodo {@link #sospendiTuttoElenco()}
	 */
	public void prepareSospendiTuttoElenco() {
		model.setElencoDocumentiAllegato(null);
		model.setDatiSoggettoAllegato(null);
	}
	/**
	 * Sospensione di tutti i dati del soggetto per l'elenco
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String sospendiTuttoElenco() {
		final String methodName = "sospendiTuttoElenco";
		AggiornaMassivaDatiSoggettoAllegatoAtto req = model.creaRequestAggiornaMassivaDatiSoggettoAllegatoAtto(model.getElencoDocumentiAllegato());
		AggiornaMassivaDatiSoggettoAllegatoAttoResponse res = allegatoAttoService.aggiornaMassivaDatiSoggettoAllegatoAtto(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaMassivaDatiSoggettoAllegatoAtto.class, res));
			addErrori(res);
			return SUCCESS;
		}
		
		log.debug(methodName, "Aggiornamento dei dati sospensione avvenuta con successo");
		
		try {
			caricaListaDatiSoggettoAllegato();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return SUCCESS;
		}
		// Comunico il successo dell'operazione
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #sospendiTuttoElenco()}
	 */
	public void validateSospendiTuttoElenco() {
		validazioneModificheDatiSoggettoAllegato();
	}
	
	/**
	 * Preparazione per il metodo {@link #ricercaDatiSospensioneElenco()}
	 */
	public void prepareRicercaDatiSospensioneElenco() {
		model.setDatiSoggettoAllegato(null);
		model.setElencoDocumentiAllegato(null);
		model.setDatiSoggettoAllegatoDeterminatiUnivocamente(false);
	}
	
	/**
	 * Ricerca dei dati di sospensione per l'elenco documenti allegato
	 * @return i dati di sospensione
	 */
	public String ricercaDatiSospensioneElenco() {
		final String methodName = "ricercaDatiSospensioneAllegato";
		RicercaDatiSospensioneAllegatoAtto req = model.creaRequestRicercaDatiSospensioneAllegatoAtto(model.getElencoDocumentiAllegato());
		RicercaDatiSospensioneAllegatoAttoResponse res = allegatoAttoService.ricercaDatiSospensioneAllegatoAtto(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDatiSospensioneAllegatoAtto.class, res));
			addErrori(res);
			return INPUT;
		}
		
		impostaDatiSoggettoAllegatoDaRicerca(res);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaDatiSospensioneElenco()}
	 */
	public void validateRicercaDatiSospensioneElenco() {
		// Deve esserci l'elenco
		checkNotNullNorInvalidUid(model.getElencoDocumentiAllegato(), "elenco");
	}
}
