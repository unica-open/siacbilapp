/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.ordinativo.pagamento;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.pagamento.GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, ordinativo di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneOrdinativoPagamentoInsContoPrimaNotaIntegrataBaseAction<M extends GestioneOrdinativoPagamentoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction <OrdinativoPagamento, ConsultaRegistrazioneMovFinOrdinativoPagamentoHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 706195749412727440L;

	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getOrdinativoPagamento() != null && model.getOrdinativoPagamento().getImportoOrdinativo() != null ? model.getOrdinativoPagamento().getImportoOrdinativo() : BigDecimal.ZERO;
	}
}

