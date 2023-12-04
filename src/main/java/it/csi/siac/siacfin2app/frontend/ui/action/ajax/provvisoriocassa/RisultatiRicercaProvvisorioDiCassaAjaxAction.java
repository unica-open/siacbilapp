/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.provvisoriocassa;

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
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.provvisoriocassa.RisultatiRicercaProvvisorioDiCassaAjaxModel;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Action per i risultati di ricerca del ProvvisorioDiCassa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/09/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaProvvisorioDiCassaAjaxAction extends PagedDataTableAjaxAction<ProvvisorioDiCassa,
		RisultatiRicercaProvvisorioDiCassaAjaxModel, ProvvisorioDiCassa, RicercaProvvisoriDiCassa, RicercaProvvisoriDiCassaResponse> {
		
	/** Per la serializzazione */
	private static final long serialVersionUID = -8102911562332915919L;
	
	@Autowired private transient ProvvisorioService provvisorioService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaProvvisorioDiCassaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PROVVISORIO_DI_CASSA);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PROVVISORIO_DI_CASSA);
		numeroPaginaIniziale = 1;
	}
	
	@Override
	protected ListaPaginata<ProvvisorioDiCassa> ottieniListaRisultatiDallaSessione() {
		List<ProvvisorioDiCassa> listaNonPaginata = sessionHandler.getParametro(getParametroSessioneLista());
		// Parametri di cui necessito per la paginazione
		Integer numeroPagina = sessionHandler.getParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN);
		Integer numeroRisultati = sessionHandler.getParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN);
		
		return toListaPaginata(listaNonPaginata, numeroPagina, numeroRisultati);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaProvvisoriDiCassa request) {
		return new ParametriPaginazione(request.getNumPagina(), request.getNumRisultatiPerPagina());
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaProvvisoriDiCassa request, ParametriPaginazione parametriPaginazione) {
		request.setNumPagina(parametriPaginazione.getNumeroPagina());
		request.setNumRisultatiPerPagina(parametriPaginazione.getElementiPerPagina());
	}
	
	@Override
	protected ProvvisorioDiCassa getInstance(ProvvisorioDiCassa e) throws FrontEndBusinessException {
		return e;
	}
	
	@Override
	protected RicercaProvvisoriDiCassaResponse getResponse(RicercaProvvisoriDiCassa request) {
		return provvisorioService.ricercaProvvisoriDiCassa(request);
	}
	
	@Override
	protected boolean controllaDatiReperiti(ListaPaginata<ProvvisorioDiCassa> lista) {
		return lista != null && !lista.isEmpty();
	}
	
	@Override
	protected ListaPaginata<ProvvisorioDiCassa> ottieniListaRisultati(RicercaProvvisoriDiCassaResponse response) {
		// Rimetto in sessione i dati
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		
		return toListaPaginata(response.getElencoProvvisoriDiCassa(), response.getNumPagina(), response.getNumRisultati());
	}
	
	@Override
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return super.ottieniBloccoNumero(parametriPaginazione) - 1;
	}
	
}