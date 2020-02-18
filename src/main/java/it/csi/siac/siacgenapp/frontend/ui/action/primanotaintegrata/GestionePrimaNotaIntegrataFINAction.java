/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.GestionePrimaNotaIntegrataBaseAction;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.GestionePrimaNotaIntegrataFINModel;

/**
 * Classe di action per la gestione della prima nota integrata. Modulo GEN
 * 
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestionePrimaNotaIntegrataFINAction extends GestionePrimaNotaIntegrataBaseAction<GestionePrimaNotaIntegrataFINModel> {

	/** per serializzazione **/
	private static final long serialVersionUID = -314768482190287380L;
	
	/**
	 * Prepare imposta gestione da CDU documento.
	 *
	 * @throws Exception the exception
	 */
	public void prepareImpostaGestioneDaCDUDocumento() throws Exception {
		super.prepareExecute();
	}
	
	/**
	 * Imposta gestione da CDU documento.
	 *
	 * @return the string
	 * @throws Exception the exception
	 */
	public String impostaGestioneDaCDUDocumento() throws Exception {
		model.setFromCDUDocumento(true);
		return super.execute();
	}

}
