/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ammortamento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.ammortamento.RisultatiRicercaCespiteAnteprimaAmmortamentoModel;

/**
 * The Class RicercaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCespiteAnteprimaAmmortamentoAction extends GenericBilancioAction<RisultatiRicercaCespiteAnteprimaAmmortamentoModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = -743681531463334722L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;
	}

}
