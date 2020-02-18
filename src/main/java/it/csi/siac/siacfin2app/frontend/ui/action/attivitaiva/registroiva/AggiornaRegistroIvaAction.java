/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.AggiornaRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di action per l'aggiornamento del Registro Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaRegistroIvaAction extends AggiornaRegistroIvaBaseAction<AggiornaRegistroIvaModel> {
		
	/** Per la serializzazione	 */
	private static final long serialVersionUID = 1265147532521353086L;

	/**
	 * Aggiorna il Gruppo Attivita Iva
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		
		AggiornaRegistroIva request = model.creaRequestAggiornaRegistroIva();
		logServiceRequest(request);
		controllaFlagLiquidazioneIva(request);
		AggiornaRegistroIvaResponse response = registroIvaService.aggiornaRegistroIva(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Fornisco l'errore a video ed esco
			log.info(methodName, "Errore nell'invocazione del servizio di AggiornaRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		RegistroIva registroIva = response.getRegistroIva();
		log.debug(methodName, "Aggiornamento Registro Iva andato a buon fine: uid registro " + registroIva.getUid());
		
		model.impostaDati(registroIva);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per l'aggiornamento siano validi.
	 */
	public void validateAggiornamento() {
		baseValidazione();
	}
	
	private void controllaFlagLiquidazioneIva(AggiornaRegistroIva request) {
		// SIAC-6276
		if (request.getRegistroIva().getFlagLiquidazioneIva() == null) {
			request.getRegistroIva().setFlagLiquidazioneIva(true);
		}
	}
	
}
