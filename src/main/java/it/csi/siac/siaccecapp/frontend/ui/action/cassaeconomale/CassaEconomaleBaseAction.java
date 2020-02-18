/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

/**
 * Classe di action base per la gestione della cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/12/2014
 * @param <M> la tipizzazione del model
 *
 */
public class CassaEconomaleBaseAction<M extends CassaEconomaleBaseModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1552929850391130589L;
	
	/** Serviz&icirc; della cassa economale */
	@Autowired protected transient CassaEconomaleService cassaEconomaleService;
	
	/**
	 * Ottiene la lista delle casse economali a partire dalle azioni consentite all'utente.
	 * 
	 * @return la lista delle casse economali gestibili
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected List<CassaEconomale> ottieniListaCasseEconomali() throws WebServiceInvocationFailureException {
		// JIRA 2623
		final String methodName = "ottieniListaCasseEconomali";
		RicercaSinteticaCassaEconomale request = model.creaRequestRicercaSinteticaCassaEconomale();
		logServiceRequest(request);
		RicercaSinteticaCassaEconomaleResponse response = cassaEconomaleService.ricercaSinteticaCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		log.debug(methodName, "Ottenute casse economali dal servizio: " + response.getCardinalitaComplessiva());
		
		return response.getListaCasseEconomali();
	}
	
	/**
	 * Calcolo degli importi della cassa economale.
	 * 
	 * @return la cassa economale con gli importi calcolati
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected CassaEconomale calcolaImportiCassaEconomale() throws WebServiceInvocationFailureException {
		return calcolaImportiCassaEconomale(model.getCassaEconomale());
	}
	
	/**
	 * Calcolo degli importi della cassa economale.
	 * 
	 * @param cassaEconomale la cassa da cui calcolare gli importi
	 * 
	 * @return la cassa economale con gli importi calcolati
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected CassaEconomale calcolaImportiCassaEconomale(CassaEconomale cassaEconomale) throws WebServiceInvocationFailureException {
		final String methodName = "calcolaImportiCassaEconomale";
		
		CalcolaDisponibilitaCassaEconomale request = model.creaRequestCalcolaDisponibilitaCassaEconomale(cassaEconomale);
		logServiceRequest(request);
		CalcolaDisponibilitaCassaEconomaleResponse response = cassaEconomaleService.calcolaDisponibilitaCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String msg = createErrorInServiceInvocationString(request, response);
			log.info(methodName, msg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(msg);
		}
		
		return response.getCassaEconomale();
	}

	/**
	 * Ricerca il dettaglio della cassa economale.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nella ricerca
	 */
	protected void ricercaDettaglioCassaEconomale() throws WebServiceInvocationFailureException{
		final String methodName = "ricercaDettaglioCassaEconomale";
		
		RicercaDettaglioCassaEconomale request = model.creaRequestRicercaDettaglioCassaEconomale();
		logServiceRequest(request);
		RicercaDettaglioCassaEconomaleResponse response = cassaEconomaleService.ricercaDettaglioCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = "Errori nell'invocazione della ricerca di dettaglio per la cassa economale con uid " + model.getCassaEconomale().getUid();
			log.debug(methodName, errorMessage);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMessage);
		}
		// La cassa e' stata reperita
		CassaEconomale cassaEconomale = response.getCassaEconomale();
		// Imposto la cassa in sessione
		sessionHandler.setParametro(BilSessionParameter.CASSA_ECONOMALE, cassaEconomale);
		model.setCassaEconomale(cassaEconomale);
	}
	
}
