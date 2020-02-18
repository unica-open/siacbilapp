/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.cespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;

/**
 * The Class RisultatiRicercaCespiteAjaxAction.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCespiteDaDismissioneAjaxAction extends BaseRisultatiRicercaCespiteAjaxAction {

	/**
	 * per la serializzazione
	 */
	private static final long serialVersionUID = -7687723264854751349L;

	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaCespiteDaDismissioneAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CESPITE_DA_DISMISSIONE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CESPITE_DA_DISMISSIONE);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}

}
