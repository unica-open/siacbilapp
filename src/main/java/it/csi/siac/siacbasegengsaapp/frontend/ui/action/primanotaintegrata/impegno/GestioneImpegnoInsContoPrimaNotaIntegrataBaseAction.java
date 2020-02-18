/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.impegno;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.impegno.GestioneImpegnoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinImpegnoHelper;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, impegno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneImpegnoInsContoPrimaNotaIntegrataBaseAction<M extends GestioneImpegnoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<Impegno, ConsultaRegistrazioneMovFinImpegnoHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4866328761410947005L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getImpegno() != null && model.getImpegno().getImportoIniziale() != null ? model.getImpegno().getImportoIniziale() : BigDecimal.ZERO;
	}

}

