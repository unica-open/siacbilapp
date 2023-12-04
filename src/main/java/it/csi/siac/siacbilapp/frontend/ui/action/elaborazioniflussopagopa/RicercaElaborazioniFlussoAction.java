/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.elaborazioniflussopagopa;

import java.util.Date;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.pagopa.frontend.webservice.PagoPAService;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioni;
import it.csi.siac.pagopa.frontend.webservice.msg.RicercaElaborazioniResponse;
import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.elaborazioniflussopagopa.RicercaElaborazioniFlussoModel;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe di action per la ricerca elaborazione flusso.
 * 
 * @author Vincenzo Gambino
 * @version 1.0.0 08/07/2020
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaElaborazioniFlussoAction extends GenericBilancioAction<RicercaElaborazioniFlussoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5674484261248203595L;



	@Autowired private transient PagoPAService pagoPAService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Ricerca con operazioni il provvedimento.
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaElaborazioni() {
		final String methodName = "ricercaElaborazioniFlusso";
		
		if (!checkIsValidaRicerca()) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}
		
		if (!checkDateInizioFine(model.getDataInizioEmissione(), model.getDataFineEmissione(), "emissione")) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}
		if (!checkDateInizioFine(model.getDataInizioElaborazioneFlusso(), model.getDataFineElaborazioneFlusso(), "elaborazione")) {
			log.debug(methodName, "Validazione fallita");
			return INPUT;
		}
		
		log.debug(methodName, "Creazione della request");
		RicercaElaborazioni req = model.creaRequestRicercaElaborazioniFlusso();
		
		Elaborazione elaborazione = new Elaborazione();
		if(model.getNumeroProvvisorio()!= null){
			elaborazione.setNumeroProvvisorio(model.getNumeroProvvisorio().toString());
		}
		elaborazione.setFlusso(model.getFlusso());
		req.setDataEmissioneDa(model.getDataInizioEmissione());
		req.setDataEmissioneA(model.getDataFineEmissione());
		req.setDataElaborazioneFlussoDa(model.getDataInizioElaborazioneFlusso());
		req.setDataElaborazioneFlussoA(model.getDataFineElaborazioneFlusso());
		req.setElaborazione(elaborazione);
		// SIAC-8046 CM 09/03/2021 Inizio
		req.setEsitoElaborazioneFlusso(model.getEsitoElaborazioneFlusso());
		// SIAC-8046 CM 09/03/2021 Fine
		logServiceRequest(req);
		RicercaElaborazioniResponse res  = pagoPAService.ricercaElaborazioni(req);
				
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio RicercaElaborazioni");
			addErrori(res);
			return INPUT;
		}		
		
		log.debug(methodName, "Ricerca effettuata con successo");
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ELABORAZIONI_FLUSSO_PAGO_PA, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ELABORAZIONI_FLUSSO_PAGO_PA, res.getElaborazioni());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	
	
	public boolean checkIsValidaRicerca() {
		final String methodName = "checkIsValidaRicerca";
		log.debugStart(methodName, "Verifica campi");
		
		boolean formValido =false;
		
		formValido =  (checkCampoValorizzato(model.getNumeroProvvisorio(), "Numero Provvisorio")
				|| checkStringaValorizzata(model.getFlusso(), "Flusso")
				|| checkCampoValorizzato(model.getDataInizioEmissione(), "Data Emissione Inizio")
				|| checkCampoValorizzato(model.getDataFineEmissione(), "Data Emissione Fine")
				|| checkCampoValorizzato(model.getDataInizioElaborazioneFlusso(), "Data Elaborazione Inizio")
				|| checkCampoValorizzato(model.getDataFineElaborazioneFlusso(), "Data Elaborazione Fine")) 
				// SIAC-8046 CM 09/03/2021 Inizio
				|| 
				(checkCampoValorizzato(model.getEsitoElaborazioneFlusso(), "Esito Elaborazione Flusso") 
						&& 
						(checkCampoValorizzato(model.getNumeroProvvisorio(), "Numero Provvisorio")
						|| checkStringaValorizzata(model.getFlusso(), "Flusso")
						|| checkCampoValorizzato(model.getDataInizioEmissione(), "Data Emissione Inizio")
						|| checkCampoValorizzato(model.getDataFineEmissione(), "Data Emissione Fine")
						|| checkCampoValorizzato(model.getDataInizioElaborazioneFlusso(), "Data Elaborazione Inizio")
						|| checkCampoValorizzato(model.getDataFineElaborazioneFlusso(), "Data Elaborazione Fine"))) 
				// SIAC-8046 CM 09/03/2021 Fine
 				;
		
		
		checkCondition(formValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		return !hasErrori();
	}
	
	private boolean checkDateInizioFine(Date parsedTimeDa, Date parsedTimeA, String tipo) {
		
		boolean error = true;
		if (parsedTimeDa==null && parsedTimeA!=null) {
			checkCondition(false,ErroreCore.VALORE_NON_CONSENTITO.getErrore(String.format("Data %s", tipo), "(non e' possibile inserire Data fine senza Data inizio)"));
			error=  !hasErrori();
		}

		if (parsedTimeDa!=null && parsedTimeA!=null && parsedTimeDa.after(parsedTimeA)) {
			checkCondition(false,ErroreCore.VALORE_NON_CONSENTITO.getErrore(String.format("Data %s", tipo), "(Data inizio deve essere minore di Data fine)"));
			error = !hasErrori();
		}
		
		return error;
		
	}
	
	

}
