/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.tipoambito.helper;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.BaseActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

public class TipoAmbitoActionHelper extends BaseActionHelper {

	@Autowired protected transient ProgettoService progettoService;

	public TipoAmbitoActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
	}

	public List<TipoAmbito> caricaListaTipoAmbito() throws WebServiceInvocationFailureException {
		List<TipoAmbito> listaInSessione = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_TIPO_AMBITO);
		if(listaInSessione == null) {
			
			RicercaTipiAmbito request = creaRequestRicercaTipiAmbito();
			action.logServiceRequest(request);
			RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);
			
			action.logServiceResponse(response);
			
			if(response.hasErrori()) {
				action.addErrori(response);
				throw new WebServiceInvocationFailureException(action.createErrorInServiceInvocationString(LeggiClassificatoriGenericiByTipoElementoBil.class, response));
			}
			listaInSessione = response.getTipiAmbito();
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_TIPO_AMBITO, listaInSessione);
		}
		
		return listaInSessione;
	}

	public RicercaTipiAmbito creaRequestRicercaTipiAmbito() {
		RicercaTipiAmbito request = new RicercaTipiAmbito();
		request.setEnte(action.getModel().getEnte());
		request.setAnno(action.getSessionHandler().getAnnoBilancio());
		request.setDataOra(new Date());
		request.setRichiedente(action.getModel().getRichiedente());
		return request;
	}	
}
