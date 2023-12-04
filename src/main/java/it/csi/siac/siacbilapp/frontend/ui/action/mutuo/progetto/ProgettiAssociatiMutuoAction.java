/*
.*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.progetto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.progetto.ProgettiAssociatiMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ProgettiAssociatiMutuoAction extends BaseMutuoAction<ProgettiAssociatiMutuoModel> {

	private static final long serialVersionUID = 1163245439373301859L;

	private static final MutuoModelDetail[] DETTAGLIO_MUTUO_MODEL_DETAILS = 
			ArrayUtil.concat(MutuoModelDetail.class, 
				MutuoModelDetail.Soggetto, 
				MutuoModelDetail.ProgettiAssociatiConTotali
			);
	
	@Override
	public String enterPage() throws Exception {
		super.enterPage();
		
		model.setMutuo(mutuoActionHelper.leggiDettaglioMutuo(DETTAGLIO_MUTUO_MODEL_DETAILS));
		
		return SUCCESS;
	}

	public String eliminaAssociazione() {
		
		EliminaAssociazioneProgettiMutuo req = model.creaRequestEliminaAssociazioneProgettiMutuo();
		EliminaAssociazioneProgettiMutuoResponse res = mutuoService.eliminaAssociazioneProgettiMutuo(req);	 
	
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	public String scaricaElencoExcel() throws Exception {

		mutuoActionHelper.scaricaProgettiAssociatiExcel();
		
		return SUCCESS;
		
	}	
}
