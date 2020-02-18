/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RicercaValidazionePrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.RicercaValidazionePrimaNotaIntegrataGSAModel;

/**
 * Classe di action per la ricerca della prima nota libera per la validazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.1.0 - 16/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaValidazionePrimaNotaIntegrataGSAAction extends RicercaValidazionePrimaNotaIntegrataBaseAction<RicercaValidazionePrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2143335765825499721L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Esposto il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterDati() {
		return BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRiepilogo() {
		return BilSessionParameter.RIEPILOGO_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN;
	}

	@Override
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
	    return 
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio);
	}
	
	
}
