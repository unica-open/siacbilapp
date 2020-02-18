/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.ordinativo.incasso;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.ordinativo.incasso.GestioneOrdinativoIncassoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoIncassoHelper;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, ordinativo di incasso.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneOrdinativoIncassoInsContoPrimaNotaIntegrataBaseAction<M extends GestioneOrdinativoIncassoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction <OrdinativoIncasso, ConsultaRegistrazioneMovFinOrdinativoIncassoHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6605882151619320481L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getOrdinativoIncasso() != null && model.getOrdinativoIncasso().getImportoOrdinativo() != null ? model.getOrdinativoIncasso().getImportoOrdinativo() : BigDecimal.ZERO;
	}
}

