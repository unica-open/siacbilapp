/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale.DettaglioMovimentiPrimaNotaIntegrataManualeGSAModel;
/**
 * Classe di action per il dettaglio dei movimenti della prima nota libera-
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 21/12/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioMovimentiPrimaNotaIntegrataManualeGSAAction extends DettaglioMovimentiPrimaNotaLiberaBaseAction <DettaglioMovimentiPrimaNotaIntegrataManualeGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3084879764678048750L;

	/**
	 * Caricamento dei movimenti
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimenti(){
		return super.caricaMovimentiBase();
	}

}
