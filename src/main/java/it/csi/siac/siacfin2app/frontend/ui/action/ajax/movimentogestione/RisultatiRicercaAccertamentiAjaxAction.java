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

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.movimentogestione.RisultatiRicercaAccertamentoAjaxModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Action per i risultati di ricerca degli accertamenti.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 20/01/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaAccertamentiAjaxAction extends GenericRisultatiRicercaAjaxAction<Accertamento, 
	RisultatiRicercaAccertamentoAjaxModel, Accertamento, RicercaSinteticaAccertamentiSubAccertamenti, RicercaSinteticaAccertamentiSubAccertamentiResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 316991920640986367L;
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaAccertamentiAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaAccertamentiSubAccertamenti request) {
		return new ParametriPaginazione(request.getNumPagina(),request.getNumRisultatiPerPagina());
	}
	
	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaAccertamentiSubAccertamenti request, ParametriPaginazione parametriPaginazione) {
		request.setNumPagina(parametriPaginazione.getNumeroPagina()+1);
		request.setNumRisultatiPerPagina(parametriPaginazione.getElementiPerPagina());		
	}
	
	
	@Override
	protected Accertamento ottieniIstanza(Accertamento e) throws FrontEndBusinessException {
		return e;
	}

	@Override
	protected RicercaSinteticaAccertamentiSubAccertamentiResponse ottieniResponse(RicercaSinteticaAccertamentiSubAccertamenti request) {
		return movimentoGestioneService.ricercaSinteticaAccertamentiSubAccertamenti(request);
	}

	@Override
	protected ListaPaginata<Accertamento> ottieniListaRisultati(RicercaSinteticaAccertamentiSubAccertamentiResponse response) {
		sessionHandler.setParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN, response.getNumPagina());
		sessionHandler.setParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN, response.getNumRisultati());
		
		return toListaPaginata(response.getListaAccertamenti(), response.getNumPagina(), response.getNumRisultati());
	}
	
	@Override
	protected ListaPaginata<Accertamento> ottieniListaRisultatiDallaSessione() {
		List<Accertamento> listaNonPaginata = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI_SUBACCERTAMENTI);
		// Parametri di cui necessito per la paginazione
		Integer numeroPagina = sessionHandler.getParametro(BilSessionParameter.NUMERO_PAGINA_SERVIZI_FIN);
		Integer numeroRisultati = sessionHandler.getParametro(BilSessionParameter.NUMERO_RISULTATI_SERVIZI_FIN);
		return toListaPaginata(listaNonPaginata, numeroPagina, numeroRisultati);
	}

	@Override
	protected boolean controllaDatiReperiti(ListaPaginata<Accertamento> lista) {
		return lista != null && !lista.isEmpty();
	}
	
	@Override
	protected int ottieniBloccoNumero(ParametriPaginazione parametriPaginazione) {
		return super.ottieniBloccoNumero(parametriPaginazione) - 1;
	}
	
}
