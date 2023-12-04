/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per l'aggiornamento del Documento di spesa, sezione Nota Credito.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaNotaCreditoAction extends AggiornaDocumentoSpesaBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9007079625141338845L;
	/** Suffisso per la action */
	private static final String SUFFIX = "_NOTECREDITO";

	/**
	 * Restituisce la lista delle note credito relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaNoteCredito() {
		//esco subito, devo solo avere accesso al model
		return SUCCESS;
	}

	/**
	 * Pulisce i campi relativi all'inserimento di una nuova notaCredito.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserimentoNuovaNotaCredito() {
		// Pulisco il model
		cleanNoteCredito();
		// Popolo i dati di base della nota di credito
		popolaNotaCreditoPerInserimento();
		
		if(model.getAttoAmministrativo() != null) {
			//pulisco i dati per l'atto amministrativo
			model.getAttoAmministrativo().setUid(0);
		}
		
		// Imposto il suffisso della nota di credito
		model.setSuffisso(SUFFIX);
		return SUCCESS;
	}
	
	/**
	 * Popola i campi relativi all'aggiornamento della notaCredito.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornamentoNuovaNotaCredito() {
		model.setSuffisso(SUFFIX);
		
		// 1. Mi faccio passare dal chiamante l'uid e faccio la ricerca di dettaglio
		// 2. Imposto nel model
		RicercaDettaglioDocumentoSpesa request = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidNotaCredito());
		logServiceRequest(request);
		RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Ottengo la nota di credito dalla response e imposto i dati nel model
		DocumentoSpesa notaCredito = response.getDocumento();
		model.setNotaCredito(notaCredito);
		impostaNotaCreditoDaAggiornare(model.getNotaCredito());
		
		if(model.getAttoAmministrativo() != null) {
			//pulisco l'uid del provvedimento
			model.getAttoAmministrativo().setUid(0);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Inserisce la notaCredito fornita dall'utente in quelle associate al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimentoNuovaNotaCredito() {
		final String methodName = "inserimentoNuovaNotaCredito";
		// Validazione dei dati
		validazioneInserimentoNuovaNotaCredito();
		//validazione degli importi
		validaImportiInserimento();
		// Effettuo anche la validazione del provvedimento.
		
		
		if(hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		//ora posso chiamare il servizio di inserimento
		InserisceNotaCreditoSpesa request = model.creaRequestInserisceNotaCreditoSpesa();
		logServiceRequest(request);
		
		InserisceNotaCreditoSpesaResponse response = documentoSpesaService.inserisceNotaCreditoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceNotaCreditoSpesa.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		// Ricalcolo i documenti collegati
		ricalcolaDocumentiCollegati();
		
		for(DocumentoSpesa doc: response.getDocumentoSpesa().getListaDocumentiSpesaPadre()){
			//ciclo sui documenti spesa padre per impostare lo stato
			model.getDocumento().setStatoOperativoDocumento(doc.getStatoOperativoDocumento());
			model.getDocumento().setDataInizioValiditaStato(doc.getDataInizioValiditaStato());
			break;
		}
		
		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Annulla la notaCredito.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaNotaCredito() {
		final String methodName = "annullaNotaCredito";
		// Validazione dei dati
		validaAnnullaNotaCredito();
	
		if(hasErrori()) {
			//si sono verificati degli errori, non posso chiamare il servizio. esco
			log.info(methodName, "Errore nella validazione del metodo");
			return SUCCESS;
		}
		
		// Ottengo la nota di credito da eliminare
		ElementoDocumento notaCredito = model.getListaDocumentoSpesa().get(model.getRigaDaEliminare().intValue());
		
		//chiamo il servizio di aggiornamento
		AnnullaNotaCreditoSpesa request = model.creaRequestAnnullaNotaCredito(notaCredito.getUid());
		logServiceRequest(request);
		AnnullaNotaCreditoSpesaResponse response = documentoSpesaService.annullaNotaCreditoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AnnullaNotaCreditoSpesa.class, response));
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "NotaCredito annullata");
		
		//ricalcolo i dati che potrebbero essere modificati
		ricalcolaDocumentiCollegati();
		
		for(DocumentoSpesa doc: response.getDocumentoSpesa().getListaDocumentiSpesaPadre()){
			//imposto lo stato e la sua data inizio validita' a partire dai documento padre
			model.getDocumento().setStatoOperativoDocumento(doc.getStatoOperativoDocumento());
			model.getDocumento().setDataInizioValiditaStato(doc.getDataInizioValiditaStato());
			break;
		}
		
		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna la notaCredito.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamentoNotaCredito() {
		final String methodName = "aggiornamentoNotaCredito";
		
		// Stessa validazione del caso dell'inserimento
		validazioneInserimentoNuovaNotaCredito();
		validaImportiAggiornamento();
		validazioneDeduzioniAggiornamento();
		
		if(hasErrori()) {
			//si sono verificati degli errori, non posso chiamare il servizio. esco
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		
		// Aggiorno la nota di credito
		AggiornaNotaCreditoSpesa request = model.creaRequestAggiornaNotaCreditoSpesa();
		logServiceRequest(request);
		AggiornaNotaCreditoSpesaResponse response = documentoSpesaService.aggiornaNotaCreditoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaNotaCreditoSpesa.class, response));
			addErrori(response);
			return SUCCESS;
		}
		// Ricalcolo i documenti collegati
		ricalcolaDocumentiCollegati();
		
		for(DocumentoSpesa doc: response.getDocumentoSpesa().getListaDocumentiSpesaPadre()){
			//imposto lo stato e la sua data inizio validita' a partire dai documento padre
			model.getDocumento().setStatoOperativoDocumento(doc.getStatoOperativoDocumento());
			model.getDocumento().setDataInizioValiditaStato(doc.getDataInizioValiditaStato());
			break;
		}
		
		// Terminato: fornisco il messaggio di successo	
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	private void validaImportiInserimento() {
		if(model.getImportoDaDedurreSuFattura() == null){
			//non ho nessun importo da dedurre, esco
			return;
		}
//		NOTA CREDITO <= TOTALE DA INCASSARE QUOTE NON COLLEGATE A ORDINATIVO
		checkCondition( model.getImportoDaDedurreSuFattura().subtract(model.getTotaleDaPagareQuoteSenzaOrdinativo()).signum()<=0, ErroreFin.TOTALE_NOTA_CREDITO_ERRATO.getErrore());
	}
	
	
	private void validaImportiAggiornamento() {
		final String methodName = "validaImportiAggiornamento";
		
		log.debug(methodName, "inizio metodo validaImportiAggiornamento");
		
		ElementoDocumento notaOld = new ElementoDocumento();
		log.debug(methodName, "uidNotaCredito: " + model.getUidNotaCredito());
		notaOld.setUid(model.getUidNotaCredito());
		int index = ComparatorUtils.getIndexByUid(model.getListaDocumentoSpesa(), notaOld);
		log.debug(methodName,"index: " +  index);
		log.debug(methodName, "lista documenti spesa is null? " + model.getListaDocumentoSpesa() == null);
		if(model.getListaDocumentoSpesa() != null){
			log.debug(methodName, "lista documenti spesa is empty? " + model.getListaDocumentoSpesa().isEmpty());
		}
		//ottengo i dati non modificati
		notaOld = model.getListaDocumentoSpesa().get(index);
		
		BigDecimal nuovoTotaleNote = model.getTotaleImportoDaDedurreSuFattura().add(model.getImportoDaDedurreSuFattura()).subtract(notaOld.getImportoDaDedurreSuFattura());
		log.debug(methodName, "nuovoTotaleNote: " + nuovoTotaleNote);
		
//		NOTA CREDITO <= TOTALE DA INCASSARE QUOTE NON COLLEGATE A ORDINATIVO
		checkCondition(model.getImportoDaDedurreSuFattura().subtract(model.getTotaleDaPagareQuoteSenzaOrdinativo()).signum()<=0, ErroreFin.TOTALE_NOTA_CREDITO_ERRATO.getErrore());
		
//		TOTALE NOTE CREDITO >= TOTALE IMPORTO DA DEDURRE
		checkCondition(nuovoTotaleNote.subtract(model.getTotaleImportoDaDedurre()).signum()>=0, ErroreCore.FORMATO_NON_VALIDO.getErrore("totale note credito ", 
				": il totale delle note di credito deve essere maggiore del totale degli importi da dedurre"));
		
	}
	
	
	/**
	 * Ricalcola i documenti collegati e li imposta nel model
	 */
	private void ricalcolaDocumentiCollegati() {
		//chiamo il servizio per ottenere i dati dei documenti spesa collegati prensenti su db
		RicercaDocumentiCollegatiByDocumentoSpesa requestCollegati = model.creaRequestRicercaDocumentiCollegatiByDocumentoSpesa(model.getUidDocumentoDaAggiornare());
		logServiceRequest(requestCollegati);
		RicercaDocumentiCollegatiByDocumentoSpesaResponse responseCollegati = documentoSpesaService.ricercaDocumentiCollegatiByDocumentoSpesa(requestCollegati);
		logServiceResponse(responseCollegati);
		
		// Ottengo i wrapper dei documenti
		List<ElementoDocumento> listaWrappers = ElementoDocumentoFactory.getInstances(responseCollegati.getDocumentiSpesaFiglio(), model.getDocumento().getSoggetto());
		
		model.setListaDocumentoSpesa(listaWrappers);
		
		// Imposto i documenti figlio nel documento di cui l'aggiornamento
		DocumentoSpesa documentoIns = model.getDocumento();
		documentoIns.setListaDocumentiSpesaFiglio(responseCollegati.getDocumentiSpesaFiglio());
		
		// Aggiorno i dati
		aggiornaDatiPerNote();
	}
	
	/**
	 * Applica gli importi da dedurre su tutte le quote.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String applicaImportiDaDedurre() {
		final String methodName = "applicaImportiDaDedurre";
		
		// Validazione dei dati
		validazioneImportiDaDedurre();
		if(hasErrori()) {
			//si sono verificati errori, non posso continuare
			log.debug(methodName, "Errori nella validazione degli importi da dedurre");
			return SUCCESS;
		}
		
		// Aggiorno gli importi
		AggiornaImportiQuoteDocumentoSpesa request = model.creaRequestAggiornaImportiQuoteDocumentoSpesa();
		logServiceRequest(request);
		AggiornaImportiQuoteDocumentoSpesaResponse response = documentoSpesaService.aggiornaImportoDaDedurreQuoteDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaImportiQuoteDocumentoSpesa.class, response));
			addErrori(response);
			return SUCCESS;
		}
		//aggiorno i dati necessari alle note
		aggiornaDatiPerNote();
		
		//imposto i dati relativi allo stato
		model.getDocumento().setStatoOperativoDocumento(response.getDocumentiReferenziatiDaiSubdocumenti().get(0).getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(response.getDocumentiReferenziatiDaiSubdocumenti().get(0).getDataInizioValiditaStato());
		
		// Terminato: fornisco il messaggio di successo	
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Effettua una validazione sugli importi da dedurre sulle quote.
	 */
	private void validazioneImportiDaDedurre() {
		BigDecimal importiDaDedurreTotali = BigDecimal.ZERO;
		//prendo tutti i subdcoumenti
		List<SubdocumentoSpesa> list = model.getListaSubdocumentoSpesa();
		for(int i = 0; i < list.size(); i++) {
			SubdocumentoSpesa ss = list.get(i);
			if(ss.getImportoDaDedurre() == null){
				// Se non ho l'importo da dedurre, allora lo considero come zero
				ss.setImportoDaDedurre(BigDecimal.ZERO);
			}
			// Aggiungo l'importo da dedurre al totale
			importiDaDedurreTotali = importiDaDedurreTotali.add(ss.getImportoDaDedurre());
			// Controllo che l'importo del documento non sia inferiore all'importo da dedurre
			checkCondition(ss.getImporto().subtract(ss.getImportoDaDedurre()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre",
					"importo da dedurre non deve essere maggiore dell'importo per la quota numero " + ss.getNumero()));
			checkCondition(importiDaDedurreTotali.compareTo(BigDecimal.ZERO) >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre", "perche' e' negativo"));
		}
		// Controllo che il totale delle note di credito non sia inferiore al totale degli importi da dedurre
		checkCondition(model.getTotaleImportoDaDedurreSuFattura().subtract(importiDaDedurreTotali).signum() >= 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre", "la somma degli importi da dedurre non deve superare l'importo delle note collegate"));
	}
	
	/**
	 * Valida l'inserimento di una nuova nota credito.
	 */
	private void validazioneInserimentoNuovaNotaCredito() {
		String methodName = "validazioneInserimentoNuovaNotaCredito";
		DocumentoSpesa nota = model.getNotaCredito();
		DocumentoSpesa doc = model.getDocumento();
		
		// Validazione formale
		checkNotNullNorInvalidUid(nota.getTipoDocumento(), "Tipo");
		checkNotNull(nota.getAnno(), "Anno");
		checkNotNullNorEmpty(nota.getNumero(), "Numero");
		checkNotNull(nota.getDataEmissione(), "Data");
		checkCondition(StringUtils.isNotBlank(nota.getDescrizione()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione"));
		checkNotNull(nota.getImporto(), "Importo");
		checkNotNull(model.getImportoDaDedurreSuFattura(), "Importo da dedurre su fattura");
		
		// Validazione logica
		log.debug(methodName, "Validazione logica campi");
		// L'importo della nota deve essere positivo
		checkCondition(nota.getImporto() != null && nota.getImporto().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
		checkCondition(model.getImportoDaDedurreSuFattura() != null && model.getImportoDaDedurreSuFattura().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo da dedurre su fattura", ": deve essere maggiore di zero"));
		
		if(model.getImportoDaDedurreSuFattura() == null || nota.getImporto() == null){
			//non ho importo da dedurre oppure la nota non ha l'importo. Esco
			return;
		}
		
		// L'importo da dedurre su fattura non deve superare l'importo della nota
		checkCondition(nota.getImporto().subtract(model.getImportoDaDedurreSuFattura()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo da dedurre su fattura", ": non deve essere maggiore dell'importo della nota di credito"));
		
		// L'importo della nota non deve superare l'importo del documento
		checkCondition(doc.getImporto().subtract(model.getImportoDaDedurreSuFattura()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo da dedurre su fattura", ": non deve essere maggiore dell'importo del documento"));
		
		if(nota.getAnno() != null) {
			// Se l'anno e' valorizzato, deve essere coerente con l'anno di esercizio
			checkCondition(model.getAnnoEsercizioInt().compareTo(nota.getAnno()) >= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
			log.debug(methodName, "ho passato il terzo check");
			log.debug(methodName, "anno nota: " + nota.getAnno());
			if(nota.getDataEmissione() != null) {
				Integer annoEmissione = Integer.decode(FormatUtils.formatDateYear(nota.getDataEmissione()));
				// La data di emissione della nota deve essere coerente con l'anno di esercizio
				checkCondition(nota.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
				log.debug(methodName, "ho passato il quarto check");
				log.debug(methodName, "anno emissione: " + annoEmissione);
			}
		}
		//verifica provvedimento
		if(checkProvvedimentoEsistente()) {
			//controllo il provvedimento
			validaProvvedimentoPerInserimentoNuovaQuota(BilConstants.GESTISCI_DOCUMENTO_SPESA_DECENTRATO.getConstant());
		}
	}
	
	/**
	 * Valida l'annullamento di una nota credito.
	 */
	private void validaAnnullaNotaCredito() {
		// Controllo se sia stata fornita la riga da eliminare
		checkNotNull(model.getRigaDaEliminare(), "Nota credito da eliminare");
		// Valido le deduzioni
		validazioneDeduzioniAnnullamento();
	}
	
	/**
	 * Valida le deduzioni per l'aggiornamento.
	 */
	private void validazioneDeduzioniAggiornamento() {
		BigDecimal totaleDeduzioni = calcolaTotaleDeduzioni();
		BigDecimal importoVecchioNoteCredito =  model.getTotaleImportoDaDedurreSuFattura();
		
		// Cerco la vecchia nota per ottenerne gli importi
		ElementoDocumento notaOld = new ElementoDocumento();
		notaOld.setUid(model.getUidNotaCredito());
		int index = ComparatorUtils.getIndexByUid(model.getListaDocumentoSpesa(), notaOld);
		notaOld = model.getListaDocumentoSpesa().get(index);
		
		// Nell'importo 'vecchio' ho ancora il vecchioValore della nota. Devo aggiungere il nuovo valore
		BigDecimal importoNuovoNoteCredito = importoVecchioNoteCredito.subtract(notaOld.getImportoDaDedurreSuFattura()).add(model.getImportoDaDedurreSuFattura());
		
		// Il nuovo importo delle note non deve essere inferiore al totale delle deduzioni
		checkCondition(importoNuovoNoteCredito.subtract(totaleDeduzioni).signum() >= 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre", "la somma degli importi da dedurre non deve superare l'importo delle note collegate"));
	}
	
	/**
	 * Valida le deduzioni per l'annullamento.
	 */
	private void validazioneDeduzioniAnnullamento() {
		BigDecimal totaleDeduzioni = calcolaTotaleDeduzioni();
		BigDecimal importoVecchioNoteCredito = model.getTotaleImportoDaDedurreSuFattura();
		// Ottengo la vecchia nota di credito
		ElementoDocumento notaCredito = model.getListaDocumentoSpesa().get(model.getRigaDaEliminare().intValue());
		BigDecimal importoNuovoNoteCredito = importoVecchioNoteCredito.subtract(notaCredito.getImportoDaDedurreSuFattura());
		
		// Il nuovo importo delle note non deve essere inferiore al totale delle deduzioni
		checkCondition(importoNuovoNoteCredito.subtract(totaleDeduzioni).signum() >= 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre", "la somma degli importi da dedurre non deve superare l'importo delle note collegate"));
	}
	
	/**
	 * Calcola il totale delle deduzioni sulle quote.
	 * 
	 * @return il totale delle deduzioni
	 */
	private BigDecimal calcolaTotaleDeduzioni() {
		BigDecimal totale = BigDecimal.ZERO;
		//ottengo tutti i subdocumenti
		List<SubdocumentoSpesa> lista = model.getListaSubdocumentoSpesa();
		
		for(SubdocumentoSpesa ss : lista) {
			if(ss.getImportoDaDedurre() != null){
				totale = totale.add(ss.getImportoDaDedurre());
				// Controlo formale: l'importo del subdocumento non puo' essere inferiore all'importo da dedurre
				checkCondition(ss.getImporto().subtract(ss.getImportoDaDedurre()).signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre",
						"importo da dedurre non deve essere maggiore dell'importo per la quota numero " + ss.getNumero()));
			}
		}
		return totale;
	}


	/**
	 * Redirezione al metodo di consultazione quote a partire dalla videata delle note di credito.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	
	public String ottieniListaQuoteNotaCredito(){
		// Mantengo la lista che ho in sessione
		model.impostaTotaliNoteQuote();
		return SUCCESS;
	}

	/**
	 * Aggiorna i dati relativi alle note di credito.
	 */
	private void aggiornaDatiPerNote() {
		//imposto i dati delle note di credito
		model.impostaTotaliNoteQuote();
		// calcolo gli importi
		model.calcoloImporti();
	}
	
	/**
	 * Pulisce il model per l'inserimento di una nuova nota credito.
	 */
	private void cleanNoteCredito() {
		//pulisco i dati nel model
		model.setNotaCredito(new DocumentoSpesa());
		model.setAttoAmministrativo(null);
		model.setTipoAtto(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setImportoDaAttribuire(null);
	}
	
	/**
	 * Imposta i dati della nota credito per l'aggiornamento.
	 * 
	 * @param nota la nota credito tramite cui popolare i dati
	 */
	private void impostaNotaCreditoDaAggiornare(DocumentoSpesa nota) {
		// Popolo i dati presenti nei classificatori esterni
		
		AttoAmministrativo attoAmministrativo = nota.getListaSubdocumenti().get(0).getAttoAmministrativo();
		model.setAttoAmministrativo(attoAmministrativo);
		if(attoAmministrativo != null) {
			//ho un provvedimento: ne imposto i dati
			model.setTipoAtto(attoAmministrativo.getTipoAtto());
			model.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		}
		//la nota credito potrebbe essere collegata a piu' documenti, devo individuare l'importo relativo al padre su cui sto lavorando adesse
		for(DocumentoSpesa docPadre : nota.getListaDocumentiSpesaPadre()){
			if(docPadre.getUid() == model.getDocumento().getUid()){
				//setto l'importo da dedurre sulla fattura
				model.setImportoDaDedurreSuFattura(docPadre.getImportoDaDedurreSuFattura());
			}
		}
	}
	
	/**
	 * Popola il documentoSpesa nota per  l'inserimento della nota di credito.
	 */
	private void popolaNotaCreditoPerInserimento() {
		//creo un tipo documento adeguato
		TipoDocumento tipo = new TipoDocumento();
		tipo.setCodiceGruppo(BilConstants.CODICE_NOTE_CREDITO.toString());
		tipo.setCodice(BilConstants.CODICE_NOTE_CREDITO.toString());
		//setto il tipo documento creato
		DocumentoSpesa notaCredito = model.getNotaCredito();
		notaCredito.setTipoDocumento(tipo);
		
		// creo il collegamento con il documento
		notaCredito.addDocumentoSpesaPadre(model.getDocumento());
		notaCredito.setSoggetto(model.getDocumento().getSoggetto());
		notaCredito.setEnte(model.getEnte());
		// la nota credito nasce in stato valido (rif. analisi 2.5.2 Servizio Inserisce Documento Spesa per le Note credito)
		notaCredito.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		
		//creo i subdocumenti 
		List<SubdocumentoSpesa> listaSubdocumenti = new ArrayList<SubdocumentoSpesa>();
		SubdocumentoSpesa s = new SubdocumentoSpesa();
		s.setAttoAmministrativo(model.getAttoAmministrativo());
		s.setEnte(model.getEnte());
		// la lista avrà sempre un solo elemento
		listaSubdocumenti.add(s);
		notaCredito.setListaSubdocumenti(listaSubdocumenti);
	}
	
	
	/**
	 * Collego la notadi creditoscelta con ildocumento padre
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String collegaNotaCreditoEsistente() {
		String methodName = "collegaNotaCreditoEsistente";

		Integer uidDaCollegare = model.getUidDocumentoDaCollegare();
		//aggiorno la relazione
		AggiornaRelazioneDocumenti request = model.creaRequestAggiornaRelazioneDocumentiSpesa(model.getUidDocumentoDaAggiornare(),uidDaCollegare);
		logServiceRequest(request);
		AggiornaRelazioneDocumentiResponse response = documentoService.collegaDocumenti(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaRelazioneDocumenti.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		// SIAC-4433
		model.getDocumento().setStatoOperativoDocumento(response.getDocPadre().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(response.getDocPadre().getDataInizioValiditaStato());
		
		//aggiorno i dati delle note necessari
		aggiornaDatiPerNote();

		// Terminato: fornisco il messaggio di successo	
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Collego la notadi creditoscelta con ildocumento padre
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String scollegaNotaCreditoEsistente() {
		String methodName = "scollegaNotaCreditoEsistente";

		Integer uidDaCollegare = model.getUidDocumentoDaScollegare();
		//aggiorno la relazione per scollegare la nota di credito
		AggiornaRelazioneDocumenti request = model.creaRequestAggiornaRelazioneDocumentiSpesa(model.getUidDocumentoDaAggiornare(),uidDaCollegare);
		logServiceRequest(request);
		AggiornaRelazioneDocumentiResponse response = documentoService.scollegaDocumenti(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaRelazioneDocumenti.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		// SIAC-4433
		model.getDocumento().setStatoOperativoDocumento(response.getDocPadre().getStatoOperativoDocumento());
		model.getDocumento().setDataInizioValiditaStato(response.getDocPadre().getDataInizioValiditaStato());
		
		//aggiorno i dati per le note
		aggiornaDatiPerNote();

		// Terminato: fornisco il messaggio di successo	
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Ricalcola la lista dei documenti collegati per aggiornala
	 * 
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricalcolaListaDocumentiCollegati () {
		String methodName = "ricalcolaListaDocumentiCollegati ";
		//ottengo da servizio i documenti collegati
		RicercaDocumentiCollegatiByDocumentoSpesa requestCollegati = model.creaRequestRicercaDocumentiCollegatiByDocumentoSpesa(model.getUidDocumentoDaAggiornare());
		logServiceRequest(requestCollegati);
		RicercaDocumentiCollegatiByDocumentoSpesaResponse responseCollegati = documentoSpesaService.ricercaDocumentiCollegatiByDocumentoSpesa(requestCollegati);
		logServiceResponse(responseCollegati);
		
		if(responseCollegati.hasErrori()) {
			//si sono verificati errori, non posso continuare
			log.info(methodName, createErrorInServiceInvocationString(RicercaDocumentiCollegatiByDocumentoSpesa.class, responseCollegati));
			addErrori(responseCollegati);
			return SUCCESS;
		}
		// Ottengo i wrapper dei documenti
		List<ElementoDocumento> listaWrappers = ElementoDocumentoFactory.getInstances(responseCollegati.getDocumentiSpesaFiglio(), model.getDocumento().getSoggetto());
		
		model.setListaDocumentoSpesa(listaWrappers);
		
		// Imposto i documenti figlio nel documento di cui l'aggiornamento
		DocumentoSpesa documentoIns = model.getDocumento();
		documentoIns.setListaDocumentiSpesaFiglio(responseCollegati.getDocumentiSpesaFiglio());
		
		// Aggiorno i dati
		aggiornaDatiPerNote();
		// Terminato: fornisco il messaggio di successo	
		impostaInformazioneSuccesso();
		return SUCCESS;
		
	}

}
