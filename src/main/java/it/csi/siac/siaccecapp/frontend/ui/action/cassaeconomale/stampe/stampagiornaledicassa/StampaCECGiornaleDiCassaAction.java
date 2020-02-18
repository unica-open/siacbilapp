/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe.stampagiornaledicassa;

import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe.GenericStampaCECAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe.StampaCECGiornaleCassaModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassaResponse;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccorser.model.Errore;

/**
 * Classe di action generica per le stampa della CEC.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 31/03/2015
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StampaCECGiornaleDiCassaAction extends GenericStampaCECAction<StampaCECGiornaleCassaModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3374603770178681584L;
	
		/* (non-Javadoc)
	 * @see it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe.GenericStampaCECAction#prepare()
	 */
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		super.execute();
		// Controllo se l'azione precedente sia stata un successo
		leggiEventualiInformazioniAzionePrecedente();
		// Controllo se vi siano stati messaggi dall'azione precedente
		leggiEventualiMessaggiAzionePrecedente();
		// Controllo se vi siano stati errori dall'azione precedente
		leggiEventualiErroriAzionePrecedente();
		
		 model.impostaDatiNelModel();
		return SUCCESS;
		
	}
	
	/**
	 * Caricamento dell'ultima stampa definitiva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaUltimaDefinitiva() {
		RicercaUltimaStampaDefinitivaGiornaleCassa request = model.creaRequestRicercaDatiUltimaStampaDefinitivaGiornaleCassa();
		logServiceRequest(request);
		RicercaUltimaStampaDefinitivaGiornaleCassaResponse response=stampaCassaEconomaleService.ricercaDatiUltimaStampaDefinitivaGiornaleCassa(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info("caricaUltimaData", createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		if(response.getStampeCassaFile() !=null && response.getStampeCassaFile().getStampaGiornale() !=null){
		 model.setStampaGiornale(response.getStampeCassaFile().getStampaGiornale());
		}
		return SUCCESS;
		
	}


	/**
	 * effettua i controlli neccessari richiesti dall'analisi prima di effettuare la stampa
	 */
	public void validaStampaGiornaleCassa() {
		cleanErroriMessaggiInformazioni();
		final String methodName = "validaStampaGiornaleCassa";
		log.debug(methodName, " verifico parametri in corso");
		checkNotNull(model.getDataDaElaborare(),"Data da elaborare");
		checkNotNull(model.getTipoStampa(),"Tipo stampa");
		
		// se il tipo stampa e' definitivo e la data da elaborare > data ultima stampa ---> errore 
		if(TipoStampa.DEFINITIVA.equals(model.getTipoStampa())
				&& model.getDataUltimaStampaDef() != null
				&& DateUtils.truncate(model.getDataUltimaStampaDef(),Calendar.DAY_OF_MONTH)
						.compareTo(DateUtils.truncate(model.getDataDaElaborare(),Calendar.DAY_OF_MONTH)) >= 0) {
			log.error(methodName, " data da elaborare < data ultima stampa");
			Errore errore = ErroreCEC.CEC_ERR_0019.getErrore();
			addErrore(errore);
		}
		
		if(hasErrori()) {
			addErrore(ErroreCEC.CEC_ERR_0018.getErrore());
		}
	}
	/**
	 * STAMPA il giornale di cassa 
	 * @return una strinfga corrispondente al risultato dell'invocazione
	 */
	public String stampaGiornaleCassa() {
		
		final String methodName = "stampaGiornaleCassa";
		log.debug(methodName, " STAMPA IN CORSO....");
	
		StampaGiornaleCassa request = model.creaRequestStampaGiornaleCassa();
		logServiceRequest(request);
		StampaGiornaleCassaResponse response = stampaCassaEconomaleService.stampaGiornaleCassa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio StampaStampaGiornaleCassa avvenuta con successo");
		impostaMessaggioStampaPresaInCarico();
		
		model.setFlagStampaEffettuata(Boolean.TRUE);
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * viene richiamata dal javascript per ottenere la lista degli errori 
	 * @return una strinfga corrispondente al risultato dell'invocazione
	 */
	public String stampaGiornaleCassaValidazione() {	
		final String methodName = "stampaGiornaleCassaValidazione";
		log.debug(methodName, "validazione dati di stampa In corso ");
	
		validaStampaGiornaleCassa();
		return SUCCESS;
	}

	
}


