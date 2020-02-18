/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.spesa;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.spesa.GestioneModificaMovimentoGestioneSpesaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento di gestione di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 * @version 1.0.2 - 04/12/2015 - modifica gestione importo (JIRA-2667)
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneModificaMovimentoGestioneSpesaInsContoPrimaNotaIntegrataBaseAction<M extends GestioneModificaMovimentoGestioneSpesaPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<Impegno, ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5601337087742893134L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		// Modifica => Subimpegno => Impegno
		// SIAC-2667: l'importo e' l'importo della modifica
		// SIAC-2667: l'importo e' l'importo OLD
		return model.getModificaMovimentoGestioneSpesa() != null && model.getModificaMovimentoGestioneSpesa().getImportoOld() != null
			? model.getModificaMovimentoGestioneSpesa().getImportoOld()
			: model.getSubImpegno() != null && model.getSubImpegno().getImportoAttuale() != null
				? model.getSubImpegno().getImportoAttuale()
				: model.getImpegno() != null && model.getImpegno().getImportoAttuale() != null
					? model.getImpegno().getImportoAttuale()
					: BigDecimal.ZERO;
	}

}

