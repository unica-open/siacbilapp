/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.dismissionecespite;

import java.util.Arrays;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite.InserisciDismissioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespiteResponse;

/**
 * The Class InserisciTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciDismissioneCespiteAction extends BaseInserisciAggiornaDismissioneCespiteAction<InserisciDismissioneCespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -388441147098736694L;

	/**
	 * Validate inserisci anagrafica.
	 */
	public void validateProseguiStep1Anagrafica() {
		validateDismissione();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		model.setDismissioneCespite(null);
		return SUCCESS;		
	}

	
	/**
	 * Inserisci anagrafica.
	 *
	 * @return the string
	 */
	public String proseguiStep1Anagrafica() {
		InserisciAnagraficaDismissioneCespite req = model.creaRequestInserisciAnagraficaDismissioneCespite();
		InserisciAnagraficaDismissioneCespiteResponse response = cespiteService.inserisciAnagraficaDismissioneCespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		model.setDismissioneCespite(response.getDismissioneCespite());
		model.setUidDismissioneCespite(response.getDismissioneCespite().getUid());
		if(model.getUidCespiteCollegamentoAutomatico() != 0) {
			model.setUidsCespitiDaCollegare(Arrays.asList(model.getUidCespiteCollegamentoAutomatico()));
			CollegaCespiteDismissioneCespiteResponse res = chiamaServizioCollegaCespiti();
			if(res.hasErrori()) {
				addErrori(res);
			}
		}
		
		return SUCCESS;
	}
	
	
	

}
