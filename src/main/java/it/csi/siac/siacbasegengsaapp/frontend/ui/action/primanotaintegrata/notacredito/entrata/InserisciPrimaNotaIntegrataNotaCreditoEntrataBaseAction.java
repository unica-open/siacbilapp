/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.entrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.InserisciPrimaNotaIntegrataNotaCreditoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.entrata.InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata per la nota di credito.
 * 
 * @author Valentina
 * @version 1.0.0 - 14/03/2016
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseAction<M extends InserisciPrimaNotaIntegrataNotaCreditoEntrataBaseModel>
		extends InserisciPrimaNotaIntegrataNotaCreditoBaseAction<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ElementoQuotaEntrataRegistrazioneMovFin, ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper, M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9012410004301347947L;
	
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_NOTA_CREDITO_ENTRATA_FIN = "CompletaNotaCreditoEntrataInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_NOTA_CREDITO_ENTRATA_FIN = "CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_NOTA_CREDITO_ENTRATA_GSA = "CompletaNotaCreditoEntrataInsPrimaNotaIntegrataGSAModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_NOTA_CREDITO_ENTRATA_GSA = "CompletaValidaNotaCreditoEntrataInsPrimaNotaIntegrataGSAModel";
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient DocumentoIvaEntrataService documentoIvaEntrataService;
	
	@Override
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumento";
		
		if(model.getDocumento() == null) {
			// Non ho il documento. Lo prendo dal movimento
			DocumentoEntrata documentoEntrata = (DocumentoEntrata) model.getRegistrazioneMovFin().getMovimento();
			model.setDocumento(documentoEntrata);
		}
		
		DocumentoEntrata notaCredito = retrieveNotaCredito();
		DocumentoEntrata documentoPadre = retrieveDocumentoPadre(methodName);
		List<SubdocumentoEntrata> listaQuote = retrieveQuoteDocumento();
		List<SubdocumentoIvaEntrata> listaIva = retrieveDatiIva();
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper(notaCredito, documentoPadre, listaQuote, listaIva, model.isGestioneUEB()));
	}

	/**
	 * Ottiene la nota di credito
	 * @return la nota di credito
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private DocumentoEntrata retrieveNotaCredito() throws WebServiceInvocationFailureException {
		final String methodName = "";
		RicercaDettaglioDocumentoEntrata req = model.creaRequestRicercaDettaglioDocumentoEntrata();
		logServiceRequest(req);
		RicercaDettaglioDocumentoEntrataResponse res = documentoEntrataService.ricercaDettaglioDocumentoEntrata(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoEntrata.class, res));
		}
		DocumentoEntrata notaCredito = res.getDocumento();
		if(notaCredito == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento di entrata", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Nessun documento reperito");
		}
		
		log.debug(methodName, "Trovato documento di entrata corrispondente all'uid " + model.getDocumento().getUid());
		model.setDocumento(notaCredito);
		return notaCredito;
	}

	/**
	 * Ottiene il documento padre
	 * @return il documento padre
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private DocumentoEntrata retrieveDocumentoPadre(final String methodName) throws WebServiceInvocationFailureException {
		if(model.getDocumento().getListaDocumentiEntrataPadre() == null || model.getDocumento().getListaDocumentiEntrataPadre().isEmpty()){
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento padre della nota di credito", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Impossibile determinare il documento a cui e' associata la nota di credito");
		}
		DocumentoEntrata documentoPadre = model.getDocumento().getListaDocumentiEntrataPadre().get(0);
		RicercaDettaglioDocumentoEntrata reqDP = model.creaRequestRicercaDettaglioDocumentoEntrata(documentoPadre);
		logServiceRequest(reqDP);
		RicercaDettaglioDocumentoEntrataResponse resDP = documentoEntrataService.ricercaDettaglioDocumentoEntrata(reqDP);
		logServiceResponse(resDP);
		
		if(resDP.hasErrori()) {
			addErrori(resDP);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoEntrata.class, resDP));
		}
		documentoPadre = resDP.getDocumento();
		if(documentoPadre == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento padre della nota di credito", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Nessun documento reperito");
		}
		
		log.debug(methodName, "Trovato documento di entrata corrispondente all'uid " + documentoPadre.getUid());
		model.setDocumentoPadre(documentoPadre);
		return documentoPadre;
	}

	/**
	 * Carica le quote del documento.
	 * @param uidDocumento l'id del documento
	 * @returns le quote del documento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoEntrata> retrieveQuoteDocumento() throws WebServiceInvocationFailureException {
		RicercaQuoteByDocumentoEntrata req = model.creaRequestRicercaQuoteByDocumentoEntrata(model.getDocumentoPadre());
		logServiceRequest(req);
		RicercaQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaQuoteByDocumentoEntrata(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaQuoteByDocumentoEntrata.class, res));
		}
		return res.getSubdocumentiEntrata();
	}
	
	/**
	 * Caricamento dei dati dell'IVA.
	 * @param uidDocumento l'id del documento
	 * @return i subdocumenti iva spesa
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoIvaEntrata> retrieveDatiIva() throws WebServiceInvocationFailureException {
		RicercaNoteCreditoIvaDocumentoEntrata req = model.creaRequestRicercaNoteCreditoIvaDocumentoEntrata(model.getDocumentoPadre());
		logServiceRequest(req);
		RicercaNoteCreditoIvaDocumentoEntrataResponse res = documentoIvaEntrataService.ricercaNoteCreditoIvaDocumentoEntrata(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaNoteCreditoIvaDocumentoEntrata.class, res));
		}
		return res.getSubdocumentoIvaEntrata();
	}
	
	@Override
	protected String computeSuffix() {
		return " (doc S)";
	}
	
	@Override
	protected ElementoQuotaEntrataRegistrazioneMovFin wrapSubdocumento(DocumentoEntrata notaCredito, SubdocumentoEntrata subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		return new ElementoQuotaEntrataRegistrazioneMovFin(notaCredito, subdocumento, registrazioneMovFin);
	}

	/**
	 * Wrap per le quote
	 */
	@Override
	protected void wrapQuote() {
		DocumentoEntrata notaCredito = model.getDocumento();
		DocumentoEntrata docPadre = model.getDocumentoPadre();
		List<SubdocumentoEntrata> listaSubdocumento = docPadre.getListaSubdocumenti();
		List<RegistrazioneMovFin> listaRegistrazioneMovFin = model.getListaRegistrazioneMovFin();
		List<ElementoQuotaEntrataRegistrazioneMovFin> listaWrapper = new ArrayList<ElementoQuotaEntrataRegistrazioneMovFin>();
		
		//non considero le quote della cnota credito, ma le quote del doc padre con importo da dedurre != 0. 
		for(SubdocumentoEntrata subdocumento : listaSubdocumento) {
			if(subdocumento.getImportoDaDedurreNotNull().compareTo(BigDecimal.ZERO) != 0){
				ElementoQuotaEntrataRegistrazioneMovFin wrapper = wrapSubdocumento(notaCredito, subdocumento, listaRegistrazioneMovFin);
				listaWrapper.add(wrapper);
			}
			
		}
		//dati iva della nota credito o del doc padre? 
		if(!docPadre.getListaSubdocumentoIva().isEmpty()) {
			// Ho i dati iva sul documento
			impostaDatiIvaSuiWrapperDaDocumento(docPadre, listaWrapper);
		}
		
		model.setListaElementoQuota(listaWrapper);
	}

}
