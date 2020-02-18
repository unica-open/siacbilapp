/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.liquidazione;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione.GestioneLiquidazionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinLiquidazioneHelper;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, liquidazione.
 * 
 * @author Valentina
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneLiquidazioneInsContoPrimaNotaIntegrataBaseAction<M extends GestioneLiquidazionePrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<Liquidazione, ConsultaRegistrazioneMovFinLiquidazioneHelper, M> {


	/** Per la serializzazione */
	private static final long serialVersionUID = 1299896613856888386L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getLiquidazione() != null && model.getLiquidazione().getImportoLiquidazione() != null ? model.getLiquidazione().getImportoLiquidazione() : BigDecimal.ZERO;
	}

}

