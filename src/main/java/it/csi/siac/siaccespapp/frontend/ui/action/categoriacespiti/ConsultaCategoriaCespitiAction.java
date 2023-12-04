/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.categoriacespiti;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti.ConsultaCategoriaCespitiModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class GenericTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCategoriaCespitiAction extends GenericCategoriaCespitiAction<ConsultaCategoriaCespitiModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -2323816010057510902L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoCalcolo();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName ="execute";
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setCategoriaCespiti(null);
		RicercaDettaglioCategoriaCespitiResponse response = ottieniRicercaDettaglioCategoriaCespitiResponse();
		if(response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errrori nel reperimento della categoria.");
			return INPUT;
		}
		CategoriaCespiti categoriaCespiti = response.getCategoriaCespiti();
		if(categoriaCespiti == null || categoriaCespiti.getUid() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("categoria cespiti con uid + " + model.getUidCategoriaCespiti()));
			return INPUT;
		}
		model.setCategoriaCespiti(categoriaCespiti);
		return SUCCESS;		
	}

}
