/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
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

/**
 * Classe base di consultazione per la registrazione movfin sulla nota di credito di spesa.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/01/2015
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseAction<M extends ConsultaRegistrazioneMovFinNotaCreditoSpesaBaseModel> extends GenericBilancioAction<M>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 2184230969321671983L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoIvaSpesaService documentoIvaSpesaService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";

		log.debug(methodName, "Ricerco il documento per uid " + model.getUidDocumento());
		
		DocumentoSpesa notaCredito;
		DocumentoSpesa documentoOriginale;
		List<SubdocumentoSpesa> listaQuote;
		List<SubdocumentoIvaSpesa> listaIva;
		// TODO?
		List<DettaglioOnere> datiOnere = new ArrayList<DettaglioOnere>();
		try {
			notaCredito = ricercaDettaglioDocumentoSpesa(model.getUidDocumento().intValue());
			int originalUid = retrieveOriginalUid(notaCredito);
			
			// Devo ottenere i dati del documento padre
			documentoOriginale = ricercaDettaglioDocumentoSpesa(originalUid);
			
			listaQuote = retrieveQuoteDocumento(originalUid);
			//datiOnere = retrieveRitenuteDocumento(documentoOriginale.getUid());
			listaIva = retrieveDatiIva(originalUid);
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinNotaCreditoSpesaHelper(notaCredito, documentoOriginale, listaQuote, listaIva, datiOnere, model.isGestioneUEB()));

		return SUCCESS;

	}
	
	/**
	 * Ricerca il dettaglio del documento di spesa per dato id.
	 * 
	 * @param uidDocumento l'id del documento
	 * @return la response del servizio
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private DocumentoSpesa ricercaDettaglioDocumentoSpesa(int uidDocumento) throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumentoSpesa";
		RicercaDettaglioDocumentoSpesa req = model.creaRequestRicercaDettaglioDocumentoSpesa(uidDocumento);
		logServiceRequest(req);

		RicercaDettaglioDocumentoSpesaResponse res = documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
		logServiceResponse(res);

		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoSpesa.class, res));
		}

		log.debug(methodName, "Documento ottenuto per uid " + uidDocumento);
		return res.getDocumento();
	}

	/**
	 * Carica le quote del documento.
	 * @param uidDocumento l'id del documento
	 * @returns le quote del documento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoSpesa> retrieveQuoteDocumento(int uidDocumento) throws WebServiceInvocationFailureException {
		RicercaQuoteByDocumentoSpesa req = model.creaRequestRicercaQuoteByDocumentoSpesa(uidDocumento);
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
	private List<SubdocumentoIvaSpesa> retrieveDatiIva(int uidDocumento) throws WebServiceInvocationFailureException {
		RicercaNoteCreditoIvaDocumentoSpesa req = model.creaRequestRicercaNoteCreditoIvaDocumentoSpesa(uidDocumento);
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

	/**
	 * Trova il documento originale
	 * @param documento il documento
	 * @return l'uid del documento originale
	 * @throws WebServiceInvocationFailureException nel caso non vi sia un padre
	 */
	private int retrieveOriginalUid(DocumentoSpesa documento) throws WebServiceInvocationFailureException {
		List<DocumentoSpesa> listaDocumentiSpesaPadre = documento.getListaDocumentiSpesaPadre();
		if(listaDocumentiSpesaPadre == null || listaDocumentiSpesaPadre.isEmpty() || listaDocumentiSpesaPadre.get(0) == null) {
			throw new WebServiceInvocationFailureException("Il documento deve avere un padre per essere considerato");
		}
		return listaDocumentiSpesaPadre.get(0).getUid();
	}
}
