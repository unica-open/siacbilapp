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
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.AggiornaTestataDocumentoSpesaModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action per l'aggiornamento della testata del Documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/07/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaTestataDocumentoSpesaAction extends GenericAggiornaDocumentoAction<AggiornaTestataDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoIvaSpesaService documentoIvaSpesaService;
	//SIAC-6186
	@Autowired private transient CodificheService codificheService;
	
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
		checkCasoDUsoApplicabile(model.getTitolo());
		return ottieniDocumento();
	}

	/**
	 * Aggiornamento dell'anagrafica del Documento di Spesa.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamentoAnagrafica() {
		final String methodName = "aggiornamentoAnagrafica";
		AggiornaDocumentoDiSpesa request = model.creaRequestAggiornaDocumentoDiSpesa();
		logServiceRequest(request);
		
		AggiornaDocumentoDiSpesaResponse response = documentoSpesaService.aggiornaTestataDocumentoDiSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaDocumentoDiSpesa.class, response));
			addErrori(response);
			return INPUT;
		}
		
		impostaInformazioneSuccesso();
		DocumentoSpesa documento = response.getDocumentoSpesa();
		log.debug(methodName, "Aggiornamento effettuato per la testata del Documento Spesa con uid: " + documento.getUid());
		
		// Imposto il dato nel model
		model.setDocumento(documento);
		
		// Tolgo il flag per l'ingresso sulle quote, se presente
		model.setIngressoTabQuote(Boolean.FALSE);
		// Ricalcolo il documento e tutti i dati associati
		return ottieniDocumento();
	}
	
	/**
	 * Valida l'aggiornamento dell'anagrafica del Documento di Spesa
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
	 * Validazione dei campi
	 */
	private void validaCampi() {
		final String methodName = "validaCampi";
		
		DocumentoSpesa documento = model.getDocumento();
		
		// Validazione campi obbligatori
		checkCondition(documento.getDataEmissione() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data"), true);
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
	 * Valida l'importo per l'inserimento del documento.
	 * 
	 * @param importo l'importo del documento
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

		checkAndObtainListeSIOPE();
		
		// Leggo i dati della precedente azione
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		
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
		RicercaDettaglioDocumentoSpesa request = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumentoDaAggiornare());
		logServiceRequest(request);
		
		RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(request);
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
		return true;
	}
	
	/* **************** AJAX **************** */
	
	/**
	 * Restituisce la lista delle quote relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaQuote() {
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
		List<SubdocumentoSpesa> listaQuote = model.getListaSubdocumentoSpesa();
		List<ElementoSubdocumentoIvaSpesa> listaQuoteIva = new ArrayList<ElementoSubdocumentoIvaSpesa>();
		
		// Inizio con il filtrare le quote rilevanti IVA
		for(SubdocumentoSpesa ss : listaQuote) {
			if(Boolean.TRUE.equals(ss.getFlagRilevanteIVA())) {
				log.debug(methodName, "Subdocumento " + ss.getNumero() + " rilevante IVA");
				listaQuoteIva.add(new ElementoSubdocumentoIvaSpesa(ss, model.getAnnoEsercizioInt(), model.isGestioneUEB()));
			}
		}
		
		Iterator<ElementoSubdocumentoIvaSpesa> i = listaQuoteIva.iterator();
		
		while(!hasErrori() && i.hasNext()) {
			ElementoSubdocumentoIvaSpesa ss = i.next();
			// Popolo i dati del subdocumento rilevante iva
			ottieniDatiCapitoloSubdocumento(ss);
		}
		
		model.setListaQuoteRilevantiIva(listaQuoteIva);
		return SUCCESS;
	}
	
	/**
	 * Ottiene i dati del capitolo collegato al subdocumento
	 * 
	 * @param ss il subdocumento
	 */
	private void ottieniDatiCapitoloSubdocumento(ElementoSubdocumentoIvaSpesa ss) {
		final String methodName = "ottieniDatiCapitoloSubdocumento";
		
		// Se non ho l'impegno, esco subito
		if(ss.getImpegno() == null) {
			log.debug(methodName, "Subdocumento numero " + ss.getNumero() + " con uid " + ss.getUid() + " senza impegno collegato");
			return;
		}
		
		ss.setCapitoloUscita(ss.getImpegno().getCapitoloUscitaGestione());
		// Ottengo i dati dell'attivita
		ottieniDatiIvaSubdocumento(ss);
	}

	/**
	 * Ottiene i dati dell'AttivitaIva.
	 * 
	 * @param ss il subdocumento da popolare
	 */
	private void ottieniDatiIvaSubdocumento(ElementoSubdocumentoIvaSpesa ss) {
		final String methodName = "ottieniDatiIvaSubdocumento";
		
		if(ss.getSubdocumentoIva() == null) {
			log.debug(methodName, "Nessun subdocumentoIva collegato");
			return;
		}
		// Creo la request per il subdocumentoIva
		RicercaDettaglioSubdocumentoIvaSpesa request = model.creaRequestRicercaDettaglioSubdocumentoIvaSpesa(ss.getSubdocumentoIva());
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.ricercaDettaglioSubdocumentoIvaSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nel servizio di RicercaDettaglioSubdocumentoIvaSpesa");
			addErrori(response);
			return;
		}
		SubdocumentoIvaSpesa sis = response.getSubdocumentoIvaSpesa();
		ss.setSubdocumentoIva(sis);
		model.setUidSubdocumentoIva(sis.getUid());
	}
	
	/**
	 * Redirige verso l'inserimento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneInserimentoDocumentoIvaSpesa() {
		final String methodName = "redirezioneInserimentoDocumentoIvaSpesa";
		StringBuilder sb = new StringBuilder();
		sb.append("Redirezione inserimento SubdocumentoIvaSpesa. Uid Documento: ")
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
	public String redirezioneAggiornamentoDocumentoIvaSpesa() {
		log.debug("redirezioneAggiornamentoDocumentoIvaSpesa",
			"Redirezione aggiornamento SubdocumentoIvaSpesa. Uid subdocumentoIva: " + model.getUidSubdocumentoIva());
		return SUCCESS;
	}

	
	
	
	//SIAC-6186
	/**
	 * Controlla se le lista delSIOPE siano presenti valorizzate nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	private void checkAndObtainListeSIOPE() {
		if(!model.getListaSiopeDocumentoTipo().isEmpty()
				&& !model.getListaSiopeDocumentoTipoAnalogico().isEmpty()) {
			// Ho gia' le liste nel model
			return;
		}
		
		// Ricerca la causale come codifica generica
		RicercaCodifiche req = model.creaRequestRicercaCodifiche(SiopeDocumentoTipo.class, SiopeDocumentoTipoAnalogico.class);
		 // decommentare linea53??
		RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
		
		// Se ho errori ignoro la response
		// TODO: gestire gli errori?
		if(!res.hasErrori()) {
			model.setListaSiopeDocumentoTipo(res.getCodifiche(SiopeDocumentoTipo.class));
			model.setListaSiopeDocumentoTipoAnalogico(res.getCodifiche(SiopeDocumentoTipoAnalogico.class));
		}
	}
}