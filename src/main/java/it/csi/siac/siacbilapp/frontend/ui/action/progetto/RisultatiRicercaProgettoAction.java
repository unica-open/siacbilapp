/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.RisultatiRicercaProgettoModel;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgettoResponse;
import it.csi.siac.siaccorser.model.Informazione;

/**
 * Action per i risultati di ricerca del progetto
 * 
 * @author Alessandra Osorio
 * @version 1.0.0 - 06/02/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaProgettoAction extends GenericBilancioAction<RisultatiRicercaProgettoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2271333474213144286L;
	
	@Autowired private transient ProgettoService progettoService;
	
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
		log.debug(methodName, "Uid del capitolo da aggiornare: " + model.getUidDaAggiornare());
		return SUCCESS;
	}

	/**
	 * Annullamento del progetto.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		log.debug(methodName, "Annullamento del progetto avente uid=" + model.getUidDaAnnullare());
		
		log.debug(methodName, "Creazione della request ed invocazione del servizio");
		AnnullaProgetto request = model.creaRequestAnnullaProgetto();
		AnnullaProgettoResponse response = progettoService.annullaProgetto(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Progetto annullato: imposto il valore TRUE al parametro RIENTRO e torno alla pagina");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	/**
	 * Riattivazione del progetto.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String riattiva() {
		final String methodName = "riattiva";
		log.debug(methodName, "Riattivazione del progetto avente uid=" + model.getUidDaAnnullare());
		
		log.debug(methodName, "Creazione della request ed invocazione del servizio");
		RiattivaProgetto request = model.creaRequestRiattivaProgetto();
		RiattivaProgettoResponse response = progettoService.riattivaProgetto(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Progetto riattivato: imposto il valore TRUE al parametro RIENTRO e torno alla pagina");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	/**
	 * Redirezione al metodo di consultazione.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid del capitolo da consultare: " + model.getUidDaConsultare());
		return SUCCESS;
	}

}
