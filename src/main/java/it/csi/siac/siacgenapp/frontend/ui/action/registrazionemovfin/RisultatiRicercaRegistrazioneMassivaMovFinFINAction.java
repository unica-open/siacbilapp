/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.registrazionemovfin;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMassivaMovFinFINModel;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;

/**
 * Action per i risultati di ricerca massiva della registrazione
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/11/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistrazioneMassivaMovFinFINAction extends RisultatiRicercaRegistrazioneMovFinBaseAction<RisultatiRicercaRegistrazioneMassivaMovFinFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7435055769495134046L;
	
	@Autowired private transient PrimaNotaService primaNotaService;

	@Override
	@BreadCrumb(MODEL_TITOLO)
	public String execute() throws Exception {
		// Aggiungo solo il breadcrumb
		return super.execute();
	}
	
	/**
	 * Registrazione massiva delle registrazioni movfin
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String registraTutto() {
		final String methodName = "registraTutto";
		// Prendo la request di ricerca dalla sessione per poter costruire la nuova request di validazione
		RicercaSinteticaRegistrazioneMovFin requestRicercaSintetica = sessionHandler.getParametroXmlType(BilSessionParameter.REQUEST_RICERCA_REGISTRAZIONI_MASSIVE_MOV_FIN_GEN,
				RicercaSinteticaRegistrazioneMovFin.class);
		
		RegistraMassivaPrimaNotaIntegrata request = model.creaRequestRegistraMassivaPrimaNotaIntegrata(requestRicercaSintetica);
		// Il servizio e' asincrono: devo wrappare la request
		AsyncServiceRequestWrapper<RegistraMassivaPrimaNotaIntegrata> asyncRequest = wrapRequestToAsync(request);
		
		AsyncServiceResponse response = primaNotaService.registraMassivaPrimaNotaIntegrataAsync(asyncRequest);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		// Successo - ritorno nella pagina, ma senza il pulsante per la validazione massiva
		log.debug(methodName, "Registrazione massiva: elaborazione asincrona inizializzata con successo. Id dell'operazione asincrona: " + response.getIdOperazioneAsincrona());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Registrazione massiva prima nota", "con successo: id dell'operazione " + response.getIdOperazioneAsincrona()));
		return SUCCESS;
	}

}
