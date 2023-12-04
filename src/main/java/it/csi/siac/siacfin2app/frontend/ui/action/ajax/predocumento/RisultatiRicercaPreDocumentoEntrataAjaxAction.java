/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.predocumento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;

/**
 * Action per i risultati di ricerca del predocumento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 24/04/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPreDocumentoEntrataAjaxAction extends BaseRisultatiRicercaPreDocumentoEntrataAjaxAction {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4079483156434678622L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPreDocumentoEntrataAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PREDOCUMENTI_ENTRATA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA);
	}
}
