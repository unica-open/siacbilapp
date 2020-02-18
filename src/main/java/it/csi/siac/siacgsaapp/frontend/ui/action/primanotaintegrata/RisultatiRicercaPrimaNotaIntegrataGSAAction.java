/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataGSAModel;
/**
 * Classe di action per i risultati di ricerca della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(RisultatiRicercaPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_RIS_RICERCA_GSA)
public class RisultatiRicercaPrimaNotaIntegrataGSAAction extends RisultatiRicercaPrimaNotaIntegrataBaseAction<RisultatiRicercaPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -1838787905119059658L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRiepilogo() {
		return BilSessionParameter.RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_GSA;
	}
	
}
