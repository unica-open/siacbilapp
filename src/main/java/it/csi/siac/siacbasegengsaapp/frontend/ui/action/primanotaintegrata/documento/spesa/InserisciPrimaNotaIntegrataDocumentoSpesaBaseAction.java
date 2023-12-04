/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.spesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento.InserisciPrimaNotaIntegrataDocumentoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.spesa.InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaSpesaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoSpesaHelper;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata per il documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataDocumentoSpesaBaseAction<M extends InserisciPrimaNotaIntegrataDocumentoSpesaBaseModel>
		extends InserisciPrimaNotaIntegrataDocumentoBaseAction<DocumentoSpesa, SubdocumentoSpesa, SubdocumentoIvaSpesa, ElementoQuotaSpesaRegistrazioneMovFin, ConsultaRegistrazioneMovFinDocumentoSpesaHelper, M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9012410004301347947L;
	
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_DOCUMENTO_SPESA_FIN = "CompletaDocumentoSpesaInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_DOCUMENTO_SPESA_FIN = "CompletaValidaDocumentoSpesaInsPrimaNotaIntegrataFINModel";
	/** Nome del modello in sessione per l'inserimento del documento di entrata */
	public static final String MODEL_COMPLETA_DOCUMENTO_SPESA_GSA = "CompletaDocumentoSpesaInsPrimaNotaIntegrataGSAModel";
	/** Nome del modello in sessione per l'inserimento e la validazione del documento di entrata */
	public static final String MODEL_COMPLETA_E_VALIDA_DOCUMENTO_SPESA_GSA = "CompletaValidaDocumentoSpesaInsPrimaNotaIntegrataGSAModel";
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioDocumento";
		
		DocumentoSpesa documento = retrieveDocumentoSpesa();
		List<SubdocumentoSpesa> listaQuote = documento.getListaSubdocumenti();
		
		log.debug(methodName, "Trovato documento di spesa corrispondente all'uid " + model.getDocumento().getUid());
		model.setDocumento(documento);
		
		// Caricamento oneri
		List<DettaglioOnere> listaOneri = caricaOneri();
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinDocumentoSpesaHelper(documento, listaQuote, listaOneri, model.isGestioneUEB()));
	}

	/**
	 * Caricamento del documento di spesa
	 * @return il documento
	 * @throws WebServiceInvocationFailureException in caso di eccezione nel caricamento dei dati da servizio
	 */
	private DocumentoSpesa retrieveDocumentoSpesa() throws WebServiceInvocationFailureException {
		if(model.getDocumento() == null) {
			// Non ho il documento. Lo prendo dal movimento
			DocumentoSpesa documentoSpesa = (DocumentoSpesa) model.getRegistrazioneMovFin().getMovimento();
			model.setDocumento(documentoSpesa);
		}
		
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
		DocumentoSpesa documento = res.getDocumento();
		if(documento == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento di spesa", "uid " + model.getDocumento().getUid()));
			throw new WebServiceInvocationFailureException("Nessun documento reperito");
		}
		return documento;
	}
	
	/**
	 * Caricamento degli oneri
	 * @return gli oneri
	 */
	private List<DettaglioOnere> caricaOneri() throws WebServiceInvocationFailureException {
		RicercaOnereByDocumentoSpesa req = model.creaRequestRicercaOnereByDocumentoSpesa();
		RicercaOnereByDocumentoSpesaResponse res = documentoSpesaService.ricercaOnereByDocumentoSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaOnereByDocumentoSpesa.class, res));
		}
		return res.getListaDettagliOnere();
	}

	@Override
	protected String computeSuffix() {
		return " (doc S)";
	}
	
	@Override
	protected ElementoQuotaSpesaRegistrazioneMovFin wrapSubdocumento(SubdocumentoSpesa subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		return new ElementoQuotaSpesaRegistrazioneMovFin(subdocumento, registrazioneMovFin);
	}

}
