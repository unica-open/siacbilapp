/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.RecuperaProtocolloRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIvaResponse;
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
@PutModelInSession
public class RecuperaProtocolloRegistroIvaAction extends AggiornaRegistroIvaBaseAction<RecuperaProtocolloRegistroIvaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4789076876733177858L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		cleanErroriMessaggiInformazioni();
		// Controllo se il CDU sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		
		RicercaDettaglioRegistroIva request = model.creaRequestRicercaDettaglioRegistroIva();
		logServiceRequest(request);
		RicercaDettaglioRegistroIvaResponse response = registroIvaService.ricercaDettaglioRegistroIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaDettaglioRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricercato gruppo con uid " + model.getUidRegistroIva());
		model.impostaDatiRecuperaProtocollo(response.getRegistroIva());
		
		return SUCCESS;
	}	
	
	/**
	 * Preparazione per il metodo {@link #recuperaProtocollo()}.
	 */
	public void prepareRecuperaProtocollo(){
		cleanErroriMessaggiInformazioni();
		model.setNumeroProtocolloDefinitivo(null);
		model.setNumeroProtocolloProvvisorio(null);
	}
	
	/**
	 *  Permette di recuperare un protocollo iva di un registro 
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String recuperaProtocollo() {
		final String methodName = "aggiornamento";
		RecuperaProtocolloRegistroIva request =  model.creaRequestRecuperaProtocolloRegistroIva();
		RecuperaProtocolloRegistroIvaResponse response = registroIvaService.recuperaProtocolloRegistroIva(request);
		
		if(response.hasErrori()) {
			// Fornisco l'errore a video ed esco
			log.info(methodName, "Errore nell'invocazione del servizio di AggiornaRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		RegistroIva registroIva = response.getRegistroIva();
		model.impostaDatiRecuperaProtocollo(registroIva);
		log.debug(methodName, "Aggiornamento Registro Iva andato a buon fine: uid registro " + registroIva.getUid());
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per l'aggiornamento siano validi.
	 */
	public void validateRecuperaProtocollo() {
		baseValidazione();
		checkCondition(model.getNumeroProtocolloDefinitivo() != null || model.getNumeroProtocolloProvvisorio() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("necessario inserire il numero protocollo"));
	}

	
	
}
