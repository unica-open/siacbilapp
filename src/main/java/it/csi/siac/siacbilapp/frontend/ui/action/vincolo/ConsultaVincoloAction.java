/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.vincolo;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.model.vincolo.ConsultaVincoloModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * Classe di Action per la gestione della consultazione del Vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaVincoloAction extends GenericVincoloAction<ConsultaVincoloModel> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		log.debug(methodName, "Ricerco il vincolo");
		RicercaDettaglioVincolo request = model.creaRequestRicercaDettaglioVincolo();
		logServiceRequest(request);
		
		RicercaDettaglioVincoloResponse response = vincoloCapitoloService.ricercaDettaglioVincolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Vincolo ottenuto");
		
		List<Capitolo<?, ?>> listaCapitoliEntrata = null;
		List<Capitolo<?, ?>> listaCapitoliUscita = null;
		
		try {
			listaCapitoliEntrata = ricercaCapitoliEntrata(response);
			listaCapitoliUscita = ricercaCapitoliUscita(response);
		} catch(ApplicationException e) {
			log.error(methodName, e.getMessage());
			return INPUT;
		}
		
		// Imposto i dati nel model
		model.impostaDati(response, listaCapitoliEntrata, listaCapitoliUscita);
		
		return SUCCESS;
	}
	
	/**
	 * Effettua una consultazione del vincolo per il caso d'uso della consultazione del capitolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 * @throws FrontEndBusinessException in caso di eccezione nell'invocazione del metodo <code>execute</code>
	 */
	public String consultaVincoloPerCapitolo() throws FrontEndBusinessException {
		final String methodName = "consultaVincoloPerCapitolo";
		try {
			execute();
		} catch(Exception e) {
			log.debug(methodName, "Exception in execute: " + e.getMessage());
			throw new FrontEndBusinessException(e);
		}
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
