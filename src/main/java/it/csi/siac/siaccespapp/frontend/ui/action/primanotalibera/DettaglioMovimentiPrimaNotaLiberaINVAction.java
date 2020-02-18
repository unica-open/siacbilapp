/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.primanotalibera;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotalibera.DettaglioMovimentiPrimaNotaLiberaFINModel;
/**
 * Classe di action per il dettaglio dei movimenti della prima nota libera-
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioMovimentiPrimaNotaLiberaINVAction extends DettaglioMovimentiPrimaNotaLiberaBaseAction <DettaglioMovimentiPrimaNotaLiberaFINModel> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7058523713784359471L;

	/**
	 * Caricamento dei movimenti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimenti(){
		return super.caricaMovimentiBase();
	}

}
