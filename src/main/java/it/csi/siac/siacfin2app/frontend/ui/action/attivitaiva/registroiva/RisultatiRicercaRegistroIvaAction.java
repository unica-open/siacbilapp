/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.RisultatiRicercaRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.BloccaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SbloccaRegistroIvaResponse;

/**
 * Action per i risultati di ricerca del GruppoAttivitaIva
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistroIvaAction extends GenericRegistroIvaAction<RisultatiRicercaRegistroIvaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 950335396478119525L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "StartPosition = " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		return SUCCESS;
	}

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid del registro da aggiornare: " + model.getUidDaAggiornare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	

	/**
	 * Eliminazione del registro.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String elimina() {
		final String methodName = "elimina";
		
		EliminaRegistroIva request = model.creaRequestEliminaRegistroIva();
		logServiceRequest(request);
		EliminaRegistroIvaResponse response = registroIvaService.eliminaRegistroIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio EliminaRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Eliminato registro con uid " + model.getUidDaEliminare());
		// Imposto il parametro DaRientro per avvertire la action di ricaricare la lista
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		// Imposto il successo in sessione
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	//CR-3791
	/**
	 * Metodo per bloccare il registro Iva selezionato
	 * @return il risultato dell'invocazione
	 */
	public String bloccaRegistro(){
		final String methodName ="bloccaRegistro";
		BloccaRegistroIva request = model.creaRequestBloccaRegistroIva();
		BloccaRegistroIvaResponse response = registroIvaService.bloccaRegistroIva(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori nel servizio di blocco del registro Iva");
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "Il registro con uid " + model.getUidRegistroSelezionato() + " e' stato bloccato con successo");
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
	/**
	 * Metodo per bloccare il registro Iva selezionato
	 * @return il risultato dell'invocazione
	 */
	public String sbloccaRegistro(){
		final String methodName ="bloccaRegistro";
		SbloccaRegistroIva request = model.creaRequestSbloccaRegistroIva();
		SbloccaRegistroIvaResponse response = registroIvaService.sbloccaRegistroIva(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori nel servizio di sblocco del registro Iva");
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "Il registro con uid " + model.getUidRegistroSelezionato() + " e' stato sbloccato con successo");
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di recuperaProtocollo.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String recuperaProtocollo() {
		final String methodName = "recuperaProtocollo";
		log.debug(methodName, "Uid del registro da cui recuperare un protocollo: " + model.getUidRegistroSelezionato());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Metodo per bloccare il registro Iva selezionato
	 * @return il risultato dell'invocazione
	 */
	public String allineaProtocolli(){
		final String methodName ="bloccaRegistro";
		AllineaProtocolloRegistroIva request = model.creaRequestAllineaProtocolloRegistroIva();
		AllineaProtocolloRegistroIvaResponse response = registroIvaService.allineaProtocolloRegistroIva(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Si sono verificati errori nel servizio di allineamento del protocollo del registro Iva");
			addErrori(response);
			return SUCCESS;
		}
		log.debug(methodName, "Allineamento dei protocolli del registro con uid " + model.getUidRegistroSelezionato() + " eseguito con successo.");
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}
	
}
