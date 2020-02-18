/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.richiestaeconomale;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.richiestaeconomale.GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRichiestaEconomaleHelper;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, richiesta economale.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneRichiestaEconomaleInsContoPrimaNotaIntegrataBaseAction<M extends GestioneRichiestaEconomalePrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<RichiestaEconomale, ConsultaRegistrazioneMovFinRichiestaEconomaleHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4866328761410947005L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getRichiestaEconomale() != null && model.getRichiestaEconomale().getImporto() != null ? model.getRichiestaEconomale().getImporto() : BigDecimal.ZERO;
	}

}

