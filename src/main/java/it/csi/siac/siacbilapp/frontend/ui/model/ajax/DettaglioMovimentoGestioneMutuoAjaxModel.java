/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.movimentogestione.ElementoMovimentoGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioMovimentoGestione;
import it.csi.siac.siacfinser.model.MovimentoGestione;



public abstract class DettaglioMovimentoGestioneMutuoAjaxModel<RDMGREQ extends RicercaDettaglioMovimentoGestione<? extends MovimentoGestione>> 
	extends DataTableAjaxModel<ElementoMovimentoGestione, RDMGREQ> {

	private static final long serialVersionUID = 8470088129080166325L;
}
