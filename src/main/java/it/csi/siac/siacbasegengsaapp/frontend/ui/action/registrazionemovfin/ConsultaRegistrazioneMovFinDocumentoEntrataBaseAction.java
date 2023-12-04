/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoEntrataHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Consultazione della registrazione del movimento finanziario per il documento di entrata. classe base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinDocumentoEntrataBaseAction<M extends ConsultaRegistrazioneMovFinDocumentoEntrataBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -216662288538725480L;
	
	@Autowired 
	private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		log.debug(methodName, "Ricerco il documento");
		
		DocumentoEntrata documento;
		List<SubdocumentoEntrata> listaQuote;
		try {
			documento = retrieveDocumento();
			model.setDocumento(documento);
			listaQuote = retrieveSubdocumento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinDocumentoEntrataHelper(documento, listaQuote, model.isGestioneUEB()));
		
		return SUCCESS;
	}

	/**
	 * Caricamento del documento
	 * @return il documento
	 * @throws WebServiceInvocationFailureException nel caso in cui il caricamento del documento fallisca
	 */
	private DocumentoEntrata retrieveDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "retrieveDocumento";
		RicercaDettaglioDocumentoEntrata req = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumento());
		logServiceRequest(req);
		
		RicercaDettaglioDocumentoEntrataResponse res = documentoEntrataService.ricercaDettaglioDocumentoEntrata(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioDocumentoEntrata.class, res));
		}
		
		log.debug(methodName, "Documento ottenuto");
		return res.getDocumento();
	}
	
	/**
	 * Carica le quote del documento.
	 * @return la lista delle quote
	 * @throws WebServiceInvocationFailureException nel caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoEntrata> retrieveSubdocumento() throws WebServiceInvocationFailureException {
		RicercaQuoteByDocumentoEntrata req = model.creaRequestRicercaQuoteByDocumentoEntrata();
		RicercaQuoteByDocumentoEntrataResponse res = documentoEntrataService.ricercaQuoteByDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaQuoteByDocumentoEntrata.class, res));
		}
		return res.getSubdocumentiEntrata();
	}
	
}
