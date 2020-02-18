/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata.GestioneModificaMovimentoGestioneEntrataPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento di gestione di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 * @version 1.0.2 - 04/12/2015 - modifica gestione importo (JIRA-2667)
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneModificaMovimentoGestioneEntrataInsContoPrimaNotaIntegrataBaseAction<M extends GestioneModificaMovimentoGestioneEntrataPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<Accertamento, ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5601337087742893134L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		// Modifica => Subimpegno => Accertamento
		// SIAC-2667: l'importo e' l'importo della modifica
		// SIAC-2667: l'importo e' l'importo OLD
		return model.getModificaMovimentoGestioneEntrata() != null && model.getModificaMovimentoGestioneEntrata().getImportoOld() != null
			? model.getModificaMovimentoGestioneEntrata().getImportoOld()
			: model.getSubAccertamento() != null && model.getSubAccertamento().getImportoAttuale() != null
				? model.getSubAccertamento().getImportoAttuale()
				: model.getAccertamento() != null && model.getAccertamento().getImportoAttuale() != null
					? model.getAccertamento().getImportoAttuale()
					: BigDecimal.ZERO;
	}

}

