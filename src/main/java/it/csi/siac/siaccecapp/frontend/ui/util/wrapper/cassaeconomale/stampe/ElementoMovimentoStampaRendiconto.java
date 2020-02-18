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
public class ElementoMovimentoStampaRendiconto extends ElementoMovimentoStampa {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1408930304403318633L;

	/**
	 * Costruttore di wrap
	 * @param movimento il movimento da wrappare
	 */
	public ElementoMovimentoStampaRendiconto(Movimento movimento) {
		super(movimento);
	}
	
	@Override
	protected Impegno extractImpegno() {
		return movimento.getRendicontoRichiesta() != null && movimento.getRendicontoRichiesta().getImpegno() != null
			? movimento.getRendicontoRichiesta().getImpegno()
			: null;
	}
	
	@Override
	protected SubImpegno extractSubImpegno() {
		return movimento.getRendicontoRichiesta() != null && movimento.getRendicontoRichiesta().getSubImpegno() != null
			? movimento.getRendicontoRichiesta().getSubImpegno()
			: null;
	}
	
	@Override
	protected TipoRichiestaEconomale extractTipo() {
		return movimento.getRendicontoRichiesta() != null && movimento.getRendicontoRichiesta().getRichiestaEconomale() != null
			? movimento.getRendicontoRichiesta().getRichiestaEconomale().getTipoRichiestaEconomale()
			: null;
	}
	
	@Override
	protected RichiestaEconomale extractRichiesta() {
		return movimento.getRendicontoRichiesta() != null
			? movimento.getRendicontoRichiesta().getRichiestaEconomale()
			: null;
	}
	
}
