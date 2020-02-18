/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.InserisciSvalutazioneCespiteModel;

/**
 * Inserimento della svalutazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 *
 */
@PutModelInSession
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciSvalutazioneCespiteAction extends BaseInserisciVariazioneCespiteAction<InserisciSvalutazioneCespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2246017912387382356L;

}
