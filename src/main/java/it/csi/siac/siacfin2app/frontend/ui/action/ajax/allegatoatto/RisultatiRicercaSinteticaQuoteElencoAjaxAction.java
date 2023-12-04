/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

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
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.RisultatiRicercaSinteticaQuoteElencoAjaxModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;

/**
 * Action per i risultati di ricerca delle quote per elenco
 * 
 * @author Nazha AHMAD
 * @version 1.0.0 - 26/07/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaQuoteElencoAjaxAction extends PagedDataTableAjaxAction<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>, 
RisultatiRicercaSinteticaQuoteElencoAjaxModel, Subdocumento<?, ?>, RicercaSinteticaQuoteElenco, RicercaSinteticaQuoteElencoResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8919010984466087404L;

	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	private final BilSessionParameter parametroElencoDocumentiAllegato;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaSinteticaQuoteElencoAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO_FILTRATE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_DETTAGLIO_QUOTE_ELENCO_FILTRATE);
		this.parametroElencoDocumentiAllegato = BilSessionParameter.ELENCO_DOCUMENTI_ALLEGATO_LIGHT;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaQuoteElenco request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaQuoteElenco request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> getInstance(Subdocumento<?, ?> e) throws FrontEndBusinessException {
		ElencoDocumentiAllegato eda = sessionHandler.getParametro(parametroElencoDocumentiAllegato);
		List<DatiSoggettoAllegato> listaDatiSoggettoAllegato = sessionHandler.getParametro(BilSessionParameter.LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO);
		
		return ElementoElencoDocumentiAllegatoFactory.getInstance(e,listaDatiSoggettoAllegato,eda,model.isGestioneUEB());
	}
	
	@Override
	protected RicercaSinteticaQuoteElencoResponse getResponse(RicercaSinteticaQuoteElenco request) {
		return allegatoAttoService.ricercaSinteticaQuoteElenco(request);
	}

	@Override
	protected ListaPaginata<Subdocumento<?, ?>> ottieniListaRisultati(RicercaSinteticaQuoteElencoResponse response) {
		return response.getSubdocumenti();
	}

	@Override
	protected void eseguiOperazioniUlterioriSuResponse(RicercaSinteticaQuoteElencoResponse response) {
		sessionHandler.setParametro(parametroElencoDocumentiAllegato, response.getElencoDocumentiAllegato());
	}
}
