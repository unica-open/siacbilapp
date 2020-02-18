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
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ajax.capitolo.TitoloEntrataAjaxModel;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 16/02/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class TitoloEntrataAjaxAction extends TitoloBaseAjaxAction<TitoloEntrataAjaxModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 7919971007099435797L;

	@Override
	protected String getCodiceTipoElementoBilancio(){
		return BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant();		
	}
	
	@Override
	protected void impostaListaDaResponse(LeggiClassificatoriByTipoElementoBilResponse response){
		model.setListaTitoloEntrata(response.getClassificatoriTitoloEntrata());
		log.logXmlTypeObject(response.getClassificatoriTitoloEntrata(), "listaTitolo entrata");
		// Imposto la lista in sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_TITOLO_ENTRATA, response.getClassificatoriTitoloEntrata());
	}
	
}
