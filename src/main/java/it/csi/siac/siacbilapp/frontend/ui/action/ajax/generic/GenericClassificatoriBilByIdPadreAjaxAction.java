/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;


/**
 * Classe per il caricamento <em>AJAX</em> del classificatori tramite id del padre.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
public abstract class GenericClassificatoriBilByIdPadreAjaxAction extends GenericClassificatoriAjaxAction<LeggiClassificatoriBilByIdPadre, LeggiClassificatoriBilByIdPadreResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8945626895107730019L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	protected LeggiClassificatoriBilByIdPadre definisciRequest(Integer id) {
		return model.creaRequestLeggiClassificatoriBilByIdPadre(id);
	}

	@Override
	protected LeggiClassificatoriBilByIdPadreResponse ottieniResponse(LeggiClassificatoriBilByIdPadre request) {
		return classificatoreBilService.leggiClassificatoriByIdPadre(request);
	}

}
