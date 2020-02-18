/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.RisultatiRicercaCausaleEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaCausaleEntrataResponse;

/**
 * Action per i risultati di ricerca della causale di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCausaleEntrataAction extends GenericCausaleEntrataAction<RisultatiRicercaCausaleEntrataModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7873129927845047009L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Ottengo il displayStart dalla sessione, se presente
		Integer posizioneStart = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		if(posizioneStart == null) {
			posizioneStart = Integer.valueOf(0);
		}
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart.intValue());
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		int startPosition = 0;
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione.intValue();
		}
		model.setSavedDisplayStart(startPosition);
		
		model.setRiepilogoRicerca((String) sessionHandler.getParametro(BilSessionParameter.RIEPILOGO_RICERCA_CAUSALE));
		
		log.debug(methodName, "StartPosition = " + startPosition);
		
		return SUCCESS;
	}

	

	/**
	 * Redirezione al metodo di aggiornamento.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Uid della causale da aggiornare: " + model.getUidDaAggiornare());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}

	/**
	 * Annullamento della causale.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Annullamento della causale avente uid=" + model.getUidDaAnnullare());
		log.debug(methodName, "Data Annullamento=" + model.getCausale().getDataScadenza());
		
		log.debug(methodName, "Creazione della request ed invocazione del servizio");

		AnnullaCausaleEntrata request = model.creaRequestAnnullaCausaleEntrata();
		AnnullaCausaleEntrataResponse response = preDocumentoEntrataService.annullaCausaleEntrata(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		// Imposto il parametro di rientro a TRUE
		log.debug(methodName, "Causale annullata: imposto il valore TRUE al parametro RIENTRO e torno alla pagina");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}
	
	
	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid della causale da consultare: " + model.getUidDaConsultare());
		return SUCCESS;
	}
		
}
