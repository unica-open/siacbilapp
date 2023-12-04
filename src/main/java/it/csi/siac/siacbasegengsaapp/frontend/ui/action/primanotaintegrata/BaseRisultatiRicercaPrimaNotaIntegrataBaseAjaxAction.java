/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di action per i risultati di ricerca della prima nota integrata per la validazione, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * 
 * @param <M>   la tipizzazione del model
 * @param <REQ> la tipizzazione della request
 * @param <RES> la tipizzazione della response
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public abstract class BaseRisultatiRicercaPrimaNotaIntegrataBaseAjaxAction <M extends BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel, REQ extends ServiceRequest, RES extends ServiceResponse> 
		extends PagedDataTableAjaxAction<ElementoPrimaNotaIntegrata, M, PrimaNota, REQ, RES> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4233695296021292114L;
	
	/** Serviz&icirc; della prima nota */
	@Autowired protected transient PrimaNotaService primaNotaService;
	
	@Override
	protected ElementoPrimaNotaIntegrata getInstance(PrimaNota e) throws FrontEndBusinessException {
		return new ElementoPrimaNotaIntegrata(e);
	}

	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
}
