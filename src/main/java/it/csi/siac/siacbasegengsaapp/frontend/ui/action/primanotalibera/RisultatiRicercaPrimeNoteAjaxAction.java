/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimeNoteAjaxModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNoteResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di action per i risultati di ricerca della causale EP, gestione AJAX.
 * 
 * @author Valentina
 * @version 1.0.0 - 22/06/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimeNoteAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoPrimaNotaIntegrata,
RisultatiRicercaPrimeNoteAjaxModel, PrimaNota, RicercaPrimeNote, RicercaPrimeNoteResponse> {
	
	
	private static final long serialVersionUID = 2401063038102160320L;

	@Autowired private transient PrimaNotaService primaNotaService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimeNoteAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTA_GEN);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTA_GEN);
	}


	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaPrimeNote request) {
		return request.getParametriPaginazione();
	}


	@Override
	protected void impostaParametriPaginazione(RicercaPrimeNote request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}


	@Override
	protected ElementoPrimaNotaIntegrata ottieniIstanza(PrimaNota e) throws FrontEndBusinessException {
		return new ElementoPrimaNotaIntegrata(e);
	}


	@Override
	protected RicercaPrimeNoteResponse ottieniResponse(RicercaPrimeNote request) {
		return primaNotaService.ricercaPrimeNote(request);
	}


	@Override
	protected ListaPaginata<PrimaNota> ottieniListaRisultati(RicercaPrimeNoteResponse response) {
		return response.getPrimeNote();
	}
	
	
}
