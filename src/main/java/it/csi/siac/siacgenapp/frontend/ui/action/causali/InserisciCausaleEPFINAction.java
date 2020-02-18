/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.BaseInserisciAggiornaCausaleEPBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.InserisciCausaleEPBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.causali.InserisciCausaleEPFINModel;

/**
 * Classe di action per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaCausaleEPBaseAction.MODEL_SESSION_NAME_INSERIMENTO_FIN)
public class InserisciCausaleEPFINAction extends InserisciCausaleEPBaseAction <InserisciCausaleEPFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4469636595935362556L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		caricaListe();
		return SUCCESS;
	}
	
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassiPiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GEN;
	}
}
