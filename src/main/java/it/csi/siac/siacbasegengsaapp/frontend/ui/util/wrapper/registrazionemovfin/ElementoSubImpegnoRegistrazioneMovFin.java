/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin;

import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Wrapper per il subimpegno nella registrazioneMovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ElementoSubImpegnoRegistrazioneMovFin extends ElementoSubMovimentoGestioneRegistrazioneMovFin<SubImpegno> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -14210693764384064L;

	/**
	 * Costruttore di wrap.
	 * 
	 * @param subImpegno il subImpegno da wrappare
	 */
	public ElementoSubImpegnoRegistrazioneMovFin(SubImpegno subImpegno) {
		super(subImpegno);
	}
	
	@Override
	public String ottieniCodiceStato() {
		if(subMovimentoGestione == null) {
			return "";
		}
		return subMovimentoGestione.getStatoOperativoMovimentoGestioneSpesa();
	}
	
	@Override
	public String ottieniDescrizioneStato() {
		if(subMovimentoGestione == null) {
			return "";
		}
		return subMovimentoGestione.getDescrizioneStatoOperativoMovimentoGestioneSpesa();
	}
}
