/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.InserisciRivalutazioneCespiteModel;

/**
 * Inserimento della rivalutazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 *
 */
@PutModelInSession
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciRivalutazioneCespiteAction extends BaseInserisciVariazioneCespiteAction<InserisciRivalutazioneCespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5843358303821539555L;

}
