/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataFINModel;
/**
 * Classe base di action per i risultati di ricerca per la validazione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaValidazionePrimaNotaIntegrataFINAction extends RisultatiRicercaValidazionePrimaNotaIntegrataBaseAction<RisultatiRicercaValidazionePrimaNotaIntegrataFINModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5617631914267176645L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Espongo il breadcrumb
		return super.execute();
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRiepilogo() {
		return BilSessionParameter.RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN;
	}
	
}