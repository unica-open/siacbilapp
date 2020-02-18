/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.variazionecespite;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite.BaseConsultaVariazioneCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespiteResponse;

/**
 * Classe base per la consultazione della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/08/2018
 *
 * @param <M> la tipizzazione del model
 */
public abstract class BaseConsultaVariazioneCespiteAction<M extends BaseConsultaVariazioneCespiteModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3307231057784789512L;
	
	@Autowired private CespiteService cespiteService;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() {
		RicercaDettaglioVariazioneCespite req = model.creaRequestRicercaDettaglioVariazioneCespite();
		RicercaDettaglioVariazioneCespiteResponse res = cespiteService.ricercaDettaglioVariazioneCespite(req);
		
		if(res.hasErrori()) {
			// Errori nella response. Esco
			// TODO: lanciare un'eccezione?
			addErrori(res);
			return INPUT;
		}
		// Imposto i dati nel model
		model.setVariazioneCespite(res.getVariazioneCespite());
		return SUCCESS;
	}
}
