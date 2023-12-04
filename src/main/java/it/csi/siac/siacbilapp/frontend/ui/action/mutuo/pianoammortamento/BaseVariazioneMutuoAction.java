/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.pianoammortamento;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento.BaseVariazioneMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;

public abstract class BaseVariazioneMutuoAction<BVMM extends BaseVariazioneMutuoModel> extends BaseMutuoAction<BVMM> {


	private static final long serialVersionUID = 664968619947664744L;

	
	@Override
	public String enterPage() throws Exception {
		super.enterPage();
		
		final String methodName = "enterPage";

		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo();
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		model.setMutuoFromResponse(response);

		model.initListaNumeriRataAnno();

		return SUCCESS;
	}

	

	public String salva() throws Exception {
		final String methodName = "salva";
		
		InserisciVariazioneMutuo request = model.creaRequestInserisciVariazioneMutuo();
		InserisciVariazioneMutuoResponse response = mutuoService.inserisciVariazioneMutuo(request);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(InserisciVariazioneMutuo.class, response));

			Mutuo mutuo = mutuoActionHelper.leggiDettaglioMutuo(MutuoModelDetail.PeriodoRimborso);
			
			model.getMutuo().setPeriodoRimborso(mutuo.getPeriodoRimborso());

			model.initListaNumeriRataAnno();
			
			addErrori(response);
			
			return INPUT;
		}
		
		addInformazione(MessaggioCore.OPERAZIONE_COMPLETATA.getMessaggio());
		
		return SUCCESS;
	}
}
