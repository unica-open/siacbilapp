/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.associa.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAssociaAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AggiornaAllegatoAttoModel.TabVisualizzazione;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AssociaDocumentoAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Classe di Action per l'associazione tra documento e allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 30/set/2014 - aggiunta la classe base
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AssociaDocumentoAllegatoAttoAction extends GenericAssociaAllegatoAttoAction<AssociaDocumentoAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8127023922599679517L;
	
	@Autowired private transient DocumentoService documentoService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Pulisco il model
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Caricamento dell'allegato atto
		RicercaDettaglioAllegatoAtto request = model.creaRequestRicercaDettaglioAllegatoAtto();
		logServiceRequest(request);
		RicercaDettaglioAllegatoAttoResponse response = allegatoAttoService.ricercaDettaglioAllegatoAtto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAllegatoAtto.class, response));
			throwExceptionFromErrori(response.getErrori());
		}
		model.setAllegatoAtto(response.getAllegatoAtto());
		
		// Caricamento liste
		try {
			log.debug(methodName, "Caricamento liste classificatori");
			caricaListaTipoAtto();
			caricaListaTipoDocumento();
			caricaListaClasseSoggetto();
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		
		model.setGestioneResiduiDisabilitata(AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.ALLEGATO_ATTO_INSERISCI_NO_RESIDUI, sessionHandler.getAzioniConsentite()));
		
		return SUCCESS;
	}
	
	/**
	 * Controlla se la lista dei TipoDocumento sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 */
	private void caricaListaTipoDocumento() {
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		if(listaTipoDocumento == null) {
			RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento();
			logServiceRequest(request);
			RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				listaTipoDocumento = response.getElencoTipiDocumento();
				sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO, listaTipoDocumento);
			}
		}
		// SIAC-5161
		List<TipoDocumento> listaDaEscludere = new ArrayList<TipoDocumento>();
		for (TipoDocumento item : listaTipoDocumento) {
			if (item.isNotaCredito()){
				listaDaEscludere.add(item);
			}
		}
		for(TipoDocumento item : listaDaEscludere){
			listaTipoDocumento.remove(item);
		}
		model.setListaTipoDocumento(listaTipoDocumento);
	}

	/**
	 * Preparazione per l'esecuzione del metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulizia del model
		model.setTipoFamigliaDocumento(null);
		model.setTipoDocumento(null);
		// Documento
		model.setAnnoDocumento(null);
		model.setNumeroDocumento(null);
		model.setNumeroSubdocumento(null);
		model.setDataEmissioneDocumento(null);
		// Movimento Gestione
		model.setAnnoMovimento(null);
		model.setNumeroMovimento(null);
		// Soggetto
		model.setSoggetto(null);
		model.setAttoAmministrativo(null);
	}
	
	/**
	 * Metodo per il completamento dello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		final String methodName = "completeStep1";
		log.debug(methodName, "Caricamento delle quote");
		int totaleQuote = 0;
		try {
			totaleQuote = caricaQuote();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, "Errore nell'invocazione dei servizi: " + wsife.getMessage(), wsife);
			return INPUT;
		}
		log.debug(methodName, "Totale quote trovate: " + totaleQuote);
		if(totaleQuote == 0) {
			log.info(methodName, "Nessuna quota corrispondente ai risultati di ricerca ottenuta");
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Documento", "relativo ai parametri di ricerca"));
			return INPUT;
		}
		
		// Imposto il totale dei subdocumenti collegati
		model.setTotaleSubdocumentiEntrata(BigDecimal.ZERO);
		model.setTotaleSubdocumentiSpesa(BigDecimal.ZERO);
		// Inizializzazione
		model.setMappaUidSubdocumenti(new HashMap<Integer, String>());
		
		log.debug(methodName, "Invocazione dei servizi effettuata con successo. Inizializzazione dei campi effettuata. Redirezione allo step 2");
		return SUCCESS;
	}

	/**
	 * Carica le quote relative alla Spesa.
	 * 
	 * @return il numero di quote ottenute
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private int caricaQuote() throws WebServiceInvocationFailureException {
		final String methodName = "caricaQuoteSpesa";
		// Caricamento delle spese
		RicercaQuoteDaAssociare request = model.creaRequestRicercaQuoteDaAssociare();
		logServiceRequest(request);
		RicercaQuoteDaAssociareResponse response = documentoService.ricercaQuoteDaAssociare(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorString = createErrorInServiceInvocationString(RicercaQuoteDaAssociare.class, response);
			log.info(methodName, errorString);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorString);
		}
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_DA_ASSOCIARE, response.getListaSubdocumenti());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUOTE_DA_ASSOCIARE, request);
		
		log.debug(methodName, "Totale quote di spesa: " + response.getTotaleElementi());
		return response.getTotaleElementi();
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		boolean isRicercaValida = (model.getTipoDocumento() != null && model.getTipoDocumento().getUid() != 0) ||
				model.getAnnoDocumento() != null || (model.getNumeroDocumento() != null && StringUtils.isNotBlank(model.getNumeroDocumento())) || model.getNumeroSubdocumento() != null ||
				model.getDataEmissioneDocumento() != null ||
				model.getNumeroMovimento() != null || model.getAnnoMovimento() != null ||
				(model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto()));
		
		checkCondition(isRicercaValida,ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		checkCondition(model.getNumeroSubdocumento() == null || (model.getAnnoDocumento() != null && model.getNumeroDocumento() != null && StringUtils.isNotBlank(model.getNumeroDocumento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero subdocumento", "deve essere valorizzato solo se indicata la chiave del documento"));
		
		checkCondition(model.getAnnoDocumento() == null || model.getAnnoDocumento().compareTo(model.getAnnoEsercizioInt()) <= 0,
			ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
		
		if(model.getDataEmissioneDocumento() != null) {
			log.debug(methodName, "Data emissione documento valorizzata: " + model.getDataEmissioneDocumento());
			Integer annoEmissione = Integer.valueOf(FormatUtils.formatDateYear(model.getDataEmissioneDocumento()));
			checkCondition(annoEmissione.compareTo(model.getAnnoEsercizioInt()) <= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
		}
		checkCondition(!(model.getAnnoMovimento() == null ^ model.getNumeroMovimento() == null),
			ErroreCore.FORMATO_NON_VALIDO.getErrore("anno e numero movimento", "devono essere entrambi valorizzati o entrambi non valorizzati"));
		// Validazione Soggetto
		checkSoggettoValido(model.getSoggetto(), Arrays.asList(StatoOperativoAnagrafica.VALIDO, StatoOperativoAnagrafica.SOSPESO));
		// Validazione Provvedimento
		try {
			checkAttoAmministrativoValido(model.getAttoAmministrativo());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errori nella validazione dell'atto amministrativo: " + pve.getMessage());
		}
	}
	
	/**
	 * Metodo per l'ingresso nello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		// Pulisco il model
		model.setMappaUidSubdocumenti(new HashMap<Integer, String>());
	}
	
	/**
	 * Completamento dello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		log.debug(methodName, "Numero di subdocumenti da associare all'elenco: " + model.getMappaUidSubdocumenti().size());
		
		InserisceElenco request = model.creaRequestInserisceElenco();
		logServiceRequest(request);
		InserisceElencoResponse response = allegatoAttoService.inserisceElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Errori nell'invocazione
			log.info(methodName, createErrorInServiceInvocationString(InserisceElenco.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Inserito elenco con uid " + response.getElencoDocumentiAllegato().getUid());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		// Importo il tab su cui redirigere in sessione
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, TabVisualizzazione.ELENCO);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkCondition(!model.getMappaUidSubdocumenti().isEmpty(),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Quota da associare"));
	}
	
}

