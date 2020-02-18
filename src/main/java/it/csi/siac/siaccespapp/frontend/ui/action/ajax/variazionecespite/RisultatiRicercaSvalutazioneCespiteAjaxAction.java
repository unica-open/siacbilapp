/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.variazionecespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;

/**
 * The Class RisultatiRicercaVariazioneCespiteAjaxAction.
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSvalutazioneCespiteAjaxAction extends BaseRisultatiRicercaVariazioneCespiteAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4329960654827308401L;

	/**
	 * Instantiates a new risultati ricerca tipo bene ajax action.
	 */
	public RisultatiRicercaSvalutazioneCespiteAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SVALUTAZIONE_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SVALUTAZIONE_CESPITE);
	}
}
