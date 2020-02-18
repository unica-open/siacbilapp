/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinDocumentoSpesaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoSpesaHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Classe base di consultazione per la registrazione movfin sul documento.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 06/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinDocumentoSpesaBaseAction<M extends ConsultaRegistrazioneMovFinDocumentoSpesaBaseModel> extends GenericBilancioAction<M>{

	/** Per la serializzazione */
	private static final long serialVersionUID = 2184230969321671983L;
	
	@Autowired
	private transient DocumentoSpesaService documentoSpesaService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";

		DocumentoSpesa documento;
		List<SubdocumentoSpesa> listaQuote;
		List<DettaglioOnere> datiOnere;
		try {
			documento = retrieveDocumento();
			model.setDocumento(documento);
			listaQuote = retrieveSubdocumento();
			datiOnere = retrieveRitenuteDocumento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}

		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinDocumentoSpesaHelper(documento, listaQuote, datiOnere, model.isGestioneUEB()));

		return SUCCESS;

	}

	/**
	 * Caricamento del documento
	 * @return il documento
	 * @throws WebServiceInvocationFailureException nel caso in cui il caricamento del documento fallisca
	 */
	private DocumentoSpesa retrieveDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "retrieveDocumento";
		log.debug(methodName, "Ricerco il documento per uid " + model.getUidDocumento());

		RicercaDettaglioDocumentoSpesa req = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumento());
		logServiceRequest(req);

		RicercaDettaglioDocumentoSpesaResponse res = documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
		logServiceResponse(res);

		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}

		log.debug(methodName, "Documento ottenuto per uid " + model.getUidDocumento());
		return res.getDocumento();
	}

	/**
	 * Carica le quote del documento.
	 * @return la lista delle quote
	 * @throws WebServiceInvocationFailureException nel caso di fallimento nell'invocazione del servizio
	 */
	private List<SubdocumentoSpesa> retrieveSubdocumento() throws WebServiceInvocationFailureException{
		RicercaQuoteByDocumentoSpesa req = model.creaRequestRicercaQuoteByDocumentoSpesa();
		RicercaQuoteByDocumentoSpesaResponse res = documentoSpesaService.ricercaQuoteByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		return res.getSubdocumentiSpesa();
	}

	/**
	 * Carica le ritenute del documento.
	 * @return la lista dei dettagli
	 * @throws WebServiceInvocationFailureException 
	 */
	private List<DettaglioOnere> retrieveRitenuteDocumento() throws WebServiceInvocationFailureException {
		RicercaOnereByDocumentoSpesa req = model.creaRequestRicercaOnereByDocumentoSpesa();
		RicercaOnereByDocumentoSpesaResponse res = documentoSpesaService.ricercaOnereByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		return res.getListaDettagliOnere();
	}

}
