/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.aggiornamento;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per la consultazione della prima nota integrata. Modulo GEN
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_GSA)
public class AggiornaPrimaNotaIntegrataGSAAction extends AggiornaPrimaNotaIntegrataBaseAction<AggiornaPrimaNotaIntegrataGSAModel> {


	/** Per la serializzazione */
	private static final long serialVersionUID = 2979872320803893849L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		return super.execute();
	}
}
