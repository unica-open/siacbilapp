/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrata;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrataFactory;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;

/**
 * Classe base di action per i risultati di ricerca della prima nota integrata, gestione AJAX.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 20/10/2016
 * @param <M> la tipizzazione del model
 */
public abstract class RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxAction<M extends RisultatiRicercaDatiFinanziariPrimaNotaIntegrataBaseAjaxModel> extends PagedDataTableAjaxAction<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>, M, Entita, OttieniEntitaCollegatePrimaNota, OttieniEntitaCollegatePrimaNotaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2085403225367653701L;
	@Autowired private transient PrimaNotaService primaNotaService;

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(OttieniEntitaCollegatePrimaNota request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(OttieniEntitaCollegatePrimaNota request,ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoMovimentoConsultazionePrimaNotaIntegrata<?> getInstance(Entita e) throws FrontEndBusinessException {
		return ElementoMovimentoConsultazionePrimaNotaIntegrataFactory.getInstance(e);
	}

	@Override
	protected OttieniEntitaCollegatePrimaNotaResponse getResponse(OttieniEntitaCollegatePrimaNota request) {
		return primaNotaService.ottieniEntitaCollegatePrimaNota(request);
	}

	@Override
	protected ListaPaginata<Entita> ottieniListaRisultati(OttieniEntitaCollegatePrimaNotaResponse response) {
		return response.getEntitaCollegate();
	}

}
