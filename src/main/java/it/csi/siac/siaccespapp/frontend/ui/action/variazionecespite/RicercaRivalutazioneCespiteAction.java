/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.RicercaRivalutazioneCespiteModel;

/**
 * Ricerca della rivalutazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaRivalutazioneCespiteAction extends BaseRicercaVariazioneCespiteAction<RicercaRivalutazioneCespiteModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -290387203508929244L;
	
}
