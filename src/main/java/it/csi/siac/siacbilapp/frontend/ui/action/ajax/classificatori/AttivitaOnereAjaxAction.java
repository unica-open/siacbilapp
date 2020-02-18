/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericClassificatoriAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe per il caricamento <em>AJAX</em> dell'Attivita Onere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AttivitaOnereAjaxAction extends GenericClassificatoriAjaxAction<RicercaAttivitaOnere, RicercaAttivitaOnereResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7872457175647166322L;
	
	@Autowired private transient DocumentoService documentoService;

	@Override
	protected RicercaAttivitaOnere definisciRequest(Integer id) {
		RicercaAttivitaOnere request = new RicercaAttivitaOnere();
		
		request.setDataOra(new Date());
		request.setEnte(model.getEnte());
		request.setRichiedente(model.getRichiedente());
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(id);
		
		request.setTipoOnere(tipoOnere);
		
		return request;
	}

	@Override
	protected RicercaAttivitaOnereResponse ottieniResponse(RicercaAttivitaOnere request) {
		return documentoService.ricercaAttivitaOnere(request);
	}

	@Override
	protected void impostaLaResponseInSessione(RicercaAttivitaOnereResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_ATTIVITA_ONERE, response.getElencoAttivitaOnere());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(RicercaAttivitaOnereResponse response) {
		// Nothing to do!
	}

	@Override
	protected void injettaResponseNelModel(RicercaAttivitaOnereResponse response) {
		model.setListaAttivitaOnere(response.getElencoAttivitaOnere());
	}

}
