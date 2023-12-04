/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.RisultatiRicercaCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespiteResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCespiteAction extends GenericCespiteAction<RisultatiRicercaCespiteModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = 983046490047138008L;


	
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
		checkCondition(model.getUidCespite() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo bene cespite da eliminare"));
	}
	
	/**
	 * Elimina categoria.
	 *
	 * @return the string
	 */
	public String elimina() {
		final String methodName ="eliminaTipoBene";
		EliminaCespite request = model.creaRequestEliminaCespite();
		EliminaCespiteResponse response = cespiteService.eliminaCespite(request);
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
	 * Rivalutazioni.
	 *
	 * @return the string
	 */
	public String rivalutazioni() {
		return SUCCESS;
	}
	
	/**
	 * Rivalutazioni.
	 *
	 * @return the string
	 */
	public String svalutazioni() {
		return SUCCESS;
	}
	
	/**
	 * Rivalutazioni.
	 *
	 * @return the string
	 */
	public String dismissioni() {
		if(model.getUidDismissioneCollegata() == 0) {
			return SUCCESS + "-inserisci";
		}
		return SUCCESS+ "-aggiorna";
	}
	
}
