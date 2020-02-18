/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;


/**
 * Classe per il caricamento <em>AJAX</em> del classificatori tramite id del padre.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 01/07/2016
 *
 */
public abstract class GenericClassificatoriByRelazioneAjaxAction extends GenericClassificatoriAjaxAction<LeggiClassificatoriByRelazione, LeggiClassificatoriByRelazioneResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8945626895107730019L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	protected LeggiClassificatoriByRelazione definisciRequest(Integer id) {
		return model.creaRequestLeggiClassificatoriByRelazione(id); 
	}

	@Override
	protected LeggiClassificatoriByRelazioneResponse ottieniResponse(LeggiClassificatoriByRelazione request) {
		return classificatoreBilService.leggiClassificatoriByRelazione(request);
	}

}
