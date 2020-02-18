/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax.provvedimento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.provvedimento.TipoAttoAjaxModel;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 17/02/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class TipoAttoAjaxAction extends GenericBilancioAction<TipoAttoAjaxModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 2559012957642689140L;
	/** Servizi del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	
	@Override
	public String execute(){
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		model.setListaTipoAtto(listaTipoAtto);
		
		return SUCCESS;
	}
}
