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
import it.csi.siac.siacbilapp.frontend.ui.model.dubbiaesigibilita.ajax.RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxModel;
import it.csi.siac.siacbilser.frontend.webservice.FondiDubbiaEsigibilitaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di Action per la gestione dei risultati di ricerca dei fondi accantonamento dubbia esigibilita
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxAction extends GenericRisultatiRicercaAjaxAction<AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxModel, AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		RicercaSinteticaFondiDubbiaEsigibilitaRendiconto, RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3720115772675193497L;
	@Autowired private transient FondiDubbiaEsigibilitaService fondiDubbiaEsigibilitaService;
	
	/**
	 * Costruttore vuoto di default
	 */
	public RisultatiRicercaAccantonamentoFondiDubbiaEsigibilitaRendicontoAjaxAction(){
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_ACCANTONAMENTO_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaFondiDubbiaEsigibilitaRendiconto request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaFondiDubbiaEsigibilitaRendiconto request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilitaRendiconto ottieniIstanza(AccantonamentoFondiDubbiaEsigibilitaRendiconto e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse ottieniResponse(RicercaSinteticaFondiDubbiaEsigibilitaRendiconto request) {
		return fondiDubbiaEsigibilitaService.ricercaSinteticaFondiDubbiaEsigibilitaRendiconto(request);
	}

	@Override
	protected ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaRendiconto> ottieniListaRisultati(RicercaSinteticaFondiDubbiaEsigibilitaRendicontoResponse response) {
		return response.getAccantonamentiFondiDubbiaEsigibilitaRendiconto();
	}

}
