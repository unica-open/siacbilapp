/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.consultazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione.RisultatiRicercaVariazioniCodificheCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoDettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoDettaglioVariazioneCodificaCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitoloResponse;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 02/01/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaVariazioniCodificheCapitoloAjaxAction extends PagedDataTableAjaxAction<ElementoDettaglioVariazioneCodificaCapitolo, 
RisultatiRicercaVariazioniCodificheCapitoloAjaxModel, DettaglioVariazioneCodificaCapitolo, RicercaStoricoVariazioniCodificheCapitolo, RicercaStoricoVariazioniCodificheCapitoloResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
		
	@Autowired private transient CapitoloService capitoloService;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaVariazioniCodificheCapitoloAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_STORICO_VARIAZIONI_CODIFICHE_CAPITOLO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_STORICO_VARIAZIONI_CODIFICHE_CAPITOLO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaStoricoVariazioniCodificheCapitolo request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaStoricoVariazioniCodificheCapitolo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}


	@Override
	protected RicercaStoricoVariazioniCodificheCapitoloResponse getResponse(RicercaStoricoVariazioniCodificheCapitolo request) {
		return capitoloService.ricercaStoricoVariazioniCodificheCapitolo(request);
	}
	

	@Override
	protected ElementoDettaglioVariazioneCodificaCapitolo getInstance(DettaglioVariazioneCodificaCapitolo e)	throws FrontEndBusinessException {		
		log.debug("ottieniIstanza", "ottengo un'istanza della variazione da consultare");
		return ElementoDettaglioVariazioneCodificaCapitoloFactory.getInstance(e);
	}

	@Override
	protected ListaPaginata<DettaglioVariazioneCodificaCapitolo> ottieniListaRisultati(RicercaStoricoVariazioniCodificheCapitoloResponse response) {
		return response.getDatiStoricoVariazioniCodificheCapitolo();
	}		
}
