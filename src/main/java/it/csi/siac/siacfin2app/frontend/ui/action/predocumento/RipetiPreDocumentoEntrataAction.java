/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.RipetiPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * Classe di action per la ripetizione del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/04/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RipetiPreDocumentoEntrataAction extends BaseInserimentoPreDocumentoEntrataAction<RipetiPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1021580050844953921L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		final String methodName = "execute";
		// Caricamento valori default
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		checkCasoDUsoApplicabile("Ripetizione preDocumento di entrata");
		
		// Inizializzo l'anagrafica
		DatiAnagraficiPreDocumento datiAnagraficiPreDocumento = new DatiAnagraficiPreDocumento();
		datiAnagraficiPreDocumento.setNazioneNascita(BilConstants.DESCRIZIONE_ITALIA.getConstant());
		datiAnagraficiPreDocumento.setNazioneIndirizzo(BilConstants.DESCRIZIONE_ITALIA.getConstant());
		
		model.setDatiAnagraficiPreDocumento(datiAnagraficiPreDocumento);
		model.setDoLoadCausale(true);
		
		try {
			ottieniDatiDaRipetere();
			caricaListaCausaleEntrata();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
		
		return SUCCESS;
	}

	/**
	 * Ottenimento dei dati da ripetere
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void ottieniDatiDaRipetere() throws WebServiceInvocationFailureException {
		final String methodName = "ottieniDatiDaRipetere";
		RicercaDettaglioPreDocumentoEntrata req = model.creaRequestRicercaDettaglioPreDocumentoEntrata();
		RicercaDettaglioPreDocumentoEntrataResponse res = preDocumentoEntrataService.ricercaDettaglioPreDocumentoEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		PreDocumentoEntrata pde = res.getPreDocumentoEntrata();
		log.debug(methodName, "Ottengo i dati da ripetere");
		
		PreDocumentoEntrata preDocumento = new PreDocumentoEntrata();
		preDocumento.setDataCompetenza(pde.getDataCompetenza());
		preDocumento.setPeriodoCompetenza(pde.getPeriodoCompetenza());
		preDocumento.setDataDocumento(pde.getDataDocumento());
		preDocumento.setCausaleEntrata(pde.getCausaleEntrata());
		
		model.setPreDocumento(preDocumento);
		model.setTipoCausale(pde.getCausaleEntrata().getTipoCausale());
		model.setCausaleEntrata(pde.getCausaleEntrata());
		model.setContoCorrente(pde.getContoCorrente());
	}
	
}
