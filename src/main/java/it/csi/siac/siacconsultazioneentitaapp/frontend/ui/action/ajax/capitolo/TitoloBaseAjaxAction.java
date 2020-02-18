/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action.ajax.capitolo;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;

/**
 * @author Elisa Chiari
 * @version 1.0.0 - 16/02/2016
 * @param <M> la tipizzazione del model
 */

public abstract class TitoloBaseAjaxAction<M extends GenericBilancioModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 7919971007099435797L;
	/** Utility per il log */
	@Autowired /* @CachedService */
	protected transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(getCodiceTipoElementoBilancio());
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio leggiClassificatoriByTipoElementoBil");
			addErrori(response);
			return SUCCESS;
		}
		impostaListaDaResponse(response);
		
		return SUCCESS;	

	}

	/**
	 * Imposta la lista dalla response
	 * @param response la response
	 */
	protected abstract void impostaListaDaResponse(LeggiClassificatoriByTipoElementoBilResponse response);

	/**
	 * Ottiene il codice dell'tipo elemento di bilancio
	 * @return il codice
	 */
	protected abstract String getCodiceTipoElementoBilancio();


	
}
