/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.ajax.cassaeconomale.tabelle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccecapp.frontend.ui.model.ajax.cassaeconomale.tabelle.RisultatiRicercaTipoGiustificativoAjaxModel;
import it.csi.siac.siaccecser.frontend.webservice.CassaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Classe di action per i risultati di ricerca dei tipi di giustificativo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoGiustificativoAjaxAction extends GenericRisultatiRicercaAjaxAction<TipoGiustificativo,
	RisultatiRicercaTipoGiustificativoAjaxModel, TipoGiustificativo, RicercaSinteticaTipoGiustificativo, RicercaSinteticaTipoGiustificativoResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6244090919129582326L;
	
	@Autowired private transient CassaEconomaleService cassaEconomaleService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaTipoGiustificativoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_TIPO_GIUSTIFICATIVO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_TIPO_GIUSTIFICATIVO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaTipoGiustificativo request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaTipoGiustificativo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected TipoGiustificativo ottieniIstanza(TipoGiustificativo e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaTipoGiustificativoResponse ottieniResponse(RicercaSinteticaTipoGiustificativo request) {
		return cassaEconomaleService.ricercaSinteticaTipoGiustificativo(request);
	}

	@Override
	protected ListaPaginata<TipoGiustificativo> ottieniListaRisultati(RicercaSinteticaTipoGiustificativoResponse response) {
		return response.getTipiGiustificativo();
	}

}
