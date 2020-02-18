/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.AggiornaCausaleEPBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.BaseInserisciAggiornaCausaleEPBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacgenapp.frontend.ui.model.causali.AggiornaCausaleEPFINModel;

/**
 * Classe di action per l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaCausaleEPBaseAction.MODEL_SESSION_NAME_AGGIORNAMENTO_FIN)
public class AggiornaCausaleEPFINAction extends AggiornaCausaleEPBaseAction<AggiornaCausaleEPFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 175479691581335856L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		
		try {
			caricaCausaleEP();
		} catch(WebServiceInvocationFailureException wsife) {
			// Fallimento nell'invocazione del servizio: esco
			return INPUT;
		}
		caricaListe();
		return SUCCESS;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassiPiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GEN;
	}
	
}
