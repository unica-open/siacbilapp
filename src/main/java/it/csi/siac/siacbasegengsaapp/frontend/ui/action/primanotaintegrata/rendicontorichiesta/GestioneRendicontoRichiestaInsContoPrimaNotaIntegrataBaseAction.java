/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.rendicontorichiesta;

import java.math.BigDecimal;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.rendicontorichiesta.GestioneRendicontoRichiestaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRendicontoRichiestaHelper;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, rendiconto richiesta economale.
 * 
 * @author Simona Paggio
 * @version 1.0.0 - 14/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneRendicontoRichiestaInsContoPrimaNotaIntegrataBaseAction<M extends GestioneRendicontoRichiestaPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<RendicontoRichiesta, ConsultaRegistrazioneMovFinRendicontoRichiestaHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4866328761410947005L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getRendicontoRichiesta().getImportoMovimento().abs();
	}

}

