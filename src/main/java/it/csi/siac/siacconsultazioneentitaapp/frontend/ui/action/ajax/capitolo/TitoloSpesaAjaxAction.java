/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax.capitolo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo.TitoloSpesaAjaxModel;

/**
 * @author Pro Logic
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class TitoloSpesaAjaxAction extends TitoloBaseAjaxAction<TitoloSpesaAjaxModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 6825530619041467119L;

	@Override
	protected String getCodiceTipoElementoBilancio(){
		return BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant();		
	}
	
	@Override
	protected void impostaListaDaResponse(LeggiClassificatoriByTipoElementoBilResponse response){
		model.setListaTitoloSpesa(response.getClassificatoriTitoloSpesa());
		// Imposto la lista in sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_SPESA, response.getClassificatoriTitoloSpesa());
	}
	

}
