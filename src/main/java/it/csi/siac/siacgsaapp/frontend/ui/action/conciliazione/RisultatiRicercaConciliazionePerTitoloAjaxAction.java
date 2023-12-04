/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conciliazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.PagedDataTableAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;
import it.csi.siac.siacgsaapp.frontend.ui.model.conciliazione.RisultatiRicercaConciliazionePerTitoloAjaxModel;
import it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione.ElementoConciliazionePerTitolo;

/**
 * Classe base di action per i risultati di ricerca della conciliazione per titolo, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/10/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaConciliazionePerTitoloAjaxAction extends PagedDataTableAjaxAction<ElementoConciliazionePerTitolo, RisultatiRicercaConciliazionePerTitoloAjaxModel,
		ConciliazionePerTitolo, RicercaSinteticaConciliazionePerTitolo, RicercaSinteticaConciliazionePerTitoloResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2085403225367653701L;
	
	@Autowired private transient ConciliazioneService conciliazioneService;

	// Azioni
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\">"
			+ "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>"
			+ "<ul class=\"dropdown-menu pull-right\">";
	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a href=\"#\" class=\"aggiornaConciliazione\">aggiorna</a></li>";
	private static final String AZIONI_CONSENTITE_ANNULLA = "<li><a href=\"#\" class=\"annullaConciliazione\">elimina</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul>"
			+ "</div>";
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaConciliazionePerTitoloAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_CONCILIAZIONI_PER_TITOLO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_CONCILIAZIONI_PER_TITOLO);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaConciliazionePerTitolo request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaConciliazionePerTitolo request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected RicercaSinteticaConciliazionePerTitoloResponse getResponse(RicercaSinteticaConciliazionePerTitolo request) {
		return conciliazioneService.ricercaSinteticaConciliazionePerTitolo(request);
	}

	@Override
	protected ListaPaginata<ConciliazionePerTitolo> ottieniListaRisultati(RicercaSinteticaConciliazionePerTitoloResponse response) {
		return response.getConciliazioni();
	}
	
	@Override
	protected void handleAzioniConsentite(ElementoConciliazionePerTitolo instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		String azioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN)
			.append(AZIONI_CONSENTITE_AGGIORNA)
			.append(AZIONI_CONSENTITE_ANNULLA)
			.append(AZIONI_CONSENTITE_END)
			.toString();
		instance.setAzioni(azioni);
	}

	@Override
	protected ElementoConciliazionePerTitolo getInstance(ConciliazionePerTitolo e) throws FrontEndBusinessException {
		return new ElementoConciliazionePerTitolo(e);
	}
	
}
