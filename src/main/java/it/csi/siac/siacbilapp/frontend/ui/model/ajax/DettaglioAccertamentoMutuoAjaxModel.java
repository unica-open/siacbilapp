/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamento;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfinser.model.Accertamento;



public class DettaglioAccertamentoMutuoAjaxModel extends DettaglioMovimentoGestioneMutuoAjaxModel<RicercaDettaglioAccertamento> {

	private static final long serialVersionUID = 8470088129080166325L;

	private Accertamento accertamento;
	
	@Override
	public RicercaDettaglioAccertamento buildRequest() {
		RicercaDettaglioAccertamento request = creaRequest(RicercaDettaglioAccertamento.class);

		request.setAccertamento(getAccertamento());
		request.setAccertamentoModelDetails(AccertamentoModelDetail.ElencoDettagliPerBilancio);
		
		return request;
	}

	public Accertamento getAccertamento() {
		return accertamento;
	}

	public void setAccertamento(Accertamento acertamento) {
		this.accertamento = acertamento;
	}


		
}
