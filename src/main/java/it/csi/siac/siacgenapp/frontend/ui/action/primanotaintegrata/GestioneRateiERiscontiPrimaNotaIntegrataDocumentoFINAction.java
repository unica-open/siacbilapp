/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacgenapp.frontend.ui.model.primanotaintegrata.GestioneRateiERiscontiPrimaNotaIntegrataDocumentoFINModel;
/**
 * Classe di action per la gestione di Ratei e Risconti della prima nota integrata.
 * 
 * @author Valentina
 * @version 1.0.0 - 11/07/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class GestioneRateiERiscontiPrimaNotaIntegrataDocumentoFINAction extends  GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseAction<GestioneRateiERiscontiPrimaNotaIntegrataDocumentoFINModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1752076326776082425L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		return super.execute();
	}


	
}
