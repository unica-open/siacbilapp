/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.movimentogestione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.movimentogestione.RisultatiRicercaImpegnoAjaxModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Action per i risultati di ricerca degli impegni.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 18/01/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaImpegniAjaxAction extends PagedDataTableAjaxAction<Impegno, 
	RisultatiRicercaImpegnoAjaxModel, Impegno, RicercaSinteticaImpegniSubImpegni, RicercaSinteticaImpegniSubimpegniResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 316991920640986367L;
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaImpegniAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_IMPEGNI_SUBIMPEGNI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_IMPEGNI_SUBIMPEGNI);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaImpegniSubImpegni request) {
		return new ParametriPaginazione(request.getNumPagina(),request.getNumRisultatiPerPagina());
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaImpegniSubImpegni request, ParametriPaginazione parametriPaginazione) {
		request.setNumPagina(parametriPaginazione.getNumeroPagina()+1);
		request.setNumRisultatiPerPagina(parametriPaginazione.getElementiPerPagina());		
	}
	
	
	@Override
	protected Impegno getInstance(Impegno e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaImpegniSubimpegniResponse getResponse(RicercaSinteticaImpegniSubImpegni request) {
		return movimentoGestioneService.ricercaSinteticaImpegniSubimpegni(request);
	}

	@Override
	protected ListaPaginata<Impegno> ottieniListaRisultati(RicercaSinteticaImpegniSubimpegniResponse response) {
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		
		return toListaPaginata(response.getListaImpegni(), response.getNumPagina(), response.getNumRisultati());
	}
	
	@Override
	protected ListaPaginata<Impegno> ottieniListaRisultatiDallaSessione() {
		List<Impegno> listaNonPaginata = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_IMPEGNI_SUBIMPEGNI);
		// Parametri di cui necessito per la paginazione
		Integer numeroPagina = sessionHandler.getParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN);
		Integer numeroRisultati = sessionHandler.getParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN);
		return toListaPaginata(listaNonPaginata, numeroPagina, numeroRisultati);
	}

	@Override
	protected boolean controllaDatiReperiti(ListaPaginata<Impegno> lista) {
		return lista != null && !lista.isEmpty();
	}
	
	@Override
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return super.ottieniBloccoNumero(parametriPaginazione) - 1;
	}
	
}
