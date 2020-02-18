/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.InserisciCapitoloUscitaPrevisioneModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciImportiStanziamentiUscPreAction extends CapitoloUscitaAction<InserisciCapitoloUscitaPrevisioneModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113246472166684092L;
	
	@Override
	public void prepare() throws Exception {

		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		// Carico eventualmente il modello dalla sessione
		caricaModelDaSessioneSePresente();
		inizializzaDatiVariazione();
		
		log.debugEnd(methodName, "");
	
	}
	
	/**
	 * Carica il model da quello presente in sessione.
	 */
	private void caricaModelDaSessioneSePresente() {
		final String methodName = "caricaModelDaSessioneSePresente";
		
		InserisciCapitoloUscitaPrevisioneModel modelInSessione = sessionHandler.getParametro(BilSessionParameter.MODEL_INSERIMENTO_USCITA_PREVISIONE);
		if(modelInSessione != null) {
			log.debug(methodName, "Model presente in sessione. Copia dei dati");
			
			// Sostituire il model attuale con quello ottenuto dalla sessione
			model = modelInSessione;
			// Pulire la sessione
			log.debug(methodName, "Pulisco la sessione");
			sessionHandler.setParametro(BilSessionParameter.MODEL_INSERIMENTO_USCITA_PREVISIONE, null);
			// Caricare le liste derivate
			log.debug(methodName, "Caricamento eventuali liste derivate");
			caricaListaCodificheAggiornamento();
		}
	}
	
	private void inizializzaDatiVariazione() {
		Boolean daVariazione = sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, null);
		model.setDaVariazione(daVariazione);
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		log.info("model", model);
		
		log.info("exevute", "execute");
		
		
		return SUCCESS;
	}
	

}
