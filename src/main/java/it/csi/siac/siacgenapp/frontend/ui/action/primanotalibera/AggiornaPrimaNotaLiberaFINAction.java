/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotalibera;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.AggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.AggiornaPrimaNotaLiberaFINModel;

/**
 *  Classe base di action per l'aggiornamento e l'aggiornamento della prima nota libera
 *   
 * @author Paggio Simona
 * @version 1.0.0 - 14/04/2015
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPrimaNotaLiberaBaseAction.MODEL_SESSION_NAME_AGGIORNAMENTO_FIN)
public class AggiornaPrimaNotaLiberaFINAction extends AggiornaPrimaNotaLiberaBaseAction<AggiornaPrimaNotaLiberaFINModel>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3252897888054803298L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN;
	}
	
}
