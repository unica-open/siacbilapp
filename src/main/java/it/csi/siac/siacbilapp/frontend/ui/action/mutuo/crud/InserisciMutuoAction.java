/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.crud;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud.InserisciMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuoResponse;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciMutuoAction extends BaseInserisciAggiornaMutuoAction<InserisciMutuoModel> {

	private static final long serialVersionUID = 2020318842439985171L;


	public String salva() {
		beforeSalva();
		
		InserisciMutuo req = model.creaRequestInserisciMutuo();
		logServiceRequest(req);
		
		InserisciMutuoResponse res = mutuoService.inserisciMutuo(req);
		logServiceResponse(res);

		if (res.hasErrori()) {
			addErrori(res);
			log.debug("salva", "Si sono verificati errori nell'invocazione del servizio");
			prepareEnterPage();
			return INPUT;
		}else {
			impostaInformazioneSuccesso();
			impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
			saveIdMutuoIntoSession(res.getMutuo().getUid());
		}

		model.setMutuo(res.getMutuo());

		return SUCCESS;
	}
}
