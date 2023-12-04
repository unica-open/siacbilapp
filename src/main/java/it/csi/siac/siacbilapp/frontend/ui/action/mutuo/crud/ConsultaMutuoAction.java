/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.crud;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud.ConsultaMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;
import xyz.timedrain.arianna.plugin.BreadCrumb;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaMutuoAction extends BaseMutuoAction<ConsultaMutuoModel> {


	private static final long serialVersionUID = -506512517381413029L;


	private static final MutuoModelDetail[] DETTAGLIO_MUTUO_MODEL_DETAILS = 
			ArrayUtil.concat(MutuoModelDetail.class, 
				MutuoModelDetail.RipartizioneCapitoliConTotali,
				MutuoModelDetail.Dettaglio, 
				MutuoModelDetail.PianoAmmortamentoConTotali,
				MutuoModelDetail.MovimentiGestioneAssociatiConTotali,
				MutuoModelDetail.ProgettiAssociatiConTotali
			);
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		RicercaDettaglioMutuoResponse response = mutuoActionHelper.ricercaDettaglioMutuo(DETTAGLIO_MUTUO_MODEL_DETAILS);
		
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioMutuo.class, response));
			addErrori(response);
			return INPUT;
		}
		
		model.setMutuoFromResponse(response);
		
		mutuoActionHelper.sortMovimentiGestioneAssociati();

		return SUCCESS;
	}

	public String scaricaPianoAmmortamentoExcel() throws Exception {

		model.setMutuo(mutuoActionHelper.leggiDettaglioMutuo(MutuoModelDetail.PianoAmmortamento));
		
		mutuoActionHelper.scaricaPianoAmmortamentoExcel();
		
		return SUCCESS;
	}

	public String scaricaMovimentiGestioneAssociatiExcel() throws Exception {

		mutuoActionHelper.scaricaMovimentiGestioneAssociatiExcel();
		
		return SUCCESS;
	}

	public String scaricaProgettiAssociatiExcel() throws Exception {

		mutuoActionHelper.scaricaProgettiAssociatiExcel();
		
		return SUCCESS;
	}
	
	public String scaricaRipartizioneExcel() throws Exception {

		model.setMutuo(mutuoActionHelper.leggiDettaglioMutuo(MutuoModelDetail.RipartizioneCapitoliConTotali));

		mutuoActionHelper.scaricaRipartizioneExcel();
		
		return SUCCESS;
	}
}
