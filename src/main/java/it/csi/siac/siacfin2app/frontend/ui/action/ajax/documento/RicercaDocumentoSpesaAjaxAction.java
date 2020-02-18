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
import it.csi.siac.siacfin2app.frontend.ui.action.documento.RicercaDocumentoSpesaAction;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento.RicercaDocumentoSpesaAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;

/**
 * Classe di action per la gestione della ricerca della lista di fatture
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaDocumentoSpesaAjaxAction extends GenericBilancioAction<RicercaDocumentoSpesaAjaxModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 9043593340635072663L;
	//servizi
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	/**
	 * Ricerca la lista delle fatture associabili.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		RicercaSinteticaDocumentoSpesa request = model.creaRequestRicercaDocumentoSpesa();
		logServiceRequest(request);
		RicercaSinteticaDocumentoSpesaResponse response = documentoSpesaService.ricercaSinteticaDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica delle fatture");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_SPESA, request);

		//imposto in sessione i risultati ottenuti
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_SPESA, response.getDocumenti());

		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		//prendo i valori che ho precedentemente messo in sessione
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		//li compongo ed imposto in sessione in modo che siano presenti in tutte le pagine di ricerca
		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO,
				model.componiStringaRiepilogo(model.getDocumentoSpesa(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		
		//imposto i valori necessari per gestire la navigazione avanti indietro
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		
		log.debug(methodName, "Imposto il totale");
		model.setImportoTotale(response.getImportoTotale());
		return SUCCESS;
	}
	
	/**
	 * Ricerca la lista delle fatture associabili.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaModulare() {
		//imposto la request
		RicercaSinteticaModulareDocumentoSpesa request = model.creaRequestRicercaSinteticaModulareDocumentoSpesa(new DocumentoSpesaModelDetail[] {DocumentoSpesaModelDetail.Soggetto, DocumentoSpesaModelDetail.Stato, DocumentoSpesaModelDetail.TipoDocumento});
		logServiceRequest(request);
		
		return ottieniResponseEImpostaDati(request);
	}
	
	/**
	 * Ricerca la lista delle fatture associabili.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaFattureDaAssociareCEC() {
		RicercaSinteticaModulareDocumentoSpesa request = model.creaRequestRicercaSinteticaModulareDocumentoSpesa(new DocumentoSpesaModelDetail[] {DocumentoSpesaModelDetail.Soggetto, DocumentoSpesaModelDetail.Stato, DocumentoSpesaModelDetail.TipoDocumento, DocumentoSpesaModelDetail.ImportoDaPagareNonPagatoInCassaEconomale});
		logServiceRequest(request);
		
		return ottieniResponseEImpostaDati(request);
	}

	
	/**
	 * Ricerca la lista delle fatture associabili.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDocumentoModale() {
		final String methodName = "ricercaDocumentoModale";

		if (!checkIsValidaRicercaDocumento()) {
			log.debug(methodName, "Validazione fallita");
			return SUCCESS;
		}
		
		return ricercaFattureDaAssociareCEC();

	}
	
	/**
	 * Ottiene la response dal servizio {@link RicercaSinteticaModulareDocumentoSpesaService} a partire da una request.
	 * Da questa response imposta poi in sessione i parametri necessari per la gestione ajax dei risultati ricerca e altri 
	 * @param request la request per la chiamata al servicio
	 * 
	 * @return il risultato dell'invocazione
	 * */
	private String ottieniResponseEImpostaDati(RicercaSinteticaModulareDocumentoSpesa request) {
		final String methodName = "ottieniResponseEImpostaDatiInSessione";
		RicercaSinteticaModulareDocumentoSpesaResponse response = documentoSpesaService.ricercaSinteticaModulareDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica delle fatture");
			addErrori(response);
			return SUCCESS;
		}
		//prendo i valori che ho precedentemente messo in sessione
		if(response.getTotaleElementi() == 0) {
			//non ho trovato risultati: imposto u errore nel model
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DOCUMENTI_SPESA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DOCUMENTI_SPESA, response.getDocumenti());

		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		//imposto i valori elaborati in sessione
		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_DOCUMENTO,
				model.componiStringaRiepilogo(model.getDocumentoSpesa(), listaTipoDocumento, listaTipoAtto, listaStrutturaAmministrativoContabile));
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA, response.getImportoTotale());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		log.debug(methodName, "Imposto il totale");
		model.setImportoTotale(response.getImportoTotale());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link RicercaDocumentoSpesaAction#ricercaDocumento()}.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	private boolean checkIsValidaRicercaDocumento() {
		final String methodName = "checkIsValidaRicercaDocumento";
		log.debugStart(methodName, "Verifica campi");
		
		DocumentoSpesa documento = model.getDocumentoSpesa();
		boolean formValido = checkPresenzaIdEntita(documento.getTipoDocumento());
		
		if(formValido) {
			// Ho il tipo documento. Ne tiro su i dati
			TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), documento.getTipoDocumento());
			documento.setTipoDocumento(tipoDocumento);
		}
		
		formValido = formValido
				|| checkCampoValorizzato(documento.getStatoOperativoDocumento(), "Stato")
				|| checkCampoValorizzato(documento.getAnno(), "Anno")
				|| checkStringaValorizzata(documento.getNumero(), "Numero")
				|| checkCampoValorizzato(documento.getDataEmissione(), "Data");
		
		//verifica dati movimento
		boolean movimentoPresente = (model.getImpegno() !=null && model.getImpegno().getAnnoMovimento() != 0 && Integer.toString(model.getImpegno().getAnnoMovimento()) != null)
				|| model.getImpegno().getNumero() != null;
		model.setMovimentoPresente(Boolean.valueOf(movimentoPresente));
		
		log.debug(methodName, "movimento presente" + movimentoPresente + "-" + model.getImpegno().getAnnoMovimento() + "-" + model.getImpegno().getNumero());
		//controllo se l'elenco sia presente
		boolean elencoPresente = model.getElencoDocumenti() != null && (model.getElencoDocumenti().getAnno() != null || model.getElencoDocumenti().getNumero() != null);
		if(elencoPresente){
			checkCondition(model.getElencoDocumenti().getAnno() != null && model.getElencoDocumenti().getNumero() != null, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Elenco", ": devono essere entrambi valorizzati o non valorizzati"));
		}
		model.setElencoPresente(Boolean.valueOf(elencoPresente));
		
		checkCondition(formValido || movimentoPresente || elencoPresente, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());

		// Anno Impegno e numero devono essere entrambi presenti o entrambi assenti
		if(movimentoPresente) {
			if(((model.getImpegno().getAnnoMovimento() != 0 && Integer.toString(model.getImpegno().getAnnoMovimento()) != null) && model.getImpegno().getNumero() == null) ||
					((model.getImpegno().getAnnoMovimento() == 0 || Integer.toString(model.getImpegno().getAnnoMovimento()) == null) && model.getImpegno().getNumero() != null)) {
				checkCondition(false, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e Numero Movimento", ": devono essere entrambi valorizzati o non valorizzati"));
			} else {
				// sono entrambi valorizzati, verifico che esista l'impegno
				verificaUnicitaImpegno();
			}
		}
		log.debugEnd(methodName, "");
		return !hasErrori();
	}
	
	/**
	 * Validazione di unicit&agrave; per l'impegno.
	 */
	private void verificaUnicitaImpegno() {
		
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
		} else {
			//deve esistere 
			checkCondition(response.getImpegno() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Movimento Anno e numero", "L'impegno indicato"));
			model.setImpegno(response.getImpegno());
		}
	}
	
	/**
	 * Ricerca delle quote per il documento di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'operazione
	 */
	public String ricercaQuoteDocumentoSpesa() {
		final String methodName = "ricercaQuoteDocumentoSpesa";
		//chiamo il servizio
		RicercaQuoteByDocumentoSpesa request = model.creaRequestRicercaQuoteByDocumentoSpesa();
		logServiceRequest(request);
		RicercaQuoteByDocumentoSpesaResponse response = documentoSpesaService.ricercaQuoteByDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Trovati " + response.getSubdocumentiSpesa().size() + " subdocumenti di spesa per il documento " + model.getDocumentoSpesa().getUid());
		model.setListaSubdocumentoSpesa(response.getSubdocumentiSpesa());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #ricercaQuoteDocumentoSpesa()}.
	 */
	public void validateRicercaQuoteDocumentoSpesa() {
		checkNotNullNorInvalidUid(model.getDocumentoSpesa(), "documento");
	}

}
