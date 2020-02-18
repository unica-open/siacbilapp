/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.ConsultaRivalutazioneCespiteModel;

/**
 * Action per la consultazione della svalutazione del cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/08/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaSvalutazioneCespiteAction extends BaseConsultaVariazioneCespiteAction<ConsultaRivalutazioneCespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2491007540637778349L;

}
