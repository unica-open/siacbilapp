/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.aggiornamento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna.AggiornaContoPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataGSAModel;

/**
 * Classe di action per l'aggiornamento dei conti della prima nota integrata. Modulo GEN
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaPrimaNotaIntegrataBaseAction.MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_GSA)
public class AggiornaContoPrimaNotaIntegrataGSAAction extends AggiornaContoPrimaNotaIntegrataBaseAction<AggiornaPrimaNotaIntegrataGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8704281885897513514L;
	
}
