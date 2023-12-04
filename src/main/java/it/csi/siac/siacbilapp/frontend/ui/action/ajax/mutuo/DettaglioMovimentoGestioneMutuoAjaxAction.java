/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.DataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.DettaglioMovimentoGestioneMutuoAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoMovimentoGestione;
import it.csi.siac.siacbilser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioMovimentoGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioMovimentoGestioneResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class DettaglioMovimentoGestioneMutuoAjaxAction<
	DMGMAM extends DettaglioMovimentoGestioneMutuoAjaxModel<RDMGREQ>,
	MG extends MovimentoGestione, 
	RDMGREQ extends RicercaDettaglioMovimentoGestione<MG>, 
	RDMGRES extends RicercaDettaglioMovimentoGestioneResponse<MG>> 
	extends DataTableAjaxAction<
		ElementoMovimentoGestione, 
		DMGMAM, 
		MG, 
		RDMGREQ, 
		RDMGRES> {


	private static final long serialVersionUID = -6446846084535980423L;

	
	@Autowired @Qualifier("movimentoGestioneBilService")
	protected MovimentoGestioneService movimentoGestioneBilService;

}
