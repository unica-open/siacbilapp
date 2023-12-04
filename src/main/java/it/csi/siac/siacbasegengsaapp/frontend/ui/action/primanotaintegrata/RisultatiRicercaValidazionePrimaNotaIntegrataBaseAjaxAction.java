/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di action per i risultati di ricerca della prima nota integrata per la validazione, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @param <M> la tipizzazione del model
 */
public abstract class RisultatiRicercaValidazionePrimaNotaIntegrataBaseAjaxAction<M extends BaseRisultatiRicercaPrimaNotaIntegrataAjaxBaseModel> extends BaseRisultatiRicercaPrimaNotaIntegrataBaseAjaxAction<M,
		RicercaSinteticaPrimaNotaIntegrataValidabile, RicercaSinteticaPrimaNotaIntegrataValidabileResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7960399332292537442L;

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaPrimaNotaIntegrataValidabile req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaPrimaNotaIntegrataValidabile req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaPrimaNotaIntegrataValidabileResponse getResponse(RicercaSinteticaPrimaNotaIntegrataValidabile req) {
		return primaNotaService.ricercaSinteticaPrimaNotaIntegrataValidabile(req);
	}

	@Override
	protected ListaPaginata<PrimaNota> ottieniListaRisultati(RicercaSinteticaPrimaNotaIntegrataValidabileResponse res) {
		return res.getPrimeNote();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
}
