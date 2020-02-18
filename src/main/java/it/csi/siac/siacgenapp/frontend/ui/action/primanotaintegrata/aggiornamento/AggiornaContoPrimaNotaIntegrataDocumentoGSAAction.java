/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata.aggiornamento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna.AggiornaContoPrimaNotaIntegrataDocumentoBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoGSAModel;

/**
 * Classe di action per l'aggiornamento dei conti della prima nota integrata. Modulo GEN
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaPrimaNotaIntegrataDocumentoBaseAction.MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_DOCUMENTO_GSA)
public class AggiornaContoPrimaNotaIntegrataDocumentoGSAAction extends AggiornaContoPrimaNotaIntegrataDocumentoBaseAction<AggiornaPrimaNotaIntegrataDocumentoGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3450155345601617005L;


}
