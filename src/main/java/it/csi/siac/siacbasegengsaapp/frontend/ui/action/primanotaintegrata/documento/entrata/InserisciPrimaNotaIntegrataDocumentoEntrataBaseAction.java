/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.entrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.InserisciPrimaNotaIntegrataDocumentoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.entrata.InserisciPrimaNotaIntegrataDocumentoEntrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaEntrataRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoEntrataHelper;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe di action per l'inserimento della prima integrata per il documento di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataDocumentoEntrataBaseAction<M extends InserisciPrimaNotaIntegrataDocumentoEntrataBaseModel>
		extends InserisciPrimaNotaIntegrataDocumentoBaseAction<DocumentoEntrata, SubdocumentoEntrata, SubdocumentoIvaEntrata, ElementoQuotaEntrataRegistrazioneMovFin, ConsultaRegistrazioneMovFinDocumentoEntrataHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4041673032589900636L;
	
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_DOCUMENTO_ENTRATA_FIN = "CompletaDocumentoEntrataInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_DOCUMENTO_ENTRATA_FIN = "CompletaValidaDocumentoEntrataInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_DOCUMENTO_ENTRATA_GSA = "CompletaDocumentoEntrataInsPrimaNotaIntegrataGSAModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_DOCUMENTO_ENTRATA_GSA = "CompletaValidaDocumentoEntrataInsPrimaNotaIntegrataGSAModel";
	
	/** Serviz&icirc; del documento di entrata */
	@Autowired protected transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumento";
		
		if(model.getDocumento() == null) {
			// Non ho il documento. Lo prendo dal movimento
			DocumentoEntrata documentoEntrata = (DocumentoEntrata) model.getRegistrazioneMovFin().getMovimento();
			model.setDocumento(documentoEntrata);
		}
		
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
		DocumentoEntrata documento = res.getDocumento();
		if(documento == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento di entrata", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Nessun documento reperito");
		}
		
		List<SubdocumentoEntrata> listaQuote = documento.getListaSubdocumenti();
		
		log.debug(methodName, "Trovato documento di entrata corrispondente all'uid " + model.getDocumento().getUid());
		model.setDocumento(documento);
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinDocumentoEntrataHelper(documento, listaQuote, model.isGestioneUEB()));
	}
	
	@Override
	protected String computeSuffix() {
		return " (doc E)";
	}
	
	@Override
	protected ElementoQuotaEntrataRegistrazioneMovFin wrapSubdocumento(SubdocumentoEntrata subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		return new ElementoQuotaEntrataRegistrazioneMovFin(subdocumento, registrazioneMovFin);
	}

}
