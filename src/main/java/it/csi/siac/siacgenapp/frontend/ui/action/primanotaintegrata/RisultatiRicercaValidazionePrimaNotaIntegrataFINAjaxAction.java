/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataBaseAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaValidazionePrimaNotaIntegrataFINAjaxModel;

/**
 * Classe base di action per i risultati di ricerca della prima nota integrata per la validazione, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/06/2015
 * @version 1.0.1 - 08/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaValidazionePrimaNotaIntegrataFINAjaxAction extends RisultatiRicercaValidazionePrimaNotaIntegrataBaseAjaxAction<RisultatiRicercaValidazionePrimaNotaIntegrataFINAjaxModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4953671335511743272L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaValidazionePrimaNotaIntegrataFINAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_VALIDABILE_GEN);
	}

}
