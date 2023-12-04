/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.contotesoreria.helper;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.BaseActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;

public class ContoTesoreriaActionHelper extends BaseActionHelper {

	@Autowired protected transient ContoTesoreriaService contoTesoreriaService;

	public ContoTesoreriaActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
	}

	public List<ContoTesoreria> caricaListaContoTesoreria() throws WebServiceInvocationFailureException {
		List<ContoTesoreria> list = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_CONTO_TESORERIA);
		
		if (list == null) {
			LeggiContiTesoreria req = creaRequestLeggiContiTesoreria();
			action.logServiceRequest(req);
			
			LeggiContiTesoreriaResponse response = contoTesoreriaService.leggiContiTesoreria(req);
			action.logServiceResponse(response);
			
			if(response.hasErrori()) {
				action.addErrori(response);
				throw new WebServiceInvocationFailureException(action.createErrorInServiceInvocationString(LeggiContiTesoreria.class, response));
			}
			
			list = response.getContiTesoreria();
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, list);
		}
		
		return list;
	}
	

	private LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = new LeggiContiTesoreria();

		request.setDataOra(new Date());
		request.setEnte(action.getModel().getEnte());
		request.setRichiedente(action.getModel().getRichiedente());

		return request;
	}
}
