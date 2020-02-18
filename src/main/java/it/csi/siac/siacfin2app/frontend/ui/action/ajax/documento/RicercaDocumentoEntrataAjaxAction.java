/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.documento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.documento.RicercaDocumentoEntrataAction;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RicercaDocumentoEntrataAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveResponse;

/**
 * Classe di action per la gestione della ricerca della lista di fatture
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDocumentoEntrataAjaxAction extends GenericBilancioAction<RicercaDocumentoEntrataAjaxModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 9043593340635072663L;
	//servizi utilizzati dalla action
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	/**
	 * Ricerca la lista delle fatture associabili.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		RicercaSinteticaModulareDocumentoEntrata request = model.creaRequestRicercaSinteticaModulareDocumentoEntrata();
		logServiceRequest(request);
		RicercaSinteticaModulareDocumentoEntrataResponse response = documentoEntrataService.ricercaSinteticaModulareDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica delle fatture");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			//imposto nel model un errore: non ho trovato nessun elemento
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		// Imposto in sessione la request
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_ENTRATA, request);
		
		//imposto in sessione la lista ottenuta dal servizio
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_ENTRATA, response.getDocumenti());
		
		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		//se sono arrivata qui, ho sicuramente impostato questi filtri di ricerca
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		//setto in sessione le informazioni che devono essere mostrate in tutte le pagine
		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO,
				model.componiStringaRiepilogo(model.getDocumentoEntrata(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		//setto in sessione i parametri che servono per non far sbarellare datatable
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		
		//imposto i valodi nel model
		log.debug(methodName, "Imposto il totale");
		model.setImportoTotale(response.getImportoTotale());
		return SUCCESS;
	}
	
	/**
	 * Ricerca la lista delle fatture associabili.
	 * @deprecated usare {@link #ricerca()}.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Deprecated
	public String ricercaDocumentoModale() {
		final String methodName = "ricercaDocumentoModale";

		if (!checkIsValidaRicercaDocumento()) {
			//si sono verificati errori
			log.debug(methodName, "Validazione fallita");
			return SUCCESS;
		}
		//questa ricerca e' troppo pesante
		RicercaSinteticaDocumentoEntrata request = model.creaRequestRicercaSinteticaDocumentoEntrata();
		logServiceRequest(request);
		RicercaSinteticaDocumentoEntrataResponse response = documentoEntrataService.ricercaSinteticaDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica delle fatture");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			//imposto nel model un errore: non ho trovato nessun elemento
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_ENTRATA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_ENTRATA, response.getDocumenti());

		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);

		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO,
				model.componiStringaRiepilogo(model.getDocumentoEntrata(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link RicercaDocumentoEntrataAction#ricercaDocumento()}.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	private boolean checkIsValidaRicercaDocumento() {
		final String methodName = "checkIsValidaRicercaDocumento";
		log.debugStart(methodName, "Verifica campi");
		
		DocumentoEntrata documento = model.getDocumentoEntrata();
		//il tipo documento e' obbligatorio
		Boolean formValido = checkPresenzaIdEntita(documento.getTipoDocumento());
		
		if(Boolean.TRUE.equals(formValido)) {
			// Ho il tipo documento. Ne tiro su i dati
			TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), documento.getTipoDocumento());
			documento.setTipoDocumento(tipoDocumento);
		}
		
		formValido = formValido ||
					 checkCampoValorizzato(documento.getStatoOperativoDocumento(), "Stato") ||
					 checkCampoValorizzato(documento.getAnno(), "Anno") ||
					 checkStringaValorizzata(documento.getNumero(), "Numero") ||
					 checkCampoValorizzato(documento.getDataEmissione(), "Data");
		
		//verifica dati movimento
		boolean movimentoPresente = (model.getAccertamento().getAnnoMovimento() != 0 && Integer.toString(model.getAccertamento().getAnnoMovimento()) != null) ||
																	model.getAccertamento().getNumero() != null;
		model.setMovimentoPresente(movimentoPresente);
		
		log.debug(methodName, "movimento presente"+movimentoPresente+"-"+model.getAccertamento().getAnnoMovimento()+"-"+model.getAccertamento().getNumero());
		//controllo che si voglia filtrare per elenco
		boolean elencoPresente = model.getElencoDocumenti() != null && ( model.getElencoDocumenti().getAnno() != null || model.getElencoDocumenti().getNumero() != null);
		if(elencoPresente){
			checkCondition(model.getElencoDocumenti().getAnno() != null && model.getElencoDocumenti().getNumero() != null, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Elenco", ": devono essere entrambi valorizzati o non valorizzati"));
		}
		model.setElencoPresente(elencoPresente);
		
		//per poter effettuare la ricerca ho bisogno di almeno un criterio di ricerca
		checkCondition(formValido || movimentoPresente || elencoPresente, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());

		// Anno Accertamento e numero devono essere entrambi presenti o entrambi assenti
		if(movimentoPresente) {
			if(((model.getAccertamento().getAnnoMovimento() != 0 && Integer.toString(model.getAccertamento().getAnnoMovimento()) != null) && model.getAccertamento().getNumero() == null) ||
			   ((model.getAccertamento().getAnnoMovimento() == 0 || Integer.toString(model.getAccertamento().getAnnoMovimento()) == null) && model.getAccertamento().getNumero() != null)) {
				checkCondition(false, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Movimento", ": devono essere entrambi valorizzati o non valorizzati"));
			} else {
				// sono entrambi valorizzati, verifico che esista l'accertamento
				verificaUnicitaAccertamento();
			}
		}
		log.debugEnd(methodName, "");
		return !hasErrori();
	}
	
	/**
	 * Validazione di unicit&agrave; per l'accertamento.
	 */
	private void verificaUnicitaAccertamento() {
		
		//chiamo il servizio ricerca accertamento per chiave
		RicercaAccertamentoPerChiave request = model.creaRequestRicercaAccertamento();
		RicercaAccertamentoPerChiaveResponse response = movimentoGestioneService.ricercaAccertamentoPerChiave(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
		} else {
			//non si sono verificatui errori, ma devo comunque controllare di aver trovato un accertamento su db
			checkCondition(response.getAccertamento() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Movimento Anno e numero", "L'impegno indicato"));
			model.setAccertamento(response.getAccertamento());
		}
	}
	
	/**
	 * Ricerca delle quote per il documento di Entrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'operazione
	 */
	public String ricercaQuoteDocumentoEntrata() {
		final String methodName = "ricercaQuoteDocumentoEntrata";
		//chiamo il servizio per ottenere le quote
		RicercaQuoteByDocumentoEntrata request = model.creaRequestRicercaQuoteByDocumentoEntrata();
		logServiceRequest(request);
		RicercaQuoteByDocumentoEntrataResponse response = documentoEntrataService.ricercaQuoteByDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		//il servizio si e' concluso con successo
		log.debug(methodName, "Trovati " + response.getSubdocumentiEntrata().size() + " subdocumenti di Entrata per il documento " + model.getDocumentoEntrata().getUid());
		model.setListaSubdocumentoEntrata(response.getSubdocumentiEntrata());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaQuoteDocumentoEntrata()}.
	 */
	public void validateRicercaQuoteDocumentoEntrata() {
		//devo avere un uid per poter filtrare le quote
		checkNotNullNorInvalidUid(model.getDocumentoEntrata(), "documento");
	}

}
