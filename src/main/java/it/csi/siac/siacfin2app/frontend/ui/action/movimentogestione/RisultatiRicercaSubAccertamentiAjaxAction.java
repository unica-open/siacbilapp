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
import it.csi.siac.siacfin2app.frontend.ui.model.movimentogestione.RisultatiRicercaSubAccertamentiAjaxModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di Action per la gestione della paginazione dei subimpegni associati ad un Impegno
 * @author Elisa Chiari
 * @version 1.0.0 - 29/08/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSubAccertamentiAjaxAction extends GenericRisultatiRicercaAjaxAction<SubAccertamento, RisultatiRicercaSubAccertamentiAjaxModel, SubAccertamento, RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -7555311758945734670L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSubAccertamentiAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI);
		numeroPaginaIniziale = 1;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaAccertamentoPerChiaveOttimizzato request) {
		
		return new ParametriPaginazione(request.getNumPagina(), request.getNumRisultatiPerPagina());
	}

	@Override
	protected void impostaParametriPaginazione(RicercaAccertamentoPerChiaveOttimizzato request,
			ParametriPaginazione parametriPaginazione) {
		request.setNumPagina(parametriPaginazione.getNumeroPagina());
		request.setNumRisultatiPerPagina(parametriPaginazione.getElementiPerPagina());
		
	}

	@Override
	protected SubAccertamento ottieniIstanza(SubAccertamento e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaAccertamentoPerChiaveOttimizzatoResponse ottieniResponse(RicercaAccertamentoPerChiaveOttimizzato request) {
		
		return movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
	}
	
	@Override
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return super.ottieniBloccoNumero(parametriPaginazione) - 1;
	}
	

	@Override
	protected ListaPaginata<SubAccertamento> ottieniListaRisultati(RicercaAccertamentoPerChiaveOttimizzatoResponse response) {
		return toListaPaginata(response.getAccertamento().getElencoSubAccertamenti(), response.getNumeroPaginaSubRestituita(), response.getNumeroTotaleSub());
				
	}


}
