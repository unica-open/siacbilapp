/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.stampa;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.frontend.webservice.FileService;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaFileResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa.RisultatiRicercaStampaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

/**
 * Action per i risultati di ricerca della Stampa Iva
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/01/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaStampaIvaAction extends GenericBilancioAction<RisultatiRicercaStampaIvaModel> {

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
		
		// Impostazione flags
		impostazioneFlagsRegistroPeriodo();
		
		return SUCCESS;
	}

	/**
	 * Imposto i flag del registro e del periodo.
	 */
	private void impostazioneFlagsRegistroPeriodo() {
		final String methodName = "impostazioneFlagsRegistroPeriodo";
		RicercaSinteticaStampaIva request = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_STAMPA_IVA, RicercaSinteticaStampaIva.class);
		TipoStampaIva tipoStampaIva = request.getStampaIva().getTipoStampaIva();
		log.debug(methodName, "Tipo di stampa iva: " + tipoStampaIva);
		
		// Il registro lo vedo solo nella stampa registro
		boolean registroVisibile = TipoStampaIva.REGISTRO.equals(tipoStampaIva);
		log.debug(methodName, "Il registro e' visibile? " + registroVisibile);
		// Il periodo lo vedo nella stampa registro e liquidazione
		boolean periodoVisibile = TipoStampaIva.REGISTRO.equals(tipoStampaIva) || TipoStampaIva.LIQUIDAZIONE_IVA.equals(tipoStampaIva);
		log.debug(methodName, "Il periodo e' visibile? " + periodoVisibile);
		
		// Imposto i dati nel model
		model.setRegistroIvaVisibile(registroVisibile);
		model.setPeriodoVisibile(periodoVisibile);
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
