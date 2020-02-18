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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe per il caricamento <em>AJAX</em> della Causale 770.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class Causale770AjaxAction extends GenericClassificatoriAjaxAction<RicercaCausale770, RicercaCausale770Response> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7872457175647166322L;
	
	@Autowired private transient DocumentoService documentoService;

	@Override
	protected RicercaCausale770 definisciRequest(Integer id) {
		RicercaCausale770 request = new RicercaCausale770();
		
		request.setDataOra(new Date());
		request.setEnte(model.getEnte());
		request.setRichiedente(model.getRichiedente());
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(id);
		
		request.setTipoOnere(tipoOnere);
		
		return request;
	}

	@Override
	protected RicercaCausale770Response ottieniResponse(RicercaCausale770 request) {
		return documentoService.ricercaCausale770(request);
	}

	@Override
	protected void impostaLaResponseInSessione(RicercaCausale770Response response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_770, response.getElencoCausali());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(RicercaCausale770Response response) {
		// Nothing to do!
	}

	@Override
	protected void injettaResponseNelModel(RicercaCausale770Response response) {
		model.setListaCausale770(response.getElencoCausali());
	}

}
