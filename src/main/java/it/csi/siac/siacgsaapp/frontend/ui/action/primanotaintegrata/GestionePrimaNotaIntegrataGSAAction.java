/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.GestionePrimaNotaIntegrataBaseAction;
import it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata.GestionePrimaNotaIntegrataGSAModel;

/**
 * Classe di action per la gestione della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestionePrimaNotaIntegrataGSAAction extends GestionePrimaNotaIntegrataBaseAction<GestionePrimaNotaIntegrataGSAModel> {

	/** per serializzazione **/
	private static final long serialVersionUID = -1470003365204897206L;

}
