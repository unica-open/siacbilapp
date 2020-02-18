/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.stampe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccorser.frontend.webservice.FileService;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFileResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.stampe.RisultatiRicercaStampaAllegatoAttoModel;
/**
 * Action per i risultati di ricerca della Stampa allegato atto
 * 
 * @author Elisa Chiaru
 * @version 1.0.0 - 30/12/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStampaAllegatoAttoAction extends GenericBilancioAction<RisultatiRicercaStampaAllegatoAttoModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -6283691149246196284L;
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
		
		return SUCCESS;
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
			log.info(methodName, createErrorInServiceInvocationString(request, response));
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
