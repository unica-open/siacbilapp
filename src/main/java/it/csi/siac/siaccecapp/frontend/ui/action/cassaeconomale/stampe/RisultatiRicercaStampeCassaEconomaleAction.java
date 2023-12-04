/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe.RisultatiRicercaStampeCassaEconomaleModel;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccorser.frontend.webservice.FileService;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFileResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;

/**
 * Action per i risultati di ricerca delle StampeCassaFile
 * 
 * @author Valentina Triolo
 * @version 1.0.0 - 01/04/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStampeCassaEconomaleAction extends GenericBilancioAction<RisultatiRicercaStampeCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6340247648209697108L;
	
	@Autowired private transient FileService fileService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart);
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		// SIAC-4799
		leggiTipoDocumentoDaSessione();
		
		return SUCCESS;
	}

	/**
	 * Lettura del tipo di documento dalla sessione
	 */
	private void leggiTipoDocumentoDaSessione() {
		TipoDocumento tipoDocumento = sessionHandler.getParametro(BilSessionParameter.TIPO_DOCUMENTO_STAMPA_CEC);
		model.setTipoDocumento(tipoDocumento);
	}

	/**
	 * Download del file
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String download() {
		final String methodName = "download";
		if(!isValidoDownload()) {
			return INPUT;
		}
		// Il file e' valido
		log.debug(methodName, "Download per il file con uid " + model.getUidFile());
		RicercaFile request = model.creaRequestRicercaFile();
		logServiceRequest(request);
		RicercaFileResponse response = fileService.ricercaFile(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaFile.class, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getElencoPaginato() == null || response.getElencoPaginato().isEmpty()) {
			log.debug(methodName, "Nessun file da scaricare");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		//Creazione dei dati per il download
		// Prendo il primo file
		File file = response.getElencoPaginato().get(0);
		model.setContentType(file.getMimeType());
		model.setContentLength(file.getDimensione());
		model.setFileName(file.getNome());
		
		InputStream inputStream = new ByteArrayInputStream(file.getContenuto());
		model.setInputStream(inputStream);
		
		log.debug(methodName, "Caricamento dati per download effettuato. Download in corso...");
		return SUCCESS;
	}

	/**
	 * Controlla se il download sia effettuabile.
	 * 
	 * @return <code>true</code> se il download pu&ograve; essere processato; <code>false</code> in caso contrario
	 */
	private boolean isValidoDownload() {
		boolean isUidValorizzato = model.getUidFile() != null && model.getUidFile().intValue() != 0;
		checkCondition(isUidValorizzato, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("File da scaricare"));
		return isUidValorizzato;
	}

}
