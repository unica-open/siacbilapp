/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.elaborazioniflussopagopa;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.elaborazioniflussopagopa.RisultatiRicercaElaborazioniFlussoModel;

/**
 * Action relativa ai risultati della ricerca per il provvedimento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/09/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaElaborazioniFlussoAction extends GenericElaborazioniFlussoAction<RisultatiRicercaElaborazioniFlussoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5762798503478748226L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		//SIAC-8005
		startPositionFromSessionParamter(methodName, BilSessionParameter.RIENTRO_POSIZIONE_START);
		
		return SUCCESS;
		
	}

	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid del documento da consultare: " + model.getUidDaConsultare());
		return SUCCESS;
	}
	
	
}
