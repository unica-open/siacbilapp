/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.tipobenecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite.ConsultaTipoBeneModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;

/**
 * The Class ConsultaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaTipoBeneAction extends GenericTipoBeneAction<ConsultaTipoBeneModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 9021669725330286849L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setTipoBeneCespite(null);
		RicercaDettaglioTipoBeneCespiteResponse response = ottieniRicercaDettaglioTipoBeneCespiteResponse();
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		model.impostaDatiNelModel(response.getTipoBeneCespite());
		return SUCCESS;		
	}
	

}
