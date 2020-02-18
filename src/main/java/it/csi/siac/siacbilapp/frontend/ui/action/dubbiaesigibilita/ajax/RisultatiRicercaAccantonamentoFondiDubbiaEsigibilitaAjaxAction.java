/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.dubbiaesigibilita.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.ajax.RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxModel;
import it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per la gestione dei risultati di ricerca dei fondi accantonamento dubbia esigibilita
 * @author Elisa Chiari
 * @version 1.0.0 - 02/11/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxAction extends GenericRisultatiRicercaAjaxAction<AccantonamentoFondiDubbiaEsigibilita, RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxModel, AccantonamentoFondiDubbiaEsigibilita, RicercaSinteticaFondiDubbiaEsigibilita, RicercaSinteticaFondiDubbiaEsigibilitaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3720115772675193497L;
	@Autowired private transient FondiDubbiaEsigibilitaService fondiDubbiaEsigibilitaService;
	
	/**
	 * Costruttore
	 */
	public RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjaxAction(){
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaFondiDubbiaEsigibilita request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaFondiDubbiaEsigibilita request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilita ottieniIstanza(AccantonamentoFondiDubbiaEsigibilita e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaFondiDubbiaEsigibilitaResponse ottieniResponse(RicercaSinteticaFondiDubbiaEsigibilita request) {
		return fondiDubbiaEsigibilitaService.ricercaSinteticaFondiDubbiaEsigibilita(request);
	}

	@Override
	protected ListaPaginata<AccantonamentoFondiDubbiaEsigibilita> ottieniListaRisultati(RicercaSinteticaFondiDubbiaEsigibilitaResponse response) {
		return response.getAccantonamentiFondiDubbiaEsigibilita();
	}

}
