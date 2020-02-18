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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.NaturaOnere;

/**
 * Classe per il caricamento <em>AJAX</em> del Tipo Onere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class TipoOnereAjaxAction extends GenericClassificatoriAjaxAction<RicercaTipoOnere, RicercaTipoOnereResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7872457175647166322L;
	
	@Autowired private transient DocumentoService documentoService;

	@Override
	protected RicercaTipoOnere definisciRequest(Integer id) {
		RicercaTipoOnere request = new RicercaTipoOnere();
		
		request.setDataOra(new Date());
		request.setEnte(model.getEnte());
		request.setRichiedente(model.getRichiedente());
		
		NaturaOnere naturaOnere = new NaturaOnere();
		naturaOnere.setUid(id);
		
		request.setNaturaOnere(naturaOnere);
		
		return request;
	}

	@Override
	protected RicercaTipoOnereResponse ottieniResponse(RicercaTipoOnere request) {
		return documentoService.ricercaTipoOnere(request);
	}

	@Override
	protected void impostaLaResponseInSessione(RicercaTipoOnereResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ONERE, response.getElencoTipiOnere());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(RicercaTipoOnereResponse response) {
		// Nothing to do!
	}

	@Override
	protected void injettaResponseNelModel(RicercaTipoOnereResponse response) {
		model.setListaTipoOnere(response.getElencoTipiOnere());
	}

}
