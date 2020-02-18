/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin;

import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Wrapper per il subaccertamento nella registrazioneMovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ElementoSubAccertamentoRegistrazioneMovFin extends ElementoSubMovimentoGestioneRegistrazioneMovFin<SubAccertamento> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5997642273816487483L;

	/**
	 * Costruttore di wrap.
	 * 
	 * @param subAccertamento il subImpegno da wrappare
	 */
	public ElementoSubAccertamentoRegistrazioneMovFin(SubAccertamento subAccertamento) {
		super(subAccertamento);
	}
	
	@Override
	public String ottieniCodiceStato() {
		if(subMovimentoGestione == null) {
			return "";
		}
		return subMovimentoGestione.getStatoOperativoMovimentoGestioneEntrata();
	}
	
	@Override
	public String ottieniDescrizioneStato() {
		if(subMovimentoGestione == null) {
			return "";
		}
		return subMovimentoGestione.getDescrizioneStatoOperativoMovimentoGestioneEntrata();
	}
	
}
