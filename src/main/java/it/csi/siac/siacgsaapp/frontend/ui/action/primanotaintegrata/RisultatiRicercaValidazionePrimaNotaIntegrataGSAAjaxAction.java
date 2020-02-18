/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataBaseAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataGSAAjaxModel;

/**
 * Classe base di action per i risultati di ricerca della prima nota integrata per la validazione, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 * @version 1.0.1 - 08/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaValidazionePrimaNotaIntegrataGSAAjaxAction extends RisultatiRicercaValidazionePrimaNotaIntegrataBaseAjaxAction<RisultatiRicercaValidazionePrimaNotaIntegrataGSAAjaxModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -160213040678516546L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaValidazionePrimaNotaIntegrataGSAAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GSA);
	}

}
