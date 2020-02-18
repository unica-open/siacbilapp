/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.ConsultaVariazioneCodificheModel;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Classe di Action per la gestione della consultazione della Variazione Codifiche.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 05/11/2013
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaVariazioneCodificheAction extends GenericBilancioAction<ConsultaVariazioneCodificheModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	@Autowired private transient ProvvedimentoService provvedimentoService;

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}
	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		/* Effettuo la ricerca di dettaglio del Capitolo */
		log.debug(methodName, "Creazione della request");
		if(model.getUidVariazione()==null) {
			log.debug(methodName, "Errore: non presente uid variazione da consultare");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}
		
		RicercaDettaglioVariazioneCodifiche request = model.creaRequestRicercaVariazioneCodifiche();
		logServiceRequest(request);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioVariazioneCodificheResponse response = variazioneDiBilancioService.ricercaDettaglioVariazioneCodifiche(request);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio della variazione codifiche");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		RicercaProvvedimento requestProvvedimento = model.creaRequestRicercaProvvedimento(response);
		RicercaProvvedimentoResponse responseProvvedimento = provvedimentoService.ricercaProvvedimento(requestProvvedimento);
		log.debug(methodName, "Richiamato il WebService per la ricerca del Provvedimento");
		logServiceResponse(responseProvvedimento);
		
		if(responseProvvedimento.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
		}
		
		log.debug(methodName, "Impostazione dei dati nel model e in sessione");
		model.impostaDatiDaResponseESessione(response, responseProvvedimento, sessionHandler);
		log.debug(methodName, "Dati impostati nel model e nella sessione");
		
		return SUCCESS;
	}
	
}
