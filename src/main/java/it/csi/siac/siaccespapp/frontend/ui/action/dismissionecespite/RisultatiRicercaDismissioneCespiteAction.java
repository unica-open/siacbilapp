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
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.RisultatiRicercaDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespiteResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RisultatiRicercaDismissioneCespiteAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDismissioneCespiteAction extends GenericDismissioneCespiteAction<RisultatiRicercaDismissioneCespiteModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = 1010484366581425071L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;		
	}

	/**
	 * Consulta.
	 *
	 * @return the string
	 */
	public String consulta() {
		//devo solo fare la redirezione alla pagina di consultazione
		return SUCCESS;
	}
	
	
	/**
	 * Aggiorna categoria.
	 *
	 * @return the string
	 */
	public String aggiorna() {
		//devo solo fare la redirezione alla pagina di aggiornamento
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validate elimina categoria.
	 */
	public void validateElimina() {
		checkCondition(model.getUidDismissioneCespite() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo bene cespite da eliminare"));
	}
	
	/**
	 * Elimina categoria.
	 *
	 * @return the string
	 */
	public String elimina() {
		final String methodName ="eliminaTipoBene";
		EliminaDismissioneCespite request = model.creaRequestEliminaDismissioneCespite();
		EliminaDismissioneCespiteResponse response = cespiteService.eliminaDismissioneCespite(request);
		if(response.hasErrori()) {
			//verificati errori
			log.debug(methodName, "si sono verificati errori nella chiamata al servizio.");
			addErrori(response);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validate effettua scritture.
	 */
	public void validateEffettuaScritture() {
		checkCondition(model.getUidDismissioneCespite()!= 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid dismissione"));
	}
	
	/**
	 * Effettua scritture.
	 *
	 * @return the string
	 */
	public String effettuaScritture() {
		
		InserisciPrimeNoteDismissioneCespite req = model.creaRequestEffettuaScrittureDismissioneCespite();
		InserisciPrimeNoteDismissioneCespiteResponse res = cespiteService.inserisciPrimeNoteDismissioneCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	
}
