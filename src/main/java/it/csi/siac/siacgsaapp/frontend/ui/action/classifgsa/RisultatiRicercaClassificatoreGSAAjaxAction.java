/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.classifgsa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.ClassificatoreGSAService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgsaapp.frontend.ui.model.classifgsa.RisultatiRicercaClassificatoreGSAAjaxModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.classifgsa.ElementoClassificatoreGSA;

/**
 * Action per i risultati di ricerca dell'Allegato Atto.
 * 
 * @author Elisa Chiari 
 * @version 1.0.0 - 04/01/2018
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaClassificatoreGSAAjaxAction extends PagedDataTableAjaxAction<ElementoClassificatoreGSA, 
		RisultatiRicercaClassificatoreGSAAjaxModel, ClassificatoreGSA, RicercaSinteticaClassificatoreGSA, RicercaSinteticaClassificatoreGSAResponse> {
	
	/** Per la serializzazione*/
	private static final long serialVersionUID = -9158819714906330183L;
		//Services
		@Autowired private transient ClassificatoreGSAService classificatoreGSAService;
	/** Costruttore vuoto di default */
	public RisultatiRicercaClassificatoreGSAAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CLASSIFICATORE_GSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CLASSIFICATORE_GSA);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaClassificatoreGSA request) {
		return request.getParametriPaginazione();
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaClassificatoreGSA request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}
	
	@Override
	protected ElementoClassificatoreGSA getInstance(ClassificatoreGSA e) throws FrontEndBusinessException {
		return new ElementoClassificatoreGSA(e);
	}
	
	@Override
	protected RicercaSinteticaClassificatoreGSAResponse getResponse(RicercaSinteticaClassificatoreGSA request) {
		return classificatoreGSAService.ricercaSinteticaClassificatoreGSA(request);
	}
	
	@Override
	protected ListaPaginata<ClassificatoreGSA> ottieniListaRisultati(RicercaSinteticaClassificatoreGSAResponse response) {
		return response.getClassificatoriGSA();
	}
}