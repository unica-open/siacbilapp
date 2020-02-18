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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggettaResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe per il caricamento <em>AJAX</em> della Somma non soggetta.
 * 
 * @author Valentina Triolo
 * @version 1.0.0 31/05/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SommaNonSoggettaAjaxAction extends GenericClassificatoriAjaxAction<RicercaSommaNonSoggetta, RicercaSommaNonSoggettaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7872457175647166322L;
	
	@Autowired private transient DocumentoService documentoService;

	@Override
	protected RicercaSommaNonSoggetta definisciRequest(Integer id) {
		RicercaSommaNonSoggetta request = new RicercaSommaNonSoggetta();
		
		request.setDataOra(new Date());
		request.setRichiedente(model.getRichiedente());
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(id);
		
		request.setTipoOnere(tipoOnere);
		
		return request;
	}

	@Override
	protected RicercaSommaNonSoggettaResponse ottieniResponse(RicercaSommaNonSoggetta request) {
		return documentoService.ricercaSommaNonSoggetta(request);
	}

	@Override
	protected void impostaLaResponseInSessione(RicercaSommaNonSoggettaResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_SOMME_NON_SOGGETTE, response.getElencoCodiciSommaNonSoggetta());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(RicercaSommaNonSoggettaResponse response) {
		// Nothing to do!
	}

	@Override
	protected void injettaResponseNelModel(RicercaSommaNonSoggettaResponse response) {
		model.setCodiciSommaNonSoggetta(response.getElencoCodiciSommaNonSoggetta());
	}

}
