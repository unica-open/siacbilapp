/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumento;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per l'aggiornamento del Documento di entrata, sezione Nota Credito.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataNotaCreditoAction extends AggiornaDocumentoEntrataBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	/** Suffisso per la action */
	private static final String SUFFIX = "_NOTECREDITO";
	
	/**
	 * Restituisce la lista delle note credito relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaNoteCredito() {
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
		// Popolo i dati iniziali
		model.popolaNotaCreditoPerInserimento();

		// Imposto il suffisso
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

		// 1. Mi faccio passare dal chiamante l'uid e faccio la ricerca di
		// dettaglio
		// 2. Imposto nel model

		RicercaDettaglioDocumentoEntrata request = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidNotaCredito());
		logServiceRequest(request);
		RicercaDettaglioDocumentoEntrataResponse response = documentoEntrataService.ricercaDettaglioDocumentoEntrata(request);
		logServiceResponse(response);

		// Ottengo il documento
		DocumentoEntrata notaCredito = response.getDocumento();
		model.setNotaCredito(notaCredito);
		model.impostaNotaCreditoDaAggiornare(model.getNotaCredito());

		return SUCCESS;
	}

	/**
	 * Inserisce la notaCredito fornita dall'utente in quelle associate al
	 * documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimentoNuovaNotaCredito() {
		final String methodName = "inserimentoNuovaNotaCredito";
		validazioneInserimentoNuovaNotaCredito();
		validaImportiInserimento();
		// Effettuo anche la validazione del provvedimento

		if (hasErrori()) {
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}
		InserisceNotaCreditoEntrata request = model.creaRequestInserisceNotaCreditoEntrata();
		logServiceRequest(request);

		InserisceNotaCreditoEntrataResponse response = documentoEntrataService.inserisceNotaCreditoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(InserisceNotaCreditoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}

		DocumentoEntrata documento = response.getDocumentoEntrata();
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documento.getUid());
		
		// Ricalcolo i documenti collegati
		ricalcolaDocumentiCollegati();
		
		for(DocumentoEntrata doc: response.getDocumentoEntrata().getListaDocumentiEntrataPadre()){
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
		validaAnnullaNotaCredito();

		if (hasErrori()) {
			log.info(methodName, "Errore nella validazione del metodo");
			return SUCCESS;
		}

		ElementoDocumento notaCredito = model.getListaDocumentoEntrata().get(model.getRigaDaEliminare().intValue());

		AnnullaNotaCreditoEntrata request = model.creaRequestAnnullaNotaCredito(notaCredito.getUid());
		logServiceRequest(request);
		AnnullaNotaCreditoEntrataResponse response = documentoEntrataService.annullaNotaCreditoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(AnnullaNotaCreditoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "NotaCredito annullata");

		DocumentoEntrata documentoEntrata = response.getDocumentoEntrata();
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - NotaCredito annullata uid: " + documentoEntrata.getUid());
		
		// Ricalcolo i documenti collegati
		ricalcolaDocumentiCollegati();
		
		for(DocumentoEntrata doc: response.getDocumentoEntrata().getListaDocumentiEntrataPadre()){
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

		if (hasErrori()) {
			log.info(methodName, "Errori di validazione presenti");
			return SUCCESS;
		}

		AggiornaNotaCreditoEntrata request = model.creaRequestAggiornaNotaCreditoEntrata();
		logServiceRequest(request);
		AggiornaNotaCreditoEntrataResponse response = documentoEntrataService.aggiornaNotaCreditoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(AggiornaNotaCreditoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}

		// Ricalcolo i documenti collegati
		ricalcolaDocumentiCollegati();
		
		for(DocumentoEntrata doc: response.getDocumentoEntrata().getListaDocumentiEntrataPadre()){
			model.getDocumento().setStatoOperativoDocumento(doc.getStatoOperativoDocumento());
			model.getDocumento().setDataInizioValiditaStato(doc.getDataInizioValiditaStato());
			break;
		}

		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	
	private void validaImportiInserimento() {
//		NOTA CREDITO <= TOTALE DA INCASSARE QUOTE NON COLLEGATE A ORDINATIVO
		if(model.getImportoDaDedurreSuFattura() == null){
			return;
		}
		checkCondition( model.getImportoDaDedurreSuFattura().subtract(model.getTotaleDaPagareQuoteSenzaOrdinativo()).signum()<=0, ErroreFin.TOTALE_NOTA_CREDITO_ERRATO.getErrore());
	}
	
	
	private void validaImportiAggiornamento() {
		final String methodName = "validaImportiAggiornamento";
		
		ElementoDocumento notaOld = new ElementoDocumento();
		notaOld.setUid(model.getUidNotaCredito());
		int index = ComparatorUtils.getIndexByUid(model.getListaDocumentoEntrata(), notaOld);
		notaOld = model.getListaDocumentoEntrata().get(index);
		
		BigDecimal nuovoTotaleNote = model.getTotaleImportoDaDedurreSuFattura().add(model.getImportoDaDedurreSuFattura()).subtract(notaOld.getImportoDaDedurreSuFattura());
		log.debug(methodName, "nuovoTotaleNote: " + nuovoTotaleNote);
		
		log.debug(methodName, "da dedurre su fattura: " + model.getImportoDaDedurreSuFattura());
		log.debug(methodName, "da pagare senza ordinativo: " + model.getTotaleDaPagareQuoteSenzaOrdinativo());
		
//		NOTA CREDITO <= TOTALE DA INCASSARE QUOTE NON COLLEGATE A ORDINATIVO
		//TODO devo fare il controllo sulla differenza!!!
//		BigDecimal diffImporto = model.getImportoDaDedurreSuFattura().subtract(notaOld.getImportoDaDedurreSuFattura());
//		checkCondition( diffImporto.signum()<=0, ErroreFin.TOTALE_NOTA_CREDITO_ERRATO.getErrore());
		
//		TOTALE NOTE CREDITO >= TOTALE IMPORTO DA DEDURRE
		checkCondition(nuovoTotaleNote.subtract(model.getTotaleImportoDaDedurre()).signum()>=0, ErroreCore.FORMATO_NON_VALIDO.getErrore("totale note credito ", 
				": il totale delle note di credito deve essere maggiore del totale degli importi da dedurre"));

		
}


	/**
	 * Ricalcola i documenti collegati e li imposta nel model
	 */
	private void ricalcolaDocumentiCollegati() {
		// Ottengo i documenti collegati
		RicercaDocumentiCollegatiByDocumentoEntrata requestCollegati =
				model.creaRequestRicercaDocumentiCollegatiByDocumentoEntrata(model.getUidDocumentoDaAggiornare());
		logServiceRequest(requestCollegati);
		RicercaDocumentiCollegatiByDocumentoEntrataResponse responseCollegati =
				documentoEntrataService.ricercaDocumentiCollegatiByDocumentoEntrata(requestCollegati);
		logServiceResponse(responseCollegati);

		// Wrappo gli elementi
		List<ElementoDocumento> listaWrappers = ElementoDocumentoFactory.getInstances(responseCollegati.getDocumentiEntrataFiglio(), model.getDocumento()
				.getSoggetto(), model.getDocumento().getStatoOperativoDocumento());

		model.setListaDocumentoEntrata(listaWrappers);

		// Reimposto i documenti di entrata
		DocumentoEntrata documentoIns = model.getDocumento();
		documentoIns.setListaDocumentiEntrataFiglio(responseCollegati.getDocumentiEntrataFiglio());
		model.setDocumento(documentoIns);

		// Aggiorna i dati
		aggiornaDatiPerNote();
	}

	/**
	 * Applica gli importi da dedurre su tutte le quote.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String applicaImportiDaDedurre() {
		final String methodName = "applicaImportiDaDedurre";

		validazioneImportiDaDedurre();
		if (hasErrori()) {
			log.debug(methodName, "Errori nella validazione degli importi da dedurre");
			return SUCCESS;
		}

		AggiornaImportiQuoteDocumentoEntrata request = model.creaRequestAggiornaImportiQuoteDocumentoEntrata();
		logServiceRequest(request);
		AggiornaImportiQuoteDocumentoEntrataResponse response = documentoEntrataService.aggiornaImportoDaDedurreQuoteDocumentoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Errore di validazione
			log.info(methodName, createErrorInServiceInvocationString(AggiornaImportiQuoteDocumentoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}

		// Aggiorna i dati per le note
		aggiornaDatiPerNote();

		// Terminato: fornisco il messaggio di successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Effettua una validazione sugli importi da dedurre sulle quote.
	 */
	private void validazioneImportiDaDedurre() {
//		BigDecimal importiDaDedurreTotali = calcolaTotaleDeduzioni();
//		//TOTALE NOTE CREDITO >= TOTALE IMPORTO DA DEDURRE
//		log.debug("validazioneImportiDaDedurre", "totale note credito: " + model.getTotaleNoteCredito() + "; importi da dedurre totali: "
//				+ importiDaDedurreTotali + "; A-B: " + model.getTotaleNoteCredito().subtract(importiDaDedurreTotali));
//		checkCondition(model.getTotaleNoteCredito().subtract(importiDaDedurreTotali).signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore(
//				"importo da dedurre", ": la somma degli importi da dedurre non deve superare l'importo delle note collegate"));
		
		BigDecimal importiDaDedurreTotali = BigDecimal.ZERO;
		List<SubdocumentoEntrata> list = model.getListaSubdocumentoEntrata();
		for(int i = 0; i < list.size(); i++) {
			SubdocumentoEntrata se = list.get(i);
			if(se.getImportoDaDedurre() == null){
				// Se non ho l'importo da dedurre, allora lo considero come zero
				se.setImportoDaDedurre(BigDecimal.ZERO);
			}
			// Aggiungo l'importo da dedurre al totale
			importiDaDedurreTotali = importiDaDedurreTotali.add(se.getImportoDaDedurre());
			// Controllo che l'importo del documento non sia inferiore all'importo da dedurre
			checkCondition(se.getImporto().subtract(se.getImportoDaDedurre()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre",
					"importo da dedurre non deve essere maggiore dell'importo per la quota numero " + se.getNumero()));
			
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
		DocumentoEntrata nota = model.getNotaCredito();
		DocumentoEntrata doc = model.getDocumento();

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
		// L'importo deve essere positivo
		checkCondition(nota.getImporto() != null && nota.getImporto().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
		checkCondition(model.getImportoDaDedurreSuFattura() != null && model.getImportoDaDedurreSuFattura().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo da dedurre su fattura", ": deve essere maggiore di zero"));
		
		if(model.getImportoDaDedurreSuFattura() == null || nota.getImporto() == null){
			return;
		}
		// L'importo da dedurre su fattura non deve superare l'importo della nota
		checkCondition(nota.getImporto().subtract(model.getImportoDaDedurreSuFattura()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo da dedurre su fattura", ": non deve essere maggiore dell'importo della nota di credito"));
		
		// L'importo della nota non deve superare l'importo del documento
		checkCondition(doc.getImporto().subtract(model.getImportoDaDedurreSuFattura()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo da dedurre su fattura", ": non deve essere maggiore dell'importo del documento"));
		
		if (nota.getAnno() != null) {
			// L'anno della nota deve essere coerente con l'anno di esercizio
			checkCondition(model.getAnnoEsercizioInt().compareTo(nota.getAnno()) >= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
			if (nota.getDataEmissione() != null) {
				// La data di emissione deve essere coerente con l'anno di esercizio
				Integer annoEmissione = Integer.decode(FormatUtils.formatDateYear(nota.getDataEmissione()));
				checkCondition(nota.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
			}
		}

		// verifica provvedimento
		if (checkProvvedimentoEsistente()) {
			validaProvvedimentoPerInserimentoNuovaQuota(BilConstants.GESTISCI_DOCUMENTO_ENTRATA_DECENTRATO.getConstant());
		}
	}

	/**
	 * Valida l'annullamento di una nota credito.
	 */
	private void validaAnnullaNotaCredito() {
		// Controlla che la riga sia fornita
		checkNotNull(model.getRigaDaEliminare(), "Nota credito da eliminare");
		// Valida le deduzioni
		validazioneDeduzioniAnnullamento();
	}

	/**
	 * Valida le deduzioni per l'annullamento
	 */
	private void validazioneDeduzioniAggiornamento() {
		BigDecimal totaleDeduzioni = calcolaTotaleDeduzioni();
		BigDecimal importoVecchioNoteCredito =  model.getTotaleImportoDaDedurreSuFattura();
		
		// Cerco la vecchia nota per ottenerne gli importi
		ElementoDocumento notaOld = new ElementoDocumento();
		notaOld.setUid(model.getUidNotaCredito());
		int index = ComparatorUtils.getIndexByUid(model.getListaDocumentoEntrata(), notaOld);
		notaOld = model.getListaDocumentoEntrata().get(index);
		
		// Nell'importo 'vecchio' ho ancora il vecchioValore della nota. Devo aggiungere il nuovo valore
		BigDecimal importoNuovoNoteCredito = importoVecchioNoteCredito.subtract(notaOld.getImportoDaDedurreSuFattura()).add(model.getImportoDaDedurreSuFattura());
		
		// Il nuovo importo delle note non deve essere inferiore al totale delle deduzioni
		checkCondition(importoNuovoNoteCredito.subtract(totaleDeduzioni).signum() >= 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre", "la somma degli importi da dedurre non deve superare l'importo delle note collegate"));
	}

	/**
	 * Valida le deduzioni per l'annullamento
	 */
	private void validazioneDeduzioniAnnullamento() {
		BigDecimal totaleDeduzioni = calcolaTotaleDeduzioni();
		BigDecimal importoVecchioNoteCredito = model.getTotaleImportoDaDedurreSuFattura();
		ElementoDocumento notaCredito = model.getListaDocumentoEntrata().get(model.getRigaDaEliminare().intValue());
		BigDecimal importoNuovoNoteCredito = importoVecchioNoteCredito.subtract(notaCredito.getImporto());
		BigDecimal nuovoValore = importoNuovoNoteCredito.subtract(totaleDeduzioni);

		log.debug("validazioneDeduzioniAnnullamento", "totaleDeduzioni: " + totaleDeduzioni + "; importoVecchioNoteCredito: " + importoVecchioNoteCredito
				+ "; importoNuovoNoteCredito: " + importoNuovoNoteCredito + "; calcolo per check: " + nuovoValore);

		checkCondition(nuovoValore.signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre",
				": la somma degli importi da dedurre non deve superare l'importo delle note collegate"));
		
	}

	/**
	 * Calcola il totale delle deduzioni sulle quote.
	 * 
	 * @return il totale delle deduzioni
	 */
	private BigDecimal calcolaTotaleDeduzioni() {
		BigDecimal totale = BigDecimal.ZERO;
		List<SubdocumentoEntrata> lista = model.getListaSubdocumentoEntrata();

		for (SubdocumentoEntrata se : lista) {
			if (se.getImportoDaDedurre() != null) {
				// Aggiungo l'importo da dedurre al totale
				totale = totale.add(se.getImportoDaDedurre());
				// Controllo che l'importo da dedurre non sia maggiore dell'importo del subdocumento
				checkCondition(se.getImporto().subtract(se.getImportoDaDedurre()).signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo da dedurre",
						"importo da dedurre non deve essere maggiore dell'importo per la quota numero " + se.getNumero()));
			}
		}
		return totale;
	}


	/**
	 * Redirezione al metodo di consultazione quote a partire dalla videata
	 * delle note di credito.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */

	public String ottieniListaQuoteNotaCredito() {
		// Mantengo la lista che ho in sessione
		model.impostaTotaliNoteQuote();
		return SUCCESS;
	}

	/**
	 * Aggiorna i dati relativi alle note di credito.
	 * 
	 */
	private void aggiornaDatiPerNote() {

		model.setListaDocumentoEntrata(model.getListaDocumentoEntrata());
		model.impostaTotaliNoteQuote();
		model.calcoloImporti();
	}
	
	/**
	 * Pulisce il model per l'inserimento di una nuova nota credito.
	 */
	public void cleanNoteCredito() {
		model.setNotaCredito(new DocumentoEntrata());
		model.setAttoAmministrativo(null);
		model.setImportoDaAttribuire(null);
	}

	
	/**
	 * Collego la notadi creditoscelta con ildocumento padre
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String collegaNotaCreditoEsistente() {
		String methodName = "collegaNotaCreditoEsistente";

		Integer uidDaCollegare = model.getUidDocumentoDaCollegare();
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
		// Ottengo i documenti collegati
		RicercaDocumentiCollegatiByDocumentoEntrata requestCollegati =
				model.creaRequestRicercaDocumentiCollegatiByDocumentoEntrata(model.getUidDocumentoDaAggiornare());
		logServiceRequest(requestCollegati);
		RicercaDocumentiCollegatiByDocumentoEntrataResponse responseCollegati =
				documentoEntrataService.ricercaDocumentiCollegatiByDocumentoEntrata(requestCollegati);
		logServiceResponse(responseCollegati);

		// Wrappo gli elementi
		List<ElementoDocumento> listaWrappers = ElementoDocumentoFactory.getInstances(responseCollegati.getDocumentiEntrataFiglio(), model.getDocumento()
				.getSoggetto(), model.getDocumento().getStatoOperativoDocumento());

		model.setListaDocumentoEntrata(listaWrappers);

		// Reimposto i documenti di entrata
		DocumentoEntrata documentoIns = model.getDocumento();
		documentoIns.setListaDocumentiEntrataFiglio(responseCollegati.getDocumentiEntrataFiglio());
		model.setDocumento(documentoIns);

		// Aggiorna i dati
		aggiornaDatiPerNote();
		// Terminato: fornisco il messaggio di successo	
		impostaInformazioneSuccesso();
		return SUCCESS;
		
	}
}
