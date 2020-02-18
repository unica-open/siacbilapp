/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericClassificatoriAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe per il caricamento <em>AJAX</em> della Classificazione Cofog.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ClassificazioneCofogAjaxAction extends GenericClassificatoriAjaxAction<LeggiClassificatoriByRelazione, LeggiClassificatoriByRelazioneResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5536829604897226577L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	protected LeggiClassificatoriByRelazione definisciRequest(Integer id) {
		return model.creaRequestLeggiClassificatoriByRelazione(id);
	}

	@Override
	protected LeggiClassificatoriByRelazioneResponse ottieniResponse(LeggiClassificatoriByRelazione request) {
		return classificatoreBilService.leggiClassificatoriByRelazione(request);
	}
	
	@Override
	protected void impostaLaResponseInSessione(LeggiClassificatoriByRelazioneResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICAZIONE_COFOG, response.getClassificatoriClassificazioneCofog());
	}
	
	@Override
	protected void impostaPerInjezioneInVariazione(LeggiClassificatoriByRelazioneResponse response) {
		injettaInVariazione(TipologiaClassificatore.CLASSIFICAZIONE_COFOG, response.getClassificatoriClassificazioneCofog());
	}
	
	@Override
	protected void injettaResponseNelModel(LeggiClassificatoriByRelazioneResponse response) {
		model.setListaClassificazioneCofog(response.getClassificatoriClassificazioneCofog());
	}
	
}
