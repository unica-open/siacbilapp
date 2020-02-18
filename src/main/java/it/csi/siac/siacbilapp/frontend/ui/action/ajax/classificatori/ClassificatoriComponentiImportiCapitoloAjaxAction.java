/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericClassificatoriByRelazioneAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe per il caricamento <em>AJAX</em> della lista Titolo.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 01/07/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ClassificatoriComponentiImportiCapitoloAjaxAction extends GenericClassificatoriByRelazioneAjaxAction {

	/**
	 * Per la Serializzazione 
	 */
	private static final long serialVersionUID = -4569567642370889748L;
	
	
	

	
	@Override
	protected void impostaLaResponseInSessione(LeggiClassificatoriByRelazioneResponse response) {
      sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, response.getClassificatoriTitoloSpesa());
      sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICAZIONE_COFOG, response.getClassificatoriClassificazioneCofog());
	
	}

	@Override
	protected void impostaPerInjezioneInVariazione(LeggiClassificatoriByRelazioneResponse response) {
		injettaInVariazione(TipologiaClassificatore.TITOLO_SPESA, response.getClassificatoriTitoloSpesa());
		injettaInVariazione(TipologiaClassificatore.CLASSIFICAZIONE_COFOG, response.getClassificatoriClassificazioneCofog());

	}

	@Override
	protected void injettaResponseNelModel(LeggiClassificatoriByRelazioneResponse response) {
		model.setListaClassificazioneCofog(response.getClassificatoriClassificazioneCofog());
		model.setListaTitoloSpesa(response.getClassificatoriTitoloSpesa());
	}
	
	@Override
	protected void setDefaultValues() {
		List<TitoloSpesa> titoloSpesaOriginale = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA_ORIGINALE);
		model.setListaTitoloSpesa(titoloSpesaOriginale);
	}
	
	@Override
	protected void responseTransform(LeggiClassificatoriByRelazione request, LeggiClassificatoriByRelazioneResponse response) {
		if(request.getIdClassif() == null || request.getIdClassif().equals(Integer.valueOf(0))) {
			// Se non ho passato i dati del classificatore, RIPRISTINO il titolo spesa originale
			List<TitoloSpesa> titoloSpesaNellaResponse = response.getClassificatoriTitoloSpesa();
			List<TitoloSpesa> titoloSpesaOriginale = sessionHandler.getParametro(BilSessionParameter.LISTA_TITOLO_SPESA_ORIGINALE);
			
			response.getClassificatori().removeAll(titoloSpesaNellaResponse);
			response.getClassificatori().addAll(titoloSpesaOriginale);
		}
	}
}
