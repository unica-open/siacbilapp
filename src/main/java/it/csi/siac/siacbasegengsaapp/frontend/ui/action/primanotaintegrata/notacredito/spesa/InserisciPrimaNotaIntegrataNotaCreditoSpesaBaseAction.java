/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.spesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.notacredito.InserisciPrimaNotaIntegrataNotaCreditoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.notacredito.spesa.InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata per la nota di credito.
 * 
 * @author Valentina
 * @version 1.0.0 - 03/03/2016
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseAction<M extends InserisciPrimaNotaIntegrataNotaCreditoSpesaBaseModel>
		extends InserisciPrimaNotaIntegrataNotaCreditoBaseAction<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ElementoQuotaSpesaRegistrazioneMovFin, ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper, M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9012410004301347947L;
	
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_NOTA_CREDITO_SPESA_FIN = "CompletaNotaCreditoSpesaInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_NOTA_CREDITO_SPESA_FIN = "CompletaValidaNotaCreditoSpesaInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_NOTA_CREDITO_SPESA_GSA = "CompletaNotaCreditoSpesaInsPrimaNotaIntegrataGSAModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_NOTA_CREDITO_SPESA_GSA = "CompletaValidaNotaCreditoSpesaInsPrimaNotaIntegrataGSAModel";
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoIvaSpesaService documentoIvaSpesaService;
	
	@Override
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		if(model.getDocumento() == null) {
			// Non ho il documento. Lo prendo dal movimento
			DocumentoSpesa documentoSpesa = (DocumentoSpesa) model.getRegistrazioneMovFin().getMovimento();
			model.setDocumento(documentoSpesa);
		}
		
		DocumentoSpesa notaCredito = retrieveNotaCredito();
		DocumentoSpesa documentoPadre = retrieveDocumentoPadre();
		List<SubdocumentoSpesa> listaQuote = retrieveQuoteDocumento();
		List<SubdocumentoIvaSpesa> listaIva = retrieveDatiIva();
		// TODO?
		List<DettaglioOnere> listaOneri = new ArrayList<DettaglioOnere>();
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper(notaCredito, documentoPadre, listaQuote, listaIva, listaOneri, model.isGestioneUEB()));
	}

	/**
	 * Caricamento della nota di credito
	 * @return la nota
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private DocumentoSpesa retrieveNotaCredito() throws WebServiceInvocationFailureException {
		final String methodName = "retrieveNotaCredito";
		RicercaDettaglioDocumentoSpesa req = model.creaRequestRicercaDettaglioDocumentoSpesa();
		logServiceRequest(req);
		RicercaDettaglioDocumentoSpesaResponse res = documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoSpesa.class, res));
		}
		DocumentoSpesa notaCredito = res.getDocumento();
		if(notaCredito == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento di spesa", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Nessun documento reperito");
		}
		
		log.debug(methodName, "Trovato documento di spesa corrispondente all'uid " + model.getDocumento().getUid());
		model.setDocumento(notaCredito);
		return notaCredito;
	}
	
	/**
	 * Recupera il documento padre
	 * @return il documento padre
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private DocumentoSpesa retrieveDocumentoPadre() throws WebServiceInvocationFailureException {
		final String methodName = "retrieveDocumentoPadre";
		if(model.getDocumento().getListaDocumentiSpesaPadre() == null || model.getDocumento().getListaDocumentiSpesaPadre().isEmpty()){
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento padre della nota di credito", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Impossibile determinare il documento a cui e' associata la nota di credito");
		}
		DocumentoSpesa documentoPadre = model.getDocumento().getListaDocumentiSpesaPadre().get(0);
		RicercaDettaglioDocumentoSpesa reqDP = model.creaRequestRicercaDettaglioDocumentoSpesa(documentoPadre);
		logServiceRequest(reqDP);
		RicercaDettaglioDocumentoSpesaResponse resDP = documentoSpesaService.ricercaDettaglioDocumentoSpesa(reqDP);
		logServiceResponse(resDP);
		
		if(resDP.hasErrori()) {
			addErrori(resDP);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoSpesa.class, resDP));
		}
		if(resDP.getDocumento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento padre della nota di credito", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Nessun documento reperito");
		}
		
		log.debug(methodName, "Trovato documento di spesa corrispondente all'uid " + documentoPadre.getUid());
		model.setDocumentoPadre(resDP.getDocumento());
		return documentoPadre;
	}
	
	/**
	 * Carica le quote del documento.
	 * @param uidDocumento l'id del documento
	 * @returns le quote del documento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoSpesa> retrieveQuoteDocumento() throws WebServiceInvocationFailureException {
		RicercaQuoteByDocumentoSpesa req = model.creaRequestRicercaQuoteByDocumentoSpesa(model.getDocumentoPadre());
		logServiceRequest(req);
		RicercaQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaQuoteByDocumentoSpesa(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaQuoteByDocumentoSpesa.class, res));
		}
		return res.getSubdocumentiSpesa();
	}
	
	/**
	 * Caricamento dei dati dell'IVA.
	 * @param uidDocumento l'id del documento
	 * @return i subdocumenti iva spesa
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoIvaSpesa> retrieveDatiIva() throws WebServiceInvocationFailureException {
		RicercaNoteCreditoIvaDocumentoSpesa req = model.creaRequestRicercaNoteCreditoIvaDocumentoSpesa(model.getDocumentoPadre());
		logServiceRequest(req);
		RicercaNoteCreditoIvaDocumentoSpesaResponse res = documentoIvaSpesaService.ricercaNoteCreditoIvaDocumentoSpesa(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaNoteCreditoIvaDocumentoSpesa.class, res));
		}
		return res.getSubdocumentoIvaSpesa();
	}
	
	@Override
	protected String computeSuffix() {
		return " (doc S)";
	}
	
	@Override
	protected ElementoQuotaSpesaRegistrazioneMovFin wrapSubdocumento(DocumentoSpesa notaCredito, SubdocumentoSpesa subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		return new ElementoQuotaSpesaRegistrazioneMovFin(notaCredito, subdocumento, registrazioneMovFin);
	}

	/**
	 * Wrap per le quote
	 */
	@Override
	protected void wrapQuote() {
		DocumentoSpesa notaCredito = model.getDocumento();
		DocumentoSpesa docPadre = model.getDocumentoPadre();
		List<SubdocumentoSpesa> listaSubdocumento = docPadre.getListaSubdocumenti();
		List<RegistrazioneMovFin> listaRegistrazioneMovFin = model.getListaRegistrazioneMovFin();
		List<ElementoQuotaSpesaRegistrazioneMovFin> listaWrapper = new ArrayList<ElementoQuotaSpesaRegistrazioneMovFin>();
		
		//non considero le quote della cnota credito, ma le quote del doc padre con importo da dedurre != 0. 
		for(SubdocumentoSpesa subdocumento : listaSubdocumento) {
			if(subdocumento.getImportoDaDedurreNotNull().compareTo(BigDecimal.ZERO) != 0){
				ElementoQuotaSpesaRegistrazioneMovFin wrapper = wrapSubdocumento(notaCredito, subdocumento, listaRegistrazioneMovFin);
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
