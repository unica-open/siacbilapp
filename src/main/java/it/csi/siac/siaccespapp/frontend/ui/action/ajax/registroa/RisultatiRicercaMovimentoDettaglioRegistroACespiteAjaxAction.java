/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.registroa;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa.RisultatiRicercaMovimentoDettaglioRegistroACespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoMovimentoDettaglioRegistroA;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoMovimentoDettaglioRegistroAFactory;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * Classe di Action per i risultati ricerca del registro A (prime note verso inventario contabile)del cespite, gestione AJAX.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaMovimentoDettaglioRegistroACespiteAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoMovimentoDettaglioRegistroA, RisultatiRicercaMovimentoDettaglioRegistroACespiteAjaxModel,
MovimentoDettaglio, RicercaSinteticaMovimentoDettaglioRegistroACespite, RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5022124617194481719L;

	@Autowired private transient PrimaNotaCespiteService primaNotaCespiteService;
	
	private static final String PULSANTE_CONSULTA =  "<button data-uid-movimento-det=\"%UID%\" type=\"button\" class=\"btn btn-secondary\"> cespiti</button>";
	private static final Pattern PATTERN = Pattern.compile("%UID%");

	/** Costruttore vuoto di default */
	public RisultatiRicercaMovimentoDettaglioRegistroACespiteAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_DETTAGLIO_REGISTRO_A_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_DETTAGLIO_REGISTRO_A_CESPITE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMovimentoDettaglioRegistroACespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMovimentoDettaglioRegistroACespite req,	ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoMovimentoDettaglioRegistroA ottieniIstanza(MovimentoDettaglio e) throws FrontEndBusinessException {
		return ElementoMovimentoDettaglioRegistroAFactory.getInstance(e);
	}

	@Override
	protected RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse ottieniResponse(RicercaSinteticaMovimentoDettaglioRegistroACespite req) {
		return primaNotaCespiteService.ricercaSinteticaMovimentoDettaglioRegistroACespite(req);
	}

	@Override
	protected ListaPaginata<MovimentoDettaglio> ottieniListaRisultati(RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse response) {
		return response.getMovimentiDettaglio();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	@Override
	protected void gestisciAzioniConsentite(ElementoMovimentoDettaglioRegistroA instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		if(Boolean.TRUE.equals(instance.getHasContoCespite())) {
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("%UID%", instance.getUid() + "");

			String azioni = StringUtilities.replacePlaceholders(PATTERN, PULSANTE_CONSULTA, map, false);
			instance.setAzioni(azioni);
		}
	}
	
}
