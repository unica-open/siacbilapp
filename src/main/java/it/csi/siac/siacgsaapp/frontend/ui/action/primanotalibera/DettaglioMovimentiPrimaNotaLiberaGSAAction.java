/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotalibera;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera.DettaglioMovimentiPrimaNotaLiberaGSAModel;
/**
 * Classe di action per il dettaglio dei movimenti della prima nota libera-
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioMovimentiPrimaNotaLiberaGSAAction extends DettaglioMovimentiPrimaNotaLiberaBaseAction <DettaglioMovimentiPrimaNotaLiberaGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6138873868020716496L;

	/**
	 * Caricamento dei movimenti
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimenti(){
		return super.caricaMovimentiBase();
	}

}
