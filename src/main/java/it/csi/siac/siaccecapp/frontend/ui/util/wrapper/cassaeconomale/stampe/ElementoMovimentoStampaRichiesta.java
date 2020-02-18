/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe;

import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Wrapper per il movimento, per la stampa del rendiconto
 * @author Marchino Alessandro
 *
 */
public class ElementoMovimentoStampaRichiesta extends ElementoMovimentoStampa {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6838030783632543261L;

	/**
	 * Costruttore di wrap
	 * @param movimento il movimento da wrappare
	 */
	public ElementoMovimentoStampaRichiesta(Movimento movimento) {
		super(movimento);
	}
	
	@Override
	protected Impegno extractImpegno() {
		return movimento.getRichiestaEconomale() != null && movimento.getRichiestaEconomale().getImpegno() != null
			? movimento.getRichiestaEconomale().getImpegno()
			: null;
	}
	
	@Override
	protected SubImpegno extractSubImpegno() {
		return movimento.getRichiestaEconomale() != null && movimento.getRichiestaEconomale().getSubImpegno() != null
			? movimento.getRichiestaEconomale().getSubImpegno()
			: null;
	}
	
	@Override
	protected TipoRichiestaEconomale extractTipo() {
		return movimento.getRichiestaEconomale() != null
			? movimento.getRichiestaEconomale().getTipoRichiestaEconomale()
			: null;
	}
	
	@Override
	protected RichiestaEconomale extractRichiesta() {
		return movimento.getRichiestaEconomale();
	}
	
}
