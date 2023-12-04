/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.stampa;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa.StampaRiepilogoAnnualeIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaRiepilogoAnnualeIvaResponse;

/**
 * Classe di action per la stampa del riepilogo annuale iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/07/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StampaRiepilogoAnnualeIvaAction extends GenericStampaIvaAction<StampaRiepilogoAnnualeIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 290636867681512487L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricamentoListe();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		return super.execute();
	}
	
	@Override
	public String effettuaStampa() {
		final String methodName = "effettuaStampa";
		StampaRiepilogoAnnualeIva request = model.creaRequestStampaRiepilogoAnnualeIva();
		logServiceRequest(request);
		StampaRiepilogoAnnualeIvaResponse response = stampaIvaService.stampaRiepilogoAnnualeIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio StampaRiepilogoAnnualeIva");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio StampaRiepilogoAnnualeIva avvenuta con successo");
		impostaMessaggioStampaPresaInCarico();
		return SUCCESS;
	}
	
	@Override
	public void validateEffettuaStampa() {
		// Validazione logica
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "Gruppo attivita' iva");
	}
	
	/**
	 * Carica le varie liste da servizio.
	 */
	private void caricamentoListe() {
		caricaListaGruppoAttivitaIva();
	}
	
}
