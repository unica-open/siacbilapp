/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
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

/**
 * Classe base di consultazione per la registrazione movfin sulla nota di credito di entrata.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2015
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseAction<M extends ConsultaRegistrazioneMovFinNotaCreditoEntrataBaseModel> extends GenericBilancioAction<M>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 2184230969321671983L;
	
	@Autowired 	private transient DocumentoEntrataService documentoEntrataService;
	@Autowired 	private transient DocumentoIvaEntrataService documentoIvaEntrataService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";

		log.debug(methodName, "Ricerco il documento per uid " + model.getUidDocumento());
		
		DocumentoEntrata notaCredito;
		DocumentoEntrata documentoOriginale;
		List<SubdocumentoEntrata> listaQuote;
		List<SubdocumentoIvaEntrata> listaIva;
		
		try {
			notaCredito = ricercaDettaglioDocumentoEntrata(model.getUidDocumento().intValue());
			int originalUid = retrieveOriginalUid(notaCredito);
			
			// Devo ottenere i dati del documento padre
			documentoOriginale = ricercaDettaglioDocumentoEntrata(originalUid);
			
			listaQuote = retrieveQuoteDocumento(originalUid);
			listaIva = retrieveDatiIva(originalUid);
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinNotaCreditoEntrataHelper(notaCredito, documentoOriginale, listaQuote, listaIva, model.isGestioneUEB()));

		return SUCCESS;

	}
	
	/**
	 * Ricerca il dettaglio del documento di entrata per dato id.
	 * 
	 * @param uidDocumento l'id del documento
	 * @return la response del servizio
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private DocumentoEntrata ricercaDettaglioDocumentoEntrata(int uidDocumento) throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumentoEntrata";
		RicercaDettaglioDocumentoEntrata req = model.creaRequestRicercaDettaglioDocumentoEntrata(uidDocumento);
		logServiceRequest(req);

		RicercaDettaglioDocumentoEntrataResponse res = documentoEntrataService.ricercaDettaglioDocumentoEntrata(req);
		logServiceResponse(res);

		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoEntrata.class, res));
		}

		log.debug(methodName, "Documento ottenuto per uid " + uidDocumento);
		return res.getDocumento();
	}

	/**
	 * Carica le quote del documento.
	 * @return le quote
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoEntrata> retrieveQuoteDocumento(int uidDocumento) throws WebServiceInvocationFailureException {
		RicercaQuoteByDocumentoEntrata req = model.creaRequestRicercaQuoteByDocumentoEntrata(uidDocumento);
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
	 * @return i subdocumenti iva
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoIvaEntrata> retrieveDatiIva(int uidDocumento) throws WebServiceInvocationFailureException {
		RicercaNoteCreditoIvaDocumentoEntrata req = model.creaRequestRicercaNoteCreditoIvaDocumentoEntrata(uidDocumento);
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
	
	/**
	 * Trova il documento originale
	 * @param documento il documento
	 * @return l'uid del documento originale
	 * @throws WebServiceInvocationFailureException nel caso non vi sia un padre
	 */
	private int retrieveOriginalUid(DocumentoEntrata documento) throws WebServiceInvocationFailureException {
		List<DocumentoEntrata> listaDocumentiEntrataPadre = documento.getListaDocumentiEntrataPadre();
		if(listaDocumentiEntrataPadre == null || listaDocumentiEntrataPadre.isEmpty() || listaDocumentiEntrataPadre.get(0) == null) {
			throw new WebServiceInvocationFailureException("Il documento deve avere un padre per essere considerato");
		}
		return listaDocumentiEntrataPadre.get(0).getUid();
	}

}
