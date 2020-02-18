/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericClassificatoriBilByIdPadreAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadreResponse;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe per il caricamento <em>AJAX</em> della Categoria Tipologia Titolo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CategoriaTipologiaTitoloAjaxAction extends GenericClassificatoriBilByIdPadreAjaxAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1503964111710346686L;

	@Override
	protected void impostaLaResponseInSessione(LeggiClassificatoriBilByIdPadreResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_CATEGORIA_TIPOLOGIA_TITOLO, response.getClassificatoriCategoriaTipologiaTitolo());
	}
	
	@Override
	protected void impostaPerInjezioneInVariazione(LeggiClassificatoriBilByIdPadreResponse response) {
		injettaInVariazione(TipologiaClassificatore.CATEGORIA, response.getClassificatoriCategoriaTipologiaTitolo());
	}
	
	@Override
	protected void injettaResponseNelModel(LeggiClassificatoriBilByIdPadreResponse response) {
		model.setListaCategoriaTipologiaTitolo(response.getClassificatoriCategoriaTipologiaTitolo());
	}
	
}
