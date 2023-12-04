/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.dismissionecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.AggiornaDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaDismissioneCespiteAction extends BaseInserisciAggiornaDismissioneCespiteAction<AggiornaDismissioneCespiteModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = -806937781564018724L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName ="execute";
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setDismissioneCespite(null);
		RicercaDettaglioDismissioneCespiteResponse response = ottieniRicercaDettaglioDismissioneCespiteResponse();
		if(response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errrori nel reperimento del cespite.");
			return INPUT;
		}
		DismissioneCespite dismissione = response.getDismissioneCespite();
		if(dismissione == null || dismissione.getUid() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("cespite con uid + " + model.getDismissioneCespite().getUid()));
			return INPUT;
		}
		impostaDatiNelModel(dismissione);
		
		return SUCCESS;		
	}

	/**
	 * @param dismissione
	 */
	private void impostaDatiNelModel(DismissioneCespite dismissione) {
		model.setDismissioneCespite(dismissione);
		model.setUidDismissioneCespite(dismissione.getUid());
		model.setAttoAmministrativo(dismissione.getAttoAmministrativo());
		model.setTipoAtto(dismissione.getAttoAmministrativo() != null? dismissione.getAttoAmministrativo().getTipoAtto() : null);
		model.setStrutturaAmministrativoContabile(dismissione.getAttoAmministrativo() != null? dismissione.getAttoAmministrativo().getStrutturaAmmContabile() : null);
	}
	
	/**
	 * Inserisci anagrafica.
	 *
	 * @return the string
	 */
	public String proseguiStep1Anagrafica() {
		final String methodName ="proseguiStep1Anagrafica";
		AggiornaAnagraficaDismissioneCespite req = model.creaRequestAggiornaAnagraficaDismissioneCespite();
		//chiamo il servizio di inserimento
		AggiornaAnagraficaDismissioneCespiteResponse response = cespiteService.aggiornaAnagraficaDismissioneCespite(req);
		//controllo se si siano verificati error
		if(response.hasErrori()) {
			//Si sono verificati errori 
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'inserimento della categoria.");
			return INPUT;
		}
		//popolo il model con i dati restituiti dalla response
		impostaDatiNelModel(response.getDismissioneCespite());
		impostaInformazioneSuccesso();

		return SUCCESS;
	}
}
