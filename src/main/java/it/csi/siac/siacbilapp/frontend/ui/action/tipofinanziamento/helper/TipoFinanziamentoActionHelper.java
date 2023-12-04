/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.tipofinanziamento.helper;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.BaseActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

public class TipoFinanziamentoActionHelper extends BaseActionHelper {

	@Autowired protected transient ClassificatoreBilService classificatoreBilService;

	public TipoFinanziamentoActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
	}

	
	public List<TipoFinanziamento> caricaListaTipoFinanziamento() throws WebServiceInvocationFailureException {
		List<TipoFinanziamento> listaInSessione = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if(listaInSessione == null) {
			
			LeggiClassificatoriGenericiByTipoElementoBil request = creaRequestLeggiClassificatoriGenericiByTipoElementoBil();
			action.logServiceRequest(request);
			LeggiClassificatoriGenericiByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(request);
			action.logServiceResponse(response);
			
			if(response.hasErrori()) {
				action.addErrori(response);
				throw new WebServiceInvocationFailureException(action.createErrorInServiceInvocationString(LeggiClassificatoriGenericiByTipoElementoBil.class, response));
			}
			
			listaInSessione = response.getClassificatoriTipoFinanziamento();
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaInSessione);
		}
		
		return listaInSessione;
	}

	public LeggiClassificatoriGenericiByTipoElementoBil creaRequestLeggiClassificatoriGenericiByTipoElementoBil() {
		LeggiClassificatoriGenericiByTipoElementoBil request = new LeggiClassificatoriGenericiByTipoElementoBil();
		request.setAnno(action.getSessionHandler().getAnnoBilancio());
		request.setDataOra(new Date());
		request.setIdEnteProprietario(action.getModel().getEnte().getUid());
		request.setRichiedente(action.getModel().getRichiedente());
		request.setTipologiaClassificatore(TipologiaClassificatore.TIPO_FINANZIAMENTO);
		return request;
	}
}
