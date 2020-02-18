/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

/**
 * Wrapper per la modifica al movimento di gestione di entrata nella registrazioneMovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public class ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin extends ElementoModificaMovimentoGestioneRegistrazioneMovFin<ModificaMovimentoGestioneEntrata> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5261652180350630021L;

	/**
	 * Costruttore di wrap.
	 * 
	 * @param modificaMovimentoGestione il modificaMovimentoGestione da wrappare
	 */
	public ElementoModificaMovimentoGestioneEntrataRegistrazioneMovFin(ModificaMovimentoGestioneEntrata modificaMovimentoGestione) {
		super(modificaMovimentoGestione);
	}
	
	@Override
	public String getDomStringDescrizione() {
		if(modificaMovimentoGestione == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a rel=\"popover\" href=\"#\" data-original-title=\"Motivo\" data-trigger=\"hover\" data-content=\"")
			.append(modificaMovimentoGestione.getMotivoModificaEntrata() != null ? FormatUtils.formatHtmlAttributeString(modificaMovimentoGestione.getMotivoModificaEntrata().getDescrizione()) : "")
			.append("\">")
			.append(modificaMovimentoGestione.getDescrizioneModificaMovimentoGestione())
			.append("</a>")
			.toString();
	}
	
}
