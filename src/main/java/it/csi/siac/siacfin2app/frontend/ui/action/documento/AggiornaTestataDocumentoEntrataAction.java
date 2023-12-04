/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.AggiornaTestataDocumentoEntrataModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action per l'aggiornamento della testata del Documento di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/07/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaTestataDocumentoEntrataAction extends GenericAggiornaDocumentoAction<AggiornaTestataDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient DocumentoIvaEntrataService documentoIvaEntrataService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
		destroyAnchorFamily("OP-SPE-insDocIvaSpe");
		destroyAnchorFamily("OP-ENT-insDocIvaEnt");
		
		// Pulisco la sessione dei parametri da cancellare
		pulisciSessione();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("Aggiornamento documento di entrata");
		return ottieniDocumento();
	}

	/**
	 * Aggiornamento dell'anagrafica del Documento di Entrata.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamentoAnagrafica() {
		final String methodName = "aggiornamentoAnagrafica";
		AggiornaDocumentoDiEntrata request = model.creaRequestAggiornaDocumentoDiEntrata();
		logServiceRequest(request);
		
		AggiornaDocumentoDiEntrataResponse response = documentoEntrataService.aggiornaTestataDocumentoDiEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaDocumentoDiEntrata.class, response));
			addErrori(response);
			return INPUT;
		}
		
		impostaInformazioneSuccesso();
		DocumentoEntrata documento = response.getDocumentoEntrata();
		log.debug(methodName, "Aggiornamento effettuato per testata del Documento Entrata con uid: " + documento.getUid());
		
		// Imposto il dato nel model
		model.setDocumento(documento);
		
		// Tolgo il flag per l'ingresso sulle quote, se presente
		model.setIngressoTabQuote(Boolean.FALSE);
		// Ricalcolo il documento e tutti i dati associati
		return ottieniDocumento();
	}
	
	/**
	 * Valida l'aggiornamento dell'anagrafica del Documento di Entrata
	 */
	public void validateAggiornamentoAnagrafica() {
		final String methodName = "validateAggiornamentoAnagrafica";
		
		try {
			validaCampi();
		} catch (ParamValidationException e) {
			log.debug(methodName, "Errori nella validazione dei campi");
		}
	}

	/**
	 * Validazione dei campi.
	 */
	private void validaCampi() {
		final String methodName = "validaCampi";
		
		DocumentoEntrata documento = model.getDocumento();
		
		// Validazione campi obbligatori
		checkNotNull(documento.getDataEmissione(), "Data");
		validazioneImporto(documento.getImporto());
		checkNotNullNorEmpty(documento.getDescrizione(), "Descrizione");
		
		if(model.getDocumentoIncompleto()) {
			checkNotNullNorEmpty(model.getSoggetto().getCodiceSoggetto(), "Codice");
			validaSoggetto(true);
		}
		
		log.debug(methodName, "Campi obbligatorii: errori rilevati? " + hasErrori());
		
		// La data di emissione del documento deve essere coerente con l'anno dello stesso
		if(documento.getDataEmissione() != null) {
			Integer annoEmissione = Integer.decode(FormatUtils.formatDateYear(documento.getDataEmissione()));
			checkCondition(documento.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
		}
		
		// Validazione degli importi
		validazioneImportiTestata(documento, BigDecimal.ZERO);
		// Se la data di scadenza è presente, deve essere maggiore o uguale la data del documento
		checkCondition(documento.getDataEmissione() == null || documento.getDataScadenza() == null ||
			documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
			ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
		
		log.debug(methodName, "Validazione logica: errori rilevati? " + hasErrori());
	}
	
	/**
	 * Valida l'importo per l'inserimento del capitolo
	 * @param importo l'importo da validare
	 */
	private void validazioneImporto(BigDecimal importo) {
		checkCondition(importo != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"), true);
		checkCondition(importo.subtract(model.getTotaleDaPagareQuote()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "deve essere maggiore del totale delle quote"));
	}
	
	/**
	 * Ottiene il documento dal servizio, e ne ricalcola i dati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione.
	 */
	private String ottieniDocumento() {
		if(!caricamentoDatiDocumento()) {
			return INPUT;
		}
		if(!caricamentoDatiSoggetto()) {
			return INPUT;
		}
		
		model.impostaFlags();
		
		// Caricamento liste
		checkAndObtainListaClassiSoggetto();
		model.impostaListaTipoDocumentoNoteCredito();
		return SUCCESS;
	}
	
	/**
	 * Carica i dati relativi al documento.
	 * 
	 * @return <code>true</code> se il caricamento &eacute; andato a buon fine; <code>false</code> in caso contrario
	 */
	private boolean caricamentoDatiDocumento() {
		final String methodName = "caricamentoDatiDocumento";
		// Ricerca di dettaglio del documento
		RicercaDettaglioDocumentoEntrata request = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumentoDaAggiornare());
		logServiceRequest(request);
		
		RicercaDettaglioDocumentoEntrataResponse response = documentoEntrataService.ricercaDettaglioDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione della response");
			addErrori(response);
			return false;
		}
		
		model.impostaDatiDocumento(response.getDocumento());
		return true;
	}
	
	/**
	 * Carica i dati relativi al soggetto.
	 * 
	 * @return <code>true</code> se il caricamento &eacute; andato a buon fine; <code>false</code> in caso contrario
	 */
	private boolean caricamentoDatiSoggetto() {
		final String methodName = "caricamentoDatiSoggetto";
		// Ricerca di dettaglio del documento
		if(model.getSoggetto() != null) {
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);
			
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.error(methodName, "Errore nell'invocazione della response");
				addErrori(response);
				return false;
			}
			
			List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			model.impostaDatiSoggetto(response.getSoggetto(), listaSedeSecondariaSoggetto, listaModalitaPagamentoSoggetto);
		}
		return true;
	}
	
	
	/* **************** AJAX **************** */
	
	/**
	 * Restituisce la lista delle quote relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaQuoteEntrata() {
		return SUCCESS;
	}
	
	/* ****************************************************************************************************************** */
	
	// Dati IVA
	
	/**
	 * Ottiene le quote rilevanti ai fini IVA con tutti i dati necessar&icirc;.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniQuoteRilevantiIva() {
		final String methodName = "ottieniQuoteRilevantiIva";
		List<SubdocumentoEntrata> listaQuote = model.getListaSubdocumentoEntrata();
		List<ElementoSubdocumentoIvaEntrata> listaQuoteIva = new ArrayList<ElementoSubdocumentoIvaEntrata>();
		
		// Inizio con il filtrare le quote rilevanti IVA
		for(SubdocumentoEntrata se : listaQuote) {
			if(Boolean.TRUE.equals(se.getFlagRilevanteIVA())) {
				log.debug(methodName, "Subdocumento " + se.getNumero() + " rilevante IVA");
				listaQuoteIva.add(new ElementoSubdocumentoIvaEntrata(se, model.getAnnoEsercizioInt(), model.isGestioneUEB()));
			}
		}
		
		Iterator<ElementoSubdocumentoIvaEntrata> i = listaQuoteIva.iterator();
		
		while(!hasErrori() && i.hasNext()) {
			ElementoSubdocumentoIvaEntrata se = i.next();
			// Popolo i dati del subdocumento rilevante iva
			ottieniDatiCapitoloSubdocumento(se);
		}
		
		model.setListaQuoteRilevantiIva(listaQuoteIva);
		return SUCCESS;
	}
	
	/**
	 * Ottiene i dati del capitolo collegato al subdocumento
	 * 
	 * @param se il subdocumento
	 */
	private void ottieniDatiCapitoloSubdocumento(ElementoSubdocumentoIvaEntrata se) {
		final String methodName = "ottieniDatiCapitoloSubdocumento";
		
		// Se non ho l'accertamento, esco subito
		if(se.getAccertamento() == null) {
			log.debug(methodName, "Subdocumento numero " + se.getNumero() + " con uid " + se.getUid() + " senza accertamento collegato");
			return;
		}
		
		se.setCapitoloEntrata(se.getAccertamento().getCapitoloEntrataGestione());
		// Ottengo i dati dell'attivita
		ottieniDatiIvaSubdocumento(se);
	}

	/**
	 * Ottiene i dati dell'AttivitaIva.
	 * 
	 * @param se il subdocumento da popolare
	 */
	private void ottieniDatiIvaSubdocumento(ElementoSubdocumentoIvaEntrata se) {
		final String methodName = "ottieniDatiIvaSubdocumento";
		
		if(se.getSubdocumentoIva() == null) {
			log.debug(methodName, "Nessun subdocumentoIva collegato");
			return;
		}
		// Creo la request per il subdocumentoIva
		RicercaDettaglioSubdocumentoIvaEntrata request = model.creaRequestRicercaDettaglioSubdocumentoIvaEntrata(se.getSubdocumentoIva());
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaEntrataResponse response = documentoIvaEntrataService.ricercaDettaglioSubdocumentoIvaEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nel servizio di RicercaDettaglioSubdocumentoIvaEntrata");
			addErrori(response);
			return;
		}
		
		se.setSubdocumentoIva(response.getSubdocumentoIvaEntrata());
	}
	
	/**
	 * Regidire verso l'inserimento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneInserimentoDocumentoIvaEntrata() {
		final String methodName = "redirezioneInserimentoDocumentoIvaEntrata";
		StringBuilder sb = new StringBuilder();
		sb.append("Redirezione inserimento SubdocumentoIvaEntrata. Uid Documento: ")
			.append(model.getUidDocumentoDaAggiornare());
		if(model.getUidQuota() != null) {
			sb.append(" Uid quota: ")
				.append(model.getUidQuota());
		}
		// Pulisco la sessione
		sessionHandler.setParametro(BilSessionParameter.MODEL_INSERISCI_DOCUMENTO_IVA_ENTRATA, null);
		
		log.debug(methodName, sb.toString());
		return SUCCESS;
	}
	
	/**
	 * Redirige verso l'aggiornamento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneAggiornamentoDocumentoIvaEntrata() {
		log.debug("redirezioneAggiornamentoDocumentoIvaEntrata",
			"Redirezione aggiornamento SubdocumentoIvaEntrata. Uid subdocumentoIva: " + model.getUidSubdocumentoIva());
		return SUCCESS;
	}

}
