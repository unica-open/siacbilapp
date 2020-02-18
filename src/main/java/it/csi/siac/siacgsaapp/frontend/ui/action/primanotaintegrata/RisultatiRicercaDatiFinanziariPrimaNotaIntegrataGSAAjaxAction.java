/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.handler.session.SessionParameter;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaDatiFinanziariPrimaNotaIntegrataGSAAjaxModel;

/**
 * Classe di Action Ajax per la gestione della paginazione dei dati finanziari nella consultazione della prima nota integrata
 * @author Elisa Chiari
 *	@version 1.0.0 - 24/10/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaDatiFinanziariPrimaNotaIntegrataGSAAjaxAction extends RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxAction<RisultatiRicercaDatiFinanziariPrimaNotaIntegrataGSAAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -544872315672207094L;
	
	/**
	 * Costruttore vuoto di default
	 */
	public RisultatiRicercaDatiFinanziariPrimaNotaIntegrataGSAAjaxAction(){
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA);
	}

	/**
	 * @return the parametroSessioneLista
	 */
	@Override
	protected SessionParameter getParametroSessioneLista() {
		return BilSessionParameter.RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA;
	}

	/**
	 * @return the parametroSessioneRequest
	 */
	@Override
	protected SessionParameter getParametroSessioneRequest() {
		return BilSessionParameter.REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GSA;
	}


}
