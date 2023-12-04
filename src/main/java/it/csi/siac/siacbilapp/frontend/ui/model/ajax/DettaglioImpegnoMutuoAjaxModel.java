/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegno;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Impegno;



public class DettaglioImpegnoMutuoAjaxModel extends DettaglioMovimentoGestioneMutuoAjaxModel<RicercaDettaglioImpegno> {

	private static final long serialVersionUID = 8470088129080166325L;

	private Impegno impegno;
	
	@Override
	public RicercaDettaglioImpegno buildRequest() {
		RicercaDettaglioImpegno request = creaRequest(RicercaDettaglioImpegno.class);

		request.setImpegno(getImpegno());
		request.setImpegnoModelDetails(ImpegnoModelDetail.ElencoDettagliPerBilancio);
		
		return request;
	}

	public Impegno getImpegno() {
		return impegno;
	}

	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

		
}
