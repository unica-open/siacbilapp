/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.movimentogestione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.movimentogestione.RisultatiRicercaSubImpegniAjaxModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di Action per la gestione della paginazione dei subimpegni associati ad un Impegno
 * @author Elisa Chiari
 * @version 1.0.0 - 29/08/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSubImpegniAjaxAction extends GenericRisultatiRicercaAjaxAction<SubImpegno, RisultatiRicercaSubImpegniAjaxModel, SubImpegno, RicercaImpegnoPerChiaveOttimizzato, RicercaImpegnoPerChiaveOttimizzatoResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7555311758945734670L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSubImpegniAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI);
		numeroPaginaIniziale = 1;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaImpegnoPerChiaveOttimizzato request) {
		
		return new ParametriPaginazione(request.getNumPagina(), request.getNumRisultatiPerPagina());
	}

	@Override
	protected void impostaParametriPaginazione(RicercaImpegnoPerChiaveOttimizzato request,
			ParametriPaginazione parametriPaginazione) {
		request.setNumPagina(parametriPaginazione.getNumeroPagina());
		request.setNumRisultatiPerPagina(parametriPaginazione.getElementiPerPagina());
		
	}

	@Override
	protected SubImpegno ottieniIstanza(SubImpegno e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaImpegnoPerChiaveOttimizzatoResponse ottieniResponse(RicercaImpegnoPerChiaveOttimizzato request) {
		
		return movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
	}
	
	@Override
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return super.ottieniBloccoNumero(parametriPaginazione) - 1;
	}
	

	@Override
	protected ListaPaginata<SubImpegno> ottieniListaRisultati(RicercaImpegnoPerChiaveOttimizzatoResponse response) {
		return toListaPaginata(response.getImpegno().getElencoSubImpegni(), response.getNumeroPaginaSubRestituita(), response.getNumeroTotaleSub());
				
	}

}
